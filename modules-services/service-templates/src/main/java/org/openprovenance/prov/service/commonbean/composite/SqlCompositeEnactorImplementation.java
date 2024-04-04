package org.openprovenance.prov.service.commonbean.composite;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.service.commonbean.simple.SqlEnactorImplementation;
import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;

import java.sql.Connection;
import java.sql.ResultSet;

public class SqlCompositeEnactorImplementation extends SqlEnactorImplementation {

    public SqlCompositeEnactorImplementation(Storage storage, Connection conn) {
        super(storage,conn);
    }

    @Override
    public BeanCompleter beanCompleterFactory(ResultSet rs) {
        return new SqlCompositeBeanCompleter(rs);
    }
}
