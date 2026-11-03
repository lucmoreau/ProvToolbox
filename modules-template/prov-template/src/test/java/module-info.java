module prov.template.test {
    requires tools.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires junit;
    requires prov.model;
    requires prov.notation;
    requires zjsonpatch;
    requires org.apache.logging.log4j;
    requires prov.template;
    requires prov.jsonld;

    exports org.openprovenance.prov.template.core.test;


}