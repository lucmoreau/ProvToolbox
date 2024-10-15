package org.openprovenance.prov.rules;

import org.openprovenance.prov.rules.counters.EntityActivityDerivationCounter;

import java.util.List;

public class TrafficLight implements Ansi {
    // enumeration red, orange, green
    public enum TrafficLightColor {
        RED, ORANGE, GREEN
    }

    public static TrafficLightResult forRootEntities(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.rootEntities/count.entities);
        String comment="Percentage of root entities";
        TrafficLightColor color;
        if (ratio<=10) {
            color = TrafficLightColor.GREEN;
        } else if (ratio<=30) {
            color =TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color);
    }

    public static TrafficLightResult forNonRootTriangle(EntityActivityDerivationCounter count) {
        int nonRootEntities = count.entities - count.rootEntities;
        double ratio=(100.0*count.entitiesWithTriangle/nonRootEntities);
        String comment="Percentage of non-root entities with triangle";
        TrafficLightColor color;
        if (ratio>=90) {
            color = TrafficLightColor.GREEN;
        } else if (ratio>=70) {
            color = TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color);
    }

    public static TrafficLightResult forActivitiesTriangle(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.activitiesWithTriangle/count.activities);
        String comment="Percentage of activities with triangle";
        TrafficLightColor color;
        if (ratio>=90) {
            color = TrafficLightColor.GREEN;
        } else if (ratio>=70) {
            color = TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color);
    }

    public static TrafficLightResult forFullyFormedTriangles(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.count3/count.triangle);
        String comment="Percentage of fully formed triangles";
        TrafficLightColor color;
        if (ratio>=90) {
            color = TrafficLightColor.GREEN;
        } else if (ratio>=70) {
            color = TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color);
    }

    public static List<TrafficLightResult> getTrafficLight(EntityActivityDerivationCounter count) {
        return List.of(
                forRootEntities(count),
                forNonRootTriangle(count),
                forActivitiesTriangle(count),
                forFullyFormedTriangles(count)
        );
    }




}
