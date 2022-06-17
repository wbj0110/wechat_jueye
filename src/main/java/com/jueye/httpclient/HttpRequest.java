package com.jueye.httpclient;

/**
 * Created by soledede on 2015/10/19.
 */
 public abstract class HttpRequest{

    public abstract Response download(RequestEntity entity,ICallback callback,RequestType qT);
}
