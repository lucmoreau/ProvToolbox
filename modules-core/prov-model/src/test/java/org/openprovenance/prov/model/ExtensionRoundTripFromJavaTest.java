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
public class ExtensionRoundTripFromJavaTest extends ProvFrameworkTest {

    public static final String EX_NS = "http://example.org/";
    public static final String EX2_NS = "http://example2.org/";
    public static final String EX_PREFIX = "ex";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";

    public static ProvFactory pFactory = new org.openprovenance.prov.vanilla.ProvFactory();
    public static Name name = pFactory.getName();


    public ExtensionRoundTripFromJavaTest() {
        this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
    }


    public void updateNamespaces(Document doc) {
        Namespace ns = Namespace.gatherNamespaces(doc);
        ns.register("provext", "http://openprovenance.org/prov/extension#");
        doc.setNamespace(ns);
    }




    public boolean checkSchema(String name) {
        if (name.contains("qualified"))  {
            return false;
        }

        return true;
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
        System.out.println("deep copy of " + file2);
        Namespace.withThreadNamespace(doc.getNamespace());
        Document doc2 = deepCopy(doc);
        table.put(file2, doc2);
    }

    static public Document deepCopy(Document doc) {
        BeanTraversal bc=new BeanTraversal(pFactory, pFactory);
        org.openprovenance.prov.model.Document doc2=bc.doAction(doc);
        return doc2;
    }


    public boolean checkTest(String name) {
        return (name.contains("qualified"));
    }


    // /////////////////////////////////////////////////////////////////////


    public boolean test = true;


    public QualifiedName q(String n) {
        return pFactory.newQualifiedName(EX_NS, n, EX_PREFIX);
    }

    public void testQualifiedSpecializationOf1() {
        QualifiedSpecializationOf gen = pFactory.newQualifiedSpecializationOf(q("spec1"), q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-specialization1");
    }

    public void testQualifiedSpecializationOf2() {
        QualifiedSpecializationOf gen = pFactory.newQualifiedSpecializationOf(null, q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-specialization2");
    }

    public void testQualifiedAlternateOf1() {
        QualifiedAlternateOf gen = pFactory.newQualifiedAlternateOf(q("alt1"), q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-alternate1");
    }

    public void testQualifiedAlternateOf2() {
        QualifiedAlternateOf gen = pFactory.newQualifiedAlternateOf(null, q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-alternate2");
    }

    public void testQualifiedHadMemberf1() {
        List<QualifiedName> entities= new LinkedList<>();
        entities.add(q("e2"));
        QualifiedHadMember gen = pFactory.newQualifiedHadMember(q("mem"), q("e1"),entities, null);
        makeDocAndTest(gen, "target/qualified-member1");
    }

    public void testQualifiedHadMember2() {
        List<QualifiedName> entities= new LinkedList<>();
        entities.add(q("e2"));
        QualifiedHadMember gen = pFactory.newQualifiedHadMember(null, q("e1"),entities, null);
        makeDocAndTest(gen, "target/qualified-member2");
    }

}
