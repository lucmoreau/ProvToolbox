package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JSON_Qualified {


    @JsonIgnore
    public boolean isUnqualified();

}
