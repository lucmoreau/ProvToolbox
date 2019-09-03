package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openprovenance.prov.model.Role;

import java.util.List;

public interface HasRole extends org.openprovenance.prov.model.HasRole{

    @JsonIgnore
    @JsonProperty("hadRole")
    List<Role> getRole();


}
