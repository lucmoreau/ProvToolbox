package org.openprovenance.prov.service.obsolete;
import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.service.commonbean.composite.SqlCompositeBeanEnactor;
import org.openprovenance.prov.template.library.plead.client.configurator.CompositeEnactorConfigurator;

import java.sql.Connection;

public class SqlCompositeEnactorConfigurator extends CompositeEnactorConfigurator {

    public SqlCompositeEnactorConfigurator(Storage storage, Connection conn) {
        super(new SqlCompositeBeanEnactor(storage, conn));
    }

}
