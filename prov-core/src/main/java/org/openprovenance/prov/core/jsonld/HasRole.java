package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Role;

import java.util.List;

public interface HasRole extends org.openprovenance.prov.model.HasRole{

    @JsonIgnore
    List<Role> getRole();


}
