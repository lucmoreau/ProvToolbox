/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
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
                    /**
                     * <p>Constructs a <code>CharRange</code> over a set of characters,
                     * optionally negating the range.</p>
                     *
                     * <p>A negated range includes everything except that defined by the
                     * start and end characters.</p>
                     *
                     * <p>If start and end are in the wrong order, they are reversed.
                     * Thus <code>a-e1</code> is the same as <code>e1-a</code>.</p>
                     *
                     * @param {string} start  first character, inclusive, in this range
                     * @param {string} end  last character, inclusive, in this range
                     * @param {boolean} negated  true to express everything except the range
                     * @class
                     * @author Apache Software Foundation
                     */
                    class CharRange {
                        constructor(start, end, negated) {
                            if (((typeof start === 'string') || start === null) && ((typeof end === 'string') || end === null) && ((typeof negated === 'boolean') || negated === null)) {
                                let __args = arguments;
                                if (this.start === undefined) {
                                    this.start = null;
                                }
                                if (this.end === undefined) {
                                    this.end = null;
                                }
                                if (this.negated === undefined) {
                                    this.negated = false;
                                }
                                if (this.iToString === undefined) {
                                    this.iToString = null;
                                }
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(start) > (c => c.charCodeAt == null ? c : c.charCodeAt(0))(end)) {
                                    const temp = start;
                                    start = end;
                                    end = temp;
                                }
                                this.start = start;
                                this.end = end;
                                this.negated = negated;
                            }
                            else if (((typeof start === 'string') || start === null) && ((typeof end === 'boolean') || end === null) && negated === undefined) {
                                let __args = arguments;
                                let ch = __args[0];
                                let negated = __args[1];
                                {
                                    let __args = arguments;
                                    let start = ch;
                                    let end = ch;
                                    if (this.start === undefined) {
                                        this.start = null;
                                    }
                                    if (this.end === undefined) {
                                        this.end = null;
                                    }
                                    if (this.negated === undefined) {
                                        this.negated = false;
                                    }
                                    if (this.iToString === undefined) {
                                        this.iToString = null;
                                    }
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(start) > (c => c.charCodeAt == null ? c : c.charCodeAt(0))(end)) {
                                        const temp = start;
                                        start = end;
                                        end = temp;
                                    }
                                    this.start = start;
                                    this.end = end;
                                    this.negated = negated;
                                }
                                if (this.start === undefined) {
                                    this.start = null;
                                }
                                if (this.end === undefined) {
                                    this.end = null;
                                }
                                if (this.negated === undefined) {
                                    this.negated = false;
                                }
                                if (this.iToString === undefined) {
                                    this.iToString = null;
                                }
                            }
                            else if (((typeof start === 'string') || start === null) && ((typeof end === 'string') || end === null) && negated === undefined) {
                                let __args = arguments;
                                {
                                    let __args = arguments;
                                    let negated = false;
                                    if (this.start === undefined) {
                                        this.start = null;
                                    }
                                    if (this.end === undefined) {
                                        this.end = null;
                                    }
                                    if (this.negated === undefined) {
                                        this.negated = false;
                                    }
                                    if (this.iToString === undefined) {
                                        this.iToString = null;
                                    }
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(start) > (c => c.charCodeAt == null ? c : c.charCodeAt(0))(end)) {
                                        const temp = start;
                                        start = end;
                                        end = temp;
                                    }
                                    this.start = start;
                                    this.end = end;
                                    this.negated = negated;
                                }
                                if (this.start === undefined) {
                                    this.start = null;
                                }
                                if (this.end === undefined) {
                                    this.end = null;
                                }
                                if (this.negated === undefined) {
                                    this.negated = false;
                                }
                                if (this.iToString === undefined) {
                                    this.iToString = null;
                                }
                            }
                            else if (((typeof start === 'string') || start === null) && end === undefined && negated === undefined) {
                                let __args = arguments;
                                let ch = __args[0];
                                {
                                    let __args = arguments;
                                    let start = ch;
                                    let end = ch;
                                    let negated = false;
                                    if (this.start === undefined) {
                                        this.start = null;
                                    }
                                    if (this.end === undefined) {
                                        this.end = null;
                                    }
                                    if (this.negated === undefined) {
                                        this.negated = false;
                                    }
                                    if (this.iToString === undefined) {
                                        this.iToString = null;
                                    }
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(start) > (c => c.charCodeAt == null ? c : c.charCodeAt(0))(end)) {
                                        const temp = start;
                                        start = end;
                                        end = temp;
                                    }
                                    this.start = start;
                                    this.end = end;
                                    this.negated = negated;
                                }
                                if (this.start === undefined) {
                                    this.start = null;
                                }
                                if (this.end === undefined) {
                                    this.end = null;
                                }
                                if (this.negated === undefined) {
                                    this.negated = false;
                                }
                                if (this.iToString === undefined) {
                                    this.iToString = null;
                                }
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Constructs a <code>CharRange</code> over a single character.</p>
                         *
                         * @param {string} ch  only character in this range
                         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
                         * @see CharRange#CharRange(char, char, boolean)
                         * @since 2.5
                         */
                        static is(ch) {
                            return new CharRange(ch, ch, false);
                        }
                        /**
                         * <p>Constructs a negated <code>CharRange</code> over a single character.</p>
                         *
                         * @param {string} ch  only character in this range
                         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
                         * @see CharRange#CharRange(char, char, boolean)
                         * @since 2.5
                         */
                        static isNot(ch) {
                            return new CharRange(ch, ch, true);
                        }
                        /**
                         * <p>Constructs a <code>CharRange</code> over a set of characters.</p>
                         *
                         * @param {string} start  first character, inclusive, in this range
                         * @param {string} end  last character, inclusive, in this range
                         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
                         * @see CharRange#CharRange(char, char, boolean)
                         * @since 2.5
                         */
                        static isIn(start, end) {
                            return new CharRange(start, end, false);
                        }
                        /**
                         * <p>Constructs a negated <code>CharRange</code> over a set of characters.</p>
                         *
                         * @param {string} start  first character, inclusive, in this range
                         * @param {string} end  last character, inclusive, in this range
                         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
                         * @see CharRange#CharRange(char, char, boolean)
                         * @since 2.5
                         */
                        static isNotIn(start, end) {
                            return new CharRange(start, end, true);
                        }
                        /**
                         * <p>Gets the start character for this character range.</p>
                         *
                         * @return {string} the start char (inclusive)
                         */
                        getStart() {
                            return this.start;
                        }
                        /**
                         * <p>Gets the end character for this character range.</p>
                         *
                         * @return {string} the end char (inclusive)
                         */
                        getEnd() {
                            return this.end;
                        }
                        /**
                         * <p>Is this <code>CharRange</code> negated.</p>
                         *
                         * <p>A negated range includes everything except that defined by the
                         * start and end characters.</p>
                         *
                         * @return {boolean} <code>true</code> is negated
                         */
                        isNegated() {
                            return this.negated;
                        }
                        contains$char(ch) {
                            return ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end)) !== this.negated;
                        }
                        contains$org_openprovenance_apache_commons_lang_CharRange(range) {
                            if (range == null) {
                                throw Object.defineProperty(new Error("The Range must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            if (this.negated) {
                                if (range.negated) {
                                    return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) >= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(range.start) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end) <= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(range.end);
                                }
                                return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(range.end) < (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(range.start) > (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end);
                            }
                            if (range.negated) {
                                return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) == 0 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE);
                            }
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) <= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(range.start) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end) >= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(range.end);
                        }
                        /**
                         * <p>Are all the characters of the passed in range contained in
                         * this range.</p>
                         *
                         * @param {org.openprovenance.apache.commons.lang.CharRange} range  the range to check against
                         * @return {boolean} <code>true</code> if this range entirely contains the input range
                         * @throws IllegalArgumentException if <code>null</code> input
                         */
                        contains(range) {
                            if (((range != null && range instanceof org.openprovenance.apache.commons.lang.CharRange) || range === null)) {
                                return this.contains$org_openprovenance_apache_commons_lang_CharRange(range);
                            }
                            else if (((typeof range === 'string') || range === null)) {
                                return this.contains$char(range);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Compares two CharRange objects, returning true if they represent
                         * exactly the same range of characters defined in the same way.</p>
                         *
                         * @param {*} obj  the object to compare to
                         * @return {boolean} true if equal
                         */
                        equals(obj) {
                            if (obj === this) {
                                return true;
                            }
                            if ((obj != null && obj instanceof org.openprovenance.apache.commons.lang.CharRange) === false) {
                                return false;
                            }
                            const other = obj;
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(other.start) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(other.end) && this.negated === other.negated;
                        }
                        /**
                         * <p>Gets a hashCode compatible with the equals method.</p>
                         *
                         * @return {number} a suitable hashCode
                         */
                        hashCode() {
                            return 83 + (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) + 7 * (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end) + (this.negated ? 1 : 0);
                        }
                        /**
                         * <p>Gets a string representation of the character range.</p>
                         *
                         * @return {string} string representation of this range
                         */
                        toString() {
                            if (this.iToString == null) {
                                const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(4);
                                if (this.isNegated()) {
                                    buf.append$char('^');
                                }
                                buf.append$char(this.start);
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.start) != (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.end)) {
                                    buf.append$char('-');
                                    buf.append$char(this.end);
                                }
                                this.iToString = buf.toString();
                            }
                            return this.iToString;
                        }
                        /**
                         * <p>Returns an iterator which can be used to walk through the characters described by this range.</p>
                         *
                         * <p>#NotThreadSafe# the iterator is not threadsafe</p>
                         * @return {*} an iterator to the chars represented by this range
                         * @since 2.5
                         */
                        iterator() {
                            return new CharRange.CharacterIterator(this);
                        }
                    }
                    /**
                     * Required for serialization support. Lang version 2.0.
                     *
                     * @see Serializable
                     */
                    CharRange.serialVersionUID = 8270183163158333422;
                    lang.CharRange = CharRange;
                    CharRange["__class"] = "org.openprovenance.apache.commons.lang.CharRange";
                    CharRange["__interfaces"] = ["java.io.Serializable"];
                    (function (CharRange) {
                        /**
                         * Character {@link Iterator}.
                         * <p>#NotThreadSafe#</p>
                         * @class
                         */
                        class CharacterIterator {
                            constructor(r) {
                                if (this.current === undefined) {
                                    this.current = null;
                                }
                                if (this.range === undefined) {
                                    this.range = null;
                                }
                                if (this.__hasNext === undefined) {
                                    this.__hasNext = false;
                                }
                                this.range = r;
                                this.__hasNext = true;
                                if (this.range.negated) {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.start) == 0) {
                                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.end) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE)) {
                                            this.__hasNext = false;
                                        }
                                        else {
                                            this.current = String.fromCharCode(((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.end) + 1));
                                        }
                                    }
                                    else {
                                        this.current = String.fromCharCode(0);
                                    }
                                }
                                else {
                                    this.current = this.range.start;
                                }
                            }
                            /**
                             * Prepare the next character in the range.
                             * @private
                             */
                            prepareNext() {
                                if (this.range.negated) {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.current) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE)) {
                                        this.__hasNext = false;
                                    }
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.current) + 1 == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.start)) {
                                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.end) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE)) {
                                            this.__hasNext = false;
                                        }
                                        else {
                                            this.current = String.fromCharCode(((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.end) + 1));
                                        }
                                    }
                                    else {
                                        this.current = String.fromCharCode(((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.current) + 1));
                                    }
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.current) < (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.range.end)) {
                                    this.current = String.fromCharCode(((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.current) + 1));
                                }
                                else {
                                    this.__hasNext = false;
                                }
                            }
                            /**
                             * Has the iterator not reached the end character yet?
                             *
                             * @return {boolean} <code>true</code> if the iterator has yet to reach the character date
                             */
                            hasNext() {
                                return this.__hasNext;
                            }
                            /**
                             * Return the next character in the iteration
                             *
                             * @return {*} <code>Character</code> for the next character
                             */
                            next() {
                                if (this.__hasNext === false) {
                                    throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.util.NoSuchElementException', 'java.lang.Exception'] });
                                }
                                const cur = this.current;
                                this.prepareNext();
                                return new String(cur);
                            }
                            /**
                             * Always throws UnsupportedOperationException.
                             *
                             * @throws UnsupportedOperationException
                             * @see Iterator#remove()
                             */
                            remove() {
                                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        CharRange.CharacterIterator = CharacterIterator;
                        CharacterIterator["__class"] = "org.openprovenance.apache.commons.lang.CharRange.CharacterIterator";
                        CharacterIterator["__interfaces"] = ["java.util.Iterator"];
                    })(CharRange = lang.CharRange || (lang.CharRange = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
