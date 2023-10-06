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
    protected final Namespace namespace;
    protected final ProvFactory pf;
    protected final HashMap<String,QualifiedName> knownAs;

    protected List<Statement> statements=new LinkedList<>();
    protected List<Bundle> bundles=new LinkedList<>();

    public Builder(ModelConstructor mc, ProvFactory pf) {
        this.mc = mc;
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
    public WasDerivedFromBuilder wasDerivedFrom() {
        return new WasDerivedFromBuilder(this, mc, pf);
    }
    public UsedBuilder used() {
        return new UsedBuilder(this,mc,pf);
    }
    public WasGeneratedByBuilder wasGeneratedBy() {
        return new WasGeneratedByBuilder(this,mc,pf);
    }


    public Document build() {
        return mc.newDocument(namespace,statements,bundles);
    }

    public Builder prefix(String prefix, String ns) {
        namespace.register(prefix,ns);
        return this;
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


}