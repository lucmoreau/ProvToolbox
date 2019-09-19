package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.Type;

import java.util.List;

public interface HasType extends org.openprovenance.prov.model.HasType{

    @JsonIgnore
    List<Type> getType();

}
