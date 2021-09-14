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
public class MessageDto implements Serializable {
    private String messageName;
    private String requestId;
    private String callbackUrl= "http://196.43.230.33:7080/tesws/messages/feedback";
    private Object payload;
}
