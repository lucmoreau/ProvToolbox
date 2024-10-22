package org.openprovenance.prov.model.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.rules.Ansi;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.TrafficLight;
import org.openprovenance.prov.rules.TrafficLightResult;
import org.openprovenance.prov.rules.counters.EntityActivityDerivationCounter;

public class TriangleTest extends TestCase implements Ansi {

    final PC1FullTest pc1Maker=new PC1FullTest();
    final ProvFactory pFactory=pc1Maker.pFactory;
    final Document pc1=pc1Maker.makePC1FullGraph(pFactory);

    public TriangleTest(String testName) {
            super(testName);
    }

    public String trafficLightRootEntities(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.rootEntities/count.entities);
        if (ratio<=10) {
            return green("green");
        }
        if (ratio<=30) {
            return orange("orange");
        }
        return red("red");
    }

    public String trafficLightNonRootTriangle(EntityActivityDerivationCounter count) {
        int nonRootEntities = count.entities - count.rootEntities;
        double ratio=(100.0*count.entitiesWithTriangle/nonRootEntities);
        if (ratio>=90) {
            return green("green");
        }
        if (ratio>=70) {
            return orange("orange");
        }
        return red("red");
    }
    public String trafficLightActivitiesTriangle(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.activitiesWithTriangle/count.activities);
        if (ratio>=90) {
            return green("green");
        }
        if (ratio>=70) {
            return orange("orange");
        }
        return red("red");
    }
    public String trafficLightFullyFormedTriangles(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.count3/count.triangle);
        if (ratio>=90) {
            return green("green");
        }
        if (ratio>=70) {
            return orange("orange");
        }


        return red("red");
    }


    public void testTriangle() {
        System.out.println("TriangleTest.testTriangle");
        Rules rules=new Rules();
        EntityActivityDerivationCounter count=rules.countDerivationsAndGenerationsAndUsages(new IndexedDocument(pFactory,pc1));

        System.out.println(count);

        int nonRootEntities = count.entities - count.rootEntities;
        display(TrafficLight.forRootEntities(count));
        display(TrafficLight.forNonRootTriangle(count));
        display(TrafficLight.forActivitiesTriangle(count));
        System.out.printf("Percentage of triangles with respect to activities in triangle: %1$.2f%%\n", (100.0*count.triangle/count.activitiesWithTriangle));
        display(TrafficLight.forFullyFormedTriangles(count));
        System.out.printf("Percentage of triangles (bare wdf: 0): %1$.3f%%\n", (100.0*count.count0/count.triangle));
        System.out.printf("Percentage of triangles (wdf with 1): %1$.3f%%\n", (100.0*count.count1/count.triangle));
        System.out.printf("Percentage of triangles (wdf with 2): %1$.3f%%\n", (100.0*count.count2/count.triangle));


    }

    private void display(TrafficLightResult trafficLightResult) {
        System.out.printf("%1$s %2$.2f%% (%3$s)\n",trafficLightResult.comment,trafficLightResult.ratio,trafficLightResult.colorAsString);
    }

}
