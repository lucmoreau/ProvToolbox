package org.openprovenance.prov.template.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.core.Bindings;
import org.openprovenance.prov.template.core.InstantiateUtil;
import org.openprovenance.prov.template.core.Instantiater;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The template vocabulary is http://openprovenance.org/ns/tmpl#; templates written before it moved under /ns/
 * declare http://openprovenance.org/tmpl#. Both instantiate alike: the legacy namespace is accepted on input and
 * never written.
 */
public class LegacyTemplateNamespaceTest extends TestCase {

    static final ProvFactory pf = new ProvFactory();
    static final String TEMPLATE = "src/test/resources/templates2/template10.provn";
    static final String BINDINGS = "src/test/resources/bindings/bindings10.json";

    Document template(String tmplNamespace) throws IOException {
        String text = new String(java.nio.file.Files.readAllBytes(new File(TEMPLATE).toPath()), StandardCharsets.UTF_8);
        assertTrue(text.contains("prefix tmpl <" + InstantiateUtil.TMPL_NS + ">"));
        text = text.replace("prefix tmpl <" + InstantiateUtil.TMPL_NS + ">", "prefix tmpl <" + tmplNamespace + ">");
        return new Utility().readDocument(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), pf);
    }

    Bindings bindings() throws IOException {
        return new ObjectMapper().readValue(new File(BINDINGS), Bindings.class);
    }

    String notation(Document doc) {
        return new Utility().convertBeanToSyntaxTree(doc, pf);
    }

    public void testLegacyNamespaceInstantiatesLikeTheCurrentOne() throws IOException {
        Document current = new Instantiater(pf).instantiate(template(InstantiateUtil.TMPL_NS), bindings());
        Document legacy = new Instantiater(pf).instantiate(template(InstantiateUtil.LEGACY_TMPL_NS), bindings());
        assertEquals(notation(current), notation(legacy));
        String written = notation(legacy);
        assertFalse(written, written.contains(InstantiateUtil.LEGACY_TMPL_NS));
        assertFalse("tmpl attributes are consumed: " + written, written.contains("tmpl:"));
        // the start and end time reached the activity
        List<Activity> activities = new org.openprovenance.prov.model.ProvUtilities().getBundle(legacy).get(0).getStatement().stream()
                .filter(s -> s instanceof Activity).map(s -> (Activity) s).collect(Collectors.toList());
        assertTrue(activities.stream().anyMatch(a -> a.getStartTime() != null && a.getEndTime() != null && String.valueOf(a.getStartTime()).startsWith("2023-09-05")));
    }

    public void testLegacyNamespaceIsRenamedOnTheWayIn() throws IOException {
        Document doc = template(InstantiateUtil.LEGACY_TMPL_NS);
        InstantiateUtil.withCurrentTemplateNamespace(doc, pf);
        Bundle bundle = (Bundle) doc.getStatementOrBundle().get(0);
        assertEquals(InstantiateUtil.TMPL_NS, doc.getNamespace().getPrefixes().get("tmpl"));
        assertFalse(doc.getNamespace().getNamespaces().containsKey(InstantiateUtil.LEGACY_TMPL_NS));
        for (Statement s : bundle.getStatement()) {
            if (s instanceof HasOther) {
                for (Other o : ((HasOther) s).getOther()) assertFalse(o.toString(), InstantiateUtil.LEGACY_TMPL_NS.equals(o.getElementName().getNamespaceURI()));
            }
        }
        String written = notation(doc);
        assertTrue(written, written.contains("prefix tmpl <" + InstantiateUtil.TMPL_NS + ">"));
        assertTrue(written, written.contains("tmpl:startTime = 'var:start'"));
    }
}
