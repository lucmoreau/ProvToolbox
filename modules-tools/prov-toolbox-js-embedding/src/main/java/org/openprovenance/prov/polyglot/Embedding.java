
package org.openprovenance.prov.polyglot;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;


public class Embedding {

    public static void main(String[] args) throws IOException {
        try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
            Set<String> languages = context.getEngine().getLanguages().keySet();
            for (String id : languages) {
                context.initialize(id);
                switch (id) {

                    case "js":
                        new JavascriptSetup().setup(args, context);
                        Source source= Source.newBuilder("js", Files.readString(Paths.get(args[0]), StandardCharsets.UTF_8), args[0]).build();
                        context.eval(source);
                        break;
                    case "python":
                    case "ruby":
                    case "wasm":
                    case "java":
                    default:
                        throw new RuntimeException("Unsupported language: " + id);

                }
            }
        }
    }

}
