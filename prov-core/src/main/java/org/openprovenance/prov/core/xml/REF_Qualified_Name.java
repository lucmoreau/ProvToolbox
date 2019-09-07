package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.xml.serialization.deserial.CustomQualifiedNameDeserializerAsXmlAttribute;
import org.openprovenance.prov.core.xml.serialization.serial.CustomQualifiedNameSerializerAsXmlAttribute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotationsInside
@JsonDeserialize(using = CustomQualifiedNameDeserializerAsXmlAttribute.class)
@JsonSerialize(using = CustomQualifiedNameSerializerAsXmlAttribute.class)

public @interface REF_Qualified_Name {
}
