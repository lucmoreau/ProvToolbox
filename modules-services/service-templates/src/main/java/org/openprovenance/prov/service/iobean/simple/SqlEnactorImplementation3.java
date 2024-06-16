package org.openprovenance.prov.service.iobean.simple;

import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanEnactor2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SqlEnactorImplementation3 implements BeanEnactor2.EnactorImplementation<ResultSet> {


    private final Storage storage;
    private final Connection conn;

    public SqlEnactorImplementation3(Storage storage, Connection conn) {
        this.storage=storage;
        this.conn=conn;
    }

    @Override
    public <IN, OUT> OUT generic_enact(OUT output,
                                       IN bean,
                                       Consumer<IN> check,
                                       BiConsumer<StringBuilder, IN> composeQuery,
                                       BiConsumer<ResultSet, OUT> completeBean) {
        check.accept(bean);
        StringBuilder sb = new StringBuilder();
        composeQuery.accept(sb, bean);

        String statement = sb.toString();
        System.out.println("statement = " + statement);
        ResultSet rs;
        try {
            rs = storage.executeQuery(conn, statement);
            if (!rs.next()) {
                rs.close();
                throw new SQLException("Single row result was expected but result set is empty ");
            }
            completeBean.accept(rs,output);
            if (rs.next()) {
                rs.close();
                throw new SQLException("Single row result was expected for query ");
            } else {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UncheckedException("Issue in enactment " + statement, e);
        }
        return output;
    }



    @Override
    public BeanCompleter2 beanCompleterFactory(ResultSet rs) {
        return new SqlBeanCompleter3(rs);
    }

    @Override
    public BeanCompleter2 beanCompleterFactory(ResultSet rs, Object [] extra) {
        return new SqlBeanCompleter3(rs);
    }

}
