package org.openprovenance.prov.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class ProvFrameworkTest extends TestCase {
    public static final String EX_NS = "http://example.org/";
    public static final String EX_PREFIX = "ex";
    static final org.openprovenance.prov.vanilla.ProvUtilities util = new org.openprovenance.prov.vanilla.ProvUtilities();
    public static ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    public static Name name = pFactory.getName();
    protected DocumentEquality documentEquality;
    protected final DocumentComparator documentComparator;

    public ProvFrameworkTest() {
        this.documentEquality = new DocumentEquality(mergeDuplicateProperties(), System.out);
        this.documentComparator = new DocumentComparator(documentEquality);
    }

    public void updateNamespaces(Document doc) {
        Namespace ns = Namespace.gatherNamespaces(doc);
        doc.setNamespace(ns);
    }

    public String extension() {
        return "";
    }

    public void makeDocAndTest(Statement stment, String file) {
        makeDocAndTest(stment, file, null, true);
    }

    public void makeDocAndTest(Statement stment, String file, boolean check) {
        makeDocAndTest(stment, file, null, check);
    }

    public void makeDocAndTest(Statement stment, Statement[] opt, String file) {
        makeDocAndTest(stment, file, opt, true);
    }

    public void makeDocAndTest(Statement[] stment, Statement[] opt, String file) {
        makeDocAndTest(stment, file, opt, true);
    }

    public void makeDocAndTest(Statement stment, String file, Statement[] opt, boolean check) {
        makeDocAndTest(new Statement[]{stment}, file, opt, check);
    }

    public void makeDocAndTest(Statement[] stment, String file, Statement[] opt, boolean check) {
        makeDocAndTest(stment, null, file, opt, check);
    }

    public void makeDocAndTest(Statement[] stment, Bundle[] bundles, String file, Statement[] opt, boolean check) {
        Document doc = pFactory.newDocument();
        for (Statement statement : stment) {
            doc.getStatementOrBundle().add(statement);
        }
        if (bundles != null) {
            for (Bundle bundle : bundles) {
                doc.getStatementOrBundle().add(bundle);
            }
        }
        updateNamespaces(doc);

        if (bundles != null) {
            for (Bundle bundle : bundles) {
                bundle.getNamespace().setParent(doc.getNamespace());
            }
        }

        String file1 = (opt == null) ? file : file + "-S";
        compareDocAndFile(doc, file1, check);

        if ((opt != null) && doOptional(opt)) {
            String file2 = file + "-M";
            doc.getStatementOrBundle().addAll(Arrays.asList(opt));
            compareDocAndFile(doc, file2, check);
        }
    }

    public boolean doOptional(Statement[] opt) {
        return true;
    }

    public void compareDocAndFile(Document doc, String file, boolean check) {
        file = file + extension();
        writeDocument(doc, file);
        if (check) conditionalCheckSchema(file);
        Document doc3 = readDocument(file);
        documentComparator.compareDocuments(doc, doc3, check && checkTest(file));
    }

    public void conditionalCheckSchema(String file) {
        if (checkSchema(file))
            doCheckSchema1(file);
    }

    public boolean checkSchema(String name) {
        return true;
    }

    public void doCheckSchema1(String file) {}

    public Document readDocument(String file1) {
        try {
            return readDocumentFromFile(file1);
        } catch (IOException e) {
            throw new UncheckedTestException(e);
        }
    }

    public void writeDocument(Document doc, String file2) {
        Namespace.withThreadNamespace(doc.getNamespace());
        try {
            writeDocumentToFile(doc, file2);
        } catch (IOException e) {
            throw new UncheckedTestException(e);
        }
    }

    public boolean checkTest(String name) {
        return false;
    }

    public boolean mergeDuplicateProperties() {
        return false;
    }

    public Document readDocumentFromFile(String file) throws IOException {
        readingMessage(file);
        return null;

    }

    public void readingMessage(String file) {
    }

    public void writeDocumentToFile(Document doc, String file) throws IOException {
        writingMessage(file);
    }

    public void writingMessage(String file) {
        System.out.println("constructing " + file);
    }

    public QualifiedName q(String n) {
        return pFactory.newQualifiedName(EX_NS, n, EX_PREFIX);
    }

    public void addLabel(HasLabel hl) {
        hl.getLabel().add(pFactory.newInternationalizedString("hello"));
    }

    public void addLabels(HasLabel hl) {
        hl.getLabel().add(pFactory.newInternationalizedString("hello"));
        hl.getLabel().add(pFactory.newInternationalizedString("bye", "en"));
        hl.getLabel().add(pFactory.newInternationalizedString("bonjour", "fr"));
    }

    public void addTypes(HasType ht) {
        ht.getType().add(pFactory.newType("a", name.XSD_STRING));
        ht.getType().add(pFactory.newType(1, name.XSD_INT));
        ht.getType().add(pFactory.newType(1.0, name.XSD_FLOAT));
        ht.getType().add(pFactory.newType(true, name.XSD_BOOLEAN));

        ht.getType().add(pFactory.newType(pFactory.newQualifiedName(EX_NS, "abc", EX_PREFIX),
                name.PROV_QUALIFIED_NAME));
        ht.getType().add(pFactory.newType(pFactory.newTimeNow(),
                name.XSD_DATETIME));


        ht.getType().add(pFactory.newType(EX_NS + "hello",
                name.XSD_ANY_URI));
    }

    public void addLocations(HasLocation hl) {
        hl.getLocation().add(pFactory.newLocation("London", name.XSD_STRING));
        hl.getLocation().add(pFactory.newLocation(1, name.XSD_INT));
        hl.getLocation().add(pFactory.newLocation(1.0, name.XSD_FLOAT));
        hl.getLocation().add(pFactory.newLocation(true, name.XSD_BOOLEAN));
        hl.getLocation().add(pFactory.newLocation(pFactory.newQualifiedName(EX_NS, "london", EX_PREFIX), name.PROV_QUALIFIED_NAME));
        hl.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(), name.XSD_DATETIME));
        hl.getLocation().add(pFactory.newLocation(EX_NS + "london", name.XSD_ANY_URI));
        hl.getLocation().add(pFactory.newLocation(pFactory.newGYear(2002), name.XSD_GYEAR));
    }

    public void addValue(HasValue hl) {
        hl.setValue(pFactory.newValue(pFactory.newQualifiedName(EX_NS, "avalue", EX_PREFIX), name.PROV_QUALIFIED_NAME));
    }

    public void addFurtherAttributes(HasOther he) {
        he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX, "hello", name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX, "bonjour", name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX, pFactory.newGYear(2002), name.XSD_GYEAR));
        he.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "bye", name.XSD_STRING));
        // he.getOthers().add(pFactory.newOther(EX_NS,"tag2",EX_PREFIX,
        // pFactory.newInternationalizedString("bonjour","fr"), "xsd:string"));
        he.getOther().add(pFactory.newOther(RoundTripFromJavaTest.EX2_NS, "tag3", RoundTripFromJavaTest.EX2_PREFIX, "hi", name.XSD_STRING));
        //  he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX,
        //                                      "hello\nover\nmore\nlines",
        //                                      name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, get0tagWithDigit(), EX_PREFIX, "hello", name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, get0tagWithDigit(), EX_PREFIX, "hello2", name.XSD_STRING));


    }

    public String get0tagWithDigit() {
        return "0tagWithDigit";
    }

    public void addFurtherAttributes0(HasOther he) {
        he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX, "hello",
                name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "bye",
                name.XSD_STRING));
        he.getOther()
                .add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                        pFactory.newInternationalizedString("bonjour",
                                "fr"),
                        name.XSD_STRING));
        he.getOther().add(pFactory.newOther(RoundTripFromJavaTest.EX2_NS, "tag3", RoundTripFromJavaTest.EX2_PREFIX, "hi",
                name.XSD_STRING));

        he.getOther()
                .add(pFactory.newOther(EX_NS, "tag", EX_PREFIX, new Integer(1),
                        name.XSD_INT));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new Long(1), name.XSD_LONG));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new Short((short) 1),
                name.XSD_SHORT));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new Double(1.0),
                name.XSD_DOUBLE));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new Float(1.0),
                name.XSD_FLOAT));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new java.math.BigDecimal(1.0),
                name.XSD_INTEGER));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new Boolean(true),
                name.XSD_BOOLEAN));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                new Byte((byte) 123),
                name.XSD_BYTE));

        addFurtherAttributesWithQNames(he);

        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX, EX_NS + "london",
                name.XSD_ANY_URI));

    }

    public void addFurtherAttributesWithQNames(HasOther he) {
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                pFactory.newQualifiedName(RoundTripFromJavaTest.EX2_NS, "newyork", RoundTripFromJavaTest.EX2_PREFIX),
                name.PROV_QUALIFIED_NAME));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                pFactory.newQualifiedName(EX_NS, "london", EX_PREFIX),
                name.PROV_QUALIFIED_NAME));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                pFactory.newQualifiedName(RoundTripFromJavaTest.EX3_NS, "london", null),
                name.PROV_QUALIFIED_NAME));

    }


    public void testAlwaysSuccessful() {
        assertTrue(true);
    }
}
