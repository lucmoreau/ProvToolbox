package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomDeferredQualifiedNameDeserializer;
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




    @JsonProperty("prefix")
    @JsonDeserialize(using = CustomNamespacePrefixDeserializer.class)
    Map<String,String> getPrefix();

    @JsonProperty("@id")
    @JsonDeserialize(using = CustomDeferredQualifiedNameDeserializer.class)
    QualifiedName getId();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Entity.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Entity.class )
    Map<org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.Entity> getEntity();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Activity.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Activity.class )
    List<org.openprovenance.prov.model.Activity> getActivity();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Agent.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Agent.class )
    List<org.openprovenance.prov.model.Agent> getAgent();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = Used.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = Used.class )
    List<org.openprovenance.prov.model.Used> getUsed();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasGeneratedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasGeneratedBy.class )
    List<org.openprovenance.prov.model.WasGeneratedBy> getWasGeneratedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInvalidatedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInvalidatedBy.class )
    List<org.openprovenance.prov.model.WasInvalidatedBy> getWasInvalidatedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasAssociatedWith.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasAssociatedWith.class )
    List<WasAssociatedWith> getWasAssociatedWith();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasAttributedTo.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasAttributedTo.class )
    List<WasAttributedTo> getWasAttributedTo();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = ActedOnBehalfOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = ActedOnBehalfOf.class )
    List<ActedOnBehalfOf> getActedOnBehalfOf() ;

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasStartedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasStartedBy.class )
    List<WasStartedBy> getWasStartedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasEndedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasEndedBy.class )
    List<WasEndedBy> getWasEndedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInformedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInformedBy.class )
    List<WasInformedBy> getWasInformedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasInfluencedBy.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasInfluencedBy.class )
    List<WasInfluencedBy> getWasInfluencedBy();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = AlternateOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = AlternateOf.class )
    List<AlternateOf> getAlternateOf();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = SpecializationOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = SpecializationOf.class )
    List<SpecializationOf> getSpecializationOf();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = HadMember.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = HadMember.class )
    List<HadMember> getHadMember();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = WasDerivedFrom.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = WasDerivedFrom.class )
    List<WasDerivedFrom> getWasDerivedFrom();

    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedSpecializationOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedSpecializationOf.class )
    List<QualifiedSpecializationOf> getQualifiedSpecializationOf();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedAlternateOf.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedAlternateOf.class )
    List<QualifiedAlternateOf> getQualifiedAlternateOf();


    @JsonSerialize(keyUsing = CustomQualifiedNameSerializerAsField.class, contentAs = QualifiedHadMember.class)
    @JsonDeserialize(keyUsing = CustomKeyDeserializer.class, contentAs = QualifiedHadMember.class )
    List<QualifiedHadMember> getQualifiedHadMember();



}
