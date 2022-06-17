package com.jueye.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by soledede on 2015/10/19.
 */
public class FutureCallbackAdapter implements FutureCallback<HttpResponse> {
    private HttpClientContext httpContext = null;
    private ICallback callback = null;

    public FutureCallbackAdapter(ICallback callback) {
        this.httpContext = httpContext;
        this.callback = callback;
    }

    public FutureCallbackAdapter() {
    }

    public void completed(HttpResponse httpResp) {
        try {
            //      val entity = httpResp.getEntity
            //      val respContent = EntityUtils.toString(entity)
            //      println(respContent)
            callback.callback(httpResp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpResp);
        }
    }

    public void failed(Exception e) {
    }

    public void cancelled() {

    }
}
