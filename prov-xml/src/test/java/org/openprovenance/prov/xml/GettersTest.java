package org.openprovenance.prov.xml;
import java.util.Hashtable;
import junit.framework.TestCase;
        
import org.openprovenance.prov.model.HasExtensibility;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Type;


/**
 * Unit test for setters/getters
 */
public class GettersTest 
    extends TestCase
{

    public static ProvFactory pFactory;

    public static ProvUtilities pUtil=new ProvUtilities();

    static final Hashtable<String,String> namespaces;

    
    static {
        namespaces=new Hashtable<String, String>();
        // currently, no prefix used, all qnames map to PC1_NS
        namespaces.put("_","http://example.com/");

        pFactory=new ProvFactory(namespaces);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GettersTest( String testName )
    {
        super( testName );
    }

    public boolean urlFlag=true;

    /**
     * @return the suite of tests being tested
     */




    public void testGetters () throws java.lang.NoSuchMethodException, java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException 
    {
        Activity a3=pFactory.newActivity("a3",
					 "align_warp 3");
        Entity e1=pFactory.newEntity("e1",
				     "file 1");

        Entity e2=pFactory.newEntity("e2",
				     "file 2");

        Used u1=pFactory.newUsed("u1", a3,"in",e1);
        WasGeneratedBy wg1=pFactory.newWasGeneratedBy("wgb1", e1,"out",a3);

        
	// System.out.println(" method " + pUtil.getter(wg1,0));
	// System.out.println(" method " + pUtil.getter(wg1,1));
	// System.out.println(" method " + pUtil.getter(wg1,2));

	assertTrue(pUtil.getter(wg1,0) == wg1.getId());
	assertTrue(pUtil.getter(wg1,1) == wg1.getEntity());
	assertTrue(pUtil.getter(wg1,2) == wg1.getActivity());
	assertEquals(pUtil.getter(wg1,3),wg1.getTime());
	assertTrue(pUtil.getter(wg1,4) == wg1.getAny());

	// System.out.println(" wgb " + wg1);

	// System.out.println(" method " + pUtil.setter(wg1,0,a3.getId()));
	// System.out.println(" method " + pUtil.setter(wg1,1,pFactory.newEntityRef(e2.getId())));
	// System.out.println(" method " + pUtil.setter(wg1,2,wg1.getActivity()));

	// System.out.println(" method " + pUtil.setter(wg1,3,pFactory.newTimeNow()));

	// System.out.println(" wgb " + wg1);

	WasDerivedFrom wd1=pFactory.newWasDerivedFrom(e2,e1,a3,wg1,u1);

	assertTrue(pUtil.getter(wd1,0) == wd1.getId());
	assertTrue(pUtil.getter(wd1,1) == wd1.getGeneratedEntity());
	assertTrue(pUtil.getter(wd1,2) == wd1.getUsedEntity());
	assertTrue(pUtil.getter(wd1,3) == wd1.getActivity());
	assertTrue(pUtil.getter(wd1,4) == wd1.getGeneration());
	assertTrue(pUtil.getter(wd1,5) == wd1.getUsage());
	assertTrue(pUtil.getter(wd1,6) == wd1.getAny());


    }



}
