package org.openprovenance.prov.rdf;

import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.contextaware.ContextAwareRepository;

/** This class is a Sesame-based rdf graph builder. Ideally, it should
 * be abstracted in a generic interface and specific implementations that
 * are based on sesame/jena/other 
 */

public class GraphBuilder {

    final ContextAwareRepository manager;

    public GraphBuilder(ContextAwareRepository manager) {
	this.manager = manager;
    }

    public void assertStatement(org.openrdf.model.Statement stmnt) {
	try {
	    if (currentContext==null) {
		manager.getConnection().add(stmnt);
	    } else {
		manager.getConnection().add(stmnt, currentContext);
	    }
	} catch (org.openrdf.repository.RepositoryException e) {
	}
    }

    public StatementImpl createDataProperty(org.openrdf.model.Resource r,
					    QName pred, 
					    LiteralImpl literalImpl) {
	return new StatementImpl(r, qnameToURI(pred), literalImpl);
    }

    public StatementImpl createDataProperty(QName subject, 
                                            QName pred,
					    LiteralImpl literalImpl) {
	return createDataProperty(qnameToResource(subject), pred, literalImpl);
    }

    public StatementImpl createObjectProperty(org.openrdf.model.Resource r,
					      QName pred, 
					      QName object) {
	return new StatementImpl(r, qnameToURI(pred), qnameToURI(object));
    }

    public StatementImpl createObjectProperty(QName subject, 
                                              QName pred,
					      QName object) {
	return new StatementImpl(qnameToResource(subject), qnameToURI(pred),
				 qnameToResource(object));
    }

    public URIImpl qnameToURI(QName qname) {
	if (qname.getNamespaceURI().equals(NamespacePrefixMapper.XSD_NS)) {
	    return new URIImpl(NamespacePrefixMapper.XSD_HASH_NS
		    + qname.getLocalPart());
	} else {
	    return new URIImpl(qname.getNamespaceURI() + qname.getLocalPart());

	}
    }

    public Resource qnameToResource(QName qname) {
	if (qname.getNamespaceURI().equals(NamespacePrefixMapper.XSD_NS)) {
	    return new URIImpl(NamespacePrefixMapper.XSD_HASH_NS
		    + qname.getLocalPart());
	}
	if (isBlankName(qname)) {
	    return new BNodeImpl(qname.getLocalPart());
	} else {
	    return new URIImpl(qname.getNamespaceURI() + qname.getLocalPart());

	}
    }

    boolean isBlankName(QName name) {
	return name.getNamespaceURI().equals(NamespacePrefixMapper.TOOLBOX_NS)
		&& name.getPrefix().equals("_");
    }

    static int blankCounter = 0;

    public QName newBlankName() {
	blankCounter++;
	return newToolboxQName("blank" + blankCounter);
    }

    public static QName newToolboxQName(String local) {
	return new QName(NamespacePrefixMapper.TOOLBOX_NS, local, "_");
    }

    void setContext() {
	currentContext=null;
    }
    
    private Resource currentContext=null;
    
    void setContext(URIImpl uri) {
	currentContext=uri;
	
    }

}
