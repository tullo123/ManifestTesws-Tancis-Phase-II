package com.ManifestTeswTancis.controllers.api;
import com.ManifestTeswTancis.dtos.ManifestDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Request.PortCallIdRequestModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ManifestTeswTancis.dtos.*;

import javax.validation.Valid;


@RequestMapping("/tancis/api")
public interface ManifestApi {
    @RequestMapping(value = "/manifest/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> receiveManifest(@RequestBody @Valid ManifestDto manifestDto);

    @RequestMapping(value = "/callInf/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> createCallInfo(@RequestBody @Valid PortCallIdRequestModel callInfDetails);


    @RequestMapping(value = "update/callInf/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> updateCallInf(@RequestBody @Valid UpdateVesselDto updateVesselDto);

    @RequestMapping(value = "receives/customClearance/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> customService(@RequestBody @Valid CustomClearanceDto customClearanceDto);


    @RequestMapping(value = "/callbcak/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<String> callbackUrl(@RequestBody Object object, @RequestHeader("reqId") String reqId);

    @RequestMapping(value = "/savePortClearanceNotice/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> savePortClearanceNotice(@RequestBody @Valid PortClearanceNoticeDto portClearanceNoticeDto);


    @RequestMapping(value = "/saveVesselBoarding/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> saveVesselBoarding(@RequestBody @Valid VesselBoardingNotificationDto vesselBoardingNotificationDto);


    @RequestMapping(value = "/saveVesselDepartureNotice/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> saveVesselDepartureNotice(@RequestBody @Valid VesselDepartureNoticeDto vesselDepartureNoticeDto);

    @RequestMapping(value = "/cancels/callInf/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> deleteVesselInfo(@RequestBody CallInfCancelDto callInfCancelDto);

    @RequestMapping(value = "/saveLiquidBulkDischargeSequence/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> saveLiquidBulkDischargeSequence(@RequestBody LiquidBulkDischargeSequence liquidBulkDischargeSequence);

    @RequestMapping(value = "/saveLiquidBulkDischargeSequenceUpdate/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> saveLiquidBulkDischargeSequenceUpdate(@RequestBody LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto);


    @RequestMapping(value = "/saveLiquidBulkQualityReport/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> saveLiquidBulkQualityReport(@RequestBody LiquidBulkQualityReportDto liquidBulkQualityReportDto);

    @RequestMapping(value = "/saveLiquidBulkDischargeSequenceUpdate/final/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> finalDischargeSequence(@RequestBody LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto);

    @RequestMapping(value = "vessel/tracking/notice/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> vesselTracking(@RequestBody @Valid VesselTrackingNotice vesselTrackingNotice);

    @RequestMapping(value = "freePratiqueReport/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> freePartique(@RequestBody @Valid FreePratiqueReport freePratiqueReport);

    @RequestMapping(value = "update/freePratiqueReport/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> updateFreePartiqueReport(@RequestBody @Valid FreePratiqueReport freePratiqueReport);

    @RequestMapping(value = "vesselDocumentation/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> vesselDocumentation(@RequestBody @Valid VesselDocumentation vesselDocumentation);

    @RequestMapping(value = "vesselDocumentation/update/v1", method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<TeswsResponse> vesselDocumentationUpdate(@RequestBody @Valid VesselDocumentation vesselDocumentation);



}
