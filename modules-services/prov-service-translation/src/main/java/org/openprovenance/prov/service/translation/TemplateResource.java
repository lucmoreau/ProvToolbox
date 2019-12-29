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

        this.setVisibleId(d.getVisibleId());
        this.setStorageId(d.getStorageId());
        this.setExpires(d.getExpires());

        this.thrown=d.thrown;

        this.setDocument(d.document());

    }
}
