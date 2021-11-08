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
	

	final
	LiquidBulkRepotRepository liquidBulkRepotRepository;

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
		lb.setCall_id(liquidBulkQualityReportDto.getCall_id());
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
		lb.setTestMethodDensityAt15(liquidBulkQualityReportDto.getTestResult().getDensityAt15().getTestMethod());
		lb.setRequirementDensityAt15(liquidBulkQualityReportDto.getTestResult().getDensityAt15().getRequirement());
		lb.setResultDensityAt15(liquidBulkQualityReportDto.getTestResult().getDensityAt15().getResult());
		lb.setPassFailAtDensity15(liquidBulkQualityReportDto.getTestResult().getDensityAt15().getPassFail());
		lb.setTestMethodDensityAt20(liquidBulkQualityReportDto.getTestResult().getDensityAt20().getTestMethod());
		lb.setRequirementDensityAt20(liquidBulkQualityReportDto.getTestResult().getDensityAt20().getRequirement());
		lb.setResultDensityAt20(liquidBulkQualityReportDto.getTestResult().getDensityAt20().getResult());
		lb.setPassFailDensityAt20(liquidBulkQualityReportDto.getTestResult().getDensityAt20().getPassFail());
		lb.setTestMethodCommercialColour(liquidBulkQualityReportDto.getTestResult().getCommercialColour().getTestMethod());
		lb.setRequirementCommercialColour(liquidBulkQualityReportDto.getTestResult().getCommercialColour().getRequirement());
		lb.setResultCommercialColour(liquidBulkQualityReportDto.getTestResult().getCommercialColour().getResult());
		lb.setPassFailCommercialColour(liquidBulkQualityReportDto.getTestResult().getCommercialColour().getPassFail());
		lb.setTestMethodDoctorTest(liquidBulkQualityReportDto.getTestResult().getDoctorTest().getTestMethod());
		lb.setRequirementDoctorTest(liquidBulkQualityReportDto.getTestResult().getDoctorTest().getRequirement());
		lb.setResultDoctorTest(liquidBulkQualityReportDto.getTestResult().getDoctorTest().getResult());
		lb.setPassFailDoctorTest(liquidBulkQualityReportDto.getTestResult().getDoctorTest().getPassFail());
		lb.setTestMethodAppearance(liquidBulkQualityReportDto.getTestResult().getAppearance().getTestMethod());
		lb.setRequirementAppearance(liquidBulkQualityReportDto.getTestResult().getAppearance().getRequirement());
		lb.setResultAppearance(liquidBulkQualityReportDto.getTestResult().getAppearance().getResult());
		lb.setPassFailAppearance(liquidBulkQualityReportDto.getTestResult().getAppearance().getPassFail());
		lb.setFirstRegisterId("TESWS");
		lb.setLastUpdateId("TESWS");
		liquidBulkRepotRepository.save(lb);
		return response;
	}

}
