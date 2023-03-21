package org.openprovenance.prov.template.compiler;

import java.util.function.Consumer;

/**
 * Represents an operation that accepts two input arguments and returns no
 * result.  This is the four-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code FourConsumer} is expected
 * to operate via side-effects.
 *
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @param <W> the type of the fourth argument to the operation
 *
 * @see Consumer

 */
@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {



    void accept(T t, U u, V v, W w);

}
