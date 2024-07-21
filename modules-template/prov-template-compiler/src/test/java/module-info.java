module prov.template.compiler.test{
    requires com.fasterxml.jackson.databind;
    requires json.schema.validator;
    requires junit;
    requires org.apache.commons.io;
    requires prov.model;
    requires prov.notation;
    requires prov.template;
    requires prov.template.compiler;

    exports org.openprovenance.prov.template.compiler.test;
    exports org.example.remote;
    exports org.example.local;
    exports org.example;
}