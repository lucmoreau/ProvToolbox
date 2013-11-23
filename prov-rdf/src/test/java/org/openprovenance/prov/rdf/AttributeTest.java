package org.openprovenance.prov.rdf;


import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.UncheckedTestException;
import org.openrdf.rio.RDFFormat;

public class AttributeTest extends org.openprovenance.prov.xml.AttributeTest {
	final Utility u = new Utility();


	public AttributeTest(String testName) {
		super(testName);
	}
	

	@Override
	public String extension() {
		return ".trig";
	}

	    
		@Override
		public Document readDocument(String file)
		{
			try
			{
				Document doc2 = u.parseRDF(file);
				return doc2;
			} catch (Exception e)
			{
				throw new UncheckedTestException(e);
			}
		}

		@Override
		public void writeDocument(Document doc, String file)
		{
			try
			{
				u.dumpRDF(pFactory, doc, RDFFormat.TRIG, file);
			} catch (Exception e)
			{
				e.printStackTrace();
				throw new UncheckedTestException(e);
			}
		}
	
		@Override
	        public boolean checkSchema(String name) {
	                return false;
	        }


}
