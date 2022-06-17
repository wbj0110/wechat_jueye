package com.jueye.httpclient;

import java.util.Map;

/**
 * Created by soledede on 2015/10/19.
 */
public class RequestEntity {

    private String url;
    private Http method = Http.GET;
    private Map<String, String> header;

    private Map<String, String> postParameters;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getPostParameters() {
        return postParameters;
    }

    public void setPostParameters(Map<String, String> postParameters) {
        this.postParameters = postParameters;
    }

    public Http getMethod() {
        return method;
    }

    public void setMethod(Http method) {
        this.method = method;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}
