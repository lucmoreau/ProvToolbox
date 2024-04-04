package org.openprovenance.prov.service.iobean.simple;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanChecker2;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanEnactor2;

import java.sql.Connection;
import java.sql.ResultSet;


public class SqlBeanEnactor3 extends BeanEnactor2<ResultSet> {

    public SqlBeanEnactor3(Storage storage, Connection conn) {
        super(new SqlEnactorImplementation3(storage,conn), new BeanChecker2());
    }




}
