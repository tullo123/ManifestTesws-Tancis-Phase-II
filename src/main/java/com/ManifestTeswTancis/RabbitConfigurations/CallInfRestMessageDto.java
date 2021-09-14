package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.Response.CallInfRest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CallInfRestMessageDto {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private CallInfRest payload;
}
