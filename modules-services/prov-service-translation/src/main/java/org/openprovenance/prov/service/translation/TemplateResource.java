package org.openprovenance.prov.service.translation;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.template.expander.Bindings;

public class TemplateResource extends DocumentResource implements Cloneable {

    public Bindings bindings;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public TemplateResource() {}


    public TemplateResource(DocumentResource d) {


        this.graphId=d.graphId;
        this.bundle=d.bundle;
        this.mimeType=d.mimeType;
        this.filepath=d.filepath;
        this.graphpath=d.graphpath;

        this.dotFilepath=d.dotFilepath;
        this.svgFilepath=d.svgFilepath;
        this.pdfFilepath=d.pdfFilepath;
        this.url=d.url;

        this.thrown=d.thrown;

        this.format=d.format;

        this.document=d.document;


        this.complete=d.complete;

        this.reportFile=d.reportFile;

        this.jsonFile=d.jsonFile;

        this.deposited=d.deposited;


        this.expires=d.expires;
    }
}
