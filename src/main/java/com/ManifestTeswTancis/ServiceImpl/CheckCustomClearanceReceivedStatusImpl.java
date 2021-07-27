package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import com.ManifestTeswTancis.Repository.CustomClearanceApprovalRepository;
import com.ManifestTeswTancis.Repository.CustomClearanceRepository;
import com.ManifestTeswTancis.Response.CustomClearanceStatus;
import com.ManifestTeswTancis.Util.ClearanceStatus;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpCall;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public class CheckCustomClearanceReceivedStatusImpl {
    @Value("http://196.192.79.121:8062/message-api/stakeholder")
    private String teswsFowardUrl;

    final
    CustomClearanceRepository customClearanceRepository;
    final
    CustomClearanceApprovalRepository customClearanceApprovalRepository;

    public CheckCustomClearanceReceivedStatusImpl(CustomClearanceRepository customClearanceRepository, CustomClearanceApprovalRepository customClearanceApprovalRepository) {
        this.customClearanceRepository = customClearanceRepository;
        this.customClearanceApprovalRepository = customClearanceApprovalRepository;
    }

    @Transactional
    @Scheduled(fixedRate=180000)
    public void CheckCustomClearanceReceiveStatusImpl() {
        List<CustomClearanceApprovalStatus> status = customClearanceApprovalRepository.findByReceivedNoticeSentFalse();
        for (CustomClearanceApprovalStatus ca : status) {
            if (!ca.isApprovedStatus()) {
                CustomClearanceEntity cs = customClearanceRepository.findFirstByCommunicationAgreedId(ca.getCommunicationAgreedId());
                if (ClearanceStatus.RECEIVED.equals(cs.getProcessingStatus())|| ClearanceStatus.REJECTED.equals(cs.getProcessingStatus())) {
                    CustomClearanceStatus customClearanceStatus = new CustomClearanceStatus();
                    customClearanceStatus.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    customClearanceStatus.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    customClearanceStatus.setStatus(getStatus(cs.getProcessingStatus()));
                    ca.setReceivedFailedStatus(getStatus(cs.getProcessingStatus()));
                    ca.setNoticeDate(customClearanceStatus.getNoticeDate());
                    ca.setReceivedNoticeSent(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendStatusNoticeToTesws(customClearanceStatus);
                    System.out.println("--------------- Status Notice Response ---------------\n" + response);
                }
            }

        }
    }

    private String sendStatusNoticeToTesws(CustomClearanceStatus customClearanceStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(customClearanceStatus);
            System.out.println("--------------- Custom Clearance Status Notice Payload ---------------\n"+payload);
            HttpMessage httpMessage = new HttpMessage();
            httpMessage.setContentType("application/json");
            httpMessage.setPayload(payload);
            httpMessage.setMessageName("CUSTOM_CLEARANCE_STATUS");
            httpMessage.setRecipient("SS");
            HttpCall httpCall = new HttpCall();
            return httpCall.httpRequest(httpMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed";
    }

    public String getStatus(String processingStatus) {
        if(processingStatus.contentEquals("D")){
            return "A";
        }else if (processingStatus.contentEquals("R")){
            return "REJECTED";
        } else if (processingStatus.contentEquals("B")){
            return "RECEIVED";
        }
        else return processingStatus;
    }
}