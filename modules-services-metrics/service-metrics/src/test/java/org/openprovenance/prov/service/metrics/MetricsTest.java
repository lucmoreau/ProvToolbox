package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.rules.TrafficLight;
import org.openprovenance.prov.rules.TrafficLightResult;
import org.openprovenance.prov.rules.counters.EntityActivityDerivationCounter;
import org.openprovenance.prov.service.signature.SignatureService;
import org.openprovenance.prov.vanilla.ProvFactory;


public class MetricsTest extends TestCase {
    static class MetricsCalculatorWithoutSignature extends MetricsCalculator {
        @Override
        public Object getSignature(SignatureService signatureService, org.openprovenance.prov.model.Document doc) {
            return null;
        }
    }
    public void testMetrics1() throws JsonProcessingException {

        InteropFramework interop = new InteropFramework();
        Document doc = interop.readDocumentFromFile("src/test/resources/metrics/doc1.provn");

        MetricsRecord metricsRecord=(MetricsRecord)new MetricsCalculatorWithoutSignature().computeMetrics(doc, ProvFactory.getFactory());


        TrafficLightResult result = (TrafficLightResult) metricsRecord.traffic;
        System.out.println("result = " + new ObjectMapper().writeValueAsString(result));

    }

    public void testMetrics2() throws JsonProcessingException {
        InteropFramework interop = new InteropFramework();
        Document doc = interop.readDocumentFromFile("src/test/resources/metrics/doc2.provn");
        MetricsRecord metricsRecord=(MetricsRecord)new MetricsCalculatorWithoutSignature().computeMetrics(doc, ProvFactory.getFactory());
        TrafficLightResult result = (TrafficLightResult) metricsRecord.traffic;
        System.out.println("result = " + new ObjectMapper().writeValueAsString(result));

    }

    public void testMetric3() throws JsonProcessingException {
        InteropFramework interop = new InteropFramework();
        Document doc = interop.readDocumentFromFile("src/test/resources/metrics/doc3.provn");
        MetricsRecord metricsRecord=(MetricsRecord)new MetricsCalculatorWithoutSignature().computeMetrics(doc, ProvFactory.getFactory());
        TrafficLightResult result = (TrafficLightResult) metricsRecord.traffic;
        System.out.println("result = " + new ObjectMapper().writeValueAsString(result));

    }
}
