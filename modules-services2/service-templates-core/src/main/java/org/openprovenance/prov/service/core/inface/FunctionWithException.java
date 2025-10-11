package org.openprovenance.prov.service.core.inface;


import java.sql.SQLException;

public interface FunctionWithException<T, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @return the function result
     * @throws SQLException a SQL exception
     */
    R call(T t) throws SQLException;
}
