package org.openprovenance.prov.json;


import java.util.List;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.xml.UncheckedTestException;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
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
		//Namespace.withThreadNamespace(doc.getNamespace());

		try {
		    convert.writeDocument(doc, file);
		} catch (Exception e) {
			throw new UncheckedTestException(e);
		}
	}
	
	@Override
        public boolean checkSchema(String name) {
	    return true;
        }
	
	@Override
	public void doCheckSchema1(String file) {
	    
	    try {
		RhinoValidator validator = new RhinoValidator();
		List<String> errors = validator.validate(file,false);
		if (errors.size() == 0)	{
		    //System.out.println("JSON is compliant with the PROV-JS Schema");
		    assertTrue(true);
		} else {
		    System.err.println("validation error(s) for " + file);
		    System.err.println(errors.size() + " validation error(s):");
		    for (String error : errors) {
			System.out.println(error);
		    }
		    assertTrue(false);
		}
	    } catch (Exception e) {
		System.err.println("Parsing failed: " + e.getMessage());
	    }
	}

	

}
