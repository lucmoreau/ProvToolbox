package org.openprovenance.prov.service.commonbean.simple;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.common.BeanChecker;
import org.openprovenance.prov.template.library.plead.client.common.BeanEnactor;

import java.sql.Connection;
import java.sql.ResultSet;


public class SqlBeanEnactor extends BeanEnactor<ResultSet> {

    public SqlBeanEnactor(Storage storage, Connection conn) {
        super(new  SqlEnactorImplementation(storage,conn), new BeanChecker());
    }



}
