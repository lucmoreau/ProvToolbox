package org.openprovenance.prov.model.extension;

import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.QualifiedRelation;
import org.openprovenance.prov.model.Relation;

public interface QualifiedAlternateOf extends Identifiable, HasLabel, HasType, HasOther, Relation, AlternateOf, QualifiedRelation {

}
