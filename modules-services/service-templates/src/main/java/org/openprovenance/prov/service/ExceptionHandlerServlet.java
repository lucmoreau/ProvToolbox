package org.openprovenance.prov.service;
import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ExceptionHandlerServlet extends HttpServlet {



    // Method
    // To handle GET method request
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {

        // Analyze the servlet exception
        Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String)request.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String)request.getAttribute("javax.servlet.error.request_uri");

        if (servletName == null) {
            servletName = "Unknown";
        }

        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "Error/Exception Information";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 "
                + "transitional//en\">\n";

        out.println(docType + "<html>\n"
                + "<head><title>" + title
                + "</title></head>\n"
                + "<body bgcolor = \"#f0f0f0\">\n");

        if (throwable == null && statusCode == null) {
            out.println("<h1>Error information not found</h1>");
            out.println("Let's go back to <a href=\""
                    + response.encodeURL(
                    "http://localhost:8080/")
                    + "\">Home Page</a>.");
        } else if (statusCode != null) {
            out.println("The status code of an error is : "
                    + statusCode);
        } else {
            out.println("<h2>Error information</h2>");
            out.println("Servlet Name : " + servletName
                    + "</br></br>");
            out.println("Exception Type : "
                    + throwable.getClass().getName()
                    + "</br></br>");
            out.println("The request URI: " + requestUri
                    + "<br><br>");
            out.println("The exception message: "
                    + throwable.getMessage());
        }
        out.println("</body>");
        out.println("</html>");
    }

    // Method
    // To handle POST method request.
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException
    {

        doGet(request, response);
    }
}