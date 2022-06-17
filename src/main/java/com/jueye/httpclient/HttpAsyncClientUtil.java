package com.jueye.httpclient;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * Created by soledede on 2015/10/19.
 */
public class HttpAsyncClientUtil {
    private volatile static HttpAsyncClientUtil singleton;
    CloseableHttpAsyncClient httpClient = null;

    private HttpAsyncClientUtil() {

        int ioThreadCount = 2;
        IOReactorConfig config = IOReactorConfig.custom()
/*    .setTcpNoDelay(true)
    .setSoReuseAddress(false)
    //.setConnectTimeout(0)
    //.setIoThreadCount(ioThreadCount)
    .setRcvBufSize(1024 * 5)
    .setBacklogSize(2000)
    .setSoKeepAlive(false)
    .setInterestOpQueued(true)*/
                .setSoReuseAddress(false)
                .setIoThreadCount(ioThreadCount)
                .setSoTimeout(60 * 1000)
                .setRcvBufSize(1024 * 64)
                .build();
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(config);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setDefaultMaxPerRoute(10);
        cm.setMaxTotal(2000);

        httpClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
        httpClient.start();
    }


    public static HttpAsyncClientUtil getInstance() {
        if (singleton == null) {
            synchronized (HttpAsyncClientUtil.class) {
                if (singleton == null) {
                    singleton = new HttpAsyncClientUtil();
                }
            }
        }
        return singleton;
    }


    public void execute(HttpRequestBase request, HttpClientContext context, ICallback callback) {

        if (httpClient != null) {
            if (context == null) {
                httpClient.execute(request, new FutureCallbackAdapter(callback));
            } else {
                httpClient.execute(request, context, new FutureCallbackAdapter(callback));
            }
        }
    }

}
