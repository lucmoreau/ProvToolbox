package org.openprovenance.prov.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.openprovenance.prov.service.core.SwaggerTags;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;

import java.io.IOException;


@Path("")
public class ViewService implements SwaggerTags {
    static Logger logger = LogManager.getLogger(ViewService.class);







    @GET
    @Path("/documents/{docId}/validation/report.html")
    @Produces("text/html")
    @Operation(summary = "Validation Report --- html representation",
            responses = { @ApiResponse(responseCode = "404", description = "Document not found") })
    @Tag(name= VIEW)
    public void getValidationReportAsHtml(@Context HttpServletResponse response,
                                          @Context HttpServletRequest request,
                                          @PathParam("docId") String visibleId)
            throws ServletException, IOException {
        logger.info("getValidationReportAsHtml: " + visibleId +  ", url:" + request.getRequestURL());
        //request.getRequestDispatcher("../../../../validationReport.html").forward(request, response);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/validationReport.html");
        logger.info("getValidationReportAsHtml:  url:" + request.getRequestURL() + " dispatcher: " + requestDispatcher);
        requestDispatcher.forward(request, response);
    }


}
