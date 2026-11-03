package org.openprovenance.prov.core.xml.serialization.deserial;


import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.dataformat.xml.deser.FromXmlParser;
import org.openprovenance.prov.core.xml.serialization.MisnamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.vanilla.Bundle;
import tools.jackson.databind.ValueDeserializer;

import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.getNamespace;
import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.newNamespace;
import static org.openprovenance.prov.core.xml.serialization.deserial.DeserializerUtil.setNamespace;

public class CustomBundleDeserializer extends ValueDeserializer<Bundle> {
    static final ProvFactory pf = org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    @Override
    public Bundle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        FromXmlParser xmlParser=(FromXmlParser)jsonParser;

        // get the document namespace from the context
        Namespace docNs = getNamespace();
        //System.out.println("document ns " + docNs.getPrefixes());

        // create a new namespace for the bundle, and define its parent as docNS
        Namespace bunNs=newNamespace();
        bunNs.setParent(docNs);

        MisnamedBundle mbun= jsonParser.readValueAs(MisnamedBundle.class);

        // attach bunNS to mbun
        mbun.setNamespace(bunNs);

        // restore document namespace
        setNamespace(docNs);

        // do the conversion, fixing the bundle id
        org.openprovenance.prov.model.Bundle theBun = mbun.toBundle(pf);

        return (Bundle) theBun;
    }
}

