
package org.openprovenance.prov.polyglot.test;



import java.io.IOException;


import org.junit.Test;
import org.openprovenance.prov.polyglot.Embedding;

public class JsTest {
	
	@Test
	public void test() throws IOException {
		// add tests here
		Embedding.main(new String[]{"src/test/resources/org/openprovenance/prov/polyglot/js/hello.js"});
		Embedding.main(new String[]{"src/test/resources/org/openprovenance/prov/polyglot/js/builder.js"});
	}

}
