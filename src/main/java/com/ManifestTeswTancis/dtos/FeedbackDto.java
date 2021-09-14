package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedbackDto implements Serializable {
    private String preparationDateTime;
    private String controlReferenceNumber;
    private String messageReferenceNumber;
    private String messageTypeId;
    private String messageFunction;
    private String receiverMessageReferenceNumber;
    private boolean error;
    private List<ErrorDetailDto> responses;
}
