package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Response.SubmittedManifestStatus;
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
import com.ManifestTeswTancis.Util.HttpCall;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.ManifestTeswTancis.Util.ManifestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CheckManifestStatusImpl {
	@Value("${tesws-fowardurl}")
	private String teswsFowardUrl;

	private final ExImportManifestRepository exImportManifestRepository;
	private final ExImportMasterBlRepository exImportMasterBlRepository;
	private final ExImportHouseBlRepository exImportHouseBlRepository;
	private final ManifestApprovalStatusRepository statusRepository;

	@Autowired
	public CheckManifestStatusImpl(ExImportManifestRepository exImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository,
								   ExImportHouseBlRepository exImportHouseBlRepository, ManifestApprovalStatusRepository statusRepository) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.exImportMasterBlRepository = exImportMasterBlRepository;
		this.exImportHouseBlRepository = exImportHouseBlRepository;
		this.statusRepository = statusRepository;
	}

	@Transactional
	@Scheduled(fixedRate = 120000)
	public void checkManifestStatus() {
		List<ManifestApprovalStatus> manifestStatusEntities = statusRepository.findByApprovedStatusFalse();
			System.err.println("*********************** Checking for any approved manifest ******************************");
		for (ManifestApprovalStatus mf : manifestStatusEntities) {
			if (!mf.isApprovedStatus()) {
				System.err.println("*********************** Approving Manifest with Voyage No. " + mf.getVoyageNumber()+ "************************");
				ExImportManifest callInf = exImportManifestRepository.findByMrn(mf.getMrn());
				switch (callInf.getProcessingStatus()) {
					case ManifestStatus.APPROVED: {
						ManifestNotice manifestNotice = new ManifestNotice();
						manifestNotice.setCall_id(callInf.getCommunicationAgreedId());
						manifestNotice.setMessageReferenceNumber(callInf.getControlReferenceNumber());
						manifestNotice.setApprovalDt(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
						manifestNotice.setMrn(mf.getMrn());
						if (mf.getMrn()!=null && mf.getMrnOut()!=null){
							mf.setMrnStatus(true); }
						manifestNotice.setApprovalStatus(getStatus(callInf.getProcessingStatus()));
						manifestNotice.setBls(getManifestNoticeBl(mf.getMrn()));
						mf.setApprovedStatus(true);
						mf.setApprovedNoticeStatus(true);
						mf.setProcessingStatus(getStatus(callInf.getProcessingStatus()));
						mf.setApprovalDt(manifestNotice.getApprovalDt());
						statusRepository.save(mf);
						String response = sendApprovedToTesws(manifestNotice);
						System.out.println("***************** Approval Notice Response ******************\n" + response);

						break;
					}
					case ManifestStatus.RECEIVED:
					case ManifestStatus.REJECTED: {
						SubmittedManifestStatus submittedManifestStatus = new SubmittedManifestStatus();
						submittedManifestStatus.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
						submittedManifestStatus.setCommunicationAgreedId(callInf.getCommunicationAgreedId());
						submittedManifestStatus.setControlReferenceNumber(callInf.getControlReferenceNumber());
						submittedManifestStatus.setApplicationReference(callInf.getApplicationReferenceNumber());
						submittedManifestStatus.setVoyageNumber(callInf.getVoyageNumber());
						submittedManifestStatus.setCustomOfficeCode(callInf.getCustomOfficeCode());
						submittedManifestStatus.setStatus(getStatus(callInf.getProcessingStatus()));
						mf.setProcessingStatus(getStatus(callInf.getProcessingStatus()));
						statusRepository.save(mf);
						String response = submittedManifestStatusToTesws(submittedManifestStatus);
						System.out.println("***************** Approval Notice Response ******************\n" + response);

						break;
					}
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
	
	private String sendApprovedToTesws(ManifestNotice manifestNotice) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String payload = mapper.writeValueAsString(manifestNotice);
			System.out.println("------ Approval notice payload ------------\n"+payload);
			HttpMessage httpMessage = new HttpMessage();
			httpMessage.setContentType("application/json");
			httpMessage.setPayload(payload);
			httpMessage.setMessageName("MANIFEST_APPROVAL_NOTICE");
			httpMessage.setRecipient("SS");
			HttpCall httpCall = new HttpCall();
		    return httpCall.httpRequest(httpMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "failed";
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
	private String submittedManifestStatusToTesws(SubmittedManifestStatus submittedManifestStatus) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String payload = mapper.writeValueAsString(submittedManifestStatus);
			System.out.println("------ Manifest Status notice payload ------------\n"+payload);
			HttpMessage httpMessage = new HttpMessage();
			httpMessage.setContentType("application/json");
			httpMessage.setPayload(payload);
			httpMessage.setMessageName("SUBMITTED_MANIFEST_STATUS");
			httpMessage.setRecipient("SS");
			HttpCall httpCall = new HttpCall();
			return httpCall.httpRequest(httpMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "failed";
	}

}
