package org.openprovenance.prov.template.utils;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;


public class VariableChecker {
    final ProvVariables pv;

    public VariableChecker(ProvVariables pv) {
        this.pv = pv;
    }


    public boolean checkActivityId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.activity));
    }

    private String ifExists(String regexp) {
        if (regexp==null) return ".*";
        else return regexp;
    }

    public boolean checkUsedId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.used));
    }

    public boolean checkEntityId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.entity));
    }

    public boolean checkWasGeneratedByEntityId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.entity)) || id.getLocalPart().matches(ifExists(pv.wasGeneratedByEntity));
    }

    public boolean checkUsedEntityId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.entity)) || id.getLocalPart().matches(ifExists(pv.usedEntity));
    }

    public boolean checkWasDerivedFromEntityId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.entity)) || id.getLocalPart().matches(ifExists(pv.wasDerivedFromEntity));
    }



    public boolean checkSpecializationOfEntityId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.entity)) || id.getLocalPart().matches(ifExists(pv.specializationOfEntity));
    }


    public boolean checkWasGeneratedById(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasGeneratedBy));
    }

    public boolean checkWasAssociatedId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasAssociatedWith));
    }

    public boolean checkWasDerivedFromId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasDerivedFrom));
    }

    public boolean checkWasAttributedToId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasAttributedTo));
    }

    public boolean checkAgentId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.agent));
    }

    public boolean checkPlanId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.plan)) || id.getLocalPart().matches(ifExists(pv.wasAssociatedWithEntity));
    }

    public boolean checkWasInvalidatedById(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasInvalidatedBy));
    }

    public boolean checkQualifiedSpecializationId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.specializationOf));
    }

    public boolean checkBundleId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.bundle));
    }

    public boolean checkTimeVariable(QualifiedName qn) {
        return qn == null || qn.getLocalPart().matches(ifExists(pv.time));
    }

    public boolean checkQualifiedHadMemberId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.hadMember));
    }

    public boolean checkCollectionId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.collection));
    }

    public boolean checkQualifiedAlternateId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.alternateOf));
    }

    public boolean checkActedOnBehalfOfId(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.actedOnBehalfOf));
    }

    public boolean checkWasStartedById(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasStartedBy));
    }

    public boolean checkWasEndedById(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasEndedBy));
    }
    public boolean checkWasInformedById(QualifiedName id) {
        return id == null || id.getLocalPart().matches(ifExists(pv.wasInformedBy));
    }


    public boolean checkPropertyValuePair(QualifiedName attribute, QualifiedName value, Statement s) {
        return (attribute == null || attribute.getLocalPart().matches(ifExists(pv.attributes.get(nameOf(s.getKind()))))) &&
                (value == null || value.getLocalPart().matches(ifExists(pv.values.get(nameOf(s.getKind())))));
    }


    public String nameOf(Statement.Kind kind) {
        return switch (kind) {
            case PROV_ENTITY -> "entity";
            case PROV_ACTIVITY -> "activity";
            case PROV_AGENT -> "agent";
            case PROV_USAGE -> "used";
            case PROV_GENERATION -> "wasGeneratedBy";
            case PROV_INVALIDATION -> "wasInvalidatedBy";
            case PROV_START -> "wasStartedBy";
            case PROV_END -> "wasEndedBy";
            case PROV_COMMUNICATION -> "wasInformedBy";
            case PROV_DERIVATION -> "wasDerivedFrom";
            case PROV_ASSOCIATION -> "wasAssociatedWith";
            case PROV_ATTRIBUTION -> "wasAttributedTo";
            case PROV_DELEGATION -> "actedOnBehalfOf";
            case PROV_INFLUENCE -> "wasInfluencedBy";
            case PROV_ALTERNATE -> "alternateOf";
            case PROV_SPECIALIZATION -> "specializationOf";
            case PROV_MEMBERSHIP -> "hadMember";
            case PROV_BUNDLE -> "bundle";
            case PROV_MENTION -> throw new UnsupportedOperationException("No attribute/value for mention");
            case PROV_DICTIONARY_INSERTION -> throw new UnsupportedOperationException("No attribute/value for dictionaryInsertion");
            case PROV_DICTIONARY_REMOVAL -> throw  new UnsupportedOperationException("No attribute/value for dictionaryRemoval");
            case PROV_DICTIONARY_MEMBERSHIP -> throw new UnsupportedOperationException("No attribute/value for dictionaryMembership");
        };
    }

    @Override
    public String toString() {
        return "VariableChecker{" +
                "pv=" + pv.checkerName +
                '}';
    }
}
