package com.adichad.orthanc.config

import java.io.Closeable

class OrthancConfig (
    val appName: String) extends Closeable {
  
  override def toString: String = {
    appName
  }
  
  override def close() {
    
  }
  
  
}