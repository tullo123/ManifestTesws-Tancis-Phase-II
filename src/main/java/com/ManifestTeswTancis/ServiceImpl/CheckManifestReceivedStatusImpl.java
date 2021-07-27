package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import com.ManifestTeswTancis.Response.SubmittedManifestStatus;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpCall;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.ManifestTeswTancis.Util.ManifestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public class CheckManifestReceivedStatusImpl {
    @Value("${tesws-fowardurl}")
    private String teswsFowardUrl;
    final
    ManifestApprovalStatusRepository statusRepository;
    final
    ExImportManifestRepository exImportManifestRepository;

    public CheckManifestReceivedStatusImpl(ManifestApprovalStatusRepository statusRepository, ExImportManifestRepository exImportManifestRepository) {
        this.statusRepository = statusRepository;
        this.exImportManifestRepository = exImportManifestRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 120000)
    public void checkReceivedManifestStatus() {
        List<ManifestApprovalStatus> manifestStatusEntities = statusRepository.findByApprovedStatusFalse();
        for (ManifestApprovalStatus mf : manifestStatusEntities) {
            if (!mf.isApprovedStatus()) {
                ExImportManifest callInf = exImportManifestRepository.findByMrn(mf.getMrn());
                if (ManifestStatus.RECEIVED.equals(callInf.getProcessingStatus()) || ManifestStatus.REJECTED.equals(callInf.getProcessingStatus())) {
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
                    System.out.println("--------------- Approval Notice Response --------------\n" + response);
                }
            }
        }
    }

    private String submittedManifestStatusToTesws(SubmittedManifestStatus submittedManifestStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(submittedManifestStatus);
            System.out.println("----------- Manifest Status notice payload ------------\n"+payload);
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


    private String getStatus(String processingStatus) {
        if (processingStatus.contentEquals("E")) {
            return "A";
        } else if (processingStatus.contentEquals("F")) {
            return "REJECT";

        } else if (processingStatus.contentEquals("B")){
            return "RECEIVED";
        }
        else {
            return processingStatus;
        }
    }
}