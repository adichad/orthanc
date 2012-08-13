package com.adichad.orthanc.config

import java.util.Properties

abstract class OrthancConfigurator {
  
  def getAppName: String
  
  def configure(conf: OrthancConfig, properties: Properties): OrthancConfig = {
    new OrthancConfig(getAppName)
  }
  
}