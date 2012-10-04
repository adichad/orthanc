/**
 * AppContextListener.scala, part of Orthanc
 * Copyright 2012 Aditya Varun Chadha ( adi@adichad.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adichad.orthanc.container.listener

import java.io.File
import java.io.FileInputStream
import java.util.Properties
import com.fasterxml.jackson.module.mrbean.MrBeanModule
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import com.adichad.orthanc.config.OrthancConfigurator
import com.adichad.orthanc.util.Constants
import ch.qos.logback.classic.LoggerContext
import grizzled.slf4j.Logging
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPool.JedisFactory
import redis.clients.jedis.Jedis
import redis.clients.jedis.ShardedJedisPool
import com.adichad.orthanc.config.OrthancConfig

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

    // load configured resources
    val mapper = new ObjectMapper
    mapper.registerModule(new MrBeanModule)
    val config = mapper.readValue(
      new File(prop.getProperty(CONF_PATH_KEY) + File.separator
        + prop.getProperty(CONF_FILE_KEY)), classOf[OrthancConfigurator]).configure(null, prop)
    
    
    val jedisPool = new JedisPool(new JedisPoolConfig, prop.getProperty(REDIS_HOST_KEY), 
        Integer.parseInt(prop.getProperty(REDIS_PORT_KEY)))
    
    
    info(config)
   
    // load app configuration and environment properties into servlet context
    val context = sce.getServletContext
    
    context.setAttribute(CONF_KEY, config)
    context.setAttribute(ENV_KEY, prop)
    context.setAttribute(CACHE_KEY, jedisPool)

    info("app context initialized")
  }

  def contextDestroyed(sce: ServletContextEvent): Unit = {
    val context = sce.getServletContext
    context.removeAttribute(CONF_KEY).asInstanceOf[OrthancConfig].close()
    context.removeAttribute(CACHE_KEY).asInstanceOf[JedisPool].destroy()
    info("app context destroyed")
  }

}