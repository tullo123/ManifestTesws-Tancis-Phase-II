package com.ManifestTeswTancis.ServiceImpl;
import java.time.LocalDateTime;
import javax.transaction.Transactional;

import com.ManifestTeswTancis.Entity.LiquidBulkQualityReportEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkRepotRepository;
import com.ManifestTeswTancis.Service.LiquidBulkQualityReportService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.LiquidBulkQualityReportDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public class LiquidBulkQualityReportServiceImpl implements LiquidBulkQualityReportService {
	final LiquidBulkRepotRepository liquidBulkRepotRepository;

	public LiquidBulkQualityReportServiceImpl(LiquidBulkRepotRepository liquidBulkRepotRepository) {
		this.liquidBulkRepotRepository = liquidBulkRepotRepository;
	}


	@Override
	@Transactional
	public TeswsResponse saveLiquidBulkQualityReport(LiquidBulkQualityReportDto liquidBulkQualityReportDto) {
		TeswsResponse response = new TeswsResponse();
		response.setAckType("LIQUID_BULK_QUALITY_REPORT");
		response.setRefId(liquidBulkQualityReportDto.getControlReferenceNumber());
		response.setCode(200);
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setDescription("Liquid Bulk Quality Report Received By TRA");

		LiquidBulkQualityReportEntity lb = new LiquidBulkQualityReportEntity();
		lb.setTbsCodeNo(liquidBulkQualityReportDto.getTbsCodeNo());
		lb.setMrn(liquidBulkQualityReportDto.getMrn());
		lb.setCommunicationAgreedId(liquidBulkQualityReportDto.getCommunicationAgreedId());
		lb.setClient(liquidBulkQualityReportDto.getClient());
		lb.setClientAddress(liquidBulkQualityReportDto.getClientAddress());
		lb.setSample(liquidBulkQualityReportDto.getSample());
		lb.setSiteAndPositionSampled(liquidBulkQualityReportDto.getSiteAndPositionSampled());
		lb.setSamplingPlan(liquidBulkQualityReportDto.getSamplingPlan());
		lb.setDateOfSampling(liquidBulkQualityReportDto.getDateOfSampling());
		lb.setDateOfReceived(liquidBulkQualityReportDto.getDateOfReceived());
		lb.setDateStarted(liquidBulkQualityReportDto.getDateStarted());
		lb.setDateFinished(liquidBulkQualityReportDto.getDateFinished());
		lb.setSpecification(liquidBulkQualityReportDto.getSpecification());
		lb.setTestMethod(liquidBulkQualityReportDto.getTestMethod());
		lb.setReportUrl(liquidBulkQualityReportDto.getReportUrl());
		lb.setReportDate(liquidBulkQualityReportDto.getReportDate());
		lb.setTestMethodDensityAt15(liquidBulkQualityReportDto.getTestResultDto().getDensityAt15().getTestMethod());
		lb.setRequirementDensityAt15(liquidBulkQualityReportDto.getTestResultDto().getDensityAt15().getRequirement());
		lb.setResultDensityAt15(liquidBulkQualityReportDto.getTestResultDto().getDensityAt15().getResult());
		lb.setPassFailAtDensity15(liquidBulkQualityReportDto.getTestResultDto().getDensityAt15().getPassFail());
		lb.setTestMethodDensityAt20(liquidBulkQualityReportDto.getTestResultDto().getDensityAt20().getTestMethod());
		lb.setRequirementDensityAt20(liquidBulkQualityReportDto.getTestResultDto().getDensityAt20().getRequirement());
		lb.setResultDensityAt20(liquidBulkQualityReportDto.getTestResultDto().getDensityAt20().getResult());
		lb.setPassFailDensityAt20(liquidBulkQualityReportDto.getTestResultDto().getDensityAt20().getPassFail());
		lb.setTestMethodCommercialColour(liquidBulkQualityReportDto.getTestResultDto().getCommercialColour().getTestMethod());
		lb.setRequirementCommercialColour(liquidBulkQualityReportDto.getTestResultDto().getCommercialColour().getRequirement());
		lb.setResultCommercialColour(liquidBulkQualityReportDto.getTestResultDto().getCommercialColour().getResult());
		lb.setPassFailCommercialColour(liquidBulkQualityReportDto.getTestResultDto().getCommercialColour().getPassFail());
		lb.setTestMethodDoctorTest(liquidBulkQualityReportDto.getTestResultDto().getDoctorTest().getTestMethod());
		lb.setRequirementDoctorTest(liquidBulkQualityReportDto.getTestResultDto().getDoctorTest().getRequirement());
		lb.setResultDoctorTest(liquidBulkQualityReportDto.getTestResultDto().getDoctorTest().getResult());
		lb.setPassFailDoctorTest(liquidBulkQualityReportDto.getTestResultDto().getDoctorTest().getPassFail());
		lb.setTestMethodAppearance(liquidBulkQualityReportDto.getTestResultDto().getAppearance().getTestMethod());
		lb.setRequirementAppearance(liquidBulkQualityReportDto.getTestResultDto().getAppearance().getRequirement());
		lb.setResultAppearance(liquidBulkQualityReportDto.getTestResultDto().getAppearance().getResult());
		lb.setPassFailAppearance(liquidBulkQualityReportDto.getTestResultDto().getAppearance().getPassFail());
		lb.setFirstRegisterId("TESWS");
		lb.setLastUpdateId("TESWS");
		liquidBulkRepotRepository.save(lb);
		return response;
	}

}
