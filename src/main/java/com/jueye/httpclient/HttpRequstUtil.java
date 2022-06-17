package com.jueye.httpclient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Map;

/**
 * Created by soledede on 2015/10/19.
 */
public class HttpRequstUtil {
    static String defalutUserAgent = "Mozilla/5.0 (Windows NT 8.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.99 Safari/537.36";

    public static HttpRequestBase createRequest(RequestEntity entity) {
        HttpRequestBase httpRequest = null;
        if (entity.getMethod() == Http.GET) {
            httpRequest = new HttpGet(entity.getUrl());
        } else if (entity.getMethod() == Http.POST) {
            httpRequest = new HttpPost(entity.getUrl());
        }
        if (httpRequest != null) {
            // initialize http request headers
            httpRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpRequest.addHeader("Accept-Charset", "utf-8");
            //httpReq.addHeader("Accept-Encoding", "gzip, deflate, sdch")
            httpRequest.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpRequest.addHeader("Connection", "close");
            httpRequest.addHeader("Cache-Control", "max-age=0");
            httpRequest.addHeader("User-Agent", defalutUserAgent);

            if (entity.getHeader() != null && entity.getHeader().size() > 0) {
                for (Map.Entry<String, String> h : entity.getHeader().entrySet()) {
                    httpRequest.addHeader(h.getKey(), h.getValue());
                }
            }
        }
        return httpRequest;
    }
}
