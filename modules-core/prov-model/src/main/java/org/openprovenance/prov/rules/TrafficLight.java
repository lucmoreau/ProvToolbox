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
        String explanation="The percentage of root entities is the number of root entities divided by the total number of entities. Root entities are entities that are effect of any relation. This means that root entities are not generated, are not derived from other entities, are not attributed to any agent, etc. In other words, root entities have no provenance. It is desirable for the percentage of root entities to be small. To increase this metrics, add an attribution or the activity that generated a root entity (and associated agent), allows for information of the root entity to be found.";
        TrafficLightColor color;
        if (ratio<=10) {
            color = TrafficLightColor.GREEN;
        } else if (ratio<=30) {
            color =TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color, explanation);
    }

    public static TrafficLightResult forNonRootTriangle(EntityActivityDerivationCounter count) {
        int nonRootEntities = count.entities - count.rootEntities;
        double ratio=(100.0*count.entitiesWithTriangle/nonRootEntities);
        String comment="Percentage of non-root entities with triangle";
        String explanation="The percentage of non-root entities with triangle is the number of non-root entities with triangle divided by the total number of non-root entities. Non-root entities are entities that are not root entities. A triangle here is formed by an entity, the activity that generated it, and some entity it was derived from. It is desirable for the percentage of non-root entities with triangle to be high. This means that entities are properly connected to the entity they are derived from via some activity. To increase this metrics, for every derivation, add the linked activity, the generation and usage.";
        TrafficLightColor color;
        if (ratio>=90) {
            color = TrafficLightColor.GREEN;
        } else if (ratio>=70) {
            color = TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color, explanation);
    }

    public static TrafficLightResult forActivitiesTriangle(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.activitiesWithTriangle/count.activities);
        String comment="Percentage of activities with triangle";
        String explanation="The percentage of activities with triangle is the number of activities with triangle divided by the total number of activities. A triangle here is formed by an activity, the entity it generated, and some entity it used, with the two entities linked up by a derivation. It is desirable for the percentage of activities with triangle to be high. This means that activities are properly connected to the entity they generated and the entity they used. To increase this metrics, for every activity, add a linked derivation, with corresponding generation and usage.";
        TrafficLightColor color;
        if (ratio>=90) {
            color = TrafficLightColor.GREEN;
        } else if (ratio>=70) {
            color = TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color, explanation);
    }

    public static TrafficLightResult forFullyFormedTriangles(EntityActivityDerivationCounter count) {
        double ratio=(100.0*count.count3/count.triangle);
        String comment="Percentage of fully formed triangles";
        TrafficLightColor color;
        String explanation="The percentage of fully formed triangles is the number of fully formed triangles divided by the total number of triangles. A fully formed triangle is a triangle where a derivation is linked to an activity, the entity it generated and the usage of an entity. Such triangle cannot be inferred. It is desirable for the percentage of fully formed triangles to be high. This means that derivations are properly connected to activities, the entity they generated and the entity they used. To increase this metrics, for every derivation, add the linked activity, the generation and usage.";
        if (ratio>=90) {
            color = TrafficLightColor.GREEN;
        } else if (ratio>=70) {
            color = TrafficLightColor.ORANGE;
        } else {
            color = TrafficLightColor.RED;
        }
        return new TrafficLightResult(comment, ratio, color, explanation);
    }

    public static List<TrafficLightResult> getTrafficLightForCounts(EntityActivityDerivationCounter count) {
        return List.of(
                forRootEntities(count),
                forNonRootTriangle(count),
                forActivitiesTriangle(count),
                forFullyFormedTriangles(count)
        );
    }




}
