package org.openprovenance.prov.json;

import java.lang.reflect.Type;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.QualifiedNameExport;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ProvDocumentSerializer implements JsonSerializer<Document> {
    final ProvFactory pFactory;

    public ProvDocumentSerializer(ProvFactory pFactory) {
	this.pFactory=pFactory;
    }
    
    @Override
    public JsonElement serialize(final Document doc, 
                                 Type typeOfSrc,
				 JsonSerializationContext context) {
	JSONConstructor jsonConstructor = new JSONConstructor(pFactory.getName());
	BeanTraversal bt = new BeanTraversal(jsonConstructor, 
	                                     pFactory);
	bt.convert(doc);
	Object jsonStructure = jsonConstructor.getJSONStructure();
	return context.serialize(jsonStructure);
    }
}
