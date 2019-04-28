package org.openprovenance.prov.template;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasTime;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedNameUtils;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementAction;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;
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

import static org.openprovenance.prov.template.ExpandUtil.TMPL_NS;
import static org.openprovenance.prov.template.ExpandUtil.TMPL_PREFIX;

public class ExpandAction implements StatementAction {

    public static final String UUID_PREFIX = "uuid";
    public static final String URN_UUID_NS = "urn:uuid:";
    final private ProvFactory pf;
    final private Expand expand;
    final private Hashtable<QualifiedName, QualifiedName> env;
    final private ProvUtilities u;
    final private List<StatementOrBundle> ll = new LinkedList<StatementOrBundle>();
    final private List<Integer> index;
    final private Bindings bindings;
    final private Groupings grp1;
    final private Hashtable<QualifiedName, List<TypedValue>> env2;
    final private boolean addOrderp;
    final private String qualifiedNameURI;
    final private boolean allUpdatedRequired;
    
    private boolean allExpanded=true;
    
    public boolean getAllExpanded() {
    	return allExpanded;
    }

    public ExpandAction(ProvFactory pf,
                        ProvUtilities u,
                        Expand expand,
                        Hashtable<QualifiedName, QualifiedName> env,
                        Hashtable<QualifiedName, List<TypedValue>> env2,
                        List<Integer> index,
                        Bindings bindings1,
                        Groupings grp1,
                        boolean addOrderp,
                        boolean allUpdatedRequired) {
        this.pf = pf;
        this.expand = expand;
        this.env = env;
        this.u = u;
        this.index = index;
        this.bindings = bindings1;
        this.grp1 = grp1;
        this.env2 = env2;
        this.addOrderp = addOrderp;
        this.qualifiedNameURI = pf.getName().PROV_QUALIFIED_NAME.getUri();
        this.allUpdatedRequired=allUpdatedRequired;
    }

    @Override
    public void doAction(Activity s) {
        Activity res = pf.newActivity(s.getId(), s.getStartTime(), s.getEndTime(), null);
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        boolean updated2 = expandAttributes(s, res);
        boolean updated = updated1 || updated2;
        boolean allUpdated = updated1 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(Used s) {
        Used res = pf.newUsed(s.getId(), s.getActivity(), s.getEntity());
        res.setTime(s.getTime());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getActivity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName ag = res.getEntity();
        boolean updated3 = setExpand(res, ag, 2);
        boolean updated4 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4;
        boolean allUpdated = updated1 && updated2 && updated3;
        allExpanded=allExpanded && allUpdated;
        
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(WasStartedBy s) {
        WasStartedBy res = pf.newWasStartedBy(s.getId(),
                                              s.getActivity(),
                                              s.getTrigger(),
                                              s.getStarter());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getActivity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName ag = res.getTrigger();
        boolean updated3 = setExpand(res, ag, 2);
        QualifiedName st = res.getStarter();
        boolean updated4 = setExpand(res, st, 3);
        boolean updated5 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4 || updated5;
        boolean allUpdated = updated1 && updated2 && updated3 && updated4 ;
        allExpanded=allExpanded && allUpdated;

        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(Agent e) {
        Agent res = pf.newAgent(e.getId());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        boolean updated2 = expandAttributes(e, res);
        boolean updated = updated1 || updated2;
        boolean allUpdated = updated1;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(AlternateOf s) {
        AlternateOf res = pf.newAlternateOf(s.getAlternate1(), s.getAlternate2());

        QualifiedName alt1 = res.getAlternate1();
        boolean updated0 = setExpand(res, alt1, 0);
        QualifiedName alt2 = res.getAlternate2();
        boolean updated1 = setExpand(res, alt2, 1);

        @SuppressWarnings("unused")
        boolean updated = updated0 || updated1;
        boolean allUpdated = updated1 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        // if (updated) addOrderAttribute(res);
    }

    @Override
    public void doAction(WasAssociatedWith s) {
        WasAssociatedWith res = pf.newWasAssociatedWith(s.getId(), s.getActivity(), s.getAgent());
        res.setPlan(s.getPlan());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getActivity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName ag = res.getAgent();
        boolean updated3 = setExpand(res, ag, 2);
        QualifiedName pl = res.getPlan();
        boolean updated4 = setExpand(res, pl, 3);
        boolean updated5 = expandAttributes(s, res);
        boolean updated = updated1 || updated2 || updated3 || updated4 || updated5;
        boolean allUpdated = updated1 && updated2 && updated3 && updated4 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(WasAttributedTo s) {
        WasAttributedTo res = pf.newWasAttributedTo(s.getId(), s.getEntity(), s.getAgent());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getEntity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName ag = res.getAgent();
        boolean updated3 = setExpand(res, ag, 2);
        boolean updated4 = expandAttributes(s, res);
        boolean updated = updated1 || updated2 || updated3 || updated4;
        boolean allUpdated = updated1 && updated2 && updated3 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(WasInfluencedBy s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(ActedOnBehalfOf s) {
        ActedOnBehalfOf res = pf.newActedOnBehalfOf(s.getId(),
                                                    s.getDelegate(),
                                                    s.getResponsible(),
                                                    s.getActivity());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName del = res.getDelegate();
        boolean updated2 = setExpand(res, del, 1);
        QualifiedName resp = res.getResponsible();
        boolean updated3 = setExpand(res, resp, 2);
        QualifiedName act = res.getActivity();
        boolean updated4 = setExpand(res, act, 3);

        boolean updated5 = expandAttributes(s, res);
        boolean updated = updated1 || updated2 || updated3 || updated4 || updated5;
        boolean allUpdated = updated1 && updated2 && updated3 && updated4 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(WasDerivedFrom s) {
        WasDerivedFrom res = pf.newWasDerivedFrom(s.getId(),
                                                  s.getGeneratedEntity(),
                                                  s.getUsedEntity());
        res.setActivity(s.getActivity());
        res.setUsage(s.getUsage());
        res.setGeneration(s.getGeneration());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en2 = res.getGeneratedEntity();
        boolean updated2 = setExpand(res, en2, 1);
        QualifiedName en1 = res.getUsedEntity();
        boolean updated3 = setExpand(res, en1, 2);
        QualifiedName act = res.getActivity();
        boolean updated4 = setExpand(res, act, 3);
        QualifiedName gen = res.getGeneration();
        boolean updated5 = setExpand(res, gen, 4);
        QualifiedName use = res.getUsage();
        boolean updated6 = setExpand(res, use, 5);

        boolean updated7 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4 || updated5 || updated6
                || updated7;
        boolean allUpdated = updated1 && updated2 && updated3 && updated4 && updated5 && updated6;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);

    }

    @Override
    public void doAction(DictionaryMembership s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(DerivedByRemovalFrom s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(WasEndedBy s) {
        WasEndedBy res = pf.newWasEndedBy(s.getId(), s.getActivity(), s.getTrigger(), s.getEnder());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getActivity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName ag = res.getTrigger();
        boolean updated3 = setExpand(res, ag, 2);
        QualifiedName st = res.getEnder();
        boolean updated4 = setExpand(res, st, 3);
        boolean updated5 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4 || updated5;
        boolean allUpdated = updated1 && updated2 && updated3 && updated4 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(Entity e) {
        Entity res = pf.newEntity(e.getId());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        boolean updated2 = expandAttributes(e, res);
        boolean updated = updated1 || updated2;
        boolean allUpdated = updated1 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    public boolean expandAttributes(Statement srcStatement, Statement dstStatement) {
        boolean found = false;
        if (dstStatement instanceof HasOther) {

            Collection<Attribute> attributes = pf.getAttributes(srcStatement);
            Collection<Attribute> dstAttributes = new LinkedList<Attribute>();

            for (Attribute attribute : attributes) {
                if (qualifiedNameURI.equals(attribute.getType().getUri())) {

                    Object o = attribute.getValue();
                    if (o instanceof QualifiedName) { // if attribute is
                                                      // constructed properly,
                                                      // this test should always
                                                      // return true
                        QualifiedName qn1 = (QualifiedName) o;

                        if (ExpandUtil.isVariable(qn1)) {
                            List<TypedValue> vals = env2.get(qn1);

                            if (vals == null) {
                                if (Expand.isGensymVariable(qn1)) {
                                    dstAttributes.add(pf.newAttribute(attribute.getElementName(),
                                                                      getUUIDQualifiedName(),
                                                                      pf.getName().PROV_QUALIFIED_NAME));
                                }
                                // if not a vargen, then simply drop this
                                // attribute
                                // dstAttributes.add(attribute);
                            } else {
                                found = true;
                                processTemplateAttributes(dstStatement,
                                                          dstAttributes,
                                                          attribute,
                                                          vals);
                            }
                        } else { // no variable here
                            dstAttributes.add(attribute);
                        }
                    } else { // not even a qualified name
                        dstAttributes.add(attribute);
                    }
                } else { // not a qualified name
                    dstAttributes.add(attribute);
                }
            }
            pf.setAttributes((HasOther) dstStatement, dstAttributes);
        }
        return found;
    }
    
    boolean allowVariableInLabelAndTime=true;

    public void processTemplateAttributes(Statement dstStatement,
                                          Collection<Attribute> dstAttributes,
                                          Attribute attribute,
                                          List<TypedValue> vals) {

        for (TypedValue val : vals) {
            String elementName = attribute.getElementName().getUri();
            if (ExpandUtil.LABEL_URI.equals(elementName)) {
                Object value=val.getValue();
                if (allowVariableInLabelAndTime && (value instanceof QualifiedName) && ((QualifiedName)value).getNamespaceURI().equals(ExpandUtil.VAR_NS)) {
                    dstAttributes.add(pf.newAttribute(attribute.getElementName(),
                                                      value,
                                                      val.getType()));
                } else {
                    dstAttributes.add(pf.newAttribute(pf.getName().PROV_LABEL,
                                                      value,
                                                      val.getType()));
                }
            } else if (ExpandUtil.TIME_URI.equals(elementName)) {
                Object value=val.getValue();
                if (allowVariableInLabelAndTime && (value instanceof QualifiedName) && ((QualifiedName)value).getNamespaceURI().equals(ExpandUtil.VAR_NS)) {
                    dstAttributes.add(pf.newAttribute(attribute.getElementName(),
                                                      value,
                                                      val.getType()));
                } else if (dstStatement instanceof HasTime) {
                    ((HasTime) dstStatement).setTime(pf.newISOTime((String) value));
                }
            } else if (ExpandUtil.STARTTIME_URI.equals(elementName)) {
                if (dstStatement instanceof Activity) {
                    ((Activity) dstStatement).setStartTime(pf.newISOTime((String) val.getValue()));
                }
            } else if (ExpandUtil.ENDTIME_URI.equals(elementName)) {
                if (dstStatement instanceof Activity) {
                    ((Activity) dstStatement).setEndTime(pf.newISOTime((String) val.getValue()));
                }
            } else {
                dstAttributes.add(pf.newAttribute(attribute.getElementName(),
                                                  val.getValue(),
                                                  val.getType()));
            }
        }
    }

    final QualifiedNameUtils qnU = new QualifiedNameUtils();

    public QualifiedName getUUIDQualifiedName() {
        UUID uuid = UUID.randomUUID();
        return pf.newQualifiedName(URN_UUID_NS,
                                   qnU.escapeProvLocalName(uuid.toString()),
                                   UUID_PREFIX);
    }

    public void addOrderAttribute(HasOther res) {
        if (addOrderp) {
            res.getOther().add(pf.newOther(TMPL_NS,
                                           "order",
                                           TMPL_PREFIX,
                                           index,
                                           pf.getName().XSD_STRING));
        }
    }

    private boolean setExpand(Statement res, QualifiedName id, int position) {
        if (ExpandUtil.isVariable(id)) {
            QualifiedName val = env.get(id);
            if (val != null) {
                u.setter(res, position, val);
                return true;
            } else {
                if (Expand.isGensymVariable(id)) {
                    QualifiedName uuid = getUUIDQualifiedName();
                    u.setter(res, position, uuid);
                    bindings.addVariable(id, uuid);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            // this is not a variable, so it's regarded as expanded
            return true;
        }
    }

    @Override
    public void doAction(WasGeneratedBy s) {
        WasGeneratedBy res = pf.newWasGeneratedBy(s.getId(), s.getEntity(), s.getActivity());
        res.setTime(s.getTime());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getEntity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName act = res.getActivity();
        boolean updated3 = setExpand(res, act, 2);
        boolean updated4 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4;
        boolean allUpdated = updated1 && updated2 && updated3 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(WasInvalidatedBy s) {
        WasInvalidatedBy res = pf.newWasInvalidatedBy(s.getId(), s.getEntity(), s.getActivity());
        res.setTime(s.getTime());
        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName en = res.getEntity();
        boolean updated2 = setExpand(res, en, 1);
        QualifiedName act = res.getActivity();
        boolean updated3 = setExpand(res, act, 2);
        boolean updated4 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4;
        boolean allUpdated = updated1 && updated2 && updated3 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    @Override
    public void doAction(HadMember s) {
        HadMember res = pf.newHadMember(s.getCollection(), s.getEntity());

        QualifiedName col = res.getCollection();
        boolean updated0 = setExpand(res, col, 0);
        List<QualifiedName> ent = res.getEntity();
        if (ent.size() > 1) {
            throw new UnsupportedOperationException(
                                                    "can't expand HadMember with more than one members");
        }
        boolean updated1 = setExpand(res, ent.get(0), 1);

        // .out.println("FIXME: to do , expand entities"); //FIXME

        @SuppressWarnings("unused")
        boolean updated = updated0 || updated1;
        boolean allUpdated = updated0 && updated1 ;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        // if (updated) addOrderAttribute(res);
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(MentionOf s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(SpecializationOf s) {
        SpecializationOf res = pf.newSpecializationOf(s.getSpecificEntity(), s.getGeneralEntity());

        QualifiedName spe = res.getSpecificEntity();
        boolean updated0 = setExpand(res, spe, 0);
        QualifiedName gen = res.getGeneralEntity();
        boolean updated1 = setExpand(res, gen, 1);

        @SuppressWarnings("unused")
        boolean updated = updated0 || updated1;
        boolean allUpdated = updated0 && updated1;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        // if (updated) addOrderAttribute(res);

    }
    
    @Override
    public void doAction(QualifiedSpecializationOf s) {
        QualifiedSpecializationOf res = pf.newQualifiedSpecializationOf(s.getId(),s.getSpecificEntity(), s.getGeneralEntity(),null);

        QualifiedName spe = res.getSpecificEntity();
        boolean updated0 = setExpand(res, spe, 0);
        QualifiedName gen = res.getGeneralEntity();
        boolean updated1 = setExpand(res, gen, 1);
        boolean updated2 = expandAttributes(s, res);

        @SuppressWarnings("unused")
        boolean updated = updated0 || updated1 || updated2;
        boolean allUpdated = updated0 && updated1 && updated2;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated) addOrderAttribute(res);

    }
    
    @Override
    public void doAction(QualifiedAlternateOf s) {
        QualifiedAlternateOf res = pf.newQualifiedAlternateOf(s.getId(),s.getAlternate1(), s.getAlternate2(),null);

        QualifiedName alt1 = res.getAlternate1();
        boolean updated0 = setExpand(res, alt1, 0);
        QualifiedName alt2 = res.getAlternate2();
        boolean updated1 = setExpand(res, alt2, 1);
        boolean updated2 = expandAttributes(s, res);

        boolean updated = updated0 || updated1 || updated2;
        boolean allUpdated = updated0 && updated1 && updated2;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated) addOrderAttribute(res);

    }
    
    @Override
    public void doAction(QualifiedHadMember s) {
        QualifiedHadMember res = pf.newQualifiedHadMember(s.getId(),s.getCollection(), s.getEntity(),null);

        QualifiedName col = res.getCollection();
        boolean updated0 = setExpand(res, col, 0);
        List<QualifiedName> ent = res.getEntity();
        if (ent.size() > 1) {
            throw new UnsupportedOperationException(
                                                    "can't expand QualfiedHadMember with more than one members");
        }
        boolean updated1 = setExpand(res, ent.get(0), 1);
        boolean updated2 = expandAttributes(s, res);

        // .out.println("FIXME: to do , expand entities"); //FIXME

        boolean updated = updated0 || updated1|| updated2;
        boolean allUpdated = updated0 && updated1 && updated2;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated) addOrderAttribute(res);
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(DerivedByInsertionFrom s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAction(WasInformedBy s) {
        WasInformedBy res = pf.newWasInformedBy(s.getId(), s.getInformed(), s.getInformant());

        QualifiedName id = res.getId();
        boolean updated1 = setExpand(res, id, 0);
        QualifiedName a2 = res.getInformed();
        boolean updated2 = setExpand(res, a2, 1);
        QualifiedName a1 = res.getInformant();
        boolean updated3 = setExpand(res, a1, 2);
        boolean updated4 = expandAttributes(s, res);

        boolean updated = updated1 || updated2 || updated3 || updated4;
        boolean allUpdated = updated1 && updated2 && updated3;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);

    }

    @Override
    public void doAction(Bundle bun, ProvUtilities provUtilities) {
        List<Statement> statements = bun.getStatement();
        List<Statement> newStatements = new LinkedList<Statement>();

        for (Statement s : statements) {
            for (StatementOrBundle sb : expand.expand(s, bindings, grp1)) {
                newStatements.add((Statement) sb);
            }

        }

        updateEnvironmentForBundleId(bun, bindings, env);

        QualifiedName newId;
        final QualifiedName bunId = bun.getId();
        if (ExpandUtil.isVariable(bunId)) {
            // System.out.println("===> bundle " + env + " " + bindings);
            QualifiedName val = env.get(bunId);
            if (val != null) {
                newId = val;
            } else {
                if (Expand.isGensymVariable(bunId)) {
                    QualifiedName uuid = getUUIDQualifiedName();
                    newId = uuid;
                    bindings.addVariable(bunId, uuid);
                } else {
                    newId = bunId;
                }
            }
        } else {
            newId = bunId;
        }
        ll.add(pf.newNamedBundle(newId, newStatements));

    }

    public void updateEnvironmentForBundleId(Bundle bun,
                                             Bindings bindings1,
                                             Hashtable<QualifiedName, QualifiedName> env0) {
        final QualifiedName id = bun.getId();
        if (ExpandUtil.isVariable(id)) {
            List<QualifiedName> vals = bindings1.getVariables().get(id);
            if (vals == null) {
                if (Expand.isGensymVariable(id)) {
                    // OK, we'll generate a uuid later
                } else {
                    throw new BundleVariableHasNoValue(id);
                }
            } else {
                if (vals.size() > 1) {
                    throw new BundleVariableHasMultipleValues(id, vals);
                } else {
                    env0.put(id, vals.get(0));
                }
            }
        }
    }

    public List<StatementOrBundle> getList() {
        return ll;
    }

}
