package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ManifestStatusServiceImp {
	private final ManifestApprovalStatusRepository statusRepository;

	@Autowired
	public ManifestStatusServiceImp(ManifestApprovalStatusRepository statusRepository) {
		this.statusRepository = statusRepository;
	}

	public ManifestApprovalStatus save(ExImportManifest exImportManifest, String refId, boolean mrn, ExportManifest exportManifest) {
		ManifestApprovalStatus manifestApprovalStatus=new ManifestApprovalStatus();
		Optional<ManifestApprovalStatus> status=statusRepository.findFirstByMrn(exImportManifest.getMrn());
		if (status.isPresent()) {
			manifestApprovalStatus=status.get();
			if (mrn) { manifestApprovalStatus.setMrnStatus(true); }
			else { manifestApprovalStatus.setApprovedStatus(false); }
			
		}else {
			manifestApprovalStatus.setControlReferenceNumber(refId);
			manifestApprovalStatus.setCommunicationAgreedId(exImportManifest.getCommunicationAgreedId());
			manifestApprovalStatus.setMrn(exImportManifest.getMrn());
			manifestApprovalStatus.setTransportMeansId(exImportManifest.getTransportMeansId());
			manifestApprovalStatus.setVoyageNumber(exImportManifest.getVoyageNumber());
			manifestApprovalStatus.setMrnOut(exportManifest.getMrnOut());
			manifestApprovalStatus.setMrnStatus(true);
			manifestApprovalStatus.setReceivedNoticeSent(false);
			manifestApprovalStatus.setExportManifestReceivedStatus(false);
			manifestApprovalStatus.setExportManifestApprovedStatus(false);
		}
		return statusRepository.save(manifestApprovalStatus);
	}
}
