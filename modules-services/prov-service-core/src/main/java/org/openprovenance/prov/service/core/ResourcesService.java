package org.openprovenance.prov.service.core;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;

import java.io.InputStream;

import static org.openprovenance.prov.model.interop.InteropMediaType.*;
import static org.openprovenance.prov.model.interop.InteropMediaType.MEDIA_APPLICATION_JSONLD;
import static org.openprovenance.prov.service.core.SwaggerTags.RESOURCES;

@Path("")
@Tag(name= RESOURCES)
public class ResourcesService implements SwaggerTags {

    final private InteropFramework interopFramework = new InteropFramework();

    final static Logger logger = LogManager.getLogger(ResourcesService.class);

    public static final String WEBRESOURCES_BASEPATH = "/webresources/";


    @GET
    @Path("/resources/{path: .+}")
    @Tag(name = RESOURCES)
    @Operation(summary = "Returns static resources.",
            description = "No content negotiation allowed here, no dynamic generation.",
            responses = {@ApiResponse(responseCode = "200", description = "Representation of resource"),
                    @ApiResponse(responseCode = "404", description = "Representatoin not found")})
    @Produces({ MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_JSON, MEDIA_APPLICATION_JSONLD, "plain/text" })
    public Response getResource(@Context HttpServletResponse response,
                                @Context HttpServletRequest request,
                                @Parameter(name = "path", description = "document path", required = true) @PathParam("path") String path) {

        logger.info("get resources " + path);

        // find file extension in  path
        String type=path.substring(path.lastIndexOf(".")+1);

        logger.info(" type " + type);

        String mediaType;
        if (!interopFramework.extensionRevMap.containsKey(type)) {
            mediaType="text/plain";
        } else {
            mediaType=interopFramework.getMimeTypeMap().get(interopFramework.extensionRevMap.get(type));
            if (mediaType == null) mediaType = "text/plain";
        }


        String the_path = WEBRESOURCES_BASEPATH + path;

        logger.info(" the_path " + the_path);

        // do not auto-close stream as returned in a closure
        InputStream stream=this.getClass().getClassLoader().getResourceAsStream(the_path);
        StreamingOutput promise;
        if (stream != null) {
            promise = stream::transferTo;
        } else {
            promise = (out) -> {
                out.write(("Resource " + the_path + " not found").getBytes());
            };
        }


        logger.info("media type is " + mediaType);

        return ServiceUtils.composeResponseOK(promise).type(mediaType).build();


    }


}