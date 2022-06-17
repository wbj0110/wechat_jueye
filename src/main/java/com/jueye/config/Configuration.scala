package com.jueye.config

import com.typesafe.config.ConfigFactory
import util.Try

/**
  * Holds service configuration settings.
  */
trait Configuration {

  /**
    *  config object.
    */
  val config = ConfigFactory.load()



  //solr
  lazy val zkHostString = Try(config.getString("solrj.zk")).getOrElse("solr1:3213,solr2:3213,solr3:3213/solr")
  lazy val collection = Try(config.getString("solrj.collection")).getOrElse("solritemcf")
  lazy val zkConnectTimeout = Try(config.getInt("solrj.zkConnectTimeout")).getOrElse(60000)
  lazy val zkClientTimeout = Try(config.getInt("solrj.zkClientTimeout")).getOrElse(60000)

  lazy val solrHost = Try(config.getString("solr.host")).getOrElse("localhost")
  lazy val solrPort = Try(config.getInt("solr.port")).getOrElse(10000)

  //redis
  lazy val redisHost = Try(config.getString("redis.host")).getOrElse("localhost")
  lazy val redisPort = Try(config.getInt("redis.port")).getOrElse(6379)
  lazy val needCache = Try(config.getBoolean("redis.needCache")).getOrElse(false)

  //image

  lazy val imageHost = Try(config.getString("image.host")).getOrElse("localhost")
  lazy val imagePort = Try(config.getInt("image.port")).getOrElse(80)

  lazy val imageSaveDir = Try(config.getString("image.saveDir")).getOrElse("/data/image/")

  lazy val tulingApi = Try(config.getString("tuling.api")).getOrElse("http://www.tuling123.com/openapi/api?key=211136afa01ac86b0fd381c02d1f49c8&info=")

}
