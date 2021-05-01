package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.CallInfCancelDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface DeleteVesselService {
    TeswsResponse deleteVesselInfo(CallInfCancelDto callInfCancelDto);
}
