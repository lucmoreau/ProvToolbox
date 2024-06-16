package org.openprovenance.prov.service.obsolete;

import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;
import org.openprovenance.prov.template.library.plead.client.integrator.Plead_transformingOutputs;
import org.openprovenance.prov.template.library.plead.client.integrator.Plead_transforming_compositeOutputs;

import java.util.Map;

// TODO: generate the following methods for composee and composite outputs

abstract public class BeanCompleter3 extends BeanCompleter2 {


    public BeanCompleter3(Map<String, Object> m) {
        super(m);
    }
    public  BeanCompleter3(Getter getter) {
        super(getter);
    }

    public Plead_transforming_compositeOutputs process(Plead_transforming_compositeOutputs bean) {
        Plead_transforming_compositeOutputs result=super.process(bean);
        result.ID=getValueFromLocation();
        return result;
    }

    public Plead_transformingOutputs process(Plead_transformingOutputs bean) {
        super.process(bean);
        setValueInLocation();
        return bean;
    }

    abstract public void setValueInLocation();

    abstract public Integer getValueFromLocation();


}
