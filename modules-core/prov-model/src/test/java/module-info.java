module prov.model.test {
    requires jakarta.xml.bind;
    requires java.sql;
    requires java.xml;
    requires org.apache.commons.collections4;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;

    requires prov.model;

    requires junit;

    exports org.openprovenance.prov.model.test;
}