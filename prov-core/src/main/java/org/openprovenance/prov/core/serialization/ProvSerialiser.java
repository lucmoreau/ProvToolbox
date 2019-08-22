package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.openprovenance.prov.core.QualifiedName;
import org.openprovenance.prov.core.serialization.CustomAttributeSerializer;
import org.openprovenance.prov.core.serialization.CustomKindSerializer;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameSerializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.IOException;
import java.io.OutputStream;

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {

    @Override
    public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
        ObjectMapper mapper = new ObjectMapper();
        if (formatted) mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module =
                new SimpleModule("CustomKindSerializer",
                        new Version(1, 0, 0, null, null, null));
        module.addSerializer(StatementOrBundle.Kind.class, new CustomKindSerializer());
        module.addSerializer(QualifiedName.class, new CustomQualifiedNameSerializer());
        module.addSerializer(Attribute.class, new CustomAttributeSerializer());

        mapper.registerModule(module);

        try {
            mapper.writeValue(out,document);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }
    }

}
