package org.openprovenance.prov.template.utils;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ProvVariables {
    public String entity;
    public String activity;
    public String agent;

    @JsonProperty("entity.wasGeneratedBy")
    public String wasGeneratedByEntity;
    @JsonProperty("entity.specializationOf")
    public String specializationOfEntity;
    @JsonProperty("entity.used")
    public String usedEntity;
    @JsonProperty("entity.wasDerivedFrom")
    public String wasDerivedFromEntity;
    @JsonProperty("plan.wasAssociatedWith")
    public String wasAssociatedWithEntity;


    public String plan;
    public String collection;

    public String bundle;

    public String used;
    public String wasGeneratedBy;
    public String wasDerivedFrom;
    public String wasAttributedTo;
    public String wasAssociatedWith;
    public String wasInvalidatedBy;
    public String actedOnBehalfOf;
    public String specializationOf;
    public String alternateOf;
    public String wasStartedBy;
    public String wasEndedBy;
    public String hadMember;
    public String wasInformedBy;

    public String time;


    public Map<String,String> attributes;
    public Map<String,String> values;
}
