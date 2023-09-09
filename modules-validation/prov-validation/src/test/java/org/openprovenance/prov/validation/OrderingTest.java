package org.openprovenance.prov.validation;



import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.validation.matrix.SparseMatrix;

public class OrderingTest extends CoreValidateTester {
    
    static final String EX_NS = "http://example.org/";
    static Logger logger = LogManager.getLogger(OrderingTest.class);


    public OrderingTest(String name) {
        super(name);
    }
    

    public void testPrecede(Validate val, String evt1, String evt2) {
        testPrecede(val,evt1,evt2,true);
    }

    public void testPrecede(Validate val, String evt1, String evt2,
                               boolean flag) {
                                
                                Integer val1=val.evtIdx.eventIndex.get(EX_NS+evt1);
                                Integer val2=val.evtIdx.eventIndex.get(EX_NS+evt2);
                                assertNotNull(val1);
                                assertNotNull(val2);
                                
                                org.openprovenance.prov.validation.matrix.Pair p=val.constraints.getMatrix().m.g(val1, val2);

                                if (flag) {
                                    assertTrue(p!=null);
                                    assertTrue(SparseMatrix.isNonStrictOrdering(p.getValue()));

                                } else {
                                    assertFalse(p!=null);
                            
                                }
                            
                            
                            }
    public void testPrecedeStrict(Validate val, String evt1, String evt2) {
        testPrecedeStrict(val,evt1,evt2,true);
    }

    public void testPrecedeStrict(Validate val, String evt1, String evt2,
                            boolean flag) {
                             
                             Integer val1=val.evtIdx.eventIndex.get(EX_NS+evt1);
                             Integer val2=val.evtIdx.eventIndex.get(EX_NS+evt2);
                             assertNotNull(val1);
                             assertNotNull(val2);
                         
                             org.openprovenance.prov.validation.matrix.Pair p=val.constraints.getMatrix().m.g(val1, val2);
                             if (flag) {
                                 assertTrue(p!=null);
                                 assertFalse(SparseMatrix.isNonStrictOrdering(p.getValue()));
                             } else {
                                 assertFalse(p!=null);

                             }
                         
                         
                         }



    //-----------------  derivation
    
    public void testConstraintsDerivation1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/derivation1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecedeStrict(val,"gen1","gen2");
        testPrecede(val,"gen2","gen1",false);

    }
    
    public void testConstraintsDerivation1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/derivation1.provn",
                                  Config.CONSTRAINT_DERIVATION_GENERATION_GENERATION_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2",false); // disabled
        testPrecede(val,"gen2","gen1",false);

    }
    


    public void testConstraintsDerivation2() {
        Validate val=testOrdering("src/test/resources/validate/ordering/derivation2.provn");
        testPrecedeStrict(val,"gen1","gen2");
        testPrecedeStrict(val,"gen2","gen1");

        // after transitive closure
        testPrecedeStrict(val,"gen1","gen1");
        testPrecedeStrict(val,"gen2","gen2");
        assertFalse(val.getReport().getCycle().isEmpty());
    }
    
    
    public void testConstraintsDerivation3() {
        Validate val=testOrdering("src/test/resources/validate/ordering/derivation3.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecedeStrict(val,"gen1","gen2");
        testPrecede(val,"gen2","gen1",false);
        testPrecede(val,"use1","gen2");

    }

    public void testConstraintsDerivation3b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/derivation3.provn",
                                  Config.CONSTRAINT_DERIVATION_USAGE_GENERATION_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecedeStrict(val,"gen1","gen2");
        testPrecede(val,"gen2","gen1",false);
        testPrecede(val,"use1","gen2",false); // disabled

    }

    public void testConstraintsStarts1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/starts1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen2","start2");
        testPrecede(val,"start1","inv1");
    }
    
    public void testConstraintsStarts1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/starts1.provn",
                                  Config.CONSTRAINT_WASSTARTEDBY_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen2","start2",false);
        testPrecede(val,"start1","inv1",false);
    }
    
    public void testConstraintsEnds1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/ends1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen2","end2");
        testPrecede(val,"end1","inv1");
    }
    
    public void testConstraintsEnds1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/ends1.provn",
                                  Config.CONSTRAINT_WASENDEDBY_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen2","end2",false);
        testPrecede(val,"end1","inv1",false);
    }
    
    
    public void testConstraintsEntity1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","use1");
        testPrecede(val,"use1","inv1");
    }
    
    public void testConstraintsEntity1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity1.provn",
                                  Config.CONSTRAINT_USAGE_PRECEDES_INVALIDATION);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","use1");
        testPrecede(val,"use1","inv1",false);
    }
    
    public void testConstraintsEntityb() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity1.provn",
                                  Config.CONSTRAINT_GENERATION_PRECEDES_USAGE);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","use1",false);
        testPrecede(val,"use1","inv1");
    }
    
    public void testConstraintsEntity2() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","inv1");
    }
    public void testConstraintsEntity2b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity2.provn",
                                  Config.CONSTRAINT_GENERATION_PRECEDES_INVALIDATION);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","inv1",false);
    }
    
    public void testConstraintsActivity1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","end1");
    }
    
    public void testConstraintsActivity1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity1.provn",
                                  Config.CONSTRAINT_START_PRECEDES_END);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","end1",false);
    }

    public void testConstraintsActivity2() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","use1");
        testPrecede(val,"use1","end1");
    }
    public void testConstraintsActivity2b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity2.provn",
                                  Config.CONSTRAINT_USAGE_WITHIN_ACTIVITY);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","use1",false);
        testPrecede(val,"use1","end1",false);
    }
    
    public void testConstraintsActivity3() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity3.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","gen1");
        testPrecede(val,"gen1","end1");
    }
    public void testConstraintsActivity3b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity3.provn",
                                  Config.CONSTRAINT_GENERATION_WITHIN_ACTIVITY);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","gen1",false);
        testPrecede(val,"gen1","end1",false);
    }
    public void testConstraintsEntity3() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity3.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2");
        testPrecede(val,"gen2","gen1");
    }
    public void testConstraintsEntity3b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity3.provn",
                                  Config.CONSTRAINT_GENERATION_GENERATION_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2",false);
        testPrecede(val,"gen2","gen1",false);
    }
    public void testConstraintsEntity4() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity4.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"inv1","inv2");
        testPrecede(val,"inv2","inv1");
    }
    public void testConstraintsEntity4b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/entity4.provn",
                                  Config.CONSTRAINT_INVALIDATION_INVALIDATION_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"inv1","inv2",false);
        testPrecede(val,"inv2","inv1",false);
    }
    
    public void testConstraintsActivity4() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity4.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","start2");
        testPrecede(val,"start2","start1");
    }
    public void testConstraintsActivity4b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity4.provn",
                                  Config.CONSTRAINT_START_START_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","start2",false);
        testPrecede(val,"start2","start1",false);
    }
    public void testConstraintsActivity5() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity5.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"end1","end2");
        testPrecede(val,"end2","end1");
    }
    public void testConstraintsActivity5b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/activity5.provn",
                                  Config.CONSTRAINT_END_END_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"end1","end2",false);
        testPrecede(val,"end2","end1",false);
    }
    public void testConstraintsAssociation1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/association1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","inv1");
        testPrecede(val,"gen1","end1");
    }
    public void testConstraintsAssociation1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/association1.provn",
                                  Config.CONSTRAINT_WASASSOCIATEDWITH_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","inv1",false);
        testPrecede(val,"gen1","end1",false);
    }
    
    public void testConstraintsAssociation2() {
        Validate val=testOrdering("src/test/resources/validate/ordering/association2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","end2");
        testPrecede(val,"start2","end1");
    }
    
    public void testConstraintsAssociation2b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/association2.provn",
                                  Config.CONSTRAINT_WASASSOCIATEDWITH_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","end2",false);
        testPrecede(val,"start2","end1",false);
    }
    
    public void testConstraintsAttribution1() {
        Validate val=testOrdering("src/test/resources/validate/ordering/attribution1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2");
    }
    public void testConstraintsAttribution1b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/attribution1.provn",
                                  Config.CONSTRAINT_WASATTRIBUTEDTO_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2",false);
    }
  
    public void testConstraintsAttribution2() {  //TODO revisit example, not sure the agent needs to be declared.
        Validate val=testOrdering("src/test/resources/validate/ordering/attribution2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","gen2");  //PROBLEM here
    }
    public void testConstraintsAttribution2b() {
        Validate val=testOrdering("src/test/resources/validate/ordering/attribution2.provn",
                                  Config.CONSTRAINT_WASATTRIBUTEDTO_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","gen2",false);
    }
    
    public void testConstraintsDelegation1() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/delegation1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","inv2");  //PROBLEM here
    }
    public void testConstraintsDelegation1b() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/delegation1.provn",
                                  Config.CONSTRAINT_ACTEDONBEHALFOF_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","inv2",false);  //PROBLEM here
    }
    public void testConstraintsDelegation2() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/delegation2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","end2");  //PROBLEM here
    }
    public void testConstraintsDelegation2b() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/delegation2.provn",
                                  Config.CONSTRAINT_ACTEDONBEHALFOF_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"start1","end2",false);  //PROBLEM here
    }
    
    // specialization
    
    public void testConstraintsSpecialization1() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/specialization1.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2",true); 
        testPrecede(val,"gen2","gen1",false); 
    }
    public void testConstraintsSpecialization1b() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/specialization1.provn",
                                  Config.CONSTRAINT_SPECIALIZATION_GENERATION_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2",false); 
        testPrecede(val,"gen2","gen1",false); 
    }
    
    
    public void testConstraintsSpecialization2() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/specialization2.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"inv2","inv1",true); 
        testPrecede(val,"inv1","inv2",false); 
    }
    public void testConstraintsSpecialization2b() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/specialization2.provn",
                                  Config.CONSTRAINT_SPECIALIZATION_INVALIDATION_ORDERING);
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"inv2","inv1",false); 
        testPrecede(val,"inv1","inv2",false); 
    }
    
    public void testConstraintsSpecialization3() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/specialization3.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecedeStrict(val,"gen1","gen2",true); 
        testPrecede(val,"gen2","gen1",false); 
    }
    
    public void testConstraintsSpecialization4() {  
        Validate val=testOrdering("src/test/resources/validate/ordering/specialization4.provn");
        if (val==null) {
            assertTrue(false); // should not be here
            return;
        }
        testPrecede(val,"gen1","gen2"); 
        testPrecedeStrict(val,"gen2","gen1"); 
    }
    
    
    // final check: have checked them all? 
    // WATCH OUT: order dependency!

    public void NOtestConstraintsXXXXX9() {
	logger.debug("implemented " + Config.implementedTable.keySet());
	logger.debug("checked" + checkedSet);
	
	assertTrue(Config.implementedTable.keySet().equals(checkedSet));
    }
}
