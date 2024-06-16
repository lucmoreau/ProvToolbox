package org.openprovenance.prov.service.commonbean.simple;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;
import org.openprovenance.prov.template.library.plead.sql.common.SqlBeanCompleter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlEnactorImplementation  extends org.openprovenance.prov.template.library.plead.sql.common.SqlEnactorImplementation {

    private final Storage storage;
    private final Connection conn;

    public SqlEnactorImplementation(Storage storage, Connection conn) {
        this.storage=storage;
        this.conn=conn;
    }

    @Override
    public ResultSet executeQuery(String statement) throws SQLException {
        return storage.executeQuery(conn, statement);
    }

    @Override
    public BeanCompleter beanCompleterFactory(ResultSet rs) {
        return new SqlBeanCompleter(rs);
    }

}
