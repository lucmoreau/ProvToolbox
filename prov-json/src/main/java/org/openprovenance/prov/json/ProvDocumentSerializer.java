package org.openprovenance.prov.json;

import java.lang.reflect.Type;

import org.openprovenance.prov.notation.BeanTreeConstructor;
import org.openprovenance.prov.xml.OldBeanTraversal;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ProvDocumentSerializer implements JsonSerializer<Document> {

	@Override
	public JsonElement serialize(Document src, Type typeOfSrc,
			JsonSerializationContext context) {
		OldBeanTraversal bt=new OldBeanTraversal(new BeanTreeConstructor(ProvFactory.getFactory(),new JSONConstructor()));
		Object jsonStructure = bt.convert(src);
		return context.serialize(jsonStructure);
	}

}
