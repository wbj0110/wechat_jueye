package com.jueye.token

import java.io.IOException

import com.alibaba.fastjson.JSON
import com.jueye.entity.enumeration.HttpRequestMethodType
import com.jueye.http.HttpClientUtil
import org.apache.http.client.utils.HttpClientUtils
import org.apache.http.util.EntityUtils

/**
  * Created by soledede on 16/3/28.
  */
class UpdateToken {

}


object UpdateToken {
  var accessToken: String = null
  private val thread = new Thread("search log thread ") {
    setDaemon(true)

    override def run() {
      while (true) {
        try {
          val token = UpdateToken.getsAccessToken
          UpdateToken.accessToken = token._1
          Thread.sleep(token._2 * 1000)
        } catch {
          case e: Exception => e.printStackTrace()
        }
      }
    }
  }


   // thread.start()

  def AccessAndSetToken()={
    val token = UpdateToken.getsAccessToken
    UpdateToken.accessToken = token._1
  }

  def getsAccessToken(): (String, Long) = {
    val appId = "xxxxxx"
    val appSecret = "xxxxxxxxxx"
    val url = s"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$appId&secret=$appSecret"

    val response = HttpClientUtil.requestHttpSyn(url, HttpRequestMethodType.GET, null, null, null)

    try {
      val sResponse: String = EntityUtils.toString(response.getEntity)
      val jsonObject = JSON.parseObject(sResponse)
      (jsonObject.getString("access_token"), jsonObject.getLong("expires_in"))
    }
    catch {
      case e: Exception => {
        e.printStackTrace
      }
        null
    } finally {
      HttpClientUtils.closeQuietly(response)
    }
  }

}
