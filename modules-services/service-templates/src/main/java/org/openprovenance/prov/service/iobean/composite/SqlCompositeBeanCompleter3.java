package org.openprovenance.prov.service.iobean.composite;


import org.openprovenance.prov.service.iobean.simple.SqlBeanCompleter3;

import java.sql.ResultSet;
import java.sql.SQLException;


class SqlCompositeBeanCompleter3 extends SqlBeanCompleter3 {

    private final ResultSet rs;

    public SqlCompositeBeanCompleter3(ResultSet rs) {
        super(rs);
        this.rs=rs;
    }

    @Override
    public boolean next() {
        try {
            //System.out.println("Calling next");
            return rs.next();  // ResultSet next() has already been called in generic_enact
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
