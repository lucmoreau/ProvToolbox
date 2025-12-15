package org.openprovenance.prov.service.core;

import org.openprovenance.prov.scala.interop.Input;
import org.openprovenance.prov.scala.interop.Output;
import org.openprovenance.prov.scala.narrator.XConfig;
import scala.Option;
import scala.collection.immutable.Seq;

import java.util.List;


public class XplainerConfig implements XConfig {
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
        List<String> ll = List.of("plead-generic-responsibility");
        return scala.jdk.CollectionConverters.CollectionHasAsScala(ll).asScala().toSeq();
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
        List<String> ll = List.of("/xplain/plead/template-library.json");
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
