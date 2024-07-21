module prov.template.compiler {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.squareup.javapoet;
    requires java.compiler;
    requires java.sql;
    requires json.schema.validator;
    requires maven.model;
    requires org.apache.commons.csv;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.commons.cli;
    requires org.apache.logging.log4j;
    requires plexus.utils;
    requires prov.model;
    requires prov.notation;
    requires prov.template;


    exports org.openprovenance.prov.template.compiler;
    exports org.openprovenance.prov.template.compiler.configuration;
    exports org.openprovenance.prov.template.compiler.util;
    exports org.openprovenance.prov.template.descriptors;
    exports org.openprovenance.prov.template.log2prov;

    opens org.openprovenance.prov.template.compiler.util;
    opens schema;

}