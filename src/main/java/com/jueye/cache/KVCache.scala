package com.jueye.cache

import com.jueye.cache.impl.RedisCache
import com.jueye.entity.RecommendResult

import scala.reflect.ClassTag


/**
  * Created by soledede on 2015/12/18.
  */
trait KVCache {

  def put(key: String, value: Seq[RecommendResult], expiredTime: Long = 60 * 60 * 17): Boolean = false

  def get(key: String): Seq[RecommendResult] = null

  def putStringList(key: String, value: List[String], expiredTime  : Long = 60 * 60 * 17): Boolean = false

  def getStringList(key: String): List[String] = null

  def getCache[T: ClassTag](key: String): T = null.asInstanceOf[T]

  def putCache[T: ClassTag](key: String, value: T, expiredTime: Long = 60 * 60 * 17): Boolean = false

}

object KVCache { 
  def apply(kvType: String = "redis"): KVCache = {
    kvType match {
      case "redis" => RedisCache()
      case _ => null
    }

  }
}
