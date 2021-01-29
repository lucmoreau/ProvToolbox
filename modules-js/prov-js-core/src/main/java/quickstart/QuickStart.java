package quickstart;
import static def.dom.Globals.*;
import static def.jquery.Globals.$;

import java.util.ArrayList;
import java.util.List;

import def.dom.HTMLCollectionOf;
import def.dom.HTMLDivElement;
import def.js.Array;
import jsweet.util.StringTypes;
import org.openprovenance.prov.json.ProvDeserialiser;
import org.openprovenance.prov.json.ProvSerialiser;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.vanilla.ProvFactory;

/**
 * This class is used within the webapp/index.html file.
 */
public class QuickStart {

	public static void main(String[] args) {
		// you can use regular Java API
		List<String> l = new ArrayList<>();
		l.add("Hello");
		l.add("world");
		// and you can also use regular JavaScript APIs
		Array<String> a = new Array<>();
		a.push("Hello", "world");
		// use of jQuery with the jQuery candy
		$("#target").text(l.toString());
		// use of the JavaScript DOM API
		//alert(a.toString());
		console.log(a.toString());

		HTMLCollectionOf<HTMLDivElement> nodeList = document.getElementsByTagName(StringTypes.div);
		for (HTMLDivElement element : nodeList) {
			element.innerText = "Hello again in vanilla JS";
		}

		ProvFactory pf=new ProvFactory();
		Entity e=pf.newEntity(pf.newQualifiedName("http://ex.org/", "e1", "ex"));
		Activity activity=pf.newActivity(pf.newQualifiedName("http://ex.org/", "a1", "ex"));
		Agent agent=pf.newAgent(pf.newQualifiedName("http://ex.org/", "ag1", "ex"));
		WasGeneratedBy wgb=pf.newWasGeneratedBy(null,e.getId(),activity.getId());
		pf.addType(e,pf.newType(pf.getName().newProvQualifiedName("SoftwareAgent"),pf.getName().PROV_QUALIFIED_NAME));
		activity.setStartTime(pf.newTimeNow());
		console.log(e);
		console.log(e.getKind());
		Document doc=pf.newDocument();
		doc.getStatementOrBundle().add(e);
		doc.getStatementOrBundle().add(activity);
		doc.getStatementOrBundle().add(agent);
		doc.getStatementOrBundle().add(wgb);
		Namespace ns=new Namespace();
		ns.addKnownNamespaces();
		ns.register("ex", "http://ex.org/");
		doc.setNamespace(ns);
		final String docString = new ProvSerialiser(pf).serialiseDocument(doc);
		console.log(docString);
		Document doc2=new ProvDeserialiser(pf).deserialiseDocument(docString);
		console.log(doc2);
		console.log(doc.equals(doc2));
	}

}
