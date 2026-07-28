/* Generated from Java with JSweet 3.1.0 - http://www.jsweet.org */
var java;
(function (java) {
    var util;
    (function (util) {
        var concurrent;
        (function (concurrent) {
            var atomic;
            (function (atomic) {
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
                    constructor(initialValue) {
                        if (((typeof initialValue === 'number') || initialValue === null)) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = 0;
                            }
                            this.value = initialValue;
                        }
                        else if (initialValue === undefined) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = 0;
                            }
                            this.value = 0;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    get() {
                        return this.value;
                    }
                    set(newValue) {
                        this.value = newValue;
                    }
                    getAndSet(newValue) {
                        const previous = this.value;
                        this.value = newValue;
                        return previous;
                    }
                    getAndIncrement() {
                        return this.value++;
                    }
                    getAndDecrement() {
                        return this.value--;
                    }
                    incrementAndGet() {
                        return ++this.value;
                    }
                    decrementAndGet() {
                        return --this.value;
                    }
                    getAndAdd(delta) {
                        const previous = this.value;
                        this.value = this.value + delta;
                        return previous;
                    }
                    addAndGet(delta) {
                        this.value = this.value + delta;
                        return this.value;
                    }
                    compareAndSet(expectedValue, newValue) {
                        if (this.value === expectedValue) {
                            this.value = newValue;
                            return true;
                        }
                        return false;
                    }
                    intValue() {
                        return this.value;
                    }
                    longValue() {
                        return this.value;
                    }
                    floatValue() {
                        return this.value;
                    }
                    doubleValue() {
                        return this.value;
                    }
                    toString() {
                        return /* valueOf */ String(this.value).toString();
                    }
                }
                atomic.AtomicInteger = AtomicInteger;
                AtomicInteger["__class"] = "org.openprovenance.candies.java.util.concurrent.atomic.AtomicInteger";
            })(atomic = concurrent.atomic || (concurrent.atomic = {}));
        })(concurrent = util.concurrent || (util.concurrent = {}));
    })(util = java.util || (java.util = {}));
})(java || (java = {}));
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var apache;
        (function (apache) {
            var commons;
            (function (commons) {
                var lang;
                (function (lang) {
                    class StringEscapeUtils {
                        static escapeJavaScript(format) {
                            return format;
                        }
                        static escapeCsv(format) {
                            const newFormat = format.split("\"").join("\"\"");
                            if (newFormat === format) {
                                if ( /* contains */(format.indexOf(",") != -1) || /* contains */ (format.indexOf("\n") != -1) || /* contains */ (format.indexOf("\r") != -1)) {
                                    return "\"" + format + "\"";
                                }
                                else {
                                    return format;
                                }
                            }
                            else {
                                return "\"" + newFormat + "\"";
                            }
                        }
                    }
                    lang.StringEscapeUtils = StringEscapeUtils;
                    StringEscapeUtils["__class"] = "org.openprovenance.apache.commons.lang.StringEscapeUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
