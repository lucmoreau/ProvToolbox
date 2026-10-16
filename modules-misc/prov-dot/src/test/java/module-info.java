module prov.dot.test {
    requires junit;

    requires prov.model;
    requires prov.notation;
    requires prov.dot;
    requires prov.model.test;
    requires prov.notation.test;
    requires jakarta.xml.bind;
    exports org.openprovenance.prov.dot.test;
    exports org.openprovenance.prov.viz.test;
}