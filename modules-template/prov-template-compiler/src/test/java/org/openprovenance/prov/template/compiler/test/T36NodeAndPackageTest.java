package org.openprovenance.prov.template.compiler.test;

import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.SubtypeException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * T36, two findings of the first application (Luc, 2026-11-16): a subtype may fix a node's local part ("Yes, fix a node's local
 * part"), and its typed bean, request and reply are in the package the subtype declares ("Honour the declared package").
 */
public class T36NodeAndPackageTest extends T36Fixture {

    static final String GENERAL = "org.example.t36.approval.client.integrator.";
    static final String ACTIONS = "org.example.actions.client.integrator.";

    boolean intNode = false;

    @Override
    tools.jackson.databind.node.ObjectNode bindings() throws Exception {
        tools.jackson.databind.node.ObjectNode b = super.bindings();
        if (intNode) ((tools.jackson.databind.node.ObjectNode) b.get("var").get("approver").get(0)).put("@type", "xsd:int");
        return b;
    }

    /** A subtype in a package of its own. */
    String inActions(String subtype) {
        return subtype.replace("\"package\":\"org.example.t36.approval\"", "\"package\":\"org.example.actions\"");
    }

    String alice() {
        return inActions(subtype("alice_line", "{\"approver\":\"alice\"}", "{\"priceBefore\":\"required\"}"));
    }

    String bob() {
        return inActions(subtype("bob_line", "{\"approver\":\"bob\",\"approver_name\":\"Bob\"}", "{\"priceBefore\":\"optional\"}"));
    }

    public void testTheTypedBeanIsInThePackageTheSubtypeDeclares() throws Exception {
        generate("pkg", false, List.of(), List.of(alice(), bob()));
        Path src = Path.of(OUT + "pkg/libs/t36/t36_cli/src/main/java");
        for (String f : List.of("Alice_lineInputs", "Bob_lineInputs")) {
            assertTrue(f + " is in the declared package", Files.exists(src.resolve("org/example/actions/client/integrator/" + f + ".java")));
            assertFalse(f + " is not in the general template's", Files.exists(src.resolve("org/example/t36/approval/client/integrator/" + f + ".java")));
        }
        String text = Files.readString(src.resolve("org/example/actions/client/integrator/Alice_lineInputs.java"));
        assertTrue(text, text.startsWith("// Generated") && text.contains("package org.example.actions.client.integrator;"));
        assertTrue("it imports the general bean it converts to", text.contains("import org.example.t36.approval.client.integrator.ApprovalInputs;"));
        String dispatcher = Files.readString(src.resolve("org/example/t36/catalogue/common/Subtypes.java"));
        assertTrue("the dispatcher imports them from there: " + dispatcher, dispatcher.contains("import org.example.actions.client.integrator.Alice_lineInputs;"));
        ClassLoader l = compileSources("pkg", List.of("org/example/actions/client/integrator/Alice_lineInputs.java", "org/example/actions/client/integrator/Bob_lineInputs.java",
                "org/example/t36/catalogue/common/Subtypes.java"));
        if (l != null) assertEquals(ACTIONS + "Alice_lineInputs", l.loadClass(ACTIONS + "Alice_lineInputs").getName());
    }

    public void testASubtypeFixesANodesLocalPart() throws Exception {
        generate("node", false, List.of(), List.of(alice(), bob()));
        ClassLoader l = compileSources("node", List.of("org/example/actions/client/integrator/Alice_lineInputs.java", "org/example/actions/client/integrator/Bob_lineInputs.java",
                "org/example/t36/catalogue/common/Subtypes.java"));
        if (l == null) return;
        Class<?> alice = l.loadClass(ACTIONS + "Alice_lineInputs"), general = l.loadClass(GENERAL + "ApprovalInputs");
        for (String f : List.of("approver", "approver_name")) {
            try {
                alice.getField(f);
                if (f.equals("approver")) fail("the fixed node is not a field of the typed bean");
            } catch (NoSuchFieldException e) {
                assertEquals("approver", f);
            }
        }
        Object a = T36SubtypeTest.newInstance(l, alice.getName());
        T36SubtypeTest.set(a, "priceBefore", 1.5);
        T36SubtypeTest.set(a, "approver_name", "Alice");
        Object g = T36SubtypeTest.invoke(a, alice, "toGeneral");
        assertEquals("the fixed node's local part is the bean's value", "alice", T36SubtypeTest.get(g, "approver"));
        assertSame(general, g.getClass());
        assertNotNull(T36SubtypeTest.invoke(null, alice, "fromGeneral", g));
        T36SubtypeTest.set(g, "approver", "carol");
        try {
            T36SubtypeTest.invoke(null, alice, "fromGeneral", g);
            fail("a bean of another node");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("approver"));
        }
        // the dispatcher tells the two subtypes by the node they fix
        Class<?> subtypes = l.loadClass("org.example.t36.catalogue.common.Subtypes");
        T36SubtypeTest.set(g, "approver", "bob");
        T36SubtypeTest.set(g, "approver_name", "Bob");
        assertEquals("t31.bob_line", T36SubtypeTest.invoke(null, subtypes, "whichApproval", g));
        T36SubtypeTest.set(g, "approver", "alice");
        assertEquals("t31.alice_line", T36SubtypeTest.invoke(null, subtypes, "whichApproval", g));
    }

    public void testTheTurtleAndTheDocumentationSayWhatIsFixed() throws Exception {
        generate("nodedoc", false, List.of(), List.of(alice(), bob()), c -> {
            c.put("turtle", "catalogue.ttl");
            c.put("turtleNamespace", "http://example.org/t36#");
        });
        String ttl = Files.readString(Path.of(OUT + "nodedoc/libs/t36/t36_cli/src/main/resources/catalogue.ttl"));
        assertTrue(ttl, ttl.contains("tpl:fixes [ tpl:variable \"approver\" ; tpl:value \"alice\" ]"));
        String doc = Files.readString(Path.of(OUT + "nodedoc/libs/" + DOC));
        assertTrue(doc, doc.contains("approver = alice"));
    }

    /** A node declared with a type holds a value of that type: fixed, it is a constant of that type (nextgen's nodes are xsd:int, the first application's actions xsd:string). */
    public void testAFixedNodeOfTypeIntIsANumber() throws Exception {
        intNode = true;
        generate("intnode", false, List.of(), List.of(inActions(subtype("seven_line", "{\"approver\":7}", "{\"priceBefore\":\"required\"}"))));
        ClassLoader l = compileSources("intnode", List.of("org/example/actions/client/integrator/Seven_lineInputs.java"));
        if (l == null) return;
        Class<?> seven = l.loadClass(ACTIONS + "Seven_lineInputs");
        Object a = T36SubtypeTest.newInstance(l, seven.getName());
        T36SubtypeTest.set(a, "priceBefore", 1.5);
        assertEquals("the node's value is the constant, an int", 7, T36SubtypeTest.get(T36SubtypeTest.invoke(a, seven, "toGeneral"), "approver"));
        // a text is no int
        try {
            new ConfigProcessor(pf).checkSubtypes(parse(catalogue("intnode2", false, List.of(), List.of(inActions(subtype("bad", "{\"approver\":\"seven\"}", "{}"))))), ".");
            fail("a text for an int node");
        } catch (SubtypeException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("fixed variable approver (xsd:int)"));
        }
    }
}
