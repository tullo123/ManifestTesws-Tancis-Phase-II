package com.ManifestTeswTancis.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMessage {
   private String teswsEndpointUrl = "http://196.192.79.121:8062/message-api/stakeholder";
   private String contentType;
   private String messageName;
   private String recipient;
   private String callBackUrl = "http://196.43.230.54:7071/tancis/api/callback/v1";
   private String payload;
   
public String getTeswsEndpointUrl() {
	return teswsEndpointUrl;
}
public void setTeswsEndpointUrl(String teswsEndpointUrl) {
	this.teswsEndpointUrl = teswsEndpointUrl;
}
public String getContentType() {
	return contentType;
}
public void setContentType(String contentType) {
	this.contentType = contentType;
}
public String getMessageName() {
	return messageName;
}
public void setMessageName(String messageName) {
	this.messageName = messageName;
}
public String getRecipient() {
	return recipient;
}
public void setRecipient(String recipient) {
	this.recipient = recipient;
}
public String getCallBackUrl() {
	return callBackUrl;
}
public void setCallBackUrl(String callBackUrl) {
	this.callBackUrl = callBackUrl;
}
public String getPayload() {
	return payload;
}
public void setPayload(String payload) {
	this.payload = payload;
}
   
   
}
