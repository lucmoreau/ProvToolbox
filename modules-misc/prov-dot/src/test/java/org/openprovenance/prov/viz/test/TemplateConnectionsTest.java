package org.openprovenance.prov.viz.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.viz.VizGraph;
import org.openprovenance.prov.viz.templates.TemplateConnectionGraph;
import org.openprovenance.prov.viz.templates.TemplateConnectionGraph.Connection;
import org.openprovenance.prov.viz.templates.TemplatesToDot;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/** The template-connection drawings, fed by hand rather than by the service's queries. */
public class TemplateConnectionsTest extends TestCase {

    static final String FT = "org.openprovenance.book.fs.FileTransforming";
    static final String FC = "org.openprovenance.book.fs.FileCreating";
    static final String PROV_ENTITY = "http://www.w3.org/ns/prov#Entity";
    static final String PROV_ACTIVITY = "http://www.w3.org/ns/prov#Activity";

    TemplateConnectionGraph graph() {
        TemplateConnectionGraph g = new TemplateConnectionGraph();
        g.provAPI = "/book/provapi";
        g.shortNames.put(FT, "FileTransforming");
        g.shortNames.put(FC, "FileCreating");
        g.inputs.put("FileTransforming", Map.of("input", "file"));
        g.outputs.put("FileTransforming", Map.of("output", "file"));
        g.outputs.put("FileCreating", Map.of("file", "file"));
        g.baseTypes.put(FT, Map.of("input", PROV_ENTITY, "output", PROV_ENTITY, "transforming", PROV_ACTIVITY));
        g.baseTypes.put(FC, Map.of("file", PROV_ENTITY));
        g.selectedSuccessors.put(FT, Map.of("input", List.of("output")));
        g.connections.add(Connection.of(FT, 12, "input", "Copy", FC, 3, "file", null));
        return g;
    }

    public void testTemplateStyle() throws IOException, InterruptedException {
        TemplatesToDot toDot = new TemplatesToDot(new org.openprovenance.prov.vanilla.ProvFactory(), false, null);
        VizGraph model = toDot.buildTemplateGraph(graph(), "connections");
        assertEquals(2, model.nodes().size());
        assertEquals(1, model.edges().size());
        assertEquals("FileTransforming_FileTransforming_12_input:n", model.edges().get(0).tailPort);
        assertEquals("FileCreating_FileCreating_3_file:s", model.edges().get(0).headPort);

        Path dot = Path.of("target/template-connections.dot");
        try (PrintStream out = new PrintStream(dot.toFile(), StandardCharsets.UTF_8)) {
            toDot.convertTemplate(graph(), out, "connections");
        }
        String text = Files.readString(dot, StandardCharsets.UTF_8);
        assertTrue(text.contains("shape=\"plaintext\""));
        assertTrue(text.contains("<TD PORT=\"FileTransforming_FileTransforming_12_input\" BGCOLOR=\"#FFFC87\""));
        assertTrue(text.contains("arrowhead=\"invempty\",arrowtail=\"empty\",dir=\"both\""));
        assertNull(MermaidValidator.run(List.of("dot", "-Tsvg", "-o", "target/template-connections.svg", dot.toString())));
    }

    public void testStartTemplateAloneIsDrawn() {
        TemplateConnectionGraph g = graph();
        g.connections.clear();
        g.startTemplate = FC;
        g.startTemplateId = 7;
        g.startSemanticType = "Original";
        TemplatesToDot toDot = new TemplatesToDot(new org.openprovenance.prov.vanilla.ProvFactory(), false, null);
        VizGraph model = toDot.buildTemplateGraph(g, "connections");
        assertEquals(1, model.nodes().size());
        assertTrue(model.nodes().get(0).rawLabel.contains("FileCreating_7 <BR/>(Original)"));
    }

    public void testEntitiesStyle() throws IOException, InterruptedException {
        TemplatesToDot toDot = new TemplatesToDot(new org.openprovenance.prov.vanilla.ProvFactory(), false, null);
        org.openprovenance.prov.model.Document doc = toDot.entitiesDocument(graph());
        assertEquals(3, doc.getStatementOrBundle().size()); // older, newer, and the derivation between them
        Path dot = Path.of("target/template-entities.dot");
        try (PrintStream out = new PrintStream(dot.toFile(), StandardCharsets.UTF_8)) {
            toDot.convertEntities(graph(), out, "entities");
        }
        String text = Files.readString(dot, StandardCharsets.UTF_8);
        assertTrue(text.contains("style=\"dashed\""));
        assertNull(MermaidValidator.run(List.of("dot", "-Tcanon", "-o", "/dev/null", dot.toString())));
    }
}
