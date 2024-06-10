package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JLD_Qualified {

    @JsonIgnore
    boolean isUnqualified();

}
