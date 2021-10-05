package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.CoCompanyCodeEntity;
import com.ManifestTeswTancis.Entity.ExImportAmendBl;
import com.ManifestTeswTancis.Entity.ExImportAmendBlContainer;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Service.ExImportManifestAmendService;
import com.ManifestTeswTancis.dtos.Bl;
import com.ManifestTeswTancis.dtos.Containers;
import com.ManifestTeswTancis.dtos.ManifestAmendmentDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
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

   @Autowired
    public ExImportManifestAmendServiceImpl(AmendItemContainerRepository amendItemContainerRepository, BlGoodItemsRepository blGoodItemsRepository, EdNoticeRepository edNoticeRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository, ExImportAmendItemRepository importAmendItemRepository, ExImportAmendBlRepository exImportAmendBlRepository, ExImportManifestRepository exImportManifestRepository, CoCompanyCodeRepository coCompanyCodeRepository) {
        this.amendItemContainerRepository = amendItemContainerRepository;
        this.blGoodItemsRepository = blGoodItemsRepository;
        this.edNoticeRepository = edNoticeRepository;
        this.exImportAmendGeneralRepository = exImportAmendGeneralRepository;
        this.importAmendItemRepository = importAmendItemRepository;
        this.exImportAmendBlRepository = exImportAmendBlRepository;
        this.exImportManifestRepository = exImportManifestRepository;
        this.coCompanyCodeRepository = coCompanyCodeRepository;
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
                List<Containers> containers= manifestAmendmentDto.getContainers();
                setBl(bl,containerBlMap,vehicleMap,amendSerialNoMap);
                if(!containers.isEmpty()){
                    saveContainer(containers, containerBlMap, amendSerialNoMap,bl);
                }
                if(!vehicleMap.isEmpty()){
                    saveVehicles(vehicleMap, amendSerialNoMap,bl);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            responseData.setDescription("Errors in saving manifest Amendment" + exception.getMessage());
            responseData.setCode(500);
        }

        return responseData;
    }

    private void setBl(Bl bl, Map<String, Map<String, String>> containerBlMap,
                       Map<String, Map<String, String>> vehicleMap, Map<String,
                     Map<String, String>> amendSerialNoMap) {
        ExImportAmendBl amendBl = new ExImportAmendBl();
              amendBl.setBlType(bl.getBlType());
    }

    private void saveContainer(List<Containers> containers, Map<String, Map<String, String>> containerBlMap,
                               Map<String, Map<String, String>> amendSerialNoMap, Bl bl) {
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
       }
    }

    private void saveVehicles(Map<String, Map<String, String>> vehicleMap,
                              Map<String, Map<String, String>> amendSerialNoMap, Bl bl) {
    }
}
