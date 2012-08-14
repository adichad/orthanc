package com.adichad.orthanc.servlet.context

import javax.servlet.{ ServletRequestListener, ServletRequestEvent }
import grizzled.slf4j.Logging
import com.adichad.orthanc.util.Constants
import org.perf4j.slf4j.Slf4JStopWatch
import org.perf4j.StopWatch

class AppRequestListener extends ServletRequestListener with Logging with Constants {

  def requestInitialized(sre: ServletRequestEvent): Unit = {
    val timer = new Slf4JStopWatch
    sre.getServletRequest.setAttribute(TIMER_KEY, timer)
  }

  def requestDestroyed(sre: ServletRequestEvent): Unit = {
    val req = sre.getServletRequest
    req.getAttribute(TIMER_KEY).asInstanceOf[StopWatch].
      stop(req.getAttribute(REQUEST_GROUP_KEY).asInstanceOf[String]);
  }

}