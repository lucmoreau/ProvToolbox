package org.openprovenance.prov.template.core.ttf;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public class TemplateTasksBatch {
    public List<String> template_path;
    public List<String> bindings_path;
    public String output_dir;

    public List<String> variableMaps;

    // jackson annotation to allow polymorphic deserialization for ExpandTask and MergeTask
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes(value = {
            @JsonSubTypes.Type(value = InstantiateTask.class, name = "instantiate"),
            @JsonSubTypes.Type(value = MergeTask.class, name = "merge")
    })
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
