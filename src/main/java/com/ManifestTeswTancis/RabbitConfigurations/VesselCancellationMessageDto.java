package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.dtos.CallInfCancelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VesselCancellationMessageDto implements Serializable {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private CallInfCancelDto payload;
}
