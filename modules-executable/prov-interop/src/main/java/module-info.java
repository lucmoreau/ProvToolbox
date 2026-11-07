module prov.interop {
    requires tools.jackson.databind;
    requires jakarta.ws.rs;
    requires org.apache.commons.cli;
    requires org.apache.logging.log4j;
    requires prov.model;
    requires prov.generator;
    requires prov.notation;
    requires prov.template;
    requires prov.template.compiler;
    requires prov.jsonld;
    requires prov.dot;

    exports org.openprovenance.prov.interop;


}