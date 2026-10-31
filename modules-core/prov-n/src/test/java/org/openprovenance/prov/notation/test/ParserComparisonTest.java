package org.openprovenance.prov.notation.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.notation.ProvNReader;
import org.openprovenance.prov.notation.ProvNSyntaxException;
import org.openprovenance.prov.notation.test.ParserComparison.Outcome;
import org.openprovenance.prov.notation.test.ParserComparison.Verdict;

/**
 * Compares the documents constructed by the ANTLR 3 parser (PROV_N.g, read via Utility) and by the ANTLR 4 parser
 * (ProvN.g4, read via ProvNReader), for the same PROV-N.
 * <p>
 * Where both parsers accept a document, they must construct the same representation. The new parser may reject a
 * document that the old one accepts, or vice versa, only for the reasons documented in the tests named
 * testDivergence*, which check what the new parser does in these cases.
 */
public class ParserComparisonTest extends TestCase {

    static final ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    static final Name name = pFactory.getName();

    final ParserComparison comparison = new ParserComparison(pFactory);
    final ProvNReader reader = new ProvNReader(pFactory);

    static final String PROLOG = "document\n"
            + "prefix ex <http://example.org/>\n"
            + "prefix ex2 <http://example2.org/>\n"
            + "prefix provext <" + NamespacePrefixMapper.PROV_EXT_NS + ">\n";
    static final String EPILOG = "\nendDocument\n";

    static String doc(String... statements) {
        return PROLOG + String.join("\n", statements) + EPILOG;
    }

    /* ======================================================================
       Corpora of PROV-N files
       ====================================================================== */

    /**
     * Files that the old parser accepts, and the new one rejects, on purpose: they have text after endDocument
     * (see {@link #testDivergenceTextAfterEndDocument()}).
     */
    static final Set<String> TEXT_AFTER_END_DOCUMENT = Set.of("w3c-publication2.prov-asn", "w3c-publication2.provn");

    /** All the PROV-N files under a directory. */
    static List<Path> provnFiles(Path root) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            return paths.filter(p -> {
                            String s = p.toString();
                            return s.endsWith(".provn") || s.endsWith(".pn") || s.endsWith(".asn") || s.endsWith(".prov-asn");
                        })
                        .filter(p -> !p.toString().contains(File.separator + "target" + File.separator))
                        .filter(p -> !p.toString().contains(File.separator + "node_modules" + File.separator))
                        .sorted()
                        .collect(Collectors.toList());
        }
    }

    Map<Verdict, List<Outcome>> compareAll(List<Path> files) {
        Map<Verdict, List<Outcome>> outcomes = new EnumMap<>(Verdict.class);
        for (Verdict v : Verdict.values()) outcomes.put(v, new LinkedList<>());
        for (Path p : files) {
            Outcome o = comparison.compareFile(p.toString());
            outcomes.get(o.verdict).add(o);
        }
        return outcomes;
    }

    static void report(String title, Map<Verdict, List<Outcome>> outcomes) {
        System.out.println("==== Comparison of the ANTLR 3 and ANTLR 4 PROV-N parsers: " + title);
        for (Verdict v : Verdict.values()) System.out.println("  " + v + ": " + outcomes.get(v).size());
        for (Verdict v : List.of(Verdict.DIFFERENT, Verdict.NEW_REJECTS, Verdict.OLD_REJECTS, Verdict.OLD_RECOVERS, Verdict.BOTH_REJECT)) {
            for (Outcome o : outcomes.get(v)) System.out.println("  " + o);
        }
    }

    /** No document is read differently by the two parsers, and the new parser only rejects what it should. */
    static void checkOutcomes(Map<Verdict, List<Outcome>> outcomes) {
        List<String> failures = new ArrayList<>();
        for (Outcome o : outcomes.get(Verdict.DIFFERENT)) failures.add(o.toString());
        for (Outcome o : outcomes.get(Verdict.NEW_REJECTS)) {
            if (!TEXT_AFTER_END_DOCUMENT.contains(Path.of(o.source).getFileName().toString())) failures.add(o.toString());
        }
        assertTrue("Parsers disagree:\n" + String.join("\n", failures), failures.isEmpty());
    }

    /** The PROV-N files of this module. */
    public void testModuleResources() throws IOException {
        Map<Verdict, List<Outcome>> outcomes = compareAll(provnFiles(Path.of("src/test/resources")));
        report("src/test/resources", outcomes);
        checkOutcomes(outcomes);
        assertTrue(outcomes.get(Verdict.SAME).size() >= 15);
    }

    /** All the PROV-N files of the ProvToolbox repository, when this module is built in it. */
    public void testRepositoryCorpus() throws IOException {
        Path root = Path.of("../..").toAbsolutePath().normalize();
        if (!Files.isDirectory(root.resolve("modules-core").resolve("prov-n"))) {
            System.out.println("ProvToolbox repository not found: skipping corpus comparison");
            return;
        }
        Map<Verdict, List<Outcome>> outcomes = compareAll(provnFiles(root));
        report("repository " + root, outcomes);
        checkOutcomes(outcomes);
    }

    /* ======================================================================
       Snippets, covering the constructs of the grammar
       ====================================================================== */

    void assertSame(String provn) {
        Outcome o = comparison.compareString(provn);
        assertEquals(o.toString(), Verdict.SAME, o.verdict);
    }

    void assertSameStatements(String... statements) {
        for (String s : statements) assertSame(doc(s));
    }

    public void testEntitiesAndAgents() {
        assertSameStatements(
                "entity(ex:e1)",
                "entity(ex:e1, [])",
                "entity(ex:e1, [ex:a=\"v\"])",
                "agent(ex:ag1)",
                "agent(ex:ag1, [prov:type='prov:Person', prov:label=\"Alice\"])",
                "entity(ex2:e1) entity(ex:e2) agent(ex:e3)");
    }

    public void testActivities() {
        assertSameStatements(
                "activity(ex:a1)",
                "activity(ex:a1, -, -)",
                "activity(ex:a1, 2011-11-16T16:05:00, -)",
                "activity(ex:a1, -, 2011-11-16T16:05:00Z)",
                "activity(ex:a1, 2011-11-16T16:05:00.123, 2011-11-16T18:05:00+01:00, [ex:a=1])",
                "activity(ex:a1, 2011-11-16T16:05:00-05:00, 2011-11-16T16:05:00.5Z)");
    }

    public void testGenerationUsageInvalidation() {
        for (String r : List.of("wasGeneratedBy", "used", "wasInvalidatedBy")) {
            assertSameStatements(
                    r + "(ex:x1)",
                    r + "(ex:x1, ex:x2, -)",
                    r + "(ex:x1, -, 2011-11-16T16:05:00)",
                    r + "(ex:id; ex:x1, ex:x2, 2011-11-16T16:05:00)",
                    r + "(-; ex:x1, ex:x2, -)",
                    r + "(ex:id; ex:x1, [prov:role='ex:r'])",
                    r + "(ex:x1, ex:x2, -, [ex:a=\"v\", prov:location=\"here\"])");
        }
    }

    public void testStartEnd() {
        for (String r : List.of("wasStartedBy", "wasEndedBy")) {
            assertSameStatements(
                    r + "(ex:a1)",
                    r + "(ex:a1, ex:e1, -, -)",
                    r + "(ex:a1, -, ex:a2, -)",
                    r + "(ex:id; ex:a1, ex:e1, ex:a2, 2011-11-16T16:05:00)",
                    r + "(ex:a1, ex:e1, ex:a2, -, [prov:role='ex:r', ex:a=2])");
        }
    }

    public void testBinaryRelations() {
        for (String r : List.of("wasInformedBy", "wasAttributedTo", "wasInfluencedBy")) {
            assertSameStatements(
                    r + "(ex:x1, ex:x2)",
                    r + "(ex:id; ex:x1, ex:x2)",
                    r + "(-; ex:x1, ex:x2, [ex:a=\"v\"])",
                    r + "(ex:x1, ex:x2, [prov:type='ex:t'])");
        }
    }

    public void testAssociationAndDelegation() {
        assertSameStatements(
                "wasAssociatedWith(ex:a1, ex:ag1, -)",
                "wasAssociatedWith(ex:a1, -, ex:pl1)",
                "wasAssociatedWith(ex:id; ex:a1, ex:ag1, ex:pl1, [prov:role='ex:r'])",
                "actedOnBehalfOf(ex:ag2, ex:ag1)",
                "actedOnBehalfOf(ex:ag2, ex:ag1, -)",
                "actedOnBehalfOf(ex:id; ex:ag2, ex:ag1, ex:a1, [prov:type='ex:t'])");
    }

    public void testDerivations() {
        assertSameStatements(
                "wasDerivedFrom(ex:e2, ex:e1)",
                "wasDerivedFrom(ex:e2, ex:e1, -, -, -)",
                "wasDerivedFrom(ex:e2, ex:e1, ex:a, ex:g, ex:u)",
                "wasDerivedFrom(ex:id; ex:e2, ex:e1, -, ex:g, -, [prov:type='prov:Revision'])");
    }

    public void testAlternatesAndMembership() {
        assertSameStatements(
                "alternateOf(ex:e1, ex:e2)",
                "specializationOf(ex:e1, ex:e2)",
                "hadMember(ex:c, ex:e1)");
    }

    public void testDictionaries() {
        assertSameStatements(
                "prov:derivedByInsertionFrom(ex:d2, ex:d1, {(\"k1\" %% xsd:string, ex:e1)})",
                "prov:derivedByInsertionFrom(ex:id; ex:d2, ex:d1, {('ex:k1', ex:e1), (\"2\" %% xsd:int, ex:e2), (\"k3\"@en, ex:e3)}, [ex:a=1])",
                "prov:derivedByRemovalFrom(ex:d2, ex:d1, {})",
                "prov:derivedByRemovalFrom(ex:d2, ex:d1, {\"k1\" %% xsd:string, 'ex:k2'})",
                "prov:hadDictionaryMember(ex:d, ex:e1, \"k1\" %% xsd:string)");
    }

    public void testLiterals() {
        assertSameStatements(
                "entity(ex:e, [ex:a=\"plain\"])",
                "entity(ex:e, [ex:a=\"\"])",
                "entity(ex:e, [ex:a=\"escaped \\\" \\\\ \\n \\t\"])",
                "entity(ex:e, [ex:a=\"\"\"long \"quoted\" and\nmultiline\"\"\"])",
                "entity(ex:e, [ex:a=\"\"\"long\"\"\"@en])",
                "entity(ex:e, [ex:a=\"bonjour\"@fr, ex:b=\"hi\"@en-GB])",
                "entity(ex:e, [ex:a=12, ex:b=-3, ex:c=0])",
                "entity(ex:e, [ex:a='ex:foo', ex:b='ex2:bar'])",
                "entity(ex:e, [ex:a=\"ex:foo\" %% prov:QUALIFIED_NAME])",
                "entity(ex:e, [ex:a=\"10\" %% xsd:int, ex:b=\"1.5\" %% xsd:double, ex:c=\"true\" %% xsd:boolean])",
                "entity(ex:e, [ex:a=\"2011-11-16T16:05:00\" %% xsd:dateTime, ex:b=\"http://x.org/\" %% xsd:anyURI])",
                "entity(ex:e, [ex:a=\"hi\"@en %% prov:InternationalizedString])",
                "entity(ex:e, [prov:value=\"v\", prov:type=\"t\", prov:label=\"l\", prov:location=\"l\"])",
                "entity(ex:e, [ex:a=\"v1\", ex:a=\"v2\", ex:a=3])");
    }

    public void testQualifiedNames() {
        assertSameStatements(
                "entity(ex:e-1.2_3)",
                "entity(ex:1)",
                "entity(ex:e/f/g)",
                "entity(ex:a%20b)",
                "entity(ex:a\\=b\\(c\\))",
                "entity(ex:@~&+*?#$!)",
                "entity(ex:)",
                "entity(ex:été)",
                "entity(ex:e, [ex:x='ex:a.b'])",
                "entity(ex:a.b)");
    }

    public void testNamespaces() {
        assertSame("document\nendDocument\n");
        assertSame("document\ndefault <http://example.org/>\nentity(e1)\nendDocument\n");
        assertSame("document\ndefault <http://example.org/>\nprefix ex2 <http://example2.org/>\nentity(e1)\nentity(ex2:e2)\nendDocument\n");
        assertSame("document\nprefix ex <http://example.org/>\nprefix ex.org <http://example.org/2/>\nprefix e-x_1 <http://example.org/3/>\n"
                + "entity(ex.org:e1)\nentity(e-x_1:e2)\nendDocument\n");
        // names that are also prefixes or integers, in the default namespace
        assertSame("document\ndefault <http://example.org/>\nentity(ex)\nentity(12)\nentity(e1, [label=\"x\", ex=2])\nendDocument\n");
    }

    public void testBundles() {
        assertSame("document\nprefix ex <http://example.org/>\n"
                + "bundle ex:b1\n entity(ex:e1)\nendBundle\n"
                + "bundle ex:b2\n prefix ex2 <http://example2.org/>\n entity(ex2:e1)\nendBundle\n"
                + "endDocument\n");
        assertSame("document\nprefix ex <http://example.org/>\nentity(ex:e0)\n"
                + "bundle ex:b1\n default <http://example3.org/>\n prefix ex <http://example4.org/>\n entity(e1)\n entity(ex:e2)\nendBundle\n"
                + "endDocument\n");
        // the bundle identifier is resolved in the bundle's namespace
        assertSame("document\nbundle b:b1\n prefix b <http://example.org/bundles/>\n entity(b:e1)\nendBundle\nendDocument\n");
    }

    public void testCommentsAndLayout() {
        assertSame("document // a comment\n"
                + "/* a\n multiline comment */\n"
                + "prefix ex <http://example.org/>\n"
                + "entity(ex:e1) // entity(ex:e2)\n"
                + "\t entity( ex:e3 ,[ ex:a = \"v\" ] )\n"
                + "/**/ endDocument");
    }

    public void testExtensions() {
        // extensions outside the provext namespace are ignored by both parsers;
        // the old parser rejects an extension right after the namespace declarations (see testDivergenceQnameDisabledFlag)
        assertSameStatements(
                "entity(ex:e0) ex:foo(ex:a1, ex:a2)",
                "entity(ex:e0) ex:foo(ex:id; ex:a1, -, \"s\", -12, 2011-11-16T16:05:00, ex:bar(ex:x), {ex:y, ex:z}, (ex:y, -3), [ex:a=1])");
        // the qualified forms of hadMember, specializationOf and alternateOf, in the provext namespace
        assertSameStatements(
                "entity(ex:e0) provext:hadMember(ex:m; ex:c, ex:e)",
                "entity(ex:e0) provext:hadMember(-; ex:c, ex:e, [ex:x=1, prov:type='ex:t'])",
                "entity(ex:e0) provext:specializationOf(ex:s; ex:e1, ex:e2, [ex:x=\"v\"])",
                "entity(ex:e0) provext:specializationOf(-; ex:e1, ex:e2)",
                "entity(ex:e0) provext:alternateOf(ex:alt; ex:e1, ex:e2, [ex:foo='ex:a'])");
    }

    public void testSyntaxErrorsRejectedByBoth() {
        for (String provn : List.of(
                doc("entity(ex:e1"),
                doc("entity()"),
                doc("entity(ex:e1, [ex:a])"),
                doc("entity(ex:e1, [ex:a=])"),
                doc("activity(ex:a1, -)"),
                doc("unknown(ex:a1)"),
                "document\nprefix 12 <http://example.org/>\nendDocument\n",
                "document\nentity(ex:e1)\n",
                "prefix ex <http://example.org/>\nentity(ex:e1)\nendDocument\n")) {
            Outcome o = comparison.compareString(provn);
            assertEquals(o.toString(), Verdict.BOTH_REJECT, o.verdict);
        }
    }

    /* ======================================================================
       Divergences: where the new parser purposely departs from the old one
       ====================================================================== */

    Document read(String provn) {
        return reader.readDocumentFromString(provn);
    }

    Statement single(String statement) {
        List<StatementOrBundle> l = read(doc(statement)).getStatementOrBundle();
        assertEquals(1, l.size());
        return (Statement) l.get(0);
    }

    QualifiedName ex(String local) {
        return pFactory.newQualifiedName("http://example.org/", local, "ex");
    }

    /**
     * The marker '-' where PROV-N requires an identifier, as ProvToolbox writes for an argument that a statement lacks:
     * the old parser reports a syntax error, but recovers the same document as the new parser, with null for the argument.
     */
    public void testMarkerForRequiredIdentifier() {
        for (String statement : List.of("wasGeneratedBy(ex:gen200;-,ex:a,-)",
                                        "hadMember(ex:e2, -)",
                                        "wasAssociatedWith(ex:assoc2;-,ex:ag1,-)",
                                        "wasDerivedFrom(-, ex:e1)",
                                        "actedOnBehalfOf(ex:ag2, -, ex:a)",
                                        "used(-; -, ex:e1, -)")) {
            Outcome o = comparison.compareString(doc("entity(ex:e0)", statement));
            assertEquals(o.toString(), Verdict.OLD_RECOVERS, o.verdict);
        }
        WasGeneratedBy wgb = (WasGeneratedBy) single("wasGeneratedBy(ex:gen200;-,ex:a,-)");
        assertEquals(ex("gen200"), wgb.getId());
        assertNull(wgb.getEntity());
        assertEquals(ex("a"), wgb.getActivity());
        assertNull(wgb.getTime());
    }

    /** The old parser ignores what follows endDocument; the new parser requires the end of the input. */
    public void testDivergenceTextAfterEndDocument() {
        Outcome o = comparison.compareString(doc("entity(ex:e1)") + "entity(ex:e2)\n");
        assertEquals(o.toString(), Verdict.NEW_REJECTS, o.verdict);
    }

    /** The old parser drops these statements; the new one reads them as derivations of the corresponding type (PROV-DM 5.2). */
    public void testDivergenceRevisionQuotationPrimarySource() {
        Map<String, QualifiedName> types = Map.of("wasRevisionOf", name.PROV_REVISION,
                                                  "wasQuotedFrom", name.PROV_QUOTATION,
                                                  "hadPrimarySource", name.PROV_PRIMARY_SOURCE);
        for (Map.Entry<String, QualifiedName> e : types.entrySet()) {
            String typeLiteral = "'prov:" + e.getValue().getLocalPart() + "'";

            Outcome o = comparison.compareString(doc(e.getKey() + "(ex:id; ex:e2, ex:e1, ex:a, ex:g, ex:u, [ex:x=1])"));
            assertEquals(o.toString(), Verdict.DIFFERENT, o.verdict);
            assertTrue(o.oldDocument.getStatementOrBundle().isEmpty());

            // the same as the derivation with that type
            Document expected = read(doc("wasDerivedFrom(ex:id; ex:e2, ex:e1, ex:a, ex:g, ex:u, [ex:x=1, prov:type=" + typeLiteral + "])"));
            assertNull(comparison.difference(expected, o.newDocument));

            WasDerivedFrom d = (WasDerivedFrom) single(e.getKey() + "(ex:e2, ex:e1)");
            assertEquals(ex("e2"), d.getGeneratedEntity());
            assertEquals(ex("e1"), d.getUsedEntity());
            assertEquals(1, d.getType().size());
            assertEquals(e.getValue(), d.getType().get(0).getValue());
        }
    }

    /** wasAssociatedWith without agent and plan: the old parser fails with a ClassCastException. */
    public void testDivergenceAssociationWithoutAgentAndPlan() {
        Outcome o = comparison.compareString(doc("wasAssociatedWith(ex:a1)"));
        assertEquals(o.toString(), Verdict.OLD_REJECTS, o.verdict);
        WasAssociatedWith waw = (WasAssociatedWith) single("wasAssociatedWith(ex:id; ex:a1, [ex:x=1])");
        assertEquals(ex("id"), waw.getId());
        assertEquals(ex("a1"), waw.getActivity());
        assertNull(waw.getAgent());
        assertNull(waw.getPlan());
    }

    /** The provext statements (also compared in {@link #testExtensions()}). */
    public void testProvextStatements() {
        QualifiedHadMember m = (QualifiedHadMember) single("provext:hadMember(ex:m; ex:c, ex:e, [ex:x=1])");
        assertEquals(ex("m"), m.getId());
        assertEquals(ex("c"), m.getCollection());
        assertEquals(List.of(ex("e")), m.getEntity());
        assertEquals(1, m.getOther().size());

        QualifiedSpecializationOf s = (QualifiedSpecializationOf) single("provext:specializationOf(-; ex:e1, ex:e2)");
        assertNull(s.getId());
        assertEquals(ex("e1"), s.getSpecificEntity());
        assertEquals(ex("e2"), s.getGeneralEntity());

        QualifiedAlternateOf a = (QualifiedAlternateOf) single("provext:alternateOf(ex:alt; ex:e1, ex:e2, [ex:foo='ex:a'])");
        assertEquals(ex("alt"), a.getId());
        assertEquals(ex("e1"), a.getAlternate1());
        assertEquals(ex("e2"), a.getAlternate2());
        assertEquals(1, a.getOther().size());
        assertEquals(ex("a"), a.getOther().get(0).getValue());

        try {
            single("provext:hadMember(ex:m; ex:c)");
            fail("provext:hadMember needs two arguments");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Keys that are plain strings or integers: the old parser fails with a ClassCastException. */
    public void testDivergenceUntypedDictionaryKeys() {
        Outcome o = comparison.compareString(doc("prov:derivedByInsertionFrom(ex:d2, ex:d1, {(\"k1\", ex:e1)})"));
        assertEquals(o.toString(), Verdict.OLD_REJECTS, o.verdict);

        DerivedByInsertionFrom d = (DerivedByInsertionFrom) single("prov:derivedByInsertionFrom(ex:d2, ex:d1, {(\"k1\", ex:e1), (2, ex:e2)})");
        assertEquals("k1", d.getKeyEntityPair().get(0).getKey().getValue());
        assertEquals(name.XSD_STRING, d.getKeyEntityPair().get(0).getKey().getType());
        assertEquals(name.XSD_INT, d.getKeyEntityPair().get(1).getKey().getType());
    }

    /**
     * Where the old lexer, which the parser steers with the static flag 'qnameDisabled', has read a qualified name ahead
     * while the flag was set, it fails: for instance, for an extension right after the namespace declarations, since the
     * parser looks ahead for another declaration.
     */
    public void testDivergenceQnameDisabledFlag() {
        for (String provn : List.of(doc("ex:foo(ex:a1, ex:a2)", "entity(ex:e1)"),
                                    doc("provext:alternateOf(ex:alt1; ex:e1, ex:e2)"),
                                    doc("provext:hadMember(ex:m; ex:c, ex:e)"))) {
            Outcome o = comparison.compareString(provn);
            assertEquals(o.toString(), Verdict.OLD_REJECTS, o.verdict);
        }
        assertEquals(1, read(doc("provext:alternateOf(ex:alt1; ex:e1, ex:e2)")).getStatementOrBundle().size());
    }

    /** Typed literals whose datatype is an IRI (not valid PROV-N, where a datatype is a qualified name): the old parser fails with a ClassCastException, the new one reports a syntax error. */
    public void testDivergenceIriDatatype() {
        Outcome o = comparison.compareString(doc("entity(ex:e, [ex:a=\"v\" %% <http://example.org/t>])"));
        assertEquals(o.toString(), Verdict.BOTH_REJECT, o.verdict);
        assertTrue(o.newFailure, o.newFailure.startsWith("ProvNSyntaxException"));
    }

    /* ======================================================================
       The new parser
       ====================================================================== */

    public void testSyntaxErrorsAreReported() {
        try {
            read("document\nprefix ex <http://example.org/>\nentity(ex:e1\nentity(ex:e2,)\nendDocument\n");
            fail("syntax errors expected");
        } catch (ProvNSyntaxException e) {
            assertFalse(e.getErrors().isEmpty());
            assertTrue(e.getErrors().toString(), e.getErrors().get(0).startsWith("line 4:"));
        }
    }

    public void testInvalidPrefixes() {
        for (String prefix : List.of("ex:", "ex:a", "12", "_ex", "ex/a", "ex.", "-ex")) {
            try {
                read("document\nprefix " + prefix + " <http://example.org/>\nendDocument\n");
                fail("invalid prefix " + prefix);
            } catch (ProvNSyntaxException expected) {
            }
        }
    }

    /** Characters beyond the Basic Multilingual Plane, which PROV-N allows in names, and the ANTLR 3 lexer rejects. */
    public void testSupplementaryCharacters() {
        Entity e = (Entity) single("entity(ex:𝔸bc, [ex:a=\"😀\"])");
        assertEquals("𝔸bc", e.getId().getLocalPart());
        assertEquals("😀", ((LangString) e.getOther().get(0).getValue()).getValue());
    }

    /** A long string may have a datatype, like a short one. */
    public void testLongStringWithDatatype() {
        Entity e1 = (Entity) single("entity(ex:e, [ex:a=\"\"\"12\"\"\" %% xsd:int])");
        Entity e2 = (Entity) single("entity(ex:e, [ex:a=\"12\" %% xsd:int])");
        assertNull(comparison.difference(read(doc("entity(ex:e, [ex:a=\"\"\"12\"\"\" %% xsd:int])")), read(doc("entity(ex:e, [ex:a=\"12\" %% xsd:int])"))));
        assertEquals(e2.getOther().get(0).getType(), e1.getOther().get(0).getType());
    }

    /** No state is shared between parsers, which can then run concurrently. */
    public void testConcurrentParsing() throws Exception {
        List<String> inputs = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            inputs.add(doc("entity(ex:e" + i + ", [ex:a=" + i + ", ex:b='ex:v" + i + "', ex:c=\"s" + i + "\"@en])",
                           "prov:derivedByInsertionFrom(ex:d" + i + ", ex:d, {(\"" + i + "\" %% xsd:int, ex:e" + i + ")})",
                           "wasGeneratedBy(ex:e" + i + ", ex:a" + i + ", 2011-11-16T16:05:00)"));
        }
        List<Document> sequential = new ArrayList<>();
        for (String s : inputs) sequential.add(read(s));

        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Future<Document>> futures = new ArrayList<>();
            for (int round = 0; round < 4; round++) {
                for (String s : inputs) futures.add(pool.submit(() -> read(s)));
            }
            for (int i = 0; i < futures.size(); i++) {
                assertNull(comparison.difference(sequential.get(i % inputs.size()), futures.get(i).get()));
            }
        } finally {
            pool.shutdown();
        }
    }
}
