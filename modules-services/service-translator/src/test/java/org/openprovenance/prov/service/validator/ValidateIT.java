package org.openprovenance.prov.service.validator;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openprovenance.prov.interop.ApiUriFragments;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.DocumentMessageBodyReader;
import org.openprovenance.prov.service.ValidationReportMessageBodyReader;

import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.service.translator.TranslateIT;
import org.openprovenance.prov.validation.report.ValidationReport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;


import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_HTML;
import static org.openprovenance.prov.interop.InteropMediaType.MEDIA_APPLICATION_JSON;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidateIT extends TestCase implements ApiUriFragments {
    static Logger logger = LogManager.getLogger(ValidateIT.class);
    final static ClientConfig config=new ClientConfig(TranslateIT.class);

    final InteropFramework intF=new InteropFramework();

    public static HashMap<String, String> table= new HashMap<>();

    public void testValidatorAction1(){
        doTestAction("src/test/resources/test-validator/doc-tovalidate.provn");
    }


    public void NOtestAction2(){
        doTestAction("src/test/resources/test-validator/picaso-file.provn");
    }

    public void NOtestAction3(){
        doTestAction("src/test/resources/test-validator/picaso-file2.provn");
    }

    public void doTestAction(String file){
        logger.info("ValidatorIT.testAction: " + file);


        logger.debug("*** action 1");
        String location=doPostStatements(config.formURL,file);
        assertNotNull("location", location);
        table.put("location", location);
        logger.info("location " + location);

        String location_report_html = contentNegotiation(location, MEDIA_TEXT_HTML, "location_html");
        String location_report_json = contentNegotiation(location, MEDIA_APPLICATION_JSON, "location_json");

        logger.info("location_report_html " + location_report_html);
        logger.info("location_report_json " + location_report_json);


        logger.info("now dereferencing location_report_html " + location_report_html);

        Response resp_html=getResource2(location_report_html, MEDIA_TEXT_HTML);
        try {
            logger.info("response status " + resp_html.getStatus());
            assertEquals("Response HTML status is NOT OK", Response.Status.OK.getStatusCode(), resp_html.getStatus());
            resp_html.close();
        } catch (AssertionError e) {
            logger.error("####### Response HTML status is not OK #######");
            //logger.throwing(e);
        }

        logger.info("now dereferencing location_report_json " + location_report_json);
        Response resp_report_json=getResource(location_report_json, MEDIA_APPLICATION_JSON);
        logger.info("response status " + resp_report_json.getStatus());
        assertEquals("Response report JSON status is NOT OK",Response.Status.OK.getStatusCode(),resp_report_json.getStatus());

        logger.info("now dereferencing another static html page " + config.htmlURL);
        Response resp_other_html=getResource2(config.htmlURL, MEDIA_TEXT_HTML);
        try {
            logger.info("response status " + resp_other_html.getStatus());
            assertEquals("Response HTML status is NOT OK", Response.Status.OK.getStatusCode(), resp_other_html.getStatus());
            resp_other_html.close();
        } catch (AssertionError e) {
            logger.error("####### Response HTML status is not OK #######");
            //logger.throwing(e);
        }

        String respAsString=readAsString(location_report_json,MEDIA_APPLICATION_JSON);
        logger.info("resp_report_json is: " + respAsString);
        assertNotNull("response as string is null", respAsString);


        logger.info("now parsing location_report_json " + location_report_json);
        ValidationReport o=readValidationReport(location_report_json, MEDIA_APPLICATION_JSON);
        logger.debug("response is " + o);
        assertNotNull("validation report", o);


        logger.info("checking contents of validation reports ");

        assertTrue("cycles ", o.getCycle().isEmpty());
        assertTrue("non strict cycles ", o.getNonStrictCycle().isEmpty());
        assertTrue("failed merges  ", o.getFailedMerge().isEmpty());
        assertNull("malformed statements  ", o.getMalformedStatements());
        assertNull("specialization report  ", o.getSpecializationReport());
        assertTrue("qualified name mismatch  ", o.getQualifiedNameMismatch().isEmpty());


        String matrix_location = location_report_json.replace("report.json", "matrix.txt");
        logger.debug("reading matrix at location " + matrix_location);
        String matrix=readAsString(matrix_location,null);
        System.out.println(matrix);

        // wait for 10 minutes
        /*try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         */


    }

    private String contentNegotiation(String location, String media, String title) {
        Response resp=getResource(location, media);
        logger.debug("response status " + resp.getStatus());
        String location_media=(String)resp.getHeaders().getFirst("Location");
        logger.info(title + " " + location_media);
        assertNotNull(title + " is null", location_media);
        return location_media;
    }


    public String doPostStatements(String url, String file) {
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is null", doc);

        String s = SerializeDocumentToProvn(doc);

        try (Client client = ClientBuilder.newBuilder().build()) {
            WebTarget target = client.target(url);

            MultipartFormDataOutput output = new MultipartFormDataOutput();
            output.addFormData("statements", s, MediaType.TEXT_PLAIN_TYPE);
            output.addFormData("type", "provn", MediaType.TEXT_PLAIN_TYPE);
            output.addFormData("validate", "Validate", MediaType.TEXT_PLAIN_TYPE);

            try (Response response = target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE))) {

                return response.getHeaderString("Location");
            }
        }

    }

    private String SerializeDocumentToProvn(Document doc) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        this.intF.writeDocument(baos, doc, ProvFormat.PROVN);
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



}
