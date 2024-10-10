package org.openprovenance.prov.service.translator;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.ValidationReportMessageBodyReader;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.service.core.readers.VanillaDocumentMessageBodyReader;
import org.openprovenance.prov.validation.report.ValidationReport;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.openprovenance.prov.model.interop.InteropMediaType.*;
import static org.openprovenance.prov.service.core.Constants.*;


public class ProvenanceIT extends TestCase implements ApiUriFragments {
    static Logger logger = LogManager.getLogger(ProvenanceIT.class);
    final static ClientConfig config=new ClientConfig(ProvenanceIT.class);

    final ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
    final InteropFramework intF=new InteropFramework(pf);

    public static HashMap<String, String> table= new HashMap<>();

    List<String> urls=List.of(
            "http://localhost:7073/metrics/webjars/prov-template-library/2.2.1-SNAPSHOT/templates/org/openprovenance/prov/templates/plead/plead-validating.provn",
            "http://localhost:7073/metrics/webjars/prov-template-library/2.2.1-SNAPSHOT/templates/org/openprovenance/prov/templates/plead/plead-validating.png",
            "http://localhost:7073/metrics/webjars/prov-template-library/2.2.1-SNAPSHOT/templates/org/openprovenance/prov/templates/plead/prov-plead-validating.prov-csv");



    public void testProvenanceOfTemplate() {
        testProvenanceOfTemplate(urls.get(0));
        // wait for 5 minutes

    }

    public void testProvenanceOfTemplate(String url) {
        Response response = getResource(url, MEDIA_TEXT_PROVENANCE_NOTATION);
        System.out.println(response.getHeaders());
        Set<Link> links = response.getLinks();

        // test there is one link in links
        assertEquals(1, links.size());
        Link link = links.iterator().next();

        System.out.println(escapeGreen(link.getParams().toString()));

        List<String> rels = link.getRels();
        assertEquals(1, rels.size());
        assertEquals(HAS_PROVENANCE, rels.get(0));


        URI uri1 = link.getUri();
        String provUri=config.hostURLprefix+uri1.getPath();

        Response response2 = getResource(provUri, MEDIA_TEXT_PROVENANCE_NOTATION);
        Set<Link> links2 = response2.getLinks();
        String o=response2.readEntity(String.class);
        assertEquals(1, links2.size());
        Link link2 = links2.iterator().next();
        System.out.println(escapeGreen(link2.getParams().toString()));
        List<String> rels2 = link2.getRels();
        assertEquals(HAS_PROVENANCE, rels2.get(0));
        URI uri2 = link2.getUri();

        assertEquals(uri1, uri2);

        System.out.println(response2.getHeaders());
        System.out.println(escapeGreen(o));

    }











    @SuppressWarnings("unused")
    public Object readObject(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(VanillaDocumentMessageBodyReader.class);
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
