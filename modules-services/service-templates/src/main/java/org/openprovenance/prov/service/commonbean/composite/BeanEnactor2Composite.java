package org.openprovenance.prov.service.commonbean.composite;

import org.openprovenance.prov.template.library.plead.client.integrator.*;

public class BeanEnactor2Composite<RESULT> extends BeanEnactor2<RESULT> {
    private final EnactorImplementation<RESULT> realiser;
    private final InputProcessor checker;

    public BeanEnactor2Composite(EnactorImplementation<RESULT> realiser, InputProcessor checker) {
        super(realiser, checker);
        this.realiser = realiser;
        this.checker = checker;
    }
    /* Overriding process meethod, to call beanCompleterFactory method with extra argument */
    public Plead_transforming_compositeOutputs process(Plead_transforming_compositeInputs bean) {
        return realiser.generic_enact(new Plead_transforming_compositeOutputs(),bean,
                checker::process,
                (sb,b) -> new QueryInvoker2Composite(sb).process(b),
                (rs,b) -> realiser.beanCompleterFactory(rs,new Object[1]).process(b));
    }
}
