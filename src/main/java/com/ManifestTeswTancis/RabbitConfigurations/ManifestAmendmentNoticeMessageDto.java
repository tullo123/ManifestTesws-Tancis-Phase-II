package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.Response.ManifestAmendmentApprovalStatusResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ManifestAmendmentNoticeMessageDto implements Serializable {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private ManifestAmendmentApprovalStatusResponse payload;
}
