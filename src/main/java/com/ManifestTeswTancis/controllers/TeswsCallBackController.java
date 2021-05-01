
package com.ManifestTeswTancis.controllers;

import com.ManifestTeswTancis.dtos.ErrorClass;
import com.ManifestTeswTancis.dtos.ResponseFromTesws;
import com.ManifestTeswTancis.ServiceImpl.HeaderServiceImpl;
import com.ManifestTeswTancis.ServiceImpl.ResponseFromTeswsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/callback")
public class TeswsCallBackController {

	@Autowired
	ResponseFromTeswsServiceImp responseFromTeswsServiceImp;

	@Autowired
	HeaderServiceImpl headerServiceImpl;

	@PostMapping(value = "/manifest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> receiveManifest(@RequestBody ResponseFromTesws manifestDto) {
		boolean res = responseFromTeswsServiceImp.manifestApprovalNoticeCallBack(manifestDto);
        System.err.println(manifestDto);
		/*
		 * try { System.err.println(InetAddress.getLocalHost()); ; } catch
		 * (UnknownHostException e) { // TODO Auto-generated catch block
		 * System.err.println(e.getMessage()); }
		 */

		return this.returnResponse(manifestDto, res);
	}

	/**
	 * @param manifestDto
	 * @return
	 * 
	 */
	private ResponseEntity<Object> returnResponse(ResponseFromTesws manifestDto, boolean status) {
		HttpHeaders headers = headerServiceImpl.getHttpHeaders("TESWS_SUPPLEMENTARY_MESSAGE",
				"https://webhook.site/12f0d14d-2630-4ccd-864c-610b2f02251b");
		ResponseFromTesws tesws = new ResponseFromTesws();
		ErrorClass errorClass = new ErrorClass();
		List<ErrorClass> errorClasses = new ArrayList<ErrorClass>();
		errorClass.setCode("404");
		errorClass.setDescription("Mrn was not found");
		tesws.setControlReferenceNumber(manifestDto.getControlReferenceNumber());
		tesws.setReceiverMessageReferenceNumber(manifestDto.getControlReferenceNumber());
		tesws.setError(true);
		tesws.setMessageTypeId("APERAK");
		LocalDateTime preparationDateTime = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		tesws.setPreparationDateTime(preparationDateTime.format(dateFormat));
		tesws.setResponses(errorClasses);
		if (!status) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).headers(headers)
					.body(tesws);
		}
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).headers(headers)
				.body("Received");
	}

	@PostMapping(value = "/mrn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> receiveMrn(@RequestBody ResponseFromTesws manifestDto) {
		boolean res = responseFromTeswsServiceImp.manifestMrnSentCallBack(manifestDto);
System.err.println(manifestDto);
		return this.returnResponse(manifestDto, res);

	}
}
