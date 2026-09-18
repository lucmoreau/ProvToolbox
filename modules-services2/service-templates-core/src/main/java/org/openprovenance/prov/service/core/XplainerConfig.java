package org.openprovenance.prov.service.core;

import org.openprovenance.prov.scala.interop.Input;
import org.openprovenance.prov.scala.interop.Output;
import org.openprovenance.prov.scala.narrator.XConfig;
import scala.Option;
import scala.collection.immutable.Seq;

import java.util.List;


public class XplainerConfig implements XConfig {

    private final String   libraryPath      ;
    private final List<String> templateList;
    private final String nlgXplanStrategy;
    private final int format_option;

    public XplainerConfig() {
        this.libraryPath  = "/xplain/plead/template-library.json";
        this.templateList = List.of("plead-generic-responsibility");
        this.nlgXplanStrategy = "default";
        this.format_option = 0;
    }

    public XplainerConfig(String libraryPath, List<String> templateList, String nlgXplanStrategy, int format_option) {
        this.libraryPath = libraryPath;
        this.templateList = templateList;
        this.nlgXplanStrategy = nlgXplanStrategy;
        this.format_option=format_option;
    }

    @Override
    public Output snlg() {
        return null;
    }

    @Override
    public boolean languageAsFilep() {
        return false;
    }

    @Override
    public Seq<String> selected_templates() {
        return scala.jdk.CollectionConverters.CollectionHasAsScala(templateList).asScala().toSeq();
    }

    @Override
    public String profile() {
        return "";
    }

    @Override
    public Option<String> batch_templates() {
        return Option.empty();
    }

    @Override
    public Seq<String> language() {
        // nlg.xplan.library may name SEVERAL libraries, comma-separated: the explainer's
        // Language.read already flat-maps the plans and dictionaries of every library it
        // is given, so a service with more than one vocabulary (chron + acct + sales in the
        // chronicle store) renders every family's records. A single path is unchanged.
        List<String> ll = new java.util.ArrayList<>();
        for (String path : libraryPath.split(",")) {
            if (!path.isBlank()) {
                ll.add(path.trim());
            }
        }
        return scala.jdk.CollectionConverters.CollectionHasAsScala(ll).asScala().toSeq();
    }

    @Override
    public boolean linear() {
        return false;
    }

    @Override
    public Input infile() {
        return null;
    }

    @Override
    public int format_option() {
        return format_option;
    }

    @Override
    public String infiles() {
        return null;
    }

    @Override
    public String toString() {
        return "XplainerConfig{" +
                "libraryPath='" + libraryPath + '\'' +
                ", templateList=" + templateList +
                ", nlgXplanStrategy='" + nlgXplanStrategy + '\'' +
                '}';
    }


}
