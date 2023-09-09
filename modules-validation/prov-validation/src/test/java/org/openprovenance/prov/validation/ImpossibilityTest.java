package org.openprovenance.prov.validation;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.openprovenance.prov.scala.immutable.Parser;

public class ImpossibilityTest extends CoreValidateTester{
    
    ProvFactory pFactory=new org.openprovenance.prov.scala.mutable.ProvFactory();

    public ImpossibilityTest(String testName) {
        super(testName);
    }

    public Validate testImpossibility(Document b, String out, String reportFile, List<String> rulesToDisable)
            throws java.io.IOException {
        Config config=Config.newYesToAllConfig(pFactory, new ValidationObjectMaker());
        for (String ruleToDisable: rulesToDisable) {
            config.config.remove(ruleToDisable);
        }
        Validate v=new Validate(config);
        v.validate(b);
        checkedSet.addAll(rulesToDisable);

        return v;
    }
    
    public Validate testImpossibility(String file) {
	return testImpossibility(file, new LinkedList<String>(), false);
    }
    public Validate testImpossibility(String file, boolean excp) {
	return testImpossibility(file, new LinkedList<String>(), excp);
    }

    public Validate testImpossibility(String file, List<String> rulesToDisable, boolean excp) {
        try {
            logger.debug("testing " + file);
            
            File f = new File(file);
            org.openprovenance.prov.scala.immutable.Document d = Parser.readDocument(file);
            Document b=pf.newDocument(d);
            String out = "target/complete-" + f.getName();
            String report = "target/report-" + f.getName();

            Validate val=testImpossibility(b,out,report,rulesToDisable);
            assertFalse(excp);
            return val;
        } catch (IOException e) {
        } catch (UnsupportedOperationException e) {
        }
        assertTrue(excp);
        return null;
    }

   
    //-----------------  specialization
    
    public void testImpossibilitySuccess1() {
        testImpossibility("src/test/resources/validate/unification/specialization-success1.provn");
    }
    public void testImpossibilitySuccess2() {
        Validate val=testImpossibility("src/test/resources/validate/unification/specialization-success2.provn");
        System.out.println("table " + val.getInference().specializationTable);
        System.out.println("table " + val.getInference().specializationTable.get(pFactory.newQualifiedName("http://example.org/","e2","ex")));
        // WATCHOUT: the qualified names in the table are those created by the parser (the mutable ones).
        assertTrue(val.getInference().specializationTable.get(new org.openprovenance.prov.scala.immutable.ProvFactory().newQualifiedName("http://example.org/","e2","ex")).size()==1);
        assertTrue(val.getInference().specializationTable.get(new org.openprovenance.prov.scala.immutable.ProvFactory().newQualifiedName("http://example.org/","e3","ex")).size()==2);

        //assertTrue(val.getInference().specializationTable.get(val.uniq.indexer.namespace.stringToQualifiedName("ex:e2",pFactory)).size()==1);
        //assertTrue(val.getInference().specializationTable.get(val.uniq.indexer.namespace.stringToQualifiedName("ex:e3",pFactory)).size()==2);
    }
    
    public void NOtestImpossibilityFail1() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/specialization-fail1.provn").getReport();
	assertFalse(val.getMalformedStatements()==null);

    }
    public void NOtestImpossibilityFail2() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/specialization-fail2.provn").getReport();
	assertFalse(val.getMalformedStatements()==null);
    }
    public void testImpossibilityFail3() {
        ValidationReport val=testImpossibility("src/test/resources/validate/unification/specialization-fail3.provn", false).getReport();
                
        assertTrue(val.getSpecializationReport().getSpecializationOf().size()==1);
    }
    public void testImpossibilityFail4() {
        ValidationReport val=testImpossibility("src/test/resources/validate/unification/specialization-fail4.provn", false).getReport();
                
        assertTrue(val.getSpecializationReport().getSpecializationOf().size()==2);
    }
   
    public void testImpossibilityFail4b() {
        List<String> rules=new LinkedList<String>();
        rules.add(Config.INFERENCE_SPECIALIZATION_TRANSITIVE);
        ValidationReport val=testImpossibility("src/test/resources/validate/unification/specialization-fail4.provn", rules, false).getReport();
                
        assertTrue(val.getSpecializationReport()==null);
    }
    public void testImpossibilityFail4c() {
        List<String> rules=new LinkedList<String>();
        rules.add(Config.CONSTRAINT_IMPOSSIBLE_SPECIALIZATION_REFLEXIVE);
        ValidationReport val=testImpossibility("src/test/resources/validate/unification/specialization-fail4.provn", rules, false).getReport();
                
        assertTrue(val.getSpecializationReport()==null);
    }
    
    //-----------------  membership

   
    public void testImpossibilityMembershipSuccess1() {
        testImpossibility("src/test/resources/validate/unification/membership-success1.provn");
    }
    public void NOtestImpossibilityMembershipFail1() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/membership-fail1.provn").getReport();
	assertFalse(val.getMalformedStatements()==null);
    }
    
    /*
    
    //  -----------------  mention
    
    public void testImpossibilityMentionSuccess1() {
        testImpossibility("src/test/resources/validate/unification/mention-success1.provn");
    }
    public void testImpossibilityMentionSuccess2() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/mention-success2.provn").getReport();
	 assertFalse(val.getSuccessfulMerge().isEmpty());
    }
    
    public void testImpossibilityMentionFail1() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/mention-fail1.provn").getReport();
	assertFalse(val.getMalformedStatements()==null);

    }
    public void testImpossibilityMentionFail2() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/mention-fail2.provn").getReport();
	assertFalse(val.getMalformedStatements()==null);

    }
    public void testImpossibilityMentionFail3() {
	ValidationReport val=testImpossibility("src/test/resources/validate/unification/mention-fail3.provn").getReport();
	assertFalse(val.getMalformedStatements()==null);
    }
    
    public void testImpossibilityMentionFail4() {
	 ValidationReport val=testImpossibility("src/test/resources/validate/unification/mention-fail4.provn", false).getReport();
	 assertFalse(val.getFailedMerge().isEmpty());
    }
    
    */
    
}
