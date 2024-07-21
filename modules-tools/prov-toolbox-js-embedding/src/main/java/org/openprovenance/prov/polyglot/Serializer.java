package org.openprovenance.prov.polyglot;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;


public class Serializer {
    public void serialize(Document doc, String file)  {
        try {
            new InteropFramework().writeDocument(file, doc);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    };

}
