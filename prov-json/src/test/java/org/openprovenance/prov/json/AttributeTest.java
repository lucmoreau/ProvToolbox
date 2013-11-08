package org.openprovenance.prov.json;


import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.UncheckedTestException;

abstract public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
        final Converter convert=new Converter();


	public AttributeTest(String testName) {
		super(testName);
	}
	

	@Override
	public String extension() {
		return ".json";
	}

	
	@Override
	public Document readDocument(String file) {
	    try {
	        return convert.readDocument(file);
	    } catch (Exception e) {
	        throw new UncheckedTestException(e);
	    }
	}
	
	
	@Override
	public void writeDocument(Document doc, String file) {
		try {
		    convert.writeDocument(doc, file);
		} catch (Exception e) {
			throw new UncheckedTestException(e);
		}
	}
	

}
