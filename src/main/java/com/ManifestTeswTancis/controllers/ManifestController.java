package com.ManifestTeswTancis.controllers;
import com.ManifestTeswTancis.Service.*;
import com.ManifestTeswTancis.dtos.ManifestDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Request.PortCallIdRequestModel;
import com.ManifestTeswTancis.ServiceImpl.HeaderServiceImpl;
import com.ManifestTeswTancis.controllers.api.ManifestApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ManifestTeswTancis.dtos.*;
import javax.validation.Valid;
import java.io.IOException;


@RestController
public  class ManifestController  implements ManifestApi {

	final ExImportManifestService exImportManifestService;
	final VesselCancellationService vesselCancellationService;
	final LiquidBulkDischargeSequenceService liquidBulkDischargeSequenceService;
	final LiquidBulkDischargeSequenceUpdateService liquidBulkDischargeSequenceUpdateService;
	final LiquidBulkQualityReportService liquidBulkQualityReportService;
	final PortCallIdService portCallIdService;
	final UpdateVesselService updateVesselService;
	final CustomClearanceService customClearanceService;
	final HeaderServiceImpl headerServiceImpl;
	final PortClearanceService portClearanceService;
	final VesselBoardingService vesselBoardingService;
	final VesselDepartureService vesselDepartureService;
	final VesselTrackingService vesselTrackingService;
	final FreePartiqueService freePartiqueService;
	final UpdateFreePartiqueService updateFreePartiqueService;
	final VesselDocumentationService vesselDocumentationService;
	final VesselDocumentationUpdateService vesselDocumentationUpdateService;
	final LiquidBulkFinalDischargeService liquidBulkFinalDischargeService;

	public ManifestController(LiquidBulkDischargeSequenceUpdateService liquidBulkDischargeSequenceUpdateService, ExImportManifestService exImportManifestService, VesselCancellationService vesselCancellationService, LiquidBulkDischargeSequenceService liquidBulkDischargeSequenceService, LiquidBulkQualityReportService liquidBulkQualityReportService, PortCallIdService portCallIdService, VesselDepartureService vesselDepartureService, UpdateVesselService updateVesselService, CustomClearanceService customClearanceService, HeaderServiceImpl headerServiceImpl, PortClearanceService portClearanceService, VesselBoardingService vesselBoardingService, VesselTrackingService vesselTrackingService, FreePartiqueService freePartiqueService, UpdateFreePartiqueService updateFreePartiqueService, VesselDocumentationService vesselDocumentationService, VesselDocumentationUpdateService vesselDocumentationUpdateService, LiquidBulkFinalDischargeService liquidBulkFinalDischargeService) {
		this.liquidBulkDischargeSequenceUpdateService = liquidBulkDischargeSequenceUpdateService;
		this.exImportManifestService = exImportManifestService;
		this.vesselCancellationService = vesselCancellationService;
		this.liquidBulkDischargeSequenceService = liquidBulkDischargeSequenceService;
		this.liquidBulkQualityReportService = liquidBulkQualityReportService;
		this.portCallIdService = portCallIdService;
		this.vesselDepartureService = vesselDepartureService;
		this.updateVesselService = updateVesselService;
		this.customClearanceService = customClearanceService;
		this.headerServiceImpl = headerServiceImpl;
		this.portClearanceService = portClearanceService;
		this.vesselBoardingService = vesselBoardingService;
		this.vesselTrackingService = vesselTrackingService;
		this.freePartiqueService = freePartiqueService;
		this.updateFreePartiqueService = updateFreePartiqueService;
		this.vesselDocumentationService = vesselDocumentationService;
		this.vesselDocumentationUpdateService = vesselDocumentationUpdateService;
		this.liquidBulkFinalDischargeService = liquidBulkFinalDischargeService;
	}

	@Override
	public ResponseEntity<TeswsResponse> receiveManifest(
			@RequestBody @Valid ManifestDto manifestDto) {
		TeswsResponse response = exImportManifestService.createManifest(manifestDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> createCallInfo(
			@RequestBody @Valid PortCallIdRequestModel callInfDetails) throws IOException {
		TeswsResponse response = portCallIdService.createCallInfo(callInfDetails);
		return ResponseEntity.ok(response);
	}
	@Override
	public ResponseEntity<TeswsResponse> updateCallInf(
			@RequestBody @Valid UpdateVesselDto updateVesselDto) {
		TeswsResponse response = updateVesselService.updateCallInf(updateVesselDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<String> callbackUrl(Object object, String reqId) {
		System.out.println(object);
		return ResponseEntity.ok(object.toString());
	}
	@Override
	public ResponseEntity<TeswsResponse> savePortClearanceNotice(
			@RequestBody @Valid PortClearanceNoticeDto portClearanceNoticeDto) {
		TeswsResponse response = portClearanceService.savePortClearanceNotice(portClearanceNoticeDto);
		return ResponseEntity.ok(response);
	}


	@Override
	public ResponseEntity<TeswsResponse> saveVesselBoarding(
			@RequestBody @Valid VesselBoardingNotificationDto vesselBoardingNotificationDto) {
		TeswsResponse response = vesselBoardingService.saveVesselBoarding(vesselBoardingNotificationDto);
		return ResponseEntity.ok(response);
	}
	@Override
	public ResponseEntity<TeswsResponse> saveVesselDepartureNotice(
			@RequestBody @Valid VesselDepartureNoticeDto vesselDepartureNoticeDto) {
		TeswsResponse response = vesselDepartureService.saveVesselDepartureNotice(vesselDepartureNoticeDto);
		return ResponseEntity.ok(response);
	}
	@Override
	public ResponseEntity<TeswsResponse> customService(
			@RequestBody @Valid CustomClearanceDto customClearanceDto) {
		TeswsResponse response = customClearanceService.customService(customClearanceDto);
		return ResponseEntity.ok(response);
	}
	@Override
	public ResponseEntity<TeswsResponse> deleteVesselInfo(
			@RequestBody PortCallIdCancellationDto portCallIdCancellationDto) {
		TeswsResponse response = vesselCancellationService.cancelVesselInfo(portCallIdCancellationDto);
		return ResponseEntity.ok(response);
	}
	@Override
	public ResponseEntity<TeswsResponse> saveLiquidBulkDischargeSequence(
			LiquidBulkDischargeSequenceDto liquidBulkDischargeSequenceDto) {
		//System.out.println(liquidBulkDischargeSequence.getPumpingSequence());
		TeswsResponse response = liquidBulkDischargeSequenceService.saveLiquidBulkDischargeSequence(liquidBulkDischargeSequenceDto);
		//return null;
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> saveLiquidBulkDischargeSequenceUpdate(
			LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto) {
		TeswsResponse response = liquidBulkDischargeSequenceUpdateService.saveLiquidBulkDischargeSequenceUpdate(liquidBulkDischargeSequenceUpdateDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> saveLiquidBulkQualityReport(
			LiquidBulkQualityReportDto liquidBulkQualityReportDto) {
		TeswsResponse response = liquidBulkQualityReportService.saveLiquidBulkQualityReport(liquidBulkQualityReportDto);
		return ResponseEntity.ok(response);

	}
	@Override
	public ResponseEntity<TeswsResponse> finalDischargeSequence(
			LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto) {
		TeswsResponse response = liquidBulkFinalDischargeService.finalDischargeSequence(liquidBulkDischargeSequenceUpdateDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> vesselTracking(@Valid VesselTrackingNoticeDto vesselTrackingNoticeDto) {
		TeswsResponse response =vesselTrackingService.vesselTracking(vesselTrackingNoticeDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> freePartique(@Valid FreePratiqueReportDto freePratiqueReportDto) {
		TeswsResponse response= freePartiqueService.freePartique(freePratiqueReportDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> updateFreePartiqueReport(@Valid FreePratiqueReportDto freePratiqueReportDto) {
		TeswsResponse response=updateFreePartiqueService.updateFreePartiqueReport(freePratiqueReportDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> vesselDocumentation(@Valid VesselDocumentationDto vesselDocumentationDto) {
		TeswsResponse response=vesselDocumentationService.vesselDocumentationReceiving(vesselDocumentationDto);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> vesselDocumentationUpdate(@Valid VesselDocumentationDto vesselDocumentationDto) {
		TeswsResponse response=vesselDocumentationUpdateService.vesselDocumentationUpdate(vesselDocumentationDto);
		return ResponseEntity.ok(response);
	}

}
