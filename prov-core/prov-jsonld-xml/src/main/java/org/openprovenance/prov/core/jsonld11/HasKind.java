package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.StatementOrBundle;

public interface HasKind{

    @JsonIgnore
    StatementOrBundle.Kind getKind();


}
