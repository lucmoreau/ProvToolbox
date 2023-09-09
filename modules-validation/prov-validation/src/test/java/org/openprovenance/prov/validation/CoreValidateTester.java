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
            doTestValidate(file);
            List<Dependencies> ll1 = report.getCycle();
            assertEquals(ll1.size(), strictCycles);
            assertEquals(report.getNonStrictCycle().size(), nonStrictCycles);
            assertTrue(failed.contains(report.getFailedMerge().size()));
            //System.out.println(report);
            //System.out.println(successes);
            //System.out.println(report.getSuccessfulMerge().size());
            assertTrue(successes.contains(report.getSuccessfulMerge().size()));
            assertEquals(report.getQualifiedNameMismatch().size(), mismatches);
            assertEquals(report.getTypeOverlap().size(), types);
            if (report.getMalformedStatements()==null) {
                assertEquals(0, malformed);
            } else {
                assertEquals(report.getMalformedStatements().getStatement().size(), malformed);
            }

            assertFalse(excp);
            return;
        } catch (IOException e) {
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        assertTrue(excp);

    }

    List<Integer> z = List.of(0);
    List<Integer> one = List.of(1);
    List<Integer> v12 = Arrays.asList(1, 2);
    List<Integer> v123 = Arrays.asList(1, 2, 3);
    List<Integer> two = List.of(2);
    List<Integer> three = List.of(3);


    public void doTestValidate(String file) throws java.io.IOException {
        logger.info("Loading file " + file);

        File f = new File(file);
        org.openprovenance.prov.scala.immutable.Document d = Parser.readDocument(file);
        Document b = pf.newDocument(d);
        String out = "target/complete-" + f.getName();
        String report = "target/report-" + f.getName();
        doTestValidate(b, out, report);
    }


    public void doTestValidate(Document b, String out, String reportFile) throws java.io.IOException {

        report=new Validate(Config.newYesToAllConfig(pf, new ValidationObjectMaker())).validate(b);

    }

    public Validate doTestOrdering(Document b, String out, String reportFile,
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

    public Validate doTestOrdering(String file) {
        return doTestOrdering(file, new LinkedList<>());
    }

    public Validate doTestOrdering(String file, String rule) {
        List<String> rulesToDisable= new LinkedList<>();
        rulesToDisable.add(rule);
        return doTestOrdering(file, rulesToDisable);
    }

    public Validate doTestOrdering(String file, List<String> rulesToDisable) {
        try {
            logger.info("testing " + file);

            File f = new File(file);
            org.openprovenance.prov.scala.immutable.Document d = Parser.readDocument(file);
            Document b = pf.newDocument(d);
            String out = "target/complete-" + f.getName();
            String report = "target/report-" + f.getName();

            return doTestOrdering(b,out,report,rulesToDisable);
        } catch (IOException | UnsupportedOperationException e) {
            logger.throwing(e);
        }
        fail();
        return null;
    }


}
