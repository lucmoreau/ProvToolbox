package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.vanilla.LangString;

public class MyLangString  extends LangString { //implements JLD_LangString {

    @JsonProperty(Constants.PROPERTY_STRING_VALUE)
    public String getValue() {
        return super.getValue();
    }

    @JsonProperty(Constants.PROPERTY_STRING_LANG)
    public String getLang() {
        return super.getLang();
    }

    public MyLangString(String value) {
        super(value);
    }

    public MyLangString(String value, String lang) {
        super(value, lang);
    }
}