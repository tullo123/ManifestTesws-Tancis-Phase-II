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

    @Autowired
    public ExImportManifestAmendServiceImpl(AmendItemContainerRepository amendItemContainerRepository, BlGoodItemsRepository blGoodItemsRepository, EdNoticeRepository edNoticeRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository, ExImportAmendItemRepository importAmendItemRepository, ExImportAmendBlRepository exImportAmendBlRepository, ExImportManifestRepository exImportManifestRepository, CoCompanyCodeRepository coCompanyCodeRepository, CommonOrdinalRepository commonOrdinalRepository, InImportManifestRepository inImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository) {
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
                Bl bl = manifestAmendmentDto.getBl();
                List<Containers> containers = manifestAmendmentDto.getContainers();
                saveGeneralAmendment(bl, manifestAmendmentDto);
                if (manifestAmendmentDto.getBl() != null) {
                    saveBl(bl, manifestAmendmentDto);
                } else if (!manifestAmendmentDto.getContainers().isEmpty()) {
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

    private void saveGeneralAmendment(Bl bl, ManifestAmendmentDto manifestAmendmentDto) {
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

    }

    private void saveBl(Bl bl, ManifestAmendmentDto manifestAmendmentDto) {
        ExImportAmendBl amendBl = new ExImportAmendBl();
        ManifestAmendmentApprovalStatus manifestAmendmentApprovalStatus= new ManifestAmendmentApprovalStatus(manifestAmendmentDto);
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
        manifestAmendmentApprovalStatus.setCommunicationAgreedId(manifestAmendmentDto.getCommunicationAgreedId());
        manifestAmendmentApprovalStatus.setMrn(manifestAmendmentDto.getMrn());
        manifestAmendmentApprovalStatus.setAmendReference(manifestAmendmentDto.getAmendmentReference());
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
        manifestAmendmentApprovalStatusRepository.save(manifestAmendmentApprovalStatus);
    }


    private void saveContainers(List<Containers> containers, Bl bl) {
        for (Containers container : containers) {
            ExImportAmendBlContainer cn = new ExImportAmendBlContainer();
            Optional<CoCompanyCodeEntity> optional = coCompanyCodeRepository.findByCompanyCode(bl.getShippingAgentCode());
            if (optional.isPresent()) {
                CoCompanyCodeEntity entity = optional.get();
                cn.setDeclarantTin(entity.getTin());
            }
            cn.setContainerNo(container.getContainerNo());
            cn.setProcessType("M");
            DateFormat df = new SimpleDateFormat("yy");
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


    private String getTradeType(Bl bl) {
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

    private void createEdNotice(ManifestAmendmentDto manifestAmendmentDto, Bl bl) {
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




