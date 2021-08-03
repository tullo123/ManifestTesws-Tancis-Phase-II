package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import com.ManifestTeswTancis.Repository.CustomClearanceApprovalRepository;
import com.ManifestTeswTancis.Repository.CustomClearanceRepository;
import com.ManifestTeswTancis.Response.ResponseCustomClearance;
import com.ManifestTeswTancis.Util.ClearanceStatus;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpCall;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public class CheckCustomClearanceApprovalStatusImpl {
    @Value("http://196.192.79.121:8062/message-api/stakeholder")
    private String teswsFowardUrl;

    final CustomClearanceRepository customClearanceRepository;
    final CustomClearanceApprovalRepository customClearanceApprovalRepository;

    @Autowired
    public CheckCustomClearanceApprovalStatusImpl(CustomClearanceRepository customClearanceRepository, CustomClearanceApprovalRepository customClearanceApprovalRepository) {
        this.customClearanceRepository = customClearanceRepository;
        this.customClearanceApprovalRepository = customClearanceApprovalRepository;
    }
    @Transactional
    @Scheduled(fixedRate=240000)
    public void CheckCustomClearanceApprovalStatusServiceImpl(){
        List<CustomClearanceApprovalStatus> status=customClearanceApprovalRepository.findByApprovedStatusFalse();
        System.out.println("--------------- Checking for approved  Custom Clearance ---------------");
        for(CustomClearanceApprovalStatus ca: status){
            if(!ca.isApprovedStatus()){
                System.out.println("---------- Approving custom clearance with CallId"+ca.getCommunicationAgreedId()+"----------");
                CustomClearanceEntity cs=customClearanceRepository.findFirstByCommunicationAgreedId(ca.getCommunicationAgreedId());
                if (ClearanceStatus.APPROVED.equals(cs.getProcessingStatus())) {
                    ResponseCustomClearance responseCustomClearance = new ResponseCustomClearance();
                    responseCustomClearance.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    responseCustomClearance.setClearanceReference(cs.getTaxClearanceNumber());
                    responseCustomClearance.setApprovalStatus(getStatus(cs.getProcessingStatus()));
                    responseCustomClearance.setComment(cs.getComments());
                    responseCustomClearance.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    ca.setApprovedStatus(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendApprovalNoticeToTesws(responseCustomClearance);
                    System.out.println("--------------- Approval Notice Response --------------\n" + response);
                }
            }
        }

    }


        private String getStatus(String processingStatus) {
            if(processingStatus.contentEquals("D")){
                return "A";
            }else if (processingStatus.contentEquals("R")){
                return "REJECTED";
            } else if (processingStatus.contentEquals("B")){
                return "RECEIVED";
            }
            else return processingStatus;
        }



    private String sendApprovalNoticeToTesws(ResponseCustomClearance responseCustomClearance) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(responseCustomClearance);
            System.out.println("--------------- Custom Clearance Approval Notice Payload ---------------\n"+payload);
            HttpMessage httpMessage = new HttpMessage();
            httpMessage.setContentType("application/json");
            httpMessage.setPayload(payload);
            httpMessage.setMessageName("CUSTOM_CLEARANCE_NOTICE");
            httpMessage.setRecipient("SS");
            HttpCall httpCall = new HttpCall();
            return httpCall.httpRequest(httpMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Failed";
        }

}