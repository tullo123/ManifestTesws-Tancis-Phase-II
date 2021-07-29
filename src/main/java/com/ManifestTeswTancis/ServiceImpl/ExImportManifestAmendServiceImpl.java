package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Service.ExImportManifestAmendService;
import com.ManifestTeswTancis.dtos.*;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ExImportManifestAmendServiceImpl implements ExImportManifestAmendService{
	private final EdNoticeRepository edNoticeRepository;
	private final ExImportAmendBlRepository exImportAmendBlRepository;
	private final AmendItemContainerRepository amendItemContainerRepository;
	private final ExImportAmendBlContainerRepository exImportAmendBlContainerRepository;
	private final ExImportAmendItemRepository exImportAmendItemRepository;
	private final ExImportAmendPenaltyRepository exImportAmendPenaltyRepository;
	private final ExImportManifestRepository exImportManifestRepository;
	private final ExImportBlContainerRepository exImportBlContainerRepository;

	public ExImportManifestAmendServiceImpl(EdNoticeRepository edNoticeRepository, ExImportAmendBlRepository exImportAmendBlRepository, AmendItemContainerRepository amendItemContainerRepository, ExImportAmendBlContainerRepository exImportAmendBlContainerRepository, ExImportAmendItemRepository exImportAmendItemRepository, ExImportAmendPenaltyRepository exImportAmendPenaltyRepository, ExImportManifestRepository exImportManifestRepository, ExImportBlContainerRepository exImportBlContainerRepository) {
		this.edNoticeRepository = edNoticeRepository;
		this.exImportAmendBlRepository = exImportAmendBlRepository;
		this.amendItemContainerRepository = amendItemContainerRepository;
		this.exImportAmendBlContainerRepository = exImportAmendBlContainerRepository;
		this.exImportAmendItemRepository = exImportAmendItemRepository;
		this.exImportAmendPenaltyRepository = exImportAmendPenaltyRepository;
		this.exImportManifestRepository = exImportManifestRepository;
		this.exImportBlContainerRepository = exImportBlContainerRepository;
	}

	@Override
	public TeswsResponse amendManifest(ManifestAmendmentDto manifestAmendmentDto) {
		TeswsResponse response=new TeswsResponse();
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		response.setRefId(manifestAmendmentDto.getAmendmentReference());
		response.setAckDate(localDateTime.format(formatter));
		response.setAckType("MANIFEST_AMENDMENT_NOTICE");

		try {
			Optional<ExImportManifest> manifestOptional =
					exImportManifestRepository.findFirstByCommunicationAgreedId(manifestAmendmentDto.getCallId());
			if(manifestOptional.isPresent()) {
				ExImportManifest ex=manifestOptional.get();
				List <AdditionalBls> additionalBls=manifestAmendmentDto.getAdditionalBls();
				Map<String, Map<String, String>> containerBlMap = new HashMap<>();
				setAdditionalBls((AdditionalBls) additionalBls,manifestAmendmentDto.getMrn(),containerBlMap);
				List<AmendedBls>amendedBls=manifestAmendmentDto.getAmendedBls();
				saveAmendedBls((AmendedBls) amendedBls,manifestAmendmentDto.getMrn(),containerBlMap);
				List<AdditionalContainer>additionalContainers=manifestAmendmentDto.getAdditionalContainers();
				saveAdditionalContainer(additionalContainers);
				List<DeletedContainerDto>deletedContainers=manifestAmendmentDto.getDeletedContainers();
				saveDeletedContainers(deletedContainers);
				List<ContainerDtoAmend> amendedContainers=manifestAmendmentDto.getAmendedContainers();
				List<DeletedBlDto>deletedBls=manifestAmendmentDto.getDeletedBls();
				saveAmendedContainers((ContainerDtoAmend) amendedContainers, (AmendedBls) amendedBls,containerBlMap);
				this.createAmendEdNotice(manifestAmendmentDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		return response;
	}
	private  void saveGeneralAmendment(ManifestAmendmentDto manifestAmendmentDto,AdditionalBls additionalBls,AmendedBls amendedBls,
									   AdditionalContainer additionalContainer, ContainerDtoAmend containerDtoAmend,DeletedBlDto deletedBls){
		ExImportAmendGeneral general=new ExImportAmendGeneral();
		ExImportManifest x = new ExImportManifest();
		if (additionalBls.getDeclarantTin()!=null || amendedBls.getDeclarantTin()!=null || additionalContainer.getDeclarantTin()!=null){
			general.setDeclarantTin(additionalBls.getDeclarantTin());
			general.setDeclarantTin(amendedBls.getDeclarantTin());
			general.setDeclarantTin(additionalContainer.getDeclarantTin());
		}
		SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
		String strYear = simpleformat.format(new Date());
		general.setAmendYear(strYear);
		general.setProcessType("M");
		general.setAmendSerialNumber(generateAmendSerial());
		general.setProcessingStatus("1");
		general.setProcessingId(x.getCarrierName());
		general.setCustomOfficeCode(x.getCustomOfficeCode());
		general.setMrn(manifestAmendmentDto.getMrn());
		general.setMsn(amendedBls.getMsn());
		general.setHsn(amendedBls.getHsn());
		if(additionalBls.getMasterBillOfLading()!=null && additionalBls.getHouseBillOfLading()==null){
			general.setAmendType("MA");
		}else if (additionalBls.getHouseBillOfLading()!=null && additionalBls.getMasterBillOfLading()!=null){
			general.setAmendType("HA");
		}else if(amendedBls.getMasterBillOfLading()!=null && amendedBls.getHouseBillOfLading()==null){
			general.setAmendType("MM");
		}else if(amendedBls.getMasterBillOfLading()!=null && amendedBls.getHouseBillOfLading()!=null){
			general.setAmendType("HM");
		}else if(containerDtoAmend.getContainerNo()!=null && containerDtoAmend.getDeclarantTin()!=null){
			general.setAmendType("CM");
		}else if(deletedBls.getMsn()!=null){
			general.setAmendType("MD");
		}else if(deletedBls.getHsn()!=null){
			general.setAmendType("HD");
		}
		general.setAmendReasonCode("01");
		general.setAmendReasonComment("SIMPLE ERROR");
		general.setAuditor("NA");
		general.setDeclarantCode(x.getCarrierId());
		general.setDeclarantName(x.getCarrierName());
	}

	@SuppressWarnings("Unchecked")
	private void saveAmendedContainers(ContainerDtoAmend amendedContainers, AmendedBls amendedBls, Map<String, Map<String, String>> containerBlMap) {
		if(amendedContainers.getContainerNo()!=null){
			ExImportAmendBlContainer ex=new ExImportAmendBlContainer();
			Map<String, String> blMap = containerBlMap.get(amendedContainers.getContainerNo());
			if(!blMap.isEmpty()){
				ex.setDeclarantTin(amendedContainers.getDeclarantTin());
				SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
				String strYear = simpleformat.format(new Date());
				ex.setAmendYear(strYear);
				ex.setProcessType("M");
				ex.setAmendSerialNumber(amendedContainers.getAmendSerialNumber());
				if(amendedBls.getHouseBillOfLading()!=null){
					ex.setBillOfLading(amendedBls.getHouseBillOfLading());
				}else {
					ex.setBillOfLading(amendedBls.getMasterBillOfLading());
				}
				ex.setContainerNo(amendedContainers.getContainerNo());
				ex.setContainerSize(amendedContainers.getContainerSize());
				ex.setTypeOfContainer(amendedContainers.getTypeOfContainer());
				ex.setFreightIndicator(amendedContainers.getFreightIndicator());
				ex.setWeight(amendedContainers.getWeight());
				ex.setWeightUnit(amendedContainers.getWeightUnit());
				ex.setVolume(amendedContainers.getVolume());
				ex.setVolumeUnit(amendedContainers.getVolumeUnit());
				ex.setMaximumTemperature(amendedContainers.getMaximumTemperature());
				ex.setMinimumTemperature(amendedContainers.getMinimumTemperature());
				if (amendedContainers.getTemperatureType().contentEquals("1")) {
					ex.setReferPlugYn("Y");
				} else {
					ex.setReferPlugYn("N");
				}
				int i = 0;
				int l = 0;
				if (amendedContainers.getSealNumbers() != null && !amendedContainers.getSealNumbers().isEmpty()) {

					for (SealNumberDto sealNumber : amendedContainers.getSealNumbers()) {
						if (sealNumber.getSealNumberIssuerType() != null
								&& sealNumber.getSealNumberIssuerType().contentEquals("CU")) {
							if (i == 0) {
								ex.setCustomSealNumberOne(sealNumber.getSealNumber());
								i++;
							} else if (i == 1) {
								ex.setCustomSealNumberTwo(sealNumber.getSealNumber());
								i++;
							} else if (i == 2) {
								ex.setCustomSealNumberThree(sealNumber.getSealNumber());
								i++;
							}
						} else {
							if (l == 0) {
								ex.setSealNumberOne(sealNumber.getSealNumber());
								l++;
							} else if (l == 1) {
								ex.setSealNumberTwo(sealNumber.getSealNumber());
								l++;
							} else if (l == 2) {
								ex.setSealNumberThree(sealNumber.getSealNumber());
								l++;
							}
						}

					}
				}

				exImportAmendBlContainerRepository.save(ex);
			}
		}


	}

	private void createAmendEdNotice(ManifestAmendmentDto manifestAmendmentDto) {
		EdNoticeEntity edNotice = new EdNoticeEntity();
		ExImportManifest ex= new ExImportManifest();
		edNotice.setDocumentCode("IMFMOD215");
		edNotice.setDocumentNumber(manifestAmendmentDto.getMrn());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String strDate = dtf.format(now);
		edNotice.setCreateDate(strDate);
		edNotice.setDocumentFunctionType("9");
		edNotice.setSequenceNumber(1);
		edNotice.setCustomsOfficeCode(ex.getCustomOfficeCode());
		edNotice.setReceiverId("INTERNAL");
		edNotice.setSenderId("EXTERNAL");
		edNotice.setDocumentType("D");
		edNotice.setOriginalDocumentCode("IMFMOD215");
		edNotice.setOriginalDocumentNumber(manifestAmendmentDto.getMrn());
		edNotice.setProcessingStatus("N");
		edNotice.setTransferType("E");
		edNoticeRepository.save(edNotice);
	}

	private void saveDeletedContainers(List<DeletedContainerDto> deletedContainers) {
		ExImportAmendBlContainer ex = new ExImportAmendBlContainer();
		for (DeletedContainerDto containerDto: deletedContainers){
			ex.setContainerNo(containerDto.getContainerNo());
		}

	}

	@SuppressWarnings("Duplicates")
	private void saveAdditionalContainer(List<AdditionalContainer> additionalContainers) {
		for (AdditionalContainer container : additionalContainers) {
			ExImportAmendBlContainer ex = new ExImportAmendBlContainer();
			if (container.getContainerNo() != null) {
				ex.setDeclarantTin(container.getDeclarantTin());
				SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
				String strYear = simpleformat.format(new Date());
				ex.setAmendYear(strYear);
				ex.setProcessType("M");
				ex.setAmendSerialNumber(generateAmendSerial());
				ex.setContainerNo(container.getContainerNo());
				ex.setContainerSize(container.getContainerSize());
				ex.setTypeOfContainer(container.getTypeOfContainer());
				ex.setVolume(container.getVolume());
				ex.setVolumeUnit(container.getVolumeUnit());
				ex.setWeight(container.getWeight());
				ex.setWeightUnit(container.getWeightUnit());
				ex.setFreightIndicator(container.getFreightIndicator());
				ex.setMaximumTemperature(container.getMaximumTemperature());
				ex.setMinimumTemperature(container.getMinimumTemperature());
				if (container.getTemperatureType().contentEquals("1")) {
					ex.setReferPlugYn("Y");
				} else {
					ex.setReferPlugYn("N");
				}
				int i = 0;
				int l = 0;
				if (container.getSealNumbers() != null && !container.getSealNumbers().isEmpty()) {

					for (SealNumberDto sealNumber : container.getSealNumbers()) {
						if (sealNumber.getSealNumberIssuerType() != null
								&& sealNumber.getSealNumberIssuerType().contentEquals("CU")) {
							if (i == 0) {
								ex.setCustomSealNumberOne(sealNumber.getSealNumber());
								i++;
							} else if (i == 1) {
								ex.setCustomSealNumberTwo(sealNumber.getSealNumber());
								i++;
							} else if (i == 2) {
								ex.setCustomSealNumberThree(sealNumber.getSealNumber());
								i++;
							}
						} else {
							if (l == 0) {
								ex.setSealNumberOne(sealNumber.getSealNumber());
								l++;
							} else if (l == 1) {
								ex.setSealNumberTwo(sealNumber.getSealNumber());
								l++;
							} else if (l == 2) {
								ex.setSealNumberThree(sealNumber.getSealNumber());
								l++;
							}
						}

					}
				}

				exImportAmendBlContainerRepository.save(ex);
			}
		}
	}


	@SuppressWarnings("Duplicates")
	private void saveAmendedBls(AmendedBls amendedBls, String mrn, Map<String, Map<String, String>> containerBlMap) {
		List<GoodsDto> goodsDtos=amendedBls.getGoodDetails();
		for (GoodsDto gd : goodsDtos) {
			ExImportAmendBl eb= new ExImportAmendBl();
			eb.setDeclatantTin(amendedBls.getDeclarantTin());
			eb.setBlType(amendedBls.getBlType());
			SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
			String strYear = simpleformat.format(new Date());
			eb.setAmendYear(strYear);
			eb.setProcessType("M");
			if(amendedBls.getMasterBillOfLading()!=null){
				eb.setBillOfLading(amendedBls.getMasterBillOfLading());
			}
			eb.setShippingAgentCode(amendedBls.getShippingAgentCode());
			eb.setForwarderCode(amendedBls.getForwarderCode());
			eb.setForwarderName(amendedBls.getForwarderName());
			eb.setForwarderTel(amendedBls.getForwarderTel());
			eb.setExporterAddress(amendedBls.getExporterAddress());
			eb.setExporterName(amendedBls.getExporterName());
			eb.setExporterTel(amendedBls.getExporterTel());
			eb.setExporterTin(amendedBls.getExporterTin());
			eb.setConsigneeAddress(amendedBls.getConsigneeAddress());
			eb.setConsigneeName(amendedBls.getConsigneeName());
			eb.setConsigneeTel(amendedBls.getConsigneeTel());
			eb.setConsigneeTin(amendedBls.getConsigneeTin());
			eb.setNotifyAddress(amendedBls.getNotifyAddress());
			eb.setAmendSerialNumber(generateAmendSerial());
			eb.setNotifyName(amendedBls.getNotifyName());
			eb.setNotifyTel(amendedBls.getNotifyTel());
			eb.setNotifyTin(amendedBls.getNotifyTin());
			eb.setGrossWeight(gd.getGrossWeight());
			eb.setGrossWeightUnit(gd.getGrossWeightUnit());
			eb.setNetWeight(gd.getNetWeight());
			eb.setNetWeightUnit(gd.getNetWeightUnit());
			eb.setVolume(gd.getVolume());
			eb.setInvoiceCurrency(gd.getInvoiceCurrency());
			eb.setInvoiceValue(gd.getInvoiceValue());
			eb.setVolumeUnit(gd.getVolumeUnit());
			eb.setOilType(gd.getOilType());
			eb.setPackingType(gd.getPackingType());
			eb.setDescription(gd.getDescription());
			eb.setPortOfLoading(amendedBls.getPortOfLoading());
			eb.setPlaceOfDestination(amendedBls.getPlaceOfDestination());
			eb.setPlaceOfDelivery(amendedBls.getPlaceOfDelivery());
			if (gd.getDangerousGoodInformation()!=null && gd.getDangerousGoodInformation().getImdgclass() != null) {
				eb.setImdgclass(gd.getDangerousGoodInformation().getImdgclass());
			}
			exImportAmendBlRepository.save(eb);
		}

	}

	private void setAdditionalBls(AdditionalBls additionalBls, String mrn, Map<String, Map<String, String>> containerBlMap) {
		if(additionalBls.getMasterBillOfLading()!=null || additionalBls.getHouseBillOfLading()!=null){
			List<GoodsDto> goodsDtos=additionalBls.getGoodDetails();
			for (GoodsDto gd : goodsDtos) //noinspection Duplicates
			{
				ExImportAmendBl eb= new ExImportAmendBl();
				eb.setBlType(additionalBls.getBlType());
				eb.setDeclatantTin(additionalBls.getDeclarantTin());
				eb.setForwarderCode(additionalBls.getForwarderCode());
				eb.setForwarderName(additionalBls.getForwarderName());
				eb.setForwarderTel(additionalBls.getForwarderTel());
				eb.setProcessType("M");
				if(additionalBls.getMasterBillOfLading()!=null){
					eb.setBillOfLading(additionalBls.getMasterBillOfLading());
				}
				eb.setShippingAgentCode(additionalBls.getShippingAgentCode());
				eb.setExporterAddress(additionalBls.getExporterAddress());
				SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
				String strYear = simpleformat.format(new Date());
				eb.setAmendYear(strYear);
				eb.setExporterTin(additionalBls.getExporterTin());
				eb.setConsigneeAddress(additionalBls.getConsigneeAddress());
				eb.setConsigneeName(additionalBls.getConsigneeName());
				eb.setConsigneeTel(additionalBls.getConsigneeTel());
				eb.setConsigneeTin(additionalBls.getConsigneeTin());
				eb.setNotifyAddress(additionalBls.getNotifyAddress());
				eb.setAmendSerialNumber(generateAmendSerial());
				eb.setNotifyName(additionalBls.getNotifyName());
				eb.setNotifyTel(additionalBls.getNotifyTel());
				eb.setNotifyTin(additionalBls.getNotifyTin());
				eb.setExporterName(additionalBls.getExporterName());
				eb.setExporterTel(additionalBls.getExporterTel());
				eb.setGrossWeight(gd.getGrossWeight());
				eb.setGrossWeightUnit(gd.getGrossWeightUnit());
				eb.setNetWeight(gd.getNetWeight());
				eb.setNetWeightUnit(gd.getNetWeightUnit());
				eb.setVolume(gd.getVolume());
				eb.setInvoiceCurrency(gd.getInvoiceCurrency());
				eb.setInvoiceValue(gd.getInvoiceValue());
				eb.setVolumeUnit(gd.getVolumeUnit());
				eb.setOilType(gd.getOilType());
				eb.setPackingType(gd.getPackingType());
				eb.setDescription(gd.getDescription());
				eb.setPortOfLoading(additionalBls.getPortOfLoading());
				eb.setPlaceOfDestination(additionalBls.getPlaceOfDestination());
				eb.setPlaceOfDelivery(additionalBls.getPlaceOfDelivery());
				if (gd.getDangerousGoodInformation()!=null && gd.getDangerousGoodInformation().getImdgclass() != null) {
					eb.setImdgclass(gd.getDangerousGoodInformation().getImdgclass());
				}
				exImportAmendBlRepository.save(eb);
			}
		}
	}

	private String generateAmendSerial(){
		int i = 1;
		String amendSerial= String.format("%07d", i);
		i++;
		return amendSerial;
	}
}
