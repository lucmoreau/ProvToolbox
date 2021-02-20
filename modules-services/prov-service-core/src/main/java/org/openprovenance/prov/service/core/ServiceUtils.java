package org.openprovenance.prov.service.core;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.ParserException;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.service.core.memory.LRUHashMap;
import org.openprovenance.prov.storage.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class ServiceUtils {

    public static final String DOCUMENT_NOT_FOUND = "Document not found";
    public static final String WILDCARD = "*";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String HEADER_PARAM_ACCEPT = "Accept";

    private static String fileName = "config.properties";

    public static final String UPLOADED_FILE_PATH = getPropertiesFromClasspath(fileName).getProperty("upload.directory");

    public static final String containerVersion = getPropertiesFromClasspath(fileName).getProperty("container.version");
    public static final String containerClassifier = getPropertiesFromClasspath(fileName).getProperty("container.classifier");

    public static final String longContainerVersion = "ProvToolbox/modules-services " + containerVersion + (((containerClassifier==null) || (containerClassifier=="")) ? "" : "-" + containerClassifier) + " (" + getPropertiesFromClasspath(fileName).getProperty("timestamp") + ")";

    private final JobManagement jobManager;



    private final NonDocumentResourceIndex<NonDocumentResource> nonDocumentResourceIndex;
    private final NonDocumentResourceStorage nonDocumentResourceStorage;
    private final InteropFramework interop;

    public Map<String, NonDocumentGenericResourceStorage<?>> getGenericResourceStorageMap() {
        return genericResourceStorageMap;
    }

    private final Map<String, NonDocumentGenericResourceStorage<?>> genericResourceStorageMap;

    public ServiceUtilsConfig getConfig() {
        return config;
    }

    private final ServiceUtilsConfig config;

    private static Properties getPropertiesFromClasspath(String propFileName) {
        return Configuration.getPropertiesFromClasspath(ServiceUtils.class, propFileName);
    }

    private static Logger logger = LogManager.getLogger(ServiceUtils.class);



    private final ResourceStorage storageManager;

    private final ResourceIndex<DocumentResource> documentResourceIndex;

    private final Map<String, ResourceIndex<?>> extensionMap;


    private final ProvFactory pFactory;

    public ServiceUtils(PostService postService, ServiceUtilsConfig config) {
        this.config=config;
        this.storageManager=config.storageManager;
        this.pFactory=config.pFactory;
        this.interop=new InteropFramework(config.pFactory);

        this.documentResourceIndex=(ResourceIndex<DocumentResource>)config.extensionMap.get(DocumentResource.getResourceKind());
        this.nonDocumentResourceIndex=config.nonDocumentResourceIndex;
        this.nonDocumentResourceStorage=config.nonDocumentResourceStorage;
        this.genericResourceStorageMap=config.genericResourceStorageMap;
        this.extensionMap =config.extensionMap;
        jobManager=postService.getJobManager();
        documentCache=new LRUHashMap<>(config.documentCacheSize);
    }

    public NonDocumentResourceStorage getNonDocumentResourceStorage() {
        return nonDocumentResourceStorage;
    }

    public ResourceIndex<DocumentResource> getDocumentResourceIndex() {
        return documentResourceIndex;
    }

    public ResourceStorage getStorageManager() {
        return storageManager;
    }

    final public Map<String, ResourceIndex<?>> getExtensionMap() { return extensionMap;}

    public NonDocumentResourceIndex<NonDocumentResource> getNonDocumentResourceIndex() {
        return nonDocumentResourceIndex;
    }

    public ProvFactory getProvFactory() {
        return pFactory;
    }
    public Destination getDestination(Map<String, List<InputPart>> formData) throws IOException {
        if (formData.get("translate") != null) {
            String val = getFormDataValue(formData, "translate");
            if (val != null) {
                if ("json".equals(val))
                    return Destination.JSON;
                if ("jsonld".equals(val))
                    return Destination.JSONLD;
                if ("provx".equals(val))
                    return Destination.XML;
                if ("provn".equals(val))
                    return Destination.PROVN;
                if ("turtle".equals(val))
                    return Destination.TURTLE;
                if ("trig".equals(val))
                    return Destination.TRIG;
                if ("svg".equals(val))
                    return Destination.SVG;
                if ("jpg".equals(val))
                    return Destination.JPG;
            }
        }
        if (formData.get("expand") != null) {
            String val = getFormDataValue(formData, "expand");
            if (val != null) {
                if ("json".equals(val))
                    return Destination.JSON;
                if ("jsonld".equals(val))
                    return Destination.JSONLD;
                if ("provx".equals(val))
                    return Destination.XML;
                if ("provn".equals(val))
                    return Destination.PROVN;
                if ("turtle".equals(val))
                    return Destination.TURTLE;
                if ("trig".equals(val))
                    return Destination.TRIG;
                if ("svg".equals(val))
                    return Destination.SVG;
                return Destination.UNKNOWN;
            }
        }
        if (formData.get("summarise") != null) {
            String val = getFormDataValue(formData, "summarise");
            if (val != null) {
                if ("json".equals(val))
                    return Destination.JSON;
                if ("provx".equals(val))
                    return Destination.XML;
                if ("provn".equals(val))
                    return Destination.PROVN;
                if ("turtle".equals(val))
                    return Destination.TURTLE;
                if ("trig".equals(val))
                    return Destination.TRIG;
                if ("svg".equals(val))
                    return Destination.SVG;
                return Destination.UNKNOWN;
            }
        }
        return Destination.UNKNOWN;
    }

    public JobManagement getJobManager() {
        return jobManager;
    }

    public enum Action {
        UNKNOWN("UNKNOWN"), VALIDATE("VALIDATE"), EXPAND("EXPAND"), NF("NF"), SIGN("SIGN"), SIGNATURE("SIGNATURE)"), CHECK("CHECK"), RANDOM("RANDOM"), LINEAR("LINEAR"), EXPLANATION("EXPLANATION"), TRANSLATE("TRANSLATE"), SUMMARISE("SUMMARISE"), UPLOAD("UPLOAD");
        // JSON("json"), XML("provx"), PROVN("provn"), TURTLE("ttl"), TRIG("trig"), SVG("svg"), JPG("jpg") ;

        Action(String text) {
            this.text = text;
        }

        private final String text;

        public String toString() {
            return text;
        }
    }

    public enum Destination {
        UNKNOWN("UNKNOWN"), JSON("json"), XML("provx"), PROVN("provn"), TURTLE("ttl"), TRIG("trig"), SVG("svg"), JPG("jpg"), JSONLD("jsonld") ;
        private Destination(String text) {
            this.text = text;
        }

        private final String text;

        public String toString() {
            return text;
        }
    }

    public String getFormDataValue(Map<String, List<InputPart>> formData,
                                   String name) throws IOException {
        List<InputPart> furtherInputParts = formData.get(name);
        if ((furtherInputParts != null) && (furtherInputParts.size() > 0)) {
            return furtherInputParts.get(0).getBodyAsString();
        } else {
            return null;
        }
    }

    public Action getAction(Map<String, List<InputPart>> formData)
            throws IOException {
        if (formData.get("validate") != null)
            return Action.VALIDATE;

        if (formData.get("check") != null)
            return Action.CHECK;

        if (formData.get("upload") != null)
            return Action.UPLOAD;

        if (formData.get("translate") != null) {
            return Action.TRANSLATE;
        }

        if (formData.get("sign") != null) {
            String val = getFormDataValue(formData, "sign");
            if (val != null) {
                if ("NF".equals(val))
                    return Action.NF;
                if ("Sign".equals(val))
                    return Action.SIGN;
                if ("Signature".equals(val))
                    return Action.SIGNATURE;
                return Action.UNKNOWN;
            }
        }
        if (formData.get("narrate") != null) {
            String val = getFormDataValue(formData, "narrate");
            if (val != null) {
                if ("Random".equals(val))
                    return Action.RANDOM;
                if ("Linear".equals(val))
                    return Action.LINEAR;
                return Action.UNKNOWN;
            }
        }
        if (formData.get("explain") != null) {
            String val = getFormDataValue(formData, "explain");
            if (val != null) {
                if ("Explanation".equals(val))
                    return Action.EXPLANATION;
                return Action.UNKNOWN;
            }
        }
        if (formData.get("summarise") != null) {
            return Action.SUMMARISE;
        }

        if (formData.get("expand") != null) {
            return Action.EXPAND;
        }
        return Action.UNKNOWN;
    }


    static public String getRequestURL(HttpServletRequest request, String api,
                                       String docId, String action) {
        String name = request.getLocalName();
        if (name.startsWith("0:0:0:0:0:0:0:1"))
            name = "localhost"; // HACK: since this is the value returned by
        // java on macosx

        int port = request.getLocalPort();

        String context = request.getContextPath();

        String url = "http://" + name + ":" + port + context + "/" + api + "/"
                + "documents/" + docId + action;

        return url;

    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static String isSelf(String s, UriInfo uInfo, String resource) {
        UriBuilder base = uInfo.getBaseUriBuilder();

        String theUri = base.path(resource).build().toString();

        logger.debug("found theUri " + theUri + " for " + s);

        if (s.startsWith(theUri)) {
            return s.substring(theUri.length());
        }
        return null;
    }

    public Response composeResponseNotFOUND(String result) {
        return Response.status(Response.Status.NOT_FOUND).entity(result)
                .type("text/plain")
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD).build();
    }


    public Response composeResponseBadRequest(String result, Throwable thrown) {
        return composeResponseError(Response.Status.BAD_REQUEST,result,thrown);

    }
    public Response composeResponseNotFOUND(String result, Throwable thrown) {
        return composeResponseError(Response.Status.NOT_FOUND,result,thrown);
    }



    public Response composeResponseError(Status status, String result, Throwable thrown) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(stringWriter);
        pw.println("<html>");
        pw.println("<h2>Error " + status.getStatusCode() + ": " + status + "</h2>");
        pw.println("<p/>");
        pw.println("<p/>");
        pw.println("<h3> " + result + ": <em>" + thrown.getMessage() + "</em></h3>");
        pw.println("<p/>");
        pw.println("<hr/>");
        pw.println("<pre>");
        if (thrown!=null) {
            printStackTrace(pw,thrown);
        }
        pw.println("</pre>");
        pw.println("</html>");
        InputStream is=new ByteArrayInputStream(stringWriter.toString().getBytes(StandardCharsets.UTF_8));
        return Response.status(status).entity(is).type("text/html")
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD).build();
    }

    public void printStackTrace(PrintWriter pw, Throwable thrown) {
        while (thrown != null) {
            thrown.printStackTrace(pw);
            pw.println("<p/><hr/><p/>");
            thrown = thrown.getCause();
        }
    }



    public Response composeResponseInternalServerError(String result, Throwable thrown) {
        return composeResponseError(Response.Status.INTERNAL_SERVER_ERROR,result,thrown);
    }

    public Response.ResponseBuilder composeResponseSeeOther(String uri) {
        return Response.seeOther(URI.create(uri))
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD);
    }

    public Response.ResponseBuilder composeResponseOK(Object o) {
        return Response.status(Response.Status.OK).entity(o)
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD);
    }

    public Response composeResponseNotAcceptable(List<Variant> vs) {
        return Response.notAcceptable(vs)
                .header(ACCESS_CONTROL_ALLOW_ORIGIN, WILDCARD).build();
    }

    public Response composeResponseNotFoundDocument(String msg) {
        return composeResponseNotFOUND("Not found document for : " + msg);
    }

    public Response composeResponseNotFoundResource(String msg) {
        return composeResponseNotFOUND("No resource found for id: " + msg);
    }

    public Response composeResponseNotFoundConstraintResource(String msg) {
        return composeResponseNotFOUND("Not found constraint resource for : "
                + msg);
    }

    public Response composeResponseNotFoundType(String msg) {
        return composeResponseNotFOUND("Not found validation resource for : "
                + msg);
    }
    public Response composeResponseNotFoundSummarisation(String msg) {
        return composeResponseNotFOUND("Not found summarisation resource for : "
                + msg);
    }



    public DocumentResource doProcessFile(InputStream inputStream,
                                          String mediaType) {
        try {

            String storedResourceIdentifier = "";

            Formats.ProvFormat format = interop.mimeTypeRevMap.get(mediaType.toString());

            storedResourceIdentifier=storageManager.newStore(format);

            logger.debug("storage Id: " + storedResourceIdentifier);


            ResourceIndex<DocumentResource> index=documentResourceIndex.getIndex();

            try {
                DocumentResource dr = index.newResource();

                dr.setStorageId(storedResourceIdentifier);

                index.put(dr.getVisibleId(), dr);

                logger.debug("visible Id: " + dr.getVisibleId());

                storageManager.copyInputStreamToStore(inputStream, format, storedResourceIdentifier);

                logger.debug("----------- Done");

                doProcessFile(dr, true);

                return dr;
            } finally {
                index.close();
            }
        } catch (Throwable e) {

            e.printStackTrace();
            throw new ParserException(e);
        }

    }

    public LRUHashMap<String,Document> documentCache;

    public boolean doProcessFile(DocumentResource dr, boolean known) {
        try {

            Document doc;

            logger.debug("doProcessFile for " + dr.getVisibleId() + " " + dr.getStorageId());

            doc=getDocumentFromCacheOrStore(dr.getStorageId());


            if (doc == null)
                throw new NullPointerException("read document returned null for " + dr.getStorageId());


            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            dr.setThrown(e);
            return false;
        }

    }

    // TODO: concurrency???
    // TODO: job scheduler should remove entry from cache before deleting files
    public Document getDocumentFromCacheOrStore(String storageId) throws IOException {
        Document doc;
        synchronized (this) {
            doc = documentCache.get(storageId);
        }
        if (doc!=null) {
            logger.debug("Retrieved document from cache: " + storageId);
            return doc;
        }
        logger.debug("Retrieving document from store: " + storageId);
        doc=storageManager.readDocument(storageId,true);
        if (doc!=null) {
            synchronized (this) {
                documentCache.put(storageId, doc);
            }
            logger.debug("Stored document in cache: " + storageId);
        }
        return doc;
    }


    public boolean deleteFromCache(String storageId) {
        Object o;
        synchronized (this) {
            o= documentCache.remove(this) ;
        }
        return o!=null;
    }

    public DocumentResource doProcessStatementsForm(List<InputPart> inputParts,
                                                    List<InputPart> type) {
        String storedResourceIdentifier = "";
        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                logger.debug("Header " + header);
                logger.debug("Header " + header.values());
                logger.debug("Header " + header.keySet());


                String mybody = inputPart.getBodyAsString();

                String mytype = type.get(0).getBodyAsString();

                Formats.ProvFormat format = interop.getTypeForFile("." + mytype);

                storedResourceIdentifier=storageManager.newStore(format);
                logger.debug("storage Id: " + storedResourceIdentifier);

                logger.debug("processStatementsForm: type is " + mytype);

                ResourceIndex<DocumentResource> index=documentResourceIndex.getIndex();

                try {
                    DocumentResource dr = index.newResource();

                    dr.setStorageId(storedResourceIdentifier);

                    index.put(dr.getVisibleId(), dr);

                    logger.debug("visible Id: " + dr.getVisibleId());

                    storageManager.copyStringToStore(mybody,format, storedResourceIdentifier);
                    //FileUtils.write(temp, mybody, StandardCharsets.UTF_8);
                    // FileUtils.copyInputStreamToFile(inputStream,temp); DOESN'T
                    // WORK??

                    return dr;
                } finally {
                    index.close();
                }

            } catch (Throwable e) {
                e.printStackTrace();
                throw new ParserException(e);
            }

        }
        throw new RuntimeException("Not properly structured input parts");
    }


    public DocumentResource doProcessFileForm(List<InputPart> inputParts) {
        String fileName;
        String storedResourceIdentifier = "";
        DocumentResource dr;

        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                logger.debug("Header " + header);
                logger.debug("Header " + header.values());
                logger.debug("Header " + header.keySet());

                fileName = getFileName(header);
                //.out.println("---------- filename " + fileName);
                if ((fileName == null) || (fileName.equals("")))
                    return null;

                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                // constructs upload file path
                //fileName = UPLOADED_FILE_PATH + fileName;

                Formats.ProvFormat format = interop.getTypeForFile(fileName);

                storedResourceIdentifier=storageManager.newStore(format);


                ResourceIndex<DocumentResource> index=documentResourceIndex.getIndex();

                try {
                    dr = index.newResource();

                    dr.setStorageId(storedResourceIdentifier);

                    index.put(dr.getVisibleId(), dr);
                } finally {
                    index.close();
                }


                logger.debug("storage Id: " + storedResourceIdentifier);
                logger.debug("visible Id: " + dr.getVisibleId());

                storageManager.copyInputStreamToStore(inputStream,format,storedResourceIdentifier);
                //FileUtils.copyInputStreamToFile(inputStream, temp);

                //System.out.println("----------- Done");
                String formatString=(format != null) ? format.toString() : "unknown";




                return dr;

            } catch (Throwable e) {
                e.printStackTrace();
                throw new ParserException(e);
            }

        }
        throw new RuntimeException("Not properly structured input parts");
    }



    public DocumentResource doProcessURLForm(List<InputPart> inputParts) {
        String storedResourceIdentifier = "";
        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                logger.debug("Header " + header);
                logger.debug("Header " + header.values());
                logger.debug("Header " + header.keySet());

                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);

                String url = IOUtils.toString(inputStream, Charset.defaultCharset()).trim();

                //System.out.println("---------- URL " + url);
                inputStream.close();

                if ((url == null) || (url.equals(""))) {
                    //OK, it's not a URL we have received, let's return null, and try other option
                    return null;
                }

                URL theURL = new URL(url);
                URLConnection conn = interop.connectWithRedirect(theURL);
                if (conn == null) throw new RuntimeException("Failed to connect to url");

                Formats.ProvFormat format = null;
                String content_type = conn.getContentType();

                logger.debug("Content-type: " + content_type);
                if (content_type != null) {
                    // Need to trim optional parameters
                    // Content-Type: text/plain; charset=UTF-8
                    int end = content_type.indexOf(";");
                    if (end < 0) {
                        end = content_type.length();
                    }
                    String actual_content_type = content_type.substring(0, end).trim();
                    logger.debug("Found Content-type: " + actual_content_type);
                    // TODO: might be worth skipping if text/plain as that seems
                    // to be the default returned by unconfigured web servers
                    format = interop.mimeTypeRevMap.get(actual_content_type);
                }
                logger.debug("Format after Content-type: " + format);

                if (format == null) {
                    format = interop.getTypeForFile(theURL.toString());
                }
                logger.debug("Format after extension: " + format);


                storedResourceIdentifier=storageManager.newStore(format);

                InputStream content_stream = conn.getInputStream();

                storageManager.copyInputStreamToStore(content_stream,format, storedResourceIdentifier);
               // FileUtils.copyInputStreamToFile(content_stream, temp);


                ResourceIndex<DocumentResource> index=documentResourceIndex.getIndex();
                try {
                    DocumentResource dr = index.newResource();
                    dr.setStorageId(storedResourceIdentifier);
                    index.put(dr.getVisibleId(), dr);


                    logger.debug("storage Id: " + storedResourceIdentifier);
                    logger.debug("visible Id: " + dr.getVisibleId());


                    return dr;
                } finally {
                    index.close();
                }

            } catch (java.net.UnknownHostException e) {
                throw new UncheckedException("UnknownHostException", e);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new ParserException(e);
            }

        }
        throw new RuntimeException("Not properly structured input parts");
    }


    /**
     * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
     * name="file"; filename="filename.extension"] }
     **/
    // get uploaded filename, is there a easy way in RESTEasy?
    public String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition")
                .split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        // return null, exception thrown elsewhere
        return null;
    }

    public Response contentNegotiationForDocument(Request request,
                                                  String msg,
                                                  String uriPath) throws IllegalArgumentException {
        return contentNegotiationForDocument(request, "documents/" + msg + uriPath);
    }
    public Response contentNegotiationForDocument(Request request,
                                                  String path) throws IllegalArgumentException {
        // http://docs.oracle.com/javaee/6/tutorial/doc/gkqbq.html

        // Use the Interop Framework to find the list of supported
        // mimetypes and feed those into the Variant list
        List<Variant> vs = interop.getVariants();
        MediaType m = MediaType.TEXT_HTML_TYPE;
        //vs.add(new Variant(m, (java.util.Locale) null, (String) null));
        Variant v = request.selectVariant(vs);

        if (v == null) {
            return composeResponseNotAcceptable(vs);
        } else {
            String type;
            if (v.getMediaType().equals(MediaType.TEXT_HTML_TYPE)) {
                type = "html";
            } else {
                String mt = v.getMediaType().toString();
                type = interop.getExtension((Formats.ProvFormat) interop.mimeTypeRevMap.get(mt));
            }
            if (type != null) {
                return composeResponseSeeOther(path + type).build();
            } else {
                return composeResponseNotFoundResource("not found type for " + path);
            }
        }
    }




}
