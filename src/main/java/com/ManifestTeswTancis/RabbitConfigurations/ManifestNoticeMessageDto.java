package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.dtos.ManifestNotice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestNoticeMessageDto {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private ManifestNotice payload;
}
