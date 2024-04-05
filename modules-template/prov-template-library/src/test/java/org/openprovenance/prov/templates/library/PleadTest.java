package org.openprovenance.prov.templates.library;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.template.library.plead.client.integrator.*;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.util.List;

public class PleadTest extends TestCase {
    private final InputOutputProcessor templateInvoker;
    private final ObjectMapper om = new ObjectMapper();

    public PleadTest(InputOutputProcessor templateInvoker) {
        this.templateInvoker = templateInvoker;
    }

    public PleadTest() {
        this.templateInvoker = new LocalEnactor();
    }

    public void testPlead1() {
        List<Object> outputs=new PleadWorkflow(templateInvoker).workflow("doc123", 1, 220, 222, 1, 50, "path",null,null);
        try {
            System.out.println(om.writeValueAsString(outputs));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
