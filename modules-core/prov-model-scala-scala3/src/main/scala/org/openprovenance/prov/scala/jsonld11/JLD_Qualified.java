package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JLD_Qualified {


    @JsonIgnore
    public boolean isUnqualified();

}
