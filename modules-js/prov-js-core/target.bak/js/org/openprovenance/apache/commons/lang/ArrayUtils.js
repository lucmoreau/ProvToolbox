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
                     * <p>ArrayUtils instances should NOT be constructed in standard programming.
                     * Instead, the class should be used as <code>ArrayUtils.clone(new int[] {2})</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean instance
                     * to operate.</p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class ArrayUtils {
                        constructor() {
                        }
                        static EMPTY_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_OBJECT_ARRAY; }
                        static EMPTY_CLASS_ARRAY_$LI$() { if (ArrayUtils.EMPTY_CLASS_ARRAY == null) {
                            ArrayUtils.EMPTY_CLASS_ARRAY = [];
                        } return ArrayUtils.EMPTY_CLASS_ARRAY; }
                        static EMPTY_STRING_ARRAY_$LI$() { if (ArrayUtils.EMPTY_STRING_ARRAY == null) {
                            ArrayUtils.EMPTY_STRING_ARRAY = [];
                        } return ArrayUtils.EMPTY_STRING_ARRAY; }
                        static EMPTY_LONG_ARRAY_$LI$() { if (ArrayUtils.EMPTY_LONG_ARRAY == null) {
                            ArrayUtils.EMPTY_LONG_ARRAY = [];
                        } return ArrayUtils.EMPTY_LONG_ARRAY; }
                        static EMPTY_LONG_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_LONG_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_LONG_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_LONG_OBJECT_ARRAY; }
                        static EMPTY_INT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_INT_ARRAY == null) {
                            ArrayUtils.EMPTY_INT_ARRAY = [];
                        } return ArrayUtils.EMPTY_INT_ARRAY; }
                        static EMPTY_INTEGER_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY; }
                        static EMPTY_SHORT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_SHORT_ARRAY == null) {
                            ArrayUtils.EMPTY_SHORT_ARRAY = [];
                        } return ArrayUtils.EMPTY_SHORT_ARRAY; }
                        static EMPTY_SHORT_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY; }
                        static EMPTY_BYTE_ARRAY_$LI$() { if (ArrayUtils.EMPTY_BYTE_ARRAY == null) {
                            ArrayUtils.EMPTY_BYTE_ARRAY = [];
                        } return ArrayUtils.EMPTY_BYTE_ARRAY; }
                        static EMPTY_BYTE_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY; }
                        static EMPTY_DOUBLE_ARRAY_$LI$() { if (ArrayUtils.EMPTY_DOUBLE_ARRAY == null) {
                            ArrayUtils.EMPTY_DOUBLE_ARRAY = [];
                        } return ArrayUtils.EMPTY_DOUBLE_ARRAY; }
                        static EMPTY_DOUBLE_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY; }
                        static EMPTY_FLOAT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_FLOAT_ARRAY == null) {
                            ArrayUtils.EMPTY_FLOAT_ARRAY = [];
                        } return ArrayUtils.EMPTY_FLOAT_ARRAY; }
                        static EMPTY_FLOAT_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY; }
                        static EMPTY_BOOLEAN_ARRAY_$LI$() { if (ArrayUtils.EMPTY_BOOLEAN_ARRAY == null) {
                            ArrayUtils.EMPTY_BOOLEAN_ARRAY = [];
                        } return ArrayUtils.EMPTY_BOOLEAN_ARRAY; }
                        static EMPTY_BOOLEAN_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY; }
                        static EMPTY_CHAR_ARRAY_$LI$() { if (ArrayUtils.EMPTY_CHAR_ARRAY == null) {
                            ArrayUtils.EMPTY_CHAR_ARRAY = [];
                        } return ArrayUtils.EMPTY_CHAR_ARRAY; }
                        static EMPTY_CHARACTER_OBJECT_ARRAY_$LI$() { if (ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY == null) {
                            ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY = [];
                        } return ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY; }
                        static toString$java_lang_Object(array) {
                            return ArrayUtils.toString$java_lang_Object$java_lang_String(array, "{}");
                        }
                        static toString$java_lang_Object$java_lang_String(array, stringIfNull) {
                            if (array == null) {
                                return stringIfNull;
                            }
                            return new org.openprovenance.apache.commons.lang.builder.ToStringBuilder(array, org.openprovenance.apache.commons.lang.builder.ToStringStyle.SIMPLE_STYLE_$LI$()).append$java_lang_Object(array).toString();
                        }
                        /**
                         * <p>Outputs an array as a String handling <code>null</code>s.</p>
                         *
                         * <p>Multi-dimensional arrays are handled correctly, including
                         * multi-dimensional primitive arrays.</p>
                         *
                         * <p>The format is that of Java source code, for example <code>{a,b}</code>.</p>
                         *
                         * @param {*} array  the array to get a toString for, may be <code>null</code>
                         * @param {string} stringIfNull  the String to return if the array is <code>null</code>
                         * @return {string} a String representation of the array
                         */
                        static toString(array, stringIfNull) {
                            if (((array != null) || array === null) && ((typeof stringIfNull === 'string') || stringIfNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toString$java_lang_Object$java_lang_String(array, stringIfNull);
                            }
                            else if (((array != null) || array === null) && stringIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toString$java_lang_Object(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Get a hashCode for an array handling multi-dimensional arrays correctly.</p>
                         *
                         * <p>Multi-dimensional primitive arrays are also handled correctly by this method.</p>
                         *
                         * @param {*} array  the array to get a hashCode for, may be <code>null</code>
                         * @return {number} a hashCode for the array, zero if null array input
                         */
                        static hashCode(array) {
                            return new org.openprovenance.apache.commons.lang.builder.HashCodeBuilder().append$java_lang_Object(array).toHashCode();
                        }
                        /**
                         * <p>Compares two arrays, using equals(), handling multi-dimensional arrays
                         * correctly.</p>
                         *
                         * <p>Multi-dimensional primitive arrays are also handled correctly by this method.</p>
                         *
                         * @param {*} array1  the left hand array to compare, may be <code>null</code>
                         * @param {*} array2  the right hand array to compare, may be <code>null</code>
                         * @return {boolean} <code>true</code> if the arrays are equal
                         */
                        static isEquals(array1, array2) {
                            return new org.openprovenance.apache.commons.lang.builder.EqualsBuilder().append$java_lang_Object$java_lang_Object(array1, array2).isEquals();
                        }
                        /**
                         * <p>Converts the given array into a {@link Map}. Each element of the array
                         * must be either a {@link Map.Entry} or an Array, containing at least two
                         * elements, where the first element is used as key and the second as
                         * value.</p>
                         *
                         * <p>This method can be used to initialize:</p>
                         * <pre>
                         * // Create a Map mapping colors.
                         * Map colorMap = MapUtils.toMap(new String[][] {{
                         * {"RED", "#FF0000"},
                         * {"GREEN", "#00FF00"},
                         * {"BLUE", "#0000FF"}});
                         * </pre>
                         *
                         * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
                         *
                         * @param {java.lang.Object[]} array  an array whose elements are either a {@link Map.Entry} or
                         * an Array containing at least two elements, may be <code>null</code>
                         * @return {*} a <code>Map</code> that was created from the array
                         * @throws IllegalArgumentException  if one element of this Array is
                         * itself an Array containing less then two elements
                         * @throws IllegalArgumentException  if the array contains elements other
                         * than {@link Map.Entry} and an Array
                         */
                        static toMap(array) {
                            if (array == null) {
                                return null;
                            }
                            const map = ({});
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const object = array[i];
                                    if (object != null && (object.constructor != null && object.constructor["__interfaces"] != null && object.constructor["__interfaces"].indexOf("java.util.Map.Entry") >= 0)) {
                                        const entry = object;
                                        /* put */ ((m, k, v) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                m.entries[i].value = v;
                                                return;
                                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(map, entry.getKey(), entry.getValue());
                                    }
                                    else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || object[0] != null)) {
                                        const entry = object;
                                        if (entry.length < 2) {
                                            throw Object.defineProperty(new Error("Array element " + i + ", \'" + object + "\', has a length less than 2"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                        }
                                        /* put */ ((m, k, v) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                m.entries[i].value = v;
                                                return;
                                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(map, entry[0], entry[1]);
                                    }
                                    else {
                                        throw Object.defineProperty(new Error("Array element " + i + ", \'" + object + "\', is neither of type Map.Entry nor an Array"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                }
                                ;
                            }
                            return map;
                        }
                        static clone$java_lang_Object_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        /**
                         * <p>Shallow clones an array returning a typecast result and handling
                         * <code>null</code>.</p>
                         *
                         * <p>The objects in the array are not cloned, thus there is no special
                         * handling for multi-dimensional arrays.</p>
                         *
                         * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
                         *
                         * @param {java.lang.Object[]} array  the array to shallow clone, may be <code>null</code>
                         * @return {java.lang.Object[]} the cloned array, <code>null</code> if <code>null</code> input
                         */
                        static clone(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$java_lang_Object_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$int_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$char_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.clone$boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static clone$long_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$int_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$short_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$char_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$byte_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$double_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$float_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static clone$boolean_A(array) {
                            if (array == null) {
                                return null;
                            }
                            return array.slice(0);
                        }
                        static nullToEmpty$java_lang_Object_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_String_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
                            }
                            return array;
                        }
                        /**
                         * <p>Defensive programming technique to change a <code>null</code>
                         * reference to an empty one.</p>
                         *
                         * <p>This method returns an empty array for a <code>null</code> input array.</p>
                         *
                         * <p>As a memory optimizing technique an empty array passed in will be overridden with
                         * the empty <code>public static</code> references in this class.</p>
                         *
                         * @param {java.lang.String[]} array  the array to check for <code>null</code> or empty
                         * @return {java.lang.String[]} the same array, <code>public static</code> empty array if <code>null</code> or empty input
                         * @since 2.5
                         */
                        static nullToEmpty(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_String_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Object_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$int_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$char_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$boolean_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Integer_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Character_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.nullToEmpty$java_lang_Boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static nullToEmpty$long_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_LONG_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$int_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_INT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$short_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_SHORT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$char_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_CHAR_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$byte_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_BYTE_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$double_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_DOUBLE_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$float_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_FLOAT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$boolean_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_BOOLEAN_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Long_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_LONG_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Integer_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Short_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Character_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Byte_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Double_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Float_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static nullToEmpty$java_lang_Boolean_A(array) {
                            if (array == null || array.length === 0) {
                                return ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY_$LI$();
                            }
                            return array;
                        }
                        static subarray$java_lang_Object_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            const type = array.constructor.getComponentType();
                            if (newSize <= 0) {
                                return new Array(0);
                            }
                            const subarray = new Array(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        /**
                         * <p>Produces a new array containing the elements between
                         * the start and end indices.</p>
                         *
                         * <p>The start index is inclusive, the end index exclusive.
                         * Null array input produces null output.</p>
                         *
                         * <p>The component type of the subarray is always the same as
                         * that of the input array. Thus, if the input is an array of type
                         * <code>Date</code>, the following usage is envisaged:</p>
                         *
                         * <pre>
                         * Date[] someDates = (Date[])ArrayUtils.subarray(allDates, 2, 5);
                         * </pre>
                         *
                         * @param {java.lang.Object[]} array  the array
                         * @param {number} startIndexInclusive  the starting index. Undervalue (&lt;0)
                         * is promoted to 0, overvalue (&gt;array.length) results
                         * in an empty array.
                         * @param {number} endIndexExclusive  elements up to endIndex-1 are present in the
                         * returned subarray. Undervalue (&lt; startIndex) produces
                         * empty array, overvalue (&gt;array.length) is demoted to
                         * array length.
                         * @return {java.lang.Object[]} a new array containing the elements between
                         * the start and end indices.
                         * @since 2.1
                         */
                        static subarray(array, startIndexInclusive, endIndexExclusive) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$java_lang_Object_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$long_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$int_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$short_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$char_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$byte_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$double_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$float_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof startIndexInclusive === 'number') || startIndexInclusive === null) && ((typeof endIndexExclusive === 'number') || endIndexExclusive === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.subarray$boolean_A$int$int(array, startIndexInclusive, endIndexExclusive);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static subarray$long_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_LONG_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$int_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_INT_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$short_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_SHORT_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$char_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_CHAR_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$byte_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_BYTE_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$double_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_DOUBLE_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$float_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_FLOAT_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static subarray$boolean_A$int$int(array, startIndexInclusive, endIndexExclusive) {
                            if (array == null) {
                                return null;
                            }
                            if (startIndexInclusive < 0) {
                                startIndexInclusive = 0;
                            }
                            if (endIndexExclusive > array.length) {
                                endIndexExclusive = array.length;
                            }
                            const newSize = endIndexExclusive - startIndexInclusive;
                            if (newSize <= 0) {
                                return ArrayUtils.EMPTY_BOOLEAN_ARRAY_$LI$();
                            }
                            const subarray = (s => { let a = []; while (s-- > 0)
                                a.push(false); return a; })(newSize);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, startIndexInclusive, subarray, 0, newSize);
                            return subarray;
                        }
                        static isSameLength$java_lang_Object_A$java_lang_Object_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks whether two arrays are the same length, treating
                         * <code>null</code> arrays as length <code>0</code>.
                         *
                         * <p>Any multi-dimensional aspects of the arrays are ignored.</p>
                         *
                         * @param {java.lang.Object[]} array1 the first array, may be <code>null</code>
                         * @param {java.lang.Object[]} array2 the second array, may be <code>null</code>
                         * @return {boolean} <code>true</code> if length of arrays matches, treating
                         * <code>null</code> as an empty array
                         */
                        static isSameLength(array1, array2) {
                            if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (array1[0] != null))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (array2[0] != null))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$java_lang_Object_A$java_lang_Object_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$long_A$long_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$int_A$int_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$short_A$short_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'string'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'string'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$char_A$char_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$byte_A$byte_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$double_A$double_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$float_A$float_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'boolean'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'boolean'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$boolean_A$boolean_A(array1, array2);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static isSameLength$long_A$long_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$int_A$int_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$short_A$short_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$char_A$char_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$byte_A$byte_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$double_A$double_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$float_A$float_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        static isSameLength$boolean_A$boolean_A(array1, array2) {
                            if ((array1 == null && array2 != null && array2.length > 0) || (array2 == null && array1 != null && array1.length > 0) || (array1 != null && array2 != null && array1.length !== array2.length)) {
                                return false;
                            }
                            return true;
                        }
                        /**
                         * <p>Returns the length of the specified array.
                         * This method can deal with <code>Object</code> arrays and with primitive arrays.</p>
                         *
                         * <p>If the input array is <code>null</code>, <code>0</code> is returned.</p>
                         *
                         * <pre>
                         * ArrayUtils.getLength(null)            = 0
                         * ArrayUtils.getLength([])              = 0
                         * ArrayUtils.getLength([null])          = 1
                         * ArrayUtils.getLength([true, false])   = 2
                         * ArrayUtils.getLength([1, 2, 3])       = 3
                         * ArrayUtils.getLength(["a", "b", "c"]) = 3
                         * </pre>
                         *
                         * @param {*} array  the array to retrieve the length from, may be null
                         * @return {number} The length of the array, or <code>0</code> if the array is <code>null</code>
                         * @throws IllegalArgumentException if the object arguement is not an array.
                         * @since 2.1
                         */
                        static getLength(array) {
                            if (array == null) {
                                return 0;
                            }
                            return /* getLength */ array.length;
                        }
                        /**
                         * <p>Checks whether two arrays are the same type taking into account
                         * multi-dimensional arrays.</p>
                         *
                         * @param {*} array1 the first array, must not be <code>null</code>
                         * @param {*} array2 the second array, must not be <code>null</code>
                         * @return {boolean} <code>true</code> if type of arrays matches
                         * @throws IllegalArgumentException if either array is <code>null</code>
                         */
                        static isSameType(array1, array2) {
                            if (array1 == null || array2 == null) {
                                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            return /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(array1.constructor) === /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(array2.constructor);
                        }
                        static reverse$java_lang_Object_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        /**
                         * <p>Reverses the order of the given array.</p>
                         *
                         * <p>There is no special handling for multi-dimensional arrays.</p>
                         *
                         * <p>This method does nothing for a <code>null</code> input array.</p>
                         *
                         * @param {java.lang.Object[]} array  the array to reverse, may be <code>null</code>
                         */
                        static reverse(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$java_lang_Object_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$int_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$char_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.reverse$boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static reverse$long_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$int_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$short_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$char_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$byte_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$double_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$float_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static reverse$boolean_A(array) {
                            if (array == null) {
                                return;
                            }
                            let i = 0;
                            let j = array.length - 1;
                            let tmp;
                            while ((j > i)) {
                                {
                                    tmp = array[j];
                                    array[j] = array[i];
                                    array[i] = tmp;
                                    j--;
                                    i++;
                                }
                            }
                            ;
                        }
                        static indexOf$java_lang_Object_A$java_lang_Object(array, objectToFind) {
                            return ArrayUtils.indexOf$java_lang_Object_A$java_lang_Object$int(array, objectToFind, 0);
                        }
                        static indexOf$java_lang_Object_A$java_lang_Object$int(array, objectToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            if (objectToFind == null) {
                                for (let i = startIndex; i < array.length; i++) {
                                    {
                                        if (array[i] == null) {
                                            return i;
                                        }
                                    }
                                    ;
                                }
                            }
                            else if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                            else if (typeof c === 'function')
                                return (o instanceof c) || (o.constructor && o.constructor === c); })(array.constructor.getComponentType(), objectToFind)) {
                                for (let i = startIndex; i < array.length; i++) {
                                    {
                                        if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(objectToFind, array[i])) {
                                            return i;
                                        }
                                    }
                                    ;
                                }
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$java_lang_Object_A$java_lang_Object(array, objectToFind) {
                            return ArrayUtils.lastIndexOf$java_lang_Object_A$java_lang_Object$int(array, objectToFind, 2147483647);
                        }
                        static lastIndexOf$java_lang_Object_A$java_lang_Object$int(array, objectToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            if (objectToFind == null) {
                                for (let i = startIndex; i >= 0; i--) {
                                    {
                                        if (array[i] == null) {
                                            return i;
                                        }
                                    }
                                    ;
                                }
                            }
                            else if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                            else if (typeof c === 'function')
                                return (o instanceof c) || (o.constructor && o.constructor === c); })(array.constructor.getComponentType(), objectToFind)) {
                                for (let i = startIndex; i >= 0; i--) {
                                    {
                                        if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(objectToFind, array[i])) {
                                            return i;
                                        }
                                    }
                                    ;
                                }
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$java_lang_Object_A$java_lang_Object(array, objectToFind) {
                            return ArrayUtils.indexOf$java_lang_Object_A$java_lang_Object(array, objectToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$long_A$long(array, valueToFind) {
                            return ArrayUtils.indexOf$long_A$long$int(array, valueToFind, 0);
                        }
                        static indexOf$long_A$long$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$long_A$long(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$long_A$long$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$long_A$long$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$long_A$long(array, valueToFind) {
                            return ArrayUtils.indexOf$long_A$long(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$int_A$int(array, valueToFind) {
                            return ArrayUtils.indexOf$int_A$int$int(array, valueToFind, 0);
                        }
                        static indexOf$int_A$int$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$int_A$int(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$int_A$int$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$int_A$int$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$int_A$int(array, valueToFind) {
                            return ArrayUtils.indexOf$int_A$int(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$short_A$short(array, valueToFind) {
                            return ArrayUtils.indexOf$short_A$short$int(array, valueToFind, 0);
                        }
                        static indexOf$short_A$short$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$short_A$short(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$short_A$short$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$short_A$short$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$short_A$short(array, valueToFind) {
                            return ArrayUtils.indexOf$short_A$short(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$char_A$char(array, valueToFind) {
                            return ArrayUtils.indexOf$char_A$char$int(array, valueToFind, 0);
                        }
                        static indexOf$char_A$char$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(valueToFind) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(array[i])) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$char_A$char(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$char_A$char$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$char_A$char$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(valueToFind) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(array[i])) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$char_A$char(array, valueToFind) {
                            return ArrayUtils.indexOf$char_A$char(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$byte_A$byte(array, valueToFind) {
                            return ArrayUtils.indexOf$byte_A$byte$int(array, valueToFind, 0);
                        }
                        static indexOf$byte_A$byte$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$byte_A$byte(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$byte_A$byte$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$byte_A$byte$int(array, valueToFind, startIndex) {
                            if (array == null) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$byte_A$byte(array, valueToFind) {
                            return ArrayUtils.indexOf$byte_A$byte(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$double_A$double(array, valueToFind) {
                            return ArrayUtils.indexOf$double_A$double$int(array, valueToFind, 0);
                        }
                        static indexOf$double_A$double$double(array, valueToFind, tolerance) {
                            return ArrayUtils.indexOf$double_A$double$int$double(array, valueToFind, 0, tolerance);
                        }
                        static indexOf$double_A$double$int(array, valueToFind, startIndex) {
                            if (ArrayUtils.isEmpty$double_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$double_A$double$int$double(array, valueToFind, startIndex, tolerance) {
                            if (ArrayUtils.isEmpty$double_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            const min = valueToFind - tolerance;
                            const max = valueToFind + tolerance;
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (array[i] >= min && array[i] <= max) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Finds the index of the given value in the array starting at the given index.
                         * This method will return the index of the first value which falls between the region
                         * defined by valueToFind - tolerance and valueToFind + tolerance.</p>
                         *
                         * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
                         *
                         * <p>A negative startIndex is treated as zero. A startIndex larger than the array
                         * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
                         *
                         * @param {double[]} array  the array to search through for the object, may be <code>null</code>
                         * @param {number} valueToFind  the value to find
                         * @param {number} startIndex  the index to start searching at
                         * @param {number} tolerance tolerance of the search
                         * @return {number} the index of the value within the array,
                         * {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
                         */
                        static indexOf(array, valueToFind, startIndex, tolerance) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof tolerance === 'number') || tolerance === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$double_A$double$int$double(array, valueToFind, startIndex, tolerance);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$short_A$short$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof valueToFind === 'string') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$char_A$char$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$byte_A$byte$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$int_A$int$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$long_A$long$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$float_A$float$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$double_A$double$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof valueToFind === 'boolean') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$boolean_A$boolean$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((valueToFind != null) || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$java_lang_Object_A$java_lang_Object$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$double_A$double$double(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$byte_A$byte(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$short_A$short(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof valueToFind === 'string') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$char_A$char(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$int_A$int(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$long_A$long(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$float_A$float(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$double_A$double(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof valueToFind === 'boolean') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$boolean_A$boolean(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((valueToFind != null) || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$java_lang_Object_A$java_lang_Object(array, valueToFind);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static lastIndexOf$double_A$double(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$double_A$double$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$double_A$double$double(array, valueToFind, tolerance) {
                            return ArrayUtils.lastIndexOf$double_A$double$int$double(array, valueToFind, 2147483647, tolerance);
                        }
                        static lastIndexOf$double_A$double$int(array, valueToFind, startIndex) {
                            if (ArrayUtils.isEmpty$double_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$double_A$double$int$double(array, valueToFind, startIndex, tolerance) {
                            if (ArrayUtils.isEmpty$double_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            const min = valueToFind - tolerance;
                            const max = valueToFind + tolerance;
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (array[i] >= min && array[i] <= max) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Finds the last index of the given value in the array starting at the given index.
                         * This method will return the index of the last value which falls between the region
                         * defined by valueToFind - tolerance and valueToFind + tolerance.</p>
                         *
                         * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
                         *
                         * <p>A negative startIndex will return {@link #INDEX_NOT_FOUND} (<code>-1</code>). A startIndex larger than the
                         * array length will search from the end of the array.</p>
                         *
                         * @param {double[]} array  the array to traverse for looking for the object, may be <code>null</code>
                         * @param {number} valueToFind  the value to find
                         * @param {number} startIndex  the start index to travers backwards from
                         * @param {number} tolerance  search for value within plus/minus this amount
                         * @return {number} the last index of the value within the array,
                         * {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
                         */
                        static lastIndexOf(array, valueToFind, startIndex, tolerance) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof tolerance === 'number') || tolerance === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$double_A$double$int$double(array, valueToFind, startIndex, tolerance);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$short_A$short$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof valueToFind === 'string') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$char_A$char$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$byte_A$byte$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$int_A$int$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$long_A$long$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$float_A$float$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$double_A$double$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof valueToFind === 'boolean') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$boolean_A$boolean$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((valueToFind != null) || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$java_lang_Object_A$java_lang_Object$int(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof startIndex === 'number') || startIndex === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$double_A$double$double(array, valueToFind, startIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$byte_A$byte(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$short_A$short(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof valueToFind === 'string') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$char_A$char(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$int_A$int(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$long_A$long(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$float_A$float(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$double_A$double(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof valueToFind === 'boolean') || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$boolean_A$boolean(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((valueToFind != null) || valueToFind === null) && startIndex === undefined && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.lastIndexOf$java_lang_Object_A$java_lang_Object(array, valueToFind);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static contains$double_A$double(array, valueToFind) {
                            return ArrayUtils.indexOf$double_A$double(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$double_A$double$double(array, valueToFind, tolerance) {
                            return ArrayUtils.indexOf$double_A$double$int$double(array, valueToFind, 0, tolerance) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Checks if a value falling within the given tolerance is in the
                         * given array.  If the array contains a value within the inclusive range
                         * defined by (value - tolerance) to (value + tolerance).</p>
                         *
                         * <p>The method returns <code>false</code> if a <code>null</code> array
                         * is passed in.</p>
                         *
                         * @param {double[]} array  the array to search
                         * @param {number} valueToFind  the value to find
                         * @param {number} tolerance  the array contains the tolerance of the search
                         * @return {boolean} true if value falling within tolerance is in array
                         */
                        static contains(array, valueToFind, tolerance) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && ((typeof tolerance === 'number') || tolerance === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$double_A$double$double(array, valueToFind, tolerance);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$short_A$short(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof valueToFind === 'string') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$char_A$char(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$byte_A$byte(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$int_A$int(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$long_A$long(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$float_A$float(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueToFind === 'number') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$double_A$double(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof valueToFind === 'boolean') || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$boolean_A$boolean(array, valueToFind);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((valueToFind != null) || valueToFind === null) && tolerance === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.contains$java_lang_Object_A$java_lang_Object(array, valueToFind);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static indexOf$float_A$float(array, valueToFind) {
                            return ArrayUtils.indexOf$float_A$float$int(array, valueToFind, 0);
                        }
                        static indexOf$float_A$float$int(array, valueToFind, startIndex) {
                            if (ArrayUtils.isEmpty$float_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$float_A$float(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$float_A$float$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$float_A$float$int(array, valueToFind, startIndex) {
                            if (ArrayUtils.isEmpty$float_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$float_A$float(array, valueToFind) {
                            return ArrayUtils.indexOf$float_A$float(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static indexOf$boolean_A$boolean(array, valueToFind) {
                            return ArrayUtils.indexOf$boolean_A$boolean$int(array, valueToFind, 0);
                        }
                        static indexOf$boolean_A$boolean$int(array, valueToFind, startIndex) {
                            if (ArrayUtils.isEmpty$boolean_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                startIndex = 0;
                            }
                            for (let i = startIndex; i < array.length; i++) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static lastIndexOf$boolean_A$boolean(array, valueToFind) {
                            return ArrayUtils.lastIndexOf$boolean_A$boolean$int(array, valueToFind, 2147483647);
                        }
                        static lastIndexOf$boolean_A$boolean$int(array, valueToFind, startIndex) {
                            if (ArrayUtils.isEmpty$boolean_A(array)) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            if (startIndex < 0) {
                                return ArrayUtils.INDEX_NOT_FOUND;
                            }
                            else if (startIndex >= array.length) {
                                startIndex = array.length - 1;
                            }
                            for (let i = startIndex; i >= 0; i--) {
                                {
                                    if (valueToFind === array[i]) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static contains$boolean_A$boolean(array, valueToFind) {
                            return ArrayUtils.indexOf$boolean_A$boolean(array, valueToFind) !== ArrayUtils.INDEX_NOT_FOUND;
                        }
                        static toPrimitive$java_lang_Character_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_CHAR_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* charValue */ array[i];
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Character_A$char(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_CHAR_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(b) == null ? valueForNull : /* charValue */ b);
                                }
                                ;
                            }
                            return result;
                        }
                        /**
                         * <p>Converts an array of object Character to primitives handling <code>null</code>.</p>
                         *
                         * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
                         *
                         * @param {java.lang.Character[]} array  a <code>Character</code> array, may be <code>null</code>
                         * @param {string} valueForNull  the value to insert if <code>null</code> found
                         * @return {char[]} a <code>char</code> array, <code>null</code> if null array input
                         */
                        static toPrimitive(array, valueForNull) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof valueForNull === 'string') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Character_A$char(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueForNull === 'number') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Byte_A$byte(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueForNull === 'number') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Short_A$short(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueForNull === 'number') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Integer_A$int(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueForNull === 'number') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Long_A$long(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueForNull === 'number') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Float_A$float(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof valueForNull === 'number') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Double_A$double(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof valueForNull === 'boolean') || valueForNull === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Boolean_A$boolean(array, valueForNull);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Character_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Integer_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && valueForNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toObject$char_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_CHARACTER_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new String(array[i]);
                                }
                                ;
                            }
                            return result;
                        }
                        /**
                         * <p>Converts an array of primitive chars to objects.</p>
                         *
                         * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
                         *
                         * @param {char[]} array a <code>char</code> array
                         * @return {java.lang.Character[]} a <code>Character</code> array, <code>null</code> if null array input
                         */
                        static toObject(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$char_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$int_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.toObject$boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toPrimitive$java_lang_Long_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_LONG_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* longValue */ array[i];
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Long_A$long(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_LONG_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* longValue */ b);
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$long_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_LONG_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new Number(array[i]).valueOf();
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Integer_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_INT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* intValue */ (array[i] | 0);
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Integer_A$int(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_INT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* intValue */ (b | 0));
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$int_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new Number(array[i]).valueOf();
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Short_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_SHORT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* shortValue */ (array[i] | 0);
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Short_A$short(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_SHORT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* shortValue */ (b | 0));
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$short_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_SHORT_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new Number(array[i]);
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Byte_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_BYTE_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* byteValue */ (array[i] | 0);
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Byte_A$byte(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_BYTE_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* byteValue */ (b | 0));
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$byte_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new Number(array[i]);
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Double_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_DOUBLE_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* doubleValue */ array[i];
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Double_A$double(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_DOUBLE_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* doubleValue */ b);
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$double_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new Number(array[i]).valueOf();
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Float_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_FLOAT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* floatValue */ array[i];
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Float_A$float(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_FLOAT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* floatValue */ b);
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$float_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_FLOAT_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = new Number(array[i]).valueOf();
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Boolean_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_BOOLEAN_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(false); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = /* booleanValue */ array[i];
                                }
                                ;
                            }
                            return result;
                        }
                        static toPrimitive$java_lang_Boolean_A$boolean(array, valueForNull) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_BOOLEAN_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(false); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    const b = array[i];
                                    result[i] = (b == null ? valueForNull : /* booleanValue */ b);
                                }
                                ;
                            }
                            return result;
                        }
                        static toObject$boolean_A(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY_$LI$();
                            }
                            const result = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    result[i] = (array[i] ? true : false);
                                }
                                ;
                            }
                            return result;
                        }
                        static isEmpty$java_lang_Object_A(array) {
                            return array == null || array.length === 0;
                        }
                        /**
                         * <p>Checks if an array of Objects is empty or <code>null</code>.</p>
                         *
                         * @param {java.lang.Object[]} array  the array to test
                         * @return {boolean} <code>true</code> if the array is empty or <code>null</code>
                         * @since 2.1
                         */
                        static isEmpty(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$int_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$char_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static isEmpty$long_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$int_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$short_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$char_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$byte_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$double_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$float_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isEmpty$boolean_A(array) {
                            return array == null || array.length === 0;
                        }
                        static isNotEmpty$java_lang_Object_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        /**
                         * <p>Checks if an array of Objects is not empty or not <code>null</code>.</p>
                         *
                         * @param {java.lang.Object[]} array  the array to test
                         * @return {boolean} <code>true</code> if the array is not empty or not <code>null</code>
                         * @since 2.5
                         */
                        static isNotEmpty(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$java_lang_Object_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$long_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$int_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$short_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$char_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$byte_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$double_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$float_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.isNotEmpty$boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static isNotEmpty$long_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$int_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$short_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$char_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$byte_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$double_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$float_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static isNotEmpty$boolean_A(array) {
                            return (array != null && array.length !== 0);
                        }
                        static addAll$java_lang_Object_A$java_lang_Object_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$java_lang_Object_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$java_lang_Object_A(array1);
                            }
                            const joinedArray = new Array(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            try {
                                /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                    while (--size >= 0)
                                        dstPts[dstOff++] = srcPts[srcOff++];
                                }
                                else {
                                    let tmp = srcPts.slice(srcOff, srcOff + size);
                                    for (let i = 0; i < size; i++)
                                        dstPts[dstOff++] = tmp[i];
                                } })(array2, 0, joinedArray, array1.length, array2.length);
                            }
                            catch (ase) {
                                const type1 = array1.constructor.getComponentType();
                                const type2 = array2.constructor.getComponentType();
                                if (!type1.isAssignableFrom(type2)) {
                                    throw Object.defineProperty(new Error("Cannot store " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(type2) + " in an array of " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(type1)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                throw ase;
                            }
                            return joinedArray;
                        }
                        /**
                         * <p>Adds all the elements of the given arrays into a new array.</p>
                         * <p>The new array contains all of the element of <code>array1</code> followed
                         * by all of the elements <code>array2</code>. When an array is returned, it is always
                         * a new array.</p>
                         *
                         * <pre>
                         * ArrayUtils.addAll(null, null)     = null
                         * ArrayUtils.addAll(array1, null)   = cloned copy of array1
                         * ArrayUtils.addAll(null, array2)   = cloned copy of array2
                         * ArrayUtils.addAll([], [])         = []
                         * ArrayUtils.addAll([null], [null]) = [null, null]
                         * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
                         * </pre>
                         *
                         * @param {java.lang.Object[]} array1  the first array whose elements are added to the new array, may be <code>null</code>
                         * @param {java.lang.Object[]} array2  the second array whose elements are added to the new array, may be <code>null</code>
                         * @return {java.lang.Object[]} The new array, <code>null</code> if both arrays are <code>null</code>.
                         * The type of the new array is the type of the first array,
                         * unless the first array is null, in which case the type is the same as the second array.
                         * @since 2.1
                         * @throws IllegalArgumentException if the array types are incompatible
                         */
                        static addAll(array1, array2) {
                            if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (array1[0] != null))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (array2[0] != null))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$java_lang_Object_A$java_lang_Object_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'boolean'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'boolean'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$boolean_A$boolean_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'string'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'string'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$char_A$char_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$byte_A$byte_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$short_A$short_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$int_A$int_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$long_A$long_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$float_A$float_A(array1, array2);
                            }
                            else if (((array1 != null && array1 instanceof Array && (array1.length == 0 || array1[0] == null || (typeof array1[0] === 'number'))) || array1 === null) && ((array2 != null && array2 instanceof Array && (array2.length == 0 || array2[0] == null || (typeof array2[0] === 'number'))) || array2 === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.addAll$double_A$double_A(array1, array2);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static addAll$boolean_A$boolean_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$boolean_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$boolean_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(false); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$char_A$char_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$char_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$char_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$byte_A$byte_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$byte_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$byte_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$short_A$short_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$short_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$short_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$int_A$int_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$int_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$int_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$long_A$long_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$long_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$long_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$float_A$float_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$float_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$float_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static addAll$double_A$double_A(array1, array2) {
                            if (array1 == null) {
                                return /* clone */ ArrayUtils.clone$double_A(array2);
                            }
                            else if (array2 == null) {
                                return /* clone */ ArrayUtils.clone$double_A(array1);
                            }
                            const joinedArray = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(array1.length + array2.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array1, 0, joinedArray, 0, array1.length);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array2, 0, joinedArray, array1.length, array2.length);
                            return joinedArray;
                        }
                        static add$java_lang_Object_A$java_lang_Object(array, element) {
                            let type;
                            if (array != null) {
                                type = array.constructor;
                            }
                            else if (element != null) {
                                type = element.constructor;
                            }
                            else {
                                type = Object;
                            }
                            const newArray = ArrayUtils.copyArrayGrow1(array, type);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$boolean_A$boolean(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.BooleanHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$byte_A$byte(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.ByteHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$char_A$char(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.CharacterHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$double_A$double(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.DoubleHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$float_A$float(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.FloatHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$int_A$int(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.IntegerHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$long_A$long(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.LongHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        static add$short_A$short(array, element) {
                            const newArray = ArrayUtils.copyArrayGrow1(array, javaemul.internal.ShortHelper.TYPE);
                            newArray[newArray.length - 1] = element;
                            return newArray;
                        }
                        /**
                         * Returns a copy of the given array of size 1 greater than the argument.
                         * The last value of the array is left to the default value.
                         *
                         * @param {*} array The array to copy, must not be <code>null</code>.
                         * @param {*} newArrayComponentType If <code>array</code> is <code>null</code>, create a
                         * size 1 array of this type.
                         * @return {*} A new copy of the array of size 1 greater than the input.
                         * @private
                         */
                        /*private*/ static copyArrayGrow1(array, newArrayComponentType) {
                            if (array != null) {
                                const arrayLength = array.length;
                                const newArray = new Array(arrayLength + 1);
                                /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                    while (--size >= 0)
                                        dstPts[dstOff++] = srcPts[srcOff++];
                                }
                                else {
                                    let tmp = srcPts.slice(srcOff, srcOff + size);
                                    for (let i = 0; i < size; i++)
                                        dstPts[dstOff++] = tmp[i];
                                } })(array, 0, newArray, 0, arrayLength);
                                return newArray;
                            }
                            return /* newInstance */ new Array(1);
                        }
                        static add$java_lang_Object_A$int$java_lang_Object(array, index, element) {
                            let clss = null;
                            if (array != null) {
                                clss = array.constructor.getComponentType();
                            }
                            else if (element != null) {
                                clss = element.constructor;
                            }
                            else {
                                return [null];
                            }
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, element, clss);
                        }
                        static add$boolean_A$int$boolean(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(element), javaemul.internal.BooleanHelper.TYPE);
                        }
                        static add$char_A$int$char(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new String(element), javaemul.internal.CharacterHelper.TYPE);
                        }
                        static add$byte_A$int$byte(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new Number(element), javaemul.internal.ByteHelper.TYPE);
                        }
                        static add$short_A$int$short(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new Number(element), javaemul.internal.ShortHelper.TYPE);
                        }
                        static add$int_A$int$int(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new Number(element).valueOf(), javaemul.internal.IntegerHelper.TYPE);
                        }
                        static add$long_A$int$long(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new Number(element).valueOf(), javaemul.internal.LongHelper.TYPE);
                        }
                        static add$float_A$int$float(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new Number(element).valueOf(), javaemul.internal.FloatHelper.TYPE);
                        }
                        static add$double_A$int$double(array, index, element) {
                            return ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, new Number(element).valueOf(), javaemul.internal.DoubleHelper.TYPE);
                        }
                        static add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, element, clss) {
                            if (array == null) {
                                if (index !== 0) {
                                    throw Object.defineProperty(new Error("Index: " + index + ", Length: 0"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                }
                                const joinedArray = new Array(1);
                                /* set */ (joinedArray[0] = 0);
                                return joinedArray;
                            }
                            const length = array.length;
                            if (index > length || index < 0) {
                                throw Object.defineProperty(new Error("Index: " + index + ", Length: " + length), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                            const result = new Array(length + 1);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, 0, result, 0, index);
                            /* set */ (result[index] = index);
                            if (index < length) {
                                /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                    while (--size >= 0)
                                        dstPts[dstOff++] = srcPts[srcOff++];
                                }
                                else {
                                    let tmp = srcPts.slice(srcOff, srcOff + size);
                                    for (let i = 0; i < size; i++)
                                        dstPts[dstOff++] = tmp[i];
                                } })(array, index, result, index + 1, length - index);
                            }
                            return result;
                        }
                        /**
                         * Underlying implementation of add(array, index, element) methods.
                         * The last parameter is the class, which may not equal element.getClass
                         * for primitives.
                         *
                         * @param {*} array  the array to add the element to, may be <code>null</code>
                         * @param {number} index  the position of the new object
                         * @param {*} element  the object to add
                         * @param {*} clss the type of the element being added
                         * @return {*} A new array containing the existing elements and the new element
                         * @private
                         */
                        static add(array, index, element, clss) {
                            if (((array != null) || array === null) && ((typeof index === 'number') || index === null) && ((element != null) || element === null) && ((clss != null && (clss["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(clss))) || clss === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$java_lang_Object$int$java_lang_Object$java_lang_Class(array, index, element, clss);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'boolean') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$boolean_A$int$boolean(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'string') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$char_A$int$char(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'number') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$byte_A$int$byte(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'number') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$short_A$int$short(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'number') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$int_A$int$int(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'number') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$long_A$int$long(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'number') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$float_A$int$float(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && ((typeof element === 'number') || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$double_A$int$double(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof index === 'number') || index === null) && ((element != null) || element === null) && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$java_lang_Object_A$int$java_lang_Object(array, index, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof index === 'boolean') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$boolean_A$boolean(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$byte_A$byte(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof index === 'string') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$char_A$char(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$short_A$short(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$int_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$long_A$long(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$float_A$float(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$double_A$double(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((index != null) || index === null) && element === undefined && clss === undefined) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.add$java_lang_Object_A$java_lang_Object(array, index);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static remove$java_lang_Object_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        /**
                         * <p>Removes the element at the specified position from the specified array.
                         * All subsequent elements are shifted to the left (substracts one from
                         * their indices).</p>
                         *
                         * <p>This method returns a new array with the same elements of the input
                         * array except the element on the specified position. The component
                         * type of the returned array is always the same as that of the input
                         * array.</p>
                         *
                         * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
                         * will be thrown, because in that case no valid index can be specified.</p>
                         *
                         * <pre>
                         * ArrayUtils.remove(["a"], 0)           = []
                         * ArrayUtils.remove(["a", "b"], 0)      = ["b"]
                         * ArrayUtils.remove(["a", "b"], 1)      = ["a"]
                         * ArrayUtils.remove(["a", "b", "c"], 1) = ["a", "c"]
                         * </pre>
                         *
                         * @param {java.lang.Object[]} array  the array to remove the element from, may not be <code>null</code>
                         * @param {number} index  the position of the element to be removed
                         * @return {java.lang.Object[]} A new array containing the existing elements except the element
                         * at the specified position.
                         * @throws IndexOutOfBoundsException if the index is out of range
                         * (index &lt; 0 || index &gt;= array.length), or if the array is <code>null</code>.
                         * @since 2.1
                         */
                        static remove(array, index) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$java_lang_Object_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$boolean_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$byte_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$char_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$double_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$float_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$int_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$long_A$int(array, index);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$short_A$int(array, index);
                            }
                            else if (((array != null) || array === null) && ((typeof index === 'number') || index === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.remove$java_lang_Object$int(array, index);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static removeElement$java_lang_Object_A$java_lang_Object(array, element) {
                            const index = ArrayUtils.indexOf$java_lang_Object_A$java_lang_Object(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$java_lang_Object_A(array);
                            }
                            return ArrayUtils.remove$java_lang_Object_A$int(array, index);
                        }
                        static remove$boolean_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$boolean_A$boolean(array, element) {
                            const index = ArrayUtils.indexOf$boolean_A$boolean(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$boolean_A(array);
                            }
                            return ArrayUtils.remove$boolean_A$int(array, index);
                        }
                        /**
                         * <p>Removes the first occurrence of the specified element from the
                         * specified array. All subsequent elements are shifted to the left
                         * (substracts one from their indices). If the array doesn't contains
                         * such an element, no elements are removed from the array.</p>
                         *
                         * <p>This method returns a new array with the same elements of the input
                         * array except the first occurrence of the specified element. The component
                         * type of the returned array is always the same as that of the input
                         * array.</p>
                         *
                         * <pre>
                         * ArrayUtils.removeElement(null, true)                = null
                         * ArrayUtils.removeElement([], true)                  = []
                         * ArrayUtils.removeElement([true], false)             = [true]
                         * ArrayUtils.removeElement([true, false], false)      = [true]
                         * ArrayUtils.removeElement([true, false, true], true) = [false, true]
                         * </pre>
                         *
                         * @param {boolean[]} array  the array to remove the element from, may be <code>null</code>
                         * @param {boolean} element  the element to be removed
                         * @return {boolean[]} A new array containing the existing elements except the first
                         * occurrence of the specified element.
                         * @since 2.1
                         */
                        static removeElement(array, element) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof element === 'boolean') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$boolean_A$boolean(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof element === 'number') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$byte_A$byte(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof element === 'string') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$char_A$char(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof element === 'number') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$short_A$short(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof element === 'number') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$int_A$int(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof element === 'number') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$long_A$long(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof element === 'number') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$float_A$float(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof element === 'number') || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$double_A$double(array, element);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((element != null) || element === null)) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.removeElement$java_lang_Object_A$java_lang_Object(array, element);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static remove$byte_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$byte_A$byte(array, element) {
                            const index = ArrayUtils.indexOf$byte_A$byte(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$byte_A(array);
                            }
                            return ArrayUtils.remove$byte_A$int(array, index);
                        }
                        static remove$char_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$char_A$char(array, element) {
                            const index = ArrayUtils.indexOf$char_A$char(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$char_A(array);
                            }
                            return ArrayUtils.remove$char_A$int(array, index);
                        }
                        static remove$double_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$double_A$double(array, element) {
                            const index = ArrayUtils.indexOf$double_A$double(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$double_A(array);
                            }
                            return ArrayUtils.remove$double_A$int(array, index);
                        }
                        static remove$float_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$float_A$float(array, element) {
                            const index = ArrayUtils.indexOf$float_A$float(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$float_A(array);
                            }
                            return ArrayUtils.remove$float_A$int(array, index);
                        }
                        static remove$int_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$int_A$int(array, element) {
                            const index = ArrayUtils.indexOf$int_A$int(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$int_A(array);
                            }
                            return ArrayUtils.remove$int_A$int(array, index);
                        }
                        static remove$long_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$long_A$long(array, element) {
                            const index = ArrayUtils.indexOf$long_A$long(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$long_A(array);
                            }
                            return ArrayUtils.remove$long_A$int(array, index);
                        }
                        static remove$short_A$int(array, index) {
                            return ArrayUtils.remove$java_lang_Object$int(array, index);
                        }
                        static removeElement$short_A$short(array, element) {
                            const index = ArrayUtils.indexOf$short_A$short(array, element);
                            if (index === ArrayUtils.INDEX_NOT_FOUND) {
                                return /* clone */ ArrayUtils.clone$short_A(array);
                            }
                            return ArrayUtils.remove$short_A$int(array, index);
                        }
                        /*private*/ static remove$java_lang_Object$int(array, index) {
                            const length = ArrayUtils.getLength(array);
                            if (index < 0 || index >= length) {
                                throw Object.defineProperty(new Error("Index: " + index + ", Length: " + length), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                            const result = new Array(length - 1);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(array, 0, result, 0, index);
                            if (index < length - 1) {
                                /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                    while (--size >= 0)
                                        dstPts[dstOff++] = srcPts[srcOff++];
                                }
                                else {
                                    let tmp = srcPts.slice(srcOff, srcOff + size);
                                    for (let i = 0; i < size; i++)
                                        dstPts[dstOff++] = tmp[i];
                                } })(array, index + 1, result, index, length - index - 1);
                            }
                            return result;
                        }
                    }
                    /**
                     * The index value when an element is not found in a list or array: <code>-1</code>.
                     * This value is returned by methods in this class and can also be used in comparisons with values returned by
                     * various method from {@link java.util.List}.
                     */
                    ArrayUtils.INDEX_NOT_FOUND = -1;
                    lang.ArrayUtils = ArrayUtils;
                    ArrayUtils["__class"] = "org.openprovenance.apache.commons.lang.ArrayUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
