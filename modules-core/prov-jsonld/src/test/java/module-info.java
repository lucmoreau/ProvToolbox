module prov.jsonld.test {
    requires com.fasterxml.jackson.databind;
    requires java.xml;
    requires junit;
    requires org.apache.logging.log4j;
    requires prov.model;
    requires prov.model.test;
    requires prov.jsonld;

    exports org.openprovenance.prov.core.json.test;
    exports org.openprovenance.prov.core.jsonld11.test;

}