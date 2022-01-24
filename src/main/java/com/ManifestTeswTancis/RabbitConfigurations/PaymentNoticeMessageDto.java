package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.Response.ManifestAmendmentPaymentNotice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentNoticeMessageDto implements Serializable {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private ManifestAmendmentPaymentNotice payload;
}
