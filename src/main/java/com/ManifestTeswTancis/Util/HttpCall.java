package com.ManifestTeswTancis.Util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpCall {
    public String httpRequest(HttpMessage httpMessage) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Throwable var6 = null;

        String responseString;
        try {
            HttpPost postRequest = new HttpPost(httpMessage.getTeswsEndpointUrl());
            postRequest.addHeader("x-tesws-msg-name", httpMessage.getMessageName());
            postRequest.addHeader("Content-Type", httpMessage.getContentType());
            postRequest.addHeader("x-tesws-msg-recepients", httpMessage.getRecipient());
            postRequest.addHeader("x-tesws-callback-url", httpMessage.getCallBackUrl());
            StringEntity userEntity = new StringEntity(httpMessage.getPayload());
            postRequest.setEntity(userEntity);
            CloseableHttpResponse response = httpClient.execute(postRequest);
            System.out.println("\n--------------Response-----------------\n");
            responseString = EntityUtils.toString(response.getEntity());
            System.out.println(responseString);
            System.out.println("\n--------------Response-----------------\n");
            response.close();
        } catch (Throwable var19) {
            var6 = var19;
            throw var19;
        } finally {
            if (httpClient != null) {
                if (var6 != null) {
                    try {
                        httpClient.close();
                    } catch (Throwable var18) {
                        var6.addSuppressed(var18);
                    }
                } else {
                    httpClient.close();
                }
            }
        }

        return responseString;
    }
}
