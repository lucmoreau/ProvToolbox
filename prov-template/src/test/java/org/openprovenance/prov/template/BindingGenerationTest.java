package org.openprovenance.prov.template;

import java.io.IOException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.BindingsBeanGenerator;

import junit.framework.TestCase;

public class BindingGenerationTest extends TestCase {
    ProvFactory pf=org.openprovenance.prov.xml.ProvFactory.getFactory();

    public BindingGenerationTest(String testName) {
        super(testName);
    }
    
    public void testTemplate(String in, String out) throws IOException, Throwable {        
        Document doc= new Utility().readDocument(in, pf);
        
        
        System.out.println("++++++++++++++++++++++++");
        assertTrue(new BindingsBeanGenerator(pf).generate(doc,"test","org.example", "target", "my/template")); 


        System.out.println("++++++++++++++++++++++++");
    }
    
    public void testTemplate() throws IOException, Throwable {
        testTemplate("src/main/resources/template-c.provn",
                     null);
    }
    
}