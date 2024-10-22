package org.openprovenance.prov.service.core;

public interface BiFunctionWithException <T, U, R, E extends Throwable> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argumen
     * @return the function result
     */
    R apply(T t, U u) throws E;
}


