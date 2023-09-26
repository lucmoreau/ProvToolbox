package org.openprovenance.prov.core.xml.serialization.deserial;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.codehaus.stax2.validation.ValidationContext;
import org.openprovenance.prov.core.xml.serialization.MisnamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.vanilla.Bundle;

import javax.xml.namespace.NamespaceContext;
import java.io.IOException;

import static org.openprovenance.prov.core.xml.serialization.deserial.CustomThreadConfig.PROVX_CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.xml.serialization.deserial.CustomThreadConfig.getAttributes;
import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.getNamespace;

public class CustomBundleDeserializer extends JsonDeserializer<Bundle> {
    static final ProvFactory pf = org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        // get the document namespace from the context
        Namespace docNs = getNamespace(deserializationContext);
        //System.out.println("document ns " + docNs.getPrefixes());

        MisnamedBundle mbun= jsonParser.readValueAs(MisnamedBundle.class);

        // restore document namespace
        getAttributes().get().put(PROVX_CONTEXT_KEY_NAMESPACE,docNs);

        // patch bundle ns
        mbun.getNamespace().setParent(docNs);
        //System.out.println("bundle ns " + mbun.getNamespace());


        // do the conversion, fixing the bundle id
        org.openprovenance.prov.model.Bundle theBun = mbun.toBundle(pf);

        return (Bundle) theBun;
    }
}

