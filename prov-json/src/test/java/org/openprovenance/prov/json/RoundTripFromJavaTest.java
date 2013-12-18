package org.openprovenance.prov.json;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.UncheckedTestException;

/**
 * Round trip testing JavaBean -> PROV-JSON -> JavaBean
 */
public class RoundTripFromJavaTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest
{
        final Converter convert=new Converter(pFactory);
    	
	public RoundTripFromJavaTest(String testName) {
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
	
	@Override
	public boolean checkSchema(String name) {
	    return false;
	}

	
	
}