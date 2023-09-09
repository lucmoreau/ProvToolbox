
package org.openprovenance.prov.validation;

import java.util.Hashtable;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
public class AttributesTest extends CoreValidateTester {
    
    private static final String EX_NS = "http://example.org/";
    static Logger logger = LogManager.getLogger(AttributesTest.class);


    public AttributesTest(String name) {
        super(name);
    }
    

    public void testTypeAttributes(Validate val, Hashtable<String, Set<Object>> table, String evt1, int n) {
        
        Set<Object> val1=table.get(EX_NS+evt1);
        System.out.println("types " + table);
        assertNotNull(val1);
        assertTrue(val1.size()==n);
    }

    //-----------------  entity
    
    public void testAttributesEntitySuccess1() {
        Validate val=testOrdering("src/test/resources/validate/unification/attributes-entity-success1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testTypeAttributes(val,val.ind.entityTypeTable, "e1",2);
        //TODO: BUG: for some reason
        // the complete documents contains 3 types for entity, test1 being duplicated!
        // 13:22:37,907 DEBUG Unification:170 - updateTypes <T>  to [{http://example.org/}test1, {http://example.org/}test1, {http://example.org/}test2]

    }

    //-----------------  activity
    
    public void testAttributesActivitySuccess1() {
        Validate val=testOrdering("src/test/resources/validate/unification/attributes-activity-success1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testTypeAttributes(val,val.ind.activityTypeTable, "a1",2);
    }

    //-----------------  start
    
    public void testAttributesStartSuccess1() {
        Validate val=testOrdering("src/test/resources/validate/unification/attributes-start-success1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testTypeAttributes(val,val.ind.wasStartedByTypeTable, "start1",2);
    }
    public void testAttributesStartSuccess2() {
        Validate val=testOrdering("src/test/resources/validate/unification/attributes-start-success2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testTypeAttributes(val,val.ind.wasStartedByTypeTable, "start1",3);
    }
    
   //-----------------  end
    
    public void testAttributesEndSuccess1() {
        Validate val=testOrdering("src/test/resources/validate/unification/attributes-end-success1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testTypeAttributes(val,val.ind.wasEndedByTypeTable, "end1",2);
    }
    public void testAttributesEndSuccess2() {
        Validate val=testOrdering("src/test/resources/validate/unification/attributes-end-success2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testTypeAttributes(val,val.ind.wasEndedByTypeTable, "end1",3);
    }
    
    
}
