package org.openprovenance.prov.validation.report.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.validation.report.Dependencies;
import org.openprovenance.prov.validation.report.MergeReport;
import org.openprovenance.prov.validation.report.SpecializationReport;
import org.openprovenance.prov.validation.report.TypeReport;

import java.util.List;

@JsonPropertyOrder({ "@context"})

public interface ValidationReportInterface {
    List<Dependencies> getCycle();

    List<Dependencies> getNonStrictCycle();

    List<MergeReport> getFailedMerge();

    List<MergeReport> getSuccessfulMerge();

    List<MergeReport> getQualifiedNameMismatch();

    SpecializationReport getSpecializationReport();

    TypeReport getTypeReport();

    boolean isDeposited();

    QualifiedName getId();
}
