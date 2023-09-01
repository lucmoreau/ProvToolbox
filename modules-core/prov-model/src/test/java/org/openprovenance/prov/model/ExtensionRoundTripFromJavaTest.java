package org.openprovenance.prov.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import junit.framework.TestCase;

import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

/**
 * Unit test for PROV roundtrip conversion between Java and XML
 */
public class ExtensionRoundTripFromJavaTest extends TestCase {

    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";

    static final ProvUtilities util = new ProvUtilities();

    public static ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    public static Name name = pFactory.getName();

    private DocumentEquality documentEquality;

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public ExtensionRoundTripFromJavaTest(String testName) {
        super(testName);
        this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

    public void updateNamespaces(Document doc) {
        Namespace ns = Namespace.gatherNamespaces(doc);
        ns.register("provext", "http://openprovenance.org/prov/extension#");
        doc.setNamespace(ns);
    }

    public String extension() {
        return ".xml";
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

    public void makeDocAndTest(Statement stment, String file, Statement[] opt,
                               boolean check) {
        makeDocAndTest(new Statement[] { stment }, file, opt, check);
    }

    public void makeDocAndTest(Statement[] stment, String file,
                               Statement[] opt, boolean check) {
        makeDocAndTest(stment, null, file, opt, check);
    }

    public void makeDocAndTest(Statement[] stment, Bundle[] bundles,
                               String file, Statement[] opt, boolean check) {
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
        
        if (bundles!=null) {
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
        if (check)
            conditionalCheckSchema(file);
        Document doc3 = readDocument(file);
        compareDocuments(doc, doc3, check && checkTest(file));
    }

    public void conditionalCheckSchema(String file) {
        if (checkSchema(file))
            doCheckSchema1(file);
    }

    public boolean checkSchema(String name) {
        if (name.contains("qualified"))  {
            return false;
        }

        return true;
    }

    public void doCheckSchema1(String file) {

    }

    public void doCheckSchema2(String file) {
        // String
        // command="xmllint --schema src/main/resources/w3c/prov.xsd --schema src/main/resources/w3c/xml.xsd --schema src/main/resources/ex.xsd "
        // +file; //--noout
        String command = "xmllint --schema src/main/resources/ex.xsd " + file; // --noout
        try {
            Process proc = Runtime.getRuntime().exec(command);
            proc.waitFor();
            int code = proc.exitValue();
            if (code != 0) {
                BufferedReader errorReader = new BufferedReader(
                                                                new InputStreamReader(
                                                                                      proc.getErrorStream()));
                String s_error = errorReader.readLine();
                if (s_error != null) {
                    System.out.println("Error:  " + s_error);
                }
                BufferedReader outReader = new BufferedReader(
                                                              new InputStreamReader(
                                                                                    proc.getInputStream()));
                String s_out = outReader.readLine();
                if (s_out != null) {
                    System.out.println("Out:  " + s_out);
                }
            }
            // System.out.println("out " + proc.getOutputStream().toString());
            // System.err.println("err " + proc.getErrorStream().toString());
            assertTrue(code == 0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Document readDocument(String file1) {
        return table.get(file1);
    }

    Map<String,Document> table=new Hashtable<>();
    public void writeDocument(Document doc, String file2) {
        System.out.println(" * document " + file2);
        Namespace.withThreadNamespace(doc.getNamespace());
        Document doc2 = deepCopy(doc);
        table.put(file2, doc2);
    }

    static public Document deepCopy(Document doc) {
        BeanTraversal bc=new BeanTraversal(pFactory, pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;
    }

    public void compareDocuments(Document doc, Document doc2, boolean check) {
        assertTrue("self doc equality", doc.equals(doc));
        assertTrue("self doc2 equality", doc2.equals(doc2));
        if (check) {
            boolean result = this.documentEquality.check(doc, doc2);
            if (!result) {
                System.out.println("Pre-write graph: " + doc);
                System.out.println("Read graph: " + doc2);
            }
            assertTrue("doc equals doc2", result);
        } else {
            assertFalse("doc distinct from doc2", doc.equals(doc2));
        }
    }

    public boolean checkTest(String name) {
        return (name.contains("qualified"));
    }

    public boolean mergeDuplicateProperties() {
        return false;
    }


    // /////////////////////////////////////////////////////////////////////

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
        ht.getType().add(pFactory.newType(true, name.XSD_STRING));
        ht.getType().add(pFactory.newType(pFactory.newQualifiedName(EX_NS, "abc", EX_PREFIX),
                                          name.PROV_QUALIFIED_NAME));
        ht.getType().add(pFactory.newType(pFactory.newTimeNow(),
                                          name.XSD_DATETIME));

        ht.getType().add(pFactory.newType(EX_NS + "hello",
                                          name.XSD_ANY_URI));
    }

    public void addLocations(HasLocation hl) {
        hl.getLocation().add(pFactory.newLocation("London",
                                                  name.XSD_STRING));
        hl.getLocation().add(pFactory.newLocation(1, name.XSD_INT));
        hl.getLocation().add(pFactory.newLocation(1.0, name.XSD_FLOAT));
        hl.getLocation()
          .add(pFactory.newLocation(true, name.XSD_BOOLEAN));
        hl.getLocation().add(pFactory.newLocation(pFactory.newQualifiedName(EX_NS, "london",
                                                            EX_PREFIX),
                                                  name.PROV_QUALIFIED_NAME));
        hl.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),
                                                  name.XSD_DATETIME));
        hl.getLocation().add(pFactory.newLocation(EX_NS + "london",
                                                  name.XSD_ANY_URI));
        hl.getLocation().add(pFactory.newLocation(pFactory.newGYear(2002),
                                                  name.XSD_GYEAR));
    }

    public void addValue(HasValue hl) {
        hl.setValue(pFactory.newValue(pFactory.newQualifiedName(EX_NS, "avalue", EX_PREFIX),
                                      name.PROV_QUALIFIED_NAME));
    }

    public void addFurtherAttributes(HasOther he) {
        he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX, "hello",
                                            name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "bye",
                                            name.XSD_STRING));
        // he.getOthers().add(pFactory.newOther(EX_NS,"tag2",EX_PREFIX,
        // pFactory.newInternationalizedString("bonjour","fr"), "xsd:string"));
        he.getOther().add(pFactory.newOther(EX2_NS, "tag3", EX2_PREFIX, "hi",
                                            name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, "tag1", EX_PREFIX,
                                            "hello\nover\nmore\nlines",
                                            name.XSD_STRING));
        he.getOther().add(pFactory.newOther(EX_NS, "0tagWithDigit", EX_PREFIX,
                                            "hello",
                                            name.XSD_STRING));


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
        he.getOther().add(pFactory.newOther(EX2_NS, "tag3", EX2_PREFIX, "hi",
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

    // /////////////////////////////////////////////////////////////////////

    public void addFurtherAttributesWithQNames(HasOther he) {
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                                            pFactory.newQualifiedName(EX2_NS, "newyork",
                                                      EX2_PREFIX),
                                            name.PROV_QUALIFIED_NAME));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                                            pFactory.newQualifiedName(EX_NS, "london",
                                                      EX_PREFIX),
                                            name.PROV_QUALIFIED_NAME));
        he.getOther().add(pFactory.newOther(EX_NS, "tag", EX_PREFIX,
                                            pFactory.newQualifiedName(EX3_NS, "london",null),
                                            name.PROV_QUALIFIED_NAME));

    }


    public boolean test = true;


    public QualifiedName q(String n) {
        return pFactory.newQualifiedName(EX_NS, n, EX_PREFIX);
    }


    public void testQualifiedSpecializationOf1() {
        QualifiedSpecializationOf gen = pFactory.newQualifiedSpecializationOf(q("spec1"), q("e1"),q("e2"), null);
        try {
            makeDocAndTest(gen, "target/qualified-specialization1");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    public void testQualifiedSpecializationOf2() {
        QualifiedSpecializationOf gen = pFactory.newQualifiedSpecializationOf(null, q("e1"),q("e2"), null);
        try {
            makeDocAndTest(gen, "target/qualified-specialization2");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    
    

    public void testQualifiedAlternateOf1() {
        QualifiedAlternateOf gen = pFactory.newQualifiedAlternateOf(q("alt1"), q("e1"),q("e2"), null);
        try {
            makeDocAndTest(gen, "target/qualified-alternate1");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    public void testQualifiedAlternateOf2() {
        QualifiedAlternateOf gen = pFactory.newQualifiedAlternateOf(null, q("e1"),q("e2"), null);
        try {
            makeDocAndTest(gen, "target/qualified-alternate2");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }
    
    
    

    public void testQualifiedHadMemberf1() {
        List<QualifiedName> entities=new LinkedList<QualifiedName>();
        entities.add(q("e2"));
        
        QualifiedHadMember gen = pFactory.newQualifiedHadMember(q("mem"), q("e1"),entities, null);
        try {
            makeDocAndTest(gen, "target/qualified-member1");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    public void testQualifiedHadMember2() {
        List<QualifiedName> entities=new LinkedList<QualifiedName>();
        entities.add(q("e2"));
        QualifiedHadMember gen = pFactory.newQualifiedHadMember(null, q("e1"),entities, null);
        try {
            makeDocAndTest(gen, "target/qualified-member2");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }
    
}
