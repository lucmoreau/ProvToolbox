package org.openprovenance.prov.service.iobean.composite;

import org.openprovenance.prov.template.library.plead.sql.integration.SqlEnactorImplementation3;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;

import java.sql.ResultSet;
import java.util.function.Function;

public class SqlCompositeEnactorImplementation3 extends SqlEnactorImplementation3 {

    public SqlCompositeEnactorImplementation3(Function<String,ResultSet> querier) {
        super( querier);
    }

    @Override
    public BeanCompleter2 beanCompleterFactory(ResultSet rs) {
        return new SqlCompositeBeanCompleter3(rs);
    }

    @Override
    public BeanCompleter2 beanCompleterFactory(ResultSet rs, Object [] extra) {
        return new SqlCompositeBeanCompleter3(rs, extra);
    }
}
