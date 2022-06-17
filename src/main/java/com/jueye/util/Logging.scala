package com.jueye.util

import org.apache.log4j.{ LogManager, PropertyConfigurator }
import org.slf4j.{ Logger, LoggerFactory }
import org.slf4j.impl.StaticLoggerBinder

/**
 * @author soledede
 * @email wengbenjue@163.com
 * record log info
 */

trait Logging extends Serializable{
  @transient private var log_ : Logger = null

  protected def logName = {
    this.getClass.getName.stripSuffix("$")
  }

  protected def log: Logger = {
    if (log_ == null) {
      initializeIfNecessary()
      log_ = LoggerFactory.getLogger(logName)
    }
    log_
  }

  protected def logInfo(msg: => String) {
    if (log.isInfoEnabled) log.info(msg)
  }

  protected def logDebug(msg: => String) {
    if (log.isDebugEnabled) log.debug(msg)
  }

  protected def logTrace(msg: => String) {
    if (log.isTraceEnabled) log.trace(msg)
  }

  protected def logWarning(msg: => String) {
    if (log.isWarnEnabled) log.warn(msg)
  }

  protected def logError(msg: => String) {
    if (log.isErrorEnabled) log.error(msg)
  }

  protected def logInfo(msg: => String, throwable: Throwable) {
    if (log.isInfoEnabled) log.info(msg, throwable)
  }

  protected def logDebug(msg: => String, throwable: Throwable) {
    if (log.isDebugEnabled) log.debug(msg, throwable)
  }

  protected def logTrace(msg: => String, throwable: Throwable) {
    if (log.isTraceEnabled) log.trace(msg, throwable)
  }

  protected def logWarning(msg: => String, throwable: Throwable) {
    if (log.isWarnEnabled) log.warn(msg, throwable)
  }

  protected def logError(msg: => String, throwable: Throwable) {
    if (log.isErrorEnabled) log.error(msg, throwable)
  }

  protected def isTraceEnabled(): Boolean = {
    log.isTraceEnabled
  }

  private def initializeIfNecessary() {
    if (!Logging.initialized) {
      Logging.initLock.synchronized {
        if (!Logging.initialized) {
          initializeLogging()
        }
      }
    }
  }

  private def initializeLogging() {
    val binderClass = StaticLoggerBinder.getSingleton.getLoggerFactoryClassStr
    val usingLog4j12 = "org.slf4j.impl.Log4jLoggerFactory".equals(binderClass)
    //val log4j12Initialized = LogManager.getRootLogger.getAllAppenders.hasMoreElements
    if (usingLog4j12) {
      val osName = System.getProperty("os.name")
      var defaultLogProps = "log4j-defaults.properties"
      if (!osName.toLowerCase().startsWith("windows"))
        defaultLogProps = "log4j-linux.properties"
      if(System.getProperty("log.conf") != null && !System.getProperty("log.conf").trim.equals("")){
        PropertyConfigurator.configure(System.getProperty("log.conf"))
      }else{
        Option(getClass.getClassLoader.getResource(defaultLogProps)) match {
          case Some(url) =>
            PropertyConfigurator.configure(url)
            System.err.println(s"Using Crawler's default log4j profile: $defaultLogProps")
          case None =>
            System.err.println(s"Crawler was unable to load $defaultLogProps")
        }
      }
    }
    Logging.initialized = true

    log
  }
}

private object Logging {
  @volatile private var initialized = false
  val initLock = new Object()
  try {
    val bridgeClass = Class.forName("org.slf4j.bridge.SLF4JBridgeHandler")
    bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null)
    val installed = bridgeClass.getMethod("isInstalled").invoke(null).asInstanceOf[Boolean]
    if (!installed) {
      bridgeClass.getMethod("install").invoke(null)
    }
  } catch {
    case e: ClassNotFoundException =>
  }
}

object JavaLogging extends Logging {
  def javaLogError(msg: String) = {
    logError(msg)
  }

  def javaLogError(msg: String, t: Throwable) = {
    logError(msg, t)
  }

  def javaLogInfo(msg: String): Unit = {
    logError(msg)
  }
}
