package org.openprovenance.prov.validation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.validation.report.TypeOverlap;
import org.openprovenance.prov.scala.immutable.Parser;

public class TypeTest extends CoreValidateTester{

    static final String EX_NS = "http://example.org/";

    static Logger logger = LogManager.getLogger(TypeTest.class);


    public Validate doTestType(Document b, String out, String reportFile, List<String> rulesToDisable)
            throws java.io.IOException {
        Config config=Config.newYesToAllConfig(new org.openprovenance.prov.scala.mutable.ProvFactory(), new ValidationObjectMaker());
        for (String ruleToDisable: rulesToDisable) {
            config.config.remove(ruleToDisable);
        }
        Validate v=new Validate(config);
        v.validate(b);
        checkedSet.addAll(rulesToDisable);

        return v;
    }

    public Validate doTestType(String file) {
        return doTestType(file, new LinkedList<String>(), false, null, 0);
    }
    public Validate doTestType(String file, String name, int typeOverlaps) {
        return doTestType(file, new LinkedList<String>(), false, name, typeOverlaps);
    }
    public Validate doTestType(String file, String rule) {
        LinkedList<String> ll=new LinkedList<String>();
        ll.add(rule);
        return doTestType(file, ll, false, null, -1);
    }

    public Validate doTestType(String file, List<String> rulesToDisable, boolean excp, String name, int typeOverlaps) {
        try {
            logger.info("testing " + file);
            File f = new File(file);
            org.openprovenance.prov.scala.immutable.Document d = Parser.readDocument(file);
            Document b=pf.newDocument(d);
            String out = "target/complete-" + f.getName();
            String report = "target/report-" + f.getName();

            Validate val= doTestType(b,out,report,rulesToDisable);
            assertFalse(excp);
            if (name!=null) {
                //assertTrue(val.constraints.getReport().getTypeReport()!=null);
                //assertTrue(val.constraints.getReport().getTypeReport().getEntityOrActivityOrWasGeneratedBy()!=null);
                //assertTrue(val.constraints.getReport().getTypeReport().getEntityOrActivityOrWasGeneratedBy().size()==typeOverlaps);
                testOverlap(val,name,typeOverlaps);

            } else {
                // assertTrue(val.constraints.getReport().getTypeReport()==null);
            }

            return val;
        } catch (IOException e) {
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        assertTrue(excp);
        return null;
    }

    public void testOverlap(Validate val, String name, int typeOverlaps) {
        if (name!=null) {
            List<TypeOverlap> ll=val.getReport().getTypeOverlap();

            //System.out.println("$$$ overlaping types " + ll);

            for (TypeOverlap to: ll) {
                if (to.getKey().equals(EX_NS+name)) {
                    //System.out.println("$$$ overlaping types " + to); 
                    //System.out.println("$$$ overlaping types " + EX_NS+name); 

                    assertEquals(to.getType().size(), typeOverlaps);
                    return;
                }
            }
            logger.debug(">>>>>>>>>>>>>> " + val.constraints.typeOverlapTable);
            logger.debug(">>>>>>>>>>>>>> " + val.typeChecker.aggregatedTypes);
            assertEquals(0, typeOverlaps);
            //assertTrue(val.expa.aggregatedTypes.get(EX_NS+name)==null || val.expa.aggregatedTypes.get(EX_NS+name).size()==typeOverlaps);
        } else {
        }

    }

    public boolean isTypeOf(Validate val, String name, String type) {
        if (name!=null) {
            logger.debug("name " + name);
            logger.debug("type " + val.typeChecker.aggregatedTypes.get(EX_NS+name));
            return val.typeChecker.aggregatedTypes.get(EX_NS+name).contains(type);
        }
        return false;
    }
    public void checkTypeOf(Validate val, String name, String type) {
        checkTypeOf(val, name, type, true);
    }
    public void checkTypeOf(Validate val, String name, String type, boolean yes) {
        assertEquals(yes, isTypeOf(val, name, type));
    }
    private boolean illegal(String type1, String type2) {
        List<String> ll=Arrays.asList(new String[]{ type1, type2 });

        Collection<String> col=new Types(null,null,null).conflictingTypes(ll);
        return !(col==null || col.isEmpty());
    }

    private Collection<String> illegal(String type1, String type2, String type3) {
        List<String> ll=Arrays.asList(new String[]{ type1, type2, type3 });

        Collection<String> col=new Types(null,null,null).conflictingTypes(ll);
        return col;
    }
    private Collection<String> illegal(String type1, String type2, String type3, String type4, String type5) {
        List<String> ll=Arrays.asList(new String[]{ type1, type2, type3, type4, type5 });

        Collection<String> col=new Types(null,null,null).conflictingTypes(ll);
        return col;
    }

    //-----------------  specialization


    public void testTypeSuccess1() {
        Validate val= doTestType("src/test/resources/validate/type/type-success1.provn","e1", 0);
    }

    public void testTypeFail1() {
        Validate val= doTestType("src/test/resources/validate/type/type-fail1.provn","e1", 2);
    }

    public void testTypeFail1b() {
        Validate val= doTestType("src/test/resources/validate/type/type-fail1.provn",Config.CONSTRAINT_TYPING);
    }


    public void testTypeFail2() {
        Validate val= doTestType("src/test/resources/validate/type/type-fail2.provn","e2",2);
        testOverlap(val, "e1", 0);
    }

    public void testTypeFail3() {
        Validate val= doTestType("src/test/resources/validate/type/type-fail3.provn","e1",2);
        testOverlap(val, "e2", 0);
    }

    public void testTypeFail4() {
        Validate val= doTestType("src/test/resources/validate/type/type-fail4.provn","gen",2);
    }


    public void testTypeSuccess2() {
        Validate val= doTestType("src/test/resources/validate/type/type-success2.provn","e1", 0);
    }

    public void testTypeSuccess3() {
        Validate val= doTestType("src/test/resources/validate/type/type-success3.provn","e1", 0);
        checkTypeOf(val, "e1", Types.VAL_TYPE_NS+"Entity");
        checkTypeOf(val, "e1", EX_NS+"test1");
        checkTypeOf(val, "e1", EX_NS+"test2", false);

        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"Entity");
        checkTypeOf(val, "e2", EX_NS+"test1");// "inherited" through specialization
        checkTypeOf(val, "e2", EX_NS+"test2");

    }


    public void testTypeSuccess4() {
        Validate val= doTestType("src/test/resources/validate/type/type-success3.provn",
                Config.INFERENCE_SPECIALIZATION_ATTRIBUTES_INFERENCE);

        checkTypeOf(val, "e1", Types.VAL_TYPE_NS+"Entity");
        //FIXME: BUG, namespace lost

        //checkTypeOf(val, "e1", EX_NS+"test1");
        checkTypeOf(val, "e1", EX_NS+"test1");
        checkTypeOf(val, "e1", EX_NS+"test2",false);

        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"Entity");
        checkTypeOf(val, "e2", EX_NS+"test1",false);// not "inherited" through specialization, since rule disabled
        //checkTypeOf(val, "e2", EX_NS+"test2"); 
        checkTypeOf(val, "e2", EX_NS+"test2");

    }


    public void testTypeMembershipSuccess1() {
        Validate val= doTestType("src/test/resources/validate/unification/membership-success1.provn");

        checkTypeOf(val, "e1", Types.VAL_TYPE_NS+"Entity");

        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"Entity");
        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"Collection");

    }

    public void testTypeMembershipFail1() {
        Validate val= doTestType("src/test/resources/validate/type/type-collection-fail1.provn", "e1", 0);
        testOverlap(val, "e1", 0);
        testOverlap(val, "e2", 2);


        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"Entity");
        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"Collection");
        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"NonEmptyCollection");
        checkTypeOf(val, "e2", Types.VAL_TYPE_NS+"EmptyCollection");
    }


    public void testAssociation() {
        Validate val= doTestType("src/test/resources/validate/ordering/association2.provn");
        testOverlap(val, "ag", 0);
    }


    public void testIllegalTypes() {

        assertTrue(illegal(Types.activityURI,Types.entityURI));
        assertTrue(illegal(Types.activityURI,Types.bundleURI));
        assertTrue(illegal(Types.activityURI,Types.collectionURI));
        assertTrue(illegal(Types.activityURI,Types.emptyCollectionURI));


        assertFalse(illegal(Types.softwareAgentURI,Types.entityURI));
        assertFalse(illegal(Types.softwareAgentURI,Types.bundleURI));
        assertFalse(illegal(Types.softwareAgentURI,Types.collectionURI));
        assertFalse(illegal(Types.softwareAgentURI,Types.emptyCollectionURI));

        assertFalse(illegal(Types.agentURI,Types.entityURI));
        assertFalse(illegal(Types.agentURI,Types.bundleURI));
        assertFalse(illegal(Types.agentURI,Types.collectionURI));
        assertFalse(illegal(Types.agentURI,Types.emptyCollectionURI));


        assertFalse(illegal(Types.bundleURI,Types.entityURI));
        assertFalse(illegal(Types.bundleURI,Types.bundleURI));
        assertFalse(illegal(Types.bundleURI,Types.collectionURI));
        assertFalse(illegal(Types.bundleURI,Types.emptyCollectionURI));


        assertTrue(illegal(Types.nonEmptyCollectionURI,Types.emptyCollectionURI));

        assertFalse(illegal("foo1", "foo2"));


    }

    public void testIllegalType3() {

        assertTrue(illegal(Types.activityURI,Types.entityURI, Types.agentURI).size()==2);

        assertTrue(illegal(Types.softwareAgentURI,
                Types.bundleURI,
                Types.agentURI,
                Types.entityURI,
                Types.agentURI).size()==0);

    }



}
