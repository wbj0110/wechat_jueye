package com.jueye.redis

import com.jueye.config.Configuration
import com.jueye.util.Constant

/**
  * Created by soledede on 2015/12/18.
  */
object RedisClient extends Configuration{



  var redisClient: redis.RedisClient =null
  implicit var akkaSystem: akka.actor.ActorSystem = null

  if(needCache){
    akkaSystem = akka.actor.ActorSystem()
    redisClient  = redis.RedisClient(port = Constant.redisPort, host = Constant.redisHost)
  }


  def apply() = {
    redisClient
  }

  def close() = {
    if(redisClient!=null)
    redisClient.shutdown()
    if(akkaSystem!=null)
    akkaSystem.shutdown()
  }

}
