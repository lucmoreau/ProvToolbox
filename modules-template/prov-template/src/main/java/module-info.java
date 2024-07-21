module prov.template {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.logging.log4j;
    requires prov.model;
    requires java.xml;

    exports org.openprovenance.prov.template.json;
    exports org.openprovenance.prov.template.json.deserializer;
    exports org.openprovenance.prov.template.expander;
    exports org.openprovenance.prov.template.expander.exception;
    exports org.openprovenance.prov.template.expander.meta;
    exports org.openprovenance.prov.template.expander.deprecated;

}