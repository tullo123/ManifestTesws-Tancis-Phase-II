package com.ManifestTeswTancis.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_IF_TESWS_MSG_STATUS")
public class QueueMessageStatusEntity {
    @Id
   @Column(name="MESSAGE_ID")
   private String messageId;

   @Column(name="REFERENCE_ID")
   private String referenceId;

   @Column(name="MESSAGE_NAME")
   private String messageName;

   @Column(name="PROCESS_STATUS")
   private String processStatus;

   @Column(name="PROCESSING_ID")
   private String processId;

   @Column(name="PROCESSING_DT")
   private String processingDate;

   @Column(name="FIRST_REGISTER_ID")
   private String firstRegistrationId;

   @Column(name="LAST_UPDATE_ID")
   private String lastUpdateId;

   @Column(name ="FIRST_REGISTER_DT" )
   private String firstRegisterDate;

   @Column(name ="LAST_UPDATE_DT" )
   private String lastUpdateDate;
}
