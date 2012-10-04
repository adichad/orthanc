/**
 * AppRequestListener.scala, part of Orthanc
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