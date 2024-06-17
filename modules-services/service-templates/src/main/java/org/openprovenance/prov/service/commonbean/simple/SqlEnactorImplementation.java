package org.openprovenance.prov.service.commonbean.simple;

import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;
import org.openprovenance.prov.template.library.plead.sql.common.SqlBeanCompleter;

import java.sql.ResultSet;
import java.util.function.Function;


public class SqlEnactorImplementation  extends org.openprovenance.prov.template.library.plead.sql.common.SqlEnactorImplementation {


    public SqlEnactorImplementation(Function<String,ResultSet> querier) {
        super(querier);
    }

    @Override
    public BeanCompleter beanCompleterFactory(ResultSet rs) {
        return new SqlBeanCompleter(rs);
    }

}
