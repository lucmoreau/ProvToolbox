package org.openprovenance.prov.notation;
import junit.framework.TestCase;

public class ParserTest extends TestCase
{
    public void testBundle0() {
        testBundle("src/test/resources/prov/container0.provn");
    }

    public void testBundle1() {
        testBundle("src/test/resources/prov/container1.provn");
    }

    public void testBundle2() {
        testBundle("src/test/resources/prov/container2.provn");
    }


    public void testBundle3() {
        testBundle("src/test/resources/prov/bundles1.provn");
    }


    public void testBundle4() {
        testBundle("src/test/resources/prov/bundles2.provn");
    }

    public void testBundle(String file) {
        try {
            new Utility().getParserForFile(file).document();
        } catch (Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
       
            assertTrue(false);
        }
    }
}


