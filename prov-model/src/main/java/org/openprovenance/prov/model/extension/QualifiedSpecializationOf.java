package org.openprovenance.prov.model.extension;

import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.QualifiedRelation;
import org.openprovenance.prov.model.Relation;
import org.openprovenance.prov.model.SpecializationOf;

public interface QualifiedSpecializationOf extends Identifiable, HasLabel, HasType, HasOther, Relation, SpecializationOf, QualifiedRelation {

}
