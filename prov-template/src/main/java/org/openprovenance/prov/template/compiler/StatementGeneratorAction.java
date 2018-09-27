package org.openprovenance.prov.template.compiler;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
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
import org.openprovenance.prov.template.expander.ExpandUtil;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

public class StatementGeneratorAction implements StatementAction {

    private Set<QualifiedName> allVars;
    private Set<QualifiedName> allAtts;
    private Builder builder;
    private String target;
    private ProvFactory pFactory;
    private Hashtable<QualifiedName, String> vmap;
    
    static final ClassName cl_collection = ClassName.get("java.util", "Collection");
    static final ClassName cl_list = ClassName.get("java.util", "List");
    static final ClassName cl_linkedList = ClassName.get("java.util", "LinkedList");
    static final ClassName cl_attribute = ClassName.get("org.openprovenance.prov.model", "Attribute");
    static final TypeName  cl_linkedListOfAttributes = ParameterizedTypeName.get(cl_linkedList, cl_attribute);
    static final TypeName  cl_listOfAttributes = ParameterizedTypeName.get(cl_list, cl_attribute);
    static final TypeName  cl_collectionOfAttributes = ParameterizedTypeName.get(cl_collection, cl_attribute);

    public StatementGeneratorAction(ProvFactory pFactory, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Hashtable<QualifiedName, String> vmap, Builder builder, String target) {
        this.pFactory=pFactory;
        this.allVars=allVars;
        this.allAtts=allAtts;
        this.builder=builder;
        this.target=target;
        this.vmap=vmap;
    }

    public String local(QualifiedName id) {
        return (id==null)? "nullqn" : id.getLocalPart();
    }
    
    @Override
    public void doAction(Activity s) {
        // TODO, start and end time
        builder.addStatement(target + ".add(pf.newActivity($N,null,null" + generateAttributes(s) + "))", s.getId().getLocalPart());        
    }

    @Override
    public void doAction(Used s) {
        //TODO time
        final String activity = local(s.getActivity());
        final String entity = local(s.getEntity());
        builder.addStatement("if (($N!=null) &&  ($N!=null)) " + target + ".add(pf.newUsed($N,$N,$N,null" + generateAttributes(s) + "))", activity, entity, local(s.getId()), activity, entity);              
    }



    @Override
    public void doAction(WasStartedBy s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(Agent s) {
        builder.addStatement(target + ".add(pf.newAgent($N" + generateAttributes(s) + "))", s.getId().getLocalPart());        
    }

    @Override
    public void doAction(AlternateOf s) {
        builder.addStatement(target + ".add(pf.newAlternateOf($N,$N))", local(s.getAlternate2()), local(s.getAlternate1()));               
    }

    @Override
    public void doAction(WasAssociatedWith s) {
        builder.addStatement(target + ".add(pf.newWasAssociatedWith($N,$N,$N,$N" + generateAttributes(s) + "))", local(s.getId()), local(s.getActivity()), local(s.getAgent()), local(s.getPlan()));             
      
    }

    @Override
    public void doAction(WasAttributedTo s) {
        builder.addStatement(target + ".add(pf.newWasAttributedTo($N,$N,$N))", local(s.getId()), local(s.getEntity()), local(s.getAgent()));              
       
    }

    @Override
    public void doAction(WasInfluencedBy s) {
        builder.addStatement(target + ".add(pf.newWasInfluencedBy($N,$N,$N))", local(s.getId()), local(s.getInfluencee()), local(s.getInfluencer()));              
    }

    @Override
    public void doAction(ActedOnBehalfOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(WasDerivedFrom s) {
        final String generated = local(s.getGeneratedEntity());
        final String used = local(s.getUsedEntity());
        builder.addStatement("if (($N!=null) &&  ($N!=null)) " + target + ".add(pf.newWasDerivedFrom($N,$N,$N))", generated, used, local(s.getId()), generated, used);             
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(Entity s) {
        final String var = s.getId().getLocalPart();
        builder.addStatement("if ($N!=null) " + target + ".add(pf.newEntity($N" + generateAttributes(s) + "))", var, var);         
    }

    public String generateAttributes(Statement s) {
        Collection<Attribute> attributes = doAttributesAction(s); 
        return (attributes.isEmpty()) ? "" : ", attrs";
    }

    public Collection<Attribute> doAttributesAction(Statement s) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        if (!(attributes.isEmpty())) {
            builder.addStatement("attrs=new $T()",cl_linkedListOfAttributes);

            for (Attribute attribute:attributes) {
                QualifiedName element=attribute.getElementName();
                QualifiedName typeq=attribute.getType();
                Object value=attribute.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName vq=(QualifiedName) value;
                    if (ExpandUtil.isVariable(vq)) {
                        if (reservedElement(element)) {
                            doReservedAttributeAction(builder,element,vq,typeq);
                        } else {
                            // use attribute variables (expected to be of type Object) and calculates its type as a QualifiedName dynamically.
                            builder.addStatement("attrs.add(pf.newAttribute($N,$N,vc.getXsdType($N)))",
                                                 vmap.get(element),
                                                 vq.getLocalPart(),
                                                 vq.getLocalPart());
                        }

                    } else {
                        builder.addStatement("attrs.add(pf.newAttribute($N,$N,$N))",
                                             vmap.get(element),
                                             vmap.get(vq),
                                             vmap.get(typeq));
                    }
                } else {
                    builder.addStatement("attrs.add(pf.newAttribute($N,$S,$N))",
                                         vmap.get(element),
                                         value.toString(),
                                         vmap.get(typeq));
                }
            }
        }
        return attributes;
    }

    public void doReservedAttributeAction(Builder builder2,
                                           QualifiedName element,
                                           QualifiedName vq,
                                           QualifiedName typeq) {
        final String elementName = element.getUri();
        if (ExpandUtil.LABEL_URI.equals(elementName)) {
            builder.addStatement("attrs.add(pf.newAttribute($N,$N,vc.getXsdType($N)))",
                                 vmap.get(pFactory.getName().PROV_LABEL),
                                 vq.getLocalPart(),
                                 vq.getLocalPart());
           
        } else if (ExpandUtil.TIME_URI.equals(elementName)) {
            throw new UnsupportedOperationException();
        } else if (ExpandUtil.STARTTIME_URI.equals(elementName)) {
            throw new UnsupportedOperationException();
        } else if (ExpandUtil.ENDTIME_URI.equals(elementName)) {
            throw new UnsupportedOperationException();
        } else {
            // can never be here
            throw new UnsupportedOperationException();
        }
        
    }

    public boolean reservedElement(QualifiedName element) {
        final String elementName = element.getUri();
        return (ExpandUtil.LABEL_URI.equals(elementName)) 
                || (ExpandUtil.TIME_URI.equals(elementName))
                || (ExpandUtil.STARTTIME_URI.equals(elementName))
                || (ExpandUtil.ENDTIME_URI.equals(elementName));

    }

    @Override
    public void doAction(WasGeneratedBy s) {
        builder.addStatement(target + ".add(pf.newWasGeneratedBy($N,$N,$N))", local(s.getId()), local(s.getEntity()), local(s.getActivity()));              
       
    }

    @Override
    public void doAction(WasInvalidatedBy s) {
        builder.addStatement(target + ".add(pf.newWasInvalidatedBy($N,$N,$N))", local(s.getId()), local(s.getEntity()), local(s.getActivity()));              
    }

    @Override
    public void doAction(HadMember s) {
        builder.addStatement(target + ".add(pf.newHadMember($N,$N))", local(s.getCollection()), local(s.getEntity().get(0)));              
    }

    @Override
    public void doAction(MentionOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(SpecializationOf s) {
       builder.addStatement(target + ".add(pf.newSpecializationOf($N,$N))", local(s.getSpecificEntity()), local(s.getGeneralEntity()));               
    }

    @Override
    public void doAction(QualifiedSpecializationOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(QualifiedAlternateOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(QualifiedHadMember s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(DerivedByInsertionFrom s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(WasInformedBy s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(Bundle bun, ProvUtilities provUtilities) {
        // TODO Auto-generated method stub
        
        final String id = bun.getId().getLocalPart();
        final String id_ = id + "_";
        builder.addStatement("$T $N = pf.newNamedBundle($N,pf.newNamespace(),null)", Bundle.class, id_, id);
        builder.addStatement(target + ".add($N)", id_);

        String target2 = id_+".getStatement()";
        StatementGeneratorAction action2=new StatementGeneratorAction(pFactory, allVars, allAtts, vmap, builder, target2);
        
        for (Statement s: bun.getStatement()) {
            provUtilities.doAction(s, action2);

        }

    }

}
