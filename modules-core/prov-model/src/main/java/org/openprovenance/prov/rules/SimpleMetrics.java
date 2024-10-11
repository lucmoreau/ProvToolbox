package org.openprovenance.prov.rules;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleMetrics {
    public Integer countEntities;
    public Integer countActivities;
    public Integer countAgents;
    public Integer countWasGeneratedBy;
    public Integer countUsed;
    public Integer countWasAssociatedWith;
    public Integer countWasAttributedTo;
    public Integer countActedOnBehalfOf;
    public Integer countWasDerivedFrom;
    public Integer countWasEndedBy;
    public Integer countWasInformedBy;
    public Integer countWasInvalidatedBy;
    public Integer countWasStartedBy;
    public Integer countWasInfluencedBy;
    public Integer countSpecializationOf;
    public Integer countAlternateOf;
    public Integer countHadMember;

    public String toCsv() {
        return nullable(countEntities) + "," + nullable(countActivities) + "," + nullable(countAgents) + "," + nullable(countWasGeneratedBy) + "," + nullable(countUsed) + ","
                + nullable(countWasAssociatedWith) + "," + nullable(countWasAttributedTo) + "," +  nullable(countActedOnBehalfOf) + "," + nullable(countWasDerivedFrom) + "," + nullable(countWasEndedBy) + ","
                + nullable(countWasInformedBy) + "," + nullable(countWasInvalidatedBy) + "," + nullable(countWasStartedBy) + "," + nullable(countWasInfluencedBy) + ","
                + nullable(countSpecializationOf) + "," + nullable(countAlternateOf) + "," + nullable(countHadMember);
    }

    private String nullable(Integer v) {
        return v == null ? "" : v.toString();
    }

    static public String headers() {
        return "countEntities,countActivities,countAgents,countWasGeneratedBy,countUsed,countWasAssociatedWith,countWasAttributedTo,countActedOnBehalfOf,countWasDerivedFrom,countWasEndedBy,countWasInformedBy,countWasInvalidatedBy,countWasStartedBy,countWasInfluencedBy,countSpecializationOf,countAlternateOf,countHadMember";
    }
}
