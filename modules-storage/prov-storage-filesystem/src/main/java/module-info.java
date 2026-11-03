module prov.storage.filesystem {
    requires tools.jackson.databind;
    requires org.apache.commons.io;
    requires prov.interop;
    requires prov.model;
    requires prov.storage.api;

    exports org.openprovenance.prov.storage.filesystem;
}