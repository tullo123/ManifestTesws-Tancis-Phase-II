package com.ManifestTeswTancis.RabbitConfigurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestIdDto implements Serializable {
    private String messageId;
}
