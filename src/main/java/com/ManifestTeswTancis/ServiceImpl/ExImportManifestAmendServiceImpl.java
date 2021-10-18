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
    CommonOrdinalEntity commonOrdinalEntity;

   @Autowired
    public ExImportManifestAmendServiceImpl(AmendItemContainerRepository amendItemContainerRepository, BlGoodItemsRepository blGoodItemsRepository, EdNoticeRepository edNoticeRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository, ExImportAmendItemRepository importAmendItemRepository, ExImportAmendBlRepository exImportAmendBlRepository, ExImportManifestRepository exImportManifestRepository, CoCompanyCodeRepository coCompanyCodeRepository, CommonOrdinalRepository commonOrdinalRepository, InImportManifestRepository inImportManifestRepository) {
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
   }

    @Override
    public TeswsResponse amendManifest(ManifestAmendmentDto manifestAmendmentDto) {
        TeswsResponse responseData = new TeswsResponse();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        responseData.setRefId(manifestAmendmentDto.getAmendmentReference());
        responseData.setAckDate(localDateTime.format(formatter));
        responseData.setAckType("MANIFEST_AMENDMENT");
        try{
            Optional<ExImportManifest> optional=exImportManifestRepository.
                    findFirstByMrn(manifestAmendmentDto.getMrn());
            if(optional.isPresent()){
                Bl bl = manifestAmendmentDto.getBl();
                Map<String, Map<String, String>> containerBlMap = new HashMap<>();
                Map<String, Map<String, String>> amendSerialNoMap = new HashMap<>();
                Map<String, Map<String, String>> vehicleMap = new HashMap<>();
                saveGeneralAmendment(bl,manifestAmendmentDto);
                List<Containers> containers= manifestAmendmentDto.getContainers();
                setBl(bl,containerBlMap,vehicleMap,amendSerialNoMap,manifestAmendmentDto);
                if(!containers.isEmpty()){
                    saveContainer(containers,bl);
                }
                if(!vehicleMap.isEmpty()){
                    saveVehicles(vehicleMap, amendSerialNoMap,bl);
                }
                this.createEdNotice(manifestAmendmentDto);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            responseData.setDescription("Errors in saving manifest Amendment" + exception.getMessage());
            responseData.setCode(500);
        }

        return responseData;
    }

    private void saveGeneralAmendment(Bl bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendGeneral amendGeneral = new ExImportAmendGeneral();
        Optional<CoCompanyCodeEntity> optional=coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
        if(optional.isPresent()){
            CoCompanyCodeEntity company= optional.get();
            amendGeneral.setDeclarantTin(company.getTin());
        }
        DateFormat df = new SimpleDateFormat("yy");
        amendGeneral.setAmendYear(df.format(Calendar.getInstance().getTime()));
        amendGeneral.setProcessType("M");
        String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
        amendGeneral.setAmendSerialNumber(suffix);
        amendGeneral.setProcessingStatus("1");
        amendGeneral.setProcessingDate(DateFormatter.getDateFromLocalDateTime(LocalDateTime.now()));
        amendGeneral.setProcessingId("SYSTEM");
        Optional<InImportManifest> option = inImportManifestRepository.findFirstByCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        if (option.isPresent()) {
            InImportManifest amend = option.get();
            amendGeneral.setCustomOfficeCode(amend.getCustomOfficeCode());
        }
        amendGeneral.setMrn(manifestAmendmentDto.getMrn());

    }

    private void setBl(Bl bl, Map<String, Map<String, String>> blMap, Map<String, Map<String, String>> containerBlMap,
                       Map<String, Map<String, String>> vehicleMap,ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendBl amendBl = new ExImportAmendBl();
        BlMeasurement blMeasurement =new BlMeasurement();
        Optional<CoCompanyCodeEntity> optional=coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
        if (optional.isPresent()){
            CoCompanyCodeEntity entity=optional.get();
            amendBl.setDeclatantTin(entity.getTin());
        }
        DateFormat df = new SimpleDateFormat("yy");
        amendBl.setAmendYear(df.format(Calendar.getInstance().getTime()));
        amendBl.setProcessType("M");
        if(bl.getHouseBillOfLading()!= null){
            amendBl.setBillOfLading(bl.getHouseBillOfLading());
        }else{
            amendBl.setBillOfLading(bl.getMasterBillOfLading());
        }
        amendBl.setTradeType(getTradeType(bl));
        amendBl.setBlType(getBlType(bl));
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
        amendBl.setGrossWeight(bl.getBlSummary().getBlGrossWeight());
        amendBl.setGrossWeightUnit("KG");
        amendBl.setNetWeight(bl.getBlSummary().getBlNetWeight());
        amendBl.setNetWeightUnit("KG");
        amendBl.setVolume(blMeasurement.getVolume());
        amendBl.setVolumeUnit(blMeasurement.getVolumeUnit());
        amendBl.setPortOfLoading(bl.getPortOfLoading());
        amendBl.setPlaceOfDestination(bl.getPlaceOfDestination());
        amendBl.setPlaceOfDestination(bl.getPlaceOfDestination());
        amendBl.setPlaceOfDelivery(bl.getPlaceOfDelivery());
        for(GoodDetails goodsDto:bl.getGoodDetails()){
            amendBl.setInvoiceValue(goodsDto.getInvoiceValue());
            amendBl.setMarksNumbers(goodsDto.getMarksNumbers());
            amendBl.setFreightCharge(goodsDto.getFreightCharge());
            amendBl.setFreightCurrency(goodsDto.getFreightCurrency());
            amendBl.setInvoiceCurrency(goodsDto.getInvoiceCurrency());
            if(goodsDto.getDangerousGoodsInformation()!=null){
                amendBl.setImdgclass(goodsDto.getDangerousGoodsInformation().getImdgclass());
            }
        }
        amendBl.setPackingType(bl.getBlPackingType());
        amendBl.setOilType(blMeasurement.getOilType());
        amendBl.setAuditStatus("NA");
        if(bl.getHouseBillOfLading()!= null){
            amendBl.setConsolidatedStatus("Y");
        }else {
            amendBl.setConsolidatedStatus("N");
        }
        amendBl.setLastUpdateId("TESWS");
        amendBl.setFirstRegisterId("TESWS");
        amendBl.setTasacControlNumber(bl.getTasacControlNumber());
            String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
            amendBl.setAmendSerialNumber(suffix);
        exImportAmendBlRepository.save(amendBl);
        Optional<InImportManifest> opt = inImportManifestRepository.findFirstByCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        if(opt.isPresent()){
            InImportManifest inImportManifest= opt.get();
            amendBl.setFirstDestinationPlace(inImportManifest.getDestinationPort());
        }
    }



    private void saveContainer(List<Containers> containers, Bl bl) {
       for (Containers container : containers) {
           ExImportAmendBlContainer cn= new ExImportAmendBlContainer();
           Optional<CoCompanyCodeEntity> optional=coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
           if (optional.isPresent()){
               CoCompanyCodeEntity entity=optional.get();
               cn.setDeclarantTin(entity.getTin());
           }
           cn.setContainerNo(container.getContainerNo());
           cn.setProcessType("M");
           DateFormat df = new SimpleDateFormat("yy");
           cn.setAmendYear(df.format(Calendar.getInstance().getTime()));
           cn.setAmendSerialNumber("");
           cn.setContainerSize(container.getContainerSize());
           cn.setTypeOfContainer("C");
           if(bl.getHouseBillOfLading()!= null){
               cn.setBillOfLading(bl.getHouseBillOfLading());
           }else{
               cn.setBillOfLading(bl.getMasterBillOfLading());
           }
           cn.setFirstRegisterId("TESWS");
           cn.setLastUpdateId("TESWS");
           cn.setFreightIndicator(container.getFreightIndicator());
           cn.setMaximumTemperature(container.getMaximumTemperature());
           cn.setMinimumTemperature(container.getMinimumTemperature());
           String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
           cn.setAmendSerialNumber(suffix);
           int i = 0;
           int l = 0;
           if (container.getSealNumbers() != null && !container.getSealNumbers().isEmpty()){
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

    private void saveVehicles(Map<String, Map<String, String>> vehicleMap,
                              Map<String, Map<String, String>> amendSerialNoMap, Bl bl) {
    }

    private String getTradeType(Bl bl) {
        String tradeType = bl.getTradeType();
        if(tradeType != null && tradeType.equalsIgnoreCase("IMPORT")) {
            return "IM";
        }
        else if (tradeType != null && tradeType.equalsIgnoreCase("TRANSIT")) {
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
    private String getBlType(Bl bl) {
       String blType = bl.getTradeType();
       if(blType != null && blType.equalsIgnoreCase("SIMPLE")){
           return "S";
       }
       else if(blType != null && blType.equalsIgnoreCase("CONSOLIDATED")){
           return "C";
       }
        return blType;
    }
    private void createEdNotice(ManifestAmendmentDto manifestAmendmentDto) {

        EdNoticeEntity edNotice = new EdNoticeEntity();
        CoCompanyCodeEntity coCompanyCodeEntity = new CoCompanyCodeEntity();
        Optional<InImportManifest> optional = inImportManifestRepository.findFirstByCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        if(optional.isPresent()) {
            InImportManifest infEntity= optional.get();
            edNotice.setCustomsOfficeCode(infEntity.getCustomOfficeCode());
            edNotice.setDocumentCode("IMFMOD215");
            edNotice.setDocumentNumber(generatedDocumentNo(coCompanyCodeEntity.getTin()));
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
            edNotice.setOriginalDocumentNumber(generatedDocumentNo(coCompanyCodeEntity.getTin()));
            edNotice.setTransferType("E");
            edNotice.setProcessingStatus("N");
            edNoticeRepository.save(edNotice);
        }
        else {
            System.out.println("Manifest Amendment with This Mrn is not present in TANCIS");
        }


    }

    private String generatedDocumentNo(String tin) {
        CommonOrdinalEntity commonOrdinalEntity;
        DateFormat df = new SimpleDateFormat("yyyy");
        String prefix =tin + df.format(Calendar.getInstance().getTime()) + "M";
        Optional<CommonOrdinalEntity> optionalCommonOrdinalEntity = commonOrdinalRepository.findByPrefix(prefix);
        if (optionalCommonOrdinalEntity.isPresent()) {
            commonOrdinalEntity = optionalCommonOrdinalEntity.get();
            commonOrdinalEntity.setSequenceNo(commonOrdinalEntity.getSequenceNo() + 1);
        }else {
            commonOrdinalEntity = new CommonOrdinalEntity();
            commonOrdinalEntity.setPrefix(prefix);
            commonOrdinalEntity.setSequenceNo(1);
        }
        commonOrdinalRepository.save(commonOrdinalEntity);
        String suffix = String.format("%1$" + 7 + "s", commonOrdinalEntity.getSequenceNo()).replace(' ', '0');
        return prefix +suffix;
    }

}
