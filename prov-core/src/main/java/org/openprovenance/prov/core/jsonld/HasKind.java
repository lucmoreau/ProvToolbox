package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.StatementOrBundle;

import java.util.List;

public interface HasKind{

    @JsonIgnore
    StatementOrBundle.Kind getKind();


}
