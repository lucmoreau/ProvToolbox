package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.prov.model.LangString;

import java.util.List;

public interface HasLabel extends org.openprovenance.prov.model.HasLabel{

    @JsonIgnore
    List<LangString> getLabel();


}
