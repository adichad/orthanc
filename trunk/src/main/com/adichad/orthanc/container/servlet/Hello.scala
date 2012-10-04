/**
 * Hello.scala, part of Orthanc
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

import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }
import javax.servlet.annotation.WebServlet
import grizzled.slf4j.Logging

package com.adichad.orthanc.container.servlet {
  
  @WebServlet(urlPatterns = Array{"/hello"})
  class Hello extends HttpServlet with Logging {
    override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
      response.getWriter.append("Hello, Scala!")
    }
    
    override def doPost(request: HttpServletRequest, response: HttpServletResponse) {
      response.getWriter.append("Hello, Scala!")
    }
  }
}