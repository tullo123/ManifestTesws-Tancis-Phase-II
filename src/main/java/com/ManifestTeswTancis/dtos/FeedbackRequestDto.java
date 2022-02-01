package com.ManifestTeswTancis.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class FeedbackRequestDto implements Serializable {
    @NotNull(message="preparationDateTime should not be null")
    @NotEmpty(message = "preparationDateTime should not be empty")
    private String preparationDateTime;
    @NotNull(message = "controlReferenceNumber should not be null")
    @NotEmpty(message = "controlReferenceNumber should not be empty")
    private String controlReferenceNumber;
    @NotNull(message = "messageReferenceNumber should not be null")
    @NotEmpty(message = "messageReferenceNumber should not be empty")
    private String messageReferenceNumber;
    @NotNull(message = "messageTypeId should not be null")
    @NotEmpty(message = "messageTypeId should not be empty")
    private String messageTypeId;
    @NotNull(message = "messageFunction should not be null")
    @NotEmpty(message = "messageFunction should not be empty")
    private String messageFunction;
    @NotNull(message = "receiverMessageReferenceNumber should not be null")
    @NotEmpty(message = "receiverMessageReferenceNumber should not be empty")
    private String receiverMessageReferenceNumber;
    @NotNull(message = "error should not be null")
    private boolean error;
    @NotNull(message = "responses should not be null")
    @NotEmpty(message = "responses should not be empty")
    private List<ErrorDetailDto> responses;
}
