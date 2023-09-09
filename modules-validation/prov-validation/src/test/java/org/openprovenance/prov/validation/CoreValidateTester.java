package org.openprovenance.prov.validation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


import junit.framework.TestCase;

import org.openprovenance.prov.validation.report.Dependencies;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.scala.immutable.Parser;


public abstract class CoreValidateTester extends TestCase {
    
    public static org.openprovenance.prov.model.ProvFactory pf=new org.openprovenance.prov.scala.mutable.ProvFactory();

    static ValidationReport report;

    static Logger logger = LogManager.getLogger(CoreValidateTester.class);

    protected static Set<String> checkedSet = new HashSet<String> ();


    public CoreValidateTester(String testName) {
        super(testName);
    }
    public void testUnification(String file, int strictCycles, int nonStrictCycles, List<Integer> failed,
                                List<Integer> successes,
                                int mismatches, int malformed, boolean excp) {
        testUnification(file, strictCycles, nonStrictCycles, failed, successes, mismatches, malformed, 0, excp);
    }

    public void testUnification(String file, int strictCycles, int nonStrictCycles, List<Integer> failed,
                                        List<Integer> successes,
                                        int mismatches, int malformed, int types, boolean excp) {
        try {
        	logger.debug("testing " + file);
            testValidate(file);
            List<Dependencies> ll1 = report.getCycle();
            assertTrue(ll1.size() == strictCycles);
            assertTrue(report.getNonStrictCycle().size() == nonStrictCycles);
            assertTrue(failed.contains(report.getFailedMerge().size()));
            System.out.println(report);
            System.out.println(successes);
            System.out.println(report.getSuccessfulMerge().size());
            assertTrue(successes.contains(report.getSuccessfulMerge().size()));
            assertTrue(report.getQualifiedNameMismatch().size() == mismatches);
            assertTrue(report.getTypeOverlap().size()==types);
            if (report.getMalformedStatements()==null) {
        	assertTrue(malformed==0);
            } else {
        	assertTrue(report.getMalformedStatements().getStatement().size()== malformed);
            }

            assertFalse(excp);
            return;
        } catch (IOException e) {
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        assertTrue(excp);

    }

    List<Integer> z = Arrays.asList(0);
    List<Integer> one = Arrays.asList(1);
    List<Integer> v12 = Arrays.asList(1, 2);
    List<Integer> v123 = Arrays.asList(1, 2, 3);
    List<Integer> two = Arrays.asList(2);
    List<Integer> three = Arrays.asList(3);

  
    public void testValidate(String file) throws 
            java.io.IOException {
        logger.info("Loading file " + file);
        
        File f = new File(file);
        org.openprovenance.prov.scala.immutable.Document d = Parser.readDocument(file);
        Document b = pf.newDocument(d);
        String out = "target/complete-" + f.getName();
        String report = "target/report-" + f.getName();
        testValidate(b, out, report);
    }
    

    public void testValidate(Document b, String out, String reportFile)
            throws java.io.IOException {
        
        report=new Validate(Config.newYesToAllConfig(pf, new ValidationObjectMaker())).validate(b);

    }

    public Validate testOrdering(Document b, String out, String reportFile,
                                List<String> rulesToDisable)
            throws  java.io.IOException {
                Config config=Config.newYesToAllConfig(pf,new ValidationObjectMaker());
                for (String ruleToDisable: rulesToDisable) {
                    config.config.remove(ruleToDisable);
                }
                Validate v=new Validate(config);
                v.validate(b);
                checkedSet.addAll(rulesToDisable);
                return v;
            }

    public Validate testOrdering(String file) {
        return testOrdering(file, new LinkedList<String>());
    }

    public Validate testOrdering(String file, String rule) {
        List<String> rulesToDisable=new LinkedList<String>();
        rulesToDisable.add(rule);
        return testOrdering(file, rulesToDisable);
    }

    public Validate testOrdering(String file, List<String> rulesToDisable) {
        try {
            logger.debug("testing " + file);

            File f = new File(file);
            org.openprovenance.prov.scala.immutable.Document d = Parser.readDocument(file);
            Document b = pf.newDocument(d);
            String out = "target/complete-" + f.getName();
            String report = "target/report-" + f.getName();
    
            Validate val=testOrdering(b,out,report,rulesToDisable);
            
            return val;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        assertTrue(false);
        return null;
    }


}
