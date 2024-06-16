package org.openprovenance.prov.service.commonbean.simple;

import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;
import org.openprovenance.prov.template.library.plead.client.common.BeanEnactor;
import org.openprovenance.prov.template.library.plead.sql.common.SqlBeanCompleter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SqlEnactorImplementation  implements BeanEnactor.EnactorImplementation<ResultSet> {


    private final Storage storage;
    private final Connection conn;

    public SqlEnactorImplementation(Storage storage, Connection conn) {
        this.storage=storage;
        this.conn=conn;
    }

    @Override
    public <T> T generic_enact(T bean,
                               Consumer<T> check,
                               BiConsumer<StringBuilder, T> composeQuery,
                               BiConsumer<ResultSet, T> completeBean) {
        check.accept(bean);
        StringBuilder sb = new StringBuilder();
        composeQuery.accept(sb, bean);

        String statement = sb.toString();
        ResultSet rs;
        try {
            rs = storage.executeQuery(conn, statement);
            if (!rs.next()) {
                rs.close();
                throw new SQLException("Single row result was expected but result set is empty ");
            }
            completeBean.accept(rs, bean);
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
        return bean;
    }


    @Override
    public BeanCompleter beanCompleterFactory(ResultSet rs) {
        return new SqlBeanCompleter(rs);
    }

    public static void printResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            do  {  // in generic_enact, we have already done next(), we are on the first record
                System.out.print ("Row " + resultSet.getRow() + " ");
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            } while (resultSet.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
