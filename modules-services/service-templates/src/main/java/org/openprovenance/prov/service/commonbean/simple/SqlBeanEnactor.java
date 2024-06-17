package org.openprovenance.prov.service.commonbean.simple;

import org.openprovenance.prov.template.library.plead.client.common.BeanChecker;
import org.openprovenance.prov.template.library.plead.client.common.BeanEnactor;

import java.sql.ResultSet;
import java.util.function.Function;


public class SqlBeanEnactor extends BeanEnactor<ResultSet> {

    public SqlBeanEnactor(Function<String,ResultSet> querier) {
        super(new  SqlEnactorImplementation(querier), new BeanChecker());
    }



}
