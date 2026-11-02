package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.Role;

import java.util.List;

public interface HasRole extends org.openprovenance.prov.model.HasRole{

    @JsonIgnore
    List<Role> getRole();


}
