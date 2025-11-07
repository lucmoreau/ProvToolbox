package org.openprovenance.prov.template.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

import java.util.List;

import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_NS;
import static org.openprovenance.prov.template.expander.ExpandUtil.isVariable;

public class StatementChecker implements org.openprovenance.prov.model.StatementAction {
    static Logger logger = LogManager.getLogger(StatementChecker.class);
    private final VariableChecker vc;
    private final ProvUtilities u=new ProvUtilities();
    private final boolean strict;

    public StatementChecker(VariableChecker vc, boolean strict) {
        this.vc = vc;
        this.strict = strict;
    }

    public StatementChecker(VariableChecker vc) {
        this.vc = vc;
        this.strict = false;
    }

    @Override
    public void doAction(Activity s) {
        assertTrue(vc.checkActivityId(s.getId()), s, "Invalid Activity id", s.getId());
        doAttributes(s.getOther(), s);
    }


    @Override
    public void doAction(Used s) {
        assertTrue(vc.checkUsedId(s.getId()), s, "Invalid Used id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkEntityId(s.getEntity()), s, "Invalid Entity id", s.getEntity());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(WasStartedBy s) {
        assertTrue(vc.checkWasStartedById(s.getId()), s, "Invalid WasStartedBy id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkEntityId(s.getTrigger()), s, "Invalid Entity id", s.getTrigger());
        assertTrue(vc.checkEntityId(s.getStarter()), s, "Invalid Activity id", s.getStarter());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(Agent s) {
        assertTrue(vc.checkAgentId(s.getId()), s, "Invalid Agent id", s.getId());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(AlternateOf s) {
        assertTrue(vc.checkEntityId(s.getAlternate1()), s, "Invalid Entity id", s.getAlternate1());
        assertTrue(vc.checkEntityId(s.getAlternate2()), s, "Invalid Entity id", s.getAlternate2());
    }

    @Override
    public void doAction(WasAssociatedWith s) {
        assertTrue(vc.checkWasAssociatedId(s.getId()), s, "Invalid WasAssociatedWith id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkAgentId(s.getAgent()), s, "Invalid Agent id", s.getAgent());
        assertTrue(vc.checkPlanId(s.getPlan()), s, "Invalid Plan id", s.getPlan());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(WasAttributedTo s) {
        assertTrue(vc.checkWasAttributedToId(s.getId()), s, "Invalid WasAttributedTo id", s.getId());
        assertTrue(vc.checkAgentId(s.getAgent()), s, "Invalid Agent id", s.getAgent());
        assertTrue(vc.checkEntityId(s.getEntity()), s, "Invalid Entity id", s.getEntity());
        doAttributes(s.getOther(), s);
    }


    @Override
    public void doAction(ActedOnBehalfOf s) {
        assertTrue(vc.checkActedOnBehalfOfId(s.getId()), s, "Invalid ActedOnBehalfOf id", s.getId());
        assertTrue(vc.checkAgentId(s.getDelegate()), s, "Invalid Agent id", s.getDelegate());
        assertTrue(vc.checkAgentId(s.getResponsible()), s, "Invalid Agent id", s.getResponsible());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(WasDerivedFrom s) {
        assertTrue(vc.checkWasDerivedFromId(s.getId()), s, "Invalid WasDerivedFrom id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkEntityId(s.getGeneratedEntity()), s, "Invalid Entity id", s.getGeneratedEntity());
        assertTrue(vc.checkEntityId(s.getUsedEntity()), s, "Invalid Entity id", s.getUsedEntity());
        assertTrue(vc.checkUsedId(s.getUsage()), s, "Invalid Usage id", s.getUsage());
        assertTrue(vc.checkWasGeneratedById(s.getGeneration()), s, "Invalid Generation id", s.getGeneration());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(WasEndedBy s) {
        assertTrue(vc.checkWasEndedById(s.getId()), s, "Invalid WasEndedBy id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkEntityId(s.getTrigger()), s, "Invalid Entity id", s.getTrigger());
        assertTrue(vc.checkEntityId(s.getEnder()), s, "Invalid Activity id", s.getEnder());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(Entity s) {
        assertTrue(vc.checkEntityId(s.getId()), s, "Invalid Entity id", s.getId());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(WasGeneratedBy s) {
        assertTrue(vc.checkWasGeneratedById(s.getId()), s, "Invalid WasGeneratedBy id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkWasGeneratedByEntityId(s.getEntity()), s, "Invalid Entity id", s.getEntity());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(WasInvalidatedBy s) {
        assertTrue(vc.checkWasInvalidatedById(s.getId()), s, "Invalid WasInvalidatedBy id", s.getId());
        assertTrue(vc.checkActivityId(s.getActivity()), s, "Invalid Activity id", s.getActivity());
        assertTrue(vc.checkEntityId(s.getEntity()), s, "Invalid Entity id", s.getEntity());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(HadMember s) {
        assertTrue(vc.checkCollectionId(s.getCollection()), s, "Invalid Collection id", s.getCollection());
        s.getEntity().forEach(e ->
                assertTrue(vc.checkEntityId(e), s, "Invalid Entity id", e));
    }



    @Override
    public void doAction(SpecializationOf s) {
        assertTrue(vc.checkSpecializationOfEntityId(s.getGeneralEntity()), s, "Invalid Entity id", s.getGeneralEntity());
        assertTrue(vc.checkSpecializationOfEntityId(s.getSpecificEntity()), s, "Invalid Entity id", s.getSpecificEntity());
    }

    @Override
    public void doAction(QualifiedSpecializationOf s) {
        assertTrue(vc.checkQualifiedSpecializationId(s.getId()), s, "Invalid QualifiedSpecializationOf id", s.getId());
        assertTrue(vc.checkSpecializationOfEntityId(s.getGeneralEntity()), s, "Invalid Entity id", s.getGeneralEntity());
        assertTrue(vc.checkSpecializationOfEntityId(s.getSpecificEntity()), s, "Invalid Entity id", s.getSpecificEntity());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(QualifiedAlternateOf s) {
        assertTrue(vc.checkQualifiedAlternateId(s.getId()), s, "Invalid QualifiedAlternateOf id", s.getId());
        assertTrue(vc.checkEntityId(s.getAlternate1()), s, "Invalid Entity id", s.getAlternate1());
        assertTrue(vc.checkEntityId(s.getAlternate2()), s, "Invalid Entity id", s.getAlternate2());
        doAttributes(s.getOther(), s);
    }

    @Override
    public void doAction(QualifiedHadMember s) {
        assertTrue(vc.checkQualifiedHadMemberId(s.getId()), s, "Invalid QualifiedHadMember id", s.getId());
        assertTrue(vc.checkCollectionId(s.getCollection()), s, "Invalid Collection id", s.getCollection());
        s.getEntity().forEach(e ->
                assertTrue(vc.checkEntityId(e), s, "Invalid Entity id", e));
        doAttributes(s.getOther(), s);
    }


    @Override
    public void doAction(WasInfluencedBy s) {

    }

    @Override
    public void doAction(MentionOf s) {

    }
    @Override
    public void doAction(DerivedByInsertionFrom s) {

    }

    @Override
    public void doAction(WasInformedBy s) {
        assertTrue(vc.checkWasInformedById(s.getId()), s, "Invalid WasInformedBy id", s.getId());
        assertTrue(vc.checkActivityId(s.getInformant()), s, "Invalid Activity id", s.getInformant());
        assertTrue(vc.checkActivityId(s.getInformed()), s, "Invalid Activity id", s.getInformed());
        doAttributes(s.getOther(), s);
    }


    @Override
    public void doAction(DictionaryMembership s) {

    }

    @Override
    public void doAction(DerivedByRemovalFrom s) {

    }


    @Override
    public void doAction(Bundle s, ProvUtilities provUtilities) {
        assertTrue(vc.checkBundleId(s.getId()), s, "Invalid Bundle id", s.getId());
        provUtilities.forAllStatement(s.getStatement(),this);
    }


    private void doAttributes(List<Other> other, Statement s) {
        for (Other o : other) {
            QualifiedName attrName = o.getElementName();
            if (TMPL_NS.equals(attrName.getNamespaceURI())) {
                if (("time".equals(attrName.getLocalPart())) || ("startTime".equals(attrName.getLocalPart())) || ("endTime".equals(attrName.getLocalPart()))) {
                    if (o.getValue() instanceof QualifiedName) {
                        QualifiedName qn = (QualifiedName) o.getValue();
                        if (isVariable(qn)) {
                            assertTrue(vc.checkTimeVariable(qn), s, "Invalid time variable", qn);
                        }
                    }
                }
            } else if (isVariable(attrName)) {
                if (o.getValue() instanceof QualifiedName) {
                    QualifiedName qn = (QualifiedName) o.getValue();
                    if (isVariable(qn)) {
                        assertTrue(vc.checkPropertyValuePair(attrName, qn, s), s, "Invalid property value pair", attrName, qn);

                    }
                }

            }
        }
    }





        public void assertTrue(boolean condition, StatementOrBundle s, String message, QualifiedName varName) {
            if (!condition) {
                System.out.println("**** FAILED CHECK: "+message+" for variable "+varName+" in statement "+s.toString());
                if (strict) throw new VariableCheckException("In statement: " + s.toString() + ": " + message + " for variable " + varName);
            }
        }


        public void assertTrue(boolean condition, Statement s, String message, QualifiedName attrName, QualifiedName qn) {
           // System.out.println("**** CHECKING: "+message+" for attribute-value pair "+attrName+","+ qn + " in statement "+s.toString());
            if (!condition) {
                System.out.println("**** FAILED CHECK: "+message+" for attribute-value pair "+attrName+","+ qn + " in statement "+s.toString());
                if (strict) throw new VariableCheckException("In statement: " + s.toString() + ": " + message+" for attribute-value pair "+attrName+","+ qn);
            }
        }

    }