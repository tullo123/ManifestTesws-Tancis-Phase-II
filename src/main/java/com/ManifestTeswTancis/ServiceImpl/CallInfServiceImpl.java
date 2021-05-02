package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Request.CallInfDetailsRequestModel;
import com.ManifestTeswTancis.Response.CallInfRest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.CallInfService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpCall;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;

@Service
public  class CallInfServiceImpl implements CallInfService {
	private final ExImportManifestRepository exImportManifestRepository;
	private final ManifestStatusServiceImp statusServiceImp;

	@Autowired
	public CallInfServiceImpl(ExImportManifestRepository exImportManifestRepository,
							  ManifestStatusServiceImp statusServiceImp) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.statusServiceImp = statusServiceImp;
	}

	@Override
	@Transactional
	public TeswsResponse createCallInfo(CallInfDetailsRequestModel callInfDetails) {
		TeswsResponse response = new TeswsResponse();
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setRefId(callInfDetails.getControlReferenceNumber());
		response.setAckType("CUSTOMS_VESSEL_REFERENCE");

		Optional<ExImportManifest> optional = exImportManifestRepository
				.findFirstByCommunicationAgreedId(callInfDetails.getCommunicationAgreedId());
		if(!optional.isPresent()) {
			ExImportManifest storedCallInfDetails =null;
			try {
				ExImportManifest exImportManifest = new ExImportManifest(callInfDetails);
				exImportManifest.setMrn(generateMrn(exImportManifest.getCarrierId()));
				if (exImportManifest.getModeOfTransport().contentEquals("1")) {
						exImportManifest.setModeOfTransport("10");
				}

				storedCallInfDetails = exImportManifestRepository.save(exImportManifest);
				//statusServiceImp.save(exImportManifest, callInfDetails.getControlReferenceNumber(), true);

				if(storedCallInfDetails != null) {
					submitCallInfoNotice(storedCallInfDetails);
					statusServiceImp.save(exImportManifest, callInfDetails.getControlReferenceNumber(), true);
				}

			} catch (Exception e) {
				response.setDescription(e.getMessage());
				e.printStackTrace();
			}
			return response;
		}
		response.setDescription("Vessel call with callId " + callInfDetails.getCommunicationAgreedId()
				+ " is already approved and sent to Tesws");
		response.setCode(405);
		return response;

	}

	@Override
	public String submitCallInfoNotice(ExImportManifest storedCallInfDetails) throws IOException {
		CallInfRest returnValue = new CallInfRest();
		returnValue.setCommunicationAgreedId(storedCallInfDetails.getCommunicationAgreedId());
		returnValue.setCustomOfficeCode(storedCallInfDetails.getCustomOfficeCode());
		returnValue.setMrn(storedCallInfDetails.getMrn());
		returnValue.setMrnDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		ObjectMapper mapper = new ObjectMapper();
		String payload = mapper.writeValueAsString(returnValue);
		System.out.println(payload);

		HttpMessage httpMessage = new HttpMessage();
		httpMessage.setContentType("application/json");
		httpMessage.setMessageName("CUSTOMS_VESSEL_REFERENCE");
		httpMessage.setPayload(payload);
		httpMessage.setRecipient("SS");
		HttpCall httpCall = new HttpCall();
		return httpCall.httpRequest(httpMessage);

	}

	private String generateMrn(String carrierCode) {
		Object nextval = exImportManifestRepository.getNextValue();
		String suffix = String.format("%06d", Long.valueOf(nextval.toString()));
		DateFormat df = new SimpleDateFormat("yy");
		String prefix = df.format(Calendar.getInstance().getTime());
		return prefix + carrierCode + suffix;
	}
}
