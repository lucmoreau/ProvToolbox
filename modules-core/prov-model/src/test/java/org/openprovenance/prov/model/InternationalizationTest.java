package org.openprovenance.prov.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import junit.framework.TestCase;

import static org.openprovenance.prov.model.ExtensionRoundTripFromJavaTest.deepCopy;

/**
 * Unit test for PROV roundtrip conversion, specially testing Internationalization.
 */
public class InternationalizationTest extends TestCase {

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
    public InternationalizationTest(String testName) {
        super(testName);
        this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

    public void updateNamespaces(Document doc) {
        Namespace ns = Namespace.gatherNamespaces(doc);
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
        for (int i = 0; i < stment.length; i++) {
            doc.getStatementOrBundle().add(stment[i]);
        }
        if (bundles != null) {
            for (int j = 0; j < bundles.length; j++) {
                doc.getStatementOrBundle().add(bundles[j]);
            }
        }
        updateNamespaces(doc);
        
        if (bundles!=null) {
            for (int j = 0; j < bundles.length; j++) {
                bundles[j].getNamespace().setParent(doc.getNamespace());
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
        compareDocuments(doc, doc3, check && checkTest(file));
    }

    public void conditionalCheckSchema(String file) {
        if (checkSchema(file))
            doCheckSchema1(file);
    }

    public boolean checkSchema(String name) {
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

    Map<String,Document> table=new HashMap<>();
    public void writeDocument(Document doc, String file2) {
        System.out.println(" * document " + file2);
        table.put(file2, deepCopy(doc));
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
        // all tests successful in this file
        return true;
    }

    public boolean mergeDuplicateProperties() {
        return false;
    }



    // /////////////////////////////////////////////////////////////////////

    public QualifiedName q(String n) {
        return pFactory.newQualifiedName(EX_NS, n, EX_PREFIX);
    }

     
    public void testInternationalEscape1() {
        Entity e = pFactory.newEntity(q("entity-escape1"));
        pFactory.addLabel(e,"a label with escaped \" quotes \" ");
        makeDocAndTest(e, "target/international_Escape1");
    }

    public void testInternationalEscape2() {
        Entity e = pFactory.newEntity(q("entity-escape2"));
	e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                            pFactory.newInternationalizedString("a string with escaped \" quotes \" ", "en"),
                                           name.PROV_LANG_STRING));
	// e.getOther().add(pFactory.newOther(EX_NS, "title2", EX_PREFIX,
        //                                     pFactory.newInternationalizedString("a string with escaped \" quotes \" ", null),
        //                                    name.PROV_LANG_STRING));

	//DOES IT MAKE SENSE?
		e.getOther().add(pFactory.newOther(EX_NS, "title3", EX_PREFIX,
	 				   "and another one \" withquotes \" ",
                                            name.XSD_STRING));
	
        makeDocAndTest(e, "target/international_Escape2");
    }

    
    public void testInternationalFR() {
        Entity e = pFactory.newEntity(q("entity-FR"));
        e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                           pFactory.newInternationalizedString("Portez ce vieux whisky au juge blond qui a fumé", "fr"),
                                           name.PROV_LANG_STRING));
        e.getOther().add(pFactory.newOther(EX_NS, "title2", EX_PREFIX,
                                           pFactory.newInternationalizedString("Voix ambiguë d’un cœur qui au zéphyr préfère les jattes de kiwi", "fr"),
                                           name.PROV_LANG_STRING));        
        e.getOther().add(pFactory.newOther(EX_NS, "title3", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS, "préfère",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));      
        e.getOther().add(pFactory.newOther(EX_NS, "ambiguë_cœur", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS,"test",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));    
        makeDocAndTest(e, "target/international_FR1");
    }
    


    public void testInternationalHE() {
        Entity e = pFactory.newEntity(q("entity-HE"));
        e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                           pFactory.newInternationalizedString("דג סקרן שט בים מאוכזב ולפתע מצא חברה", "he"),
                                           name.PROV_LANG_STRING));
        e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                           pFactory.newInternationalizedString("A curious fish sailed the sea disappointedly, and suddenly found company", "en"),
                                           name.PROV_LANG_STRING));        
        e.getOther().add(pFactory.newOther(EX_NS, "title3", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS, "חברה",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));      
        e.getOther().add(pFactory.newOther(EX_NS, "חברה", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS,"test",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));    
        makeDocAndTest(e, "target/international_HE1");
    }
    

    public void testInternationalJP() {
        Entity e = pFactory.newEntity(q("entity-HE"));
        e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                           pFactory.newInternationalizedString("いろはにほへと ちりぬるを わかよたれそ つねならむ うゐのおくやま けふこえて あさきゆめみし ゑひもせす（ん）", "jp"),
                                           name.PROV_LANG_STRING));  
        e.getOther().add(pFactory.newOther(EX_NS, "title3", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS, "わかよたれそ",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));      
        e.getOther().add(pFactory.newOther(EX_NS, "わかよたれそ", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS,"test",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));    
        makeDocAndTest(e, "target/international_JP1");
    }
    

    public void testInternationalRU() {
        Entity e = pFactory.newEntity(q("entity-HE"));
        e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                           pFactory.newInternationalizedString("Любя, съешь щипцы, — вздохнёт мэр, — Кайф жгуч!", "ru"),
                                           name.PROV_LANG_STRING));  
        e.getOther().add(pFactory.newOther(EX_NS, "title1", EX_PREFIX,
                                           pFactory.newInternationalizedString("The mayor will sigh, “Eat the pliers with love; pleasure burns!", "en"),
                                           name.PROV_LANG_STRING));
        e.getOther().add(pFactory.newOther(EX_NS, "title3", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS, "Любя",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));      
        e.getOther().add(pFactory.newOther(EX_NS, "Любя", EX_PREFIX,
                                           pFactory.newQualifiedName(EX_NS,"test",
                                                     EX_PREFIX),
                                           name.PROV_QUALIFIED_NAME));    
        makeDocAndTest(e, "target/international_RU1");
    }
    



}
