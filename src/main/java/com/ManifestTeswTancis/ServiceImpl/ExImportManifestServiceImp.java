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
	final MasterBlGoodItemsRepository masterBlGoodItemsRepository;
	final HouseBlGoodItemsRepository houseBlGoodItemsRepository;

	@Autowired
	public ExImportManifestServiceImp(ExImportManifestRepository exImportManifestRepository, ExImportMasterBlRepository exImportMasterBlRepository,
									  ExImportHouseBlRepository exImportHouseBlRepository, ExImportBlContainerRepository exImportBlContainerRepository,
									  EdNoticeRepository edNoticeRepository, MasterBlGoodItemsRepository masterBlGoodItemsRepository, HouseBlGoodItemsRepository houseBlGoodItemsRepository) {
		this.exImportManifestRepository = exImportManifestRepository;
		this.exImportMasterBlRepository = exImportMasterBlRepository;
		this.exImportHouseBlRepository = exImportHouseBlRepository;
		this.exImportBlContainerRepository = exImportBlContainerRepository;
		this.edNoticeRepository = edNoticeRepository;
		this.masterBlGoodItemsRepository = masterBlGoodItemsRepository;
		this.houseBlGoodItemsRepository = houseBlGoodItemsRepository;
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
					.findFirstByMrn(manifestDto.getManifestReferenceNumber());
			if (callInfEntity.isPresent()) {
				System.out.print("-----------------Saving Manifest method called------------------------");
				ExImportManifest infEntity = callInfEntity.get();
				List<BillOfLadingDto> billOfLadingDtos = manifestDto.getConsignments();
				Map<String, Map<String, String>> containerBlMap = new HashMap<>();
				Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap = new HashMap<>();
				Map<String, Map<String, String>> msnMap = new HashMap<>();
				Map<String, Map<String, String>> vehicleMap = new HashMap<>();

				setConsignments(billOfLadingDtos, infEntity.getMrn(), containerBlMap,vehicleMap,msnMap,containerSaveMap,manifestDto.getContainers());
				List<ContainerDto> containerDtos = manifestDto.getContainers();
				if (!containerDtos.isEmpty()) { saveContainers(msnMap, infEntity.getMrn(),containerSaveMap); }
				if(!vehicleMap.isEmpty()){ saveVehicles(vehicleMap, msnMap, infEntity.getMrn()); }
				this.createEdNotice(infEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseData.setDescription("Error In saving manifest\n" + e.getMessage());
			responseData.setCode(500);
		}

		return responseData;
	}



	private void saveVehicles(Map<String, Map<String, String>> vehicleMap,
							  Map<String, Map<String, String>> msnHsnMap, String mrn){

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

			ExImportBlContainer blContainer = new ExImportBlContainer();
			blContainer.setMrn(mrn);
			blContainer.setContainerNo(set.getKey());
			blContainer.setContainerSize("VH");
			blContainer.setTypeOfContainer("V");
			blContainer.setMasterBillOfLading(masterBl);
			blContainer.setHouseBillOfLading(houseBl);
			blContainer.setMsn(msn);
			blContainer.setHsn(msnMap.get(msn));
			blContainer.setLastUpdateId("TESWS");
			blContainer.setFirstRegisterId("TESWS");

			LocalDateTime localDateTime = LocalDateTime.now();
			blContainer.setFirstRegisterDate(localDateTime);

			exImportBlContainerRepository.save(blContainer);
		}
	}

	private void saveContainers(
			Map<String, Map<String, String>> msnHsnMap, String mrn,
			Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap) {
	     	containerSaveMap.forEach((key,contMap)->{
			contMap.forEach((container,blMap)->{
			ExImportBlContainer cnEn = new ExImportBlContainer();
			BlMeasurement blMeasurement =new  BlMeasurement();
			Map<String, String> msnMap = msnHsnMap.get(container.getContainerNo());
			if (!blMap.isEmpty()) {
				cnEn.setMrn(mrn);
				cnEn.setContainerNo(container.getContainerNo());
				cnEn.setContainerSize(container.getContainerSize());
				cnEn.setTypeOfContainer("C");

				Set<String> mblset = blMap.keySet();
				Iterator<String> iter = mblset.iterator();
				String mBl = iter.next();
				String hBl = (blMap.get(mBl) != null)?blMap.get(mBl):"SIMPLE";

				Set<String> msnSet = msnMap.keySet();
				Iterator<String> msnInter = msnSet.iterator();
				String msn = msnInter.next();

				cnEn.setMasterBillOfLading(mBl);
				cnEn.setHouseBillOfLading(hBl);
				cnEn.setMsn(msn);
				cnEn.setHsn(msnMap.get(msn));

				cnEn.setFreightIndicator(container.getFreightIndicator());
				cnEn.setWeight(container.getWeight());
				cnEn.setWeightUnit(fixUnit(container.getWeightUnit()));
				cnEn.setWeightUnit(fixUnit(container.getGrossWeightUnit()));
				cnEn.setVolume(container.getVolume());
				cnEn.setVolumeUnit("CBM");
				cnEn.setMaximumTemperature(container.getMaximumTemperature());
				cnEn.setMinimumTemperature(container.getMinimumTemperature());
				cnEn.setLastUpdateId("TESWS");
				cnEn.setFirstRegisterId("TESWS");
				cnEn.setContainerPackage(blMeasurement.getPkQuantity());
				cnEn.setPackageUnit(blMeasurement.getPkType());

				if(container.getTemperatureType() != null && container.getTemperatureType().contentEquals("1")) {
					cnEn.setReferPlugYn("Y");
				} else
					cnEn.setReferPlugYn("N");


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
			});
		});
	}

	private void setConsignments(
			List<BillOfLadingDto> billOfLadingDtos,
			String mrn,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap, Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap,
			List<ContainerDto> containers) {
		int i = 1;
		int j = 1;
		Map<String, String> msns = new HashMap<>();
		for (BillOfLadingDto bl : billOfLadingDtos) {
			String msn = String.format("%04d", i);
			msns.put(bl.getMasterBillOfLading(), msn);
			if (bl.getHouseBillOfLading() == null || bl.getHouseBillOfLading().contentEquals(bl.getMasterBillOfLading())) {
				saveMasterBl(bl, mrn, msn, containerBlMap, vehicleMap, msnMap,containerSaveMap,containers);
				i++;
			}
			if (bl.getGoodDetails() != null && bl.getMasterBillOfLading() != null) {
				saveMasterBlGoodItems(bl, mrn, msn);
			}
		}

		for (BillOfLadingDto bl : billOfLadingDtos) {
			if (bl.getHouseBillOfLading() != null && !bl.getHouseBillOfLading().isEmpty()
					&& !bl.getHouseBillOfLading().contentEquals(bl.getMasterBillOfLading())) {
				String msn = msns.get(bl.getMasterBillOfLading());
				System.out.println("msn =" + msn + " and Master =" + bl.getMasterBillOfLading());
				String hsn = String.format("%03d", j);
				saveHouseBl(bl, mrn, msn, hsn, containerBlMap, vehicleMap, msnMap,containerSaveMap,containers);
				j++;

			}
			if (bl.getGoodDetails() != null && bl.getHouseBillOfLading() != null) {
				saveHouseBlGoodItems(bl, mrn);

			}

		}

	}




	private void saveMasterBl(
			BillOfLadingDto bl,
			String mrn,
			String msn,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap,Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap,
			List<ContainerDto> containers) {
		BlMeasurement blMeasurement = getBlMeasurement(bl, msn, "   ", containerBlMap,vehicleMap,msnMap,containerSaveMap,containers);
		System.out.println("generated msn:" + msn);
		ExImportMasterBl exImportMasterBl = new ExImportMasterBl(bl);
		exImportMasterBl.setMrn(mrn);
		exImportMasterBl.setMsn(msn);
		exImportMasterBl.setAuditStatus("NA");
		exImportMasterBl.setTradeType(getTradeType(bl));
		exImportMasterBl.setBlType(getBlType(bl));
		exImportMasterBl.setConsolidatedStatus(getConsolidatedStatus(bl));
		exImportMasterBl.setBlPackage(blMeasurement.getPkQuantity());
		exImportMasterBl.setPackageUnit(blMeasurement.getPkType());
		exImportMasterBl.setGrossWeightUnit(blMeasurement.getWeightUnit());
		exImportMasterBl.setNetWeightUnit(blMeasurement.getWeightUnit());
		exImportMasterBl.setVolume(blMeasurement.getVolume());
		exImportMasterBl.setVolumeUnit(blMeasurement.getVolumeUnit());
		exImportMasterBl.setBlPackingType(bl.getBlPackingType());
		exImportMasterBl.setOilType(blMeasurement.getOilType());
		exImportMasterBl.setImdgclass(blMeasurement.getImdgClass());
		exImportMasterBl.setBlDescription(bl.getBlDescription());
		exImportMasterBl.setInvoiceCurrency(blMeasurement.getInvoiceCurrency());
		exImportMasterBl.setFreightCurrency(blMeasurement.getFreightCurrency());
		exImportMasterBl.setInvoiceValue(blMeasurement.getInvoiceValue());
		exImportMasterBl.setFreightCharge(blMeasurement.getFreightCharge());
		exImportMasterBl.setMarksNumbers(blMeasurement.getMarksNumbers());

		exImportMasterBlRepository.save(exImportMasterBl);


	}


	private void saveHouseBl(
			BillOfLadingDto bl,
			String mrn, String msn,
			String hsn,
			Map<String, Map<String, String>> containerBlMap,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap, Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap,
			List<ContainerDto> containers) {

		BlMeasurement blMeasurement = getBlMeasurement(bl,msn,hsn,containerBlMap,vehicleMap,msnMap,containerSaveMap,containers);
		ExImportHouseBl exImportHouseBl = new ExImportHouseBl(bl);
		exImportHouseBl.setMrn(mrn);
		exImportHouseBl.setMsn(msn);
		exImportHouseBl.setHsn(hsn);
		exImportHouseBl.setTradeType((getTradeType(bl)));
		exImportHouseBl.setBlPackage(blMeasurement.getPkQuantity());
		exImportHouseBl.setPackageUnit(blMeasurement.getPkType());
		exImportHouseBl.setNetWeightUnit(blMeasurement.getWeightUnit());
		exImportHouseBl.setVolumeUnit(blMeasurement.getVolumeUnit());
		exImportHouseBl.setVolume(blMeasurement.getVolume());
		exImportHouseBl.setBlPackingType(bl.getBlPackingType());
		exImportHouseBl.setOilType(blMeasurement.getOilType());
		exImportHouseBl.setImdgclass(blMeasurement.getImdgClass());
		exImportHouseBl.setDescription(bl.getBlDescription());
		exImportHouseBl.setInvoiceCurrency(blMeasurement.getInvoiceCurrency());
		exImportHouseBl.setFreightCurrency(blMeasurement.getFreightCurrency());
		exImportHouseBl.setInvoiceValue(blMeasurement.getInvoiceValue());
		exImportHouseBl.setFreightCharge(blMeasurement.getFreightCharge());
		exImportHouseBl.setMarksNumbers(blMeasurement.getMarksNumbers());


		exImportHouseBlRepository.save(exImportHouseBl);

	}

	private BlMeasurement getBlMeasurement(
			BillOfLadingDto bl,
			String msn, String hsn,
			Map<String, Map<String, String>> containerBlMap ,
			Map<String, Map<String, String>> vehicleMap,
			Map<String, Map<String, String>> msnMap, Map<String,Map<ContainerDto, Map<String, String>>> containerSaveMap,
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
		Map<String, String> map = new HashMap<>();
		map.put(msn,hsn);
		for (GoodsDto gd : bl.getGoodDetails()) {
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
					msnMap.put(gd.getVehicleVIN(),map);
				}
			}
			int i =1;
			for (GoodPlacementDto pc : gd.getPlacements()) {
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
				imdgClass,
				invoiceCurrency,
				freightCurrency,
				invoiceValue,freightCharge,marksNumbers);

		measurement.setDescription(description);
		return measurement;
	}



	private void saveMasterBlGoodItems(BillOfLadingDto bl, String mrn, String msn) {
		MasterBlGoodItemsEntity masterBlGoodItems = new MasterBlGoodItemsEntity(bl);
		masterBlGoodItems.setMrn(mrn);
		masterBlGoodItems.setMsn(msn);
		masterBlGoodItemsRepository.save(masterBlGoodItems);
	}

	private void saveHouseBlGoodItems(BillOfLadingDto bl, String mrn) {
		HouseBlGoodItemsEntity houseBlGoodItems = new HouseBlGoodItemsEntity(bl);
		houseBlGoodItems.setMrn(mrn);
		houseBlGoodItems.setHouseBillOfLading(bl.getHouseBillOfLading());
		houseBlGoodItemsRepository.save(houseBlGoodItems);
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
	private String getBlType(BillOfLadingDto bl) {
		String blType = bl.getBlType();
		if(blType != null && blType.equalsIgnoreCase("SIMPLE") ){
			return "S";
		} else if(blType!=null && blType.equalsIgnoreCase("CONSOLIDATED")){
			return "C";
		}else{
			return bl.getBlType();
		}
	}

	private String getConsolidatedStatus(BillOfLadingDto bl) {
		if(bl.getBlType()!= null && bl.getBlType().equalsIgnoreCase("SIMPLE")){
			return "U";
		}else if(bl.getBlType()!= null && bl.getBlType().equalsIgnoreCase("CONSOLIDATED")){
			return "C";
		}else {
			return bl.getBlType();
		}
	}


	private void createEdNotice(ExImportManifest exImportManifest) throws InterruptedException {

		EdNoticeEntity edNotice = new EdNoticeEntity();
		Optional<EdNoticeEntity> optional = edNoticeRepository.findByDocumentNumber(exImportManifest.getMrn());
		if(!optional.isPresent()) {
			System.out.println(
					"#---------------CreateEdNotice START ---------------#");
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
			edNotice.setReceiverId("INTERNAL");
			edNotice.setSenderId("EXTERNAL");
			edNotice.setDocumentType("D");
			edNotice.setOriginalDocumentCode("CUSMAN201");
			edNotice.setOriginalDocumentNumber(exImportManifest.getMrn());
			edNotice.setTransferType("E");
			edNotice.setProcessingStatus("N");
			edNoticeRepository.save(edNotice);
			System.out.println(
					"#---------------CreateEdNotice END ---------------#");
			TimeUnit.SECONDS.sleep(10);
		}
		else {
			System.out.println("Manifest with This Mrn already submitted to TANCIS INTERNAL.");
		}


	}
}