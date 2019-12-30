package org.openprovenance.prov.service.translation;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.template.expander.Bindings;

public interface TemplateResource extends DocumentResource, Cloneable{
     String TEMPLATE = "TEMPLATE";
     static String getResourceKind() {
          return TEMPLATE;
     }

     Bindings getBindings() ;

     void setBindings(Bindings bindings);

}
