package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Descriptor {
    @JsonIgnore
    DescriptorTypes getDescriptorType ();
}
