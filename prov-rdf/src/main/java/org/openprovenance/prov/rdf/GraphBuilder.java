package org.openprovenance.prov.rdf;

import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

public class GraphBuilder {

	final ElmoManager manager;
	public GraphBuilder(ElmoManager manager) {
		this.manager = manager;
	}

	public void assertStatement(org.openrdf.model.Statement stmnt) {
		try {
			((org.openrdf.elmo.sesame.SesameManager) manager).getConnection()
					.add(stmnt);
		} catch (org.openrdf.repository.RepositoryException e) {
		}
	}

	public StatementImpl createDataProperty(org.openrdf.model.Resource r,
			QName pred, LiteralImpl literalImpl) {
		return new StatementImpl(r, qnameToURI(pred), literalImpl);
	}
	public StatementImpl createDataProperty(QName subject, QName pred,
			LiteralImpl literalImpl) {
		return createDataProperty(qnameToResource(subject), pred, literalImpl);
	}

	public StatementImpl createObjectProperty(org.openrdf.model.Resource r,
			QName pred, QName object) {
		return new StatementImpl(r, qnameToURI(pred), qnameToURI(object));
	}
	public StatementImpl createObjectProperty(QName subject, QName pred,
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

	QName newBlankName() {
		blankCounter++;
		return newToolboxQName("blank" + blankCounter);
	}

	public static QName newToolboxQName(String local) {
		return new QName(NamespacePrefixMapper.TOOLBOX_NS, local, "_");
	}

	void setContext() {
		((SesameManager) manager).getConnection().setAddContexts();
	}

	void setContext(URIImpl uri) {
		((SesameManager) manager).getConnection().setAddContexts(uri);
	}

}
