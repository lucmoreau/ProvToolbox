package org.openprovenance.prov.service.commonbean.composite;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.common.BeanChecker;
import org.openprovenance.prov.template.library.plead.client.common.BeanEnactor;
import org.openprovenance.prov.template.library.plead.client.common.BeanProcessor;
import org.openprovenance.prov.template.library.plead.client.common.Delegator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Function;


public class SqlCompositeBeanEnactor extends BeanEnactor<ResultSet> {

    static final class CheckerAndFixShared extends Delegator {
        CheckerAndFixShared(BeanProcessor bc) {
            super(bc);
        }

        /*
        public Setting_objective_compositeBean process(Setting_objective_compositeBean bean) {
            bean.__elements.forEach(b1 -> {
                b1.policy1=-1;
                b1.setting=-2;
            });
            return super.process(bean);
        }

         */
    }

    public SqlCompositeBeanEnactor(Function<String,ResultSet> querier) {
            super(new  SqlCompositeEnactorImplementation(querier), new CheckerAndFixShared(new BeanChecker()));
    }

}


