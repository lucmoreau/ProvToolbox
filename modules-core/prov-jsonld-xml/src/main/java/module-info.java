module prov.jsonld.xml {
    requires com.ctc.wstx;
    requires com.fasterxml.jackson.annotation;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.dataformat.xml;
    requires java.xml;
    requires org.apache.logging.log4j;
    requires org.codehaus.stax2;
    requires prov.model;

    exports org.openprovenance.prov.core.xml;
    exports org.openprovenance.prov.core.xml.serialization;
    exports org.openprovenance.prov.core.xml.serialization.deserial;
    exports org.openprovenance.prov.core.xml.serialization.serial;

    opens org.openprovenance.prov.core.xml.serialization.serial;

}