package org.openprovenance.prov.template.expander.meta;

import java.util.List;

public class TemplateTasksBatch {
    public List<String> template_path;
    public List<String> bindings_path;
    public String output_dir;

    public List<String> variableMaps;
    public List<ConfigTask> tasks;

    public static List<String> addBaseDir(String basedir, List<String> directories) {
        if (directories!=null) {
            for (int i=0; i<directories.size(); i++) {
                if (!directories.get(i).startsWith("/") & !directories.get(i).startsWith(".")) {
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
        public String description;
        public String input;
        public String input2;
        public List<String> template_path;
        public String output;
        public String bindings;
        public List<String> formats;
        public Boolean copyinput;
        public Boolean clean2;
        public String hasProvenance;
        public List<String> variableMaps;
        public boolean addOutputDirToInputPath=false;

        @Override
        public String toString() {
            return "ConfigTask{" +
                    "type='" + type + '\'' +
                    ", description='" + description + '\'' +
                    ", input='" + input + '\'' +
                    ", input2='" + input2 + '\'' +
                    ", template_path=" + template_path +
                    ", output='" + output + '\'' +
                    ", bindings='" + bindings + '\'' +
                    ", formats=" + formats +
                    ", copyinput=" + copyinput +
                    ", clean2=" + clean2 +
                    ", hasProvenance='" + hasProvenance + '\'' +
                    ", variableMaps=" + variableMaps +
                    ", addOutputDirToInputPath=" + addOutputDirToInputPath +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "template_path='" + template_path + '\'' +
                ", bindings_path='" + bindings_path + '\'' +
                ", output_dir='" + output_dir + '\'' +
                ", variableMaps=" + variableMaps +
                ", tasks=" + tasks +
                '}';
    }
}
