package org.openprovenance.prov.service.iobean.composite;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.service.commonbean.composite.BeanEnactor2Composite;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanChecker2;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanEnactor2;

import java.sql.Connection;
import java.sql.ResultSet;


public class SqlCompositeBeanEnactor3 extends BeanEnactor2Composite<ResultSet> {

    public SqlCompositeBeanEnactor3(Storage storage, Connection conn) {
        super(new SqlCompositeEnactorImplementation3(storage,conn), new BeanChecker2());
    }

}

