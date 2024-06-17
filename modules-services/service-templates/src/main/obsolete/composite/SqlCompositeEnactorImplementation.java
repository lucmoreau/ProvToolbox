package org.openprovenance.prov.service.commonbean.composite;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.service.commonbean.simple.SqlEnactorImplementation;
import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;
import org.openprovenance.prov.template.library.plead.sql.common.SqlCompositeBeanCompleter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Function;

public class SqlCompositeEnactorImplementation extends SqlEnactorImplementation {

    public SqlCompositeEnactorImplementation(Function<String,ResultSet> querier) {
        super(querier);
    }

    @Override
    public BeanCompleter beanCompleterFactory(ResultSet rs) {
        return new SqlCompositeBeanCompleter(rs);
    }
}
