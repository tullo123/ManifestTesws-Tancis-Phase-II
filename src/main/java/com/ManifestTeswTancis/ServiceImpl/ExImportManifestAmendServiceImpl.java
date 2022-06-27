package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Service.ExImportManifestAmendService;
import com.ManifestTeswTancis.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExImportManifestAmendServiceImpl implements ExImportManifestAmendService {
    final AmendItemContainerRepository amendItemContainerRepository;
    final MasterBlGoodItemsRepository masterBlGoodItemsRepository;
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
    final ExImportAmendBlContainerRepository exImportAmendBlContainerRepository;

    @Autowired
    public ExImportManifestAmendServiceImpl(AmendItemContainerRepository amendItemContainerRepository, MasterBlGoodItemsRepository masterBlGoodItemsRepository, EdNoticeRepository edNoticeRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository, ExImportAmendItemRepository importAmendItemRepository, ExImportAmendBlRepository exImportAmendBlRepository, ExImportManifestRepository exImportManifestRepository, CoCompanyCodeRepository coCompanyCodeRepository, CommonOrdinalRepository commonOrdinalRepository, InImportManifestRepository inImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, ExImportBlContainerRepository exImportBlContainerRepository, ExImportHouseBlRepository exImportHouseBlRepository, ExImportAmendBlContainerRepository exImportAmendBlContainerRepository) {
        this.amendItemContainerRepository = amendItemContainerRepository;
        this.masterBlGoodItemsRepository = masterBlGoodItemsRepository;
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
        this.exImportAmendBlContainerRepository = exImportAmendBlContainerRepository;
    }

    @Override
    @Transactional
    public TeswsResponse amendManifest(ManifestAmendmentDto manifestAmendmentDto) {
        TeswsResponse responseData = new TeswsResponse();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        responseData.setRefId(manifestAmendmentDto.getAmendmentReference());
        responseData.setAckDate(localDateTime.format(formatter));
        responseData.setAckType("MANIFEST_AMENDMENT");
        try {
            Optional<ExImportManifest> optional = exImportManifestRepository.findFirstByMrn(manifestAmendmentDto.getMrn());
            if (optional.isPresent()) {
                Optional<ManifestAmendmentApprovalStatus> opt = manifestAmendmentApprovalStatusRepository.findByAmendReference(manifestAmendmentDto.getAmendmentReference());
                if (!opt.isPresent()) {
                    BlDto bl = manifestAmendmentDto.getBl();
                    List<Containers> containers = manifestAmendmentDto.getContainers();
                    saveGeneralAmendment(bl, manifestAmendmentDto);
                    if (manifestAmendmentDto.getBl() != null && manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_BL")) {
                        saveAmendBl(bl, manifestAmendmentDto);
                    } else if (manifestAmendmentDto.getBl() != null && manifestAmendmentDto.getAmendType().equalsIgnoreCase("ADD_BL")) {
                        addBl(bl, manifestAmendmentDto);
                    }
                    if (!manifestAmendmentDto.getContainers().isEmpty() && manifestAmendmentDto.getAmendType().
                            equalsIgnoreCase("AMEND_CONTAINER")) {
                        saveAmendedContainerDetail(containers, bl, manifestAmendmentDto);
                    } else if (!manifestAmendmentDto.getContainers().isEmpty()) {
                        saveAddedContainer(containers, bl);
                    }
                    this.createEdNotice(manifestAmendmentDto, bl);
                }
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
            if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null
                    && manifestAmendmentDto.getModifier().equalsIgnoreCase("FF")) {
                amendGeneral.setAmendType("HA");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode() == null
                    && manifestAmendmentDto.getModifier().equalsIgnoreCase("SA")) {
                amendGeneral.setAmendType("MA");
            }
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_BL")) {
            if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("FF")) {
                amendGeneral.setAmendType("HM");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode() == null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("SA")) {
                amendGeneral.setAmendType("MM");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode()!= null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("SA") && bl.getBlType().equalsIgnoreCase("CONSOLIDATED")) {
                amendGeneral.setAmendType("MM");
            } else if  (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("FF") && bl.getBlType().equalsIgnoreCase("CONSOLIDATED")) {
                amendGeneral.setAmendType("HM");
            }  else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode()== null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("SA") && bl.getBlType().equalsIgnoreCase("SIMPLE")) {
                amendGeneral.setAmendType("MM");
            }

        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("DELETE_BL")) {
            if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() != null && bl.getForwarderCode() != null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("FF") && bl.getBlType().equalsIgnoreCase("CONSOLIDATED")) {
                amendGeneral.setAmendType("HD");
            } else if (bl.getMasterBillOfLading() != null && bl.getHouseBillOfLading() == null && bl.getForwarderCode() == null &&
                    manifestAmendmentDto.getModifier().equalsIgnoreCase("SA")) {
                amendGeneral.setAmendType("MD");
            }
        } else if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_BL") && bl.getPlaceOfDelivery() != null) {
            amendGeneral.setAmendType("TI");
        }
        if (manifestAmendmentDto.getAmendType().equalsIgnoreCase("ADD_CONTAINER")
                || manifestAmendmentDto.getAmendType().equalsIgnoreCase("AMEND_CONTAINER")
                || manifestAmendmentDto.getAmendType().equalsIgnoreCase("DELETE_CONTAINER")) {
            amendGeneral.setAmendType("CM");
        }

        amendGeneral.setAmendReasonCode("01");
        amendGeneral.setAmendReasonComment("Simple Error");
        amendGeneral.setDeclarantCode(bl.getShippingAgentCode());
        amendGeneral.setDeclarantName(bl.getShippingAgentName());
        amendGeneral.setFirstRegisterId("TESWS");
        amendGeneral.setLastUpdateId("TESWS");
        amendGeneral.setProcessingDate(new java.sql.Date(System.currentTimeMillis()));

        exImportAmendGeneralRepository.save(amendGeneral);

        ManifestAmendmentApprovalStatus manifestAmendmentApprovalStatus= new ManifestAmendmentApprovalStatus(manifestAmendmentDto);
        manifestAmendmentApprovalStatus.setCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        manifestAmendmentApprovalStatus.setMrn(manifestAmendmentDto.getMrn());
        manifestAmendmentApprovalStatus.setAmendReference(manifestAmendmentDto.getAmendmentReference());
        manifestAmendmentApprovalStatus.setAmendType(manifestAmendmentDto.getAmendType());
        manifestAmendmentApprovalStatus.setAmendSerialNo(amendGeneral.getAmendSerialNumber());
        manifestAmendmentApprovalStatus.setDeclarantTin(amendGeneral.getDeclarantTin());
        manifestAmendmentApprovalStatus.setAmendYear(amendGeneral.getAmendYear());
        manifestAmendmentApprovalStatusRepository.save(manifestAmendmentApprovalStatus);

    }

    private void addBl(BlDto bl, ManifestAmendmentDto manifestAmendmentDto) {
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
        if(bl.getBlType().equalsIgnoreCase("SIMPLE")){
            amendBl.setBlType("S");
        } else if(bl.getBlType().equalsIgnoreCase("CONSOLIDATED")){
            amendBl.setBlType("C");
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
        ExImportAmendItem amendItem ;
        List<ExImportAmendItem>  amendItems = new ArrayList<>();
        BlMeasurement blMeasurement = new BlMeasurement();
        CommonOrdinalEntity commonOrdinalEntity = new CommonOrdinalEntity();
        String declarantTin = null;
        String amendSerialNumber = null;
        String amendYear= null;
        Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
        if (optional.isPresent()) {
            CoCompanyCodeEntity entity = optional.get();
            declarantTin = entity.getTin();
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
                amendSerialNumber = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
                DateFormat df = new SimpleDateFormat("yyyy");
                amendYear=df.format(Calendar.getInstance().getTime());
            }

        }
        if(bl.getMasterBillOfLading()!=null && bl.getHouseBillOfLading()==null){
            Optional<ExImportMasterBl> opt=exImportMasterBlRepository.
                    findFirstByMrnAndMasterBillOfLading(manifestAmendmentDto.getMrn(), bl.getMasterBillOfLading());
            if(opt.isPresent()){
                ExImportMasterBl blItem = opt.get();
                if(bl.getPlaceOfDelivery()!=null && !bl.getPlaceOfDelivery().equalsIgnoreCase(blItem.getPlaceOfDelivery())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getPlaceOfDelivery());
                    amendItem.setAfterItemComments(bl.getPlaceOfDelivery());
                    amendItem.setItemNumber("M33");
                    amendItems.add(amendItem);
                }
                if(bl.getBlDescription()!=null && !bl.getBlDescription().equalsIgnoreCase(blItem.getBlDescription())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getBlDescription());
                    amendItem.setAfterItemComments(bl.getBlDescription());
                    amendItem.setItemNumber("M22");
                    amendItems.add(amendItem);
                }
                if(bl.getBlSummary()!=null  && bl.getBlSummary().getTotalBlPackage()!=null && !bl.getBlSummary().getTotalBlPackage().equals(blItem.getBlPackage()) ){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getBlPackage().toString());
                    amendItem.setAfterItemComments(blMeasurement.getPkQuantity().toString());
                    amendItem.setItemNumber("M23");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getPkType()!=null && !blMeasurement.getPkType().equalsIgnoreCase(blItem.getPackageUnit())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getPackageUnit());
                    amendItem.setAfterItemComments(blMeasurement.getPkType());
                    amendItem.setItemNumber("M24");
                    amendItems.add(amendItem);
                }
                if(bl.getBlSummary()!=null && bl.getBlSummary().getBlGrossWeight()!=null && !bl.getBlSummary().getBlGrossWeight().equals(blItem.getBlGrossWeight())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getBlGrossWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlGrossWeight().toString());
                    amendItem.setItemNumber("M25");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getWeightUnit()!=null && !blMeasurement.getWeightUnit().equalsIgnoreCase(blItem.getGrossWeightUnit())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getGrossWeightUnit());
                    amendItem.setAfterItemComments(blMeasurement.getWeightUnit());
                    amendItem.setItemNumber("M26");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getVolume()!=null && !blMeasurement.getVolume().equals(blItem.getVolume())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getVolume().toString());
                    amendItem.setAfterItemComments(blMeasurement.getVolume().toString());
                    amendItem.setItemNumber("M29");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getVolumeUnit()!=null && !blMeasurement.getVolumeUnit().equalsIgnoreCase(blItem.getVolumeUnit())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getVolumeUnit());
                    amendItem.setAfterItemComments(blMeasurement.getVolumeUnit());
                    amendItem.setItemNumber("M30");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterName()!=null && !bl.getExporterName().equalsIgnoreCase(blItem.getExporterName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getExporterName());
                    amendItem.setAfterItemComments(bl.getExporterName());
                    amendItem.setItemNumber("M11");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterTel()!=null && !bl.getExporterTel().equalsIgnoreCase(blItem.getExporterTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getExporterTel());
                    amendItem.setAfterItemComments(bl.getExporterTel());
                    amendItem.setItemNumber("M12");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterAddress()!=null && !bl.getExporterAddress().equalsIgnoreCase(blItem.getExporterAddress())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getExporterAddress());
                    amendItem.setAfterItemComments(bl.getExporterAddress());
                    amendItem.setItemNumber("M13");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterTin()!=null && !bl.getExporterTin().equalsIgnoreCase(blItem.getExporterTin())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getExporterTin());
                    amendItem.setAfterItemComments(bl.getExporterTin());
                    amendItem.setItemNumber("M10");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeName()!=null && !bl.getConsigneeName().equalsIgnoreCase(blItem.getConsigneeName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getConsigneeName());
                    amendItem.setAfterItemComments(bl.getConsigneeName());
                    amendItem.setItemNumber("M15");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeTel()!=null && !bl.getConsigneeTel().equalsIgnoreCase(blItem.getConsigneeTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getConsigneeTel());
                    amendItem.setAfterItemComments(bl.getConsigneeTel());
                    amendItem.setItemNumber("M16");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeAddress()!=null && !bl.getConsigneeAddress().equalsIgnoreCase(blItem.getConsigneeAddress())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getConsigneeAddress());
                    amendItem.setAfterItemComments(bl.getConsigneeAddress());
                    amendItem.setItemNumber("M17");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeTin()!=null && !bl.getConsigneeTin().equalsIgnoreCase(blItem.getConsigneeTin())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getConsigneeTin());
                    amendItem.setAfterItemComments(bl.getConsigneeTin());
                    amendItem.setItemNumber("M14");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyName()!=null && !bl.getNotifyName().equalsIgnoreCase(blItem.getNotifyName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getNotifyName());
                    amendItem.setAfterItemComments(bl.getNotifyName());
                    amendItem.setItemNumber("M19");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyTel()!=null && !bl.getNotifyTel().equalsIgnoreCase(blItem.getNotifyTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getNotifyTel());
                    amendItem.setAfterItemComments(bl.getNotifyTel());
                    amendItem.setItemNumber("M20");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyAddress()!=null && !bl.getNotifyAddress().equalsIgnoreCase(blItem.getNotifyAddress())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getNotifyAddress());
                    amendItem.setAfterItemComments(bl.getNotifyAddress());
                    amendItem.setItemNumber("M21");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyTin()!=null && !bl.getNotifyTin().equalsIgnoreCase(blItem.getNotifyTin())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getNotifyTin());
                    amendItem.setAfterItemComments(bl.getNotifyTin());
                    amendItem.setItemNumber("M18");
                    amendItems.add(amendItem);
                }
                if(bl.getBlSummary()!=null && bl.getBlSummary().getNumberOfContainers()!=null && !bl.getBlSummary().getNumberOfContainers().equals(blItem.getContainerCount())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getContainerCount().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getNumberOfContainers().toString());
                    amendItem.setItemNumber("M45");
                    amendItems.add(amendItem);
                }
                if(bl.getForwarderTel()!=null && !bl.getForwarderTel().equalsIgnoreCase(blItem.getForwarderTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getForwarderTel());
                    amendItem.setAfterItemComments(bl.getForwarderTel());
                    amendItem.setItemNumber("M09");
                    amendItems.add(amendItem);
                }
                if(bl.getForwarderName()!=null && !bl.getForwarderName().equalsIgnoreCase(blItem.getForwarderName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getForwarderName());
                    amendItem.setAfterItemComments(bl.getForwarderName());
                    amendItem.setItemNumber("M08");
                    amendItems.add(amendItem);
                }
                if(bl.getForwarderCode()!=null && !bl.getForwarderCode().equalsIgnoreCase(blItem.getForwarderCode())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getForwarderCode());
                    amendItem.setAfterItemComments(bl.getForwarderCode());
                    amendItem.setItemNumber("M07");
                    amendItems.add(amendItem);
                }
                if(bl.getShippingAgentCode()!=null && !bl.getShippingAgentCode().equalsIgnoreCase(blItem.getShippingAgentCode())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getShippingAgentCode());
                    amendItem.setAfterItemComments(bl.getShippingAgentCode());
                    amendItem.setItemNumber("M06");
                    amendItems.add(amendItem);
                }
                if(bl.getBlType()!=null && !bl.getBlType().substring(0,1).equalsIgnoreCase(blItem.getBlType())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getBlType());
                    amendItem.setAfterItemComments(bl.getBlType().substring(0,1));
                    amendItem.setItemNumber("M05");
                    amendItems.add(amendItem);
                }
                if(bl.getTradeType()!=null && !bl.getTradeType().substring(0,2).equalsIgnoreCase(blItem.getTradeType())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getTradeType());
                    amendItem.setAfterItemComments(bl.getTradeType().substring(0,2));
                    amendItem.setItemNumber("M04");
                    amendItems.add(amendItem);
                }
                if(bl.getMasterBillOfLading()!=null && !bl.getMasterBillOfLading().equalsIgnoreCase(blItem.getMasterBillOfLading())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getMasterBillOfLading());
                    amendItem.setAfterItemComments(bl.getMasterBillOfLading());
                    amendItem.setItemNumber("M03");
                    amendItems.add(amendItem);
                }
                if(bl.getPlaceOfDestination()!=null && !bl.getPlaceOfDestination().equalsIgnoreCase(blItem.getPlaceOfDestination())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getPlaceOfDestination());
                    amendItem.setAfterItemComments(bl.getPlaceOfDestination());
                    amendItem.setItemNumber("M32");
                    amendItems.add(amendItem);
                }
                if(bl.getPortOfLoading()!=null && !bl.getPortOfLoading().equalsIgnoreCase(blItem.getPortOfLoading())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getPortOfLoading());
                    amendItem.setAfterItemComments(bl.getPortOfLoading());
                    amendItem.setItemNumber("M31");
                    amendItems.add(amendItem);
                }
                if(bl.getBlSummary()!=null && bl.getBlSummary().getBlNetWeight()!=null && !bl.getBlSummary().getBlNetWeight().equals(blItem.getBlGrossWeight())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getBlNetWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlNetWeight().toString());
                    amendItem.setItemNumber("M27");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getOilType()!=null && !blMeasurement.getOilType().equalsIgnoreCase(blItem.getOilType())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getOilType());
                    amendItem.setAfterItemComments(blMeasurement.getOilType());
                    amendItem.setItemNumber("M40");
                    amendItems.add(amendItem);
                }
                if(bl.getBlPackingType()!=null && !bl.getBlPackingType().equalsIgnoreCase(blItem.getBlPackingType())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(blItem.getBlPackingType());
                    amendItem.setAfterItemComments(bl.getBlPackingType());
                    amendItem.setItemNumber("M39");
                    amendItems.add(amendItem);
                }
            }

        }else if(bl.getMasterBillOfLading()!=null && bl.getHouseBillOfLading()!=null && bl.getForwarderCode()!=null){
            Optional<ExImportHouseBl> option =exImportHouseBlRepository.
                    findByMrnAndMasterBillOfLadingAndHouseBillOfLading(manifestAmendmentDto.getMrn(),bl.getMasterBillOfLading(),bl.getHouseBillOfLading());
            if(option.isPresent()){
                ExImportHouseBl houseBlItem =option.get();
                if(bl.getConsigneeAddress()!=null && !bl.getConsigneeAddress().equalsIgnoreCase(houseBlItem.getConsigneeAddress())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeAddress());
                    amendItem.setAfterItemComments(bl.getConsigneeAddress());
                    amendItem.setItemNumber("H14");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyTin()!=null && !bl.getNotifyTin().equalsIgnoreCase(houseBlItem.getNotifyTin())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyTin());
                    amendItem.setAfterItemComments(bl.getNotifyTin());
                    amendItem.setItemNumber("H15");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyName()!=null && !bl.getNotifyName().equalsIgnoreCase(houseBlItem.getNotifyName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyName());
                    amendItem.setAfterItemComments(bl.getNotifyName());
                    amendItem.setItemNumber("H16");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyTel()!=null && !bl.getNotifyTel().equalsIgnoreCase(houseBlItem.getNotifyTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyTel());
                    amendItem.setAfterItemComments(bl.getNotifyTel());
                    amendItem.setItemNumber("H17");
                    amendItems.add(amendItem);
                }
                if(bl.getNotifyAddress()!=null && !bl.getNotifyAddress().equalsIgnoreCase(houseBlItem.getNotifyAddress())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getNotifyAddress());
                    amendItem.setAfterItemComments(bl.getNotifyAddress());
                    amendItem.setItemNumber("H18");
                    amendItems.add(amendItem);
                }
                if(bl.getBlDescription()!=null && !bl.getBlDescription().equalsIgnoreCase(houseBlItem.getDescription())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getDescription());
                    amendItem.setAfterItemComments(bl.getBlDescription());
                    amendItem.setItemNumber("H19");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getPkQuantity()!=null && !blMeasurement.getPkQuantity().equals(houseBlItem.getBlPackage())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getBlPackage().toString());
                    amendItem.setAfterItemComments(blMeasurement.getPkQuantity().toString());
                    amendItem.setItemNumber("H20");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getPkType()!=null && !blMeasurement.getPkType().equalsIgnoreCase(houseBlItem.getPackageUnit())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getPackageUnit());
                    amendItem.setAfterItemComments(blMeasurement.getPkType());
                    amendItem.setItemNumber("H21");
                    amendItems.add(amendItem);
                }
                if(bl.getBlSummary()!=null && bl.getBlSummary().getBlGrossWeight()!=null && !bl.getBlSummary().getBlGrossWeight().equals(houseBlItem.getBlGrossWeight())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getBlGrossWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlGrossWeight().toString());
                    amendItem.setItemNumber("H22");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getWeightUnit()!=null && !blMeasurement.getWeightUnit().equalsIgnoreCase(houseBlItem.getGrossWeightUnit())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getGrossWeightUnit());
                    amendItem.setAfterItemComments(blMeasurement.getWeightUnit());
                    amendItem.setItemNumber("H23");
                    amendItems.add(amendItem);
                }
                if(bl.getBlSummary()!=null && bl.getBlSummary().getBlNetWeight()!=null && !bl.getBlSummary().getBlNetWeight().equals(houseBlItem.getBlNetWeight())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getBlNetWeight().toString());
                    amendItem.setAfterItemComments(bl.getBlSummary().getBlNetWeight().toString());
                    amendItem.setItemNumber("H24");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getVolume()!=null && !blMeasurement.getVolume().equals(houseBlItem.getVolume())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getVolume().toString());
                    amendItem.setAfterItemComments(blMeasurement.getVolume().toString());
                    amendItem.setItemNumber("H26");
                    amendItems.add(amendItem);
                }
                if(blMeasurement.getVolumeUnit()!=null && !blMeasurement.getVolumeUnit().equalsIgnoreCase(houseBlItem.getVolumeUnit())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getVolumeUnit());
                    amendItem.setAfterItemComments(blMeasurement.getVolumeUnit());
                    amendItem.setItemNumber("H27");
                    amendItems.add(amendItem);
                }
                if(bl.getPlaceOfDestination()!=null && !bl.getPlaceOfDestination().equalsIgnoreCase(houseBlItem.getPlaceOfDestination())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getPlaceOfDestination());
                    amendItem.setAfterItemComments(bl.getPlaceOfDestination());
                    amendItem.setItemNumber("H28");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeTel()!=null && !bl.getConsigneeTel().equalsIgnoreCase(houseBlItem.getConsigneeTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeTel());
                    amendItem.setAfterItemComments(bl.getConsigneeTel());
                    amendItem.setItemNumber("H13");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeName()!=null && !bl.getConsigneeName().equalsIgnoreCase(houseBlItem.getConsigneeName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeName());
                    amendItem.setAfterItemComments(bl.getConsigneeName());
                    amendItem.setItemNumber("H12");
                    amendItems.add(amendItem);
                }
                if(bl.getConsigneeTin()!=null && !bl.getConsigneeTin().equalsIgnoreCase(houseBlItem.getConsigneeTin())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getConsigneeTin());
                    amendItem.setAfterItemComments(bl.getConsigneeTin());
                    amendItem.setItemNumber("H11");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterAddress()!=null && !bl.getExporterAddress().equalsIgnoreCase(houseBlItem.getExporterAddress())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getExporterAddress());
                    amendItem.setAfterItemComments(bl.getExporterAddress());
                    amendItem.setItemNumber("H10");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterTel()!=null && !bl.getExporterTel().equalsIgnoreCase(houseBlItem.getExporterTel())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getExporterTel());
                    amendItem.setAfterItemComments(bl.getExporterTel());
                    amendItem.setItemNumber("H09");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterName()!=null && !bl.getExporterName().equalsIgnoreCase(houseBlItem.getExporterName())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getExporterName());
                    amendItem.setAfterItemComments(bl.getExporterName());
                    amendItem.setItemNumber("H08");
                    amendItems.add(amendItem);
                }
                if(bl.getExporterTin()!=null && !bl.getExporterTin().equalsIgnoreCase(houseBlItem.getExporterTin())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getExporterTin());
                    amendItem.setAfterItemComments(bl.getExporterTin());
                    amendItem.setItemNumber("H07");
                    amendItems.add(amendItem);
                }
                if(bl.getTradeType()!=null && !bl.getTradeType().substring(0,2).equalsIgnoreCase(houseBlItem.getTradeType())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getTradeType());
                    amendItem.setAfterItemComments(bl.getTradeType());
                    amendItem.setItemNumber("H06");
                    amendItems.add(amendItem);
                }
                if(bl.getBlPackingType()!=null && !bl.getBlPackingType().equalsIgnoreCase(houseBlItem.getBlPackingType())){
                    amendItem= new ExImportAmendItem();
                    amendItem.setBeforeItemComments(houseBlItem.getBlPackingType());
                    amendItem.setAfterItemComments(bl.getBlPackingType());
                    amendItem.setItemNumber("H34");
                    amendItems.add(amendItem);
                }

            }
        }
        for(ExImportAmendItem amendedItem : amendItems){
            amendedItem.setDeclarantTin(declarantTin);
            amendedItem.setAmendSerialNumber(amendSerialNumber);
            amendedItem.setProcessType("M");
            amendedItem.setAmendYear(amendYear);
            amendedItem.setLastUpdateId("TESWS");
            amendedItem.setFirstRegisterId("TESWS");
            System.out.println(amendedItem.toString()) ;
            importAmendItemRepository.save(amendedItem);
        }

    }
    private void saveAmendedContainerDetail(List<Containers> containers, BlDto bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendItemContainer itemContainer = new ExImportAmendItemContainer();
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

    private void saveAddedContainer(List<Containers> containers, BlDto bl) {
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
            exImportAmendBlContainerRepository.save(cn);
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




