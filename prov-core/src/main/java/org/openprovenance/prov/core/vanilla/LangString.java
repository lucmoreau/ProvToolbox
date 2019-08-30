package org.openprovenance.prov.core.vanilla;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.core.jsonld.serialization.Constants;


import java.util.Optional;

@JsonPropertyOrder({ "value", "lang" })
public class LangString implements org.openprovenance.prov.model.LangString, Equals, HashCode, ToString, Constants {

    @JsonInclude(JsonInclude.Include.NON_NULL)

    Optional<String> lang;
    String value;

    /* Private constructor for serialization purpose. */
     private LangString() {
        this.lang=Optional.empty();
    }

    public LangString(String value) {
        this.value=value;
        this.lang=Optional.empty();
    }

    public LangString(String value, String lang) {
        this.value=value;
        this.lang=Optional.ofNullable(lang);
    }

    @JsonProperty(Constants.PROPERTY_STRING_VALUE)
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value=value;
    }

    @Override
    @JsonProperty(Constants.PROPERTY_STRING_LANG)
    public String getLang() {
        return lang.orElse(null);
    }

    @Override
    public void setLang(String value) {
      this.lang=Optional.ofNullable(value);
    }


    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof LangString)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final LangString that = ((LangString) object);
        equalsBuilder.append(this.getValue(), that.getValue());
        equalsBuilder.append(this.getLang(), that.getLang());
    }

    public boolean equals(Object object) {
        if (!(object instanceof LangString)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getValue());
        hashCodeBuilder.append(this.getLang());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theValue;
            theValue = this.getValue();
            toStringBuilder.append("value", theValue);
        }
        {
            String theLang;
            theLang = this.getLang();
            toStringBuilder.append("lang", theLang);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
