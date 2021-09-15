package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CommonOrdinalEntity;
import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.CommonOrdinalRepository;
import com.ManifestTeswTancis.Repository.ExportManifestRepository;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Request.CallInfDetailsRequestModel;
import com.ManifestTeswTancis.Response.CallInfRest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.CallInfService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("${spring.rabbitmq.exchange.out}")
	private String OUTBOUND_EXCHANGE;
    @Autowired
	MessageProducer rabbitMqMessageProducer;
	final ExportManifestRepository exportManifestRepository;
	private final ExImportManifestRepository exImportManifestRepository;
	private final ManifestStatusServiceImp statusServiceImp;
	@Autowired
	CommonOrdinalRepository commonOrdinalRepository;

	@Autowired
	public CallInfServiceImpl(ExImportManifestRepository exImportManifestRepository,
							  ManifestStatusServiceImp statusServiceImp, ExportManifestRepository exportManifestRepository) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.statusServiceImp = statusServiceImp;
		this.exportManifestRepository = exportManifestRepository;
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
			ExImportManifest storedCallInfDetails;
			try {
				ExImportManifest exImportManifest = new ExImportManifest(callInfDetails);
				ExportManifest exportManifest=new ExportManifest(callInfDetails);
				exImportManifest.setMrn(generateMrn(exImportManifest.getCarrierId()));
				exportManifest.setMrnOut(generatemrnOut(exportManifest.getCarrierId()));
				if(exportManifest.getModeOfTransport().contentEquals("1")){
					exportManifest.setModeOfTransport("10");
				}
				exportManifest.setBallast("N");
				exportManifestRepository.save(exportManifest);

				if (exImportManifest.getModeOfTransport().contentEquals("1")) {
						exImportManifest.setModeOfTransport("10");
				}
				 exImportManifest.setBallast("N");
				storedCallInfDetails = exImportManifestRepository.save(exImportManifest);
				statusServiceImp.save(exImportManifest, callInfDetails.getControlReferenceNumber(), true, exportManifest);

				if(storedCallInfDetails != null) {
					submitCallInfoNotice(storedCallInfDetails,exportManifest);
					//statusServiceImp.save(exImportManifest, callInfDetails.getControlReferenceNumber(), true);
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
	public String submitCallInfoNotice(ExImportManifest storedCallInfDetails, ExportManifest exportManifest) throws IOException {
		CallInfRest returnValue = new CallInfRest();
		returnValue.setCommunicationAgreedId(storedCallInfDetails.getCommunicationAgreedId());
		returnValue.setCustomOfficeCode(storedCallInfDetails.getCustomOfficeCode());
		returnValue.setMrnIn(storedCallInfDetails.getMrn());
		returnValue.setMrnOut(exportManifest.getMrnOut());
		returnValue.setMrnDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));

		ObjectMapper mapper = new ObjectMapper();
		String payload = mapper.writeValueAsString(returnValue);
		System.out.println("--------------- Custom Vessel Reference ---------------\n"+payload);
		MessageDto messageDto = new MessageDto();
		CallInfRestMessageDto callInfRestMessageDto = new CallInfRestMessageDto();
		callInfRestMessageDto.setMessageName(MessageNames.CUSTOMS_VESSEL_REFERENCE);
		RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
		callInfRestMessageDto.setRequestId(requestIdDto.getMessageId());
		messageDto.setPayload(returnValue);
		AcknowledgementDto queueResponse = rabbitMqMessageProducer.
				sendMessage(OUTBOUND_EXCHANGE, MessageNames.CUSTOMS_VESSEL_REFERENCE, callInfRestMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
		System.out.println(queueResponse);

		return "success";

	}
	private String getId() throws IOException {
		String url = "http://192.168.30.200:7074/GetId";
		HttpGet request = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();

		return EntityUtils.toString(entity);
	}

//	private String generateMrn(String carrierCode) {
//		Object nextval = exImportManifestRepository.getNextValue();
//		String suffix = String.format("%06d", Long.valueOf(nextval.toString()));
//		DateFormat df = new SimpleDateFormat("yy");
//		String prefix = df.format(Calendar.getInstance().getTime());
//		return prefix + carrierCode + suffix;
//	}
//
//     private String generatemrnOut(String carrierId) {
//		Object nextval = exImportManifestRepository.getNextValue();
//		String suffix = String.format("%06d", Long.valueOf(nextval.toString()));
//		DateFormat df = new SimpleDateFormat("yy");
//		String prefix = df.format(Calendar.getInstance().getTime());
//		return prefix + carrierId + suffix;
//	}

private String generateMrn(String carrierCode) {
	CommonOrdinalEntity commonOrdinalEntity;
	DateFormat df = new SimpleDateFormat("yy");
	String prefix= df.format(Calendar.getInstance().getTime()) +carrierCode;
	Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
	if (optionalCommonOrdinalEntity.isPresent()) {
		commonOrdinalEntity = optionalCommonOrdinalEntity.get();
		commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo() + 1);
	}else {
		commonOrdinalEntity = new CommonOrdinalEntity();
		commonOrdinalEntity.setPrefix(prefix);
		commonOrdinalEntity.setSequenceNo(1);
	}
	commonOrdinalRepository.save(commonOrdinalEntity);
	String suffix = String.format("%1$" + 6 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
	return prefix +suffix;
}
	private String generatemrnOut(String carrierCode) {
		new CommonOrdinalEntity();
		CommonOrdinalEntity commonOrdinalEntity;
		DateFormat df = new SimpleDateFormat("yy");
		String prefix= df.format(Calendar.getInstance().getTime()) +carrierCode;
		Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
		if (optionalCommonOrdinalEntity.isPresent()) {
			commonOrdinalEntity = optionalCommonOrdinalEntity.get();
			commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo() + 1);
		}else {
			commonOrdinalEntity = new CommonOrdinalEntity();
			commonOrdinalEntity.setPrefix(prefix);
			commonOrdinalEntity.setSequenceNo(1);
		}
		 commonOrdinalRepository.save(commonOrdinalEntity);
		String suffix = String.format("%1$" + 6 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
		return prefix + suffix;
	}
}
