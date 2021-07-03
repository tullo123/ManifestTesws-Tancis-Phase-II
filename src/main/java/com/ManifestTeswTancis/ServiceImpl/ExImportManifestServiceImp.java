package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.dtos.*;
import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Service.ExImportManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ExImportManifestServiceImp implements ExImportManifestService {
	private final ExImportManifestRepository exImportManifestRepository;
	private final ExImportMasterBlRepository exImportMasterBlRepository;
	private final ExImportHouseBlRepository exImportHouseBlRepository;
	private final ExImportBlContainerRepository exImportBlContainerRepository;
	private final EdNoticeRepository edNoticeRepository;
	private final ManifestStatusServiceImp statusServiceImp;
	final
	BlGoodItemsRepository blGoodItemsRepository;

	@Autowired
	public ExImportManifestServiceImp(ExImportManifestRepository exImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository,
									  ExImportHouseBlRepository exImportHouseBlRepository, ExImportBlContainerRepository exImportBlContainerRepository,
									  EdNoticeRepository edNoticeRepository, ManifestStatusServiceImp statusServiceImp, BlGoodItemsRepository blGoodItemsRepository) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.exImportMasterBlRepository = exImportMasterBlRepository;
		this.exImportHouseBlRepository = exImportHouseBlRepository;
		this.exImportBlContainerRepository = exImportBlContainerRepository;
		this.edNoticeRepository = edNoticeRepository;
		this.statusServiceImp = statusServiceImp;
		this.blGoodItemsRepository = blGoodItemsRepository;
	}

	@Override
	public TeswsResponse createManifest(ManifestDto manifestDto) {
		TeswsResponse responseData = new TeswsResponse();
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		responseData.setRefId(manifestDto.getControlReferenceNumber());
		responseData.setAckDate(localDateTime.format(formatter));
		responseData.setAckType("CUSTOMS_MANIFEST");

		try {
			Optional<ExImportManifest> callInfEntity = exImportManifestRepository
					.findFirstByMrn(manifestDto.getMessageReferenceNumber());
			if (callInfEntity.isPresent()) {
				System.out.print("-----------------Saving Manifest method called------------------------");
				ExImportManifest infEntity = callInfEntity.get();
				List<BillOfLadingDto> billOfLadingDtos = manifestDto.getConsignments();
				Map<String, Map<String, String>> containerBlMap = new HashMap<>();
				Map<String, Map<String, String>> msnMap = new HashMap<>();
				Map<String, Map<String, String>> vehicleMap = new HashMap<>();

				setConsignments(billOfLadingDtos, infEntity.getMrn(), containerBlMap,vehicleMap,msnMap);
				List<ContainerDto> containerDtos = manifestDto.getContainers();
				this.createEdNotice(infEntity);
				if (!containerDtos.isEmpty()) {
					saveContainers(containerDtos, containerBlMap, msnMap, infEntity.getMrn());
				}
				if(!vehicleMap.isEmpty()){
					saveVehicles(vehicleMap, msnMap, infEntity.getMrn());
				}
				//statusServiceImp.save(infEntity, manifestDto.getControlReferenceNumber(), false);
				// 12-june-2021 this line was commented
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setDescription("Error In saving manifest\n" + e.getMessage());
			responseData.setCode(500);
		}

		return responseData;
	}

	private void createEdNotice(ExImportManifest exImportManifest) throws InterruptedException {

		EdNoticeEntity edNotice = new EdNoticeEntity();
		Optional<EdNoticeEntity> optional = edNoticeRepository.findByDocumentNumber(exImportManifest.getMrn());
		if(!optional.isPresent()) {
			System.out.println(
					"#-------------------------createEdNotice START --------------------------#");
			TimeUnit.SECONDS.sleep(10);
			edNotice.setDocumentCode("CUSMAN201");
			edNotice.setDocumentNumber(exImportManifest.getMrn());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			LocalDateTime now = LocalDateTime.now();
			String strDate = dtf.format(now);
			edNotice.setCreateDate(strDate);
			edNotice.setSequenceNumber(1);
			edNotice.setDocumentFunctionType("9");
			edNotice.setCustomsOfficeCode(exImportManifest.getCustomOfficeCode());
			System.out.println(
					"#----------------------------------------------------------#");
			TimeUnit.SECONDS.sleep(10);
			edNotice.setReceiverId("INTERNAL");
			edNotice.setSenderId("EXTERNAL");
			edNotice.setDocumentType("D");
			edNotice.setOriginalDocumentCode("CUSMAN201");
			edNotice.setOriginalDocumentNumber(exImportManifest.getMrn());
			edNotice.setTransferType("E");
			edNotice.setProcessingStatus("N");
			edNoticeRepository.save(edNotice);
			System.out.println(
					"#------------------------createEdNotice END ----------------#");
			TimeUnit.SECONDS.sleep(10);
		}
		else {
			System.out.println("Manifest with This Mrn already submitted to TANCIS INTERNAL.");
		}


	}

	private void saveVehicles(Map<String, Map<String, String>> vehicleMap,
							  Map<String, Map<String, String>> msnHsnMap, String mrn){

		for (Map.Entry<String, Map<String, String>> set : vehicleMap.entrySet()) {
			Map<String, String> blMap = vehicleMap.get(set.getKey());
			Map<String, String> msnMap = msnHsnMap.get(set.getKey());

			Set<String> mblset = blMap.keySet();
			Iterator iter = mblset.iterator();
			String masterBl = iter.next().toString();
			String houseBl = (blMap.get(masterBl) != null)?blMap.get(masterBl):"SIMPLE";

			Set<String> msnSet = msnMap.keySet();
			Iterator msnInter = msnSet.iterator();
			String msn = msnInter.next().toString();

			ExImportBlContainer blContainer = new ExImportBlContainer();
			blContainer.setMrn(mrn);
			blContainer.setContainerNo(set.getKey());
			blContainer.setContainerSize("VH");
			blContainer.setTypeOfContainer("V");
			blContainer.setMasterBillOfLading(masterBl);
			blContainer.setHouseBillOfLading(houseBl);
			blContainer.setMsn(msn);
			blContainer.setHsn(msnMap.get(msn));

			exImportBlContainerRepository.save(blContainer);
		}
	}

	private void saveContainers(
			List<ContainerDto> containerDtos,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> msnHsnMap, String mrn) {
		for (ContainerDto container : containerDtos) {
			ExImportBlContainer cnEn = new ExImportBlContainer();
			Map<String, String> blMap = containerBlMap.get(container.getContainerNo());
			Map<String, String> msnMap = msnHsnMap.get(container.getContainerNo());
			if (container.getHouseBillOfLading() != null) {
				cnEn.setHouseBillOfLading(container.getHouseBillOfLading());
			}
			if (!blMap.isEmpty()) {
				cnEn.setMrn(mrn);
				cnEn.setContainerNo(container.getContainerNo());
				cnEn.setContainerSize(container.getContainerSize());
				cnEn.setTypeOfContainer(container.getTypeOfContainer());

				Set<String> mblset = blMap.keySet();
				Iterator iter = mblset.iterator();
				String mBl = iter.next().toString();
                String hBl = (blMap.get(mBl) != null)?blMap.get(mBl):"SIMPLE";

				Set<String> msnSet = msnMap.keySet();
				Iterator msnInter = msnSet.iterator();
				String msn = msnInter.next().toString();

				cnEn.setMasterBillOfLading(mBl);
				cnEn.setHouseBillOfLading(hBl);
				cnEn.setMsn(msn);
				cnEn.setHsn(msnMap.get(msn));

				cnEn.setFreightIndicator(container.getFreightIndicator());
				cnEn.setWeight(container.getGrossWeight());
				cnEn.setWeightUnit(fixUnit(container.getGrossWeightUnit()));
				cnEn.setVolume(container.getVolume());
				cnEn.setVolumeUnit(fixUnit(container.getVolumeUnit()));
				cnEn.setMaximumTemperature(container.getMaximumTemperature());
				cnEn.setMinimumTemperature(container.getMinimumTemperature());
				cnEn.setFirstRegisterId("TESWS");
				cnEn.setLastUpdateId("TESWS");

				if(container.getTemperatureType() != null) {
					cnEn.setReferPlugYn(container.getTemperatureType().contentEquals("1")?"Y":"N");
				}

				int i = 0;
				int l = 0;
				if (container.getSealNumbers() != null && !container.getSealNumbers().isEmpty()) {

					for (SealNumberDto sealNumber : container.getSealNumbers()) {
						if (sealNumber.getSealNumberIssuerType() != null
								&& sealNumber.getSealNumberIssuerType().contentEquals("CU")) {
							if (i == 0) {
								cnEn.setCustomSealNumberOne(sealNumber.getSealNumber());
								i++;
							} else if (i == 1) {
								cnEn.setCustomSealNumberTwo(sealNumber.getSealNumber());
								i++;
							} else if (i == 2) {
								cnEn.setCustomSealNumberThree(sealNumber.getSealNumber());
								i++;
							}
						} else {
							if (l == 0) {
								cnEn.setSealNumber(sealNumber.getSealNumber());
								l++;
							} else if (l == 1) {
								cnEn.setSealNumberTwo(sealNumber.getSealNumber());
								l++;
							} else if (l == 2) {
								cnEn.setSealNumberThree(sealNumber.getSealNumber());
								l++;
							}
						}

					}
				}

				exImportBlContainerRepository.save(cnEn);
			}

		}
	}

	private void setConsignments(
			List<BillOfLadingDto> billOfLadingDtos,
			String mrn,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap) {
		int i = 1;
		int j = 1;
		Map<String,String> msns = new HashMap<>();
		for (BillOfLadingDto bl : billOfLadingDtos) {
			String msn = String.format("%04d", i);
			msns.put(bl.getMasterBillOfLading(),msn);
			if (bl.getHouseBillOfLading() == null || bl.getHouseBillOfLading().contentEquals(bl.getMasterBillOfLading())) {
				saveMasterBl(bl, mrn, msn, containerBlMap, vehicleMap, msnMap);
				i++;
			}
		}

		for (BillOfLadingDto bl : billOfLadingDtos) {
			if (bl.getHouseBillOfLading() != null && !bl.getHouseBillOfLading().isEmpty()
					&& !bl.getHouseBillOfLading().contentEquals(bl.getMasterBillOfLading())) {
				String msn = msns.get(bl.getMasterBillOfLading());
				System.out.println("msn ="+msn+" and Master ="+bl.getMasterBillOfLading());
				String hsn = String.format("%03d", j);
				saveHouseBl(bl, mrn, msn, hsn, containerBlMap, vehicleMap, msnMap);
				saveGoodsItems(bl);
				j++;

			}
		}
	}


	private void saveMasterBl(
			BillOfLadingDto bl,
			String mrn,
			String msn,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap) {
		BlMeasurement blMeasurement = getBlMeasurement(bl, msn, "   ", containerBlMap,vehicleMap,msnMap);
		System.out.println("generated msn:" + msn);
		ExImportMasterBl exImportMasterBl = new ExImportMasterBl(bl);
		BlSummary blSummary = new BlSummary();
		exImportMasterBl.setMrn(mrn);
		exImportMasterBl.setMsn(msn);
		exImportMasterBl.setAuditStatus("NA");
		if (bl.getHouseBillOfLading() !=null){
			exImportMasterBl.setConsolidatedStatus("Y");

		}else {
			exImportMasterBl.setConsolidatedStatus("N");
		}
		exImportMasterBl.setTradeType(getTradeType(bl));
		exImportMasterBl.setBlPackage(blMeasurement.getPkQuantity());
		exImportMasterBl.setPackageUnit(blMeasurement.getPkType());
		//exImportMasterBl.setGrossWeight(blMeasurement.getGrossWeight());
		exImportMasterBl.setGrossWeight(blSummary.getBlGrossWeight());
		exImportMasterBl.setGrossWeightUnit(blMeasurement.getWeightUnit());
		exImportMasterBl.setNetWeight(blSummary.getBlNetWeight());
		//exImportMasterBl.setNetWeight(blMeasurement.getNetWeight());
		exImportMasterBl.setNetWeightUnit(blMeasurement.getWeightUnit());
		exImportMasterBl.setVolume(blMeasurement.getVolume());
		exImportMasterBl.setVolumeUnit(blMeasurement.getVolumeUnit());
		exImportMasterBl.setPackingType(bl.getBlPackingType());
		exImportMasterBl.setOilType(blMeasurement.getOilType());
		exImportMasterBl.setImdgclass(blMeasurement.getImdgClass());
		exImportMasterBl.setDescription(bl.getBlDescription());
		exImportMasterBlRepository.save(exImportMasterBl);

	}



	private void saveHouseBl(
			BillOfLadingDto bl,
			String mrn, String msn,
			String hsn,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap) {

		BlMeasurement blMeasurement = getBlMeasurement(bl,msn,hsn,containerBlMap,vehicleMap,msnMap);
		BlSummary blSummary = new BlSummary();
		ExImportHouseBl exImportHouseBl = new ExImportHouseBl(bl);
		exImportHouseBl.setMrn(mrn);
		exImportHouseBl.setMsn(msn);
		exImportHouseBl.setHsn(hsn);
		exImportHouseBl.setTradeType((getTradeType(bl)));
		exImportHouseBl.setBlPackage(blMeasurement.getPkQuantity());
		exImportHouseBl.setPackageUnit(blMeasurement.getPkType());
		//exImportHouseBl.setGrossWeight(blMeasurement.getGrossWeight());
		exImportHouseBl.setGrossWeight(blSummary.getBlGrossWeight());
		//exImportHouseBl.setGrossWeightUnit(blMeasurement.getWeightUnit());
		exImportHouseBl.setNetWeight(blSummary.getBlNetWeight());
		exImportHouseBl.setNetWeightUnit(blMeasurement.getWeightUnit());
		exImportHouseBl.setVolumeUnit(blMeasurement.getVolumeUnit());
		exImportHouseBl.setVolume(blMeasurement.getVolume());
		exImportHouseBl.setPackingType(bl.getBlPackingType());
		exImportHouseBl.setOilType(blMeasurement.getOilType());
		exImportHouseBl.setImdgclass(blMeasurement.getImdgClass());
		exImportHouseBl.setDescription(bl.getBlDescription());

		exImportHouseBlRepository.save(exImportHouseBl);

	}

	private BlMeasurement getBlMeasurement(
			BillOfLadingDto bl,
			String msn, String hsn,
			Map<String, Map<String, String>> containerBlMap ,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap) {
		double pkQuantity = 0.0;
		String pkType = "PK";
		String description = "";
		double grossWeight = 0.0;
		double netWeight = 0.0;
		String weightUnit = "KG";
		double volume = 0.0;
		String volumeUnit = "KG";
		String oilType = null;
		String packingType = "";
		String imdgClass = "";
		Map<String, String> map = new HashMap<>();
		map.put(msn,hsn);
		for (GoodsDto gd : bl.getGoodDetails()) {
			pkQuantity += (gd.getPackageQuantity()!=null)?gd.getPackageQuantity():0;
			pkType = fixUnit(gd.getPackageType());
			description = gd.getDescription();
			grossWeight += (gd.getGrossWeight() != null)?gd.getGrossWeight():0;
			weightUnit = fixUnit(gd.getGrossWeightUnit());
			netWeight += (gd.getNetWeight() != null)?gd.getNetWeight():0;
			volume += (gd.getVolume()!=null)?gd.getVolume():0;
			volumeUnit = fixUnit(gd.getVolumeUnit());
			oilType = gd.getOilType();
			packingType = fixUnit(gd.getPackingType());

			if (gd.getDangerousGoodInformation() != null) {
				imdgClass = gd.getDangerousGoodInformation().getImdgclass();
			}

			if(gd.getPackingType() != null){
			if(packingType.equals("V")){
				Map<String, String> blMap = new HashMap<>();
				blMap.put(bl.getMasterBillOfLading(), bl.getHouseBillOfLading());
				vehicleMap.put(gd.getVehicleVIN(),blMap);
				msnMap.put(gd.getVehicleVIN(),map);
			}
			}

			for (GoodPlacementDto pc : gd.getPlacements()) {
				Map<String, String> blMap = new HashMap<>();
				blMap.put(bl.getMasterBillOfLading(), bl.getHouseBillOfLading());
				containerBlMap.put(pc.getContainerNo(), blMap);
				msnMap.put(pc.getContainerNo(),map);
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
				imdgClass);

		measurement.setDescription(description);
		return measurement;
	}

	private String fixUnit(String unit) {
		if(unit != null) {
			if (unit.equalsIgnoreCase("KGM")) {
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

	private String getTradeType(BillOfLadingDto billOfLadingDto){
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
	private void saveGoodsItems(BillOfLadingDto bl) {
		BlGoodItemsEntity blGoodItemsEntity= new BlGoodItemsEntity();
		blGoodItemsEntity.setMrn(bl.getMrn());
		blGoodItemsEntity.setHouseBillOfLading(bl.getHouseBillOfLading());
		blGoodItemsEntity.setMasterBillOfLading(bl.getMasterBillOfLading());
		blGoodItemsEntity.setGoodsItemNo(bl.getGoodsItemNo());
		blGoodItemsEntity.setLastUpdateId("TESWS");
		blGoodItemsEntity.setFirstRegisterId("TESWS");
		for(GoodsDto goodsDto: bl.getGoodDetails()){
			blGoodItemsEntity.setDescription(goodsDto.getDescription());
			blGoodItemsEntity.setPackageType(goodsDto.getPackageType());
			blGoodItemsEntity.setPackingType(goodsDto.getPackingType());
			blGoodItemsEntity.setPackageQuantity(goodsDto.getPackageQuantity());
			blGoodItemsEntity.setOilType(goodsDto.getOilType());
			blGoodItemsEntity.setInvoiceValue(goodsDto.getInvoiceValue());
			blGoodItemsEntity.setInvoiceCurrency(goodsDto.getInvoiceCurrency());
			blGoodItemsEntity.setFreightCharge(goodsDto.getFreightCharge());
			blGoodItemsEntity.setFreightCurrency(goodsDto.getFreightCurrency());
			blGoodItemsEntity.setGrossWeight(goodsDto.getGrossWeight());
			blGoodItemsEntity.setGrossWeightUnit("KG");
			blGoodItemsEntity.setNetWeight(goodsDto.getNetWeight());
			blGoodItemsEntity.setNetWeightUnit("KG");
			blGoodItemsEntity.setVolume(goodsDto.getVolume());
			blGoodItemsEntity.setVolumeUnit(goodsDto.getVolumeUnit());
			blGoodItemsEntity.setLength(goodsDto.getLength());
			blGoodItemsEntity.setLengthUnit(goodsDto.getLengthUnit());
			blGoodItemsEntity.setWidth(goodsDto.getWidth());
			blGoodItemsEntity.setWidthUnit(goodsDto.getWidthUnit());
			blGoodItemsEntity.setHeight(goodsDto.getHeight());
			blGoodItemsEntity.setHeightUnit(goodsDto.getHeightUnit());
			blGoodItemsEntity.setMarksNumbers(goodsDto.getMarksNumbers());
			blGoodItemsEntity.setVehicleVIN(goodsDto.getVehicleVIN());
			blGoodItemsEntity.setVehicleModel(goodsDto.getVehicleModel());
			blGoodItemsEntity.setVehicleMake(goodsDto.getVehicleMake());
			blGoodItemsEntity.setVehicleOwnDrive(goodsDto.getVehicleOwnDrive());
			blGoodItemsEntity.setClassCode(goodsDto.getDangerousGoodInformation().getClassCode());
			blGoodItemsEntity.setFlashpointValue(goodsDto.getDangerousGoodInformation().getFlashpointValue());
			blGoodItemsEntity.setShipmFlashptValue(goodsDto.getDangerousGoodInformation().getShipmFlashptValue());
			blGoodItemsEntity.setShipmFlashptUnit(goodsDto.getDangerousGoodInformation().getShipmFlashptUnit());
			blGoodItemsEntity.setPackingGroup(goodsDto.getDangerousGoodInformation().getPackingGroup());
			blGoodItemsEntity.setMarPolCategory(goodsDto.getDangerousGoodInformation().getMarPolCategory());
			blGoodItemsEntity.setImdgpage(goodsDto.getDangerousGoodInformation().getImdgpage());
			blGoodItemsEntity.setImdgclass(goodsDto.getDangerousGoodInformation().getImdgclass());
			blGoodItemsEntity.setUnnumber(goodsDto.getDangerousGoodInformation().getUnnumber());
			blGoodItemsEntity.setTremcard(goodsDto.getDangerousGoodInformation().getTremcard());
			blGoodItemsEntity.setMfag(goodsDto.getDangerousGoodInformation().getMfag());
			blGoodItemsEntity.setEms(goodsDto.getDangerousGoodInformation().getEms());
		}
		blGoodItemsRepository.save(blGoodItemsEntity);
	}
}
