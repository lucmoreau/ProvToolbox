package org.openprovenance.prov.rdf;


import org.openprovenance.prov.model.QualifiedName;

public interface GraphBuilder<RESOURCE, LITERAL, STATEMENT> {

    public abstract void assertStatement(STATEMENT stmnt);

    public abstract STATEMENT createDataProperty(RESOURCE r, 
                                                 QualifiedName pred,
						 LITERAL literal);

    public abstract STATEMENT createDataProperty(QualifiedName subject, 
                                                 QualifiedName pred,
						 LITERAL literal);

    public abstract STATEMENT createObjectProperty(RESOURCE r, QualifiedName pred,
						   QualifiedName object);
    
    public abstract STATEMENT createObjectProperty(QualifiedName subject, 
                                                   QualifiedName pred,
						   QualifiedName object);

    public abstract LITERAL newLiteral(String value, QualifiedName type);

    public abstract LITERAL newLiteral(String value, String lang);

    public abstract RESOURCE qualifiedNameToURI(QualifiedName qname);

    public abstract RESOURCE qualifiedNameToResource(QualifiedName qname);

    public abstract QualifiedName newBlankName();

    public void setContext();

    public void setContext(RESOURCE uri);

}