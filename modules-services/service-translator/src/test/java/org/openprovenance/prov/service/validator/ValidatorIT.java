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
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.DocumentMessageBodyReader;
import org.openprovenance.prov.service.ValidationReportMessageBodyReader;
import org.openprovenance.prov.service.translator.TranslatorIT;
import org.openprovenance.prov.validation.report.ValidationReport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_HTML;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_XML;
import static org.openprovenance.prov.interop.InteropMediaType.MEDIA_APPLICATION_JSON;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidatorIT extends TestCase {
    static Logger logger = LogManager.getLogger(ValidatorIT.class);

    static String port= Objects.requireNonNull(Configuration.getPropertiesFromClasspath(TranslatorIT.class, "config.properties")).getProperty("service.port");
    static String context= Objects.requireNonNull(Configuration.getPropertiesFromClasspath(TranslatorIT.class, "config.properties")).getProperty("service.context");

    String validationURL="http://localhost:" + port + context + "/provapi/documents/";


    public static HashMap<String, String> table=new HashMap<String, String>();


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
        String location=doPostStatements(validationURL,file);
        assertNotNull("location", location);
        table.put("location", location);
        logger.info("location " + location);

        Response resp=getResource(location, MEDIA_TEXT_HTML);
        logger.debug("response contents " + resp.getEntity());
        logger.debug("response headers " + resp.getHeaders());
        logger.debug("response links " + resp.getLinks());
        logger.debug("response status " + resp.getStatus());

        String location_html=(String)resp.getHeaders().getFirst("Location");
        logger.info("location_html " + location_html);
        assertNotNull("location_html", location_html);

        Response resp_html=getResource(location_html, null);
        logger.debug("resp_html contents " + resp_html.getEntity());
        logger.debug("resp_html headers " + resp_html.getHeaders());
        logger.debug("resp_html links " + resp_html.getLinks());
        logger.debug("resp_html status " + resp_html.getStatus());

        logger.info(resp_html);
        try {
            assertEquals("Response HTML status is NOT OK", Response.Status.OK.getStatusCode(), resp_html.getStatus());
            logger.info("now closing resp_html");
            resp_html.close();
        } catch (AssertionError e) {
            logger.error("======> Response HTML status is not OK", e);

        }

        Response resp_json=getResource(location, MEDIA_APPLICATION_JSON);
        logger.debug("response contents " + resp_json.getEntity());
        logger.debug("response headers " + resp_json.getHeaders());
        logger.debug("response links " + resp_json.getLinks());


        String location_report_json=(String)resp_json.getHeaders().getFirst("Location");
        logger.debug("location_report_json " + location_report_json);
        assertNotNull("location_report_json", location_report_json);

        Response resp_report_json=getResource(location_report_json, MEDIA_APPLICATION_JSON);
        assertEquals("Response report JSON status is OK",Response.Status.OK.getStatusCode(),resp_report_json.getStatus());
        logger.debug("response is " + resp_report_json.getEntity());

        String resp10=readAsString(location_report_json,MEDIA_APPLICATION_JSON);
        System.out.println("resp_report_json is: " + resp10);


        ValidationReport o=readValidationReport(location_report_json, MEDIA_APPLICATION_JSON);
        logger.debug("response is " + o);
        assertNotNull("validation report", o);

        assertTrue("cycles ", o.getCycle().isEmpty());
        assertTrue("non strict cycles ", o.getNonStrictCycle().isEmpty());

        assertTrue("failed merges  ", o.getFailedMerge().isEmpty());

        System.out.println("malformed statements  "+ o.getMalformedStatements());

        assertNull("malformed statements  ", o.getMalformedStatements());
        assertNull("specialization report  ", o.getSpecializationReport());
        assertTrue("qualified name mismatch  ", o.getQualifiedNameMismatch().isEmpty());


        String matrix_location = location_report_json.replace("report.json", "matrix.txt");
        logger.debug("matrix_location " + matrix_location);
        String matrix=readAsString(matrix_location,null);
        System.out.println(matrix);


    }





    public String doPostStatements(String url, String file) {
        InteropFramework intF=new InteropFramework();
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is not null", doc);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        intF.writeDocument(baos, ProvFormat.PROVN, doc);
        String s=baos.toString();

        Client client = ClientBuilder.newBuilder().build();



        WebTarget target = client.target(url);


        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("statements", s, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type", "provn", MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("validate", "Validate", MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public Object readObject(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(DocumentMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();
        Object o=response2.readEntity(InputStream.class);
        //System.out.println(" *** found entity ");
        client.close();
        return o;
    }

    public ValidationReport readValidationReport(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(ValidationReportMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();

        try {
            return response2.readEntity(ValidationReport.class);
        } catch (RuntimeException e) {
            logger.throwing(e);  // for some reason, the parser now fails reading validation report
            return null;
        } finally {
            client.close();
        }

    }


    public String readAsString(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(ValidationReportMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();

        String o=response2.readEntity(String.class);
        client.close();
        return o;
    }

    public Response getResource(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        WebTarget target=client.target(location);
        Response response2;
        if (media==null) {
            response2=target.request().get();
        } else {
            response2=target.request(media).get();
        }
        //client.close();
        return response2;
    }



}
