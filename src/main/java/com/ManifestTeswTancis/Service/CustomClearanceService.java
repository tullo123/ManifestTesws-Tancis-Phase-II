package com.ManifestTeswTancis.Service;
import com.ManifestTeswTancis.dtos.CustomClearanceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomClearanceService {
    TeswsResponse customService(CustomClearanceDto customClearanceDto);
}
