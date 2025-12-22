package org.openprovenance.prov.template.expander;

import java.util.*;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.template.expander.exception.BundleVariableHasMultipleValues;
import org.openprovenance.prov.template.expander.exception.BundleVariableHasNoValue;
import org.openprovenance.prov.template.json.*;

import static org.openprovenance.prov.template.expander.InstantiateUtil.*;

public class InstantiateAction implements StatementAction {


    final private ProvFactory pf;
    final private Instantiater instantiater;
    final private ProvUtilities u;
    final private List<StatementOrBundle> ll = new LinkedList<>();
    final private List<Integer> index;
    final private Groupings grp1;
    final private boolean addOrderp;
    final private String qualifiedNameURI;
    final private boolean allUpdatedRequired;
    private final boolean preserveUnboundVariables;
    private final Bindings bindings;
    private final Map<QualifiedName, QDescriptor> env;
    private final Map<QualifiedName, SingleDescriptors> env2;
    private final DescriptorUtils dUtils;

    private boolean allExpanded=true;
    
    public boolean getAllExpanded() {
    	return allExpanded;
    }

    public InstantiateAction(ProvFactory pf,
                             ProvUtilities u,
                             Instantiater instantiater,
                             Map<QualifiedName, QDescriptor> env,
                             Map<QualifiedName, SingleDescriptors> env2,
                             List<Integer> index,
                             Bindings bindings,
                             Groupings grp1,
                             boolean addOrderp,
                             boolean allUpdatedRequired,
                             boolean preserveUnboundVariables) {
        this.pf = pf;
        this.instantiater = instantiater;
        this.u = u;
        this.index = index;
        this.bindings=bindings;
        this.grp1 = grp1;
        this.env=env;
        this.env2=env2;
        this.addOrderp = addOrderp;
        this.qualifiedNameURI = pf.getName().PROV_QUALIFIED_NAME.getUri();
        this.allUpdatedRequired=allUpdatedRequired;
        this.preserveUnboundVariables=preserveUnboundVariables;
        this.dUtils = new DescriptorUtils(pf);

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
            // NOTE change: if a variable in id position was replaced with null value, then we ignore the change
            if (updated1 && res.getId()!=null) ll.add(res);
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
            // NOTE change: if a variable in id position was replaced with null value, then we ignore the change
            if (updated1 && res.getId()!=null) ll.add(res);

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
        WasAssociatedWith res = pf.newWasAssociatedWith(s.getId(), s.getActivity(), s.getAgent(), s.getPlan());
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
            if (updated3 && res.getAgent()!=null) ll.add(res);
            //ll.add(res);
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
            if (res.getAgent()!=null) ll.add(res);
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
            if (updated3 && res.getResponsible()!=null  && res.getDelegate()!=null) ll.add(res);
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
            // NOTE change: if a variable in id position was replaced with null value, then we ignore the change
            if (updated1 && res.getId()!=null) ll.add(res);
        }
        if (updated)
            addOrderAttribute(res);
    }

    public boolean expandAttributes(Statement srcStatement, Statement dstStatement) {
        boolean found = false;
        if (dstStatement instanceof HasOther) {

            Collection<Attribute> srcAttributes = pf.getAttributes(srcStatement);
            Collection<Attribute> dstAttributes = new LinkedList<>();

            for (final Attribute srcAttribute : srcAttributes) {
                QualifiedName srcAttributeElementName = srcAttribute.getElementName();
                if (InstantiateUtil.isVariable(srcAttributeElementName)) {
                    int count=0;
                    SingleDescriptors singleDescriptors=env2.get(srcAttributeElementName);

                    if (singleDescriptors!=null && singleDescriptors.values!=null) {

                        List<SingleDescriptor> srcAttributeDescriptors=singleDescriptors.values;

                        // if prop has not value, we skip this
                        for (SingleDescriptor srcAttributeDescriptor : srcAttributeDescriptors) {
                            if (srcAttributeDescriptor instanceof QDescriptor) {
                                QualifiedName qn1= dUtils.newQualifiedName((QDescriptor) srcAttributeDescriptor, bindings);
                                Attribute srcAttribute2 = pf.newAttribute(qn1, srcAttribute.getValue(), srcAttribute.getType());
                                found = expandAttributeValue(dstStatement, srcAttribute2, dstAttributes, found, count);
                                count++;
                            } else {
                                // this was not a qualified name, so we ignore it
                            }
                        }
                    }
                } else {

                    found = expandAttributeValue(dstStatement, srcAttribute, dstAttributes, found, null);
                }
            }
            pf.setAttributes((HasOther) dstStatement, dstAttributes);
        }
        return found;
    }

    private boolean expandAttributeValue(Statement dstStatement, Attribute attribute, Collection<Attribute> dstAttributes, boolean found, Integer count) {
        if (qualifiedNameURI.equals(attribute.getType().getUri())) {
            QualifiedName attributeElementName=attribute.getElementName();
            Object attributeValue = attribute.getValue();
            if (attributeValue instanceof QualifiedName) { // if attribute is constructed properly,
                // this test should always return true
                QualifiedName qn1 = (QualifiedName) attributeValue;

                if (InstantiateUtil.isVariable(qn1)) {
                    SingleDescriptors singleDescriptors = env2.get(qn1);

                    if (singleDescriptors == null || singleDescriptors.values==null) {
                        if (InstantiateUtil.isGensymVariable(qn1)) {
                            dstAttributes.add(pf.newAttribute(attribute.getElementName(),
                                                getUUIDQualifiedName(),
                                                pf.getName().PROV_QUALIFIED_NAME));
                        }
                        // if not a vargen, then simply drop this attribute
                    } else {
                        // qn1 is a bound variable
                        found = true;
                        if (count!=null) {
                            // it means that the property name was a variable (say prop), it was bound to
                            // the count value associated with prop. We are now about to process the value
                            // of prop which has been determined to be a variable (say prop_value).
                            // We select the 'count' value associated with prop_value.

                            SingleDescriptor srcAttributeDescriptor= singleDescriptors.values.get(count);
                            SingleDescriptors sds=new SingleDescriptors();
                            sds.values=List.of(srcAttributeDescriptor);

                            processTemplateAttributes(dstStatement,
                                                        dstAttributes,
                                                        attributeElementName,
                                                        sds);
                        } else {

                            processTemplateAttributes(dstStatement,
                                                        dstAttributes,
                                                        attributeElementName,
                                                        singleDescriptors);
                        }
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
        return found;
    }

    final boolean allowVariableInLabelAndTime=true;

    public void processTemplateAttributes(Statement dstStatement,
                                          Collection<Attribute> dstAttributes,
                                          QualifiedName elementName,
                                          SingleDescriptors singleDescriptors) {

        for (SingleDescriptor singleDescriptor : singleDescriptors.values) {
            String elementNameUri = elementName.getUri();
            if (InstantiateUtil.LABEL_URI.equals(elementNameUri)) {
                if (allowVariableInLabelAndTime && dUtils.isVariable(singleDescriptor, bindings)) {
                    dstAttributes.add(dUtils.newAttribute(elementName, singleDescriptor, bindings));
                } else {
                    dstAttributes.add(dUtils.newAttribute(pf.getName().PROV_LABEL, singleDescriptor, bindings));
                }
            } else if (InstantiateUtil.TIME_URI.equals(elementNameUri)) {
                if (allowVariableInLabelAndTime && dUtils.isVariable(singleDescriptor, bindings)) {
                    dstAttributes.add(dUtils.newAttribute(elementName, singleDescriptor, bindings));
                } else if (dstStatement instanceof HasTime) {
                    ((HasTime) dstStatement).setTime(pf.newISOTime(asString(singleDescriptor)));
                }
            } else if (InstantiateUtil.STARTTIME_URI.equals(elementNameUri)) {
                if (dstStatement instanceof Activity) {
                    if (allowVariableInLabelAndTime && dUtils.isVariable(singleDescriptor, bindings)) {
                        dstAttributes.add(dUtils.newAttribute(elementName, singleDescriptor, bindings));
                    } else {
                        ((Activity) dstStatement).setStartTime(pf.newISOTime(asString(singleDescriptor)));
                    }
                }
            } else if (InstantiateUtil.ENDTIME_URI.equals(elementNameUri)) {
                if (dstStatement instanceof Activity) {
                    if (allowVariableInLabelAndTime && dUtils.isVariable(singleDescriptor, bindings)) {
                        dstAttributes.add(dUtils.newAttribute(elementName, singleDescriptor, bindings));
                    } else {
                        ((Activity) dstStatement).setEndTime(pf.newISOTime(asString(singleDescriptor)));
                    }
                }
            } else if (InstantiateUtil.IDVAR_URI.equals(elementNameUri)) {
                if (dstStatement instanceof Identifiable) {
                    Identifiable dstStatement1 = (Identifiable) dstStatement;
                    if ((singleDescriptor instanceof QDescriptor) && dstStatement1.getId()==null){
                        dstStatement1.setId(dUtils.newQualifiedName(((QDescriptor) singleDescriptor), bindings));
                    } else {
                        dstAttributes.add(dUtils.newAttribute(elementName, singleDescriptor, bindings));
                    }
                }
            } else {
                dstAttributes.add(dUtils.newAttribute(elementName, singleDescriptor, bindings));
            }
        }
    }

    private String asString(SingleDescriptor singleDescriptor) {
        return ((VDescriptor)singleDescriptor).value;
    }


    static final QualifiedNameUtils qnU = new QualifiedNameUtils();

    public QualifiedName getUUIDQualifiedName() {
        return getUUIDQualifiedName2(pf);
    }

    static public QualifiedName getUUIDQualifiedName2(ProvFactory pf) {
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
        if (InstantiateUtil.isVariable(id)) {
            QDescriptor qDescriptor=env.get(id);
            if (qDescriptor != null && qDescriptor.id != null) {
                u.setter(res, position, dUtils.newQualifiedName(qDescriptor,bindings));
                return true;
            } else {

                if (InstantiateUtil.isGensymVariable(id)) {
                    QualifiedName uuid = getUUIDQualifiedName();
                    u.setter(res, position, uuid);
                    dUtils.addVariable(bindings, id, uuid);
                    return true;
                } else {
                    // allows for null value associated with id
                    if (!preserveUnboundVariables || env.containsKey(id)) {
                       u.setter(res, position, null);
                    }
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
            throw new UnsupportedOperationException("can't expand HadMember with more than one members");
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
    public void doAction(QualifiedHadMember s) {
        QualifiedHadMember res = pf.newQualifiedHadMember(s.getId(),s.getCollection(), s.getEntity(),null);

        QualifiedName id = res.getId();
        boolean updated0 = setExpand(res, id, 0);
        QualifiedName col = res.getCollection();
        boolean updated1 = setExpand(res, col, 1);
        List<QualifiedName> ent = res.getEntity();
        if (ent.size() > 1) {
            throw new UnsupportedOperationException("can't expand QualfiedHadMember with more than one members");
        }
        boolean updated2 = setExpand(res, ent.get(0), 2);
        boolean updated3 = expandAttributes(s, res);

        boolean updated = updated0 || updated1 || updated2 || updated3;
        boolean allUpdated = updated0 && updated1 && updated2 && updated3;
        allExpanded=allExpanded && allUpdated;
        if (!allUpdatedRequired || allUpdated) {
            ll.add(res);
        }
        if (updated) addOrderAttribute(res);

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

        QualifiedName id = res.getId();
        boolean updated0 = setExpand(res, id, 0);
        QualifiedName spe = res.getSpecificEntity();
        boolean updated1 = setExpand(res, spe, 1);
        QualifiedName gen = res.getGeneralEntity();
        boolean updated2 = setExpand(res, gen, 2);
        boolean updated3 = expandAttributes(s, res);

        @SuppressWarnings("unused")
        boolean updated = updated0 || updated1 || updated2 || updated3;
        boolean allUpdated = updated0 && updated1 && updated2 && updated3;
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
        List<Statement> newStatements = new LinkedList<>();

        for (Statement s : statements) {
            for (StatementOrBundle sb : instantiater.expand(s,  bindings, grp1,  InstantiateUtil.usedGroups(s, grp1, bindings))) {
                newStatements.add((Statement) sb);
            }

        }

        updateEnvironmentForBundleId(bun, bindings, env);

        QualifiedName newId;
        final QualifiedName bunId = bun.getId();
        if (InstantiateUtil.isVariable(bunId)) {
            QDescriptor qDescriptor=env.get(bunId);
            if (qDescriptor != null) {
                newId = dUtils.newQualifiedName(qDescriptor, bindings);
            } else {
                if (InstantiateUtil.isGensymVariable(bunId)) {
                    QualifiedName uuid = getUUIDQualifiedName();
                    newId = uuid;
                    dUtils.addVariable(bindings, bunId, uuid);
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
                                             Bindings bindings,
                                             Map<QualifiedName, QDescriptor> env) {
        final QualifiedName id = bun.getId();
        if (InstantiateUtil.isVariable(id)) {
            Descriptors descriptors=bindings.var.get(id.getLocalPart());
            if (descriptors==null) {
                if (bindings.vargen!=null) {
                    descriptors = bindings.vargen.get(id.getLocalPart());
                }
            }
            if (descriptors == null) {
                if (InstantiateUtil.isGensymVariable(id)) {
                    // OK, we'll generate a uuid later
                } else {
                    throw new BundleVariableHasNoValue(id);
                }
            } else {
                if (descriptors.values.size() > 1) {
                    throw new BundleVariableHasMultipleValues(id, descriptors);
                } else {
                    Descriptor descriptor = descriptors.values.get(0);
                    env.put(id, (QDescriptor) descriptor);
                }
            }
        }
    }



    public List<StatementOrBundle> getList() {
        return ll;
    }

}
