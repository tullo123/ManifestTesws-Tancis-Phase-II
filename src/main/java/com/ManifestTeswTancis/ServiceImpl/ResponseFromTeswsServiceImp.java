package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.dtos.*;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Service
public class ResponseFromTeswsServiceImp {

	@Value("${tesws-fowardurl}")
	private String teswsFowardUrl;

	@Value("${tesws-callback-base-url}")
	private String teswsCallbackBaseUrl;

	@Autowired
	ManifestApprovalStatusRepository statusRepository;

	@Autowired
	HeaderServiceImpl headerServiceImpl;

	public boolean manifestApprovalNoticeCallBack(ResponseFromTesws tesws) {
		Optional<ManifestApprovalStatus> statusEntityO = statusRepository
				.findFirstByMrn(tesws.getReceiverMessageReferenceNumber());
		if (tesws.isError()) {
			if (statusEntityO.isPresent()) {
				ManifestApprovalStatus statusEntity = statusEntityO.get();
				statusEntity.setApprovedStatus(false);
				statusRepository.save(statusEntity);
			}
		} else {
			if (statusEntityO.isPresent()) {
				ManifestApprovalStatus statusEntity = statusEntityO.get();
				statusEntity.setApprovedNoticeStatus(true);
				statusRepository.save(statusEntity);
				return true;
			}
		}
		return false;
	}

	public boolean manifestMrnSentCallBack(ResponseFromTesws tesws) {
		Optional<ManifestApprovalStatus> statusEntityO = statusRepository
				.findFirstByMrn(tesws.getReceiverMessageReferenceNumber());
		if (tesws.isError()) {
			if (statusEntityO.isPresent()) {
				ManifestApprovalStatus statusEntity = statusEntityO.get();
				statusEntity.setMrnStatus(false);
				statusRepository.save(statusEntity);
			}
		} else {
			if (statusEntityO.isPresent()) {
				ManifestApprovalStatus statusEntity = statusEntityO.get();
				statusEntity.setMrnStatusFeedback(true);
				statusRepository.save(statusEntity);
				return true;
			}
		}
		return false;
	}

	public boolean sendStructuredResponse(Object responseObject, String smsType, String callbackUrl) {
		RestTemplate restTemplate = new RestTemplate();
		InetAddress inetAddress;
		String baseUrl = "";
		try {
			inetAddress = InetAddress.getLocalHost();
			baseUrl = inetAddress.getHostAddress();
		} catch (UnknownHostException e1) {
			System.err.println(e1.getMessage());
		}

		baseUrl = !teswsCallbackBaseUrl.isEmpty() ? teswsCallbackBaseUrl : baseUrl;
		HttpHeaders headers = headerServiceImpl.getHttpHeaders(smsType, teswsCallbackBaseUrl + callbackUrl);

		HttpEntity<Object> entity = new HttpEntity<Object>(responseObject, headers);
		try {
			String responseBody = restTemplate.exchange(teswsFowardUrl, HttpMethod.POST, entity, String.class)
					.getBody();

			System.err.println("====================== Response Body Start ========================");

			System.out.println(responseBody);

			System.err.println("====================== Response Body End ===========================");

			return true;
		} catch (Exception e) {
			System.err.println("====================== Sending Error Start ======================");
			System.err.println(e.getMessage());
			System.err.println("====================== Sending Error End ========================");
		}
		return false;
	}

}
