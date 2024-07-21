module prov.interop {
    requires com.fasterxml.jackson.databind;
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
    requires prov.jsonld.xml;

    exports org.openprovenance.prov.interop;


}