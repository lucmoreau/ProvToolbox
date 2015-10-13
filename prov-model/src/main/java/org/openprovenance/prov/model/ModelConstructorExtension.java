package org.openprovenance.prov.model;
import java.util.Collection;

import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;


/** Interface for constructing concrete representations of the PROV data model */

public interface ModelConstructorExtension {
 
    public QualifiedAlternateOf newQualifiedAlternateOf(QualifiedName id, QualifiedName e2, QualifiedName e1, Collection<Attribute> attributes);
    public QualifiedSpecializationOf newQualifiedSpecializationOf(QualifiedName id, QualifiedName e2, QualifiedName e1, Collection<Attribute> attributes);
    public QualifiedHadMember newQualifiedHadMember(QualifiedName id,
                                                    QualifiedName c,
                                                    Collection<QualifiedName> e,
                                                    Collection<Attribute> attributes);
 
}
