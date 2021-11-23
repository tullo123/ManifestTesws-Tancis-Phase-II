package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.FreePratiqueReportDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface FreePartiqueService {
    TeswsResponse freePartique(FreePratiqueReportDto freePratiqueReportDto);
}
