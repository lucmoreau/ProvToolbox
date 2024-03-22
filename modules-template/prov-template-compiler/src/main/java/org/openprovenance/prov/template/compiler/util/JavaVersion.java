package org.openprovenance.prov.template.compiler.util;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath;

public class JavaVersion {
    public static final String JAVA12_HOME = "JAVA12_HOME";
    final static String java12Home=System.getenv(JAVA12_HOME);
    final static String javaVersion=System.getProperty("java.version");
    final static String javaHome=System.getProperty("java.home");


    public String getJava12Home() {
        if (javaVersion.startsWith("12")) {
            return javaHome;
        }

        if (java12Home!=null) {
            return java12Home;
        }

        String embeddedJava12Home = Objects.requireNonNull(getPropertiesFromClasspath(JavaVersion.class, "config.properties")).getProperty("java12.home");
        if (embeddedJava12Home!=null) {
            if (Files.exists(Paths.get(embeddedJava12Home))) {
                return embeddedJava12Home;
            }
        }

        return null;
    }


}
