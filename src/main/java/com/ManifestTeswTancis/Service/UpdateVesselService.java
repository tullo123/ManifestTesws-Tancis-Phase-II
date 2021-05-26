package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.UpdateVesselDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UpdateVesselService {
     TeswsResponse updateCallInf(UpdateVesselDto updateVesselDto);
}
