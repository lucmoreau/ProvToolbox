package org.openprovenance.prov.template.expander.meta;

import java.util.List;

public class Config {
    public List<String> mtemplate_dir;
    public String mbindings_dir;
    public String expand_dir;

    public List<String> variableMaps;
    public List<ConfigTask> tasks;

    public static List<String> addBaseDir(String basedir, List<String> directories) {
        if (directories!=null) {
            for (int i=0; i<directories.size(); i++) {
                if (!directories.get(i).startsWith("/")) {
                    directories.set(i, basedir + "/" + directories.get(i));
                }
            }
        }
        return directories;
    }
    public static String addBaseDir(String basedir, String directory) {
        if (directory!=null) {
            if (!directory.startsWith("/")) {
                return basedir + "/" + directory;
            }
        }

        return directory;
    }


    //@JsonPropertyOrder({"type", "src", "output", "bindings", "formats"})
    public static class ConfigTask {
        public String type;
        public String input;
        public String input2;
        public List<String> mtemplate_dir;
        public String output;
        public String bindings;
        public List<String> formats;
        public Boolean copyinput;
        public Boolean clean2;
        public List<String> variableMaps;

        @Override
        public String toString() {
            return "ConfigTask{" +
                    "type='" + type + '\'' +
                    ", input='" + input + '\'' +
                    ", input2='" + input2 + '\'' +
                    ", mtemplate_dir=" + mtemplate_dir +
                    ", output='" + output + '\'' +
                    ", bindings='" + bindings + '\'' +
                    ", formats=" + formats +
                    ", copyinput=" + copyinput +
                    ", clean2=" + clean2 +
                    ", variableMaps=" + variableMaps +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "mtemplate_dir='" + mtemplate_dir + '\'' +
                ", mbindings_dir='" + mbindings_dir + '\'' +
                ", expand_dir='" + expand_dir + '\'' +
                ", variableMaps=" + variableMaps +
                ", tasks=" + tasks +
                '}';
    }
}
