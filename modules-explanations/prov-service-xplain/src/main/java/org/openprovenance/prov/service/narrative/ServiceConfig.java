package org.openprovenance.prov.service.narrative;

import org.openprovenance.prov.scala.interop.Input;
import org.openprovenance.prov.scala.interop.Output;
import scala.Option;
import scala.collection.immutable.Seq;
import scala.jdk.CollectionConverters;


import java.util.LinkedList;
import java.util.List;

public  class ServiceConfig implements org.openprovenance.prov.scala.nlg.Config {

    private final String template;
    private final String profile0;
    private final String language;
    private final String batch;
    private final int format_option;
    private String infiles;

    ServiceConfig(String template, String profile0, String language, int format_option) {
        if (template.startsWith("batch:")) {
            this.template = null;
            this.batch=template.substring("batch:".length());
        } else {
            this.template=template;
            this.batch=null;
        }
        this.profile0=profile0;
        this.language=language;
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
        List<String> templates=new LinkedList<>();
        if (templates!=null) templates.add(template);
        return convertListToSeq(templates);
    }

    @Override
    public String profile() {
        return profile0;
    }

    @Override
    public Option<String> batch_templates() {
        if (batch==null) {
            return Option.empty();
        } else {
            return Option.apply(batch);
        }
    }

    @Override
    public Seq<String> language() {
        List<String> languages=new LinkedList<>();
        languages.add(language);
        return convertListToSeq(languages);
    }

    @Override
    public boolean linear() {
        return false;
    }

    @Override
    public Input infile() {
        throw new UnsupportedOperationException("ServiceConfig.infile() method is not implemented");
    }

    @Override
    public int format_option() {
        return format_option;
    }

    @Override
    public String infiles() {
        return infiles;
    }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "template='" + template + '\'' +
                ", profile0='" + profile0 + '\'' +
                ", language='" + language + '\'' +
                ", batch='" + batch + '\'' +
                ", format_option=" + format_option +
                '}';
    }

    public Seq<String> convertListToSeq(List<String> inputList) {
        return CollectionConverters.ListHasAsScala(inputList).asScala().toSeq();
    }


}
