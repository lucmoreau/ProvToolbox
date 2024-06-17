package org.openprovenance.prov.service.obsolete;


import java.sql.ResultSet;
import java.sql.SQLException;


class SqlCompositeBeanCompleter extends SqlBeanCompleter {

    private final ResultSet rs;

    public SqlCompositeBeanCompleter(ResultSet rs) {
        super(rs);
        this.rs=rs;
    }

    @Override
    public boolean next() {
        try {
            return rs.next();  // ResultSet next() has already been called in generic_enact
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
