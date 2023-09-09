package org.openprovenance.prov.validation;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementAction;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.validation.report.MalformedStatements;

/**
 * This class checks that 1. All mandatory arguments are present, and have an
 * entry in the corresponding table. Absent mandatory argument results in a
 * malformedExpression, and missing entries are added. 2. All expandable
 * optional arguments have been added, with an entry in the table.
 *
 * @author lavm
 *
 *
 *
 *         Relation Expandable Non-expandable wasGeneratedBy(id;e,a,t,attrs)
 *         id,a,t used(id;a,e,t,attrs) id,e,t wasInformedBy(id;a2,a1,attrs) id
 *         wasStartedBy(id;a2,e,a1,t,attrs) id,e,a1,t
 *         wasEndedBy(id;a2,e,a1,t,attrs) id,e,a1,t
 *         wasInvalidatedBy(id;e,a,t,attrs) id,a,t
 *         wasDerivedFrom(id;e2,e1,a,g2,u1,attrs) id,g2,u1 a
 *         wasDerivedFrom(id;e2,e1,attrs) id wasAttributedTo(id;e,ag,attr) id
 *         wasAssociatedWith(id;a,ag,pl,attrs) id,ag pl
 *         actedOnBehalfOf(id;ag2,ag1,a,attrs) id,a
 *         wasInfluencedBy(id;e2,e1,attrs) id
 */

public class Expansion {

    static Logger logger = LogManager.getLogger(Expansion.class);

    final ProvFactory p;
    final ProvUtilities u;

    private final Indexer indexer;
    private final Gensym g;
    private final Config config;
    private final Types typeChecker;
    private final MalformedStatements malformed=new MalformedStatements();

    public Expansion(Indexer indexer, Config config, Types typeChecker) {
        this.indexer = indexer;
        this.g = indexer.g;
        this.p = indexer.p;
        this.u = indexer.u;
        this.config=config;
        this.typeChecker=typeChecker;
    }

    class ExpansionAction implements StatementAction {
        Config config;

        public ExpansionAction(Config config) {
            this.config = config;
        }

        public void doAction(Entity e) {
            expansion_entity(config, e);
        }

        public void doAction(Activity a) {
            expansion_activity(config, a);
        }

        public void doAction(Agent ag) {
            expansion_agent(config, ag);
        }

        public void doAction(WasGeneratedBy gen) {
            expansion_wasGeneratedBy(config, gen);
        }

        public void doAction(Used use) {
            expansion_used(config, use);
        }

        public void doAction(WasInvalidatedBy inv) {
            expansion_wasInvalidatedBy(config, inv);
        }

        public void doAction(WasStartedBy start) {
            expansion_wasStartedBy(config, start);
        }

        public void doAction(WasEndedBy end) {
            expansion_wasEndedBy(config, end);
        }

        public void doAction(WasInformedBy inf) {
            expansion_wasInformedBy(config, inf);
        }

        public void doAction(WasDerivedFrom der) {
            expansion_wasDerivedFrom(config, der);
        }

        public void doAction(WasAssociatedWith assoc) {
            expansion_wasAssociatedWith(config, assoc);
        }

        public void doAction(WasAttributedTo attr) {
            expansion_wasAttributedTo(config, attr);
        }

        public void doAction(ActedOnBehalfOf del) {
            expansion_actedOnBehalfOf(config, del);
        }

        public void doAction(WasInfluencedBy infl) {
            expansion_wasInfluencedBy(config, infl);
        }

        public void doAction(AlternateOf alt) {
            expansion_alternateOf(config, alt);
        }

        public void doAction(QualifiedAlternateOf alt) {
            expansion_alternateOf(config, alt);
        }

        public void doAction(MentionOf men) {
            expansion_mentionOf(config, men);
        }

        public void doAction(SpecializationOf spec) {
            expansion_specializationOf(config, spec);
        }

        public void doAction(QualifiedSpecializationOf spec) {
            expansion_specializationOf(config, spec);
        }

        public void doAction(QualifiedHadMember mem) {
            expansion_membership(config, mem);
        }

        public void doAction(HadMember mem) {
            expansion_membership(config, mem);
        }


        public void doAction(DictionaryMembership s) {
        }

        public void doAction(DerivedByRemovalFrom s) {
        }

        public void doAction(DerivedByInsertionFrom s) {
        }

        public void doAction(Bundle s,
                             org.openprovenance.prov.model.ProvUtilities provUtilities) {
        }
    }

    public void expansion(Config config, List<Statement> records) {
        u.forAllStatement(records, new ExpansionAction(config));


        List<?> toremove=malformed.getStatement();
        if (!(toremove.isEmpty())) {
            logger.debug("removing " + toremove.size());
            logger.debug("removing before " + records.size());
            records.removeAll(toremove);
            logger.debug("removing before " + records.size());
        }
    }



    public MalformedStatements getMalformed() {
        return malformed;
    }



    public boolean checkEntityArgument(Config config, Statement o, int i) {
        QualifiedName entity = (QualifiedName) u.getter(o, i);
        boolean res = entity != null;
        if (res) typeChecker.addInferredType(entity,Types.entityURI);
        return res;
    }

    public boolean checkAgentArgument(Config config, Statement o, int i) {
        QualifiedName agent = (QualifiedName) u.getter(o, i);
        boolean res = agent != null;
        if (res) typeChecker.addInferredType(agent,Types.agentURI);
        return res;
    }

    /* Check an activity exists for this argument */
    public boolean checkActivityArgument(Config config, Statement o, int i) {
        QualifiedName activity = (QualifiedName) u.getter(o, i);
        boolean res = activity != null;
        if (res) typeChecker.addInferredType(activity,Types.activityURI);
        return res;
    }

    /**
     * Checks an activity is declared. If not, add a declaration in activity
     * table.
     * @param activity1 an activity
     */
    public void checkActivityIsDeclared(QualifiedName activity1) {
        if (activity1 != null) {
            String activity1URI = activity1.getUri();
            if (indexer.activityTable.get(activity1URI) == null) {
                // activity1 is named, but not declared
                indexer.activityTable.put(activity1URI,
                        p.newActivity(activity1));
            }
            typeChecker.addInferredType(activity1,Types.activityURI);
        }
    }

    /** Checks an entity is declared. If not, add a declaration in entity table. 
     * @param entity1 an entity id
     */
    public void checkEntityIsDeclared(QualifiedName entity1) {
        if (entity1 != null) {
            String entity1URI = entity1.getUri();
            if (indexer.entityTable.get(entity1URI) == null) {
                // entity1 is named, but not declared
                indexer.entityTable.put(entity1URI,
                        p.newEntity(entity1));
            }
            typeChecker.addInferredType(entity1,Types.entityURI);
        }
    }

    /** Checks an agent is declared. If not, add a declaration in agent table.
     * @param agent1 an agent id
     */
    public void checkAgentIsDeclared(QualifiedName agent1) {
        if (agent1 != null) {
            String agent1URI = agent1.getUri();
            if (indexer.agentTable.get(agent1URI) == null) {
                // agent1 is named, but not declared
                indexer.agentTable.put(agent1URI, p.newAgent(agent1));
            }
            typeChecker.addInferredType(agent1,Types.agentURI);
        }
    }

    public void expand_entityArgument(Config config, Statement o, int i) {
        QualifiedName entity = (QualifiedName) u.getter(o, i);
        if (config.isTrue(Config.DEFINITION_OPTIONAL_PLACEHOLDERS)
                && (entity == null)) {
            //entity = p.newQualifiedName((QualifiedName) null);
            //g.setId(entity);
            entity=g.newId(entity);
            u.setter(o, i, entity);
        }
        typeChecker.addInferredType(entity,Types.entityURI);
        return;
    }

    public void expand_activityArgument(Config config, Statement o, int i) {
        QualifiedName activity = (QualifiedName) u.getter(o, i);
        if (config.isTrue(Config.DEFINITION_OPTIONAL_PLACEHOLDERS)
                && (activity == null)) {
            //activity = p.newIDRef((QualifiedName) null);
            //g.setId(activity);
            activity=g.newId(activity);
            u.setter(o, i, activity);
        }
        typeChecker.addInferredType(activity,Types.activityURI);
        return;
    }

    public void expand_timeArgument(Config config, Statement o, int i) {
        XMLGregorianCalendar time = (XMLGregorianCalendar) u.getter(o, i);
        if (config.isTrue(Config.DEFINITION_OPTIONAL_PLACEHOLDERS)
                && (time == null)) {
            time = g.newVarTime();
            u.setter(o, i, time);
        }
        return;
    }

    public void expand_agentArgument(Config config, Statement o, int i) {
        QualifiedName agent = (QualifiedName) u.getter(o, i);
        if (config.isTrue(Config.DEFINITION_OPTIONAL_PLACEHOLDERS)
                && (agent == null)) {
            //agent = p.newIDRef((QualifiedName) null);
            agent=g.newId(agent);
            u.setter(o, i, agent);
        }
        typeChecker.addInferredType(agent,Types.agentURI);
        return;
    }



    public void expand_usageArgument(Config config, Statement o, int i) {
        QualifiedName dep = (QualifiedName) u.getter(o, i);
        if (config.isTrue(Config.DEFINITION_OPTIONAL_PLACEHOLDERS)
                && (dep == null)) {
            //dep = p.newIDRef((QualifiedName) null);
            //g.setId(dep);
            dep=g.newId(dep);
            u.setter(o, i, dep);
        }
        typeChecker.addInferredType(dep,Types.usageURI);
        return;
    }

    public void expand_generationArgument(Config config, Statement o, int i) {
        QualifiedName dep = (QualifiedName) u.getter(o, i);
        if (config.isTrue(Config.DEFINITION_OPTIONAL_PLACEHOLDERS)
                && (dep == null)) {
            //dep = p.newIDRef((QualifiedName) null);
            //g.setId(dep);
            dep=g.newId(dep);
            u.setter(o, i, dep);
        }
        typeChecker.addInferredType(dep,Types.generationURI);

        return;
    }

    public void expansion_specializationOf(Config config,
                                           SpecializationOf spec) {

        if (!checkEntityArgument(config, spec, 0)) {
            malformedExpression(spec);
            return;
        }
        if (!checkEntityArgument(config, spec, 1)) {
            malformedExpression(spec);
            return;
        }
        typeChecker.addInferredType(spec.getGeneralEntity(),Types.entityURI);
        typeChecker.addInferredType(spec.getSpecificEntity(),Types.entityURI);

    }

    public void expansion_mentionOf(Config config, MentionOf men) {
        // NOTE how index numbers have been increased by one, since we introduced a dummy attribute id at index 0

        if (!checkEntityArgument(config, men, 1)) {
            malformedExpression(men);
            return;
        }
        if (!checkEntityArgument(config, men, 2)) {
            malformedExpression(men);
            return;
        }
        if (!checkEntityArgument(config, men, 3)) {
            malformedExpression(men);
            return;
        }
        typeChecker.addInferredType(men.getGeneralEntity(),Types.entityURI);
        typeChecker.addInferredType(men.getSpecificEntity(),Types.entityURI);
        typeChecker.addInferredType(men.getBundle(),Types.entityURI);
        typeChecker.addInferredType(men.getBundle(),Types.bundleURI);
    }

    public void expansion_alternateOf(Config config, AlternateOf alt) {
        // TODO Auto-generated method stub

    }

    public void expansion_agent(Config config, Agent ag) {
        typeChecker.addInferredType(ag.getId(),Types.agentURI);

    }

    public void expansion_activity(Config config, Activity a) {
        typeChecker.addInferredType(a.getId(),Types.activityURI);
        expand_timeArgument(config, a, 1);
        expand_timeArgument(config, a, 2);
    }

    public void expansion_entity(Config config, Entity e) {
        typeChecker.addInferredType(e.getId(),Types.entityURI);
    }

    public void expansion_wasStartedBy(Config config, WasStartedBy start) {
        if (start.getId() == null) g.setId(start);
        typeChecker.addInferredType(start.getId(),Types.startURI);
        if (!checkActivityArgument(config, start, 1)) {
            malformedExpression(start);
        }
        expand_entityArgument(config, start, 2);
        expand_activityArgument(config, start, 3);
        expand_timeArgument(config, start, 4);
    }

    public void expansion_wasEndedBy(Config config, WasEndedBy end) {
        if (end.getId() == null) g.setId(end);
        typeChecker.addInferredType(end.getId(),Types.endURI);
        if (!checkActivityArgument(config, end, 1)) {
            malformedExpression(end);
        }
        expand_entityArgument(config, end, 2);
        expand_activityArgument(config, end, 3);
        expand_timeArgument(config, end, 4);
    }

    public void expansion_used(Config config, Used use) {
        if (use.getId() == null) g.setId(use);
        typeChecker.addInferredType(use.getId(),Types.usageURI);
        if (!checkActivityArgument(config, use, 1)) {
            malformedExpression(use);
        }
        expand_entityArgument(config, use, 2);
        expand_timeArgument(config, use, 3);
    }

    public void expansion_wasGeneratedBy(Config config, WasGeneratedBy gen) {
        if (gen.getId() == null) g.setId(gen);
        typeChecker.addInferredType(gen.getId(),Types.generationURI);
        if (!checkEntityArgument(config, gen, 1)) {
            malformedExpression(gen);
        }
        expand_activityArgument(config, gen, 2);
        expand_timeArgument(config, gen, 3);
    }

    public void expansion_wasInvalidatedBy(Config config, WasInvalidatedBy inv) {
        if (inv.getId() == null) g.setId(inv);
        typeChecker.addInferredType(inv.getId(),Types.invalidationURI);
        if (!checkEntityArgument(config, inv, 1)) {
            malformedExpression(inv);
        }
        expand_activityArgument(config, inv, 2);
        expand_timeArgument(config, inv, 3);
    }

    public void expansion_wasDerivedFrom(Config config, WasDerivedFrom der) {
        if (der.getId() == null) g.setId(der);
        typeChecker.addInferredType(der.getId(),Types.derivationURI);
        if (!checkEntityArgument(config, der, 1)) {
            malformedExpression(der);
        }
        if (!checkEntityArgument(config, der, 2)) {
            malformedExpression(der);
        }
        if (der.getActivity() != null) {
            expand_generationArgument(config, der, 4);
            expand_usageArgument(config, der, 5);

            QualifiedName usage = der.getUsage();
            if (usage != null) {
                String usageURI = usage.getUri();
                if (indexer.usedTable.get(usageURI) == null) {
                    // TODO I disabled the creation of a new used. Check that's
                    // what we want
                    // usage is named, but not declared
                    // indexer.usedTable.put(usageURI,
                    // p.newUsed(usage.getRef()));
                }
            }

            QualifiedName generation = der.getGeneration();
            if (generation != null) {
                String generationURI = generation.getUri();
                if (indexer.wasGeneratedByTable.get(generationURI) == null) {
                    // TODO I disabled the creation of a new generation. Check
                    // that's what we want
                    // generation is named, but not declared
                    // indexer.wasGeneratedByTable.put(generationURI, p
                    // .newWasGeneratedBy(generation.getRef()));
                }
            }

        } else {
            der.setActivity(g.newUnknown());

            if (der.getGeneration() != null) {
                // record expression as malformed but continue
                malformedExpression(der, false);
            } else {
                der.setGeneration(g.newUnknown());
            }
            if (der.getUsage() != null) {
                // record expression as malformed but continue
                malformedExpression(der, false);
            } else {
                der.setUsage(g.newUnknown());
            }
        }
    }

    public void expansion_wasAssociatedWith(Config config,
                                            WasAssociatedWith assoc) {
        if (assoc.getId() == null) g.setId(assoc);
        if (!checkActivityArgument(config, assoc, 1)) {
            malformedExpression(assoc);
        }
        expand_agentArgument(config, assoc, 2);
        // plan is not expandable!
        if (assoc.getPlan() == null) {
            assoc.setPlan(g.newUnknown());
        }
    }

    public void expansion_actedOnBehalfOf(Config config, ActedOnBehalfOf del) {
        if (del.getId() == null) g.setId(del);
        if (!checkAgentArgument(config, del, 1)) {
            malformedExpression(del);
        }
        expand_agentArgument(config, del, 2);
        expand_activityArgument(config, del, 3);

    }

    public void expansion_wasAttributedTo(Config config, WasAttributedTo attr) {
        if (attr.getId() == null) g.setId(attr);
        if (!checkEntityArgument(config, attr, 1)) {
            malformedExpression(attr);
        }
        if (!checkAgentArgument(config, attr, 2)) {
            malformedExpression(attr);
        }

    }

    public void expansion_wasInformedBy(Config config, WasInformedBy inf) {
        if (inf.getId() == null) g.setId(inf);
        if (!checkActivityArgument(config, inf, 1)) {
            malformedExpression(inf);
        }
        if (!checkActivityArgument(config, inf, 2)) {
            malformedExpression(inf);
        }
    }

    public void expansion_wasInfluencedBy(Config config, WasInfluencedBy infl) {
        if (infl.getId() == null) g.setId(infl);
        if (infl.getInfluencer() == null) {
            malformedExpression(infl);
        }
        if (infl.getInfluencee() == null) {
            malformedExpression(infl);
        }
    }

    public void expansion_membership(Config config, HadMember mem) {
        if (mem.getCollection()==null) {
            malformedExpression(mem);
            return;
        }
        if (mem.getEntity()==null
                || mem.getEntity().isEmpty()
                || (mem.getEntity().size()==1 && mem.getEntity().get(0)==null)) { // test required, since prov-n parser returns this for scruffy hadMember(e,-)
            malformedExpression(mem);
            return;
        }
        typeChecker.addInferredType(mem.getCollection(),Types.entityURI);
        typeChecker.addInferredType(mem.getCollection(),Types.collectionURI);

        boolean empty=true;
        for (QualifiedName entity: mem.getEntity()) {
            if (entity!=null) {
                typeChecker.addInferredType(entity,Types.entityURI);
                empty=false;
            }
        }

        if (!empty) {
            typeChecker.addInferredType(mem.getCollection(),Types.nonEmptyCollectionURI);
        }

    }


    public <T> void malformedExpression(T tmp) {
        malformedExpression(tmp, true);
    }

    public <T> void malformedExpression(T tmp,
                                        boolean stop) {
        if (malformed != null)
            malformed.getStatement().add((Statement)tmp);
        // @TODO empty object, add to malformed expressions
        //if (stop)
        //    throw new UnsupportedOperationException("malformed expression");
    }

}
