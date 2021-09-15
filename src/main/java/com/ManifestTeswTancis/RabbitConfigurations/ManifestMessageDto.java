package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.dtos.ManifestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestMessageDto  implements Serializable {
    private String messageName;
    private String requestId;
    private String callbackUrl;
    private ManifestDto payload;
}
