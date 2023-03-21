package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Descriptor {
    String SQL_TABLE = "@sql.table";
    String SQL_NEW_INPUTS = "@sql.new.inputs";

    @JsonIgnore
    DescriptorTypes getDescriptorType ();
}
