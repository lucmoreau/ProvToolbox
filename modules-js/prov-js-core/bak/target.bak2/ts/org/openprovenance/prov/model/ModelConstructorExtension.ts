/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Interface for constructing concrete representations of the PROV data model
     * @class
     */
    export interface ModelConstructorExtension {
        newQualifiedAlternateOf(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedAlternateOf;

        newQualifiedSpecializationOf(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

        newQualifiedHadMember(id: org.openprovenance.prov.model.QualifiedName, c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedHadMember;
    }
}

