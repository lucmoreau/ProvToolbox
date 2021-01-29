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
                     * <p>A set of characters.</p>
                     *
                     * <p>Instances are immutable, but instances of subclasses may not be.</p>
                     *
                     * <p>#ThreadSafe#</p>
                     * @author Apache Software Foundation
                     * @author Phil Steitz
                     * @author Pete Gieser
                     * @author Gary Gregory
                     * @since 1.0
                     * @version $Id: CharSet.java 1056988 2011-01-09 17:58:53Z niallp $
                     * @class
                     */
                    class CharSet {
                        constructor(setStr) {
                            if (((typeof setStr === 'string') || setStr === null)) {
                                let __args = arguments;
                                this.set = java.util.Collections.synchronizedSet(([]));
                                this.add(setStr);
                            }
                            else if (((setStr != null && setStr instanceof Array && (setStr.length == 0 || setStr[0] == null || (typeof setStr[0] === 'string'))) || setStr === null)) {
                                let __args = arguments;
                                let set = __args[0];
                                this.set = java.util.Collections.synchronizedSet(([]));
                                const sz = set.length;
                                for (let i = 0; i < sz; i++) {
                                    {
                                        this.add(set[i]);
                                    }
                                    ;
                                }
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static __static_initialize() { if (!CharSet.__static_initialized) {
                            CharSet.__static_initialized = true;
                            CharSet.__static_initializer_0();
                        } }
                        static EMPTY_$LI$() { CharSet.__static_initialize(); if (CharSet.EMPTY == null) {
                            CharSet.EMPTY = new CharSet(null);
                        } return CharSet.EMPTY; }
                        static ASCII_ALPHA_$LI$() { CharSet.__static_initialize(); if (CharSet.ASCII_ALPHA == null) {
                            CharSet.ASCII_ALPHA = new CharSet("a-zA-Z");
                        } return CharSet.ASCII_ALPHA; }
                        static ASCII_ALPHA_LOWER_$LI$() { CharSet.__static_initialize(); if (CharSet.ASCII_ALPHA_LOWER == null) {
                            CharSet.ASCII_ALPHA_LOWER = new CharSet("a-z");
                        } return CharSet.ASCII_ALPHA_LOWER; }
                        static ASCII_ALPHA_UPPER_$LI$() { CharSet.__static_initialize(); if (CharSet.ASCII_ALPHA_UPPER == null) {
                            CharSet.ASCII_ALPHA_UPPER = new CharSet("A-Z");
                        } return CharSet.ASCII_ALPHA_UPPER; }
                        static ASCII_NUMERIC_$LI$() { CharSet.__static_initialize(); if (CharSet.ASCII_NUMERIC == null) {
                            CharSet.ASCII_NUMERIC = new CharSet("0-9");
                        } return CharSet.ASCII_NUMERIC; }
                        static COMMON_$LI$() { CharSet.__static_initialize(); if (CharSet.COMMON == null) {
                            CharSet.COMMON = java.util.Collections.synchronizedMap(({}));
                        } return CharSet.COMMON; }
                        static __static_initializer_0() {
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), null, CharSet.EMPTY_$LI$());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), "", CharSet.EMPTY_$LI$());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), "a-zA-Z", CharSet.ASCII_ALPHA_$LI$());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), "A-Za-z", CharSet.ASCII_ALPHA_$LI$());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), "a-z", CharSet.ASCII_ALPHA_LOWER_$LI$());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), "A-Z", CharSet.ASCII_ALPHA_UPPER_$LI$());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(CharSet.COMMON_$LI$(), "0-9", CharSet.ASCII_NUMERIC_$LI$());
                        }
                        static getInstance$java_lang_String(setStr) {
                            const set = ((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return m.entries[i].value;
                                } return null; })(CharSet.COMMON_$LI$(), setStr);
                            if (set != null) {
                                return set;
                            }
                            return new CharSet(setStr);
                        }
                        /**
                         * <p>Factory method to create a new CharSet using a special syntax.</p>
                         *
                         * <ul>
                         * <li><code>null</code> or empty string ("")
                         * - set containing no characters</li>
                         * <li>Single character, such as "a"
                         * - set containing just that character</li>
                         * <li>Multi character, such as "a-e1"
                         * - set containing characters from one character to the other</li>
                         * <li>Negated, such as "^a" or "^a-e1"
                         * - set containing all characters except those defined</li>
                         * <li>Combinations, such as "abe-g"
                         * - set containing all the characters from the individual sets</li>
                         * </ul>
                         *
                         * <p>The matching order is:</p>
                         * <ol>
                         * <li>Negated multi character range, such as "^a-e1"
                         * <li>Ordinary multi character range, such as "a-e1"
                         * <li>Negated single character, such as "^a"
                         * <li>Ordinary single character, such as "a"
                         * </ol>
                         * <p>Matching works left to right. Once a match is found the
                         * search starts again from the next character.</p>
                         *
                         * <p>If the same range is defined twice using the same syntax, only
                         * one range will be kept.
                         * Thus, "a-ca-c" creates only one range of "a-c".</p>
                         *
                         * <p>If the start and end of a range are in the wrong order,
                         * they are reversed. Thus "a-e1" is the same as "e1-a".
                         * As a result, "a-ee-a" would create only one range,
                         * as the "a-e1" and "e1-a" are the same.</p>
                         *
                         * <p>The set of characters represented is the union of the specified ranges.</p>
                         *
                         * <p>All CharSet objects returned by this method will be immutable.</p>
                         *
                         * @param {string} setStr  the String describing the set, may be null
                         * @return {org.openprovenance.apache.commons.lang.CharSet} a CharSet instance
                         * @since 2.0
                         */
                        static getInstance(setStr) {
                            if (((typeof setStr === 'string') || setStr === null)) {
                                return org.openprovenance.apache.commons.lang.CharSet.getInstance$java_lang_String(setStr);
                            }
                            else if (((setStr != null && setStr instanceof Array && (setStr.length == 0 || setStr[0] == null || (typeof setStr[0] === 'string'))) || setStr === null)) {
                                return org.openprovenance.apache.commons.lang.CharSet.getInstance$java_lang_String_A(setStr);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static getInstance$java_lang_String_A(setStrs) {
                            if (setStrs == null) {
                                return null;
                            }
                            return new CharSet(setStrs);
                        }
                        /**
                         * <p>Add a set definition string to the <code>CharSet</code>.</p>
                         *
                         * @param {string} str  set definition string
                         */
                        add(str) {
                            if (str == null) {
                                return;
                            }
                            const len = str.length;
                            let pos = 0;
                            while ((pos < len)) {
                                {
                                    const remainder = (len - pos);
                                    if (remainder >= 4 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(pos)) == '^'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(pos + 2)) == '-'.charCodeAt(0)) {
                                        /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                            s.push(e);
                                            return true;
                                        }
                                        else {
                                            return false;
                                        } })(this.set, org.openprovenance.apache.commons.lang.CharRange.isNotIn(str.charAt(pos + 1), str.charAt(pos + 3)));
                                        pos += 4;
                                    }
                                    else if (remainder >= 3 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(pos + 1)) == '-'.charCodeAt(0)) {
                                        /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                            s.push(e);
                                            return true;
                                        }
                                        else {
                                            return false;
                                        } })(this.set, org.openprovenance.apache.commons.lang.CharRange.isIn(str.charAt(pos), str.charAt(pos + 2)));
                                        pos += 3;
                                    }
                                    else if (remainder >= 2 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(pos)) == '^'.charCodeAt(0)) {
                                        /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                            s.push(e);
                                            return true;
                                        }
                                        else {
                                            return false;
                                        } })(this.set, org.openprovenance.apache.commons.lang.CharRange.isNot(str.charAt(pos + 1)));
                                        pos += 2;
                                    }
                                    else {
                                        /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                            s.push(e);
                                            return true;
                                        }
                                        else {
                                            return false;
                                        } })(this.set, org.openprovenance.apache.commons.lang.CharRange.is(str.charAt(pos)));
                                        pos += 1;
                                    }
                                }
                            }
                            ;
                        }
                        /**
                         * <p>Gets the internal set as an array of CharRange objects.</p>
                         *
                         * @return {org.openprovenance.apache.commons.lang.CharRange[]} an array of immutable CharRange objects
                         * @since 2.0
                         */
                        getCharRanges() {
                            return this.set.slice(0);
                        }
                        /**
                         * <p>Does the <code>CharSet</code> contain the specified
                         * character <code>ch</code>.</p>
                         *
                         * @param {string} ch  the character to check for
                         * @return {boolean} <code>true</code> if the set contains the characters
                         */
                        contains(ch) {
                            for (const it = ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(this.set); it.hasNext();) {
                                {
                                    const range = it.next();
                                    if (range.contains$char(ch)) {
                                        return true;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        /**
                         * <p>Compares two CharSet objects, returning true if they represent
                         * exactly the same set of characters defined in the same way.</p>
                         *
                         * <p>The two sets <code>abc</code> and <code>a-c</code> are <i>not</i>
                         * equal according to this method.</p>
                         *
                         * @param {*} obj  the object to compare to
                         * @return {boolean} true if equal
                         * @since 2.0
                         */
                        equals(obj) {
                            if (obj === this) {
                                return true;
                            }
                            if ((obj != null && obj instanceof org.openprovenance.apache.commons.lang.CharSet) === false) {
                                return false;
                            }
                            const other = obj;
                            return /* equals */ ((a1, a2) => { if (a1 == null && a2 == null)
                                return true; if (a1 == null || a2 == null)
                                return false; if (a1.length != a2.length)
                                return false; for (let i = 0; i < a1.length; i++) {
                                if (a1[i] != a2[i])
                                    return false;
                            } return true; })(this.set, other.set);
                        }
                        /**
                         * <p>Gets a hashCode compatible with the equals method.</p>
                         *
                         * @return {number} a suitable hashCode
                         * @since 2.0
                         */
                        hashCode() {
                            return 89 + /* hashCode */ ((o) => { if (o.hashCode) {
                                return o.hashCode();
                            }
                            else {
                                return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0)) | 0, 0);
                            } })(this.set);
                        }
                        /**
                         * <p>Gets a string representation of the set.</p>
                         *
                         * @return {string} string representation of the set
                         */
                        toString() {
                            return /* toString */ ('[' + this.set.join(', ') + ']');
                        }
                    }
                    CharSet.__static_initialized = false;
                    /**
                     * Required for serialization support. Lang version 2.0.
                     *
                     * @see Serializable
                     */
                    CharSet.serialVersionUID = 5947847346149275958;
                    lang.CharSet = CharSet;
                    CharSet["__class"] = "org.openprovenance.apache.commons.lang.CharSet";
                    CharSet["__interfaces"] = ["java.io.Serializable"];
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
org.openprovenance.apache.commons.lang.CharSet.__static_initialize();
