package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * T44: reading a template when the interop framework (module prov-interop, not a dependency of the compiler) is not on the classpath.
 * It is not an error: a template in provn is read by the provn reader, with nothing written to the error stream; a template in another
 * format is refused with a message that says what is missing, instead of being handed to a provn parser.
 */
public class T44InteropLookupTest extends TestCase {

    static final String PROVN = "src/test/resources/t36/flow/exchanging.provn";

    final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();

    /** A compiler utility on a classpath that does not have the interop framework, whatever the test's own classpath has. */
    final CompilerUtil withoutInterop = new CompilerUtil(pf) {
        @Override
        protected boolean interopFrameworkAvailable() {
            return false;
        }
    };

    public void testAProvnTemplateIsReadWithoutTheInteropFrameworkAndWithoutNoise() throws Exception {
        PrintStream err = System.err;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Document doc;
        try {
            System.setErr(new PrintStream(captured, true));
            doc = withoutInterop.readDocumentFromFile(PROVN);
        } finally {
            System.setErr(err);
        }
        assertNotNull(doc);
        assertFalse("the document has statements", doc.getStatementOrBundle().isEmpty());
        assertEquals("nothing is written to the error stream", "", captured.toString());
    }

    public void testATemplateInAnotherFormatSaysWhatIsMissing() throws Exception {
        for (String file : new String[] { "template.jsonld", "template.json" }) {
            try {
                withoutInterop.readDocumentFromFile("src/test/resources/t36/flow/" + file);
                fail("a " + file + " cannot be read without the interop framework");
            } catch (ClassNotFoundException e) {
                assertTrue(e.getMessage(), e.getMessage().contains("cannot read"));
                assertTrue(e.getMessage(), e.getMessage().contains("InteropFramework"));
                assertTrue(e.getMessage(), e.getMessage().contains("only .provn"));
            }
        }
    }

    /** With or without the interop framework on this test's own classpath, the same provn file gives the same statements. */
    public void testTheSameProvnFileGivesTheSameStatementsEitherWay() throws Exception {
        int without = withoutInterop.readDocumentFromFile(PROVN).getStatementOrBundle().size();
        int with = new CompilerUtil(pf).readDocumentFromFile(PROVN).getStatementOrBundle().size();
        assertEquals(without, with);
    }
}
