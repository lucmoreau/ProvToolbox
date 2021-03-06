package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomKeyDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomNamespacePrefixDeserializer;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.core.json.serialization.serial.CustomQualifiedNameSerializerAsField;
import org.openprovenance.prov.vanilla.*;

import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"prefix", "defaultNamespace"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JSON_SortedBundle {
 //   @JsonFilter("nsFilter")
//    @JsonProperty("context")
  //  Namespace getNamespace();



    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Entity.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Entity.class )
    public Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.Entity> getEntity();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Activity.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Activity.class )
    public List<org.openprovenance.prov.model.Activity> getActivity();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Agent.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Agent.class )
    public List<org.openprovenance.prov.model.Agent> getAgent();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Used.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Used.class )
    public List<org.openprovenance.prov.model.Used> getUsed();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasGeneratedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasGeneratedBy.class )
    public List<org.openprovenance.prov.model.WasGeneratedBy> getWasGeneratedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInvalidatedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInvalidatedBy.class )
    public List<org.openprovenance.prov.model.WasInvalidatedBy> getWasInvalidatedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasAssociatedWith.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasAssociatedWith.class )
    public List<WasAssociatedWith> getWasAssociatedWith();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasAttributedTo.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasAttributedTo.class )
    public List<WasAttributedTo> getWasAttributedTo();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = ActedOnBehalfOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = ActedOnBehalfOf.class )
    public List<ActedOnBehalfOf> getActedOnBehalfOf() ;

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasStartedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasStartedBy.class )
    public List<WasStartedBy> getWasStartedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasEndedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasEndedBy.class )
    public List<WasEndedBy> getWasEndedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInformedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInformedBy.class )
    public List<WasInformedBy> getWasInformedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInfluencedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInfluencedBy.class )
    public List<WasInfluencedBy> getWasInfluencedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = AlternateOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = AlternateOf.class )
    public List<AlternateOf> getAlternateOf();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = SpecializationOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = SpecializationOf.class )
    public List<SpecializationOf> getSpecializationOf();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = HadMember.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = HadMember.class )
    public List<HadMember> getHadMember();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasDerivedFrom.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasDerivedFrom.class )
    public List<WasDerivedFrom> getWasDerivedFrom();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedSpecializationOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedSpecializationOf.class )
    public List<QualifiedSpecializationOf> getQualifiedSpecializationOf();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedAlternateOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedAlternateOf.class )
    public List<QualifiedAlternateOf> getQualifiedAlternateOf();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedHadMember.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedHadMember.class )
    public List<QualifiedHadMember> getQualifiedHadMember();


    @JsonIgnore
    @JsonProperty("@id")
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getId();

    @JsonProperty("prefix")
    @JsonDeserialize(using = CustomNamespacePrefixDeserializer.class)
    public Map<String,String> getPrefix();


}
