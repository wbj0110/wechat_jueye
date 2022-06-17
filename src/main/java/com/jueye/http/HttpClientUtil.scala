package com.jueye.http

import java.io.{IOException, File}

import akka.event.slf4j.SLF4JLogging
import com.jueye.entity.enumeration.HttpRequestMethodType
import com.jueye.token.UpdateToken
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpPost, HttpEntityEnclosingRequestBase, HttpUriRequest, CloseableHttpResponse}
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HttpContext
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.client.utils.HttpClientUtils
import org.apache.http.client.config.RequestConfig
import org.apache.http.{HttpEntity, HttpHost, HttpResponse}
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.util.EntityUtils
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.map.ObjectMapper

import scala.collection.JavaConversions._


class HttpClientUtil private extends SLF4JLogging {

  // use Proxy
  def execute(request: HttpUriRequest, proxy: HttpHost, callback: (HttpContext, CloseableHttpResponse) => Unit): Unit = {
    val context: HttpClientContext = HttpClientContext.adapt(new BasicHttpContext);
    val reqCfg = RequestConfig.custom().setProxy(proxy).build();
    context.setRequestConfig(reqCfg)
    this.execute(request, context, callback)
  }


  def execute(request: HttpUriRequest, context: HttpContext, callback: (HttpContext, CloseableHttpResponse) => Unit): Unit = {
    var httpResp: CloseableHttpResponse = null
    try {
      if (context == null) {
        httpResp = HttpClientUtil.httpClient.execute(request)
      } else {
        httpResp = HttpClientUtil.httpClient.execute(request, context)
      }
      callback(context, httpResp)
    } catch {
      case t: Throwable => log.error("Http Error", t)
    }
  }

  def execute(request: HttpUriRequest, callback: (HttpContext, CloseableHttpResponse) => Unit): Unit = {
    this.execute(request, null.asInstanceOf[HttpContext], callback)
  }

  def executeSyn(request: HttpUriRequest, context: HttpContext): CloseableHttpResponse = {
    var httpResp: CloseableHttpResponse = null
    try {
      if (context == null) {
        httpResp = HttpClientUtil.httpClient.execute(request)
      } else {
        httpResp = HttpClientUtil.httpClient.execute(request, context)
      }
    } catch {
      case t: Throwable => log.error("Http Error", t)
    }
    httpResp
  }

  def executeSyn(request: HttpUriRequest): CloseableHttpResponse = {
    this.executeSyn(request, null.asInstanceOf[HttpContext])
  }


}

object HttpClientUtil {

  private val httpClient: CloseableHttpClient = HttpClients.createDefault();

  private val instance: HttpClientUtil = new HttpClientUtil

  def getInstance(): HttpClientUtil = {
    return instance
  }

  def closeHttpClient = HttpClientUtils.closeQuietly(httpClient);


  def uploadFile(name: String, file: File, uploadServerUrl: String, fileName: String, textParameters: java.util.Map[String, String]): CloseableHttpResponse = {

    val uploadFile: HttpPost = new HttpPost(uploadServerUrl)

    val builder: MultipartEntityBuilder = MultipartEntityBuilder.create()
    if (textParameters != null && textParameters.size() > 0) {
      textParameters.foreach { case (k, v) =>
        builder.addTextBody(k, v, ContentType.TEXT_PLAIN)
      }
    }

    builder.addBinaryBody(name, file, ContentType.APPLICATION_OCTET_STREAM, fileName)
    val multipart: HttpEntity = builder.build()

    uploadFile.setEntity(multipart)

    val response: CloseableHttpResponse = httpClient.execute(uploadFile)
    response
  }


  def requestHttpSyn(url: String, requestType: String, paremeters: java.util.Map[String, Object], headers: java.util.Map[String, String]): CloseableHttpResponse = {
    var rType = HttpRequestMethodType.GET
    requestType match {
      case "post" => rType = HttpRequestMethodType.POST
      case _ => null
    }
    requestHttpSyn(url, rType, paremeters, headers, null)
  }

  def requestHttpSyn(url: String, requestType: HttpRequestMethodType.Type, paremeters: java.util.Map[String, Object], headers: java.util.Map[String, String], contexts: java.util.Map[String, String]): CloseableHttpResponse = {
    val request = HttpRequstUtil.createRequest(requestType, url)
    var isJson = false
    if (headers != null && !headers.isEmpty) {
      headers.foreach { header =>
        val key = header._1
        val value = header._2
        if (key.toLowerCase.trim.equalsIgnoreCase("content-type")) {
          if (value.toLowerCase.trim.equalsIgnoreCase("application/json")) isJson = true
        }
        request.addHeader(key, value)
      }
    }
    val context: HttpClientContext = HttpClientContext.adapt(new BasicHttpContext)
    if (contexts != null && !contexts.isEmpty) {
      contexts.foreach { attr =>
        context.setAttribute(attr._1, attr._2)
      }
    }

    if (paremeters != null && !paremeters.isEmpty) {
      val formparams: java.util.List[BasicNameValuePair] = new java.util.ArrayList[BasicNameValuePair]()
      paremeters.foreach { p =>
        formparams.add(new BasicNameValuePair(p._1, p._2.toString))
      }

      var entity: StringEntity = null
      if (isJson) {
        val mapper = new ObjectMapper()
        val jStirng = mapper.writeValueAsString(paremeters)
        println("string json:" + jStirng)
        entity = new StringEntity(jStirng, "utf-8");
      } else entity = new UrlEncodedFormEntity(formparams, "utf-8");

      request.asInstanceOf[HttpEntityEnclosingRequestBase].setEntity(entity)
    }
    HttpClientUtil.getInstance().executeSyn(request, context)

  }


  def requestHttp(url: String, requestType: String, paremeters: java.util.Map[String, Object], headers: java.util.Map[String, String], callback: (HttpContext, HttpResponse) => Unit): Unit = {
    var rType = HttpRequestMethodType.GET
    requestType match {
      case "post" => rType = HttpRequestMethodType.POST
      case _ => null
    }
    requestHttp(url, rType, paremeters, headers, null, callback)
  }

  def requestHttp(url: String, requestType: HttpRequestMethodType.Type, paremeters: java.util.Map[String, Object], headers: java.util.Map[String, String], contexts: java.util.Map[String, String], callback: (HttpContext, HttpResponse) => Unit): Unit = {
    val request = HttpRequstUtil.createRequest(requestType, url)
    var isJson = false
    if (headers != null && !headers.isEmpty) {
      headers.foreach { header =>
        val key = header._1
        val value = header._2
        if (key.toLowerCase.trim.equalsIgnoreCase("content-type")) {
          if (value.toLowerCase.trim.equalsIgnoreCase("application/json")) isJson = true
        }
        request.addHeader(key, value)
      }
    }
    val context: HttpClientContext = HttpClientContext.adapt(new BasicHttpContext)
    if (contexts != null && !contexts.isEmpty) {

      contexts.foreach { attr =>
        context.setAttribute(attr._1, attr._2)
      }
    }

    if (paremeters != null && !paremeters.isEmpty) {
      val formparams: java.util.List[BasicNameValuePair] = new java.util.ArrayList[BasicNameValuePair]()
      paremeters.foreach { p =>
        formparams.add(new BasicNameValuePair(p._1, p._2.toString))
      }

      var entity: StringEntity = null
      if (isJson) {
        val mapper = new ObjectMapper()
        val jStirng = mapper.writeValueAsString(paremeters)
        println("string json:" + jStirng)
        entity = new StringEntity(jStirng, "utf-8");
      } else entity = new UrlEncodedFormEntity(formparams, "utf-8");

      request.asInstanceOf[HttpEntityEnclosingRequestBase].setEntity(entity)
    }
    HttpClientUtil.getInstance().execute(request, context, callback)
  }
}

object TestHttpClientUtil {

  def main(args: Array[String]): Unit = {


  }



}