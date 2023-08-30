package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomKeyDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomKeyDeserializerNoAction;
import org.openprovenance.prov.core.json.serialization.serial.CustomQualifiedNameSerializerAsField;
import org.openprovenance.prov.vanilla.*;

import java.util.Map;

@JsonPropertyOrder({ "prefix", "defaultNamespace"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JSON_SortedDocument {
   // @JsonFilter("nsFilter")
   // @JsonProperty("context")
   // Namespace getNamespace();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Entity.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Entity.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.Entity> getEntity();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Activity.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Activity.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.Activity> getActivity();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Agent.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Agent.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.Agent> getAgent();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Used.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Used.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.Used> getUsed();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasGeneratedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasGeneratedBy.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.WasGeneratedBy> getWasGeneratedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInvalidatedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInvalidatedBy.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.WasInvalidatedBy> getWasInvalidatedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasAssociatedWith.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasAssociatedWith.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasAssociatedWith> getWasAssociatedWith();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasAttributedTo.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasAttributedTo.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasAttributedTo> getWasAttributedTo();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = ActedOnBehalfOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = ActedOnBehalfOf.class )
    public Map<org.openprovenance.prov.model.QualifiedName, ActedOnBehalfOf> getActedOnBehalfOf() ;

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasStartedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasStartedBy.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasStartedBy> getWasStartedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasEndedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasEndedBy.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasEndedBy> getWasEndedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInformedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInformedBy.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasInformedBy> getWasInformedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInfluencedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInfluencedBy.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasInfluencedBy> getWasInfluencedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = AlternateOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = AlternateOf.class )
    public Map<org.openprovenance.prov.model.QualifiedName, AlternateOf> getAlternateOf();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = SpecializationOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = SpecializationOf.class )
    public Map<org.openprovenance.prov.model.QualifiedName, SpecializationOf> getSpecializationOf();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = HadMember.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = HadMember.class )
    public Map<org.openprovenance.prov.model.QualifiedName, HadMember> getHadMember();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasDerivedFrom.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasDerivedFrom.class )
    public Map<org.openprovenance.prov.model.QualifiedName, WasDerivedFrom> getWasDerivedFrom();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedSpecializationOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedSpecializationOf.class )
    public Map<org.openprovenance.prov.model.QualifiedName, QualifiedSpecializationOf> getQualifiedSpecializationOf();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedAlternateOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedAlternateOf.class )
    public Map<org.openprovenance.prov.model.QualifiedName, QualifiedAlternateOf> getQualifiedAlternateOf();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedHadMember.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedHadMember.class )
    public Map<org.openprovenance.prov.model.QualifiedName, QualifiedHadMember> getQualifiedHadMember();



    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Bundle.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializerNoAction.class, contentAs = Bundle.class )
    public Map<org.openprovenance.prov.model.QualifiedName, Bundle> getBundle();



}
