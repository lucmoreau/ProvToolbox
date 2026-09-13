package org.openprovenance.prov.dot.test;

import junit.framework.TestCase;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.notation.Utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The qualified rendering draws every qualified relation with an attribute box beside it.
 * A relation whose attributes are all links to other statements (openprov:*) and which has no
 * identifier has nothing to show, so no box must be drawn: an empty box is a defect.
 */
public class QualifiedVisualisationTest extends TestCase {

    static final String RESOURCES = "src/test/resources/org/openprovenance/prov/dot/test/";

    /** A graphviz node group: <!-- title --> <g id="..." class="node"> ... </g> */
    static final Pattern NODE = Pattern.compile("<g id=\"[^\"]*\" class=\"node\">\\s*<title>([^<]*)</title>(.*?)</g>", Pattern.DOTALL);

    final ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();

    Document readProvn(String provnFile) throws IOException {
        Utility u = new Utility(DateTimeOption.PRESERVE, null);
        Document doc = (Document) u.convertSyntaxTreeToJavaBean(provnFile, pFactory);
        Namespace.withThreadNamespace(doc.getNamespace());
        return doc;
    }

    String toQualifiedSvg(String provnFile, String svgFile) throws IOException {
        Document doc = readProvn(provnFile);
        Map<String, ProvSerialiser> serialisers = ProvToDot.registerQualifiedSerialisers(pFactory, new HashMap<>());
        ProvSerialiser qualifiedSvg = serialisers.get(ProvToDot.QUALIFIED_SVG);
        try (FileOutputStream out = new FileOutputStream(svgFile)) {
            qualifiedSvg.serialiseDocument(out, doc, true);
        }
        return Files.readString(Path.of(svgFile), StandardCharsets.UTF_8);
    }

    /** Attribute boxes are the nodes named "-attrs...": each must carry at least one line of text. */
    static List<String> emptyAttributeBoxes(String svg) {
        List<String> empty = new ArrayList<>();
        Matcher m = NODE.matcher(svg);
        while (m.find()) {
            String title = m.group(1);
            String body = m.group(2);
            if (title.contains("attrs") && !body.contains("<text")) {
                empty.add(title);
            }
        }
        return empty;
    }

    static void assertNoEmptyAttributeBox(String svgFile, String svg) {
        List<String> empty = emptyAttributeBoxes(svg);
        if (!empty.isEmpty()) {
            throw new IllegalStateException("empty attribute box(es) in " + svgFile + ": " + empty);
        }
    }

    public void testTransportingQualifiedSvg() throws IOException {
        String svgFile = "target/transporting.qualified.svg";
        String svg = toQualifiedSvg(RESOURCES + "transporting.provn", svgFile);
        assertTrue("dot produced an svg", svg.contains("<svg"));
        assertNoEmptyAttributeBox(svgFile, svg);
        // the identified relations keep their boxes
        assertTrue(svg.contains(">asc1<"));
        assertTrue(svg.contains(">gen1<"));
    }

    public void testTransportingWithIdsQualifiedSvg() throws IOException {
        String svgFile = "target/transporting-with-ids.qualified.svg";
        String svg = toQualifiedSvg(RESOURCES + "transporting-with-ids.provn", svgFile);
        assertTrue("dot produced an svg", svg.contains("<svg"));
        assertNoEmptyAttributeBox(svgFile, svg);
        // attribution and specialisations now have identifiers, shown in their boxes
        assertTrue(svg.contains(">att1<"));
        assertTrue(svg.contains(">spe1<"));
        assertTrue(svg.contains(">spe2<"));
    }

    public void testEmptyAttributeBoxIsDetected() {
        String svg = "<g id=\"node18\" class=\"node\">\n<title>&#45;attrsbn06</title>\n<polygon fill=\"none\"/>\n</g>\n"
                + "<g id=\"asc1\" class=\"node\">\n<title>&#45;attrsasc15</title>\n<polygon fill=\"none\"/>\n<text>id:</text>\n</g>\n";
        assertEquals(List.of("&#45;attrsbn06"), emptyAttributeBoxes(svg));
    }

    public void testTransportingQualifiedPdf() throws IOException {
        Document doc = readProvn(RESOURCES + "transporting-with-ids.provn");
        String pdfFile = "target/transporting-with-ids.qualified.pdf";
        try (FileOutputStream out = new FileOutputStream(pdfFile)) {
            ProvToDot.newQualifiedSerialiser(pFactory, ProvToDot.QUALIFIED_PDF).serialiseDocument(out, doc, true);
        }
        byte[] pdf = Files.readAllBytes(Path.of(pdfFile));
        assertTrue("dot produced a pdf", pdf.length > 4 && new String(pdf, 0, 4, StandardCharsets.US_ASCII).equals("%PDF"));
    }

    public void testQualifiedFormats() {
        assertEquals(List.of("qualified.png", "qualified.svg", "qualified.pdf"), ProvToDot.QUALIFIED_FORMATS);
        try {
            ProvToDot.newQualifiedSerialiser(pFactory, "svg");
            fail("plain svg is not a qualified format");
        } catch (IllegalArgumentException expected) {
            // fine
        }
    }
}
