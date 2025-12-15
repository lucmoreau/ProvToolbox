#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.bookptm.LocalEnactor;
{groupId}.TemplatesToDot;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.bk.physical.client.integrator.InputOutputProcessor;

import ${groupId}.BoxWorkflow;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.openprovenance.bk.physical.Init.pf;

public class BoxWorkflowIT extends TestCase {
    private final InputOutputProcessor templateInvoker;
    private final LocalEnactor localEnactor;
    private final ObjectMapper om = new ObjectMapper();

    public BoxWorkflowIT(InputOutputProcessor templateInvoker) {
        this.templateInvoker = templateInvoker;
        if (templateInvoker instanceof LocalEnactor) {
            this.localEnactor = (LocalEnactor) templateInvoker;
        } else {
            this.localEnactor = null;
        }
    }

    public BoxWorkflowIT() {
        Map<String,AtomicInteger> map=new HashMap<>() {{
                put("activity", new AtomicInteger(1000));
                put("file", new AtomicInteger(1000));
                put("score", new AtomicInteger(10000));
                put("approval", new AtomicInteger(100000));
            }};

        this.localEnactor = new LocalEnactor();
        this.templateInvoker = null;
    }

    final static ClientConfig config=new ClientConfig(BoxWorkflowIT.class);

    public void testPleadLocal() throws FileNotFoundException {
        if (localEnactor==null) return;
        List<Object> inputs=new LinkedList<>();
        List<Object> outputs=new LinkedList<>();
        BoxWorkflow workflow = new BoxWorkflow(localEnactor, null);
        List<Object> results= new ArrayList<>(workflow.run());

        new TemplatesToDot(workflow.connections, localEnactor.getId2array(), "template", pf, null).convert(null, new FileOutputStream("target/viz.svg"), "template_connections");
        new TemplatesToDot(workflow.connectionsNoAgent, localEnactor.getId2array(), "template", pf, null).convert(null, new FileOutputStream("target/viz2.svg"), "template_connections");


        try {
            System.out.println(om.writeValueAsString(outputs));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public void testPleadRemote() {
        pleadRemote(true);
    }

    public void pleadRemote(boolean authorizer) {
        List<Object> inputs=new LinkedList<>();
        List<Object> outputs=new LinkedList<>();


        String statementsURL= config.hostURLprefixContext + "/provapi/statements";

        System.out.println("statementsURL: "+statementsURL);

        try {
            BoxWorkflow workflow=new BoxWorkflow(new WebTemplateInvoker(statementsURL, authorizer), null);
            List<Object> results= new ArrayList<>(workflow.run());
            results.forEach(x -> {
                try {
                    System.out.println(om.writeValueAsString(x));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            assertTrue(e.getMessage().contains("5xxServerError"));
        }


        try {
            System.out.println(om.writeValueAsString(outputs));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
