package org.openprovenance.prov.xml;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import junit.framework.TestCase;
        
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.NamespaceGatherer;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;


/**
 * Unit test for setters/getters
 */
public class NamespacesTest 
    extends TestCase
{

    public static final String EXAMPLE_NS = "http://example.com/";
    public static final String EXAMPLE2_NS = "http://example2.com/";
    public static final String EXAMPLE3_NS = "http://example3.com/";
    public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";

    public static ProvFactory pFactory;

    public static ProvUtilities pUtil=new ProvUtilities();

    static final Hashtable<String,String> namespaces;

    
    static {
        namespaces=new Hashtable<String, String>();
        namespaces.put("ex",EXAMPLE_NS);
        pFactory=new ProvFactory(namespaces);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NamespacesTest( String testName )
    {
        super( testName );
    }

   
    public void testNamespaces1 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All ns: " + nss.getPrefixes());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==2);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
    }


    
    public void testNamespaces2 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getType().add(pFactory.newType(new QName(EXAMPLE_NS, "Amazing", "ex2"), 
                                          ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All prefixes: " + nss.getPrefixes());
        System.out.println("All ns: " + nss.getNamespaces());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==4);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("ex2", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
        
        
        assertTrue(nss.getNamespaces().size()==3);
        assertTrue(nss.getNamespaces().get(NamespacePrefixMapper.PROV_NS).equals("prov"));
        assertTrue(nss.getNamespaces().get(EXAMPLE_NS).equals("ex"));
        assertTrue(nss.getNamespaces().get(XSD_NS).equals("xsd"));
        

    }
    
    
    public void testNamespaces3 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getType().add(pFactory.newType(new QName(EXAMPLE_NS, "Amazing", "ex"), 
                                          ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All ns: " + nss.getPrefixes());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==3);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));

    }

    
    public void testNamespaces4 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getLocation().add(pFactory.newLocation(new QName(EXAMPLE_NS, "Amazing"), 
                                                  ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All ns: " + nss.getPrefixes());
        assertTrue(nss.getDefaultNamespace().equals(EXAMPLE_NS));
        assertTrue(nss.getPrefixes().size()==3);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
    }


    public void testNamespaces5 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getType().add(pFactory.newType(new QName("http://www.w3.org/ns/prov#", "emptyCollection", "prov"), 
                                          ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All ns: " + nss.getPrefixes());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==3);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
    }


    public void testNamespaces6 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getOthers().add(pFactory.newOther(new QName (EXAMPLE_NS,"tag1", "ex"),
                                             new QName("http://www.w3.org/ns/prov#", "emptyCollection", "prov"), 
                                             ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All ns: " + nss.getPrefixes());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==3);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
    }
    
    public void testNamespaces7 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getOthers().add(pFactory.newOther(new QName (EXAMPLE2_NS,"tag1", "ex2"),
                                             new QName("http://www.w3.org/ns/prov#", "emptyCollection", "prov"), 
                                             ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All ns: " + nss.getPrefixes());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==4);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
        assertTrue(nss.check("ex2", EXAMPLE2_NS));
    }

    public void testNamespaces8 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getOthers().add(pFactory.newOther(new QName (EXAMPLE2_NS,"tag1", "ex"),
                                             new QName("http://www.w3.org/ns/prov#", "emptyCollection", "prov"), 
                                             ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All prefixes: " + nss.getPrefixes());
        System.out.println("All ns: " + nss.getNamespaces());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==4);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
        assertTrue(nss.check(NamespaceGatherer.xmlns+"0", EXAMPLE2_NS));
        
        assertTrue(nss.getNamespaces().size()==4);
        assertTrue(nss.getNamespaces().get(NamespacePrefixMapper.PROV_NS).equals("prov"));
        assertTrue(nss.getNamespaces().get(EXAMPLE_NS).equals("ex"));
        assertTrue(nss.getNamespaces().get(XSD_NS).equals("xsd"));
        assertTrue(nss.getNamespaces().get(EXAMPLE2_NS).equals(NamespaceGatherer.xmlns+"0"));
        
    }

    public void testNamespaces9 () 
    {
        Activity a1=pFactory.newActivity("ex:a1");
        a1.getOthers().add(pFactory.newOther(new QName(EXAMPLE2_NS,"tag1", "ex"),
                                             new QName(EXAMPLE3_NS,"tag1", "ex"), 
                                             ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All prefixes: " + nss.getPrefixes());
        System.out.println("All ns: " + nss.getNamespaces());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==5);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
        assertTrue(nss.check(NamespaceGatherer.xmlns+"0", EXAMPLE2_NS));
        assertTrue(nss.check(NamespaceGatherer.xmlns+"1", EXAMPLE3_NS));
        
        assertTrue(nss.getNamespaces().size()==5);
        assertTrue(nss.getNamespaces().get(NamespacePrefixMapper.PROV_NS).equals("prov"));
        assertTrue(nss.getNamespaces().get(EXAMPLE_NS).equals("ex"));
        assertTrue(nss.getNamespaces().get(XSD_NS).equals("xsd"));
        assertTrue(nss.getNamespaces().get(EXAMPLE2_NS).equals(NamespaceGatherer.xmlns+"0"));
        assertTrue(nss.getNamespaces().get(EXAMPLE3_NS).equals(NamespaceGatherer.xmlns+"1"));
        
    }
    
    public void testNamespaces10 () 
    {
        Activity a1=pFactory.newActivity("ex:a10");
        a1.getType().add(pFactory.newType(new QName(EXAMPLE_NS, "Amazing","other"), 
                                                  ValueConverter.QNAME_XSD_QNAME));
        Document doc=pFactory.newDocument();
        doc.getStatementOrBundle().add(a1);
        Namespace nss=Namespace.gatherNamespaces(doc);
        System.out.println("Default ns is: " + nss.getDefaultNamespace());
        System.out.println("All prefixes: " + nss.getPrefixes());
        System.out.println("All ns: " + nss.getNamespaces());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==4);
        assertTrue(nss.check("prov", NamespacePrefixMapper.PROV_NS));
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("other", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
    }



}
