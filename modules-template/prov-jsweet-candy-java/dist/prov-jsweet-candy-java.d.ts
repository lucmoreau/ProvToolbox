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
