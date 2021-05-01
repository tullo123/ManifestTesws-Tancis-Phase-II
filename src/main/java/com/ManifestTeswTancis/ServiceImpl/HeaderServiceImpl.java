package com.ManifestTeswTancis.ServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class HeaderServiceImpl {

	public HttpHeaders getHttpHeaders(String message,String callBackUrl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-tesws-msg-name", message);//"MANIFEST_APPROVAL_NOTICE"
		headers.set("x-tesws-callback-url", callBackUrl); //"https://webhook.site/12f0d14d-2630-4ccd-864c-610b2f02251b"
		return headers;
	}
}
