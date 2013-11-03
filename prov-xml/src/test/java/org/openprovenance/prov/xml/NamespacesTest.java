package org.openprovenance.prov.xml;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import junit.framework.TestCase;
        
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.model.Document;


/**
 * Unit test for setters/getters
 */
public class NamespacesTest 
    extends TestCase
{

    public static final String EXAMPLE_NS = "http://example.com";
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
        assertTrue(nss.getPrefixes().size()==1);
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
        System.out.println("All ns: " + nss.getPrefixes());
        assertNull(nss.getDefaultNamespace());
        assertTrue(nss.getPrefixes().size()==3);
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("ex2", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
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
        assertTrue(nss.getPrefixes().size()==2);
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
        assertTrue(nss.getPrefixes().size()==2);
        assertTrue(nss.check("ex", EXAMPLE_NS));
        assertTrue(nss.check("xsd", XSD_NS));
    }



}
