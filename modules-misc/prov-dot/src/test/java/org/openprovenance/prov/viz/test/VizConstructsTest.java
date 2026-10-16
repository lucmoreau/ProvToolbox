package org.openprovenance.prov.viz.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.exception.DocumentedUnsupportedCaseException;
import org.openprovenance.prov.model.test.RoundTripFromJavaTest;
import org.openprovenance.prov.viz.ProvToMermaid;
import org.openprovenance.prov.viz.ProvViz;
import org.openprovenance.prov.viz.VizChecks;
import org.openprovenance.prov.viz.VizGraph;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Every PROV construct the model tests enumerate, drawn plain and qualified by both backends:
 * the model must satisfy its invariants, {@code dot} must parse the dot text, and {@code mmdc}
 * must render the mermaid text (checked once for all files, when the suite ends).
 */
public class VizConstructsTest extends RoundTripFromJavaTest {

    static final Path DIR = Path.of("target/viz-constructs");
    static final List<Path> mermaidFiles = new ArrayList<>();
    static boolean mmdcAvailable;

    public static Test suite() {
        return new TestSetup(new TestSuite(VizConstructsTest.class)) {
            @Override
            protected void setUp() throws IOException {
                java.nio.file.Files.createDirectories(DIR);
                mermaidFiles.clear();
                mmdcAvailable = MermaidValidator.available();
                if (!mmdcAvailable) System.out.println("########## mmdc not found: mermaid output is produced but not validated");
            }

            @Override
            protected void tearDown() throws Exception {
                if (!mmdcAvailable) return;
                List<String> failures = MermaidValidator.validateAll(mermaidFiles, DIR);
                assertTrue("mermaid files mmdc rejects:\n" + String.join("\n", failures), failures.isEmpty());
                System.out.println("mmdc accepted " + mermaidFiles.size() + " mermaid diagrams");
            }
        };
    }

    @Override
    public String extension() {
        return "";
    }

    @Override
    public boolean checkSchema(String name) {
        return false;
    }

    /** Dictionaries are documented as unsupported by the visualisation; the day they are, this test says so. */
    boolean expectsUnsupported() {
        return getName().startsWith("testDictionary");
    }

    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        String base = new File(file).getName();
        Namespace.withThreadNamespace(doc.getNamespace());
        try {
            for (boolean qualified : new boolean[]{false, true}) {
                String stem = base + (qualified ? ".qualified" : "");
                ProvToDot toDot = qualified ? ProvToDot.newProvToDot(pFactory, true) : new ProvToDot(pFactory);
                ProvToMermaid toMermaid = qualified ? ProvToMermaid.newProvToMermaid(pFactory, true) : new ProvToMermaid(pFactory);
                checkAndRender(toDot, doc, stem, false);
                Path mmd = checkAndRender(toMermaid, doc, stem, true);
                mermaidFiles.add(mmd);
                parseWithDot(DIR.resolve(stem + ".dot"));
            }
            if (expectsUnsupported()) fail("dictionaries are drawn now: drop the exception in VizConstructsTest");
        } catch (DocumentedUnsupportedCaseException | UnsupportedOperationException e) {
            if (!expectsUnsupported()) throw e;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    Path checkAndRender(ProvViz viz, Document doc, String stem, boolean requireDeclaredEndpoints) throws IOException {
        VizGraph graph = viz.process(viz.build(doc, stem));
        List<String> violations = VizChecks.violations(graph, requireDeclaredEndpoints);
        assertTrue(stem + " (" + viz.notationExtension() + "): " + violations, violations.isEmpty());
        Path out = DIR.resolve(stem + "." + viz.notationExtension());
        try (PrintStream ps = new PrintStream(out.toFile(), StandardCharsets.UTF_8)) {
            viz.render(graph, ps);
        }
        return out;
    }

    void parseWithDot(Path dot) throws IOException, InterruptedException {
        String problem = MermaidValidator.run(List.of("dot", "-Tcanon", "-o", "/dev/null", dot.toString()));
        assertNull("dot rejects " + dot + ": " + problem, problem);
    }
}
