package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Service.ExImportManifestAmendService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ExImportManifestAmendServiceImpl implements ExImportManifestAmendService {
    final AmendItemContainerRepository amendItemContainerRepository;
    final BlGoodItemsRepository blGoodItemsRepository;
    final EdNoticeRepository edNoticeRepository;
    final ExImportAmendGeneralRepository exImportAmendGeneralRepository;
    final ExImportAmendItemRepository importAmendItemRepository;
    final ExImportAmendBlRepository exImportAmendBlRepository;
    final ExImportManifestRepository exImportManifestRepository;
    final CoCompanyCodeRepository coCompanyCodeRepository;
    final CommonOrdinalRepository commonOrdinalRepository;
    final InImportManifestRepository inImportManifestRepository;
    final ExImportMasterBlRepository exImportMasterBlRepository;
    final ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository;
    final ExImportBlContainerRepository exImportBlContainerRepository;
    final ExImportHouseBlRepository exImportHouseBlRepository;

    @Autowired
    public ExImportManifestAmendServiceImpl(AmendItemContainerRepository amendItemContainerRepository, BlGoodItemsRepository blGoodItemsRepository, EdNoticeRepository edNoticeRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository, ExImportAmendItemRepository importAmendItemRepository, ExImportAmendBlRepository exImportAmendBlRepository, ExImportManifestRepository exImportManifestRepository, CoCompanyCodeRepository coCompanyCodeRepository, CommonOrdinalRepository commonOrdinalRepository, InImportManifestRepository inImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, ExImportBlContainerRepository exImportBlContainerRepository, ExImportHouseBlRepository exImportHouseBlRepository) {
        this.amendItemContainerRepository = amendItemContainerRepository;
        this.blGoodItemsRepository = blGoodItemsRepository;
        this.edNoticeRepository = edNoticeRepository;
        this.exImportAmendGeneralRepository = exImportAmendGeneralRepository;
        this.importAmendItemRepository = importAmendItemRepository;
        this.exImportAmendBlRepository = exImportAmendBlRepository;
        this.exImportManifestRepository = exImportManifestRepository;
        this.coCompanyCodeRepository = coCompanyCodeRepository;
        this.commonOrdinalRepository = commonOrdinalRepository;
        this.inImportManifestRepository = inImportManifestRepository;
        this.exImportMasterBlRepository = exImportMasterBlRepository;
        this.manifestAmendmentApprovalStatusRepository = manifestAmendmentApprovalStatusRepository;
        this.exImportBlContainerRepository = exImportBlContainerRepository;
        this.exImportHouseBlRepository = exImportHouseBlRepository;
    }

    @Override
    public TeswsResponse amendManifest(ManifestAmendmentDto manifestAmendmentDto) {
        TeswsResponse responseData = new TeswsResponse();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        responseData.setRefId(manifestAmendmentDto.getAmendmentReference());
        responseData.setAckDate(localDateTime.format(formatter));
        responseData.setAckType("MANIFEST_AMENDMENT");
        try {
            Optional<ExImportManifest> optional = exImportManifestRepository.
                    findFirstByMrn(manifestAmendmentDto.getMrn());
            if (optional.isPresent()) {
                BlDto bl = manifestAmendmentDto.getBl();
                List<Containers> containers = manifestAmendmentDto.getContainers();
                saveGeneralAmendment(bl, manifestAmendmentDto);
                if(manifestAmendmentDto.getBl()!=null && manifestAmendmentDto.getAmendType().
                        equalsIgnoreCase("AMEND_BL")){
                    saveAmendBl(bl,manifestAmendmentDto);
                }
                else if (manifestAmendmentDto.getBl() != null) {
                    saveBl(bl, manifestAmendmentDto);
                }if(!manifestAmendmentDto.getContainers().isEmpty() && manifestAmendmentDto.getAmendType().
                        equalsIgnoreCase("AMEND_CONTAINER")){
                    saveAmendedContainerDetail(containers, bl,manifestAmendmentDto);
                }
                 else if (!manifestAmendmentDto.getContainers().isEmpty()) {
                    saveContainers(containers, bl);
                }
                this.createEdNotice(manifestAmendmentDto, bl);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            responseData.setDescription("Errors in saving manifest Amendment" + exception.getMessage());
            responseData.setCode(500);
        }

        return responseData;
    }




    private void saveGeneralAmendment(BlDto bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendGeneral amendGeneral = new ExImportAmendGeneral();
        CommonOrdinalEntity commonOrdinalEntity;
        Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
        if (optional.isPresent()) {
            CoCompanyCodeEntity company = optional.get();
            amendGeneral.setDeclarantTin(company.getTin());
            Optional<CoCompanyCodeEntity> opt = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
            if (opt.isPresent()) {
                CoCompanyCodeEntity entity = optional.get();
                String tin = entity.getTin();
                DateFormat df = new SimpleDateFormat("yyyy");
                String prefix = tin + df.format(Calendar.getInstance().getTime()) + "M";
                Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
                if (optionalCommonOrdinalEntity.isPresent()) {
                    commonOrdinalEntity = optionalCommonOrdinalEntity.get();
                    commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo() + 1);
                } else {
                    commonOrdinalEntity = new CommonOrdinalEntity();
                    commonOrdinalEntity.setPrefix(prefix);
                    commonOrdinalEntity.setSequenceNo(1);
                }
                commonOrdinalRepository.save(commonOrdinalEntity);
                String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
                amendGeneral.setAmendSerialNumber(suffix);

            }
        }
        DateFormat df = new SimpleDateFormat("yyyy");
        amendGeneral.setAmendYear(df.format(Calendar.getInstance().getTime()));
        amendGeneral.setProcessType("M");
        amendGeneral.setProcessingStatus("1");
        amendGeneral.setProcessingDate(DateFormatter.getDateFromLocalDateTime(LocalDateTime.now()));
        amendGeneral.setProcessingId("SYSTEM");
        Optional<InImportManifest> option = inImportManifestRepository.findFirstByCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        if (option.isPresent()) {
            InImportManifest amend = option.get();
            amendGeneral.setCustomOfficeCode(amend.getCustomOfficeCode());
        }
        amendGeneral.setMrn(manifestAmendmentDto.getMrn());
        String crn= bl.getCrn().trim();
        if(crn.length()==15) {
            amendGeneral.setMsn(bl.getCrn().substring(11, 15));
        }else if (crn.length()==18){
            amendGeneral.setHsn(bl.getCrn().substring(15,18));
        }

        if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("ADD_BL")) {
            if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null) {
                amendGeneral.setAmendType("HA");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode() == null) {
                amendGeneral.setAmendType("MA");
            }
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_BL")) {
            if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null) {
                amendGeneral.setAmendType("HM");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode() == null) {
                amendGeneral.setAmendType("MM");
            }
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("DELETE_BL")) {
            if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null) {
                amendGeneral.setAmendType("HD");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode() == null) {
                amendGeneral.setAmendType("MD");
            }
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_BL") && bl.getPlaceOfDelivery() != null) {
            amendGeneral.setAmendType("TI");
        }
        if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("ADD_CONTAINER")) {
            amendGeneral.setAmendType("CA");
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_CONTAINER")) {
            amendGeneral.setAmendType("CM");
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("DELETE_CONTAINER")) {
            amendGeneral.setAmendType("CD");
        }


        amendGeneral.setAmendReasonCode("01");
        amendGeneral.setDeclarantCode(bl.getShippingAgentCode());
        amendGeneral.setDeclarantName(bl.getShippingAgentName());
        amendGeneral.setFirstRegisterId("TESWS");
        amendGeneral.setFirstRegisterDate(DateFormatter.getDateFromLocalDateTime(LocalDateTime.now()));
        amendGeneral.setSubmitDate(DateFormatter.getDateFromLocalDateTime(LocalDateTime.now()));
        amendGeneral.setLastUpdateId("TESWS");
        exImportAmendGeneralRepository.save(amendGeneral);
        ManifestAmendmentApprovalStatus manifestAmendmentApprovalStatus= new ManifestAmendmentApprovalStatus(manifestAmendmentDto);
        manifestAmendmentApprovalStatus.setCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        manifestAmendmentApprovalStatus.setMrn(manifestAmendmentDto.getMrn());
        manifestAmendmentApprovalStatus.setAmendReference(manifestAmendmentDto.getAmendmentReference());
        manifestAmendmentApprovalStatus.setAmendType(manifestAmendmentDto.getAmendType());
        manifestAmendmentApprovalStatus.setAmendSerialNo(amendGeneral.getAmendSerialNumber());
        manifestAmendmentApprovalStatusRepository.save(manifestAmendmentApprovalStatus);

    }

    private void saveBl(BlDto bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendBl amendBl = new ExImportAmendBl();
        BlMeasurement blMeasurement = new BlMeasurement();
        Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
        if (optional.isPresent()) {
            CoCompanyCodeEntity entity = optional.get();
            amendBl.setDeclatantTin(entity.getTin());
        }
        DateFormat df = new SimpleDateFormat("yyyy");
        amendBl.setAmendYear(df.format(Calendar.getInstance().getTime()));
        amendBl.setProcessType("M");
        if (bl.getHouseBillOfLading() != null) {
            amendBl.setBillOfLading(bl.getHouseBillOfLading());
        } else {
            amendBl.setBillOfLading(bl.getMasterBillOfLading());
        }
        amendBl.setTradeType(getTradeType(bl));
        if (bl.getHouseBillOfLading() != null) {
            amendBl.setBlType("C");
        } else {
            amendBl.setBlType("S");
        }
        amendBl.setShippingAgentCode(bl.getShippingAgentCode());
        amendBl.setForwarderCode(bl.getForwarderCode());
        amendBl.setForwarderName(bl.getForwarderName());
        amendBl.setForwarderTel(bl.getForwarderTel());
        amendBl.setExporterTin(bl.getExporterTin());
        amendBl.setExporterName(bl.getExporterName());
        amendBl.setExporterTel(bl.getExporterTel());
        amendBl.setExporterAddress(bl.getExporterAddress());
        amendBl.setConsigneeTin(bl.getConsigneeTin());
        amendBl.setConsigneeName(bl.getConsigneeName());
        amendBl.setConsigneeTel(bl.getConsigneeTel());
        amendBl.setConsigneeAddress(bl.getConsigneeAddress());
        amendBl.setNotifyTin(bl.getNotifyTin());
        amendBl.setNotifyName(bl.getNotifyName());
        amendBl.setNotifyTel(bl.getNotifyTel());

        amendBl.setNotifyAddress(bl.getNotifyAddress());
        amendBl.setDescription(bl.getBlDescription());
        amendBl.setBlPackage(blMeasurement.getPkQuantity());
        amendBl.setPackageUnit(blMeasurement.getPkType());
        if (bl.getBlSummary() != null) {
            amendBl.setGrossWeight(bl.getBlSummary().getBlGrossWeight());
            amendBl.setNetWeight(bl.getBlSummary().getBlNetWeight());
        }
        amendBl.setGrossWeightUnit("KG");
        amendBl.setNetWeightUnit("KG");
        amendBl.setVolume(blMeasurement.getVolume());
        amendBl.setVolumeUnit(blMeasurement.getVolumeUnit());
        amendBl.setPortOfLoading(bl.getPortOfLoading());
        amendBl.setPlaceOfDestination(bl.getPlaceOfDestination());
        amendBl.setPlaceOfDestination(bl.getPlaceOfDestination());
        amendBl.setPlaceOfDelivery(bl.getPlaceOfDelivery());
        for (GoodDetails goodsDto : bl.getGoodDetails()) {
            amendBl.setInvoiceValue(goodsDto.getInvoiceValue());
            amendBl.setMarksNumbers(goodsDto.getMarksNumbers());
            amendBl.setFreightCharge(goodsDto.getFreightCharge());
            amendBl.setFreightCurrency(goodsDto.getFreightCurrency());
            amendBl.setInvoiceCurrency(goodsDto.getInvoiceCurrency());
            if (goodsDto.getDangerousGoodsInformation() != null) {
                amendBl.setImdgclass(goodsDto.getDangerousGoodsInformation().getImdgclass());
            }
        }
        amendBl.setPackingType(bl.getBlPackingType());
        amendBl.setOilType(blMeasurement.getOilType());
        amendBl.setLastUpdateId("TESWS");
        amendBl.setFirstRegisterId("TESWS");
        CommonOrdinalEntity commonOrdinalEntity = new CommonOrdinalEntity();
        DateFormat dT = new SimpleDateFormat("yyyy");
        String prefix = amendBl.getDeclatantTin() + dT.format(Calendar.getInstance().getTime()) + "M";
        Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
        if (optionalCommonOrdinalEntity.isPresent()) {
            commonOrdinalEntity = optionalCommonOrdinalEntity.get();
            commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo());
        }
        String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
        amendBl.setAmendSerialNumber(suffix);
        Optional<ExImportMasterBl> opt = exImportMasterBlRepository.
                findFirstByMrnAndMasterBillOfLading(manifestAmendmentDto.getMrn(), bl.getMasterBillOfLading());
        if (opt.isPresent()) {
            ExImportMasterBl masterBl = opt.get();
            amendBl.setFirstDestinationPlace(masterBl.getPlaceOfDestination());
        }
        exImportAmendBlRepository.save(amendBl);

    }

    private void saveAmendBl(BlDto bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendItem amendItem = new ExImportAmendItem();
        BlMeasurement blMeasurement = new BlMeasurement();
        CommonOrdinalEntity commonOrdinalEntity = new CommonOrdinalEntity();
        Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
        if (optional.isPresent()) {
            CoCompanyCodeEntity entity = optional.get();
            amendItem.setDeclarantTin(entity.getTin());
            Optional<CoCompanyCodeEntity> opt = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
            if (opt.isPresent()) {
                CoCompanyCodeEntity coCompanyCodeEntity = optional.get();
                String tin = coCompanyCodeEntity.getTin();
                DateFormat dT = new SimpleDateFormat("yyyy");
                String prefix = tin + dT.format(Calendar.getInstance().getTime()) + "M";
                Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
                if (optionalCommonOrdinalEntity.isPresent()) {
                    commonOrdinalEntity = optionalCommonOrdinalEntity.get();
                    commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo());
                }
                String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
                amendItem.setAmendSerialNumber(suffix);
                amendItem.setProcessType("M");
                DateFormat df = new SimpleDateFormat("yyyy");
                amendItem.setAmendYear(df.format(Calendar.getInstance().getTime()));
                amendItem.setLastUpdateId("TESWS");
                amendItem.setFirstRegisterId("TESWS");
            }

        }
        if(bl.getMasterBillOfLading()!=null && bl.getHouseBillOfLading()==null){
            Optional<ExImportMasterBl> opt=exImportMasterBlRepository.
                    findFirstByMrnAndMasterBillOfLading(manifestAmendmentDto.getMrn(), bl.getMasterBillOfLading());
            if(opt.isPresent()){
                ExImportMasterBl blItem = opt.get();
                if(!bl.getPlaceOfDelivery().equalsIgnoreCase(blItem.getPlaceOfDelivery())){
                    amendItem.setBeforeItemComments(blItem.getPlaceOfDelivery());
                    amendItem.setAfterItemComments(bl.getPlaceOfDelivery());
                    amendItem.setItemNumber("M33");
                }else if(!bl.getBlDescription().equalsIgnoreCase(blItem.getBlDescription())){
                    amendItem.setBeforeItemComments(blItem.getBlDescription());
                    amendItem.setAfterItemComments(bl.getBlDescription());
                    amendItem.setItemNumber("M22");
                }else if(!blMeasurement.getPkQuantity().equals(blItem.getBlPackage())){
                    amendItem.setBeforeItemComments(blItem.getBlPackage().toString());
                    amendItem.setAfterItemComments(blMeasurement.getPkQuantity().toString());
                    amendItem.setItemNumber("M23");
                }else if(!blMeasurement.getPkType().equalsIgnoreCase(blItem.getPackageUnit())){
                    amendItem.setBeforeItemComments(blItem.getPackageUnit());
                    amendItem.setAfterItemComments(blMeasurement.getPkType());
                    amendItem.setItemNumber("M24");
                }else if(!bl.getBlSummary().getBlGrossWeight().equals(blItem.getBlGrossWeight())){
                    amendItem.setBeforeItemComments(blItem.getBlGrossWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlGrossWeight().toString());
                    amendItem.setItemNumber("M25");
                }else if(!blMeasurement.getWeightUnit().equalsIgnoreCase(blItem.getGrossWeightUnit())){
                    amendItem.setBeforeItemComments(blItem.getGrossWeightUnit());
                    amendItem.setAfterItemComments(blMeasurement.getWeightUnit());
                    amendItem.setItemNumber("M26");
                }else if(!blMeasurement.getVolume().equals(blItem.getVolume())){
                    amendItem.setBeforeItemComments(blItem.getVolume().toString());
                    amendItem.setAfterItemComments(blMeasurement.getVolume().toString());
                    amendItem.setItemNumber("M29");
                }else if(!blMeasurement.getVolumeUnit().equalsIgnoreCase(blItem.getVolumeUnit())){
                    amendItem.setBeforeItemComments(blItem.getVolumeUnit());
                    amendItem.setAfterItemComments(blMeasurement.getVolumeUnit());
                    amendItem.setItemNumber("M30");
                }else if(!bl.getExporterName().equalsIgnoreCase(blItem.getExporterName())){
                    amendItem.setBeforeItemComments(blItem.getExporterName());
                    amendItem.setAfterItemComments(bl.getExporterName());
                    amendItem.setItemNumber("M11");
                }else if(!bl.getExporterTel().equalsIgnoreCase(blItem.getExporterTel())){
                    amendItem.setBeforeItemComments(blItem.getExporterTel());
                    amendItem.setAfterItemComments(bl.getExporterTel());
                    amendItem.setItemNumber("M12");
                }else if(!bl.getExporterAddress().equalsIgnoreCase(blItem.getExporterAddress())){
                    amendItem.setBeforeItemComments(blItem.getExporterAddress());
                    amendItem.setAfterItemComments(bl.getExporterAddress());
                    amendItem.setItemNumber("M13");
                }else if(!bl.getExporterTin().equalsIgnoreCase(blItem.getExporterTin())){
                    amendItem.setBeforeItemComments(blItem.getExporterTin());
                    amendItem.setAfterItemComments(bl.getExporterTin());
                    amendItem.setItemNumber("M10");
                }else if(!bl.getConsigneeName().equalsIgnoreCase(blItem.getConsigneeName())){
                    amendItem.setBeforeItemComments(blItem.getConsigneeName());
                    amendItem.setAfterItemComments(bl.getConsigneeName());
                    amendItem.setItemNumber("M15");
                }else if(!bl.getConsigneeTel().equalsIgnoreCase(blItem.getConsigneeTel())){
                    amendItem.setBeforeItemComments(blItem.getConsigneeTel());
                    amendItem.setAfterItemComments(bl.getConsigneeTel());
                    amendItem.setItemNumber("M16");
                }else if(!bl.getConsigneeAddress().equalsIgnoreCase(blItem.getConsigneeAddress())){
                    amendItem.setBeforeItemComments(blItem.getConsigneeAddress());
                    amendItem.setAfterItemComments(bl.getConsigneeAddress());
                    amendItem.setItemNumber("M17");
                }else if(!bl.getConsigneeTin().equalsIgnoreCase(blItem.getConsigneeTin())){
                    amendItem.setBeforeItemComments(blItem.getConsigneeTin());
                    amendItem.setAfterItemComments(bl.getConsigneeTin());
                    amendItem.setItemNumber("M14");
                }else if(!bl.getNotifyName().equalsIgnoreCase(blItem.getNotifyName())){
                    amendItem.setBeforeItemComments(blItem.getNotifyName());
                    amendItem.setAfterItemComments(bl.getNotifyName());
                    amendItem.setItemNumber("M19");
                }else if(!bl.getNotifyTel().equalsIgnoreCase(blItem.getNotifyTel())){
                    amendItem.setBeforeItemComments(blItem.getNotifyTel());
                    amendItem.setAfterItemComments(bl.getNotifyTel());
                    amendItem.setItemNumber("M20");
                }else if(!bl.getNotifyAddress().equalsIgnoreCase(blItem.getNotifyAddress())){
                    amendItem.setBeforeItemComments(blItem.getNotifyAddress());
                    amendItem.setAfterItemComments(bl.getNotifyAddress());
                    amendItem.setItemNumber("M21");
                }else if(!bl.getNotifyTin().equalsIgnoreCase(blItem.getNotifyTin())){
                    amendItem.setBeforeItemComments(blItem.getNotifyTin());
                    amendItem.setAfterItemComments(bl.getNotifyTin());
                    amendItem.setItemNumber("M18");
                }else if(!bl.getBlSummary().getNumberOfContainers().equals(blItem.getContainerCount())){
                    amendItem.setBeforeItemComments(blItem.getContainerCount().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getNumberOfContainers().toString());
                    amendItem.setItemNumber("M45");
                }else if(!bl.getForwarderTel().equalsIgnoreCase(blItem.getForwarderTel())){
                    amendItem.setBeforeItemComments(blItem.getForwarderTel());
                    amendItem.setAfterItemComments(bl.getForwarderTel());
                    amendItem.setItemNumber("M09");
                }else if(!bl.getForwarderName().equalsIgnoreCase(blItem.getForwarderName())){
                    amendItem.setBeforeItemComments(blItem.getForwarderName());
                    amendItem.setAfterItemComments(bl.getForwarderName());
                    amendItem.setItemNumber("M08");
                }else if(!bl.getForwarderCode().equalsIgnoreCase(blItem.getForwarderCode())){
                    amendItem.setBeforeItemComments(blItem.getForwarderCode());
                    amendItem.setAfterItemComments(bl.getForwarderCode());
                    amendItem.setItemNumber("M07");
                }else if(!bl.getShippingAgentCode().equalsIgnoreCase(blItem.getShippingAgentCode())){
                    amendItem.setBeforeItemComments(blItem.getShippingAgentCode());
                    amendItem.setAfterItemComments(bl.getShippingAgentCode());
                    amendItem.setItemNumber("M06");
                }else if(!bl.getBlType().substring(0,1).equalsIgnoreCase(blItem.getBlType())){
                    amendItem.setBeforeItemComments(blItem.getBlType());
                    amendItem.setAfterItemComments(bl.getBlType());
                    amendItem.setItemNumber("M05");
                }else if(!bl.getTradeType().substring(0,2).equalsIgnoreCase(blItem.getTradeType())){
                    amendItem.setBeforeItemComments(blItem.getTradeType());
                    amendItem.setAfterItemComments(bl.getTradeType());
                    amendItem.setItemNumber("M04");
                }else if(!bl.getMasterBillOfLading().equalsIgnoreCase(blItem.getMasterBillOfLading())){
                    amendItem.setBeforeItemComments(blItem.getMasterBillOfLading());
                    amendItem.setAfterItemComments(bl.getMasterBillOfLading());
                    amendItem.setItemNumber("M03");
                }else if(!bl.getPlaceOfDestination().equalsIgnoreCase(blItem.getPlaceOfDestination())){
                    amendItem.setBeforeItemComments(blItem.getPlaceOfDestination());
                    amendItem.setAfterItemComments(bl.getPlaceOfDestination());
                    amendItem.setItemNumber("M32");
                }else if(!bl.getPortOfLoading().equalsIgnoreCase(blItem.getPortOfLoading())){
                    amendItem.setBeforeItemComments(blItem.getPortOfLoading());
                    amendItem.setAfterItemComments(bl.getPortOfLoading());
                    amendItem.setItemNumber("M31");
                }else if(!blMeasurement.getNetWeight().equals(blItem.getBlNetWeight())){
                    amendItem.setBeforeItemComments(blItem.getBlNetWeight().toString());
                    amendItem.setAfterItemComments(blMeasurement.getNetWeight().toString());
                    amendItem.setItemNumber("M27");
                }else if(!blMeasurement.getOilType().equalsIgnoreCase(blItem.getOilType())){
                    amendItem.setBeforeItemComments(blItem.getOilType());
                    amendItem.setAfterItemComments(blMeasurement.getOilType());
                    amendItem.setItemNumber("M40");
                }else if(!bl.getBlPackingType().equalsIgnoreCase(blItem.getBlPackingType())){
                    amendItem.setBeforeItemComments(blItem.getBlPackingType());
                    amendItem.setAfterItemComments(bl.getBlPackingType());
                    amendItem.setItemNumber("M39");
                }
            }

        }else if(bl.getMasterBillOfLading()!=null && bl.getHouseBillOfLading()!=null && bl.getForwarderCode()!=null){
            Optional<ExImportHouseBl> option =exImportHouseBlRepository.
                    findByMrnAndMasterBillOfLadingAndHouseBillOfLading(manifestAmendmentDto.getMrn(),bl.getMasterBillOfLading(),bl.getHouseBillOfLading());
            if(option.isPresent()){
                ExImportHouseBl houseBlItem =option.get();
                if(!bl.getConsigneeAddress().equalsIgnoreCase(houseBlItem.getConsigneeAddress())){
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeAddress());
                    amendItem.setAfterItemComments(bl.getConsigneeAddress());
                    amendItem.setItemNumber("H14");
                }else if(!bl.getNotifyTin().equalsIgnoreCase(houseBlItem.getNotifyTin())){
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyTin());
                    amendItem.setAfterItemComments(bl.getNotifyTin());
                    amendItem.setItemNumber("H15");
                }else if(!bl.getNotifyName().equalsIgnoreCase(houseBlItem.getNotifyName())){
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyName());
                    amendItem.setAfterItemComments(bl.getNotifyName());
                    amendItem.setItemNumber("H16");
                }else if(!bl.getNotifyTel().equalsIgnoreCase(houseBlItem.getNotifyTel())){
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyTel());
                    amendItem.setAfterItemComments(bl.getNotifyTel());
                    amendItem.setItemNumber("H17");
                }else if(!bl.getNotifyAddress().equalsIgnoreCase(houseBlItem.getNotifyAddress())){
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyAddress());
                    amendItem.setAfterItemComments(bl.getNotifyAddress());
                    amendItem.setItemNumber("H18");
                }else if(!bl.getBlDescription().equalsIgnoreCase(houseBlItem.getDescription())){
                    amendItem.setBeforeItemComments(houseBlItem.getDescription());
                    amendItem.setAfterItemComments(bl.getBlDescription());
                    amendItem.setItemNumber("H19");
                }else if(!blMeasurement.getPkQuantity().equals(houseBlItem.getBlPackage())){
                    amendItem.setBeforeItemComments(houseBlItem.getBlPackage().toString());
                    amendItem.setAfterItemComments(blMeasurement.getPkQuantity().toString());
                    amendItem.setItemNumber("H20");
                }else if(!blMeasurement.getPkType().equalsIgnoreCase(houseBlItem.getPackageUnit())){
                    amendItem.setBeforeItemComments(houseBlItem.getPackageUnit());
                    amendItem.setAfterItemComments(blMeasurement.getPkType());
                    amendItem.setItemNumber("H21");
                }else if(!bl.getBlSummary().getBlGrossWeight().equals(houseBlItem.getBlGrossWeight())){
                    amendItem.setBeforeItemComments(houseBlItem.getBlGrossWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlGrossWeight().toString());
                    amendItem.setItemNumber("H22");
                }else if(!blMeasurement.getWeightUnit().equalsIgnoreCase(houseBlItem.getGrossWeightUnit())){
                    amendItem.setBeforeItemComments(houseBlItem.getGrossWeightUnit());
                    amendItem.setAfterItemComments(blMeasurement.getWeightUnit());
                    amendItem.setItemNumber("H23");
                }else if(!bl.getBlSummary().getBlNetWeight().equals(houseBlItem.getBlNetWeight())){
                    amendItem.setBeforeItemComments(houseBlItem.getBlNetWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlNetWeight().toString());
                    amendItem.setItemNumber("H24");
                }else if(!blMeasurement.getVolume().equals(houseBlItem.getVolume())){
                    amendItem.setBeforeItemComments(houseBlItem.getVolume().toString());
                    amendItem.setAfterItemComments(blMeasurement.getVolume().toString());
                    amendItem.setItemNumber("H26");
                }else if(!blMeasurement.getVolumeUnit().equalsIgnoreCase(houseBlItem.getVolumeUnit())){
                    amendItem.setBeforeItemComments(houseBlItem.getVolumeUnit());
                    amendItem.setAfterItemComments(blMeasurement.getVolumeUnit());
                    amendItem.setItemNumber("H27");
                }else if(!bl.getPlaceOfDestination().equalsIgnoreCase(houseBlItem.getPlaceOfDestination())){
                    amendItem.setBeforeItemComments(houseBlItem.getPlaceOfDestination());
                    amendItem.setAfterItemComments(bl.getPlaceOfDestination());
                    amendItem.setItemNumber("H28");
                }else if(!bl.getConsigneeTel().equalsIgnoreCase(houseBlItem.getConsigneeTel())){
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeTel());
                    amendItem.setAfterItemComments(bl.getConsigneeTel());
                    amendItem.setItemNumber("H13");
                }else if(!bl.getConsigneeName().equalsIgnoreCase(houseBlItem.getConsigneeName())){
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeName());
                    amendItem.setAfterItemComments(bl.getConsigneeName());
                    amendItem.setItemNumber("H12");
                }else if(!bl.getConsigneeTin().equalsIgnoreCase(houseBlItem.getConsigneeTin())){
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeTin());
                    amendItem.setAfterItemComments(bl.getConsigneeTin());
                    amendItem.setItemNumber("H11");
                }else if(!bl.getExporterAddress().equalsIgnoreCase(houseBlItem.getExporterAddress())){
                    amendItem.setBeforeItemComments(houseBlItem.getExporterAddress());
                    amendItem.setAfterItemComments(bl.getExporterAddress());
                    amendItem.setItemNumber("H10");
                }else if(!bl.getExporterTel().equalsIgnoreCase(houseBlItem.getExporterTel())){
                    amendItem.setBeforeItemComments(houseBlItem.getExporterTel());
                    amendItem.setAfterItemComments(bl.getExporterTel());
                    amendItem.setItemNumber("H09");
                }else if(!bl.getExporterName().equalsIgnoreCase(houseBlItem.getExporterName())){
                    amendItem.setBeforeItemComments(houseBlItem.getExporterName());
                    amendItem.setAfterItemComments(bl.getExporterName());
                    amendItem.setItemNumber("H08");
                }else if(!bl.getExporterTin().equalsIgnoreCase(houseBlItem.getExporterTin())){
                    amendItem.setBeforeItemComments(houseBlItem.getExporterTin());
                    amendItem.setAfterItemComments(bl.getExporterTin());
                    amendItem.setItemNumber("H07");
                }else if(!bl.getTradeType().substring(0,2).equalsIgnoreCase(houseBlItem.getTradeType())){
                    amendItem.setBeforeItemComments(houseBlItem.getTradeType());
                    amendItem.setAfterItemComments(bl.getTradeType());
                    amendItem.setItemNumber("H06");
                }else if(!bl.getBlPackingType().equalsIgnoreCase(houseBlItem.getBlPackingType())){
                    amendItem.setBeforeItemComments(houseBlItem.getBlPackingType());
                    amendItem.setAfterItemComments(bl.getBlPackingType());
                    amendItem.setItemNumber("H34");
                }

            }
        }
        importAmendItemRepository.save(amendItem);
    }
    private void saveAmendedContainerDetail(List<Containers> containers, BlDto bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendItemContainer itemContainer = new ExImportAmendItemContainer();
        BlMeasurement blMeasurement = new BlMeasurement();
        for (Containers container : containers) {
            Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
            if (optional.isPresent()) {
                CoCompanyCodeEntity entity = optional.get();
                itemContainer.setDeclarantTin(entity.getTin());
            }
            DateFormat df = new SimpleDateFormat("yyyy");
            itemContainer.setAmendYear(df.format(Calendar.getInstance().getTime()));
            itemContainer.setProcessType("M");
            CommonOrdinalEntity commonOrdinalEntity = new CommonOrdinalEntity();
            DateFormat dT = new SimpleDateFormat("yyyy");
            String prefix =itemContainer.getDeclarantTin()+ dT.format(Calendar.getInstance().getTime()) + "M";
            Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
            if (optionalCommonOrdinalEntity.isPresent()) {
                commonOrdinalEntity = optionalCommonOrdinalEntity.get();
                commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo());
            }
            String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
            itemContainer.setAmendSerialNumber(suffix);
            itemContainer.setItemNumber("C00");
            itemContainer.setContainerNumber(container.getContainerNo());
            itemContainer.setContainerSize(container.getContainerSize());
            if(itemContainer.getContainerType().startsWith("C")){
                itemContainer.setContainerType("C");
            }else if(itemContainer.getContainerType().startsWith("V")){
                itemContainer.setContainerType("V");
            }
            itemContainer.setContainerWeight(container.getWeight());
            itemContainer.setWeightUnit(container.getWeightUnit());
            itemContainer.setContainerVolume(container.getVolume());
            itemContainer.setVolumeUnit(container.getVolumeUnit());
            itemContainer.setFreightIndicator(container.getFreightIndicator());
            itemContainer.setMaximumTemperature(container.getMaximumTemperature());
            itemContainer.setMinimumTemperature(container.getMinimumTemperature());
            itemContainer.setImdgCode(blMeasurement.getImdgClass());
            itemContainer.setFirstRegisterId("TESWS");
            itemContainer.setLastUpdateId("TESWS");
            Optional<ExImportBlContainer> option=exImportBlContainerRepository.
                    findByMrnAndMasterBillOfLading(manifestAmendmentDto.getMrn(), bl.getMasterBillOfLading());
            if(option.isPresent()){
                ExImportBlContainer blContainer= option.get();
                itemContainer.setOldContainerNumber(blContainer.getContainerNo());
            }

            if(container.getTemperatureType() != null) {
                itemContainer.setReferPlugYn(container.getTemperatureType().contentEquals("1")?"Y":"N");
            }
            int i = 0;
            int l = 0;
            if (container.getSealNumbers() != null && !container.getSealNumbers().isEmpty()) {
                for (SealNumberDto sealNumber : container.getSealNumbers()) {
                    if (sealNumber.getSealNumberIssuerType() != null
                            && sealNumber.getSealNumberIssuerType().contentEquals("CU")) {
                        if (i == 0) {
                            itemContainer.setCustomSealNumberOne(sealNumber.getSealNumber());
                            i++;
                        } else if (i == 1) {
                            itemContainer.setCustomSealNumberTwo(sealNumber.getSealNumber());
                            i++;
                        } else if (i == 2) {
                            itemContainer.setCustomSealNumberThree(sealNumber.getSealNumber());
                            i++;
                        }
                    } else {
                        if (l == 0) {
                            itemContainer.setSealNumberOne(sealNumber.getSealNumber());
                            l++;
                        } else if (l == 1) {
                            itemContainer.setSealNumberTwo(sealNumber.getSealNumber());
                            l++;
                        } else if (l == 2) {
                            itemContainer.setSealNumberThree(sealNumber.getSealNumber());
                            l++;
                        }
                    }

                }
            }

        }
        amendItemContainerRepository.save(itemContainer);
    }

    private void saveContainers(List<Containers> containers, BlDto bl) {
        for (Containers container : containers) {
            ExImportAmendBlContainer cn = new ExImportAmendBlContainer();
            Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
            if (optional.isPresent()) {
                CoCompanyCodeEntity entity = optional.get();
                cn.setDeclarantTin(entity.getTin());
            }
            cn.setContainerNo(container.getContainerNo());
            cn.setProcessType("M");
            DateFormat df = new SimpleDateFormat("yyyy");
            cn.setAmendYear(df.format(Calendar.getInstance().getTime()));
            CommonOrdinalEntity commonOrdinalEntity = new CommonOrdinalEntity();
            String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
            cn.setAmendSerialNumber(suffix);
            cn.setContainerSize(container.getContainerSize());
            cn.setTypeOfContainer("C");
            if (bl.getHouseBillOfLading() != null) {
                cn.setBillOfLading(bl.getHouseBillOfLading());
            } else {
                cn.setBillOfLading(bl.getMasterBillOfLading());
            }
            cn.setFirstRegisterId("TESWS");
            cn.setLastUpdateId("TESWS");
            cn.setFreightIndicator(container.getFreightIndicator());
            cn.setMaximumTemperature(container.getMaximumTemperature());
            cn.setMinimumTemperature(container.getMinimumTemperature());
            int i = 0;
            int l = 0;
            if (container.getSealNumbers() != null && !container.getSealNumbers().isEmpty()) {
                for (SealNumberDto sealNumber : container.getSealNumbers()) {
                    if (sealNumber.getSealNumberIssuerType() != null
                            && sealNumber.getSealNumberIssuerType().contentEquals("CU")) {
                        if (i == 0) {
                            cn.setCustomSealNumberOne(sealNumber.getSealNumber());
                            i++;
                        } else if (i == 1) {
                            cn.setCustomSealNumberTwo(sealNumber.getSealNumber());
                            i++;
                        } else if (i == 2) {
                            cn.setCustomSealNumberThree(sealNumber.getSealNumber());
                            i++;
                        }
                    } else {
                        if (l == 0) {
                            cn.setSealNumberOne(sealNumber.getSealNumber());
                            l++;
                        } else if (l == 1) {
                            cn.setSealNumberTwo(sealNumber.getSealNumber());
                            l++;
                        } else if (l == 2) {
                            cn.setSealNumberThree(sealNumber.getSealNumber());
                            l++;
                        }
                    }

                }
            }

        }
    }


    private String getTradeType(BlDto bl) {
        String tradeType = bl.getTradeType();
        if (tradeType != null && tradeType.equalsIgnoreCase("IMPORT")) {
            return "IM";
        } else if (tradeType != null && tradeType.equalsIgnoreCase("TRANSIT")) {
            return "TR";
        } else if (tradeType != null && tradeType.equalsIgnoreCase("TRANSSHIPMENT")) {
            return "TS";
        } else if (tradeType != null && tradeType.equalsIgnoreCase("EXPRESS")) {
            return "XE";
        } else if (tradeType != null && tradeType.equalsIgnoreCase("EXPORT")) {
            return "EX";

        } else {
            return bl.getTradeType();
        }
    }

    private void createEdNotice(ManifestAmendmentDto manifestAmendmentDto, BlDto bl) {
        EdNoticeEntity edNotice = new EdNoticeEntity();
        Optional<InImportManifest> optional = inImportManifestRepository.findFirstByCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        if (optional.isPresent()) {
            InImportManifest infEntity = optional.get();
            edNotice.setCustomsOfficeCode(infEntity.getCustomOfficeCode());
            edNotice.setDocumentCode("IMFMOD215");
            Optional<CoCompanyCodeEntity> option = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
            if (option.isPresent()) {
                CoCompanyCodeEntity entity = option.get();
                String tin = entity.getTin();
                edNotice.setDocumentNumber(generatedDocumentNo(tin));
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String strDate = dtf.format(now);
            edNotice.setCreateDate(strDate);
            edNotice.setSequenceNumber(1);
            edNotice.setDocumentFunctionType("9");
            edNotice.setReceiverId("INTERNAL");
            edNotice.setSenderId("EXTERNAL");
            edNotice.setDocumentType("D");
            edNotice.setOriginalDocumentCode("IMFMOD215");
            edNotice.setOriginalDocumentNumber(edNotice.getDocumentNumber());
            edNotice.setTransferType("E");
            edNotice.setProcessingStatus("N");
            edNoticeRepository.save(edNotice);
        } else {
            System.out.println("Manifest Amendment with This Mrn is not present in TANCIS");
        }



    }

    private String generatedDocumentNo(String tin) {
        CommonOrdinalEntity commonOrdinalEntity = new CommonOrdinalEntity();
            DateFormat df = new SimpleDateFormat("yyyy");
            String prefix = tin + df.format(Calendar.getInstance().getTime()) + "M";
            Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
            if (optionalCommonOrdinalEntity.isPresent()) {
                commonOrdinalEntity = optionalCommonOrdinalEntity.get();
                commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo());
            }
            String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
            return prefix + suffix;
        }

    }




