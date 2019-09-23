package org.openprovenance.prov.service.core;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.CommandLineArguments;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;

public class ServiceUtils {

    public static final String DOCUMENT_NOT_FOUND = "Document not found";
    public static final String WILDCARD = "*";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String HEADER_PARAM_ACCEPT = "Accept";

    static Logger logger = Logger.getLogger(ServiceUtils.class);

  //  final public ProvFactory f;

   // final protected Namespace ns;

    public ServiceUtils() {
   //     this.f = f;
  //      this.ns = ns;
    }

    private static String fileName = "config.properties";

    public final String UPLOADED_FILE_PATH = getPropertiesFromClasspath(fileName).getProperty("upload.directory");

    public static final String containerVersion = getPropertiesFromClasspath(fileName).getProperty("container.version");
    public static final String containerClassifier = getPropertiesFromClasspath(fileName).getProperty("container.classifier");

    public static final String longContainerVersion = "ProvToolbox/modules-services ... " + containerVersion + (((containerClassifier==null) || (containerClassifier=="")) ? "" : "-" + containerClassifier) + " (" + getPropertiesFromClasspath(fileName).getProperty("timestamp") + ")";

    private static Properties getPropertiesFromClasspath(String propFileName) {
        return CommandLineArguments.getPropertiesFromClasspath(ServiceUtils.class, propFileName);
    }

    public Destination getDestination(Map<String, List<InputPart>> formData) throws IOException {
        if (formData.get("translate") != null) {
            String val = getFormDataValue(formData, "translate");
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
                if ("jpg".equals(val))
                    return Destination.JPG;
            }
        }
        if (formData.get("expand") != null) {
            String val = getFormDataValue(formData, "expand");
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

    public enum Action {
        UNKNOWN("UNKNOWN"), VALIDATE("VALIDATE"), EXPAND("EXPAND"), NF("NF"), SIGN("SIGN"), SIGNATURE("SIGNATURE)"), CHECK("CHECK"), RANDOM("RANDOM"), LINEAR("LINEAR"), TRANSLATE("TRANSLATE"), SUMMARISE("SUMMARISE"), UPLOAD("UPLOAD");
        // JSON("json"), XML("provx"), PROVN("provn"), TURTLE("ttl"), TRIG("trig"), SVG("svg"), JPG("jpg") ;

        private Action(String text) {
            this.text = text;
        }

        private final String text;

        public String toString() {
            return text;
        }
    };

    public enum Destination {
        UNKNOWN("UNKNOWN"), JSON("json"), XML("provx"), PROVN("provn"), TURTLE("ttl"), TRIG("trig"), SVG("svg"), JPG("jpg") ;
        private Destination(String text) {
            this.text = text;
        }

        private final String text;

        public String toString() {
            return text;
        }
    };

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
            /*
            String val = getFormDataValue(formData, "translate");
            if (val != null) {
                if ("json".equals(val))
                    return Action.JSON;
                if ("provx".equals(val))
                    return Action.XML;
                if ("provn".equals(val))
                    return Action.PROVN;
                if ("turtle".equals(val))
                    return Action.TURTLE;
                if ("trig".equals(val))
                    return Action.TRIG;
                if ("svg".equals(val))
                    return Action.SVG;
                if ("jpg".equals(val))
                    return Action.JPG;
                return Action.UNKNOWN;
            } */
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
        if (formData.get("summarise") != null) {
            return Action.SUMMARISE;
        }

        if (formData.get("expand") != null) {
            return Action.EXPAND;
            /*
            String val = getFormDataValue(formData, "expand");
            if (val != null) {
                if ("json".equals(val))
                    return Action.JSON;
                if ("provx".equals(val))
                    return Action.XML;
                if ("provn".equals(val))
                    return Action.PROVN;
                if ("turtle".equals(val))
                    return Action.TURTLE;
                if ("trig".equals(val))
                    return Action.TRIG;
                if ("svg".equals(val))
                    return Action.SVG;
                return Action.UNKNOWN;
            }

             */
        }
        // legacy
        /*
         * if (formData.get("validate") != null) return Action.VALIDATE; if
         * (formData.get("json") != null) return Action.JSON; if
         * (formData.get("xml") != null) return Action.XML; if
         * (formData.get("provn") != null) return Action.PROVN; if
         * (formData.get("turtle") != null) return Action.TURTLE; if
         * (formData.get("trig") != null) return Action.TRIG; if
         * (formData.get("svg") != null) return Action.SVG;
         */
        return Action.UNKNOWN;
    }


    static public String getRequestURL(HttpServletRequest request, String api,
                                       String docId, String action) {
        // <%
        // out.print( request.getRemoteAddr() );
        // out. print( request.getRemoteHost() );
        // %>

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
        return composeResponseNotFOUND("Not found resource for : " + msg);
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

            String absoluteFilePath = "";

            InteropFramework interop = new InteropFramework();

            Formats.ProvFormat format = interop.mimeTypeRevMap
                    .get(mediaType.toString());
            String extension = interop.getExtension(format);

            File temp = createTempFile(extension);
            absoluteFilePath = temp.getAbsolutePath();

            String s = temp.getName();
            String graphId = s.substring(0, s.lastIndexOf("."));

            System.out.println("---------- Destination name " + graphId);
            System.out.println("---------- Temp file name " + absoluteFilePath);
            // System.out.println("---------- mime type " + mimeType);

            // new InputStreamReader(inputStream,"UTF-8")
            FileUtils.copyInputStreamToFile(inputStream, temp);

            System.out.println("----------- Done");


            DocumentResource vr = new DocumentResource();
            vr.format = format;
            vr.graphId = graphId;
            vr.filepath = absoluteFilePath;
            vr.graphpath = absoluteFilePath.substring(0, absoluteFilePath
                    .lastIndexOf("."));
            vr.mimeType = null;
            DocumentResource.table.put(graphId, vr);

            doProcessFile(vr, true);

            return vr;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean doProcessFile(DocumentResource vr, boolean known) {
        try {
            InteropFramework interop = new InteropFramework();

            Document doc;
            if (known) {
                doc = (Document) interop.readDocumentFromFile(vr.filepath);
            } else {
                doc = (Document) interop.loadProvUnknownGraph(vr.filepath);
            }

            if (doc == null)
                throw new NullPointerException("read document returned null for " + vr.filepath);

            vr.document = (org.openprovenance.prov.xml.Document) doc;

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            vr.thrown = e;
            return false;
        }

    }


    public DocumentResource doProcessStatementsForm(List<InputPart> inputParts,
                                                    List<InputPart> type) {
        String absoluteFilePath = "";
        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                System.out.println("Header " + header);
                System.out.println("Header " + header.values());
                System.out.println("Header " + header.keySet());

                // convert the uploaded file to inputstream
                // InputStream inputStream =
                // inputPart.getBody(InputStream.class,null);
                // DOESN'T WORK????

                String mybody = inputPart.getBodyAsString();
                // System.out.println("Body is " +mybody);

                String mytype = type.get(0).getBodyAsString();

                InteropFramework interop = new InteropFramework();
                Formats.ProvFormat format = interop.getTypeForFile("." + mytype);
                String extension = interop.getExtension(format);

                logger.debug("processStatementsForm: type is " + mytype);
                logger.debug("processStatementsForm: extension is " + extension);


                File temp = createTempFile(extension);

                absoluteFilePath = temp.getAbsolutePath();

                String s = temp.getName();
                String graphId = s.substring(0, s.lastIndexOf("."));

                System.out.println("---------- Destination name " + graphId);
                System.out.println("---------- Temp file name "
                        + absoluteFilePath);
                // System.out.println("---------- mime type " + mimeType);

                FileUtils.write(temp, mybody);
                // FileUtils.copyInputStreamToFile(inputStream,temp); DOESN'T
                // WORK??

                System.out.println("----------- Done");


                DocumentResource vr = new DocumentResource();
                vr.format = format;

                vr.graphId = graphId;
                vr.filepath = absoluteFilePath;
                vr.graphpath = absoluteFilePath.substring(0, absoluteFilePath
                        .lastIndexOf("."));
                // vr.mimeType=mimeType;
                DocumentResource.table.put(graphId, vr);

                // doProcessFile(vr, true, om);

                return vr;

            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }


    public DocumentResource doProcessFileForm(List<InputPart> inputParts) {
        String fileName = "";
        String absoluteFilePath = "";
        DocumentResource vr;

        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                System.out.println("Header " + header);
                System.out.println("Header " + header.values());
                System.out.println("Header " + header.keySet());

                fileName = getFileName(header);
                System.out.println("---------- filename " + fileName);
                if ((fileName == null) || (fileName.equals("")))
                    return null;


                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,
                        null);

                // constructs upload file path
                fileName = UPLOADED_FILE_PATH + fileName;

                InteropFramework interop = new InteropFramework();
                Formats.ProvFormat format = interop.getTypeForFile(fileName);
                String extension = interop.getExtension(format);

                File temp = createTempFile(extension);
                absoluteFilePath = temp.getAbsolutePath();

                String s = temp.getName();
                String graphId = s.substring(0, s.lastIndexOf("."));

                System.out.println("---------- Destination name " + graphId);
                System.out.println("---------- Temp file name "
                        + absoluteFilePath);
                // System.out.println("---------- mime type " + mimeType);

                FileUtils.copyInputStreamToFile(inputStream, temp);

                System.out.println("----------- Done");
                String formatString=(format != null) ? format.toString() : "unknown";

                vr = new DocumentResource();

                vr.format = format;
                vr.graphId = graphId;
                vr.filepath = absoluteFilePath;
                vr.graphpath = absoluteFilePath.substring(0, absoluteFilePath
                        .lastIndexOf("."));
                vr.mimeType = null;
                DocumentResource.table.put(graphId, vr);

                // doProcessFile(vr, true, om);

                return vr;

            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }



    public DocumentResource doProcessURLForm(List<InputPart> inputParts) {
        String absoluteFilePath = "";
        for (InputPart inputPart : inputParts) {

            try {

                MultivaluedMap<String, String> header = inputPart.getHeaders();
                System.out.println("Header " + header);
                System.out.println("Header " + header.values());
                System.out.println("Header " + header.keySet());

                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,
                        null);

                String url = IOUtils.toString(inputStream).trim();

                System.out.println("---------- URL " + url);
                inputStream.close();

                if ((url == null) || (url.equals("")))
                    return null;

                InteropFramework interop = new InteropFramework();
                URL theURL = new URL(url);
                URLConnection conn = interop.connectWithRedirect(theURL);
                if (conn == null)
                    return null;

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
                    String actual_content_type = content_type.substring(0, end)
                            .trim();
                    logger.debug("Found Content-type: " + actual_content_type);
                    // TODO: might be worth skipping if text/plain as that seems
                    // to be the
                    // default returned by unconfigured web servers
                    format = interop.mimeTypeRevMap.get(actual_content_type);
                }
                logger.debug("Format after Content-type: " + format);

                if (format == null) {
                    format = interop.getTypeForFile(theURL.toString());
                }
                logger.debug("Format after extension: " + format);

                String formatString=(format != null) ? format.toString() : "unknown";

                String extension = interop.getExtension(format);

                File temp = createTempFile(extension);

                InputStream content_stream = conn.getInputStream();
                FileUtils.copyInputStreamToFile(content_stream, temp);

                absoluteFilePath = temp.getAbsolutePath();

                String s = temp.getName();
                String graphId = s.substring(0, s.lastIndexOf("."));

                System.out.println("---------- Destination name " + graphId);
                System.out.println("---------- Temp file name "
                        + absoluteFilePath);

                System.out.println("----------- Done");

                DocumentResource vr = new DocumentResource();

                vr.format = format;
                vr.graphId = graphId;
                vr.filepath = absoluteFilePath;
                vr.graphpath = absoluteFilePath.substring(0, absoluteFilePath
                        .lastIndexOf("."));
                vr.mimeType = null;
                DocumentResource.table.put(graphId, vr);
                vr.url = url;

                // doProcessFile(vr, true, om);

                return vr;

            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }

        }
        return null;
    }



    public File createTempFile(String extension) throws IOException {
        return File.createTempFile("graph", "." + extension, new File(
                UPLOADED_FILE_PATH));
    }

    public File createConfigurationFile() throws IOException {
        return File.createTempFile("config", "." + "json", new File(
                UPLOADED_FILE_PATH));
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
        InteropFramework intF = new InteropFramework();
        List<Variant> vs = intF.getVariants();
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
                type = intF.getExtension((Formats.ProvFormat) intF.mimeTypeRevMap.get(mt));
            }
            if (type != null) {
                return composeResponseSeeOther(path + type).build();
            } else {
                return composeResponseNotFoundResource("not found type for " + path);
            }
        }
    }




}
