package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.FreePratiqueReport;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface FreePartiqueService {
    TeswsResponse freePartique(FreePratiqueReport freePratiqueReport);
}
