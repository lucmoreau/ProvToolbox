package org.openprovenance.prov.template.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.types.TypesRecordProcessor.numeral;

@JsonPropertyOrder({"@type", "count", "relation", "arguments"})
public class DescriptorTree implements Descriptor {
    final String category="tree";
    long count;
    String relation;
    List<Descriptor> arguments;


    public DescriptorTree(long count, String relation, List<Descriptor> arguments) {
        this.count=count;
        this.relation = relation;
        this.arguments = arguments;
    }

    @Override
    public String toText() {
        return numeral(count) + relation + arguments.stream().map(Descriptor::toText).collect(Collectors.joining(",", "(", ")"));
    }

    @Override
    @JsonProperty("@type")
    public String getCategory() {
        return category;
    }

    public long getCount() {
        return count;
    }

    public String getRelation() {
        return relation;
    }
/*
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "@type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DescriptorAtom.class, name = "atom"),
            @JsonSubTypes.Type(value = DescriptorTree.class, name = "tree")
    })

 */
    public List<Descriptor> getArguments() {
        return arguments;
    }
}
