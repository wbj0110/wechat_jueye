package com.jueye.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by soledede on 2015/10/19.
 */
public class HttpClientUtil {
    static CloseableHttpClient httpClient = HttpClients.createDefault();


    public static Response execute(HttpRequestBase request, HttpClientContext context) {
        Response result = new Response();

        CloseableHttpResponse httpResp = null;
        try {
            if (context == null) {
                httpResp = httpClient.execute(request);
            } else {
                httpResp = httpClient.execute(request, context);
            }

           int statuCode = httpResp.getStatusLine().getStatusCode();
            if(statuCode==200){
               HttpEntity entity =  httpResp.getEntity();
                result.setContent(EntityUtils.toByteArray(entity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            HttpClientUtils.closeQuietly(httpResp);
        }
        return result;
    }
}
