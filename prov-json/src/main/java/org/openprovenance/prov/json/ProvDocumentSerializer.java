package org.openprovenance.prov.json;

import java.io.BufferedWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.BeanTraversal;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.ModelConstructor;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.QNameExport;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.ValueConverter;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasAttributedTo;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInfluencedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ProvDocumentSerializer implements JsonSerializer<Document> {
	@Override
	public JsonElement serialize(Document src, Type typeOfSrc,
			JsonSerializationContext context) {
		ProvFactory pFactory=new ProvFactory();
		JSONConstructor jsonConstructor = new JSONConstructor(pFactory);
		BeanTraversal bt = new BeanTraversal(jsonConstructor, pFactory, new ValueConverter(pFactory));
		Object jsonStructure = jsonConstructor.getJSONStructure();
		return context.serialize(jsonStructure);
	}
}
