module prov.model {
    requires jakarta.xml.bind;
    requires java.sql;
    requires java.xml;
    requires org.apache.commons.collections4;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.annotation;

    exports org.openprovenance.prov.model;
    exports org.openprovenance.prov.vanilla;
    exports org.openprovenance.prov.bookptm;
    exports org.openprovenance.prov.model.interop;
    exports org.openprovenance.prov.model.extension;
    exports org.openprovenance.prov.model.exception;
    exports org.openprovenance.prov.model.builder;
    exports org.openprovenance.prov.configuration;
    exports org.openprovenance.prov.rules;

    exports org.openprovenance.apache.commons.lang;

    opens org.openprovenance.prov.vanilla;
    opens org.openprovenance.prov.model;
    opens org.openprovenance.prov.model.interop;
    opens org.openprovenance.prov.rules;
    exports org.openprovenance.prov.rules.counters;


}