package org.openprovenance.prov.model.builder;


// Using the Builder pattern, define a class Builder that creates instances of the class, created by the factory methods in ModelConstructor.java.
// The Builder class should have a constructor that takes a ModelConstuctor as argument.
// The Builder class should have  methods for each factory method in ModelConstructor.java, such that arguments to the factory method are passed one by one.


import org.openprovenance.prov.model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class Builder {

    protected final ModelConstructor mc;
    protected Namespace namespace;
    protected final ProvFactory pf;
    protected HashMap<String,QualifiedName> knownAs;
    protected final ModelConstructorExtension mce;

    protected List<Statement> statements=new LinkedList<>();
    protected List<Bundle> bundles=new LinkedList<>();

    public Builder(ModelConstructor mc, ModelConstructorExtension mce, ProvFactory pf) {
        this.mc=mc;
        this.mce=mce;
        this.pf=pf;
        this.namespace=new Namespace();
        this.namespace.addKnownNamespaces();
        this.knownAs=new HashMap<>();
    }

    public ActivityBuilder activity() {
        return new ActivityBuilder(this, mc, pf);
    }
    public EntityBuilder entity() {
        return new EntityBuilder(this, mc, pf);
    }
    public AgentBuilder agent() {
        return new AgentBuilder(this, mc, pf);
    }
    public WasDerivedFromBuilder wasDerivedFrom() {
        return new WasDerivedFromBuilder(this, mc, pf);
    }
    public WasAttributedToBuilder wasAttributedTo() {
        return new WasAttributedToBuilder(this, mc, pf);
    }
    public UsedBuilder used() {
        return new UsedBuilder(this,mc,pf);
    }
    public WasGeneratedByBuilder wasGeneratedBy() {
        return new WasGeneratedByBuilder(this,mc,pf);
    }
    public WasInformedByBuilder wasInformedBy() {
        return new WasInformedByBuilder(this,mc,pf);
    }
    public WasStartedByBuilder wasStartedBy() {
        return new WasStartedByBuilder(this,mc,pf);
    }
    public WasEndedByBuilder wasEndedBy() {
        return new WasEndedByBuilder(this,mc,pf);
    }
    public SpecializationOfBuilder specializationOf() {
        return new SpecializationOfBuilder(this,mc,mce,pf);
    }
    public AlternateOfBuilder alternateOf() {
        return new AlternateOfBuilder(this,mc,mce,pf);
    }
    public HadMemberBuilder hadMember() {
        return new HadMemberBuilder(this,mc,mce,pf);
    }
    public WasInfluencedByBuilder wasInfluencedBy() {
        return new WasInfluencedByBuilder(this,mc,pf);
    }
    public WasInvalidatedByBuilder wasInvalidatedBy() {
        return new WasInvalidatedByBuilder(this,mc,pf);
    }
    public ActedOnBehalfOfBuilder actedOnBehalfOf() {
        return new ActedOnBehalfOfBuilder(this,mc,pf);
    }
    public WasAssociatedWithBuilder wasAssociatedWith() {
        return new WasAssociatedWithBuilder(this,mc,pf);
    }


    public Document build() {
        return mc.newDocument(namespace,statements,bundles);
    }

    public BundleBuilder bundle() {
        return new BundleBuilder(this,mc,mce,pf);
    }

    public Builder prefix(Prefix prefix, String ns) {
        namespace.register(prefix.get(),ns);
        return this;
    }
    public Builder prefix(String prefix, String ns) {
        namespace.register(prefix,ns);
        return this;
    }


    final public QualifiedName qn(Prefix prefix, String local) {
        return mc.newQualifiedName(namespace.lookupPrefix(prefix.get()),local,prefix.get());
    }

    final public QualifiedName qn(String prefix, String local) {
        return mc.newQualifiedName(namespace.lookupPrefix(prefix),local,prefix);
    }

    public <T> Builder forEach(Collection<T> collection, Function<T,Builder> function) {
        for (T t: collection) {
            function.apply(t);
        }
        return this;
    }
    public <T> Builder forEach(Collection<T> collection, BiFunction<T,Integer, Builder> function) {
        int count=0;
        for (T t: collection) {
            function.apply(t, count++);
        }
        return this;
    }

    public Builder buildBundle() {
        throw new UnsupportedOperationException("Cannot build a Bundle from a document builder. Call build instead.");
    }


    public Prefix prefix(String pref) {
        return new Prefix(this,pref);
    }

    public QualifiedName PROV_PERSON() {
        return pf.getName().PROV_PERSON;
    }
}