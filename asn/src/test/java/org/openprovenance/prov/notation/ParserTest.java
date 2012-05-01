package org.openprovenance.prov.notation;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParserTest extends TestCase
{
    public void testContainer0() {
        testContainer("src/test/resources/prov/container0.prov");
    }

    public void testContainer1() {
        testContainer("src/test/resources/prov/container1.prov");
    }

    public void testContainer2() {
        testContainer("src/test/resources/prov/container2.prov");
    }

    public void testContainer(String file) {
        try {
            new Utility().getParserForFile(file).bundle();
        } catch (Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
       
            assertTrue(false);
        }
    }
}


