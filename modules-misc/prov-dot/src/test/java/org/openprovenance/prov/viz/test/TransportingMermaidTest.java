package org.openprovenance.prov.viz.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.viz.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** The transporting template as mermaid, plain and qualified, rendered to svg and png by mmdc when it is installed. */
public class TransportingMermaidTest extends TestCase {

    static final String RESOURCES = "src/test/resources/org/openprovenance/prov/dot/test/";
    final ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();

    Document readProvn(String provnFile) throws IOException {
        Document doc = (Document) new Utility(DateTimeOption.PRESERVE, null).convertSyntaxTreeToJavaBean(provnFile, pFactory);
        Namespace.withThreadNamespace(doc.getNamespace());
        return doc;
    }

    /** Builds the model, checks it, writes the mermaid text, and renders it when mmdc is there. */
    String mermaid(String provn, String stem, boolean qualified) throws IOException {
        Document doc = readProvn(RESOURCES + provn);
        ProvToMermaid toMermaid = qualified ? ProvToMermaid.newProvToMermaid(pFactory, true) : new ProvToMermaid(pFactory);
        toMermaid.setMaxStringLength(QualifiedFormats.QUALIFIED_MAX_STRING_LENGTH);

        VizGraph graph = toMermaid.process(toMermaid.build(doc, stem));
        List<String> violations = VizChecks.violations(graph);
        assertTrue(stem + ": " + violations, violations.isEmpty());

        Path mmd = Path.of("target/" + stem + ".mmd");
        try (FileOutputStream out = new FileOutputStream(mmd.toFile())) {
            toMermaid.convert(doc, out, ProvToMermaid.NOTATION_EXTENSION, stem);
        }
        String text = Files.readString(mmd, StandardCharsets.UTF_8);
        assertTrue(text.startsWith("---\ntitle: \"" + stem + "\"\n---\nflowchart BT\n"));

        if (toMermaid.mmdcAvailable()) {
            for (String type : List.of("svg", "png")) {
                Path rendered = Path.of("target/" + stem + "." + type);
                try (FileOutputStream out = new FileOutputStream(rendered.toFile())) {
                    toMermaid.convert(doc, out, type, stem);
                }
                assertTrue(rendered + " is empty", Files.size(rendered) > 0);
            }
            assertTrue(Files.readString(Path.of("target/" + stem + ".svg"), StandardCharsets.UTF_8).contains("<svg"));
        } else {
            System.out.println("########## mmdc not found: " + stem + " not rendered");
        }
        return text;
    }

    public void testTransportingPlain() throws IOException {
        String text = mermaid("transporting.provn", "transporting.mermaid", false);
        assertTrue(text.contains("transporting@{ shape: rect"));
        assertTrue(text.contains("transporter@{ shape: trap-b"));
        assertTrue(text.contains("item1@{ shape: stadium"));
    }

    public void testTransportingQualified() throws IOException {
        String text = mermaid("transporting.provn", "transporting.qualified.mermaid", true);
        // the identified relations have an id box, the unidentified attribution and specialisation have none
        assertTrue(text.contains("id: asc1"));
        assertFalse(text.contains("label: \"\" }"));
    }

    public void testTransportingWithIdsQualified() throws IOException {
        String text = mermaid("transporting-with-ids.provn", "transporting-with-ids.qualified.mermaid", true);
        assertTrue(text.contains("id: att1"));
        assertTrue(text.contains("id: spe1"));
        assertTrue(text.contains("id: spe2"));
    }

    public void testQualifiedMmdSerialiser() throws IOException {
        Document doc = readProvn(RESOURCES + "transporting-with-ids.provn");
        org.openprovenance.prov.model.ProvSerialiser s = QualifiedFormats.newQualifiedSerialiser(pFactory, QualifiedFormats.QUALIFIED_MMD);
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        s.serialiseDocument(out, doc, true);
        assertTrue(out.toString(StandardCharsets.UTF_8).contains("flowchart BT"));
        assertEquals(List.of("qualified.png", "qualified.svg", "qualified.pdf", "qualified.mmd"), QualifiedFormats.ALL_FORMATS);
    }
}
