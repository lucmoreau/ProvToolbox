package org.openprovenance.prov.template;

import java.util.HashSet;
import java.util.Set;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;;

public class BindingsBeanGenerator {
    
    final private ProvFactory pFactory;


    public BindingsBeanGenerator(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
    
    static ProvUtilities u= new ProvUtilities();

    
    public Object fromDocument(Document doc) {


        Bundle bun=u.getBundle(doc).get(0);
        
        Set<QualifiedName> allVars=new HashSet<QualifiedName>();
        Set<QualifiedName> allAtts=new HashSet<QualifiedName>();
        
        for (Statement statement: bun.getStatement()) {
            Set<QualifiedName> vars=ExpandUtil.freeVariables(statement);
            allVars.addAll(vars);
            Set<QualifiedName> vars2=ExpandUtil.freeAttributeVariables(statement, pFactory);
            allAtts.addAll(vars2);
        }
        
        System.out.println("BEAN var: " + allVars);
        System.out.println("BEAN Att: " + allAtts);
        
        return null;

    }

}
