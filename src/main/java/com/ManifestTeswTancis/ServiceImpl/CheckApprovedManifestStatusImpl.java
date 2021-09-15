package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.*;
import com.ManifestTeswTancis.dtos.ManifestNoticeBl;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Entity.ExImportHouseBl;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Entity.ExImportMasterBl;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Repository.ExImportHouseBlRepository;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import com.ManifestTeswTancis.Repository.ExImportMasterBlRepository;
import com.ManifestTeswTancis.Util.ManifestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Component
@Service
public class CheckApprovedManifestStatusImpl {
	@Value("${spring.rabbitmq.exchange.out}")
	private String OUTBOUND_EXCHANGE;
	final MessageProducer rabbitMqMessageProducer;
	private final ExImportManifestRepository exImportManifestRepository;
	private final ExImportMasterBlRepository exImportMasterBlRepository;
	private final ExImportHouseBlRepository exImportHouseBlRepository;
	private final ManifestApprovalStatusRepository statusRepository;

	@Autowired
	public CheckApprovedManifestStatusImpl(ExImportManifestRepository exImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository,
										   ExImportHouseBlRepository exImportHouseBlRepository, ManifestApprovalStatusRepository statusRepository, MessageProducer rabbitMqMessageProducer) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.exImportMasterBlRepository = exImportMasterBlRepository;
		this.exImportHouseBlRepository = exImportHouseBlRepository;
		this.statusRepository = statusRepository;
		this.rabbitMqMessageProducer = rabbitMqMessageProducer;
	}

	@Transactional
	@Scheduled(fixedRate = 150000)
	public void checkManifestStatus() {
		List<ManifestApprovalStatus> manifestStatusEntities = statusRepository.findByApprovedStatusFalse();
			System.err.println("--------------- Checking for any approved manifest ---------------");
		for (ManifestApprovalStatus mf : manifestStatusEntities) {
			if (!mf.isApprovedStatus()) {
				System.err.println("--------------- Approving Manifest with Voyage No. " + mf.getVoyageNumber()+ "---------------");
				ExImportManifest callInf = exImportManifestRepository.findByMrn(mf.getMrn());
				if (ManifestStatus.APPROVED.equals(callInf.getProcessingStatus())) {
					ManifestNotice manifestNotice = new ManifestNotice();
					manifestNotice.setCommunicationAgreedId(callInf.getCommunicationAgreedId());
					manifestNotice.setMessageReferenceNumber(callInf.getControlReferenceNumber());
					manifestNotice.setApprovalDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
					manifestNotice.setMrn(mf.getMrn());
					if (mf.getMrn() != null && mf.getMrnOut() != null) {
						mf.setMrnStatus(true);
					}
					manifestNotice.setApprovalStatus(getStatus(callInf.getProcessingStatus()));
					manifestNotice.setBls(getManifestNoticeBl(mf.getMrn()));
					mf.setApprovedStatus(true);
					mf.setApprovedNoticeStatus(true);
					mf.setProcessingStatus(getStatus(callInf.getProcessingStatus()));
					mf.setApprovalDt(manifestNotice.getApprovalDate());
					statusRepository.save(mf);
					String response = sendApprovedNoticeToQueue(manifestNotice);
					System.out.println("--------------- Approval Notice Response ---------------\n" + response);
				}
			}
		}
		

	}

	private List<ManifestNoticeBl> getManifestNoticeBl(String mrn){
		List<ManifestNoticeBl> manifestNotices  = new ArrayList<>();
		List<ExImportMasterBl> masterBlEntities = exImportMasterBlRepository.findByMrn(mrn);
		List<ExImportHouseBl> houseBls = exImportHouseBlRepository.findByMrn(mrn);
		for(ExImportMasterBl exImportMasterBl : masterBlEntities ) {
			ManifestNoticeBl manifestNoticeBl = new ManifestNoticeBl();
			manifestNoticeBl.setMsn(exImportMasterBl.getMsn());
			manifestNoticeBl.setCrn(mrn+ exImportMasterBl.getMsn());
			manifestNoticeBl.setMasterBillOfLading(exImportMasterBl.getMasterBillOfLading());
			manifestNoticeBl.setTasacControlNumber(exImportMasterBl.getTasacControlNumber());
			manifestNoticeBl.setHsn(null);
			manifestNoticeBl.setHouseBillOfLading(null);
			manifestNotices.add(manifestNoticeBl);
		}
		if(!houseBls.isEmpty()) {
			for(ExImportHouseBl exImportHouseBl : houseBls) {
			ManifestNoticeBl manifestNoticeBl = new ManifestNoticeBl();
			manifestNoticeBl.setMsn(exImportHouseBl.getMsn());
			manifestNoticeBl.setCrn(mrn+ exImportHouseBl.getMsn()+ exImportHouseBl.getHsn());
			manifestNoticeBl.setMasterBillOfLading(exImportHouseBl.getMasterBillOfLading());
			manifestNoticeBl.setTasacControlNumber(exImportHouseBl.getTasacControlNumber());
			manifestNoticeBl.setHsn(exImportHouseBl.getHsn());
			manifestNoticeBl.setHouseBillOfLading(exImportHouseBl.getHouseBillOfLading());
			manifestNotices.add(manifestNoticeBl);
			}
		}
		return manifestNotices;
	}
	
	private String sendApprovedNoticeToQueue(ManifestNotice manifestNotice) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String payload = mapper.writeValueAsString(manifestNotice);
			System.out.println("----------- Approval notice payload ------------\n"+payload);
			MessageDto messageDto = new MessageDto();
			ManifestNoticeMessageDto manifestNoticeMessageDto = new ManifestNoticeMessageDto();
			manifestNoticeMessageDto.setMessageName(MessageNames.MANIFEST_APPROVAL_NOTICE);
			RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
			manifestNoticeMessageDto.setRequestId(requestIdDto.getMessageId());
			messageDto.setPayload(manifestNotice);
			AcknowledgementDto queueResponse = rabbitMqMessageProducer.
					sendMessage(OUTBOUND_EXCHANGE, MessageNames.MANIFEST_APPROVAL_NOTICE, manifestNoticeMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
			System.out.println(queueResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}

	private String getStatus(String s) {
		if (s.contentEquals("E")) {
			return "A";
		} else if (s.contentEquals("F")) {
			return "REJECT";

		} else if (s.contentEquals("B")){
			return "RECEIVED";
		}
		else {
			return s;
		}
		
	}
	private String getId() throws IOException {
		String url = "http://192.168.30.200:7074/GetId";
		HttpGet request = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();

		return EntityUtils.toString(entity);
	}
}
