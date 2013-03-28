/**
 * Indexer.scala, part of orthanc
 *
 * Copyright 2012 Aditya Varun Chadha ( adi@adichad.com )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.adichad.orthanc.container.servlet

import grizzled.slf4j.Logging
import javax.servlet.http.HttpServlet
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import com.adichad.orthanc.util.Constants
import org.perf4j.StopWatch
import com.adichad.orthanc.config.OrthancConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.{ Boolean ⇒ JBool }

/**
 * @author Aditya Varun Chadha
 * <b>url-syntax</b>: www.adichad.com/orthanc/<schema>
 */
@WebServlet(urlPatterns = Array { "/data/*" })
class DataManager extends HttpServlet with Logging with Constants {
  override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
    response.getWriter.append("Hello, Scala!")
  }

  override def doPost(request: HttpServletRequest, response: HttpServletResponse) {
    val timer = request.getAttribute(TIMER_KEY).asInstanceOf[StopWatch];
    val context = request getServletContext;

    val resources = context
      .getAttribute(CONF_KEY).asInstanceOf[OrthancConfig]

    val reader = new BufferedReader(new InputStreamReader(
      request.getInputStream, "UTF-8"))

    //resourceMapper.mapToAttributes(request, resources)

    val commit = JBool.parseBoolean(request.getParameter("commit"))
    //val innerLog = new TaskStatus
    val requestType = request.getServletPath + request.getPathInfo
    request.setAttribute(REQUEST_GROUP_KEY, requestType)
    //try {
      //request.getAttribute(DATA_MANAGER_KEY).asInstanceOf[DataManager].upsert(
        //reader, innerLog, commit);
      //info("["+requestType+"] ["+timer.getElapsedTime()+" ms] "+innerLog.info)
    //} catch {
      //case e: Exception ⇒ error("["+requestType+"] ["+timer.getElapsedTime()+" ms] "+innerLog.info, e)
    //}

  }

  override def doDelete(request: HttpServletRequest, response: HttpServletResponse) {
    response.getWriter.append("Hello, Scala!")
  }

  override def doPut(request: HttpServletRequest, response: HttpServletResponse) {
    response.getWriter.append("Hello, Scala!")
  }

}