package org.openprovenance.prov.service.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.template.library.plead.integrator.InputOutputProcessor;
import org.openprovenance.prov.templates.library.LocalEnactor;
import org.openprovenance.prov.templates.library.PleadWorkflow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

 public class PleadWorkflowIT extends TestCase {
    private final InputOutputProcessor templateInvoker;
    private final ObjectMapper om = new ObjectMapper();

    public PleadWorkflowIT(InputOutputProcessor templateInvoker) {
        this.templateInvoker = templateInvoker;
    }

    public PleadWorkflowIT() {
        Map<String,AtomicInteger> map=new HashMap<>() {{
                put("activity", new AtomicInteger(1000));
                put("file", new AtomicInteger(1000));
                put("score", new AtomicInteger(10000));
                put("approval", new AtomicInteger(100000));
            }};

        this.templateInvoker = new LocalEnactor() {
            @Override
            public Integer incrementCounter(String counter) {
                return map.get(counter).getAndIncrement();
            }
        };
    }
    final static ClientConfig config=new ClientConfig(PleadWorkflowIT.class);

    public void testPleadLocal() {
        List<Object> inputs=new LinkedList<>();
        List<Object> outputs=new LinkedList<>();
        new PleadWorkflow(templateInvoker,null,null).workflow("doc123", 1, 220, 222, 1, 50, "path",null,null);
        new PleadWorkflow(templateInvoker,inputs,outputs).workflow("doc123", 1, 220, 222, 1, 50, "path",null,null);
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
            new PleadWorkflow(new WebTemplateInvoker(statementsURL, authorizer), inputs, outputs).workflow("doc123", 1, 220, 222, 1, 50, "path", null, null);
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
