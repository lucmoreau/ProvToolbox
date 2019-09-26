package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface XML_Qualified {

    @JsonIgnore
    public boolean isUnqualified();

}
