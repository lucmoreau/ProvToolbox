module prov.template {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;

    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.logging.log4j;
    requires prov.model;
    requires java.xml;


    exports org.openprovenance.prov.template.utils;
    exports org.openprovenance.prov.template.json;
    exports org.openprovenance.prov.template.json.deserializer;
    exports org.openprovenance.prov.template.core;
    exports org.openprovenance.prov.template.core.exception;
    exports org.openprovenance.prov.template.core.ttf;
    exports org.openprovenance.prov.template.core.deprecated;

}