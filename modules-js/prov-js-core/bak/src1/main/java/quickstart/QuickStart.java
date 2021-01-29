package quickstart;

import static def.dom.Globals.alert;
import static def.dom.Globals.document;
import static def.jquery.Globals.$;

import java.util.ArrayList;
import java.util.List;

import def.dom.HTMLCollectionOf;
import def.dom.HTMLDivElement;
import def.js.Array;
import jsweet.util.StringTypes;

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
		alert(a.toString());

		HTMLCollectionOf<HTMLDivElement> nodeList = document.getElementsByTagName(StringTypes.div);
		for (HTMLDivElement element : nodeList) {
			element.innerText = "Hello again in vanilla JS";
		}
	}

}
