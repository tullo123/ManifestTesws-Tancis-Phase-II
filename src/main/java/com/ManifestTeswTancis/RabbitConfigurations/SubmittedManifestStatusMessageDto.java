package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.Response.SubmittedManifestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubmittedManifestStatusMessageDto {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private SubmittedManifestStatus payload;
}
