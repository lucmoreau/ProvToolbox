package org.openprovenance.prov.template;

import java.util.Set;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
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

import com.squareup.javapoet.MethodSpec.Builder;

public class StatementGeneratorAction implements StatementAction {

    private Set<QualifiedName> allVars;
    private Set<QualifiedName> allAtts;
    private Builder builder;
    private String target;

    public StatementGeneratorAction(Set<QualifiedName> allVars, Set<QualifiedName> allAtts, Builder builder, String target) {
        this.allVars=allVars;
        this.allAtts=allAtts;
        this.builder=builder;
        this.target=target;
    }

    public String local(QualifiedName id) {
        return (id==null)? "nullqn" : id.getLocalPart();
    }
    
    @Override
    public void doAction(Activity s) {
        builder.addStatement(target + ".add(pf.newActivity($N))", s.getId().getLocalPart());        
    }

    @Override
    public void doAction(Used s) {
        builder.addStatement(target + ".add(pf.newUsed($N,$N,$N))", local(s.getId()), local(s.getActivity()), local(s.getEntity()));              
    }



    @Override
    public void doAction(WasStartedBy s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(Agent s) {
        builder.addStatement(target + ".add(pf.newAgent($N))", s.getId().getLocalPart());        
    }

    @Override
    public void doAction(AlternateOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(WasAssociatedWith s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(WasAttributedTo s) {
        builder.addStatement(target + ".add(pf.newWasAttributedTo($N,$N,$N))", local(s.getId()), local(s.getEntity()), local(s.getAgent()));              
       
    }

    @Override
    public void doAction(WasInfluencedBy s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(ActedOnBehalfOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(WasDerivedFrom s) {
        builder.addStatement(target + ".add(pf.newWasDerivedFrom($N,$N,$N))", local(s.getId()), local(s.getGeneratedEntity()), local(s.getUsedEntity()));             
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
        builder.addStatement(target + ".add(pf.newEntity($N))", s.getId().getLocalPart());        
       
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
     
    }

    @Override
    public void doAction(MentionOf s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doAction(SpecializationOf s) {
        // TODO Auto-generated method stub
        
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
        builder.addStatement("$T $N = pf.newNamedBundle($N,null,null)", Bundle.class, id_, id);
        builder.addParameter(QualifiedName.class, id);
        builder.addStatement(target + ".add($N)", id_);

        String target2 = id_+".getStatement()";
        StatementGeneratorAction action2=new StatementGeneratorAction(allVars, allAtts, builder, target2);
        
        for (Statement s: bun.getStatement()) {
            provUtilities.doAction(s, action2);

        }

    }

}
