package org.openprovenance.prov.json;

import java.lang.reflect.Type;

import javax.xml.namespace.QName;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.QNameExport;
import org.openprovenance.prov.xml.ProvFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ProvDocumentSerializer implements JsonSerializer<Document> {
    @Override
    public JsonElement serialize(final Document doc, 
                                 Type typeOfSrc,
				 JsonSerializationContext context) {
	ProvFactory pFactory = new ProvFactory();
	QNameExport qExport = new QNameExport() {
	    @Override
	    public String qnameToString(QName qname) {
		return doc.getNamespace().qnameToString(qname);
	    }
	};
	JSONConstructor jsonConstructor = new JSONConstructor(qExport);
	BeanTraversal bt = new BeanTraversal(jsonConstructor, 
	                                     pFactory);
	bt.convert(doc);
	Object jsonStructure = jsonConstructor.getJSONStructure();
	return context.serialize(jsonStructure);
    }
}
