package org.openprovenance.prov.service.obsolete;


import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.service.commonbean.simple.SqlBeanEnactor;
import org.openprovenance.prov.template.library.plead.client.configurator.EnactorConfigurator;

import java.sql.Connection;

public class SqlEnactorConfigurator extends EnactorConfigurator {
    public SqlEnactorConfigurator(Storage storage, Connection conn) {
        super(new SqlBeanEnactor(storage, conn));
    }

}
