/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    export interface AtomConstructor {
        newRole(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Role;

        newLocation(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Location;

        newType(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Type;

        newLabel(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Label;

        newInternationalizedString(s?: any, lang?: any): any;

        newValue(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Value;

        newOther(elementName: org.openprovenance.prov.model.QualifiedName, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Other;
    }
}

