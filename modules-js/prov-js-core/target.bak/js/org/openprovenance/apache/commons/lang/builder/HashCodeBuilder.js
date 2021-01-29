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
                         * <p>
                         * Two randomly chosen, non-zero, odd numbers must be passed in. Ideally these should be different for each class,
                         * however this is not vital.
                         * </p>
                         *
                         * <p>
                         * Prime numbers are preferred, especially for the multiplier.
                         * </p>
                         *
                         * @param {number} initialNonZeroOddNumber
                         * a non-zero, odd number used as the initial value
                         * @param {number} multiplierNonZeroOddNumber
                         * a non-zero, odd number used as the multiplier
                         * @throws IllegalArgumentException
                         * if the number is zero or even
                         * @class
                         * @author Apache Software Foundation
                         */
                        class HashCodeBuilder {
                            constructor(initialNonZeroOddNumber, multiplierNonZeroOddNumber) {
                                if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null)) {
                                    let __args = arguments;
                                    if (this.iConstant === undefined) {
                                        this.iConstant = 0;
                                    }
                                    this.iTotal = 0;
                                    if (initialNonZeroOddNumber === 0) {
                                        throw Object.defineProperty(new Error("HashCodeBuilder requires a non zero initial value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (initialNonZeroOddNumber % 2 === 0) {
                                        throw Object.defineProperty(new Error("HashCodeBuilder requires an odd initial value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (multiplierNonZeroOddNumber === 0) {
                                        throw Object.defineProperty(new Error("HashCodeBuilder requires a non zero multiplier"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (multiplierNonZeroOddNumber % 2 === 0) {
                                        throw Object.defineProperty(new Error("HashCodeBuilder requires an odd multiplier"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    this.iConstant = multiplierNonZeroOddNumber;
                                    this.iTotal = initialNonZeroOddNumber;
                                }
                                else if (initialNonZeroOddNumber === undefined && multiplierNonZeroOddNumber === undefined) {
                                    let __args = arguments;
                                    if (this.iConstant === undefined) {
                                        this.iConstant = 0;
                                    }
                                    this.iTotal = 0;
                                    this.iConstant = 37;
                                    this.iTotal = 17;
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static REGISTRY_$LI$() { if (HashCodeBuilder.REGISTRY == null) {
                                HashCodeBuilder.REGISTRY = (new java.lang.ThreadLocal());
                            } return HashCodeBuilder.REGISTRY; }
                            /**
                             * <p>
                             * Returns the registry of objects being traversed by the reflection methods in the current thread.
                             * </p>
                             *
                             * @return {Array} Set the registry of objects being traversed
                             * @since 2.3
                             */
                            static getRegistry() {
                                return ((tlObj) => { if (tlObj.___value) {
                                    return tlObj.___value;
                                }
                                else {
                                    return tlObj.___value = tlObj.initialValue();
                                } })(HashCodeBuilder.REGISTRY_$LI$());
                            }
                            /**
                             * <p>
                             * Returns <code>true</code> if the registry contains the given object. Used by the reflection methods to avoid
                             * infinite loops.
                             * </p>
                             *
                             * @param {*} value
                             * The object to lookup in the registry.
                             * @return {boolean} boolean <code>true</code> if the registry contains the given object.
                             * @since 2.3
                             */
                            static isRegistered(value) {
                                const registry = HashCodeBuilder.getRegistry();
                                return registry != null && /* contains */ (registry.indexOf((new org.openprovenance.apache.commons.lang.builder.IDKey(value))) >= 0);
                            }
                            /**
                             * <p>
                             * Appends the fields and values defined by the given object of the given <code>Class</code>.
                             * </p>
                             *
                             * @param {*} object
                             * the object to append details of
                             * @param {*} clazz
                             * the class to append details of
                             * @param {org.openprovenance.apache.commons.lang.builder.HashCodeBuilder} builder
                             * the builder to append to
                             * @param {boolean} useTransients
                             * whether to use transient fields
                             * @param {java.lang.String[]} excludeFields
                             * Collection of String field names to exclude from use in calculation of hash code
                             * @private
                             */
                            /*private*/ static reflectionAppend(object, clazz, builder, useTransients, excludeFields) {
                                if (HashCodeBuilder.isRegistered(object)) {
                                    return;
                                }
                                try {
                                    HashCodeBuilder.register(object);
                                    const fields = clazz.getDeclaredFields();
                                    java.lang.reflect.AccessibleObject.setAccessible(fields, true);
                                    for (let i = 0; i < fields.length; i++) {
                                        {
                                            const field = fields[i];
                                            if (!org.openprovenance.apache.commons.lang.ArrayUtils.contains$java_lang_Object_A$java_lang_Object(excludeFields, /* getName */ field.name) && ( /* getName */field.name.indexOf('$') === -1) && (useTransients || !java.lang.reflect.Modifier.isTransient(field.getModifiers())) && (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))) {
                                                try {
                                                    const fieldValue = object[field.name];
                                                    builder.append$java_lang_Object(fieldValue);
                                                }
                                                catch (e) {
                                                    throw Object.defineProperty(new Error("Unexpected IllegalAccessException"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.VirtualMachineError', 'java.lang.InternalError', 'java.lang.Error', 'java.lang.Object'] });
                                                }
                                            }
                                        }
                                        ;
                                    }
                                }
                                finally {
                                    HashCodeBuilder.unregister(object);
                                }
                            }
                            static reflectionHashCode$int$int$java_lang_Object(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object) {
                                return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null, null);
                            }
                            static reflectionHashCode$int$int$java_lang_Object$boolean(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients) {
                                return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null, null);
                            }
                            static reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass) {
                                return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, null);
                            }
                            static reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, excludeFields) {
                                if (object == null) {
                                    throw Object.defineProperty(new Error("The object to build a hash code for must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                const builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
                                let clazz = object.constructor;
                                HashCodeBuilder.reflectionAppend(object, clazz, builder, testTransients, excludeFields);
                                while ((clazz.getSuperclass() != null && clazz !== reflectUpToClass)) {
                                    {
                                        clazz = clazz.getSuperclass();
                                        HashCodeBuilder.reflectionAppend(object, clazz, builder, testTransients, excludeFields);
                                    }
                                }
                                ;
                                return builder.toHashCode();
                            }
                            /**
                             * <p>
                             * This method uses reflection to build a valid hash code.
                             * </p>
                             *
                             * <p>
                             * It uses <code>AccessibleObject.setAccessible</code> to gain access to private fields. This means that it will
                             * throw a security exception if run under a security manager, if the permissions are not set up correctly. It is
                             * also not as efficient as testing explicitly.
                             * </p>
                             *
                             * <p>
                             * If the TestTransients parameter is set to <code>true</code>, transient members will be tested, otherwise they
                             * are ignored, as they are likely derived fields, and not part of the value of the <code>Object</code>.
                             * </p>
                             *
                             * <p>
                             * Static fields will not be included. Superclass fields will be included up to and including the specified
                             * superclass. A null superclass is treated as java.lang.Object.
                             * </p>
                             *
                             * <p>
                             * Two randomly chosen, non-zero, odd numbers must be passed in. Ideally these should be different for each class,
                             * however this is not vital. Prime numbers are preferred, especially for the multiplier.
                             * </p>
                             *
                             * @param {number} initialNonZeroOddNumber
                             * a non-zero, odd number used as the initial value
                             * @param {number} multiplierNonZeroOddNumber
                             * a non-zero, odd number used as the multiplier
                             * @param {*} object
                             * the Object to create a <code>hashCode</code> for
                             * @param {boolean} testTransients
                             * whether to include transient fields
                             * @param {*} reflectUpToClass
                             * the superclass to reflect up to (inclusive), may be <code>null</code>
                             * @param {java.lang.String[]} excludeFields
                             * array of field names to exclude from use in calculation of hash code
                             * @return {number} int hash code
                             * @throws IllegalArgumentException
                             * if the Object is <code>null</code>
                             * @throws IllegalArgumentException
                             * if the number is zero or even
                             * @since 2.0
                             */
                            static reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, excludeFields) {
                                if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(reflectUpToClass))) || reflectUpToClass === null) && ((excludeFields != null && excludeFields instanceof Array && (excludeFields.length == 0 || excludeFields[0] == null || (typeof excludeFields[0] === 'string'))) || excludeFields === null)) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, excludeFields);
                                }
                                else if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(reflectUpToClass))) || reflectUpToClass === null) && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass);
                                }
                                else if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && ((typeof testTransients === 'boolean') || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients);
                                }
                                else if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object);
                                }
                                else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && ((multiplierNonZeroOddNumber != null && (multiplierNonZeroOddNumber instanceof Array)) || multiplierNonZeroOddNumber === null) && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object$java_util_Collection(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
                                }
                                else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && ((multiplierNonZeroOddNumber != null && multiplierNonZeroOddNumber instanceof Array && (multiplierNonZeroOddNumber.length == 0 || multiplierNonZeroOddNumber[0] == null || (typeof multiplierNonZeroOddNumber[0] === 'string'))) || multiplierNonZeroOddNumber === null) && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
                                }
                                else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'boolean') || multiplierNonZeroOddNumber === null) && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object$boolean(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
                                }
                                else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && multiplierNonZeroOddNumber === undefined && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                                    return org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object(initialNonZeroOddNumber);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static reflectionHashCode$java_lang_Object(object) {
                                return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(17, 37, object, false, null, null);
                            }
                            static reflectionHashCode$java_lang_Object$boolean(object, testTransients) {
                                return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(17, 37, object, testTransients, null, null);
                            }
                            static reflectionHashCode$java_lang_Object$java_util_Collection(object, excludeFields) {
                                return HashCodeBuilder.reflectionHashCode$java_lang_Object$java_lang_String_A(object, org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toNoNullStringArray$java_util_Collection(excludeFields));
                            }
                            static reflectionHashCode$java_lang_Object$java_lang_String_A(object, excludeFields) {
                                return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(17, 37, object, false, null, excludeFields);
                            }
                            /**
                             * <p>
                             * Registers the given object. Used by the reflection methods to avoid infinite loops.
                             * </p>
                             *
                             * @param {*} value
                             * The object to register.
                             */
                            static register(value) {
                                {
                                    if (HashCodeBuilder.getRegistry() == null) {
                                        HashCodeBuilder.REGISTRY_$LI$().set(([]));
                                    }
                                }
                                ;
                                /* add */ ((s, e) => { if (s.indexOf(e) == -1) {
                                    s.push(e);
                                    return true;
                                }
                                else {
                                    return false;
                                } })(HashCodeBuilder.getRegistry(), new org.openprovenance.apache.commons.lang.builder.IDKey(value));
                            }
                            /**
                             * <p>
                             * Unregisters the given object.
                             * </p>
                             *
                             * <p>
                             * Used by the reflection methods to avoid infinite loops.
                             *
                             * @param {*} value
                             * The object to unregister.
                             * @since 2.3
                             */
                            static unregister(value) {
                                let registry = HashCodeBuilder.getRegistry();
                                if (registry != null) {
                                    /* remove */ (a => { let index = a.indexOf(new org.openprovenance.apache.commons.lang.builder.IDKey(value)); if (index >= 0) {
                                        a.splice(index, 1);
                                        return true;
                                    }
                                    else {
                                        return false;
                                    } })(registry);
                                    {
                                        registry = HashCodeBuilder.getRegistry();
                                        if (registry != null && /* isEmpty */ (registry.length == 0)) {
                                            HashCodeBuilder.REGISTRY_$LI$().set(null);
                                        }
                                    }
                                    ;
                                }
                            }
                            append$boolean(value) {
                                this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
                                return this;
                            }
                            append$boolean_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$boolean(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            /**
                             * <p>
                             * Append a <code>hashCode</code> for a <code>boolean</code> array.
                             * </p>
                             *
                             * @param {boolean[]} array
                             * the array to add to the <code>hashCode</code>
                             * @return {org.openprovenance.apache.commons.lang.builder.HashCodeBuilder} this
                             */
                            append(array) {
                                if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                    return this.append$boolean_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.append$byte_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'string'))) || array === null)) {
                                    return this.append$char_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.append$double_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.append$float_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.append$int_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.append$long_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null)) {
                                    return this.append$java_lang_Object_A(array);
                                }
                                else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'number'))) || array === null)) {
                                    return this.append$short_A(array);
                                }
                                else if (((typeof array === 'boolean') || array === null)) {
                                    return this.append$boolean(array);
                                }
                                else if (((typeof array === 'number') || array === null)) {
                                    return this.append$byte(array);
                                }
                                else if (((typeof array === 'string') || array === null)) {
                                    return this.append$char(array);
                                }
                                else if (((typeof array === 'number') || array === null)) {
                                    return this.append$short(array);
                                }
                                else if (((typeof array === 'number') || array === null)) {
                                    return this.append$int(array);
                                }
                                else if (((typeof array === 'number') || array === null)) {
                                    return this.append$long(array);
                                }
                                else if (((typeof array === 'number') || array === null)) {
                                    return this.append$float(array);
                                }
                                else if (((typeof array === 'number') || array === null)) {
                                    return this.append$double(array);
                                }
                                else if (((array != null) || array === null)) {
                                    return this.append$java_lang_Object(array);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            append$byte(value) {
                                this.iTotal = this.iTotal * this.iConstant + value;
                                return this;
                            }
                            append$byte_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$byte(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$char(value) {
                                this.iTotal = this.iTotal * this.iConstant + (c => c.charCodeAt == null ? c : c.charCodeAt(0))(value);
                                return this;
                            }
                            append$char_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$char(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$double(value) {
                                return this.append$long(/* doubleToLongBits */ ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(Math.fround(value)));
                            }
                            append$double_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$double(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$float(value) {
                                this.iTotal = this.iTotal * this.iConstant + /* floatToIntBits */ ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(value);
                                return this;
                            }
                            append$float_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$float(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$int(value) {
                                this.iTotal = this.iTotal * this.iConstant + value;
                                return this;
                            }
                            append$int_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$int(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$long(value) {
                                this.iTotal = this.iTotal * this.iConstant + (((value ^ (value >> 32)) | 0));
                                return this;
                            }
                            append$long_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$long(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$java_lang_Object(object) {
                                if (object == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    if (object.constructor.isArray()) {
                                        if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'number')) {
                                            this.append$long_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'number')) {
                                            this.append$int_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'number')) {
                                            this.append$short_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'string')) {
                                            this.append$char_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'number')) {
                                            this.append$byte_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'number')) {
                                            this.append$double_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'number')) {
                                            this.append$float_A(object);
                                        }
                                        else if (object != null && object instanceof Array && (object.length == 0 || object[0] == null || typeof object[0] === 'boolean')) {
                                            this.append$boolean_A(object);
                                        }
                                        else {
                                            this.append$java_lang_Object_A(object);
                                        }
                                    }
                                    else {
                                        this.iTotal = this.iTotal * this.iConstant + /* hashCode */ ((o) => { if (o.hashCode) {
                                            return o.hashCode();
                                        }
                                        else {
                                            return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0)) | 0, 0);
                                        } })(object);
                                    }
                                }
                                return this;
                            }
                            append$java_lang_Object_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$java_lang_Object(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            append$short(value) {
                                this.iTotal = this.iTotal * this.iConstant + value;
                                return this;
                            }
                            append$short_A(array) {
                                if (array == null) {
                                    this.iTotal = this.iTotal * this.iConstant;
                                }
                                else {
                                    for (let i = 0; i < array.length; i++) {
                                        {
                                            this.append$short(array[i]);
                                        }
                                        ;
                                    }
                                }
                                return this;
                            }
                            /**
                             * <p>
                             * Adds the result of super.hashCode() to this builder.
                             * </p>
                             *
                             * @param {number} superHashCode
                             * the result of calling <code>super.hashCode()</code>
                             * @return {org.openprovenance.apache.commons.lang.builder.HashCodeBuilder} this HashCodeBuilder, used to chain calls.
                             * @since 2.0
                             */
                            appendSuper(superHashCode) {
                                this.iTotal = this.iTotal * this.iConstant + superHashCode;
                                return this;
                            }
                            /**
                             * <p>
                             * Return the computed <code>hashCode</code>.
                             * </p>
                             *
                             * @return {number} <code>hashCode</code> based on the fields appended
                             */
                            toHashCode() {
                                return this.iTotal;
                            }
                            /**
                             * <p>
                             * The computed <code>hashCode</code> from toHashCode() is returned due to the likelyhood
                             * of bugs in mis-calling toHashCode() and the unlikelyness of it mattering what the hashCode for
                             * HashCodeBuilder itself is.
                             *
                             * @return {number} <code>hashCode</code> based on the fields appended
                             * @since 2.5
                             */
                            hashCode() {
                                return this.toHashCode();
                            }
                        }
                        builder_1.HashCodeBuilder = HashCodeBuilder;
                        HashCodeBuilder["__class"] = "org.openprovenance.apache.commons.lang.builder.HashCodeBuilder";
                    })(builder = lang.builder || (lang.builder = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
