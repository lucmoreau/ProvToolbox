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

    public XplainerConfig() {
        this.libraryPath  = "/xplain/plead/template-library.json";
        this.templateList = List.of("plead-generic-responsibility");
    }

    public XplainerConfig(String libraryPath, List<String> templateList) {
        this.libraryPath = libraryPath;
        this.templateList = templateList;
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
        //List<String> ll = List.of("/nlg/templates/plead.cs/plead-template-library.json");
        List<String> ll = List.of(libraryPath);
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
        return 0;
    }

    @Override
    public String infiles() {
        return null;
    }
}
