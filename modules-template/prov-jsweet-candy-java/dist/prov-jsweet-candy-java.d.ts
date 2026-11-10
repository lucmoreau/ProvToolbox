declare namespace org.openprovenance.prov.utils {
    /**
     * T31 (R16): JSweet candy of {@code org.openprovenance.prov.utils.Pseudonyms} (prov-model), so that the
     * generated client checks a pseudonymised variable in JavaScript as it does in Java. Patterns are anchored
     * ({@code ^...$}), so {@code RegExp.test} matches the whole value, as Java's {@code matches()} does; the {@code u} flag
     * reads non-ASCII characters as code points.
     * @class
     */
    class Pseudonyms {
        static matches(value: string, pattern: string): boolean;
        static check(value: string, pattern: string, template: string, variable: string, method: string): void;
    }
}
declare namespace java.util.concurrent.atomic {
    /**
     * JSweet candy implementation of {@code java.util.concurrent.atomic.AtomicInteger}.
     *
     * JavaScript is single-threaded, so a plain field provides the required
     * semantics; only the api surface of the JDK class is reproduced (the subset
     * commonly used by generated template code, plus the usual accessors).
     * @param {number} initialValue
     * @class
     */
    class AtomicInteger {
        value: number;
        constructor(initialValue?: any);
        get(): number;
        set(newValue: number): void;
        getAndSet(newValue: number): number;
        getAndIncrement(): number;
        getAndDecrement(): number;
        incrementAndGet(): number;
        decrementAndGet(): number;
        getAndAdd(delta: number): number;
        addAndGet(delta: number): number;
        compareAndSet(expectedValue: number, newValue: number): boolean;
        intValue(): number;
        longValue(): number;
        floatValue(): number;
        doubleValue(): number;
        toString(): string;
    }
}
declare namespace org.openprovenance.apache.commons.lang {
    class StringEscapeUtils {
        static escapeJavaScript(format: string): string;
        static escapeCsv(format: string): string;
    }
}
declare namespace com.fasterxml.jackson.annotation { }
