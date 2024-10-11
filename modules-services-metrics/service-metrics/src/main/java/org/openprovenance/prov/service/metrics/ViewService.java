package org.openprovenance.prov.service.metrics;

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
    @Path("/view/documents/{docId}/validation/report.html")
    @Produces("text/html")
    @Operation(summary = "Validation Report --- html representation",
            responses = { @ApiResponse(responseCode = "404", description = "Document not found") })
    @Tag(name= VIEW)
    public void getValidationReportAsHtml(@Context HttpServletResponse response,
                                          @Context HttpServletRequest request,
                                          @PathParam("docId") String visibleId)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/validationReport.html");
        requestDispatcher.forward(request, response);
    }


}
