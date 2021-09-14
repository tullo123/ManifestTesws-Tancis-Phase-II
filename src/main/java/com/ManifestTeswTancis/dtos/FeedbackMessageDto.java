package com.ManifestTeswTancis.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FeedbackMessageDto implements Serializable {
    private HttpStatus httpStatus;
    private FeedbackDto feedbackDto;
}
