package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManifestStatusServiceImp {
	private final ManifestApprovalStatusRepository statusRepository;

	@Autowired
	public ManifestStatusServiceImp(ManifestApprovalStatusRepository statusRepository) {
		this.statusRepository = statusRepository;
	}

	public ManifestApprovalStatus save(ExImportManifest exImportManifest, String refId, boolean mrn, ExportManifest exportManifest) {
		ManifestApprovalStatus statusEntity=new ManifestApprovalStatus();
		Optional<ManifestApprovalStatus> statusEntityo=statusRepository.findFirstByMrn(exImportManifest.getMrn());
		if (statusEntityo.isPresent()) {
			statusEntity=statusEntityo.get();
			if (mrn) {
				statusEntity.setMrnStatus(true);
			}else {
				statusEntity.setApprovedStatus(false);
			}
			
		}else {
			statusEntity.setMessageReferenceNumber(refId);
			statusEntity.setCommunicationAgreedId(exImportManifest.getCommunicationAgreedId());
			statusEntity.setMrn(exImportManifest.getMrn());
			statusEntity.setTransportMeansId(exImportManifest.getTransportMeansId());
			statusEntity.setVoyageNumber(exImportManifest.getVoyageNumber());
			statusEntity.setMrnOut(exportManifest.getMrnOut());
			statusEntity.setReceivedNoticeSent(false);
			statusEntity.setControlReferenceNumber(exImportManifest.getControlReferenceNumber());
		}
		return statusRepository.save(statusEntity);
	}
}
