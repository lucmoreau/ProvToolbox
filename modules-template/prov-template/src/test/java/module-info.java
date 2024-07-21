module prov.template.test {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires junit;
    requires prov.model;
    requires prov.notation;
    requires zjsonpatch;
    requires org.apache.logging.log4j;
    requires prov.template;

    exports org.openprovenance.prov.template.expander.test;


}