package org.openprovenance.prov.notation.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.QualifiedNameException;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.notation.ProvNReader;
import org.openprovenance.prov.notation.ProvNSyntaxException;
import org.openprovenance.prov.notation.Utility;

/**
 * The ANTLR 4 parser (ProvN.g4, read via ProvNReader), on corpora of PROV-N files and on snippets covering the
 * constructs of the grammar.
 * <p>
 * Up to its removal, the ANTLR 3 parser was compared with it here: they constructed the same documents, but for the
 * deliberate differences described in ProvN.g4 and in ProvDeserialiser, which the tests still check. See the git
 * history for the comparison.
 */
public class ProvNReaderTest extends TestCase {

    static final ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    static final Name name = pFactory.getName();

    final DocumentComparison comparison = new DocumentComparison(pFactory);
    final Utility u = new Utility();
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
     * All the PROV-N files under a directory, in this checkout only: hidden directories (.git, .claude, .idea, ...),
     * nested checkouts (git worktrees, which hold other branches), and build outputs are skipped.
     */
    static List<Path> provnFiles(Path root) throws IOException {
        List<Path> files = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (dir.equals(root)) return FileVisitResult.CONTINUE;
                String name = dir.getFileName().toString();
                if (name.startsWith(".") || name.equals("target") || name.equals("node_modules")
                        || Files.exists(dir.resolve(".git"))) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String s = file.getFileName().toString();
                if (s.endsWith(".provn") || s.endsWith(".pn") || s.endsWith(".asn") || s.endsWith(".prov-asn")) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        files.sort(null);
        return files;
    }

    /** The files that the parser rejects, with what it reports. */
    Map<String, String> rejected(List<Path> files) {
        Map<String, String> rejected = new TreeMap<>();
        for (Path p : files) {
            try {
                reader.readDocument(p.toString());
            } catch (RuntimeException e) {
                rejected.put(p.toString(), DocumentComparison.describe(e));
            }
        }
        return rejected;
    }

    /** The PROV-N files of this module: those that cannot be read are test cases for this, or date from early drafts. */
    public void testModuleResources() throws IOException {
        Path root = Path.of("src/test/resources");
        List<Path> files = provnFiles(root);
        Set<String> rejected = new TreeSet<>();
        for (String file : rejected(files).keySet()) rejected.add(root.resolve("prov").relativize(Path.of(file)).toString());
        assertEquals(new TreeSet<>(List.of("bundles2.provn", "container0.provn", "container1.provn", "container2.provn",
                                           "file-example3.asn", "paolo-test.pn", "w3c-publication1.prov-asn")), rejected);
        assertTrue(files.size() - rejected.size() >= 18);
    }

    /**
     * All the PROV-N files of the ProvToolbox repository, when this module is built in it. PROV-N shipped in src/main,
     * such as the templates that prov-template instantiates at build time, is read by the toolbox: the parser must accept it.
     */
    public void testRepositoryCorpus() throws IOException {
        Path root = Path.of("../..").toAbsolutePath().normalize();
        if (!Files.isDirectory(root.resolve("modules-core").resolve("prov-n"))) {
            System.out.println("ProvToolbox repository not found: skipping corpus");
            return;
        }
        List<String> unreadable = new ArrayList<>();
        Map<String, String> rejected = rejected(provnFiles(root));
        System.out.println("==== PROV-N files of " + root + " that the parser rejects: " + rejected.size());
        for (Map.Entry<String, String> e : rejected.entrySet()) {
            System.out.println("  " + e.getKey() + "\n    " + e.getValue());
            if (e.getKey().contains(File.separator + "src" + File.separator + "main" + File.separator)) unreadable.add(e.getKey());
        }
        assertTrue("PROV-N in src/main that the parser rejects:\n" + String.join("\n", unreadable), unreadable.isEmpty());
    }

    /* ======================================================================
       Snippets, covering the constructs of the grammar
       ====================================================================== */

    /** The parser accepts it, and reads back the same document from what is written for it. */
    void assertAccepted(String provn) {
        Document d = read(provn);
        String difference = comparison.difference(d, read(u.convertBeanToSyntaxTree(d, pFactory)));
        assertNull(provn + "\n" + difference, difference);
    }

    void assertAcceptedStatements(String... statements) {
        for (String s : statements) assertAccepted(doc(s));
    }

    /** The parser rejects it: what it reports. */
    String rejection(String provn) {
        try {
            read(provn);
        } catch (RuntimeException e) {
            return DocumentComparison.describe(e);
        }
        fail("expected to be rejected: " + provn);
        return null;
    }

    public void testEntitiesAndAgents() {
        assertAcceptedStatements(
                "entity(ex:e1)",
                "entity(ex:e1, [])",
                "entity(ex:e1, [ex:a=\"v\"])",
                "agent(ex:ag1)",
                "agent(ex:ag1, [prov:type='prov:Person', prov:label=\"Alice\"])",
                "entity(ex2:e1) entity(ex:e2) agent(ex:e3)");
    }

    public void testActivities() {
        assertAcceptedStatements(
                "activity(ex:a1)",
                "activity(ex:a1, -, -)",
                "activity(ex:a1, 2011-11-16T16:05:00, -)",
                "activity(ex:a1, -, 2011-11-16T16:05:00Z)",
                "activity(ex:a1, 2011-11-16T16:05:00.123, 2011-11-16T18:05:00+01:00, [ex:a=1])",
                "activity(ex:a1, 2011-11-16T16:05:00-05:00, 2011-11-16T16:05:00.5Z)");
    }

    public void testGenerationUsageInvalidation() {
        for (String r : List.of("wasGeneratedBy", "used", "wasInvalidatedBy")) {
            assertAcceptedStatements(
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
            assertAcceptedStatements(
                    r + "(ex:a1)",
                    r + "(ex:a1, ex:e1, -, -)",
                    r + "(ex:a1, -, ex:a2, -)",
                    r + "(ex:id; ex:a1, ex:e1, ex:a2, 2011-11-16T16:05:00)",
                    r + "(ex:a1, ex:e1, ex:a2, -, [prov:role='ex:r', ex:a=2])");
        }
    }

    public void testBinaryRelations() {
        for (String r : List.of("wasInformedBy", "wasAttributedTo", "wasInfluencedBy")) {
            assertAcceptedStatements(
                    r + "(ex:x1, ex:x2)",
                    r + "(ex:id; ex:x1, ex:x2)",
                    r + "(-; ex:x1, ex:x2, [ex:a=\"v\"])",
                    r + "(ex:x1, ex:x2, [prov:type='ex:t'])");
        }
    }

    public void testAssociationAndDelegation() {
        assertAcceptedStatements(
                "wasAssociatedWith(ex:a1, ex:ag1, -)",
                "wasAssociatedWith(ex:a1, -, ex:pl1)",
                "wasAssociatedWith(ex:id; ex:a1, ex:ag1, ex:pl1, [prov:role='ex:r'])",
                "actedOnBehalfOf(ex:ag2, ex:ag1)",
                "actedOnBehalfOf(ex:ag2, ex:ag1, -)",
                "actedOnBehalfOf(ex:id; ex:ag2, ex:ag1, ex:a1, [prov:type='ex:t'])");
    }

    public void testDerivations() {
        assertAcceptedStatements(
                "wasDerivedFrom(ex:e2, ex:e1)",
                "wasDerivedFrom(ex:e2, ex:e1, -, -, -)",
                "wasDerivedFrom(ex:e2, ex:e1, ex:a, ex:g, ex:u)",
                "wasDerivedFrom(ex:id; ex:e2, ex:e1, -, ex:g, -, [prov:type='prov:Revision'])");
    }

    public void testAlternatesAndMembership() {
        assertAcceptedStatements(
                "alternateOf(ex:e1, ex:e2)",
                "specializationOf(ex:e1, ex:e2)",
                "hadMember(ex:c, ex:e1)");
    }

    public void testDictionaries() {
        assertAcceptedStatements(
                "prov:derivedByInsertionFrom(ex:d2, ex:d1, {(\"k1\" %% xsd:string, ex:e1)})",
                "prov:derivedByInsertionFrom(ex:id; ex:d2, ex:d1, {('ex:k1', ex:e1), (\"2\" %% xsd:int, ex:e2), (\"k3\"@en, ex:e3)}, [ex:a=1])",
                "prov:derivedByRemovalFrom(ex:d2, ex:d1, {})",
                "prov:derivedByRemovalFrom(ex:d2, ex:d1, {\"k1\" %% xsd:string, 'ex:k2'})",
                "prov:hadDictionaryMember(ex:d, ex:e1, \"k1\" %% xsd:string)");
    }

    public void testLiterals() {
        assertAcceptedStatements(
                "entity(ex:e, [ex:a=\"plain\"])",
                "entity(ex:e, [ex:a=\"\"])",
                "entity(ex:e, [ex:a=\"escaped \\\" \\\\ \\n \\t \\b \\r \\f \\'\"])",
                "entity(ex:e, [ex:a=\"\"\"long \"quoted\" and\nmultiline\"\"\"])",
                "entity(ex:e, [ex:a=\"\"\"multiline\nin \\\"french\\\"\"\"\"@fr])",
                "entity(ex:e, [ex:a=\"\"\"multiline\ntyped\"\"\" %% ex:t])",
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
        assertAcceptedStatements(
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
        assertAccepted("document\nendDocument\n");
        assertAccepted("document\ndefault <http://example.org/>\nentity(e1)\nendDocument\n");
        assertAccepted("document\ndefault <http://example.org/>\nprefix ex2 <http://example2.org/>\nentity(e1)\nentity(ex2:e2)\nendDocument\n");
        assertAccepted("document\nprefix ex <http://example.org/>\nprefix ex.org <http://example.org/2/>\nprefix e-x_1 <http://example.org/3/>\n"
                + "entity(ex.org:e1)\nentity(e-x_1:e2)\nendDocument\n");
        // names that are also prefixes or integers, in the default namespace
        assertAccepted("document\ndefault <http://example.org/>\nentity(ex)\nentity(12)\nentity(e1, [label=\"x\", ex=2])\nendDocument\n");
    }

    public void testBundles() {
        assertAccepted("document\nprefix ex <http://example.org/>\n"
                + "bundle ex:b1\n entity(ex:e1)\nendBundle\n"
                + "bundle ex:b2\n prefix ex2 <http://example2.org/>\n entity(ex2:e1)\nendBundle\n"
                + "endDocument\n");
        assertAccepted("document\nprefix ex <http://example.org/>\nentity(ex:e0)\n"
                + "bundle ex:b1\n default <http://example3.org/>\n prefix ex <http://example4.org/>\n entity(e1)\n entity(ex:e2)\nendBundle\n"
                + "endDocument\n");
        // the bundle identifier is resolved in the bundle's namespace
        assertAccepted("document\nbundle b:b1\n prefix b <http://example.org/bundles/>\n entity(b:e1)\nendBundle\nendDocument\n");
    }

    public void testCommentsAndLayout() {
        assertAccepted("document // a comment\n"
                + "/* a\n multiline comment */\n"
                + "prefix ex <http://example.org/>\n"
                + "entity(ex:e1) // entity(ex:e2)\n"
                + "\t entity( ex:e3 ,[ ex:a = \"v\" ] )\n"
                + "/**/ endDocument");
    }

    public void testExtensions() {
        // extensions outside the provext namespace are ignored by both parsers;
        // the old parser rejects an extension right after the namespace declarations (see testDivergenceQnameDisabledFlag)
        assertAcceptedStatements(
                "entity(ex:e0) ex:foo(ex:a1, ex:a2)",
                "entity(ex:e0) ex:foo(ex:id; ex:a1, -, \"s\", -12, 2011-11-16T16:05:00, ex:bar(ex:x), {ex:y, ex:z}, (ex:y, -3), [ex:a=1])");
        // the qualified forms of hadMember, specializationOf and alternateOf, in the provext namespace
        assertAcceptedStatements(
                "entity(ex:e0) provext:hadMember(ex:m; ex:c, ex:e)",
                "entity(ex:e0) provext:hadMember(-; ex:c, ex:e, [ex:x=1, prov:type='ex:t'])",
                "entity(ex:e0) provext:specializationOf(ex:s; ex:e1, ex:e2, [ex:x=\"v\"])",
                "entity(ex:e0) provext:specializationOf(-; ex:e1, ex:e2)",
                "entity(ex:e0) provext:alternateOf(ex:alt; ex:e1, ex:e2, [ex:foo='ex:a'])");
    }

    public void testSyntaxErrorsRejected() {
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
            rejection(provn);
        }
    }

    /* ======================================================================
       Where the parser purposely departs from the ANTLR 3 one, up to 2.3.0
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
     * The marker '-' where PROV-N requires an identifier, read as an absent argument (null). Statements sharing an
     * identifier may be merged into a valid one, although each lacks required arguments (see Markers, in ProvN.g4).
     * ProvToolbox writes the marker for an argument that a statement lacks.
     */
    public void testMarkerForRequiredIdentifier() {
        for (String statement : List.of("wasGeneratedBy(ex:gen200;-,ex:a,-)",
                                        "hadMember(ex:e2, -)",
                                        "wasAssociatedWith(ex:assoc2;-,ex:ag1,-)",
                                        "wasDerivedFrom(-, ex:e1)",
                                        "actedOnBehalfOf(ex:ag2, -, ex:a)",
                                        "used(-; -, ex:e1, -)")) {
            assertEquals(2, read(doc("entity(ex:e0)", statement)).getStatementOrBundle().size());
        }
        // two parts of one delegation, each lacking a required argument, which their merge has
        Document d = read(doc("actedOnBehalfOf(ex:del1; ex:ag2, -, ex:a2)", "actedOnBehalfOf(ex:del1; ex:ag2, ex:ag1, -)"));
        ActedOnBehalfOf part1 = (ActedOnBehalfOf) d.getStatementOrBundle().get(0);
        ActedOnBehalfOf part2 = (ActedOnBehalfOf) d.getStatementOrBundle().get(1);
        assertEquals(part1.getId(), part2.getId());
        assertNull(part1.getResponsible());
        assertEquals(ex("a2"), part1.getActivity());
        assertEquals(ex("ag1"), part2.getResponsible());
        assertNull(part2.getActivity());

        WasGeneratedBy wgb = (WasGeneratedBy) single("wasGeneratedBy(ex:gen200;-,ex:a,-)");
        assertEquals(ex("gen200"), wgb.getId());
        assertNull(wgb.getEntity());
        assertEquals(ex("a"), wgb.getActivity());
        assertNull(wgb.getTime());
    }

    /**
     * Only whitespace and comments may follow the document; the ANTLR 3 parser ignored what followed (see {@link #testSeveralDocuments()} for streams of documents).
     */
    public void testTextAfterEndDocument() {
        for (String trailing : List.of("entity(ex:e2)\n", "#entity(ex:e2)\n", doc("entity(ex:e2)"))) {
            rejection(doc("entity(ex:e1)") + trailing);
        }
        // comments and whitespace are fine
        assertAccepted(doc("entity(ex:e1)") + "\n// entity(ex:e2)\n/* entity(ex:e3) */\n\n");
    }

    /**
     * wasRevisionOf, wasQuotedFrom and hadPrimarySource, of early drafts of PROV-N, are not in the W3C Recommendation:
     * there, they are derivations with prov:type prov:Revision, prov:Quotation, prov:PrimarySource. The ANTLR 3 parser
     * accepted them as keywords, and dropped the statements. Here, as for the Recommendation, such a statement
     * is an extensibility expression, named by an unprefixed qualified name: ignored, as any extension it does not
     * know, if there is a default namespace, and rejected, since its name cannot be resolved, if there is none.
     */
    public void testRevisionQuotationPrimarySource() {
        for (String keyword : List.of("wasRevisionOf", "wasQuotedFrom", "hadPrimarySource")) {
            String statement = keyword + "(ex:id; ex:e2, ex:e1, ex:a, ex:g, ex:u, [ex:x=1])";

            Document d = read("document\ndefault <http://example.org/default/>\nprefix ex <http://example.org/>\n"
                              + "entity(ex:e0)\n" + statement + "\nendDocument\n");
            assertEquals(1, d.getStatementOrBundle().size());

            String failure = rejection(doc("entity(ex:e0)", statement));
            assertTrue(failure, failure.contains(keyword));
        }
    }

    /** wasAssociatedWith without agent and plan: the ANTLR 3 parser failed with a ClassCastException. */
    public void testAssociationWithoutAgentAndPlan() {
        assertEquals(1, read(doc("wasAssociatedWith(ex:a1)")).getStatementOrBundle().size());
        WasAssociatedWith waw = (WasAssociatedWith) single("wasAssociatedWith(ex:id; ex:a1, [ex:x=1])");
        assertEquals(ex("id"), waw.getId());
        assertEquals(ex("a1"), waw.getActivity());
        assertNull(waw.getAgent());
        assertNull(waw.getPlan());
    }

    /** The provext statements (see also {@link #testExtensions()}). */
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

    /** Keys that are plain strings or integers: the ANTLR 3 parser failed with a ClassCastException. */
    public void testUntypedDictionaryKeys() {
        DerivedByInsertionFrom d = (DerivedByInsertionFrom) single("prov:derivedByInsertionFrom(ex:d2, ex:d1, {(\"k1\", ex:e1), (2, ex:e2)})");
        assertEquals("k1", d.getKeyEntityPair().get(0).getKey().getValue());
        assertEquals(name.XSD_STRING, d.getKeyEntityPair().get(0).getKey().getType());
        assertEquals(name.XSD_INT, d.getKeyEntityPair().get(1).getKey().getType());
    }

    /**
     * The ANTLR 3 lexer, which the parser steered with a static flag, failed where it had read a qualified name ahead
     * while the flag was set: for instance, for an extension right after the namespace declarations, since the parser
     * looks ahead for another declaration.
     */
    public void testExtensionAfterNamespaceDeclarations() {
        for (String provn : List.of(doc("ex:foo(ex:a1, ex:a2)", "entity(ex:e1)"),
                                    doc("provext:alternateOf(ex:alt1; ex:e1, ex:e2)"),
                                    doc("provext:hadMember(ex:m; ex:c, ex:e)"))) {
            read(provn);
        }
        assertEquals(1, read(doc("provext:alternateOf(ex:alt1; ex:e1, ex:e2)")).getStatementOrBundle().size());
    }

    /** Typed literals whose datatype is an IRI (not valid PROV-N, where a datatype is a qualified name): a syntax error. */
    public void testIriDatatype() {
        String failure = rejection(doc("entity(ex:e, [ex:a=\"v\" %% <http://example.org/t>])"));
        assertTrue(failure, failure.startsWith("ProvNSyntaxException"));
    }

    /* ======================================================================
       Other behaviour of the parser
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

    /** A stream of documents: readDocuments reads them all, readDocument rejects more than one. */
    public void testSeveralDocuments() {
        String two = "document\nprefix ex <http://example.org/>\nentity(ex:e1)\nendDocument\n"
                + "// the second one\n"
                + "document\nprefix ex <http://example2.org/>\nentity(ex:e2)\n"
                + "bundle ex:b\n entity(ex:e3)\nendBundle\nendDocument\n";
        List<Document> docs = reader.readDocumentsFromString(two);
        assertEquals(2, docs.size());
        // each document as if read alone
        assertNull(comparison.difference(read("document\nprefix ex <http://example.org/>\nentity(ex:e1)\nendDocument\n"), docs.get(0)));
        // prefixes are declared per document: ex is bound anew in the second one
        Entity e2 = (Entity) docs.get(1).getStatementOrBundle().get(0);
        assertEquals("http://example2.org/", e2.getId().getNamespaceURI());
        assertEquals("http://example2.org/", docs.get(1).getNamespace().getPrefixes().get("ex"));

        // a prefix of the first document is not declared in the second
        try {
            reader.readDocumentsFromString("document\nprefix ex <http://example.org/>\nendDocument\ndocument\nentity(ex:e1)\nendDocument\n");
            fail("ex is not declared in the second document");
        } catch (QualifiedNameException expected) {
        }

        assertTrue(reader.readDocumentsFromString("").isEmpty());
        assertTrue(reader.readDocumentsFromString("// no document\n").isEmpty());
        assertEquals(1, reader.readDocumentsFromString(doc("entity(ex:e1)")).size());

        try {
            read(two);
            fail("readDocument reads a single document");
        } catch (ProvNSyntaxException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("use readDocuments"));
        }
        try {
            reader.readDocumentsFromString(two + "entity(ex:e4)\n");
            fail("statements outside a document");
        } catch (ProvNSyntaxException expected) {
        }
    }

    /** Characters beyond the Basic Multilingual Plane, which PROV-N allows in names, and the ANTLR 3 lexer rejects. */
    public void testSupplementaryCharacters() {
        Entity e = (Entity) single("entity(ex:𝔸bc, [ex:a=\"😀\"])");
        assertEquals("𝔸bc", e.getId().getLocalPart());
        assertEquals("😀", ((LangString) e.getOther().get(0).getValue()).getValue());
    }

    /** Escape sequences denote the characters they stand for, in short and in long strings. */
    public void testEscapeSequences() {
        for (String quote : List.of("\"", "\"\"\"")) {
            Entity e = (Entity) single("entity(ex:e, [ex:a=" + quote + "a\\tb\\bc\\nd\\re\\ff\\\"g\\'h\\\\i\\\\n" + quote + "])");
            assertEquals("a\tb\bc\nd\re\ff\"g'h\\i\\n", ((LangString) e.getOther().get(0).getValue()).getValue());
        }
    }

    /** What is written is read back with the same value, whatever it holds. */
    public void testValuesRoundTrip() {
        for (String value : List.of("C:\\temp\\new", "\\", "\\\\", "a\\", "\"", "\"\"\"", "line1\nline2", "\nline2\n", "cr\rlf\r\n", "\ttab", "ends with \"")) {
            Entity e = pFactory.newEntity(ex("e"));
            e.getOther().add(pFactory.newOther(ex("a"), pFactory.newInternationalizedString(value), name.XSD_STRING));
            e.getOther().add(pFactory.newOther(ex("b"), pFactory.newInternationalizedString(value, "en"), name.PROV_LANG_STRING));
            e.getOther().add(pFactory.newOther(ex("c"), value, ex("t")));
            Document d = pFactory.newDocument();
            d.setNamespace(reader.readDocumentFromString(PROLOG + EPILOG).getNamespace());
            d.getStatementOrBundle().add(e);

            Document back = read(u.convertBeanToSyntaxTree(d, pFactory));
            String difference = comparison.difference(d, back);
            assertNull(value + "\n" + difference, difference);
            difference = comparison.difference(back, read(u.convertBeanToSyntaxTree(back, pFactory)));
            assertNull(value + "\n" + difference, difference);
        }
    }

    /** A value with a line break is written as valid PROV-N, with its language or its datatype. */
    public void testLineBreaksAreWrittenValidly() {
        Entity e = pFactory.newEntity(ex("e"));
        e.getOther().add(pFactory.newOther(ex("a"), pFactory.newInternationalizedString("l1\nl2", "fr"), name.PROV_LANG_STRING));
        e.getOther().add(pFactory.newOther(ex("b"), "l1\nl2", ex("t")));
        String a = ProvUtilities.valueToNotationString(e.getOther().get(0).getValue(), name.PROV_LANG_STRING);
        String b = ProvUtilities.valueToNotationString(e.getOther().get(1).getValue(), ex("t"));
        assertEquals("\"l1\\nl2\"@fr", a);
        assertEquals("\"\"\"l1\nl2\"\"\" %% ex:t", b);
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
