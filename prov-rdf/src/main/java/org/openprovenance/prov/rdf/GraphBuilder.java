package org.openprovenance.prov.rdf;

import javax.xml.namespace.QName;

import org.openprovenance.prov.model.QualifiedName;

public interface GraphBuilder<RESOURCE, LITERAL, STATEMENT> {

    public abstract void assertStatement(STATEMENT stmnt);

    public abstract STATEMENT createDataProperty(RESOURCE r, QName pred,
						 LITERAL literal);

    public abstract STATEMENT createDataProperty(QName subject, QName pred,
						 LITERAL literal);

    public abstract STATEMENT createObjectProperty(RESOURCE r, QName pred,
						   QName object);

    public abstract STATEMENT createObjectProperty(QName subject, QName pred,
						   QName object);
    
    public abstract STATEMENT createObjectProperty(QName subject, QName pred,
						   QualifiedName object);

    public abstract LITERAL newLiteral(String value, QName type);

    public abstract LITERAL newLiteral(String value, String lang);

    public abstract RESOURCE qnameToURI(QName qname);

    public abstract RESOURCE qnameToResource(QName qname);

    public abstract QName newBlankName();

    public void setContext();

    public void setContext(RESOURCE uri);

}