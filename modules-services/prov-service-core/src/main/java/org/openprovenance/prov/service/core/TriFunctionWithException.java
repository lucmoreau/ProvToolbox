package org.openprovenance.prov.service.core;

public interface TriFunctionWithException<T, U, V, R, E extends Throwable> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argumen
     * @param v the thrid function argumen
     * @return the function result
     */
    R apply(T t, U u, V v) throws E;
}


