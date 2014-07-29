package org.openprovenance.prov.tutorial.tutorial1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;


/** A little provenance goes a long way.
 * 
 * @author lavm
 * @see http://blog.provbook.org/2013/10/11/a-little-provenance-goes-a-long-way/
 */
public class Little {

    public static final String PROVBOOK_NS = "http://www.provbook.org";
    public static final String PROVBOOK_PREFIX = "provbook";
    
    public static final String PRIM_NS = "http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX = "prim";
      
    private final ProvFactory pFactory;

    public Little(ProvFactory pFactory) {
	this.pFactory=pFactory;
    }
        
    
    public QualifiedName q(String n) {
   	//return new org.openprovenance.prov.xml.QualifiedName(PC1_NS, n, PC1_PREFIX);
   	return pFactory.newQualifiedName(PROVBOOK_NS, n, PROVBOOK_PREFIX);
    }
 
 



    public Document makeLittle(ProvFactory pFactory) {

	

	Entity quote = pFactory.newEntity(q("a-little-provenance-goes-a-long-way"));
	quote.setValue(pFactory.newValue("A little provenance goes a long way", pFactory.getName().QNAME_XSD_HASH_STRING));
	
	Agent paul = pFactory.newAgent(q("Paul"), "Paul Groth");
	Agent luc = pFactory.newAgent(q("Luc"), "Luc Moreau");

	WasAttributedTo attr1=pFactory.newWasAttributedTo(null, quote.getId(), paul.getId());
	WasAttributedTo attr2=pFactory.newWasAttributedTo(null, quote.getId(), luc.getId());

	Entity original=pFactory.newEntity(pFactory.newQualifiedName("http://www.cs.rpi.edu/~hendler/", "LittleSemanticsWeb.html", "hendler"));
	
	WasDerivedFrom wdf=pFactory.newWasDerivedFrom(quote.getId(), original.getId());
	
		Document document = pFactory.newDocument();
		document.getStatementOrBundle().addAll(Arrays.asList(new StatementOrBundle[] {quote, paul, luc, attr1, attr2, original, wdf}));
		
	document.setNamespace(Namespace.gatherNamespaces(document));

	return document;
    }









}
