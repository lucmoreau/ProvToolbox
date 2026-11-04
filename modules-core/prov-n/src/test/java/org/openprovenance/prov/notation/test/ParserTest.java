package org.openprovenance.prov.notation.test;
import junit.framework.TestCase;
import org.antlr.v4.runtime.CharStreams;
import org.openprovenance.prov.notation.ProvNReader;
import org.openprovenance.prov.notation.ProvNSyntaxException;

import java.io.IOException;

/** Files that are syntactically valid PROV-N, whether or not a document can be constructed from them. */
public class ParserTest extends TestCase {
    final ProvNReader reader = new ProvNReader(new org.openprovenance.prov.vanilla.ProvFactory());

    public void testBundle0() throws IOException {
        dotestBundle("src/test/resources/prov/container0.provn");
    }

    public void testBundle1() throws IOException {
        dotestBundle("src/test/resources/prov/container1.provn");
    }

    /** A datatype that is an IRI, where PROV-N requires a qualified name. */
    public void testBundle2() throws IOException {
        try {
            dotestBundle("src/test/resources/prov/container2.provn");
            fail("syntax errors expected");
        } catch (ProvNSyntaxException expected) {
        }
    }


    public void testBundle3() throws IOException {
        dotestBundle("src/test/resources/prov/bundles1.provn");
    }

    public void testBundle4() throws IOException {
        dotestBundle("src/test/resources/prov/bundles2.provn");
    }

    public void dotestBundle(String file) throws IOException {
        assertNotNull(reader.parse(CharStreams.fromFileName(file)));
    }
}
