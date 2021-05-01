package com.ManifestTeswTancis.Service;
import com.ManifestTeswTancis.dtos.CustomClearanceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CustomClearanceService {
    TeswsResponse customService(CustomClearanceDto customClearanceDto);

    String submitCustomClearanceNotice(CustomClearanceDto customClearanceDto) throws IOException;
}
