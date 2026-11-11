module prov.template.compiler {
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;

    requires com.squareup.javapoet;
    requires java.compiler;
    requires java.sql;
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
    requires com.networknt.schema;

    exports org.openprovenance.prov.template.compiler;
    exports org.openprovenance.prov.template.compiler.past;
    exports org.openprovenance.prov.template.compiler.past.annotations;
    exports org.openprovenance.prov.template.compiler.past.checker;
    exports org.openprovenance.prov.template.compiler.past.emitter;
    exports org.openprovenance.prov.template.compiler.past.emitter.registry;
    exports org.openprovenance.prov.template.compiler.past.type;
    exports org.openprovenance.prov.template.compiler.configuration;
    exports org.openprovenance.prov.template.compiler.common;
    exports org.openprovenance.prov.template.compiler.util;
    exports org.openprovenance.prov.template.descriptors;
    exports org.openprovenance.prov.template.log2prov;
    // T32: the generated log2prov builders implement these interfaces (ProxyClientAccessorInterface), from their own module or the class path
    exports org.openprovenance.prov.template.log2prov.interfaces;
    // T31: data-protection methods (R4): the SPI an application implements, and the policy's data classes
    exports org.openprovenance.prov.template.compiler.privacy;
    opens org.openprovenance.prov.template.compiler.privacy;
    uses org.openprovenance.prov.template.compiler.privacy.PrivacyMethod;
    // qualified export: the SQL composer is internal, opened only to its unit test (T-38)
    exports org.openprovenance.prov.template.compiler.sql to prov.template.compiler.test;

    opens org.openprovenance.prov.template.compiler.util;
    opens org.openprovenance.prov.template.compiler;
    opens org.openprovenance.prov.template.compiler.past.emitter.registry;
    opens schema;

}
