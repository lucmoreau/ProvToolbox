package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.json.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.core.vanilla.*;
import org.openprovenance.prov.model.Namespace;

import java.util.List;

@JsonPropertyOrder({"prefix", "defaultNamespace"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JSON_SortedBundle {
    @JsonFilter("nsFilter")
    @JsonProperty("context")
    Namespace getNamespace();


    @JsonSerialize(contentAs = Entity.class)
    @JsonDeserialize(contentAs = Entity.class)
    public List<org.openprovenance.prov.model.Entity> getEntity();

    @JsonSerialize(contentAs = Activity.class)
    @JsonDeserialize(contentAs = Activity.class)
    public List<org.openprovenance.prov.model.Activity> getActivity();

    @JsonSerialize(contentAs = Agent.class)
    @JsonDeserialize(contentAs = Agent.class)
    public List<org.openprovenance.prov.model.Agent> getAgent();

    @JsonSerialize(contentAs = Used.class)
    @JsonDeserialize(contentAs = Used.class)
    public List<org.openprovenance.prov.model.Used> getUsed();

    @JsonSerialize(contentAs = WasGeneratedBy.class)
    @JsonDeserialize(contentAs = WasGeneratedBy.class)
    public List<org.openprovenance.prov.model.WasGeneratedBy> getWasGeneratedBy();

    @JsonSerialize(contentAs = WasInvalidatedBy.class)
    @JsonDeserialize(contentAs = WasInvalidatedBy.class)
    public List<org.openprovenance.prov.model.WasInvalidatedBy> getWasInvalidatedBy();

    @JsonSerialize(contentAs = WasAssociatedWith.class)
    @JsonDeserialize(contentAs = WasAssociatedWith.class)
    public List<WasAssociatedWith> getWasAssociatedWith();

    @JsonSerialize(contentAs = WasAttributedTo.class)
    @JsonDeserialize(contentAs = WasAttributedTo.class)
    public List<WasAttributedTo> getWasAttributedTo();

    @JsonSerialize(contentAs = ActedOnBehalfOf.class)
    @JsonDeserialize(contentAs = ActedOnBehalfOf.class)
    public List<ActedOnBehalfOf> getActedOnBehalfOf() ;

    @JsonSerialize(contentAs = WasStartedBy.class)
    @JsonDeserialize(contentAs = WasStartedBy.class)
    public List<WasStartedBy> getWasStartedBy();

    @JsonSerialize(contentAs = WasEndedBy.class)
    @JsonDeserialize(contentAs = WasEndedBy.class)
    public List<WasEndedBy> getWasEndedBy();

    @JsonSerialize(contentAs = WasInformedBy.class)
    @JsonDeserialize(contentAs = WasInformedBy.class)
    public List<WasInformedBy> getWasInformedBy();

    @JsonSerialize(contentAs = WasInfluencedBy.class)
    @JsonDeserialize(contentAs = WasInfluencedBy.class)
    public List<WasInfluencedBy> getWasInfluencedBy();

    @JsonSerialize(contentAs = AlternateOf.class)
    @JsonDeserialize(contentAs = AlternateOf.class)
    public List<AlternateOf> getAlternateOf();

    @JsonSerialize(contentAs = SpecializationOf.class)
    @JsonDeserialize(contentAs = SpecializationOf.class)
    public List<SpecializationOf> getSpecializationOf();

    @JsonSerialize(contentAs = HadMember.class)
    @JsonDeserialize(contentAs = HadMember.class)
    public List<HadMember> getHadMember();

    @JsonSerialize(contentAs = WasDerivedFrom.class)
    @JsonDeserialize(contentAs = WasDerivedFrom.class)
    public List<WasDerivedFrom> getWasDerivedFrom();

    @JsonSerialize(contentAs = QualifiedSpecializationOf.class)
    @JsonDeserialize(contentAs = QualifiedSpecializationOf.class)
    public List<QualifiedSpecializationOf> getQualifiedSpecializationOf();


    @JsonSerialize(contentAs = QualifiedAlternateOf.class)
    @JsonDeserialize(contentAs = QualifiedAlternateOf.class)
    public List<QualifiedAlternateOf> getQualifiedAlternateOf();


    @JsonSerialize(contentAs = QualifiedHadMember.class)
    @JsonDeserialize(contentAs = QualifiedHadMember.class)
    public List<QualifiedHadMember> getQualifiedHadMember();

    @JsonProperty("@id")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getId();


}
