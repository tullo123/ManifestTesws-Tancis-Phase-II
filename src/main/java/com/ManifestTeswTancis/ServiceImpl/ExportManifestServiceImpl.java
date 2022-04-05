package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Service.ExportManifestService;
import com.ManifestTeswTancis.dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ExportManifestServiceImpl implements ExportManifestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportManifestServiceImpl.class);
    final ExportHouseBlRepository exportHouseBlRepository;
    final ExportMasterBlRepository exportMasterBlRepository;
    final ExportContainerRepository exportContainerRepository;
    final EdNoticeRepository edNoticeRepository;
    final ExportManifestRepository exportManifestRepository;

    public ExportManifestServiceImpl(ExportHouseBlRepository exportHouseBlRepository, ExportMasterBlRepository exportMasterBlRepository, ExportContainerRepository exportContainerRepository, EdNoticeRepository edNoticeRepository, ExportManifestRepository exportManifestRepository) {
        this.exportHouseBlRepository = exportHouseBlRepository;
        this.exportMasterBlRepository = exportMasterBlRepository;
        this.exportContainerRepository = exportContainerRepository;
        this.edNoticeRepository = edNoticeRepository;
        this.exportManifestRepository = exportManifestRepository;
    }

    @Override
    public TeswsResponse createExportManifest(ExportManifestDto exportManifestDto) {
        TeswsResponse responseData = new TeswsResponse();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        responseData.setRefId(exportManifestDto.getControlReferenceNumber());
        responseData.setAckDate(localDateTime.format(formatter));
        responseData.setAckType("SUBMITTED_EXPORT_MANIFEST");
        try{
            Optional<ExportManifest> manifest = exportManifestRepository.findFirstByMrnOut(exportManifestDto.getManifestReferenceNumber());
            if (manifest.isPresent()) {
                LOGGER.info("------Saving Export Manifest method called-------");
                ExportManifest exportManifest = manifest.get();
                List<ExportBillOfLadingDto> billOfLadingDtos = exportManifestDto.getConsignments();
                Map<String, Map<String, String>> containerBlMap = new HashMap<>();
                Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap = new HashMap<>();
                Map<String, Map<String, String>> msnMap = new HashMap<>();
                Map<String, String> hsnMap = new HashMap<>();
                Map<String, Map<String, String>> vehicleMap = new HashMap<>();

                setConsignments(billOfLadingDtos, exportManifest.getMrnOut(), hsnMap,containerBlMap,vehicleMap,msnMap,containerSaveMap,exportManifestDto.getContainers());
                List<ContainerDto> containerDtos = exportManifestDto.getContainers();
                if (!containerDtos.isEmpty()) { saveContainers(msnMap,hsnMap, exportManifest.getMrnOut(),containerSaveMap); }
                if(!vehicleMap.isEmpty()){ saveVehicles(vehicleMap, msnMap, exportManifest.getMrnOut()); }
                this.createEdNotice(exportManifest); }

                } catch (Exception exception) {
                    exception.printStackTrace(); }
           return responseData;
    }



    private void saveVehicles(Map<String, Map<String, String>> vehicleMap,
                              Map<String, Map<String, String>> msnHsnMap, String mrnOut) {
        for (Map.Entry<String, Map<String, String>> set : vehicleMap.entrySet()) {
            Map<String, String> blMap = vehicleMap.get(set.getKey());
            Map<String, String> msnMap = msnHsnMap.get(set.getKey());

            Set<String> mblset = blMap.keySet();
            Iterator<String> iter = mblset.iterator();
            String masterBl = iter.next();
            String houseBl = (blMap.get(masterBl) != null)?blMap.get(masterBl):"SIMPLE";

            Set<String> msnSet = msnMap.keySet();
            Iterator<String> msnInter = msnSet.iterator();
            String msn = msnInter.next();

            ExportContainerEntity blContainer = new ExportContainerEntity();
            blContainer.setMrn(mrnOut);
            blContainer.setContainerNo(set.getKey());
            blContainer.setContainerSize("VH");
            blContainer.setContainerType("V");
            blContainer.setMasterBillOfLading(masterBl);
            blContainer.setHouseBillOfLading(houseBl);
            blContainer.setMsn(msn);
            blContainer.setHsn((msnMap.get(msn)!=null)?msnMap.get(msn):"   ");
            blContainer.setLastUpdateId("TESWS");
            blContainer.setFirstRegisterId("TESWS");




            exportContainerRepository.save(blContainer);
        }
    }



    private void saveContainers(
            Map<String, Map<String, String>> msnMap,
            Map<String, String> hsnMap, String mrnOut,
            Map<String, Map<ContainerDto, Map<String, String>>> containerSaveMap) {
        containerSaveMap.forEach((key,contMap)-> contMap.forEach((container, blMap)->{
            ExportContainerEntity cont = new ExportContainerEntity();
            Map<String, String> msns = msnMap.get(container.getContainerNo());
            if (!blMap.isEmpty()) {
                cont.setMrn(mrnOut);
                cont.setContainerNo(container.getContainerNo());
                cont.setContainerSize(container.getContainerSize());
                cont.setContainerType("C");

                Set<String> mblset = blMap.keySet();
                Iterator<String> iter = mblset.iterator();
                String mBl = iter.next();
                String hBl = (blMap.get(mBl) != null)?blMap.get(mBl):"SIMPLE";

                String msn = msns.get(mBl);

                cont.setMasterBillOfLading(mBl);
                cont.setHouseBillOfLading(hBl);
                cont.setMsn(msn);
                cont.setHsn((hsnMap.get(hBl)!=null)?hsnMap.get(hBl):"   ");

                cont.setFreightIndicator(container.getFreightIndicator());
                cont.setContainerWeight(container.getWeight());
                cont.setWeightUnit(fixUnit(container.getWeightUnit()));
                cont.setWeightUnit(fixUnit(container.getGrossWeightUnit()));
                cont.setContainerVolume(container.getVolume());
                cont.setVolumeUnit("CBM");
                cont.setMaximumTemperature(container.getMaximumTemperature());
                cont.setMinimumTemperature(container.getMinimumTemperature());
                cont.setLastUpdateId("TESWS");
                cont.setFirstRegisterId("TESWS");
                if(container.getTemperatureType() != null && container.getTemperatureType().contentEquals("1")) {
                    cont.setReferPlugYn("Y");
                } else
                    cont.setReferPlugYn("N");


                int i = 0;
                int l = 0;
                if (container.getSealNumbers() != null && !container.getSealNumbers().isEmpty()) {

                    for (SealNumberDto sealNumber : container.getSealNumbers()) {
                        if (sealNumber.getSealNumberIssuerType() != null
                                && sealNumber.getSealNumberIssuerType().contentEquals("CU")) {
                            if (i == 0) {
                                cont.setSealNumber(sealNumber.getSealNumber());
                                i++;
                            } else if (i == 1) {
                                cont.setSealNumberTwo(sealNumber.getSealNumber());
                                i++;
                            } else if (i == 2) {
                                cont.setSealNumberThree(sealNumber.getSealNumber());
                                i++;
                            }
                        } else {
                            if (l == 0) {
                                cont.setSealNumber(sealNumber.getSealNumber());
                                l++;
                            } else if (l == 1) {
                                cont.setSealNumberTwo(sealNumber.getSealNumber());
                                l++;
                            } else if (l == 2) {
                                cont.setSealNumberThree(sealNumber.getSealNumber());
                                l++;
                            }
                        }

                    }
                }
                exportContainerRepository.save(cont);
            }
        }));
    }
    private void setConsignments(List<ExportBillOfLadingDto> billOfLadingDtos, String mrnOut,
                                 Map<String, String> hsnMap, Map<String, Map<String, String>> containerBlMap,
                                 Map<String, Map<String, String>> vehicleMap,
                                 Map<String, Map<String, String>> msnMap,
                                 Map<String, Map<ContainerDto, Map<String, String>>> containerSaveMap,
                                 List<ContainerDto> containers) {
        int m = 1;
        int h = 1;

        for (ExportBillOfLadingDto bl : billOfLadingDtos) {
            Map<String, String> msns = new HashMap<>();
            String msn = String.format("%04d", m);
            msns.put(bl.getMasterBillOfLading(), msn);
            if (bl.getHouseBillOfLading() == null || bl.getHouseBillOfLading().contentEquals(bl.getMasterBillOfLading())) {
                msnMap.put(bl.getMasterBillOfLading(),msns);
                saveExportMasterBl(bl, mrnOut, msn,hsnMap, containerBlMap, vehicleMap, msnMap,containerSaveMap,containers);
                m++;
            }
        }

        for (ExportBillOfLadingDto bl : billOfLadingDtos) {
            if (bl.getHouseBillOfLading() != null && !bl.getHouseBillOfLading().isEmpty()
                    && !bl.getHouseBillOfLading().contentEquals(bl.getMasterBillOfLading())) {
                Map<String, String> msns = msnMap.get(bl.getMasterBillOfLading());
                String msn = msns.get(bl.getMasterBillOfLading());
                System.out.println("msn =" + msn + " and Master =" + bl.getMasterBillOfLading());
                String hsn = String.format("%03d", h);
                hsnMap.put(bl.getHouseBillOfLading(),hsn);
                saveExportHouseBl(bl, mrnOut, msn, hsnMap, containerBlMap, vehicleMap, msnMap,containerSaveMap,containers);
                h++;

            }

        }

    }




    private void saveExportMasterBl(ExportBillOfLadingDto bl,
                              String mrnOut,
                              String msn,
                              Map<String, String> hsnMap,
                              Map<String, Map<String, String>> containerBlMap,
                              Map<String, Map<String, String>> vehicleMap,
                              Map<String, Map<String, String>> msnMap,
                              Map<String, Map<ContainerDto, Map<String, String>>> containerSaveMap,
                              List<ContainerDto> containers) {
        BlMeasurement blMeasurement = getBlMeasurement(bl, hsnMap, containerBlMap,vehicleMap,msnMap,containerSaveMap,containers);
        ExportMasterBlEntity exportMasterBlEntity = new ExportMasterBlEntity(bl);
        exportMasterBlEntity.setMrn(mrnOut);
        exportMasterBlEntity.setMsn(msn);
        exportMasterBlEntity.setTradeType(getTradeType(bl));
        exportMasterBlEntity.setBlType(getBlType(bl));
        exportMasterBlEntity.setConsolidatedStatus(getConsolidatedStatus(bl));
        exportMasterBlEntity.setBlPackage(blMeasurement.getPkQuantity());
        exportMasterBlEntity.setPackageUnit(blMeasurement.getPkType());
        exportMasterBlEntity.setGrossWeightUnit(blMeasurement.getWeightUnit());
        exportMasterBlEntity.setNetWeightUnit(blMeasurement.getWeightUnit());
        exportMasterBlEntity.setBlVolume(blMeasurement.getVolume());
        exportMasterBlEntity.setVolumeUnit(blMeasurement.getVolumeUnit());
        exportMasterBlEntity.setPackingType(bl.getBlPackingType());
        exportMasterBlEntity.setOilType(blMeasurement.getOilType());
        exportMasterBlEntity.setImdgCode(blMeasurement.getImdgClass());
        exportMasterBlEntity.setBlDescription(bl.getBlDescription());
        exportMasterBlEntity.setInvoiceCurrency(blMeasurement.getInvoiceCurrency());
        exportMasterBlEntity.setFreightCurrency(blMeasurement.getFreightCurrency());
        exportMasterBlEntity.setInvoiceValue(blMeasurement.getInvoiceValue());
        exportMasterBlEntity.setFreightCharge(blMeasurement.getFreightCharge());
        exportMasterBlEntity.setMarksNumbers(blMeasurement.getMarksNumbers());

        exportMasterBlRepository.save(exportMasterBlEntity);
    }






    private void saveExportHouseBl(ExportBillOfLadingDto bl,
                                   String mrnOut,
                                   String msn,
                                   Map<String, String> hsnMap,
                                   Map<String, Map<String, String>> containerBlMap,
                                   Map<String, Map<String, String>> vehicleMap,
                                   Map<String, Map<String, String>> msnMap,
                                   Map<String, Map<ContainerDto, Map<String, String>>> containerSaveMap,
                                   List<ContainerDto> containers) {
        BlMeasurement blMeasurement = getBlMeasurement(bl,hsnMap,containerBlMap,vehicleMap,msnMap,containerSaveMap,containers);
        ExportHouseBlEntity exportHouseBlEntity = new ExportHouseBlEntity(bl);
        exportHouseBlEntity.setMrn(mrnOut);
        exportHouseBlEntity.setMsn(msn);
        exportHouseBlEntity.setHsn(hsnMap.get(bl.getHouseBillOfLading()));
        exportHouseBlEntity.setTradeType((getTradeType(bl)));
        exportHouseBlEntity.setBlPackage(blMeasurement.getPkQuantity());
        exportHouseBlEntity.setPackageUnit(blMeasurement.getPkType());
        exportHouseBlEntity.setNetWeightUnit(blMeasurement.getWeightUnit());
        exportHouseBlEntity.setVolumeUnit(blMeasurement.getVolumeUnit());
        exportHouseBlEntity.setBlVolume(blMeasurement.getVolume());
        exportHouseBlEntity.setPackingType(bl.getBlPackingType());
        exportHouseBlEntity.setOilType(blMeasurement.getOilType());
        exportHouseBlEntity.setImdgCode(blMeasurement.getImdgClass());
        exportHouseBlEntity.setBlDescription(bl.getBlDescription());
        exportHouseBlEntity.setInvoiceCurrency(blMeasurement.getInvoiceCurrency());
        exportHouseBlEntity.setFreightCurrency(blMeasurement.getFreightCurrency());
        exportHouseBlEntity.setInvoiceValue(blMeasurement.getInvoiceValue());
        exportHouseBlEntity.setFreightCharge(blMeasurement.getFreightCharge());
        exportHouseBlEntity.setMarksNumbers(blMeasurement.getMarksNumbers());

        exportHouseBlRepository.save(exportHouseBlEntity);

    }





    private BlMeasurement getBlMeasurement(ExportBillOfLadingDto bl,
                                           Map<String, String> hsnMap,
                                           Map<String, Map<String, String>> containerBlMap,
                                           Map<String, Map<String, String>> vehicleMap,
                                           Map<String, Map<String, String>> msnMap,
                                           Map<String, Map<ContainerDto, Map<String, String>>> containerSaveMap,
                                           List<ContainerDto> containers) {
        double pkQuantity = 0.0;
        String pkType = "PK";
        String description = "";
        String invoiceCurrency="";
        String freightCurrency="";
        String marksNumbers="";
        double grossWeight = 0.0;
        double invoiceValue =0.0;
        double freightCharge=0.0;
        double netWeight = 0.0;
        String weightUnit = "KG";
        double volume = 0.0;
        String volumeUnit = "KG";
        String oilType = null;
        String packingType = "";
        String imdgClass = "";
        for (ExportManifestGoodsDto gd : bl.getGoodDetails()) {
            pkQuantity += (gd.getPackageQuantity()!=null)?gd.getPackageQuantity():0;
            invoiceValue +=(gd.getInvoiceValue()!=null)?gd.getInvoiceValue():0;
            freightCharge +=(gd.getFreightCharge()!=null)?gd.getFreightCharge():0;
            pkType = fixUnit(gd.getPackageType());
            description = gd.getDescription();
            marksNumbers= gd.getMarksNumbers();
            invoiceCurrency= gd.getInvoiceCurrency();
            freightCurrency= gd.getFreightCurrency();
            grossWeight += (gd.getGrossWeight() != null)?gd.getGrossWeight():0;
            weightUnit = fixUnit(gd.getGrossWeightUnit());
            netWeight += (gd.getNetWeight() != null)?gd.getNetWeight():0;
            volume += (gd.getVolume()!=null)?gd.getVolume():0;
            volumeUnit = fixUnit(gd.getVolumeUnit());
            oilType = gd.getOilType();
            packingType = fixUnit(gd.getPackingType());

            if (gd.getDangerousGoodsInformation() != null) {
                imdgClass = gd.getDangerousGoodsInformation().getImdgclass();
            }

            if(gd.getPackingType() != null){
                if(packingType.equals("V")){
                    Map<String, String> blMap = new HashMap<>();
                    blMap.put(bl.getMasterBillOfLading(), bl.getHouseBillOfLading());
                    vehicleMap.put(gd.getVehicleVIN(),blMap);
                    msnMap.put(gd.getVehicleVIN(),msnMap.get(bl.getMasterBillOfLading()));
                }
            }
            for (ExportGoodsPlacementDto pc : gd.getPlacements()) {
                if(bl.getBlType().equalsIgnoreCase("CONSOLIDATED"))
                    continue;
                Map<String, String> blMap = new HashMap<>();
                Map<ContainerDto,Map<String, String>> cmap = new HashMap<>();
                blMap.put(bl.getMasterBillOfLading(), bl.getHouseBillOfLading());
                containerBlMap.put(pc.getContainerNo(), blMap);
                ContainerDto container = containers.stream().filter(c->c.getContainerNo().equalsIgnoreCase(pc.getContainerNo())).findAny().get();
                String key = bl.getMasterBillOfLading()+"-"+pc.getContainerNo()+"-"+(bl.getHouseBillOfLading() != null ? "-"+bl.getHouseBillOfLading():"");
                cmap.put(container,blMap);
                containerSaveMap.put(key,cmap);
                msnMap.put(pc.getContainerNo(),msnMap.get(bl.getMasterBillOfLading()));
                hsnMap.put(pc.getContainerNo(),hsnMap.get(bl.getHouseBillOfLading()));
            }
        }

        if(packingType.equals("V") && netWeight < 1){
            netWeight = grossWeight;
        }
        BlMeasurement measurement = new BlMeasurement(
                pkQuantity,
                pkType,
                description,
                (grossWeight>0)?grossWeight:null,
                (netWeight>0)?netWeight:null,
                weightUnit,
                (volume>0)?volume:null,
                volumeUnit,
                oilType,
                packingType,
                imdgClass,
                invoiceCurrency,
                freightCurrency,
                invoiceValue,freightCharge,marksNumbers);

        measurement.setDescription(description);
        return measurement;
    }




    private String fixUnit(String unit) {
        if(unit != null) {
            if (unit.equalsIgnoreCase("KGM")|| unit.equalsIgnoreCase("KGS")) {
                return "KG";
            }
            if (unit.equalsIgnoreCase("CN")) {
                return "C";
            }
            if (unit.equalsIgnoreCase("VH")) {
                return "V";
            }
        }
        return unit;
    }



    private String getTradeType(ExportBillOfLadingDto billOfLadingDto){
        String tradeType = billOfLadingDto.getTradeType();
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
            return billOfLadingDto.getTradeType();
        }

    }


    private String getBlType(ExportBillOfLadingDto bl) {
        String blType = bl.getBlType();
        if(blType != null && blType.equalsIgnoreCase("SIMPLE") ){
            return "S";
        } else if(blType!=null && blType.equalsIgnoreCase("CONSOLIDATED")){
            return "C";
        }else{
            return bl.getBlType();
        }
    }



    private String getConsolidatedStatus(ExportBillOfLadingDto bl) {
        if(bl.getBlType()!= null && bl.getBlType().equalsIgnoreCase("SIMPLE")){
            return "U";
        }else if(bl.getBlType()!= null && bl.getBlType().equalsIgnoreCase("CONSOLIDATED")){
            return "C";
        }else {
            return bl.getBlType();
        }
    }


    private void createEdNotice(ExportManifest exportManifest) throws InterruptedException {
        EdNoticeEntity edNotice = new EdNoticeEntity();
        Optional<EdNoticeEntity> optional = edNoticeRepository.findByDocumentNumber(exportManifest.getMrnOut());
        if(!optional.isPresent()) {
            LOGGER.info("#---CreateEdNotice START----#");
            TimeUnit.SECONDS.sleep(10);
            edNotice.setDocumentCode("CUSMAN201");
            edNotice.setDocumentNumber(exportManifest.getMrnOut());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String strDate = dtf.format(now);
            edNotice.setCreateDate(strDate);
            edNotice.setSequenceNumber(1);
            edNotice.setDocumentFunctionType("9");
            edNotice.setCustomsOfficeCode(exportManifest.getCustomOfficeCode());
            edNotice.setReceiverId("INTERNAL");
            edNotice.setSenderId("EXTERNAL");
            edNotice.setDocumentType("D");
            edNotice.setOriginalDocumentCode("CUSMAN201");
            edNotice.setOriginalDocumentNumber(exportManifest.getMrnOut());
            edNotice.setTransferType("E");
            edNotice.setProcessingStatus("N");
            edNoticeRepository.save(edNotice);
            LOGGER.info("#---CreateEdNotice END---------#");
            TimeUnit.SECONDS.sleep(10); }
        else {
            LOGGER.info("Manifest with This Mrn already submitted to TANCIS INTERNAL.");
        }

    }

}




