declare namespace org.openprovenance.prov.utils {
    /**
     * T36: JSweet candy of {@code org.openprovenance.prov.utils.JsonText} (prov-model), so that the generated client writes and
     * reads the JSON text of an {@code @inJson} carrier in JavaScript as it does in Java. Both must give the same text for the
     * same values: the conformance vectors of the compiler's tests ({@code t36/json-text-vectors.json}) are run against each.
     * <p>
     * This is the JavaScript implementation: JavaScript has one number type, so a number is written as the language writes it,
     * which is the rule of RFC 8785 ({@code "" + d}); it is not meant to run on a JVM, where prov-model's class is used. Written
     * with what JSweet translates: no {@code BigDecimal}, no {@code String.format}, no exceptions from number parsing. An object is a
     * JavaScript object, so a key that reads as an array index (an integer) is written before the others, whatever its place.
     * @class
     */
    class JsonText {
        static HEX: string;
        static newObject(): any;
        static write(map: any): string;
        static toText(value: any): string;
        /**
         * The text of a number or a boolean as the carrier writes it, a string as itself, null as null (T36 part E: the conversion "as text" of a flow).
         * @param {*} value
         * @return {string}
         */
        static text(value: any): string;
        static writeValue(sb: {
            str: string;
            toString: Function;
        }, v: any): void;
        static writeString(sb: {
            str: string;
            toString: Function;
        }, s: string): void;
        static read(text: string): any;
        static parse(text: string): any;
        static merge(declared: any, others: any, where: string): any;
        static others(object: any, declared: string[]): any;
        static notA(key: string, what: string): Error;
        static getString(m: any, key: string): string;
        static getJson(m: any, key: string): string;
        static getBoolean(m: any, key: string): boolean;
        static getLong(m: any, key: string): number;
        static getInteger(m: any, key: string): number;
        static getDouble(m: any, key: string): number;
        static getFloat(m: any, key: string): number;
        static same(actual: any, constant: any): boolean;
    }
    namespace JsonText {
        class Parser {
            s: string;
            i: number;
            constructor(s: string);
            error(why: string): Error;
            ws(): void;
            value(): any;
            literal(word: string): void;
            object(): any;
            array(): Array<any>;
            hex(c: string): number;
            string(): string;
            digit(): boolean;
            number(): any;
        }
    }
}
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
