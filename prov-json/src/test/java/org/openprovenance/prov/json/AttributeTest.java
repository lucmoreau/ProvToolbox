package org.openprovenance.prov.json;


import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.UncheckedTestException;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
        final Converter convert=new Converter(pFactory);
        final JsonSchema schema;

	public AttributeTest(String testName) throws ProcessingException, IOException {
		super(testName);
		final JsonNode schemaJSON = JsonLoader.fromPath("schema/prov-json-schema-v4.js");
	        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	        this.schema = factory.getJsonSchema(schemaJSON);
	}
	
	
	@Override
	public String extension() {
		return ".json";
	}

	@Override	
	public boolean checkSchema(String name)  {
	    return true;
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
	public void doCheckSchema1(String file) {
	    
	    try {
		final JsonNode fileJSON = JsonLoader.fromPath(file);
	        ProcessingReport report = schema.validate(fileJSON);
	        if (!report.isSuccess())
	            System.err.println(report);
	        assertTrue(report.isSuccess());

	    } catch (IOException e) {
		System.err.println("Cannot load file: " + e.getMessage());
	    } catch (ProcessingException e) {
		System.err.println("Parsing failed: " + e.getMessage());
	    }
	}

	

}
