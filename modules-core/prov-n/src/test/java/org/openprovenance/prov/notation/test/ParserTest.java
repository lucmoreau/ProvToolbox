package org.openprovenance.prov.notation.test;
import junit.framework.TestCase;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.notation.Utility;

public class ParserTest extends TestCase {
    public void testBundle0() {
        dotestBundle("src/test/resources/prov/container0.provn");
    }

    public void testBundle1() {
        dotestBundle("src/test/resources/prov/container1.provn");
    }

    public void testBundle2() {
        dotestBundle("src/test/resources/prov/container2.provn");
    }


    public void testBundle3() {
        dotestBundle("src/test/resources/prov/bundles1.provn");
    }

    public void testBundle4() {
        dotestBundle("src/test/resources/prov/bundles2.provn");
    }
    
    public void dotestBundle(String file) {
        try {
            new Utility(DateTimeOption.PRESERVE, null).getParserForFile(file).document();
        } catch (Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
       
            assertTrue(false);
        }
    }
}


