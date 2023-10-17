package org.openprovenance.prov.template.expander.meta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    public String mtemplate_dir;
    public String mbindings_dir;
    public String expand_dir;

    public List<String> variableMaps;
    public List<ConfigTask> tasks;



    //@JsonPropertyOrder({"type", "src", "output", "bindings", "formats"})
    public static class ConfigTask {
        public String type;
        public String input;
        public String mtemplate_dir;
        public String output;
        public String bindings;
        public List<String> formats;
        public Boolean copyinput;
        public List<String> variableMaps;

        @Override
        public String toString() {
            return "ConfigTask{" +
                    "type='" + type + '\'' +
                    ", input='" + input + '\'' +
                    ", mtemplate_dir='" + mtemplate_dir + '\'' +
                    ", output='" + output + '\'' +
                    ", bindings='" + bindings + '\'' +
                    ", formats=" + formats +
                    ", copyinput=" + copyinput +
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
