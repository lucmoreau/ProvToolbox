package org.openprovenance.prov.polyglot;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.openprovenance.prov.bookptm.DerivationBuilder;
import org.openprovenance.prov.model.builder.Builder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.util.Arrays;

public class JavascriptSetup {




    public void setup(String[] args, Context context) throws IOException {
        System.out.println("args: " + Arrays.asList(args));

        Value jsBindings = context.getBindings("js");
        ProvFactory pFactory = new ProvFactory();
        Builder builder = new Builder(pFactory);
        jsBindings.putMember("pFactory", pFactory);
        jsBindings.putMember("builder", builder);
        jsBindings.putMember("defs", new DerivationBuilder().getDefinitions( builder));
        jsBindings.putMember("serializer", new Serializer());
        jsBindings.putMember("name", new pFactory.getName());

        context.eval("js", "print(builder);");
    }
}
