module prov.jsonld {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.datatransfer;
    requires java.xml;
    requires org.apache.logging.log4j;
    requires prov.model;

    opens org.openprovenance.prov.core.json;
    opens org.openprovenance.prov.core.json.serialization;
    opens org.openprovenance.prov.core.json.serialization.serial;
    opens org.openprovenance.prov.core.json.serialization.deserial;
    opens org.openprovenance.prov.core.jsonld11;
    opens org.openprovenance.prov.core.jsonld11.serialization;
    opens org.openprovenance.prov.core.jsonld11.serialization.serial;
    opens org.openprovenance.prov.core.jsonld11.serialization.deserial;

    exports org.openprovenance.prov.core.json;
    exports org.openprovenance.prov.core.json.serialization;
    exports org.openprovenance.prov.core.json.serialization.serial;
    exports org.openprovenance.prov.core.json.serialization.deserial;
    exports org.openprovenance.prov.core.jsonld11;
    exports org.openprovenance.prov.core.jsonld11.serialization;
    exports org.openprovenance.prov.core.jsonld11.serialization.serial;
    exports org.openprovenance.prov.core.jsonld11.serialization.deserial;
}