package org.openprovenance.prov.notation;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParserTest extends TestCase
{
    public void testBundle0() {
        testBundle("src/test/resources/prov/container0.prov");
    }

    public void testBundle1() {
        testBundle("src/test/resources/prov/container1.prov");
    }

    public void testBundle2() {
        testBundle("src/test/resources/prov/container2.prov");
    }

    public void testBundle(String file) {
        try {
            new Utility().getParserForFile(file).bundle();
        } catch (Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
       
            assertTrue(false);
        }
    }
}


