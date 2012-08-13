package com.adichad.orthanc.servlet.context

import java.io.File
import java.io.FileInputStream
import java.util.Properties

import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.mrbean.MrBeanModule
import org.slf4j.LoggerFactory

import com.adichad.orthanc.config.OrthancConfigurator
import com.adichad.orthanc.util.Constants

import ch.qos.logback.classic.LoggerContext
import grizzled.slf4j.Logging
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

class AppContextListener extends ServletContextListener with Logging with Constants {

  def contextInitialized(sce: ServletContextEvent): Unit = {
    // configure logging. this depends on logback.xml being in the classpath
    LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext].putProperty(
      USER_HOME_KEY, System.getProperty(USER_HOME_KEY))

    // load environment.properties from env.dir
    val prop = new Properties
    val fis = new FileInputStream(System.getProperty(ENV_DIR_KEY) + File.separator + ENV_FILE)
    try prop.load(fis)
    finally if (fis != null) fis.close

    // load application specific configuration
    val mapper = new ObjectMapper
    mapper.registerModule(new MrBeanModule)
    val config = mapper.readValue(
      new File(prop.getProperty(CONF_PATH_KEY) + File.separator
        + prop.getProperty(CONF_FILE_KEY)), classOf[OrthancConfigurator]).configure(null, prop)
    info(config)
   
    // load app configuration and environment properties into servlet context
    val context = sce.getServletContext
    
    context.setAttribute(CONF_KEY, config)
    context.setAttribute(ENV_KEY, prop)

    info("app context initialized")
  }

  def contextDestroyed(sce: ServletContextEvent): Unit = {
    info("app context destroyed")
  }

}