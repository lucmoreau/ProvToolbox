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
                     * <p><code>ObjectUtils</code> instances should NOT be constructed in
                     * standard programming. Instead, the class should be used as
                     * <code>ObjectUtils.defaultIfNull("a","b");</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean instance
                     * to operate.</p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class ObjectUtils {
                        constructor() {
                        }
                        static NULL_$LI$() { if (ObjectUtils.NULL == null) {
                            ObjectUtils.NULL = new ObjectUtils.Null();
                        } return ObjectUtils.NULL; }
                        /**
                         * <p>Returns a default value if the object passed is
                         * <code>null</code>.</p>
                         *
                         * <pre>
                         * ObjectUtils.defaultIfNull(null, null)      = null
                         * ObjectUtils.defaultIfNull(null, "")        = ""
                         * ObjectUtils.defaultIfNull(null, "zz")      = "zz"
                         * ObjectUtils.defaultIfNull("abc", *)        = "abc"
                         * ObjectUtils.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
                         * </pre>
                         *
                         * @param {*} object  the <code>Object</code> to test, may be <code>null</code>
                         * @param {*} defaultValue  the default value to return, may be <code>null</code>
                         * @return {*} <code>object</code> if it is not <code>null</code>, defaultValue otherwise
                         */
                        static defaultIfNull(object, defaultValue) {
                            return object != null ? object : defaultValue;
                        }
                        /**
                         * <p>Compares two objects for equality, where either one or both
                         * objects may be <code>null</code>.</p>
                         *
                         * <pre>
                         * ObjectUtils.equals(null, null)                  = true
                         * ObjectUtils.equals(null, "")                    = false
                         * ObjectUtils.equals("", null)                    = false
                         * ObjectUtils.equals("", "")                      = true
                         * ObjectUtils.equals(Boolean.TRUE, null)          = false
                         * ObjectUtils.equals(Boolean.TRUE, "true")        = false
                         * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
                         * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
                         * </pre>
                         *
                         * @param {*} object1  the first object, may be <code>null</code>
                         * @param {*} object2  the second object, may be <code>null</code>
                         * @return {boolean} <code>true</code> if the values of both objects are the same
                         */
                        static equals(object1, object2) {
                            if (object1 === object2) {
                                return true;
                            }
                            if ((object1 == null) || (object2 == null)) {
                                return false;
                            }
                            return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(object1, object2);
                        }
                        /**
                         * <p>Compares two objects for inequality, where either one or both
                         * objects may be <code>null</code>.</p>
                         *
                         * <pre>
                         * ObjectUtils.notEqual(null, null)                  = false
                         * ObjectUtils.notEqual(null, "")                    = true
                         * ObjectUtils.notEqual("", null)                    = true
                         * ObjectUtils.notEqual("", "")                      = false
                         * ObjectUtils.notEqual(Boolean.TRUE, null)          = true
                         * ObjectUtils.notEqual(Boolean.TRUE, "true")        = true
                         * ObjectUtils.notEqual(Boolean.TRUE, Boolean.TRUE)  = false
                         * ObjectUtils.notEqual(Boolean.TRUE, Boolean.FALSE) = true
                         * </pre>
                         *
                         * @param {*} object1  the first object, may be <code>null</code>
                         * @param {*} object2  the second object, may be <code>null</code>
                         * @return {boolean} <code>false</code> if the values of both objects are the same
                         * @since 2.6
                         */
                        static notEqual(object1, object2) {
                            return ObjectUtils.equals(object1, object2) === false;
                        }
                        /**
                         * <p>Gets the hash code of an object returning zero when the
                         * object is <code>null</code>.</p>
                         *
                         * <pre>
                         * ObjectUtils.hashCode(null)   = 0
                         * ObjectUtils.hashCode(obj)    = obj.hashCode()
                         * </pre>
                         *
                         * @param {*} obj  the object to obtain the hash code of, may be <code>null</code>
                         * @return {number} the hash code of the object, or zero if null
                         * @since 2.1
                         */
                        static hashCode(obj) {
                            return (obj == null) ? 0 : /* hashCode */ ((o) => { if (o.hashCode) {
                                return o.hashCode();
                            }
                            else {
                                return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0)) | 0, 0);
                            } })(obj);
                        }
                        static identityToString$java_lang_Object(object) {
                            if (object == null) {
                                return null;
                            }
                            const buffer = { str: "", toString: function () { return this.str; } };
                            ObjectUtils.identityToString$java_lang_StringBuffer$java_lang_Object(buffer, object);
                            return /* toString */ buffer.str;
                        }
                        static identityToString$java_lang_StringBuffer$java_lang_Object(buffer, object) {
                            if (object == null) {
                                throw Object.defineProperty(new Error("Cannot get the toString of a null identity"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.NullPointerException', 'java.lang.Exception'] });
                            }
                            /* append */ (sb => { sb.str += javaemul.internal.IntegerHelper.toHexString(java.lang.System.identityHashCode(object)); return sb; })(/* append */ (sb => { sb.str += '@'; return sb; })(/* append */ (sb => { sb.str += (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor); return sb; })(buffer)));
                        }
                        /**
                         * <p>Appends the toString that would be produced by <code>Object</code>
                         * if a class did not override toString itself. <code>null</code>
                         * will throw a NullPointerException for either of the two parameters. </p>
                         *
                         * <pre>
                         * ObjectUtils.identityToString(buf, "")            = buf.append("java.lang.String@1e23"
                         * ObjectUtils.identityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa"
                         * ObjectUtils.identityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
                         * </pre>
                         *
                         * @param {{ str: string, toString: Function }} buffer  the buffer to append to
                         * @param {*} object  the object to create a toString for
                         * @since 2.4
                         */
                        static identityToString(buffer, object) {
                            if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((object != null) || object === null)) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.identityToString$java_lang_StringBuffer$java_lang_Object(buffer, object);
                            }
                            else if (((buffer != null) || buffer === null) && object === undefined) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.identityToString$java_lang_Object(buffer);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Appends the toString that would be produced by <code>Object</code>
                         * if a class did not override toString itself. <code>null</code>
                         * will return <code>null</code>.</p>
                         *
                         * <pre>
                         * ObjectUtils.appendIdentityToString(*, null)            = null
                         * ObjectUtils.appendIdentityToString(null, "")           = "java.lang.String@1e23"
                         * ObjectUtils.appendIdentityToString(null, Boolean.TRUE) = "java.lang.Boolean@7fa"
                         * ObjectUtils.appendIdentityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
                         * </pre>
                         *
                         * @param {{ str: string, toString: Function }} buffer  the buffer to append to, may be <code>null</code>
                         * @param {*} object  the object to create a toString for, may be <code>null</code>
                         * @return {{ str: string, toString: Function }} the default toString text, or <code>null</code> if
                         * <code>null</code> passed in
                         * @since 2.0
                         * @deprecated The design of this method is bad - see LANG-360. Instead, use identityToString(StringBuffer, Object).
                         */
                        static appendIdentityToString(buffer, object) {
                            if (object == null) {
                                return null;
                            }
                            if (buffer == null) {
                                buffer = { str: "", toString: function () { return this.str; } };
                            }
                            return /* append */ (sb => { sb.str += javaemul.internal.IntegerHelper.toHexString(java.lang.System.identityHashCode(object)); return sb; })(/* append */ (sb => { sb.str += '@'; return sb; })(/* append */ (sb => { sb.str += (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor); return sb; })(buffer)));
                        }
                        static toString$java_lang_Object(obj) {
                            return obj == null ? "" : obj.toString();
                        }
                        static toString$java_lang_Object$java_lang_String(obj, nullStr) {
                            return obj == null ? nullStr : obj.toString();
                        }
                        /**
                         * <p>Gets the <code>toString</code> of an <code>Object</code> returning
                         * a specified text if <code>null</code> input.</p>
                         *
                         * <pre>
                         * ObjectUtils.toString(null, null)           = null
                         * ObjectUtils.toString(null, "null")         = "null"
                         * ObjectUtils.toString("", "null")           = ""
                         * ObjectUtils.toString("bat", "null")        = "bat"
                         * ObjectUtils.toString(Boolean.TRUE, "null") = "true"
                         * </pre>
                         *
                         * @see StringUtils#defaultString(String,String)
                         * @see String#valueOf(Object)
                         * @param {*} obj  the Object to <code>toString</code>, may be null
                         * @param {string} nullStr  the String to return if <code>null</code> input, may be null
                         * @return {string} the passed in Object's toString, or nullStr if <code>null</code> input
                         * @since 2.0
                         */
                        static toString(obj, nullStr) {
                            if (((obj != null) || obj === null) && ((typeof nullStr === 'string') || nullStr === null)) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.toString$java_lang_Object$java_lang_String(obj, nullStr);
                            }
                            else if (((obj != null) || obj === null) && nullStr === undefined) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.toString$java_lang_Object(obj);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * Null safe comparison of Comparables.
                         *
                         * @param {*} c1  the first comparable, may be null
                         * @param {*} c2  the second comparable, may be null
                         * @return
                         * <ul>
                         * <li>If both objects are non-null and unequal, the lesser object.
                         * <li>If both objects are non-null and equal, c1.
                         * <li>If one of the comparables is null, the non-null object.
                         * <li>If both the comparables are null, null is returned.
                         * </ul>
                         * @return {*}
                         */
                        static min(c1, c2) {
                            return (ObjectUtils.compare$java_lang_Comparable$java_lang_Comparable$boolean(c1, c2, true) <= 0 ? c1 : c2);
                        }
                        /**
                         * Null safe comparison of Comparables.
                         *
                         * @param {*} c1  the first comparable, may be null
                         * @param {*} c2  the second comparable, may be null
                         * @return
                         * <ul>
                         * <li>If both objects are non-null and unequal, the greater object.
                         * <li>If both objects are non-null and equal, c1.
                         * <li>If one of the comparables is null, the non-null object.
                         * <li>If both the comparables are null, null is returned.
                         * </ul>
                         * @return {*}
                         */
                        static max(c1, c2) {
                            return (ObjectUtils.compare$java_lang_Comparable$java_lang_Comparable$boolean(c1, c2, false) >= 0 ? c1 : c2);
                        }
                        static compare$java_lang_Comparable$java_lang_Comparable(c1, c2) {
                            return ObjectUtils.compare$java_lang_Comparable$java_lang_Comparable$boolean(c1, c2, false);
                        }
                        static compare$java_lang_Comparable$java_lang_Comparable$boolean(c1, c2, nullGreater) {
                            if (c1 === c2) {
                                return 0;
                            }
                            else if (c1 == null) {
                                return (nullGreater ? 1 : -1);
                            }
                            else if (c2 == null) {
                                return (nullGreater ? -1 : 1);
                            }
                            return c1.compareTo(c2);
                        }
                        /**
                         * Null safe comparison of Comparables.
                         *
                         * @param {*} c1  the first comparable, may be null
                         * @param {*} c2  the second comparable, may be null
                         * @param {boolean} nullGreater if true <code>null</code> is considered greater
                         * than a Non-<code>null</code> value or if false <code>null</code> is
                         * considered less than a Non-<code>null</code> value
                         * @return {number} a negative value if c1 &lt; c2, zero if c1 = c2
                         * and a positive value if c1 &gt; c2
                         * @see java.util.Comparator#compare(Object, Object)
                         * @since 2.6
                         */
                        static compare(c1, c2, nullGreater) {
                            if (((c1 != null && (c1.constructor != null && c1.constructor["__interfaces"] != null && c1.constructor["__interfaces"].indexOf("java.lang.Comparable") >= 0)) || c1 === null) && ((c2 != null && (c2.constructor != null && c2.constructor["__interfaces"] != null && c2.constructor["__interfaces"].indexOf("java.lang.Comparable") >= 0)) || c2 === null) && ((typeof nullGreater === 'boolean') || nullGreater === null)) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.compare$java_lang_Comparable$java_lang_Comparable$boolean(c1, c2, nullGreater);
                            }
                            else if (((c1 != null && (c1.constructor != null && c1.constructor["__interfaces"] != null && c1.constructor["__interfaces"].indexOf("java.lang.Comparable") >= 0)) || c1 === null) && ((c2 != null && (c2.constructor != null && c2.constructor["__interfaces"] != null && c2.constructor["__interfaces"].indexOf("java.lang.Comparable") >= 0)) || c2 === null) && nullGreater === undefined) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.compare$java_lang_Comparable$java_lang_Comparable(c1, c2);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * Clone an object.
                         *
                         * @param {*} o the object to clone
                         * @return {*} the clone if the object implements {@link Cloneable} otherwise <code>null</code>
                         * @throws CloneFailedException if the object is cloneable and the clone operation fails
                         * @since 2.6
                         */
                        static clone(o) {
                            if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("java.lang.Cloneable") >= 0)) {
                                let result;
                                if (o.constructor.isArray()) {
                                    const componentType = o.constructor.getComponentType();
                                    if (!(componentType === '__erasedPrimitiveType__')) {
                                        result = /* clone */ o.slice(0);
                                    }
                                    else {
                                        let length = o.length;
                                        result = /* newInstance */ new Array(length);
                                        while ((length-- > 0)) {
                                            {
                                                /* set */ (result[length] = length);
                                            }
                                        }
                                        ;
                                    }
                                }
                                else {
                                    try {
                                        result = org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A(o, "clone", null);
                                    }
                                    catch (__e) {
                                        if (__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.NoSuchMethodException") >= 0)) {
                                            const e = __e;
                                            throw new org.openprovenance.apache.commons.lang.exception.CloneFailedException("Cloneable type " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(o.constructor) + " has no clone method", e);
                                        }
                                        if (__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalAccessException") >= 0)) {
                                            const e = __e;
                                            throw new org.openprovenance.apache.commons.lang.exception.CloneFailedException("Cannot clone Cloneable type " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(o.constructor), e);
                                        }
                                        if (__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.reflect.InvocationTargetException") >= 0)) {
                                            const e = __e;
                                            throw new org.openprovenance.apache.commons.lang.exception.CloneFailedException("Exception cloning Cloneable type " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(o.constructor), e.getTargetException());
                                        }
                                    }
                                }
                                return result;
                            }
                            return null;
                        }
                        /**
                         * Clone an object if possible. This method is similar to {@link #clone(Object)}, but will
                         * return the provided instance as the return value instead of <code>null</code> if the instance
                         * is not cloneable. This is more convenient if the caller uses different
                         * implementations (e1.g. of a service) and some of the implementations do not allow concurrent
                         * processing or have state. In such cases the implementation can simply provide a proper
                         * clone implementation and the caller's code does not have to change.
                         *
                         * @param {*} o the object to clone
                         * @return {*} the clone if the object implements {@link Cloneable} otherwise the object itself
                         * @throws CloneFailedException if the object is cloneable and the clone operation fails
                         * @since 2.6
                         */
                        static cloneIfPossible(o) {
                            const clone = ObjectUtils.clone(o);
                            return clone == null ? o : clone;
                        }
                    }
                    lang.ObjectUtils = ObjectUtils;
                    ObjectUtils["__class"] = "org.openprovenance.apache.commons.lang.ObjectUtils";
                    (function (ObjectUtils) {
                        /**
                         * <p>Class used as a null placeholder where <code>null</code>
                         * has another meaning.</p>
                         *
                         * <p>For example, in a <code>HashMap</code> the
                         * {@link java.util.HashMap#get(Object)} method returns
                         * <code>null</code> if the <code>Map</code> contains
                         * <code>null</code> or if there is no matching key. The
                         * <code>Null</code> placeholder can be used to distinguish between
                         * these two cases.</p>
                         *
                         * <p>Another example is <code>Hashtable</code>, where <code>null</code>
                         * cannot be stored.</p>
                         * @class
                         */
                        class Null {
                            constructor() {
                            }
                            /**
                             * <p>Ensure singleton.</p>
                             *
                             * @return {*} the singleton value
                             * @private
                             */
                            readResolve() {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.NULL_$LI$();
                            }
                        }
                        /**
                         * Required for serialization support. Declare serialization compatibility with Commons Lang 1.0
                         *
                         * @see Serializable
                         */
                        Null.serialVersionUID = 7092611880189329093;
                        ObjectUtils.Null = Null;
                        Null["__class"] = "org.openprovenance.apache.commons.lang.ObjectUtils.Null";
                        Null["__interfaces"] = ["java.io.Serializable"];
                    })(ObjectUtils = lang.ObjectUtils || (lang.ObjectUtils = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
