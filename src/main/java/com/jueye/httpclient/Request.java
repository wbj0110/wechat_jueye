package com.jueye.httpclient;

/**
 * Created by soledede on 2015/10/19.
 */
public interface Request {

    public Response download(RequestEntity entity,Http method,ICallback callback);
}
