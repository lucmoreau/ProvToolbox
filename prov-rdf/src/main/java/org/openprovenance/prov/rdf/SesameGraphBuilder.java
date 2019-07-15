package org.openprovenance.prov.rdf;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedNameUtils;
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

/**
 * @author lavm
 *
 */
public class SesameGraphBuilder implements GraphBuilder<Resource,LiteralImpl,org.openrdf.model.Statement> {

    final ContextAwareRepository manager;
    private final ProvFactory pFactory;

    final QualifiedNameUtils qnU;

    public SesameGraphBuilder(ContextAwareRepository manager, ProvFactory pFactory) {
	this.manager = manager;
	this.pFactory=pFactory;
	this.qnU=new QualifiedNameUtils();
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#assertStatement(org.openrdf.model.Statement)
     */
    @Override
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


    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#createDataProperty(java.lang.Object, org.openprovenance.prov.model.QualifiedName, java.lang.Object)
     */
    @Override
    public org.openrdf.model.Statement createDataProperty(org.openrdf.model.Resource r,
					    QualifiedName pred, 
					    LiteralImpl literalImpl) {
	return new StatementImpl(r, qualifiedNameToURI(pred), literalImpl);
    }

     /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#createDataProperty(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.lang.Object)
     */
    @Override
    public org.openrdf.model.Statement createDataProperty(QualifiedName subject, 
                                            QualifiedName pred,
					    LiteralImpl literalImpl) {
	return createDataProperty(qualifiedNameToResource(subject), pred, literalImpl);
    }


    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#createObjectProperty(java.lang.Object, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName)
     */
    @Override
    public org.openrdf.model.Statement createObjectProperty(org.openrdf.model.Resource r,
                                                            QualifiedName pred, 
                                                            QualifiedName object) {
	return new StatementImpl(r, qualifiedNameToURI(pred), qualifiedNameToURI(object));
    }

  
    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#createObjectProperty(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName)
     */
    @Override
    public  org.openrdf.model.Statement createObjectProperty(QualifiedName subject, 
                                                             QualifiedName pred,
                                                             QualifiedName object) {
	return new StatementImpl(qualifiedNameToResource(subject), 
	                         qualifiedNameToURI(pred),
				 qualifiedNameToResource(object));
    }

   

    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#qualifiedNameToURI(org.openprovenance.prov.model.QualifiedName)
     */
    @Override
    public URIImpl qualifiedNameToURI(QualifiedName name) {
	String unescapedLocalName = qnU.unescapeProvLocalName(name.getLocalPart());
	return new URIImpl(name.getNamespaceURI() + unescapedLocalName);
    }


    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#qualifiedNameToResource(org.openprovenance.prov.model.QualifiedName)
     */
    @Override
    public Resource qualifiedNameToResource(QualifiedName name) {
	String unescapedLocalName = qnU.unescapeProvLocalName(name.getLocalPart());
	if (isBlankName(name)) {
	    return new BNodeImpl(unescapedLocalName);
	} else {
	    return new URIImpl(name.getNamespaceURI() + unescapedLocalName);

	}
    }


    boolean isBlankName(QualifiedName name) {
	return name.getNamespaceURI().equals(NamespacePrefixMapper.TOOLBOX_NS)
		&& name.getPrefix().equals("_");
    }

    static int blankCounter = 0;

    /* (non-Javadoc)
     * @see org.openprovenance.prov.rdf.GraphBuilder#newBlankName()
     */
    @Override
    public QualifiedName newBlankName() {
	blankCounter++;
	return newToolboxQualifiedName("blank" + blankCounter);
    }

    public QualifiedName newToolboxQualifiedName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.TOOLBOX_NS, local, "_");
    }

    public void setContext() {
	currentContext=null;
    }
    
    private Resource currentContext=null;
    
    public void setContext(Resource uri) {
	currentContext=uri;
	
    }
    
    @Override
    public LiteralImpl newLiteral(String value, QualifiedName type) {
	 return new LiteralImpl(value,
	                        qualifiedNameToURI(type));
    }
    @Override
    public LiteralImpl newLiteral(String value, String lang) {
	if (lang==null) {
	    return new LiteralImpl(value);
	} else {
	 return new LiteralImpl(value,lang);
	}

    }

}
