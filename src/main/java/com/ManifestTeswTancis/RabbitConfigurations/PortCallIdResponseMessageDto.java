package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.Response.PortCallIdResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PortCallIdResponseMessageDto {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private PortCallIdResponse payload;
}
