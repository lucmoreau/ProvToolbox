module prov.dot {
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires prov.model;
    requires jakarta.xml.bind;
    exports org.openprovenance.prov.dot;
    exports org.openprovenance.prov.viz;
    exports org.openprovenance.prov.viz.templates;
}
