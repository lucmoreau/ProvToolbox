package org.openprovenance.prov.configuration;


import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class Configuration {
    private static String fileName = "config.properties";
    static public final String toolboxVersion;
    static public final String longToolboxVersion;

    static {
        toolboxVersion = getPropertiesFromClasspath(fileName).getProperty("toolbox.version");
        longToolboxVersion = toolboxVersion + " (" + getPropertiesFromClasspath(fileName).getProperty("timestamp") + ")";

    }

    public static Properties getPropertiesFromClasspath(Class<?> clazz, String propFileName) {
        Properties props = new Properties();
        InputStream inputStream = clazz.getResourceAsStream(propFileName);
        if (inputStream == null) {
            return null;
        }
        try {
            props.load(inputStream);
        } catch (IOException ee) {
            return null;
        }
        return props;
    }

    public static Properties getPropertiesFromClasspath(String propFileName) {
        return getPropertiesFromClasspath(Configuration.class, propFileName);
    }



}
