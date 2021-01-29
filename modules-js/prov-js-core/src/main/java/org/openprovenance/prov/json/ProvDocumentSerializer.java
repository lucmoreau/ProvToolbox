package org.openprovenance.prov.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.lang.reflect.Type;
import java.util.Map;

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
		BeanTraversal bt = new BeanTraversal(jsonConstructor, pFactory);
		bt.doAction(doc);
		Map<String, Object> jsonStructure = jsonConstructor.getJSONStructure();
		return context.serialize(jsonStructure);
	}

}
