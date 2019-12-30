package org.openprovenance.prov.service.translation;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.template.expander.Bindings;

public interface TemplateResource extends DocumentResource, Cloneable{
     public static final String TEMPLATE = "TEMPLATE";
     static public String getResourceKind() {
          return TEMPLATE;
     }

     Bindings getBindings() ;

     void setBindings(Bindings bindings);

}
