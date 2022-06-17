package com.jueye.httpclient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by soledede on 2015/10/19.
 */
public class HttpClientRequest extends HttpRequest {

    @Override
    public Response download(RequestEntity entity, ICallback callback, RequestType qT) {
        Response result = null;
        String url = entity.getUrl();
        HttpRequestBase request = HttpRequstUtil.createRequest(entity);

        if (entity.getMethod() == Http.POST) {
            java.util.List<BasicNameValuePair> pavPs = new java.util.ArrayList<BasicNameValuePair>();
            Map<String, String> postParameters = entity.getPostParameters();
            for (Map.Entry<String, String> entry : postParameters.entrySet()) {
                pavPs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity postEntity = null;
            try {
                postEntity = new UrlEncodedFormEntity(pavPs, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ((HttpEntityEnclosingRequestBase) request).setEntity(postEntity);
        }

        HttpClientContext context = HttpClientContext.adapt(new BasicHttpContext());
        // Http request start time
        context.setAttribute("requestStartTime", System.currentTimeMillis());
        if(qT==RequestType.ASYNC) {
            HttpAsyncClientUtil.getInstance().execute(request, context, callback);
        }else{
            result = HttpClientUtil.execute(request, context);
        }
        return result;
    }
}
