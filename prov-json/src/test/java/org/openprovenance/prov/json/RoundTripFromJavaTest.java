package org.openprovenance.prov.json;

import java.io.IOException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.UncheckedTestException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;

/**
 * Round trip testing JavaBean -> PROV-JSON -> JavaBean
 */
public class RoundTripFromJavaTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest
{
        final Converter convert=new Converter(pFactory);
        final JsonSchema schema;
    	
	public RoundTripFromJavaTest(String testName) throws IOException, ProcessingException {
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
	        if (name.endsWith("association2" + extension())
	                || name.endsWith("end1" + extension())
	                || name.endsWith("end4" + extension())
	                || name.endsWith("delegation1" + extension())
	                || name.endsWith("delegation2" + extension())
	                || name.endsWith("dictionaryRemoval1-S" + extension())
	                || name.endsWith("dictionaryRemoval1-M" + extension())
	                || name.endsWith("dictionaryRemoval2-S" + extension())
	                || name.endsWith("dictionaryRemoval2-M" + extension())
	                || name.endsWith("attribution1" + extension())
	                || name.endsWith("attribution2" + extension())
	                || name.endsWith("mention1" + extension())
	                || name.endsWith("derivation1" + extension())
	                || name.endsWith("derivation2" + extension())
	                || name.endsWith("derivation9" + extension())
	                || name.endsWith("communication1" + extension())
	                || name.endsWith("communication2" + extension())
	                || name.endsWith("influence1" + extension())
	                || name.endsWith("influence2" + extension())
	                || name.endsWith("start1" + extension())
	                || name.endsWith("start4" + extension())
	                || name.endsWith("usage1" + extension())
	                || name.endsWith("dictionaryInsertion1-S" + extension())
	                || name.endsWith("dictionaryInsertion1-M" + extension())
	                || name.endsWith("dictionaryInsertion2-S" + extension())
	                || name.endsWith("dictionaryInsertion2-M" + extension())
	                || name.contains("scruffy")
	                ) {
	            return false;
	        }
	    return true;
	}

	@Override
	public void doCheckSchema1(String file) {
	    
	    try {
		final JsonNode fileJSON = JsonLoader.fromPath(file);
	        ProcessingReport report = schema.validate(fileJSON);
	        if (!report.isSuccess()) {
	            System.err.println("Cannot validate " + file + " against the PROV-JSON schema.");
	            System.err.println(report);
	        }
	        assertTrue(report.isSuccess());

	    } catch (IOException e) {
		System.err.println("Cannot load file: " + e.getMessage());
	    } catch (ProcessingException e) {
		System.err.println("Parsing failed: " + e.getMessage());
	    }
	}	
	
}