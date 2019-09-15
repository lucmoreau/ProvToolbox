package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JLD_Qualified {


    @JsonIgnore
    public boolean isUnqualified();

}
