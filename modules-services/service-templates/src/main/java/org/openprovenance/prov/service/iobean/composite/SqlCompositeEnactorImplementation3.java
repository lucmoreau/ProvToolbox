package org.openprovenance.prov.service.iobean.composite;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.service.iobean.simple.SqlEnactorImplementation3;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;

import java.sql.Connection;
import java.sql.ResultSet;

public class SqlCompositeEnactorImplementation3 extends SqlEnactorImplementation3 {

    public SqlCompositeEnactorImplementation3(Storage storage, Connection conn) {
        super(storage,conn);
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
