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
                         * <p>Constructs a builder for the specified object.</p>
                         *
                         * <p>If the style is <code>null</code>, the default style is used.</p>
                         *
                         * <p>If the buffer is <code>null</code>, a new one is created.</p>
                         *
                         * @param {*} object  the Object to build a <code>toString</code> for, not recommended to be null
                         * @param {org.openprovenance.apache.commons.lang.builder.ToStringStyle} style  the style of the <code>toString</code> to create, null uses the default style
                         * @param {{ str: string, toString: Function }} buffer  the <code>StringBuffer</code> to populate, may be null
                         * @class
                         * @author Apache Software Foundation
                         */
                        class ToStringBuilder {
                            constructor(object, style, buffer) {
                                if (((object != null) || object === null) && ((style != null && style instanceof org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((buffer != null && (buffer instanceof Object)) || buffer === null)) {
                                    let __args = arguments;
                                    if (this.buffer === undefined) {
                                        this.buffer = null;
                                    }
                                    if (this.object === undefined) {
                                        this.object = null;
                                    }
                                    if (this.style === undefined) {
                                        this.style = null;
                                    }
                                    if (style == null) {
                                        style = ToStringBuilder.getDefaultStyle();
                                    }
                                    if (buffer == null) {
                                        buffer = { str: "", toString: function () { return this.str; } };
                                    }
                                    this.buffer = buffer;
                                    this.style = style;
                                    this.object = object;
                                    style.appendStart(buffer, object);
                                }
                                else if (((object != null) || object === null) && ((style != null && style instanceof org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && buffer === undefined) {
                                    let __args = arguments;
                                    {
                                        let __args = arguments;
                                        let buffer = null;
                                        if (this.buffer === undefined) {
                                            this.buffer = null;
                                        }
                                        if (this.object === undefined) {
                                            this.object = null;
                                        }
                                        if (this.style === undefined) {
                                            this.style = null;
                                        }
                                        if (style == null) {
                                            style = ToStringBuilder.getDefaultStyle();
                                        }
                                        if (buffer == null) {
                                            buffer = { str: "", toString: function () { return this.str; } };
                                        }
                                        this.buffer = buffer;
                                        this.style = style;
                                        this.object = object;
                                        style.appendStart(buffer, object);
                                    }
                                    if (this.buffer === undefined) {
                                        this.buffer = null;
                                    }
                                    if (this.object === undefined) {
                                        this.object = null;
                                    }
                                    if (this.style === undefined) {
                                        this.style = null;
                                    }
                                }
                                else if (((object != null) || object === null) && style === undefined && buffer === undefined) {
                                    let __args = arguments;
                                    {
                                        let __args = arguments;
                                        let style = null;
                                        let buffer = null;
                                        if (this.buffer === undefined) {
                                            this.buffer = null;
                                        }
                                        if (this.object === undefined) {
                                            this.object = null;
                                        }
                                        if (this.style === undefined) {
                                            this.style = null;
                                        }
                                        if (style == null) {
                                            style = ToStringBuilder.getDefaultStyle();
                                        }
                                        if (buffer == null) {
                                            buffer = { str: "", toString: function () { return this.str; } };
                                        }
                                        this.buffer = buffer;
                                        this.style = style;
                                        this.object = object;
                                        style.appendStart(buffer, object);
                                    }
                                    if (this.buffer === undefined) {
                                        this.buffer = null;
                                    }
                                    if (this.object === undefined) {
                                        this.object = null;
                                    }
                                    if (this.style === undefined) {
                                        this.style = null;
                                    }
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static defaultStyle_$LI$() { if (ToStringBuilder.defaultStyle == null) {
                                ToStringBuilder.defaultStyle = org.openprovenance.apache.commons.lang.builder.ToStringStyle.DEFAULT_STYLE_$LI$();
                            } return ToStringBuilder.defaultStyle; }
                            /**
                             * <p>Gets the default <code>ToStringStyle</code> to use.</p>
                             *
                             * <p>This method gets a singleton default value, typically for the whole JVM.
                             * Changing this default should generally only be done during application startup.
                             * It is recommended to pass a <code>ToStringStyle</code> to the constructor instead
                             * of using this global default.</p>
                             *
                             * <p>This method can be used from multiple threads.
                             * Internally, a <code>volatile</code> variable is used to provide the guarantee
                             * that the latest value set using {@link #setDefaultStyle} is the value returned.
                             * It is strongly recommended that the default style is only changed during application startup.</p>
                             *
                             * <p>One reason for changing the default could be to have a verbose style during
                             * development and a compact style in production.</p>
                             *
                             * @return {org.openprovenance.apache.commons.lang.builder.ToStringStyle} the default <code>ToStringStyle</code>, never null
                             */
                            static getDefaultStyle() {
                                return ToStringBuilder.defaultStyle_$LI$();
                            }
                            /**
                             * <p>Sets the default <code>ToStringStyle</code> to use.</p>
                             *
                             * <p>This method sets a singleton default value, typically for the whole JVM.
                             * Changing this default should generally only be done during application startup.
                             * It is recommended to pass a <code>ToStringStyle</code> to the constructor instead
                             * of changing this global default.</p>
                             *
                             * <p>This method is not intended for use from multiple threads.
                             * Internally, a <code>volatile</code> variable is used to provide the guarantee
                             * that the latest value set is the value returned from {@link #getDefaultStyle}.</p>
                             *
                             * @param {org.openprovenance.apache.commons.lang.builder.ToStringStyle} style  the default <code>ToStringStyle</code>
                             * @throws IllegalArgumentException if the style is <code>null</code>
                             */
                            static setDefaultStyle(style) {
                                if (style == null) {
                                    throw Object.defineProperty(new Error("The style must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                ToStringBuilder.defaultStyle = style;
                            }
                            static reflectionToString$java_lang_Object(object) {
                                return org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object(object);
                            }
                            static reflectionToString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle(object, style) {
                                return org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle(object, style);
                            }
                            static reflectionToString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean(object, style, outputTransients) {
                                return org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, style, outputTransients, false, null);
                            }
                            static reflectionToString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$java_lang_Class(object, style, outputTransients, reflectUpToClass) {
                                return org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, style, outputTransients, false, reflectUpToClass);
                            }
                            /**
                             * <p>Uses <code>ReflectionToStringBuilder</code> to generate a
                             * <code>toString</code> for the specified object.</p>
                             *
                             * @param {*} object  the Object to be output
                             * @param {org.openprovenance.apache.commons.lang.builder.ToStringStyle} style  the style of the <code>toString</code> to create, may be <code>null</code>
                             * @param {boolean} outputTransients  whether to include transient fields
                             * @param {*} reflectUpToClass  the superclass to reflect up to (inclusive), may be <code>null</code>
                             * @return {string} the String result
                             * @see ReflectionToStringBuilder#toString(Object,ToStringStyle,boolean,boolean,Class)
                             * @since 2.0
                             */
                            static reflectionToString(object, style, outputTransients, reflectUpToClass) {
                                if (((object != null) || object === null) && ((style != null && style instanceof org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(reflectUpToClass))) || reflectUpToClass === null)) {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringBuilder.reflectionToString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$java_lang_Class(object, style, outputTransients, reflectUpToClass);
                                }
                                else if (((object != null) || object === null) && ((style != null && style instanceof org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && reflectUpToClass === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringBuilder.reflectionToString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean(object, style, outputTransients);
                                }
                                else if (((object != null) || object === null) && ((style != null && style instanceof org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && outputTransients === undefined && reflectUpToClass === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringBuilder.reflectionToString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle(object, style);
                                }
                                else if (((object != null) || object === null) && style === undefined && outputTransients === undefined && reflectUpToClass === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.ToStringBuilder.reflectionToString$java_lang_Object(object);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            append$boolean(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$boolean(this.buffer, null, value);
                                return this;
                            }
                            append$boolean_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$byte(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$byte(this.buffer, null, value);
                                return this;
                            }
                            append$byte_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$char(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$char(this.buffer, null, value);
                                return this;
                            }
                            append$char_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$double(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$double(this.buffer, null, value);
                                return this;
                            }
                            append$double_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$float(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$float(this.buffer, null, value);
                                return this;
                            }
                            append$float_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$int(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$int(this.buffer, null, value);
                                return this;
                            }
                            append$int_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$long(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$long(this.buffer, null, value);
                                return this;
                            }
                            append$long_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$java_lang_Object(obj) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(this.buffer, null, obj, null);
                                return this;
                            }
                            append$java_lang_Object_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$short(value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$short(this.buffer, null, value);
                                return this;
                            }
                            append$short_A(array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(this.buffer, null, array, null);
                                return this;
                            }
                            append$java_lang_String$boolean(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$boolean(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$boolean_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$boolean_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            /**
                             * <p>Append to the <code>toString</code> a <code>boolean</code>
                             * array.</p>
                             *
                             * <p>A boolean parameter controls the level of detail to show.
                             * Setting <code>true</code> will output the array in full. Setting
                             * <code>false</code> will output a summary, typically the size of
                             * the array.</p>
                             *
                             * @param {string} fieldName  the field name
                             * @param {boolean[]} array  the array to add to the <code>toString</code>
                             * @param {boolean} fullDetail  <code>true</code> for detail, <code>false</code>
                             * for summary info
                             * @return {org.openprovenance.apache.commons.lang.builder.ToStringBuilder} this
                             */
                            append(fieldName, array, fullDetail) {
                                if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$boolean_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$byte_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$char_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$double_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$float_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$int_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$long_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$java_lang_Object_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$short_A$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                                    return this.append$java_lang_String$java_lang_Object$boolean(fieldName, array, fullDetail);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$boolean_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$byte_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$char_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$double_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$float_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$int_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$long_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$java_lang_Object_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$short_A(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'boolean') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$boolean(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$byte(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'string') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$char(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$short(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$int(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$long(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$float(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$double(fieldName, array);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && ((array != null) || array === null) && fullDetail === undefined) {
                                    return this.append$java_lang_String$java_lang_Object(fieldName, array);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'boolean'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$boolean_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'number'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$byte_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'string'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$char_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'number'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$double_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'number'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$float_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'number'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$int_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'number'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$long_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (fieldName[0] != null))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$java_lang_Object_A(fieldName);
                                }
                                else if (((fieldName != null && fieldName instanceof Array && (fieldName.length == 0 || fieldName[0] == null || (typeof fieldName[0] === 'number'))) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$short_A(fieldName);
                                }
                                else if (((typeof fieldName === 'boolean') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$boolean(fieldName);
                                }
                                else if (((typeof fieldName === 'number') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$byte(fieldName);
                                }
                                else if (((typeof fieldName === 'string') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$char(fieldName);
                                }
                                else if (((typeof fieldName === 'number') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$short(fieldName);
                                }
                                else if (((typeof fieldName === 'number') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$int(fieldName);
                                }
                                else if (((typeof fieldName === 'number') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$long(fieldName);
                                }
                                else if (((typeof fieldName === 'number') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$float(fieldName);
                                }
                                else if (((typeof fieldName === 'number') || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$double(fieldName);
                                }
                                else if (((fieldName != null) || fieldName === null) && array === undefined && fullDetail === undefined) {
                                    return this.append$java_lang_Object(fieldName);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            append$java_lang_String$byte(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$byte(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$byte_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$byte_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$char(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$char(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$char_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$char_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$double(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$double(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$double_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$double_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$float(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$float(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$float_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$float_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$int(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$int(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$int_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$int_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$long(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$long(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$long_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$long_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$java_lang_Object(fieldName, obj) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(this.buffer, fieldName, obj, null);
                                return this;
                            }
                            append$java_lang_String$java_lang_Object$boolean(fieldName, obj, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(this.buffer, fieldName, obj, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$java_lang_Object_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$java_lang_Object_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            append$java_lang_String$short(fieldName, value) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$short(this.buffer, fieldName, value);
                                return this;
                            }
                            append$java_lang_String$short_A(fieldName, array) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(this.buffer, fieldName, array, null);
                                return this;
                            }
                            append$java_lang_String$short_A$boolean(fieldName, array, fullDetail) {
                                this.style.append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(this.buffer, fieldName, array, org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(fullDetail));
                                return this;
                            }
                            /**
                             * <p>Appends with the same format as the default <code>Object toString()
                             * </code> method. Appends the class name followed by
                             * {@link System#identityHashCode(Object)}.</p>
                             *
                             * @param {*} object  the <code>Object</code> whose class name and id to output
                             * @return {org.openprovenance.apache.commons.lang.builder.ToStringBuilder} this
                             * @since 2.0
                             */
                            appendAsObjectToString(object) {
                                org.openprovenance.apache.commons.lang.ObjectUtils.identityToString$java_lang_StringBuffer$java_lang_Object(this.getStringBuffer(), object);
                                return this;
                            }
                            /**
                             * <p>Append the <code>toString</code> from the superclass.</p>
                             *
                             * <p>This method assumes that the superclass uses the same <code>ToStringStyle</code>
                             * as this one.</p>
                             *
                             * <p>If <code>superToString</code> is <code>null</code>, no change is made.</p>
                             *
                             * @param {string} superToString  the result of <code>super.toString()</code>
                             * @return {org.openprovenance.apache.commons.lang.builder.ToStringBuilder} this
                             * @since 2.0
                             */
                            appendSuper(superToString) {
                                if (superToString != null) {
                                    this.style.appendSuper(this.buffer, superToString);
                                }
                                return this;
                            }
                            /**
                             * <p>Append the <code>toString</code> from another object.</p>
                             *
                             * <p>This method is useful where a class delegates most of the implementation of
                             * its properties to another class. You can then call <code>toString()</code> on
                             * the other class and pass the result into this method.</p>
                             *
                             * <pre>
                             * private AnotherObject delegate;
                             * private String fieldInThisClass;
                             *
                             * public String toString() {
                             * return new ToStringBuilder(this).
                             * appendToString(delegate.toString()).
                             * append(fieldInThisClass).
                             * toString();
                             * }</pre>
                             *
                             * <p>This method assumes that the other object uses the same <code>ToStringStyle</code>
                             * as this one.</p>
                             *
                             * <p>If the <code>toString</code> is <code>null</code>, no change is made.</p>
                             *
                             * @param {string} toString  the result of <code>toString()</code> on another object
                             * @return {org.openprovenance.apache.commons.lang.builder.ToStringBuilder} this
                             * @since 2.0
                             */
                            appendToString(toString) {
                                if (toString != null) {
                                    this.style.appendToString(this.buffer, toString);
                                }
                                return this;
                            }
                            /**
                             * <p>Returns the <code>Object</code> being output.</p>
                             *
                             * @return {*} The object being output.
                             * @since 2.0
                             */
                            getObject() {
                                return this.object;
                            }
                            /**
                             * <p>Gets the <code>StringBuffer</code> being populated.</p>
                             *
                             * @return {{ str: string, toString: Function }} the <code>StringBuffer</code> being populated
                             */
                            getStringBuffer() {
                                return this.buffer;
                            }
                            /**
                             * <p>Gets the <code>ToStringStyle</code> being used.</p>
                             *
                             * @return {org.openprovenance.apache.commons.lang.builder.ToStringStyle} the <code>ToStringStyle</code> being used
                             * @since 2.0
                             */
                            getStyle() {
                                return this.style;
                            }
                            /**
                             * <p>Returns the built <code>toString</code>.</p>
                             *
                             * <p>This method appends the end of data indicator, and can only be called once.
                             * Use {@link #getStringBuffer} to get the current string state.</p>
                             *
                             * <p>If the object is <code>null</code>, return the style's <code>nullText</code></p>
                             *
                             * @return {string} the String <code>toString</code>
                             */
                            toString() {
                                if (this.getObject() == null) {
                                    /* append */ (sb => { sb.str += this.getStyle().getNullText(); return sb; })(this.getStringBuffer());
                                }
                                else {
                                    this.style.appendEnd(this.getStringBuffer(), this.getObject());
                                }
                                return /* toString */ this.getStringBuffer().str;
                            }
                        }
                        builder.ToStringBuilder = ToStringBuilder;
                        ToStringBuilder["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringBuilder";
                    })(builder = lang.builder || (lang.builder = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
