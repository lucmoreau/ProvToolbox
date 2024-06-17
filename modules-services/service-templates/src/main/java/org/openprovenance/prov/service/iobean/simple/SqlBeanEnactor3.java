package org.openprovenance.prov.service.iobean.simple;

import org.openprovenance.prov.service.iobean.composite.SqlCompositeEnactorImplementation3;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanChecker2;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanEnactor2;

import java.sql.ResultSet;
import java.util.function.Function;


public class SqlBeanEnactor3 extends BeanEnactor2<ResultSet> {

    public SqlBeanEnactor3(Function<String,ResultSet> querier) {
        super(new SqlCompositeEnactorImplementation3(querier), new BeanChecker2());
    }




}
