package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.Location;

import java.util.List;

public interface HasLocation extends org.openprovenance.prov.model.HasLocation{


    @JsonIgnore
    List<Location> getLocation();

}
