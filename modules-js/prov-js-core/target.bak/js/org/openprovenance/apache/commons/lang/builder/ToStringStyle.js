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
                    var builder;
                    (function (builder) {
                        /**
                         * <p>Controls {@code String} formatting for {@link ToStringBuilder}.
                         * The main public interface is always via {@code ToStringBuilder}.</p>
                         *
                         * <p>These classes are intended to be used as {@code Singletons}.
                         * There is no need to instantiate a new style each time. A program
                         * will generally use one of the predefined constants on this class.
                         * Alternatively, the  StandardToStringStyle class can be used
                         * to set the individual settings. Thus most styles can be achieved
                         * without subclassing.</p>
                         *
                         * <p>If required, a subclass can override as many or as few of the
                         * methods as it requires. Each object type (from {@code boolean}
                         * to {@code long} to {@code Object} to {@code int[]}) has
                         * its own methods to output it. Most have two versions, detail and summary.
                         *
                         * <p>For example, the detail version of the array based methods will
                         * output the whole array, whereas the summary method will just output
                         * the array length.</p>
                         *
                         * <p>If you want to format the output of certain objects, such as dates, you
                         * must create a subclass and override a method.
                         * <pre>
                         * public class MyStyle extends ToStringStyle {
                         * protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
                         * if (value instanceof Date) {
                         * value = new SimpleDateFormat("yyyy-MM-dd").format(value);
                         * }
                         * buffer.append(value);
                         * }
                         * }
                         * </pre>
                         *
                         * @author Apache Software Foundation
                         * @author Gary Gregory
                         * @author Pete Gieser
                         * @author Masato Tezuka
                         * @since 1.0
                         * @version $Id: ToStringStyle.java 907168 2010-02-06 03:33:50Z mbenson $
                         * @class
                         */
                        class ToStringStyle {
                            constructor() {
                                this.useFieldNames = true;
                                this.useClassName = true;
                                this.useShortClassName = false;
                                this.useIdentityHashCode = true;
                                this.contentStart = "[";
                                this.contentEnd = "]";
                                this.fieldNameValueSeparator = "=";
                                this.fieldSeparatorAtStart = false;
                                this.fieldSeparatorAtEnd = false;
                                this.fieldSeparator = ",";
                                this.arrayStart = "{";
                                this.arraySeparator = ",";
                                this.arrayContentDetail = true;
                                this.arrayEnd = "}";
                                this.defaultFullDetail = true;
                                this.nullText = "<null>";
                                this.sizeStartText = "<size=";
                                this.sizeEndText = ">";
                                this.summaryObjectStartText = "<";
                                this.summaryObjectEndText = ">";
                            }
                            static DEFAULT_STYLE_$LI$() { if (ToStringStyle.DEFAULT_STYLE == null) {
                                ToStringStyle.DEFAULT_STYLE = new ToStringStyle.DefaultToStringStyle();
                            } return ToStringStyle.DEFAULT_STYLE; }
                            static MULTI_LINE_STYLE_$LI$() { if (ToStringStyle.MULTI_LINE_STYLE == null) {
                                ToStringStyle.MULTI_LINE_STYLE = new ToStringStyle.MultiLineToStringStyle();
                            } return ToStringStyle.MULTI_LINE_STYLE; }
                            static NO_FIELD_NAMES_STYLE_$LI$() { if (ToStringStyle.NO_FIELD_NAMES_STYLE == null) {
                                ToStringStyle.NO_FIELD_NAMES_STYLE = new ToStringStyle.NoFieldNameToStringStyle();
                            } return ToStringStyle.NO_FIELD_NAMES_STYLE; }
                            static SHORT_PREFIX_STYLE_$LI$() { if (ToStringStyle.SHORT_PREFIX_STYLE == null) {
                                ToStringStyle.SHORT_PREFIX_STYLE = new ToStringStyle.ShortPrefixToStringStyle();
                            } return ToStringStyle.SHORT_PREFIX_STYLE; }
                            static SIMPLE_STYLE_$LI$() { if (ToStringStyle.SIMPLE_STYLE == null) {
                                ToStringStyle.SIMPLE_STYLE = new ToStringStyle.SimpleToStringStyle();
                            } return ToStringStyle.SIMPLE_STYLE; }
                            static REGISTRY_$LI$() { if (ToStringStyle.REGISTRY == null) {
                                ToStringStyle.REGISTRY = (new java.lang.ThreadLocal());
                            } return ToStringStyle.REGISTRY; }
                            /**
                             * <p>
                             * Returns the registry of objects being traversed by the {@code reflectionToString}
                             * methods in the current thread.
                             * </p>
                             *
                             * @return {*} Set the registry of objects being traversed
                             */
                            static getRegistry() {
                                return ((tlObj) => { if (tlObj.___value) {
                                    return tlObj.___value;
                                }
                                else {
                                    return tlObj.___value = tlObj.initialValue();
                                } })(ToStringStyle.REGISTRY_$LI$());
                            }
                            /**
                             * <p>
                             * Returns {@code true} if the registry contains the given object.
                             * Used by the reflection methods to avoid infinite loops.
                             * </p>
                             *
                             * @param {*} value
                             * The object to lookup in the registry.
                             * @return {boolean} boolean {@code true} if the registry contains the given
                             * object.
                             */
                            static isRegistered(value) {
                                const m = ToStringStyle.getRegistry();
                                return m != null && /* containsKey */ ((m, k) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        return true;
                                    } return false; })(m, value);
                            }
                            /**
                             * <p>
                             * Registers the given object. Used by the reflection methods to avoid
                             * infinite loops.
                             * </p>
                             *
                             * @param {*} value
                             * The object to register.
                             */
                            static register(value) {
                                if (value != null) {
                                    let m = ToStringStyle.getRegistry();
                                    if (m == null) {
                                        m = ({});
                                        ToStringStyle.REGISTRY_$LI$().set(m);
                                    }
                                    /* put */ ((m, k, v) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            m.entries[i].value = v;
                                            return;
                                        } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(m, value, null);
                                }
                            }
                            /**
                             * <p>
                             * Unregisters the given object.
                             * </p>
                             *
                             * <p>
                             * Used by the reflection methods to avoid infinite loops.
                             * </p>
                             *
                             * @param {*} value
                             * The object to unregister.
                             */
                            static unregister(value) {
                                if (value != null) {
                                    const m = ToStringStyle.getRegistry();
                                    if (m != null) {
                                        /* remove */ ((m, k) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                return m.entries.splice(i, 1)[0];
                                            } })(m, value);
                                        if ( /* isEmpty */((m) => { if (m.entries == null)
                                            m.entries = []; return m.entries.length == 0; })(m)) {
                                            ToStringStyle.REGISTRY_$LI$().set(null);
                                        }
                                    }
                                }
                            }
                            /**
                             * <p>Append to the {@code toString} the superclass toString.</p>
                             * <p>NOTE: It assumes that the toString has been created from the same ToStringStyle. </p>
                             *
                             * <p>A {@code null} {@code superToString} is ignored.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} superToString  the {@code super.toString()}
                             * @since 2.0
                             */
                            appendSuper(buffer, superToString) {
                                this.appendToString(buffer, superToString);
                            }
                            /**
                             * <p>Append to the {@code toString} another toString.</p>
                             * <p>NOTE: It assumes that the toString has been created from the same ToStringStyle. </p>
                             *
                             * <p>A {@code null} {@code toString} is ignored.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} toString  the additional {@code toString}
                             * @since 2.0
                             */
                            appendToString(buffer, toString) {
                                if (toString != null) {
                                    const pos1 = toString.indexOf(this.contentStart) + this.contentStart.length;
                                    const pos2 = toString.lastIndexOf(this.contentEnd);
                                    if (pos1 !== pos2 && pos1 >= 0 && pos2 >= 0) {
                                        const data = toString.substring(pos1, pos2);
                                        if (this.fieldSeparatorAtStart) {
                                            this.removeLastFieldSeparator(buffer);
                                        }
                                        /* append */ (sb => { sb.str += data; return sb; })(buffer);
                                        this.appendFieldSeparator(buffer);
                                    }
                                }
                            }
                            /**
                             * <p>Append to the {@code toString} the start of data indicator.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {*} object  the {@code Object} to build a {@code toString} for
                             */
                            appendStart(buffer, object) {
                                if (object != null) {
                                    this.appendClassName(buffer, object);
                                    this.appendIdentityHashCode(buffer, object);
                                    this.appendContentStart(buffer);
                                    if (this.fieldSeparatorAtStart) {
                                        this.appendFieldSeparator(buffer);
                                    }
                                }
                            }
                            /**
                             * <p>Append to the {@code toString} the end of data indicator.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {*} object  the {@code Object} to build a
                             * {@code toString} for.
                             */
                            appendEnd(buffer, object) {
                                if (this.fieldSeparatorAtEnd === false) {
                                    this.removeLastFieldSeparator(buffer);
                                }
                                this.appendContentEnd(buffer);
                                ToStringStyle.unregister(object);
                            }
                            /**
                             * <p>Remove the last field separator from the buffer.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @since 2.0
                             */
                            removeLastFieldSeparator(buffer) {
                                const len = buffer.str.length;
                                const sepLen = this.fieldSeparator.length;
                                if (len > 0 && sepLen > 0 && len >= sepLen) {
                                    let match = true;
                                    for (let i = 0; i < sepLen; i++) {
                                        {
                                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(/* charAt */ buffer.str.charAt(len - 1 - i)) != (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.fieldSeparator.charAt(sepLen - 1 - i))) {
                                                match = false;
                                                break;
                                            }
                                        }
                                        ;
                                    }
                                    if (match) {
                                        /* setLength */ ((sb, length) => sb.str = sb.str.substring(0, length))(buffer, len - sepLen);
                                    }
                                }
                            }
                            append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(buffer, fieldName, value, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (value == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else {
                                    this.appendInternal(buffer, fieldName, value, this.isFullDetail(fullDetail));
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            /**
                             * <p>Append to the {@code toString} an {@code Object},
                             * correctly interpreting its type.</p>
                             *
                             * <p>This method performs the main lookup by Class type to correctly
                             * route arrays, {@code Collections}, {@code Maps} and
                             * {@code Objects} to the appropriate method.</p>
                             *
                             * <p>Either detail or summary views can be specified.</p>
                             *
                             * <p>If a cycle is detected, an object will be appended with the
                             * {@code Object.toString()} format.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             * @param {*} value  the value to add to the {@code toString},
                             * not {@code null}
                             * @param {boolean} detail  output detail or not
                             */
                            appendInternal(buffer, fieldName, value, detail) {
                                if (ToStringStyle.isRegistered(value) && !((typeof value === 'number') || (typeof value === 'boolean') || (typeof value === 'string'))) {
                                    this.appendCyclicObject(buffer, fieldName, value);
                                    return;
                                }
                                ToStringStyle.register(value);
                                try {
                                    if (value != null && (value instanceof Array)) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Collection(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummarySize(buffer, fieldName, /* size */ value.length);
                                        }
                                    }
                                    else if (value != null && (value instanceof Object)) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Map(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummarySize(buffer, fieldName, /* size */ ((m) => { if (m.entries == null)
                                                m.entries = []; return m.entries.length; })(value));
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'number')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'number')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'number')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'number')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'string')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'number')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'number')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value != null && value instanceof Array && (value.length == 0 || value[0] == null || typeof value[0] === 'boolean')) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, value);
                                        }
                                    }
                                    else if (value.constructor.isArray()) {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, value);
                                        }
                                    }
                                    else {
                                        if (detail) {
                                            this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, value);
                                        }
                                        else {
                                            this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, value);
                                        }
                                    }
                                }
                                finally {
                                    ToStringStyle.unregister(value);
                                }
                            }
                            /**
                             * <p>Append to the {@code toString} an {@code Object}
                             * value that has been detected to participate in a cycle. This
                             * implementation will print the standard string value of the value.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             * @param {*} value  the value to add to the {@code toString},
                             * not {@code null}
                             *
                             * @since 2.2
                             */
                            appendCyclicObject(buffer, fieldName, value) {
                                org.openprovenance.apache.commons.lang.ObjectUtils.identityToString$java_lang_StringBuffer$java_lang_Object(buffer, value);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Collection(buffer, fieldName, coll) {
                                /* append */ (sb => { sb.str += coll; return sb; })(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} a {@code Collection}.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             * @param {Array} coll  the {@code Collection} to add to the
                             * {@code toString}, not {@code null}
                             */
                            appendDetail(buffer, fieldName, coll) {
                                if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && (coll instanceof Array)) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Collection(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && (coll instanceof Object)) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Map(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (coll[0] != null))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'number'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'number'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'number'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'number'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'string'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'number'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'number'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof Array && (coll.length == 0 || coll[0] == null || (typeof coll[0] === 'boolean'))) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'string') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'boolean') || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, coll);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null) || coll === null)) {
                                    return this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, coll);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Map(buffer, fieldName, map) {
                                /* append */ (sb => { sb.str += map; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += this.summaryObjectStartText; return sb; })(buffer);
                                /* append */ (sb => { sb.str += this.getShortClassName(value.constructor); return sb; })(buffer);
                                /* append */ (sb => { sb.str += this.summaryObjectEndText; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, value) {
                                this.appendFieldStart(buffer, fieldName);
                                this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, value);
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, value) {
                                /* append */ (sb => { sb.str += value; return sb; })(buffer);
                            }
                            append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            /**
                             * <p>Append to the {@code toString} an {@code Object}
                             * array.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name
                             * @param {java.lang.Object[]} array  the array to add to the toString
                             * @param {boolean} fullDetail  {@code true} for detail, {@code false}
                             * for summary info, {@code null} for style decides
                             */
                            append(buffer, fieldName, array, fullDetail) {
                                if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(buffer, fieldName, array, fullDetail);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'string') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'boolean') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, array);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        const item = array[i];
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        if (item == null) {
                                            this.appendNullText(buffer, fieldName);
                                        }
                                        else {
                                            this.appendInternal(buffer, fieldName, item, this.arrayContentDetail);
                                        }
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} the detail of an array type.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             * @param {*} array  the array to add to the {@code toString},
                             * not {@code null}
                             * @since 2.0
                             */
                            reflectionAppendArrayDetail(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                const length = array.length;
                                for (let i = 0; i < length; i++) {
                                    {
                                        const item = array[i];
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        if (item == null) {
                                            this.appendNullText(buffer, fieldName);
                                        }
                                        else {
                                            this.appendInternal(buffer, fieldName, item, this.arrayContentDetail);
                                        }
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            /**
                             * <p>Append to the {@code toString} a summary of an
                             * {@code Object} array.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             * @param {java.lang.Object[]} array  the array to add to the {@code toString},
                             * not {@code null}
                             */
                            appendSummary(buffer, fieldName, array) {
                                if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array);
                                }
                                else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null) || array === null)) {
                                    return this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, array);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(buffer, fieldName, array, fullDetail) {
                                this.appendFieldStart(buffer, fieldName);
                                if (array == null) {
                                    this.appendNullText(buffer, fieldName);
                                }
                                else if (this.isFullDetail(fullDetail)) {
                                    this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array);
                                }
                                else {
                                    this.appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array);
                                }
                                this.appendFieldEnd(buffer, fieldName);
                            }
                            appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array) {
                                /* append */ (sb => { sb.str += this.arrayStart; return sb; })(buffer);
                                for (let i = 0; i < array.length; i++) {
                                    {
                                        if (i > 0) {
                                            /* append */ (sb => { sb.str += this.arraySeparator; return sb; })(buffer);
                                        }
                                        this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, array[i]);
                                    }
                                    ;
                                }
                                /* append */ (sb => { sb.str += this.arrayEnd; return sb; })(buffer);
                            }
                            appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array) {
                                this.appendSummarySize(buffer, fieldName, array.length);
                            }
                            /**
                             * <p>Append to the {@code toString} the class name.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {*} object  the {@code Object} whose name to output
                             */
                            appendClassName(buffer, object) {
                                if (this.useClassName && object != null) {
                                    ToStringStyle.register(object);
                                    if (this.useShortClassName) {
                                        /* append */ (sb => { sb.str += this.getShortClassName(object.constructor); return sb; })(buffer);
                                    }
                                    else {
                                        /* append */ (sb => { sb.str += (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor); return sb; })(buffer);
                                    }
                                }
                            }
                            /**
                             * <p>Append the {@link System#identityHashCode(Object)}.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {*} object  the {@code Object} whose id to output
                             */
                            appendIdentityHashCode(buffer, object) {
                                if (this.isUseIdentityHashCode() && object != null) {
                                    ToStringStyle.register(object);
                                    /* append */ (sb => { sb.str += '@'; return sb; })(buffer);
                                    /* append */ (sb => { sb.str += javaemul.internal.IntegerHelper.toHexString(java.lang.System.identityHashCode(object)); return sb; })(buffer);
                                }
                            }
                            /**
                             * <p>Append to the {@code toString} the content start.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             */
                            appendContentStart(buffer) {
                                /* append */ (sb => { sb.str += this.contentStart; return sb; })(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} the content end.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             */
                            appendContentEnd(buffer) {
                                /* append */ (sb => { sb.str += this.contentEnd; return sb; })(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} an indicator for {@code null}.</p>
                             *
                             * <p>The default indicator is {@code '&lt;null&gt;'}.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             */
                            appendNullText(buffer, fieldName) {
                                /* append */ (sb => { sb.str += this.nullText; return sb; })(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} the field separator.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             */
                            appendFieldSeparator(buffer) {
                                /* append */ (sb => { sb.str += this.fieldSeparator; return sb; })(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} the field start.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name
                             */
                            appendFieldStart(buffer, fieldName) {
                                if (this.useFieldNames && fieldName != null) {
                                    /* append */ (sb => { sb.str += fieldName; return sb; })(buffer);
                                    /* append */ (sb => { sb.str += this.fieldNameValueSeparator; return sb; })(buffer);
                                }
                            }
                            /**
                             * <p>Append to the {@code toString}  the field end.</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             */
                            appendFieldEnd(buffer, fieldName) {
                                this.appendFieldSeparator(buffer);
                            }
                            /**
                             * <p>Append to the {@code toString} a size summary.</p>
                             *
                             * <p>The size summary is used to summarize the contents of
                             * {@code Collections}, {@code Maps} and arrays.</p>
                             *
                             * <p>The output consists of a prefix, the passed in size
                             * and a suffix.</p>
                             *
                             * <p>The default format is {@code '&lt;size=n&gt;'} .</p>
                             *
                             * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
                             * @param {string} fieldName  the field name, typically not used as already appended
                             * @param {number} size  the size to append
                             */
                            appendSummarySize(buffer, fieldName, size) {
                                /* append */ (sb => { sb.str += this.sizeStartText; return sb; })(buffer);
                                /* append */ (sb => { sb.str += size; return sb; })(buffer);
                                /* append */ (sb => { sb.str += this.sizeEndText; return sb; })(buffer);
                            }
                            /**
                             * <p>Is this field to be output in full detail.</p>
                             *
                             * <p>This method converts a detail request into a detail level.
                             * The calling code may request full detail ({@code true}),
                             * but a subclass might ignore that and always return
                             * {@code false}. The calling code may pass in
                             * {@code null} indicating that it doesn't care about
                             * the detail level. In this case the default detail level is
                             * used.</p>
                             *
                             * @param {boolean} fullDetailRequest  the detail level requested
                             * @return {boolean} whether full detail is to be shown
                             */
                            isFullDetail(fullDetailRequest) {
                                if (fullDetailRequest == null) {
                                    return this.defaultFullDetail;
                                }
                                return /* booleanValue */ fullDetailRequest;
                            }
                            /**
                             * <p>Gets the short class name for a class.</p>
                             *
                             * <p>The short class name is the classname excluding
                             * the package name.</p>
                             *
                             * @param {*} cls  the {@code Class} to get the short name of
                             * @return {string} the short name
                             */
                            getShortClassName(cls) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Class(cls);
                            }
                            /**
                             * <p>Gets whether to use the class name.</p>
                             *
                             * @return {boolean} the current useClassName flag
                             */
                            isUseClassName() {
                                return this.useClassName;
                            }
                            /**
                             * <p>Sets whether to use the class name.</p>
                             *
                             * @param {boolean} useClassName  the new useClassName flag
                             */
                            setUseClassName(useClassName) {
                                this.useClassName = useClassName;
                            }
                            /**
                             * <p>Gets whether to output short or long class names.</p>
                             *
                             * @return {boolean} the current useShortClassName flag
                             * @since 2.0
                             */
                            isUseShortClassName() {
                                return this.useShortClassName;
                            }
                            /**
                             * <p>Gets whether to output short or long class names.</p>
                             *
                             * @return {boolean} the current shortClassName flag
                             * @deprecated Use {@link #isUseShortClassName()}
                             * Method will be removed in Commons Lang 3.0.
                             */
                            isShortClassName() {
                                return this.useShortClassName;
                            }
                            /**
                             * <p>Sets whether to output short or long class names.</p>
                             *
                             * @param {boolean} useShortClassName  the new useShortClassName flag
                             * @since 2.0
                             */
                            setUseShortClassName(useShortClassName) {
                                this.useShortClassName = useShortClassName;
                            }
                            /**
                             * <p>Sets whether to output short or long class names.</p>
                             *
                             * @param {boolean} shortClassName  the new shortClassName flag
                             * @deprecated Use {@link #setUseShortClassName(boolean)}
                             * Method will be removed in Commons Lang 3.0.
                             */
                            setShortClassName(shortClassName) {
                                this.useShortClassName = shortClassName;
                            }
                            /**
                             * <p>Gets whether to use the identity hash code.</p>
                             *
                             * @return {boolean} the current useIdentityHashCode flag
                             */
                            isUseIdentityHashCode() {
                                return this.useIdentityHashCode;
                            }
                            /**
                             * <p>Sets whether to use the identity hash code.</p>
                             *
                             * @param {boolean} useIdentityHashCode  the new useIdentityHashCode flag
                             */
                            setUseIdentityHashCode(useIdentityHashCode) {
                                this.useIdentityHashCode = useIdentityHashCode;
                            }
                            /**
                             * <p>Gets whether to use the field names passed in.</p>
                             *
                             * @return {boolean} the current useFieldNames flag
                             */
                            isUseFieldNames() {
                                return this.useFieldNames;
                            }
                            /**
                             * <p>Sets whether to use the field names passed in.</p>
                             *
                             * @param {boolean} useFieldNames  the new useFieldNames flag
                             */
                            setUseFieldNames(useFieldNames) {
                                this.useFieldNames = useFieldNames;
                            }
                            /**
                             * <p>Gets whether to use full detail when the caller doesn't
                             * specify.</p>
                             *
                             * @return {boolean} the current defaultFullDetail flag
                             */
                            isDefaultFullDetail() {
                                return this.defaultFullDetail;
                            }
                            /**
                             * <p>Sets whether to use full detail when the caller doesn't
                             * specify.</p>
                             *
                             * @param {boolean} defaultFullDetail  the new defaultFullDetail flag
                             */
                            setDefaultFullDetail(defaultFullDetail) {
                                this.defaultFullDetail = defaultFullDetail;
                            }
                            /**
                             * <p>Gets whether to output array content detail.</p>
                             *
                             * @return {boolean} the current array content detail setting
                             */
                            isArrayContentDetail() {
                                return this.arrayContentDetail;
                            }
                            /**
                             * <p>Sets whether to output array content detail.</p>
                             *
                             * @param {boolean} arrayContentDetail  the new arrayContentDetail flag
                             */
                            setArrayContentDetail(arrayContentDetail) {
                                this.arrayContentDetail = arrayContentDetail;
                            }
                            /**
                             * <p>Gets the array start text.</p>
                             *
                             * @return {string} the current array start text
                             */
                            getArrayStart() {
                                return this.arrayStart;
                            }
                            /**
                             * <p>Sets the array start text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} arrayStart  the new array start text
                             */
                            setArrayStart(arrayStart) {
                                if (arrayStart == null) {
                                    arrayStart = "";
                                }
                                this.arrayStart = arrayStart;
                            }
                            /**
                             * <p>Gets the array end text.</p>
                             *
                             * @return {string} the current array end text
                             */
                            getArrayEnd() {
                                return this.arrayEnd;
                            }
                            /**
                             * <p>Sets the array end text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} arrayEnd  the new array end text
                             */
                            setArrayEnd(arrayEnd) {
                                if (arrayEnd == null) {
                                    arrayEnd = "";
                                }
                                this.arrayEnd = arrayEnd;
                            }
                            /**
                             * <p>Gets the array separator text.</p>
                             *
                             * @return {string} the current array separator text
                             */
                            getArraySeparator() {
                                return this.arraySeparator;
                            }
                            /**
                             * <p>Sets the array separator text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} arraySeparator  the new array separator text
                             */
                            setArraySeparator(arraySeparator) {
                                if (arraySeparator == null) {
                                    arraySeparator = "";
                                }
                                this.arraySeparator = arraySeparator;
                            }
                            /**
                             * <p>Gets the content start text.</p>
                             *
                             * @return {string} the current content start text
                             */
                            getContentStart() {
                                return this.contentStart;
                            }
                            /**
                             * <p>Sets the content start text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} contentStart  the new content start text
                             */
                            setContentStart(contentStart) {
                                if (contentStart == null) {
                                    contentStart = "";
                                }
                                this.contentStart = contentStart;
                            }
                            /**
                             * <p>Gets the content end text.</p>
                             *
                             * @return {string} the current content end text
                             */
                            getContentEnd() {
                                return this.contentEnd;
                            }
                            /**
                             * <p>Sets the content end text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} contentEnd  the new content end text
                             */
                            setContentEnd(contentEnd) {
                                if (contentEnd == null) {
                                    contentEnd = "";
                                }
                                this.contentEnd = contentEnd;
                            }
                            /**
                             * <p>Gets the field name value separator text.</p>
                             *
                             * @return {string} the current field name value separator text
                             */
                            getFieldNameValueSeparator() {
                                return this.fieldNameValueSeparator;
                            }
                            /**
                             * <p>Sets the field name value separator text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} fieldNameValueSeparator  the new field name value separator text
                             */
                            setFieldNameValueSeparator(fieldNameValueSeparator) {
                                if (fieldNameValueSeparator == null) {
                                    fieldNameValueSeparator = "";
                                }
                                this.fieldNameValueSeparator = fieldNameValueSeparator;
                            }
                            /**
                             * <p>Gets the field separator text.</p>
                             *
                             * @return {string} the current field separator text
                             */
                            getFieldSeparator() {
                                return this.fieldSeparator;
                            }
                            /**
                             * <p>Sets the field separator text.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} fieldSeparator  the new field separator text
                             */
                            setFieldSeparator(fieldSeparator) {
                                if (fieldSeparator == null) {
                                    fieldSeparator = "";
                                }
                                this.fieldSeparator = fieldSeparator;
                            }
                            /**
                             * <p>Gets whether the field separator should be added at the start
                             * of each buffer.</p>
                             *
                             * @return {boolean} the fieldSeparatorAtStart flag
                             * @since 2.0
                             */
                            isFieldSeparatorAtStart() {
                                return this.fieldSeparatorAtStart;
                            }
                            /**
                             * <p>Sets whether the field separator should be added at the start
                             * of each buffer.</p>
                             *
                             * @param {boolean} fieldSeparatorAtStart  the fieldSeparatorAtStart flag
                             * @since 2.0
                             */
                            setFieldSeparatorAtStart(fieldSeparatorAtStart) {
                                this.fieldSeparatorAtStart = fieldSeparatorAtStart;
                            }
                            /**
                             * <p>Gets whether the field separator should be added at the end
                             * of each buffer.</p>
                             *
                             * @return {boolean} fieldSeparatorAtEnd flag
                             * @since 2.0
                             */
                            isFieldSeparatorAtEnd() {
                                return this.fieldSeparatorAtEnd;
                            }
                            /**
                             * <p>Sets whether the field separator should be added at the end
                             * of each buffer.</p>
                             *
                             * @param {boolean} fieldSeparatorAtEnd  the fieldSeparatorAtEnd flag
                             * @since 2.0
                             */
                            setFieldSeparatorAtEnd(fieldSeparatorAtEnd) {
                                this.fieldSeparatorAtEnd = fieldSeparatorAtEnd;
                            }
                            /**
                             * <p>Gets the text to output when {@code null} found.</p>
                             *
                             * @return {string} the current text to output when null found
                             */
                            getNullText() {
                                return this.nullText;
                            }
                            /**
                             * <p>Sets the text to output when {@code null} found.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} nullText  the new text to output when null found
                             */
                            setNullText(nullText) {
                                if (nullText == null) {
                                    nullText = "";
                                }
                                this.nullText = nullText;
                            }
                            /**
                             * <p>Gets the start text to output when a {@code Collection},
                             * {@code Map} or array size is output.</p>
                             *
                             * <p>This is output before the size value.</p>
                             *
                             * @return {string} the current start of size text
                             */
                            getSizeStartText() {
                                return this.sizeStartText;
                            }
                            /**
                             * <p>Sets the start text to output when a {@code Collection},
                             * {@code Map} or array size is output.</p>
                             *
                             * <p>This is output before the size value.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} sizeStartText  the new start of size text
                             */
                            setSizeStartText(sizeStartText) {
                                if (sizeStartText == null) {
                                    sizeStartText = "";
                                }
                                this.sizeStartText = sizeStartText;
                            }
                            /**
                             * <p>Gets the end text to output when a {@code Collection},
                             * {@code Map} or array size is output.</p>
                             *
                             * <p>This is output after the size value.</p>
                             *
                             * @return {string} the current end of size text
                             */
                            getSizeEndText() {
                                return this.sizeEndText;
                            }
                            /**
                             * <p>Sets the end text to output when a {@code Collection},
                             * {@code Map} or array size is output.</p>
                             *
                             * <p>This is output after the size value.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} sizeEndText  the new end of size text
                             */
                            setSizeEndText(sizeEndText) {
                                if (sizeEndText == null) {
                                    sizeEndText = "";
                                }
                                this.sizeEndText = sizeEndText;
                            }
                            /**
                             * <p>Gets the start text to output when an {@code Object} is
                             * output in summary mode.</p>
                             *
                             * <p>This is output before the size value.</p>
                             *
                             * @return {string} the current start of summary text
                             */
                            getSummaryObjectStartText() {
                                return this.summaryObjectStartText;
                            }
                            /**
                             * <p>Sets the start text to output when an {@code Object} is
                             * output in summary mode.</p>
                             *
                             * <p>This is output before the size value.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} summaryObjectStartText  the new start of summary text
                             */
                            setSummaryObjectStartText(summaryObjectStartText) {
                                if (summaryObjectStartText == null) {
                                    summaryObjectStartText = "";
                                }
                                this.summaryObjectStartText = summaryObjectStartText;
                            }
                            /**
                             * <p>Gets the end text to output when an {@code Object} is
                             * output in summary mode.</p>
                             *
                             * <p>This is output after the size value.</p>
                             *
                             * @return {string} the current end of summary text
                             */
                            getSummaryObjectEndText() {
                                return this.summaryObjectEndText;
                            }
                            /**
                             * <p>Sets the end text to output when an {@code Object} is
                             * output in summary mode.</p>
                             *
                             * <p>This is output after the size value.</p>
                             *
                             * <p>{@code null} is accepted, but will be converted to
                             * an empty String.</p>
                             *
                             * @param {string} summaryObjectEndText  the new end of summary text
                             */
                            setSummaryObjectEndText(summaryObjectEndText) {
                                if (summaryObjectEndText == null) {
                                    summaryObjectEndText = "";
                                }
                                this.summaryObjectEndText = summaryObjectEndText;
                            }
                        }
                        builder.ToStringStyle = ToStringStyle;
                        ToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle";
                        ToStringStyle["__interfaces"] = ["java.io.Serializable"];
                        (function (ToStringStyle) {
                            /**
                             * <p>Default {@code ToStringStyle}.</p>
                             *
                             * <p>This is an inner class rather than using
                             * {@code StandardToStringStyle} to ensure its immutability.</p>
                             * @extends org.openprovenance.apache.commons.lang.builder.ToStringStyle
                             * @class
                             */
                            class DefaultToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
                                constructor() {
                                    super();
                                }
                                /**
                                 * <p>Ensure {@code Singleton} after serialization.</p>
                                 *
                                 * @return {*} the singleton
                                 * @private
                                 */
                                readResolve() {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringStyle.DEFAULT_STYLE_$LI$();
                                }
                            }
                            /**
                             * Required for serialization support.
                             *
                             * @see Serializable
                             */
                            DefaultToStringStyle.serialVersionUID = 1;
                            ToStringStyle.DefaultToStringStyle = DefaultToStringStyle;
                            DefaultToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle.DefaultToStringStyle";
                            DefaultToStringStyle["__interfaces"] = ["java.io.Serializable"];
                            /**
                             * <p>{@code ToStringStyle} that does not print out
                             * the field names.</p>
                             *
                             * <p>This is an inner class rather than using
                             * {@code StandardToStringStyle} to ensure its immutability.
                             * @extends org.openprovenance.apache.commons.lang.builder.ToStringStyle
                             * @class
                             */
                            class NoFieldNameToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
                                constructor() {
                                    super();
                                    this.setUseFieldNames(false);
                                }
                                /**
                                 * <p>Ensure {@code Singleton} after serialization.</p>
                                 *
                                 * @return {*} the singleton
                                 * @private
                                 */
                                readResolve() {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringStyle.NO_FIELD_NAMES_STYLE_$LI$();
                                }
                            }
                            NoFieldNameToStringStyle.serialVersionUID = 1;
                            ToStringStyle.NoFieldNameToStringStyle = NoFieldNameToStringStyle;
                            NoFieldNameToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle.NoFieldNameToStringStyle";
                            NoFieldNameToStringStyle["__interfaces"] = ["java.io.Serializable"];
                            /**
                             * <p>{@code ToStringStyle} that prints out the short
                             * class name and no identity hashcode.</p>
                             *
                             * <p>This is an inner class rather than using
                             * {@code StandardToStringStyle} to ensure its immutability.</p>
                             * @extends org.openprovenance.apache.commons.lang.builder.ToStringStyle
                             * @class
                             */
                            class ShortPrefixToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
                                constructor() {
                                    super();
                                    this.setUseShortClassName(true);
                                    this.setUseIdentityHashCode(false);
                                }
                                /**
                                 * <p>Ensure {@code Singleton</ode> after serialization.</p>
                                 * @return {*} the singleton
                                 * @private
                                 */
                                readResolve() {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE_$LI$();
                                }
                            }
                            ShortPrefixToStringStyle.serialVersionUID = 1;
                            ToStringStyle.ShortPrefixToStringStyle = ShortPrefixToStringStyle;
                            ShortPrefixToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle.ShortPrefixToStringStyle";
                            ShortPrefixToStringStyle["__interfaces"] = ["java.io.Serializable"];
                            /**
                             * <p>{@code ToStringStyle} that does not print out the
                             * classname, identity hashcode, content start or field name.</p>
                             *
                             * <p>This is an inner class rather than using
                             * {@code StandardToStringStyle} to ensure its immutability.</p>
                             * @extends org.openprovenance.apache.commons.lang.builder.ToStringStyle
                             * @class
                             */
                            class SimpleToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
                                constructor() {
                                    super();
                                    this.setUseClassName(false);
                                    this.setUseIdentityHashCode(false);
                                    this.setUseFieldNames(false);
                                    this.setContentStart("");
                                    this.setContentEnd("");
                                }
                                /**
                                 * <p>Ensure {@code Singleton</ode> after serialization.</p>
                                 * @return {*} the singleton
                                 * @private
                                 */
                                readResolve() {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringStyle.SIMPLE_STYLE_$LI$();
                                }
                            }
                            SimpleToStringStyle.serialVersionUID = 1;
                            ToStringStyle.SimpleToStringStyle = SimpleToStringStyle;
                            SimpleToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle.SimpleToStringStyle";
                            SimpleToStringStyle["__interfaces"] = ["java.io.Serializable"];
                            /**
                             * <p>{@code ToStringStyle} that outputs on multiple lines.</p>
                             *
                             * <p>This is an inner class rather than using
                             * {@code StandardToStringStyle} to ensure its immutability.</p>
                             * @extends org.openprovenance.apache.commons.lang.builder.ToStringStyle
                             * @class
                             */
                            class MultiLineToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
                                constructor() {
                                    super();
                                    this.setContentStart("[");
                                    this.setFieldSeparator(org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$() + "  ");
                                    this.setFieldSeparatorAtStart(true);
                                    this.setContentEnd(org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$() + "]");
                                }
                                /**
                                 * <p>Ensure {@code Singleton} after serialization.</p>
                                 *
                                 * @return {*} the singleton
                                 * @private
                                 */
                                readResolve() {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringStyle.MULTI_LINE_STYLE_$LI$();
                                }
                            }
                            MultiLineToStringStyle.serialVersionUID = 1;
                            ToStringStyle.MultiLineToStringStyle = MultiLineToStringStyle;
                            MultiLineToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle.MultiLineToStringStyle";
                            MultiLineToStringStyle["__interfaces"] = ["java.io.Serializable"];
                        })(ToStringStyle = builder.ToStringStyle || (builder.ToStringStyle = {}));
                    })(builder = lang.builder || (lang.builder = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
