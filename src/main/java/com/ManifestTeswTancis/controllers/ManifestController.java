package com.ManifestTeswTancis.controllers;
import com.ManifestTeswTancis.Service.*;
import com.ManifestTeswTancis.dtos.ManifestDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Request.CallInfDetailsRequestModel;
import com.ManifestTeswTancis.ServiceImpl.HeaderServiceImpl;
import com.ManifestTeswTancis.controllers.api.ManifestApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ManifestTeswTancis.dtos.*;
import javax.validation.Valid;


@RestController
public  class ManifestController  implements ManifestApi {

	final ExImportManifestService exImportManifestService;
	final VesselCancellationService vesselCancellationService;
	final LiquidBulkDischargeSequenceService liquidBulkDischargeSequenceService;
	final LiquidBulkDischargeSequenceUpdateService liquidBulkDischargeSequenceUpdateService;
	final LiquidBulkQualityReportService liquidBulkQualityReportService;
	final CallInfService callInfService;
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

	public ManifestController(LiquidBulkDischargeSequenceUpdateService liquidBulkDischargeSequenceUpdateService, ExImportManifestService exImportManifestService, VesselCancellationService vesselCancellationService, LiquidBulkDischargeSequenceService liquidBulkDischargeSequenceService, LiquidBulkQualityReportService liquidBulkQualityReportService, CallInfService callInfService, VesselDepartureService vesselDepartureService, UpdateVesselService updateVesselService, CustomClearanceService customClearanceService, HeaderServiceImpl headerServiceImpl, PortClearanceService portClearanceService, VesselBoardingService vesselBoardingService, VesselTrackingService vesselTrackingService, FreePartiqueService freePartiqueService, UpdateFreePartiqueService updateFreePartiqueService, VesselDocumentationService vesselDocumentationService, VesselDocumentationUpdateService vesselDocumentationUpdateService, LiquidBulkFinalDischargeService liquidBulkFinalDischargeService) {
		this.liquidBulkDischargeSequenceUpdateService = liquidBulkDischargeSequenceUpdateService;
		this.exImportManifestService = exImportManifestService;
		this.vesselCancellationService = vesselCancellationService;
		this.liquidBulkDischargeSequenceService = liquidBulkDischargeSequenceService;
		this.liquidBulkQualityReportService = liquidBulkQualityReportService;
		this.callInfService = callInfService;
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
			@RequestBody @Valid CallInfDetailsRequestModel callInfDetails) {
		TeswsResponse response = callInfService.createCallInfo(callInfDetails);
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
			@RequestBody CallInfCancelDto callInfCancelDto) {
		TeswsResponse response = vesselCancellationService.cancelVesselInfo(callInfCancelDto);
		return ResponseEntity.ok(response);
	}
	@Override
	public ResponseEntity<TeswsResponse> saveLiquidBulkDischargeSequence(
			LiquidBulkDischargeSequence liquidBulkDischargeSequence) {
		//System.out.println(liquidBulkDischargeSequence.getPumpingSequence());
		TeswsResponse response = liquidBulkDischargeSequenceService.saveLiquidBulkDischargeSequence(liquidBulkDischargeSequence);
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
	public ResponseEntity<TeswsResponse> vesselTracking(@Valid VesselTrackingNotice vesselTrackingNotice) {
		TeswsResponse response =vesselTrackingService.vesselTracking(vesselTrackingNotice);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> freePartique(@Valid FreePratiqueReport freePratiqueReport) {
		TeswsResponse response= freePartiqueService.freePartique(freePratiqueReport);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> updateFreePartiqueReport(@Valid FreePratiqueReport freePratiqueReport) {
		TeswsResponse response=updateFreePartiqueService.updateFreePartiqueReport(freePratiqueReport);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> vesselDocumentation(@Valid VesselDocumentation vesselDocumentation) {
		TeswsResponse response=vesselDocumentationService.vesselDocumentationReceiving(vesselDocumentation);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TeswsResponse> vesselDocumentationUpdate(@Valid VesselDocumentation vesselDocumentation) {
		TeswsResponse response=vesselDocumentationUpdateService.vesselDocumentationUpdate(vesselDocumentation);
		return ResponseEntity.ok(response);
	}

}
