package org.openprovenance.prov.notation.test;

import java.io.File;

/** The files written by the round trip tests of the ANTLR 4 parser are kept apart from those of the ANTLR 3 parser's tests. */
final class Antlr4Files {
    private Antlr4Files() {}

    static String relocate(String file) {
        String relocated = file.startsWith("target/") ? "target/antlr4/" + file.substring("target/".length()) : file;
        File parent = new File(relocated).getParentFile();
        if (parent != null) parent.mkdirs();
        return relocated;
    }
}
