package org.openprovenance.prov.validation.report.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.validation.report.Dependencies;
import org.openprovenance.prov.validation.report.MergeReport;
import org.openprovenance.prov.validation.report.SpecializationReport;
import org.openprovenance.prov.validation.report.TypeReport;

import java.util.List;

@JsonPropertyOrder({ "@context"})

public interface ValidationReportInterface {
    @JsonProperty("@context")
    Namespace getNamespace();
    @JsonProperty("@context")
    void setNamespace(Namespace namespace);



    List<Dependencies> getCycle();

    List<Dependencies> getNonStrictCycle();

    List<MergeReport> getFailedMerge();

    List<MergeReport> getSuccessfulMerge();

    List<MergeReport> getQualifiedNameMismatch();

    SpecializationReport getSpecializationReport();

    TypeReport getTypeReport();

    boolean isDeposited();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    org.openprovenance.prov.vanilla.QualifiedName getId();
}
