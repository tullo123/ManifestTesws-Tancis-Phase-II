package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CommonOrdinalEntity;
import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.CommonOrdinalRepository;
import com.ManifestTeswTancis.Repository.ExportManifestRepository;
import com.ManifestTeswTancis.Repository.QueueMessageStatusRepository;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Request.PortCallIdRequestModel;
import com.ManifestTeswTancis.Response.PortCallIdResponse;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.PortCallIdService;
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
public  class PortCallIdServiceImpl implements PortCallIdService {
	@Value("${spring.rabbitmq.exchange.out}")
	private String OUTBOUND_EXCHANGE;
	@Value("${url}")
	private String url;
    final MessageProducer rabbitMqMessageProducer;
	final ExportManifestRepository exportManifestRepository;
	private final ExImportManifestRepository exImportManifestRepository;
	private final ManifestStatusServiceImp statusServiceImp;
	final CommonOrdinalRepository commonOrdinalRepository;
	final QueueMessageStatusRepository queueMessageStatusRepository;

	@Autowired
	public PortCallIdServiceImpl(ExImportManifestRepository exImportManifestRepository,
								 ManifestStatusServiceImp statusServiceImp, ExportManifestRepository exportManifestRepository, MessageProducer rabbitMqMessageProducer, CommonOrdinalRepository commonOrdinalRepository, QueueMessageStatusRepository queueMessageStatusRepository) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.statusServiceImp = statusServiceImp;
		this.exportManifestRepository = exportManifestRepository;
		this.rabbitMqMessageProducer = rabbitMqMessageProducer;
		this.commonOrdinalRepository = commonOrdinalRepository;
		this.queueMessageStatusRepository = queueMessageStatusRepository;
	}

	@Override
	@Transactional
	public TeswsResponse createCallInfo(PortCallIdRequestModel callInfDetails) {
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
				exportManifest.setMrnOut(generateMrnOut(exportManifest.getCarrierId()));
				if(exportManifest.getModeOfTransport().contentEquals("1")){ exportManifest.setModeOfTransport("10"); }
				exportManifestRepository.save(exportManifest);
				if (exImportManifest.getModeOfTransport().contentEquals("1")) { exImportManifest.setModeOfTransport("10"); }
				storedCallInfDetails = exImportManifestRepository.save(exImportManifest);
				statusServiceImp.save(exImportManifest, callInfDetails.getControlReferenceNumber(), true, exportManifest);
				submitCallInfoNotice(storedCallInfDetails,exportManifest);
			} catch (Exception e) {
				response.setDescription(e.getMessage());
				e.printStackTrace(); }
			    return response;
		    }
		   response.setDescription("Vessel call with callId " + callInfDetails.getCommunicationAgreedId()
				+ " is already approved and sent to Tesws");
		        response.setCode(405);
		return response;
	}


	@Override
	public String submitCallInfoNotice(ExImportManifest storedCallInfDetails, ExportManifest exportManifest) throws IOException {
		PortCallIdResponse returnValue = new PortCallIdResponse();
		returnValue.setCommunicationAgreedId(storedCallInfDetails.getCommunicationAgreedId());
		returnValue.setCustomOfficeCode(storedCallInfDetails.getCustomOfficeCode());
		returnValue.setMrnIn(storedCallInfDetails.getMrn());
		returnValue.setMrnOut(exportManifest.getMrnOut());
		returnValue.setMrnDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));

		ObjectMapper mapper = new ObjectMapper();
		String payload = mapper.writeValueAsString(returnValue);
		System.out.println("---- Custom Vessel Reference----\n"+payload);
		MessageDto messageDto = new MessageDto();
		PortCallIdResponseMessageDto portCallIdResponseMessageDto = new PortCallIdResponseMessageDto();
		portCallIdResponseMessageDto.setMessageName(MessageNames.CUSTOMS_VESSEL_REFERENCE);
		RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
		portCallIdResponseMessageDto.setRequestId(requestIdDto.getMessageId());
		messageDto.setPayload(returnValue);
		AcknowledgementDto queueResponse = rabbitMqMessageProducer.
				sendMessage(OUTBOUND_EXCHANGE, MessageNames.CUSTOMS_VESSEL_REFERENCE, portCallIdResponseMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
		System.out.println(queueResponse);
		QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
		queueMessage.setMessageId(returnValue.getCommunicationAgreedId());
		queueMessage.setReferenceId(portCallIdResponseMessageDto.getRequestId());
		queueMessage.setMessageName(MessageNames.CUSTOMS_VESSEL_REFERENCE);
		queueMessage.setProcessStatus("1");
		queueMessage.setProcessId("TANCIS-TESWS.API");
		queueMessage.setFirstRegistrationId("TANCIS-TESWS.API");
		queueMessage.setLastUpdateId("TANCIS-TESWS.API");
		queueMessage.setProcessingDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		queueMessage.setFirstRegisterDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		queueMessage.setLastUpdateDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		queueMessageStatusRepository.save(queueMessage);

		return "success";
	}
	private String getId() throws IOException {
		HttpGet request = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity);
	}


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
	private String generateMrnOut(String carrierCode) {
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
