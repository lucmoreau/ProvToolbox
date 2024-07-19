package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BundleBuilder extends Builder {
    private final Builder parentBuilder;
    private final ProvFactory pf;
    private final HashMap<String, QualifiedName> knowAsCopy;
    private QualifiedName id;
    private Namespace namespaceCopy;
    private List<Statement> statementsCopy;

    public BundleBuilder(Builder parentBuilder, ModelConstructor mc, ModelConstructorExtension mce, ProvFactory pf) {
        super(mc, mce, pf);
        this.pf = pf;
        this.parentBuilder = parentBuilder;
        this.namespaceCopy = parentBuilder.namespace;
        this.namespace = new Namespace();
        this.namespace.setParent(this.namespaceCopy);
        this.statementsCopy = parentBuilder.statements;
        this.statements = new LinkedList<>();
        this.knowAsCopy = parentBuilder.knownAs;
        this.knownAs = new HashMap<>();
    }

    public BundleBuilder id(QualifiedName id) {
        this.id = id;
        return this;
    }

    public BundleBuilder id(String prefix, String local) {
        this.id = qn(prefix, local);
        return this;
    }

    public BundleBuilder id(Prefix prefix, String local) {
        this.id = qn(prefix.get(), local);
        return this;
    }

    public Builder buildBundle() {
        Bundle bundle = pf.newNamedBundle(id, namespace, statements);
        parentBuilder.bundles.add(bundle);
        parentBuilder.namespace = namespaceCopy;
        parentBuilder.statements = statementsCopy;
        parentBuilder.knownAs = knowAsCopy;
        return parentBuilder;
    }

    @Override
    public Document build() {
        throw new UnsupportedOperationException("Cannot build a document from a bundle builder. Call cuildBundle instead.");
    }

    public BundleBuilder aka() {
        knownAs.put(id.getLocalPart(), id);
        return this;
    }

    public EntityBuilder entity() {
        return new EntityBuilder(this, mc, pf);
    }

}
