package org.openprovenance.prov.template.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

public class MyTest extends TestCase {

    /*public void testMethod1() throws IOException {
        ObjectMapper om=new ObjectMapper();
        TestBean tb=new TestBean();
        tb.var=new Hashtable<>();
        Descriptors ds=new Descriptors();
        ds.values=new LinkedList<>();
        SingleDescriptor sd = new SingleDescriptor();
        sd.id="ex:luc";
        ds.values.add(sd);
        tb.var.put("author", ds);
        om.writeValue(System.out,tb);
        assertTrue(true);

    }*/

    public void testMethod2() throws IOException {
        ObjectMapper om=new ObjectMapper();


        TestBean tb101=om.readValue(new File("src/test/resources/bindings/bindings101.json"), TestBean.class);
        System.out.println("tb101 = " + tb101);
        assertNotNull("tb101 is null", tb101);

        TestBean tb20=om.readValue(new File("src/test/resources/bindings/bindings20.json"), TestBean.class);
        System.out.println("tb20 = " + tb20);
        assertNotNull("tb20 is null", tb20);
    }
}
