package org.openprovenance.prov.template.service.inface;

import java.sql.SQLException;

@FunctionalInterface
public interface BiConsumerWithException<T, U> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @throws SQLException a SQL exception
     */
    void accept(T t, U u) throws SQLException;
}