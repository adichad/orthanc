
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }
import javax.servlet.annotation.WebServlet

package com.adichad.orthanc.servlet {
  
  @WebServlet(urlPatterns = Array{"/hello"})
  class Hello extends HttpServlet {
    override def doGet(request: HttpServletRequest, response: HttpServletResponse) {
      response.getWriter.append("Hello, Scala!")
    }
  }
}