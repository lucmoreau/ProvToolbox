package org.openprovenance.prov.service.translator;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.DocumentMessageBodyReader;
import org.openprovenance.prov.service.ValidationReportMessageBodyReader;

import org.openprovenance.prov.validation.report.ValidationReport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

import static org.openprovenance.prov.interop.InteropMediaType.*;
import static org.openprovenance.prov.model.DateTimeOption.*;
import static org.openprovenance.prov.service.core.Constants.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateIT extends TestCase {
    static Logger logger = LogManager.getLogger(DateIT.class);


    final static Properties properties = Objects.requireNonNull(Configuration.getPropertiesFromClasspath(TranslateIT.class, "config.properties"));
    final static String port= properties.getProperty("service.port");
    final static String context= properties.getProperty("service.context");
    final static String host= properties.getProperty("service.host");
    final static String protocol= properties.getProperty("service.protocol");

    final static String hostURLprefix= protocol + "://" + host + ":" + port + context;
    final static String postURL=hostURLprefix + "/provapi/documents2/";
    final static String expansionURL=hostURLprefix + "/provapi/documents/";
    final static String resourcesURLprefix=hostURLprefix + "/provapi/resources/";
    final static String formURL =hostURLprefix + "/provapi/documents/";
    final static String htmlURL=hostURLprefix + "/contact.html";


    final InteropFramework intF=new InteropFramework();



    public static HashMap<String, String> table= new HashMap<>();


    public void testTranslationWithDate1(){
        doTestAction("src/test/resources/dates/date_with_tz_offset.provn", "provn", 1);
    }
    public void testTranslationWithDate2(){
        doTestAction("src/test/resources/dates/date_with_tz_offset.jsonld", "jsonld", 1);
    }
    public void testTranslationWithDate3(){
        doTestAction("src/test/resources/dates/date_with_tz_offset.json", "json", 1);
    }
    public void testTranslationWithDate4(){
       doTestAction("src/test/resources/dates/date_with_tz_offset.provx", "provx", 1);
    }

    public void testTranslationWithDate1b(){
        doTestAction("src/test/resources/dates/date_with_tz_offset.provn", "provn", 2);
    }

    public void doTestAction(String file, String extension, int postKind) {
        logger.info(escapeGreen("DateIt.testAction: " + file + " "  + postKind));


        logger.debug("*** action 1");
        String location;
        switch (postKind) {
            case 1:
                location = doPostStatementsWithForm(formURL, file, extension);
                break;
            case 2:
                location = doPostStatementsInProvEncoding(postURL, file, extension);
                break;
            default:
                throw new UnsupportedOperationException("postKind " + postKind + " not supported");
        }
        assertNotNull("location", location);
        location=location.replace("."+extension,"");
        table.put("location", location);
        logger.info("location " + location);

        String location_report_json = contentNegotiation(location, MEDIA_APPLICATION_JSON, "location_json");
        String location_report_jsonld = contentNegotiation(location, MEDIA_APPLICATION_JSONLD, "location_jsonld");
        String location_report_provn = contentNegotiation(location, MEDIA_TEXT_PROVENANCE_NOTATION, "location_provn");
        String location_report_provx = contentNegotiation(location, MEDIA_APPLICATION_PROVENANCE_XML, "location_provx");

        logger.info("location_report_json " + location_report_json);
        logger.info("location_report_jsonld " + location_report_jsonld);
        logger.info("location_report_provn " + location_report_provn);
        logger.info("location_report_provx " + location_report_provx);

        logger.info("Reading document with PRESERVE ...");

        Document doc1 = readDocument(location, MEDIA_APPLICATION_JSONLD,         ".jsonld", PRESERVE, null);
        Document doc2 = readDocument(location, MEDIA_TEXT_PROVENANCE_NOTATION,   ".provn", PRESERVE, null);
        Document doc3 = readDocument(location, MEDIA_APPLICATION_PROVENANCE_XML, ".provx", PRESERVE, null);
        Document doc4 = readDocument(location, MEDIA_APPLICATION_JSON,           ".json", PRESERVE, null);

        checkDates_preserve(doc1, "preserve: jsonld");
        checkDates_preserve(doc2, "preserve: provn");
        checkDates_preserve(doc3, "preserve: provx");
        checkDates_preserve(doc4, "preserve: json");

        logger.info("Reading document with UTC ...");

        doc1 = readDocument(location, MEDIA_APPLICATION_JSONLD,         ".jsonld", UTC, null);
        doc2 = readDocument(location, MEDIA_TEXT_PROVENANCE_NOTATION,   ".provn", UTC, null);
        doc3 = readDocument(location, MEDIA_APPLICATION_PROVENANCE_XML, ".provx", UTC, null);
        doc4 = readDocument(location, MEDIA_APPLICATION_JSON,           ".json", UTC, null);

        checkDates_utc(doc1, "UTC: jsonld");
        checkDates_utc(doc2, "UTC: provn");
        checkDates_utc(doc3, "UTC: provx");
        checkDates_utc(doc4, "UTC: json");

        logger.info("Reading document with TIMEZONE Japan ...");

        doc1 = readDocument(location, MEDIA_APPLICATION_JSONLD,         ".jsonld", TIMEZONE, "Japan");
        doc2 = readDocument(location, MEDIA_TEXT_PROVENANCE_NOTATION,   ".provn", TIMEZONE, "Japan");
        doc3 = readDocument(location, MEDIA_APPLICATION_PROVENANCE_XML, ".provx", TIMEZONE, "Japan");
        doc4 = readDocument(location, MEDIA_APPLICATION_JSON,           ".json", TIMEZONE, "Japan");

        checkDates_japan(doc1, "UTC: jsonld");
        checkDates_japan(doc2, "UTC: provn");
        checkDates_japan(doc3, "UTC: provx");
        checkDates_japan(doc4, "UTC: json");

    }

    private void checkDates_preserve(Document document, String title) {
        assertEquals(title + " activity start", ((Activity)(document.getStatementOrBundle().get(0))).getStartTime().toXMLFormat(), "2023-09-08T20:12:45.109-04:00");
        assertEquals(title + " activity end", ((Activity)(document.getStatementOrBundle().get(0))).getEndTime().toXMLFormat(), "2023-10-15T20:35:06.793-02:00");
    }
    private void checkDates_utc(Document document, String title) {
        assertEquals(title + " activity start", ((Activity)(document.getStatementOrBundle().get(0))).getStartTime().toXMLFormat(), "2023-09-09T00:12:45.109Z");
        assertEquals(title + " activity end", ((Activity)(document.getStatementOrBundle().get(0))).getEndTime().toXMLFormat(), "2023-10-15T22:35:06.793Z");
    }

    private void checkDates_japan(Document document, String title) {
        assertEquals(title + " activity start", ((Activity)(document.getStatementOrBundle().get(0))).getStartTime().toXMLFormat(), "2023-09-09T09:12:45.109+09:00");
        assertEquals(title + " activity end", ((Activity)(document.getStatementOrBundle().get(0))).getEndTime().toXMLFormat(), "2023-10-16T07:35:06.793+09:00");
    }

    private String contentNegotiation(String location, String media, String title) {
        Response resp=getResource(location, media);
        logger.debug("response status " + resp.getStatus());
        String location_media=(String)resp.getHeaders().getFirst("Location");
        logger.info(title + " " + location_media);
        assertNotNull(title + " is null", location_media);
        return location_media;
    }
    public Document readDocument(String location, String media, String extension, DateTimeOption dateTimeOption, String timezone) {
        try (Client client=ClientBuilder.newBuilder().build()) {
            client.register(DocumentMessageBodyReader.class);
            WebTarget target = client.target(location + extension);
            Invocation.Builder builder = target.request(media).header(HTTP_HEADER_PROVENANCE_ACCEPT_DATETIME_OPTION, dateTimeOption);
            if (timezone!=null) {
                builder = builder.header(HTTP_HEADER_PROVENANCE_ACCEPT_TIMEZONE, timezone);
            }
            Response response2 = builder.get();
            MultivaluedMap<String, String> headers=response2.getStringHeaders();
            assertNotNull(HTTP_HEADER_PROVENANCE_CONTENT_DATETIME_OPTION + " is null", headers.get(HTTP_HEADER_PROVENANCE_CONTENT_DATETIME_OPTION));
            assertEquals(HTTP_HEADER_PROVENANCE_CONTENT_DATETIME_OPTION + " is different", dateTimeOption.name(), headers.get(HTTP_HEADER_PROVENANCE_CONTENT_DATETIME_OPTION).get(0));
            if (timezone!=null) {
                assertNotNull(HTTP_HEADER_PROVENANCE_CONTENT_TIMEZONE + " is null", headers.get(HTTP_HEADER_PROVENANCE_CONTENT_TIMEZONE));
                assertEquals(HTTP_HEADER_PROVENANCE_CONTENT_TIMEZONE + " is different", timezone, headers.get(HTTP_HEADER_PROVENANCE_CONTENT_TIMEZONE).get(0));
            }
            return response2.readEntity(org.openprovenance.prov.vanilla.Document.class);
        }
    }


    public String doPostStatementsWithForm(String url, String file, String extension) {
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is null", doc);

        String s = SerializeDocumentToProv(doc, ProvFormat.valueOf(extension.toUpperCase()));

        try (Client client = ClientBuilder.newBuilder().build()) {
            WebTarget target = client.target(url);

            MultipartFormDataOutput output = new MultipartFormDataOutput();
            output.addFormData("statements", s, MediaType.TEXT_PLAIN_TYPE);
            output.addFormData("type", extension, MediaType.TEXT_PLAIN_TYPE);
            output.addFormData("translate", extension, MediaType.TEXT_PLAIN_TYPE);

            try (Response response = target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE))) {
                return response.getHeaderString("Location");
            }
        }
    }

    public String doPostStatementsInProvEncoding(String url, String file, String extension) {
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is null", doc);
        try (Client client = ClientBuilder.newBuilder().build()) {
            WebTarget target = client.target(postURL);
            target.register(new org.openprovenance.prov.service.core.DocumentMessageBodyWriter(intF));
            Response response=target.request(MEDIA_TEXT_PROVENANCE_NOTATION).post(Entity.entity(doc, MEDIA_TEXT_PROVENANCE_NOTATION));
            String location=response.getHeaderString("Location");
            location=location.replace(".provn","");
            return location;

        }
    }




    private String SerializeDocumentToProv(Document doc, ProvFormat format) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        this.intF.writeDocument(baos, format, doc);
        return baos.toString();
    }


    @SuppressWarnings("unused")
    public Object readObject(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(DocumentMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();
        Object o=response2.readEntity(InputStream.class);
        client.close();
        return o;
    }

    public ValidationReport readValidationReport(String location, String media) {
        try (Client client = ClientBuilder.newBuilder().build()) {
            client.register(ValidationReportMessageBodyReader.class);
            WebTarget target = client.target(location);
            Response response2 = target.request(media).get();
            return response2.readEntity(ValidationReport.class);
        }

    }

    public String readAsString(String location, String media) {
        try (Client client=ClientBuilder.newBuilder().build()) {
            client.register(ValidationReportMessageBodyReader.class);
            WebTarget target = client.target(location);
            Response response2 = target.request(media).get();
            return response2.readEntity(String.class);
        }
    }

    public Response getResource(String location, String media) {
        try (Client client=ClientBuilder.newBuilder().build()) {
            WebTarget target = client.target(location);
            Response response2;
            if (media == null) {
                response2 = target.request().get();
            } else {
                response2 = target.request(media).get();
            }
            return response2;
        }
    }

    public Response getResource2(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        WebTarget target=client.target(location);
        Response response2;
        if (media==null) {
            response2=target.request().get();
        } else {
            response2=target.request(MediaType.TEXT_HTML_TYPE).get();
        }
        String o=response2.readEntity(String.class);
        client.close();
        return response2;
    }

    static String escapeRed(String str) {
        return "\u001B[31m" + str + "\u001B[0m";
    }
    static String escapeGreen(String str) {
        return "\u001B[32m" + str + "\u001B[0m";
    }

}
