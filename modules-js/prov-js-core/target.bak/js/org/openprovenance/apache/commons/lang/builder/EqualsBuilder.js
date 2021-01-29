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
                    (function (builder_1) {
                        /**
                         * <p>Constructor for EqualsBuilder.</p>
                         *
                         * <p>Starts off assuming that equals is <code>true</code>.</p>
                         * @see Object#equals(Object)
                         * @class
                         * @author Apache Software Foundation
                         */
                        class EqualsBuilder {
                            constructor() {
                                this.__isEquals = true;
                            }
                            static reflectionEquals$java_lang_Object$java_lang_Object(lhs, rhs) {
                                return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, false, null, null);
                            }
                            static reflectionEquals$java_lang_Object$java_lang_Object$java_util_Collection(lhs, rhs, excludeFields) {
                                return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$java_lang_String_A(lhs, rhs, org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toNoNullStringArray$java_util_Collection(excludeFields));
                            }
                            static reflectionEquals$java_lang_Object$java_lang_Object$java_lang_String_A(lhs, rhs, excludeFields) {
                                return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, false, null, excludeFields);
                            }
                            static reflectionEquals$java_lang_Object$java_lang_Object$boolean(lhs, rhs, testTransients) {
                                return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, null, null);
                            }
                            static reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class(lhs, rhs, testTransients, reflectUpToClass) {
                                return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, reflectUpToClass, null);
                            }
                            static reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, reflectUpToClass, excludeFields) {
                                if (lhs === rhs) {
                                    return true;
                                }
                                if (lhs == null || rhs == null) {
                                    return false;
                                }
                                const lhsClass = lhs.constructor;
                                const rhsClass = rhs.constructor;
                                let testClass;
                                if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                    return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                else if (typeof c === 'function')
                                    return (o instanceof c) || (o.constructor && o.constructor === c); })(lhsClass, rhs)) {
                                    testClass = lhsClass;
                                    if (!((c, o) => { if (typeof c === 'string')
                                        return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                    else if (typeof c === 'function')
                                        return (o instanceof c) || (o.constructor && o.constructor === c); })(rhsClass, lhs)) {
                                        testClass = rhsClass;
                                    }
                                }
                                else if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                    return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                else if (typeof c === 'function')
                                    return (o instanceof c) || (o.constructor && o.constructor === c); })(rhsClass, lhs)) {
                                    testClass = rhsClass;
                                    if (!((c, o) => { if (typeof c === 'string')
                                        return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                    else if (typeof c === 'function')
                                        return (o instanceof c) || (o.constructor && o.constructor === c); })(lhsClass, rhs)) {
                                        testClass = lhsClass;
                                    }
                                }
                                else {
                                    return false;
                                }
                                const equalsBuilder = new EqualsBuilder();
                                try {
                                    EqualsBuilder.reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                                    while ((testClass.getSuperclass() != null && testClass !== reflectUpToClass)) {
                                        {
                                            testClass = testClass.getSuperclass();
                                            EqualsBuilder.reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                                        }
                                    }
                                    ;
                                }
                                catch (e) {
                                    return false;
                                }
                                return equalsBuilder.isEquals();
                            }
                            /**
                             * <p>This method uses reflection to determine if the two <code>Object</code>s
                             * are equal.</p>
                             *
                             * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
                             * fields. This means that it will throw a security exception if run under
                             * a security manager, if the permissions are not set up correctly. It is also
                             * not as efficient as testing explicitly.</p>
                             *
                             * <p>If the testTransients parameter is set to <code>true</code>, transient
                             * members will be tested, otherwise they are ignored, as they are likely
                             * derived fields, and not part of the value of the <code>Object</code>.</p>
                             *
                             * <p>Static fields will not be included. Superclass fields will be appended
                             * up to and including the specified superclass. A null superclass is treated
                             * as java.lang.Object.</p>
                             *
                             * @param {*} lhs  <code>this</code> object
                             * @param {*} rhs  the other object
                             * @param {boolean} testTransients  whether to include transient fields
                             * @param {*} reflectUpToClass  the superclass to reflect up to (inclusive),
                             * may be <code>null</code>
                             * @param {java.lang.String[]} excludeFields  array of field names to exclude from testing
                             * @return {boolean} <code>true</code> if the two Objects have tested equals.
                             * @since 2.0
                             */
                            static reflectionEquals(lhs, rhs, testTransients, reflectUpToClass, excludeFields) {
                                if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(reflectUpToClass))) || reflectUpToClass === null) && ((excludeFields != null && excludeFields instanceof Array && (excludeFields.length == 0 || excludeFields[0] == null || (typeof excludeFields[0] === 'string'))) || excludeFields === null)) {
                                    return org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, reflectUpToClass, excludeFields);
                                }
                                else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(reflectUpToClass))) || reflectUpToClass === null) && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class(lhs, rhs, testTransients, reflectUpToClass);
                                }
                                else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((testTransients != null && (testTransients instanceof Array)) || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$java_util_Collection(lhs, rhs, testTransients);
                                }
                                else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((testTransients != null && testTransients instanceof Array && (testTransients.length == 0 || testTransients[0] == null || (typeof testTransients[0] === 'string'))) || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$java_lang_String_A(lhs, rhs, testTransients);
                                }
                                else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((typeof testTransients === 'boolean') || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean(lhs, rhs, testTransients);
                                }
                                else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object(lhs, rhs);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            /**
                             * <p>Appends the fields and values defined by the given object of the
                             * given Class.</p>
                             *
                             * @param {*} lhs  the left hand object
                             * @param {*} rhs  the right hand object
                             * @param {*} clazz  the class to append details of
                             * @param {org.openprovenance.apache.commons.lang.builder.EqualsBuilder} builder  the builder to append to
                             * @param {boolean} useTransients  whether to test transient fields
                             * @param {java.lang.String[]} excludeFields  array of field names to exclude from testing
                             * @private
                             */
                            /*private*/ static reflectionAppend(lhs, rhs, clazz, builder, useTransients, excludeFields) {
                                const fields = clazz.getDeclaredFields();
                                java.lang.reflect.AccessibleObject.setAccessible(fields, true);
                                for (let i = 0; i < fields.length && builder.__isEquals; i++) {
                                    {
                                        const f = fields[i];
                                        if (!org.openprovenance.apache.commons.lang.ArrayUtils.contains$java_lang_Object_A$java_lang_Object(excludeFields, /* getName */ f.name) && ( /* getName */f.name.indexOf('$') === -1) && (useTransients || !java.lang.reflect.Modifier.isTransient(f.getModifiers())) && (!java.lang.reflect.Modifier.isStatic(f.getModifiers()))) {
                                            try {
                                                builder.append$java_lang_Object$java_lang_Object(/* get */ lhs[f.name], /* get */ rhs[f.name]);
                                            }
                                            catch (e) {
                                                throw Object.defineProperty(new Error("Unexpected IllegalAccessException"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.VirtualMachineError', 'java.lang.InternalError', 'java.lang.Error', 'java.lang.Object'] });
                                            }
                                        }
                                    }
                                    ;
                                }
                            }
                            /**
                             * <p>Adds the result of <code>super.equals()</code> to this builder.</p>
                             *
                             * @param {boolean} superEquals  the result of calling <code>super.equals()</code>
                             * @return {org.openprovenance.apache.commons.lang.builder.EqualsBuilder} EqualsBuilder - used to chain calls.
                             * @since 2.0
                             */
                            appendSuper(superEquals) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = superEquals;
                                return this;
                            }
                            append$java_lang_Object$java_lang_Object(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                const lhsClass = lhs.constructor;
                             //   console.log(lhsClass)
                               // if (!lhsClass.isArray()) {
                                if (!lhs instanceof Array) {  //Luc
                                    this.__isEquals = /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(lhs, rhs);
                                }
                                else if (lhs.constructor !== rhs.constructor) {
                                    this.setEquals(false);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'number')) {
                                    this.append$long_A$long_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'number')) {
                                    this.append$int_A$int_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'number')) {
                                    this.append$short_A$short_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'string')) {
                                    this.append$char_A$char_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'number')) {
                                    this.append$byte_A$byte_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'number')) {
                                    this.append$double_A$double_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'number')) {
                                    this.append$float_A$float_A(lhs, rhs);
                                }
                                else if (lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || typeof lhs[0] === 'boolean')) {
                                    this.append$boolean_A$boolean_A(lhs, rhs);
                                }
                                else {
                                    this.append$java_lang_Object_A$java_lang_Object_A(lhs, rhs);
                                }
                                return this;
                            }
                            append$long$long(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = (lhs === rhs);
                                return this;
                            }
                            append$int$int(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = (lhs === rhs);
                                return this;
                            }
                            append$short$short(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = (lhs === rhs);
                                return this;
                            }
                            append$char$char(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(lhs) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(rhs));
                                return this;
                            }
                            append$byte$byte(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = (lhs === rhs);
                                return this;
                            }
                            append$double$double(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                return this.append$long$long(/* doubleToLongBits */ ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(Math.fround(lhs)), /* doubleToLongBits */ ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(Math.fround(rhs)));
                            }
                            append$float$float(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                return this.append$int$int(/* floatToIntBits */ ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(lhs), /* floatToIntBits */ ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(rhs));
                            }
                            append$boolean$boolean(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                this.__isEquals = (lhs === rhs);
                                return this;
                            }
                            append$java_lang_Object_A$java_lang_Object_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$java_lang_Object$java_lang_Object(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            /**
                             * <p>Performs a deep comparison of two <code>Object</code> arrays.</p>
                             *
                             * <p>This also will be called for the top level of
                             * multi-dimensional, ragged, and multi-typed arrays.</p>
                             *
                             * @param {java.lang.Object[]} lhs  the left hand <code>Object[]</code>
                             * @param {java.lang.Object[]} rhs  the right hand <code>Object[]</code>
                             * @return {org.openprovenance.apache.commons.lang.builder.EqualsBuilder} EqualsBuilder - used to chain calls.
                             */
                            append(lhs, rhs) {
                                if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (lhs[0] != null))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (rhs[0] != null))) || rhs === null)) {
                                    return this.append$java_lang_Object_A$java_lang_Object_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'number'))) || rhs === null)) {
                                    return this.append$long_A$long_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'number'))) || rhs === null)) {
                                    return this.append$int_A$int_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'number'))) || rhs === null)) {
                                    return this.append$short_A$short_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'string'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'string'))) || rhs === null)) {
                                    return this.append$char_A$char_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'number'))) || rhs === null)) {
                                    return this.append$byte_A$byte_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'number'))) || rhs === null)) {
                                    return this.append$double_A$double_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'number'))) || rhs === null)) {
                                    return this.append$float_A$float_A(lhs, rhs);
                                }
                                else if (((lhs != null && lhs instanceof Array && (lhs.length == 0 || lhs[0] == null || (typeof lhs[0] === 'boolean'))) || lhs === null) && ((rhs != null && rhs instanceof Array && (rhs.length == 0 || rhs[0] == null || (typeof rhs[0] === 'boolean'))) || rhs === null)) {
                                    return this.append$boolean_A$boolean_A(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return this.append$short$short(lhs, rhs);
                                }
                                else if (((typeof lhs === 'string') || lhs === null) && ((typeof rhs === 'string') || rhs === null)) {
                                    return this.append$char$char(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return this.append$byte$byte(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return this.append$int$int(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return this.append$long$long(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return this.append$float$float(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return this.append$double$double(lhs, rhs);
                                }
                                else if (((typeof lhs === 'boolean') || lhs === null) && ((typeof rhs === 'boolean') || rhs === null)) {
                                    return this.append$boolean$boolean(lhs, rhs);
                                }
                                else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null)) {
                                    return this.append$java_lang_Object$java_lang_Object(lhs, rhs);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            append$long_A$long_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$long$long(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$int_A$int_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$int$int(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$short_A$short_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$short$short(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$char_A$char_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$char$char(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$byte_A$byte_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$byte$byte(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$double_A$double_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$double$double(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$float_A$float_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$float$float(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            append$boolean_A$boolean_A(lhs, rhs) {
                                if (this.__isEquals === false) {
                                    return this;
                                }
                                if (lhs === rhs) {
                                    return this;
                                }
                                if (lhs == null || rhs == null) {
                                    this.setEquals(false);
                                    return this;
                                }
                                if (lhs.length !== rhs.length) {
                                    this.setEquals(false);
                                    return this;
                                }
                                for (let i = 0; i < lhs.length && this.__isEquals; ++i) {
                                    {
                                        this.append$boolean$boolean(lhs[i], rhs[i]);
                                    }
                                    ;
                                }
                                return this;
                            }
                            /**
                             * <p>Returns <code>true</code> if the fields that have been checked
                             * are all equal.</p>
                             *
                             * @return {boolean} boolean
                             */
                            isEquals() {
                                return this.__isEquals;
                            }
                            /**
                             * Sets the <code>isEquals</code> value.
                             *
                             * @param {boolean} isEquals The value to set.
                             * @since 2.1
                             */
                            setEquals(isEquals) {
                                this.__isEquals = isEquals;
                            }
                            /**
                             * Reset the EqualsBuilder so you can use the same object again
                             * @since 2.5
                             */
                            reset() {
                                this.__isEquals = true;
                            }
                        }
                        builder_1.EqualsBuilder = EqualsBuilder;
                        EqualsBuilder["__class"] = "org.openprovenance.apache.commons.lang.builder.EqualsBuilder";
                    })(builder = lang.builder || (lang.builder = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
