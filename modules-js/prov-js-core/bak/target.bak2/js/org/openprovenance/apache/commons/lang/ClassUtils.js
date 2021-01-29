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
                     * <p>ClassUtils instances should NOT be constructed in standard programming.
                     * Instead, the class should be used as
                     * <code>ClassUtils.getShortClassName(cls)</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean
                     * instance to operate.</p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class ClassUtils {
                        constructor() {
                        }
                        static __static_initialize() { if (!ClassUtils.__static_initialized) {
                            ClassUtils.__static_initialized = true;
                            ClassUtils.__static_initializer_0();
                            ClassUtils.__static_initializer_1();
                            ClassUtils.__static_initializer_2();
                        } }
                        static PACKAGE_SEPARATOR_$LI$() { ClassUtils.__static_initialize(); if (ClassUtils.PACKAGE_SEPARATOR == null) {
                            ClassUtils.PACKAGE_SEPARATOR = /* valueOf */ String(ClassUtils.PACKAGE_SEPARATOR_CHAR).toString();
                        } return ClassUtils.PACKAGE_SEPARATOR; }
                        static INNER_CLASS_SEPARATOR_$LI$() { ClassUtils.__static_initialize(); if (ClassUtils.INNER_CLASS_SEPARATOR == null) {
                            ClassUtils.INNER_CLASS_SEPARATOR = /* valueOf */ String(ClassUtils.INNER_CLASS_SEPARATOR_CHAR).toString();
                        } return ClassUtils.INNER_CLASS_SEPARATOR; }
                        static primitiveWrapperMap_$LI$() { ClassUtils.__static_initialize(); if (ClassUtils.primitiveWrapperMap == null) {
                            ClassUtils.primitiveWrapperMap = ({});
                        } return ClassUtils.primitiveWrapperMap; }
                        static __static_initializer_0() {
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.BooleanHelper.TYPE, Boolean);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.ByteHelper.TYPE, Number);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.CharacterHelper.TYPE, String);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.ShortHelper.TYPE, Number);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.IntegerHelper.TYPE, Number);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.LongHelper.TYPE, Number);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.DoubleHelper.TYPE, Number);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.FloatHelper.TYPE, Number);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.primitiveWrapperMap_$LI$(), void .TYPE, void .TYPE);
                        }
                        static wrapperPrimitiveMap_$LI$() { ClassUtils.__static_initialize(); if (ClassUtils.wrapperPrimitiveMap == null) {
                            ClassUtils.wrapperPrimitiveMap = ({});
                        } return ClassUtils.wrapperPrimitiveMap; }
                        static __static_initializer_1() {
                            for (const it = ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(/* keySet */ ((m) => { let r = []; if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                r.push(m.entries[i].key); return r; })(ClassUtils.primitiveWrapperMap_$LI$())); it.hasNext();) {
                                {
                                    const primitiveClass = it.next();
                                    const wrapperClass = ((m, k) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            return m.entries[i].value;
                                        } return null; })(ClassUtils.primitiveWrapperMap_$LI$(), primitiveClass);
                                    if (!((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(primitiveClass, wrapperClass)) {
                                        /* put */ ((m, k, v) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                m.entries[i].value = v;
                                                return;
                                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.wrapperPrimitiveMap_$LI$(), wrapperClass, primitiveClass);
                                    }
                                }
                                ;
                            }
                        }
                        static abbreviationMap_$LI$() { ClassUtils.__static_initialize(); if (ClassUtils.abbreviationMap == null) {
                            ClassUtils.abbreviationMap = ({});
                        } return ClassUtils.abbreviationMap; }
                        static reverseAbbreviationMap_$LI$() { ClassUtils.__static_initialize(); if (ClassUtils.reverseAbbreviationMap == null) {
                            ClassUtils.reverseAbbreviationMap = ({});
                        } return ClassUtils.reverseAbbreviationMap; }
                        /**
                         * Add primitive type abbreviation to maps of abbreviations.
                         *
                         * @param {string} primitive Canonical name of primitive type
                         * @param {string} abbreviation Corresponding abbreviation of primitive type
                         * @private
                         */
                        /*private*/ static addAbbreviation(primitive, abbreviation) {
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.abbreviationMap_$LI$(), primitive, abbreviation);
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(ClassUtils.reverseAbbreviationMap_$LI$(), abbreviation, primitive);
                        }
                        static __static_initializer_2() {
                            ClassUtils.addAbbreviation("int", "I");
                            ClassUtils.addAbbreviation("boolean", "Z");
                            ClassUtils.addAbbreviation("float", "F");
                            ClassUtils.addAbbreviation("long", "J");
                            ClassUtils.addAbbreviation("short", "S");
                            ClassUtils.addAbbreviation("byte", "B");
                            ClassUtils.addAbbreviation("double", "D");
                            ClassUtils.addAbbreviation("char", "C");
                        }
                        static getShortClassName$java_lang_Object$java_lang_String(object, valueIfNull) {
                            if (object == null) {
                                return valueIfNull;
                            }
                            return ClassUtils.getShortClassName$java_lang_Class(object.constructor);
                        }
                        /**
                         * <p>Gets the class name minus the package name for an <code>Object</code>.</p>
                         *
                         * @param {*} object  the class to get the short name for, may be null
                         * @param {string} valueIfNull  the value to return if null
                         * @return {string} the class name of the object without the package name, or the null value
                         */
                        static getShortClassName(object, valueIfNull) {
                            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Object$java_lang_String(object, valueIfNull);
                            }
                            else if (((object != null && (object["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(object))) || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Class(object);
                            }
                            else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_String(object);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static getShortClassName$java_lang_Class(cls) {
                            if (cls == null) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            return ClassUtils.getShortClassName$java_lang_String(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
                        }
                        static getShortClassName$java_lang_String(className) {
                            if (className == null) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            if (className.length === 0) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            const arrayPrefix = new org.openprovenance.apache.commons.lang.text.StrBuilder();
                            if ( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(className, "[")) {
                                while (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(className.charAt(0)) == '['.charCodeAt(0))) {
                                    {
                                        className = className.substring(1);
                                        arrayPrefix.append$java_lang_String("[]");
                                    }
                                }
                                ;
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(className.charAt(0)) == 'L'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(className.charAt(className.length - 1)) == ';'.charCodeAt(0)) {
                                    className = className.substring(1, className.length - 1);
                                }
                            }
                            if ( /* containsKey */((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return true;
                                } return false; })(ClassUtils.reverseAbbreviationMap_$LI$(), className)) {
                                className = ((m, k) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        return m.entries[i].value;
                                    } return null; })(ClassUtils.reverseAbbreviationMap_$LI$(), className);
                            }
                            const lastDotIdx = className.lastIndexOf(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                            const innerIdx = className.indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR, lastDotIdx === -1 ? 0 : lastDotIdx + 1);
                            let out = className.substring(lastDotIdx + 1);
                            if (innerIdx !== -1) {
                                out = /* replace */ out.split(ClassUtils.INNER_CLASS_SEPARATOR_CHAR).join(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                            }
                            return out + arrayPrefix;
                        }
                        static getPackageName$java_lang_Object$java_lang_String(object, valueIfNull) {
                            if (object == null) {
                                return valueIfNull;
                            }
                            return ClassUtils.getPackageName$java_lang_Class(object.constructor);
                        }
                        /**
                         * <p>Gets the package name of an <code>Object</code>.</p>
                         *
                         * @param {*} object  the class to get the package name for, may be null
                         * @param {string} valueIfNull  the value to return if null
                         * @return {string} the package name of the object, or the null value
                         */
                        static getPackageName(object, valueIfNull) {
                            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getPackageName$java_lang_Object$java_lang_String(object, valueIfNull);
                            }
                            else if (((object != null && (object["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(object))) || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getPackageName$java_lang_Class(object);
                            }
                            else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getPackageName$java_lang_String(object);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static getPackageName$java_lang_Class(cls) {
                            if (cls == null) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            return ClassUtils.getPackageName$java_lang_String(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
                        }
                        static getPackageName$java_lang_String(className) {
                            if (className == null || className.length === 0) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            while (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(className.charAt(0)) == '['.charCodeAt(0))) {
                                {
                                    className = className.substring(1);
                                }
                            }
                            ;
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(className.charAt(0)) == 'L'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(className.charAt(className.length - 1)) == ';'.charCodeAt(0)) {
                                className = className.substring(1);
                            }
                            const i = className.lastIndexOf(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                            if (i === -1) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            return className.substring(0, i);
                        }
                        /**
                         * <p>Gets a <code>List</code> of superclasses for the given class.</p>
                         *
                         * @param {*} cls  the class to look up, may be <code>null</code>
                         * @return {Array} the <code>List</code> of superclasses in order going up from this one
                         * <code>null</code> if null input
                         */
                        static getAllSuperclasses(cls) {
                            if (cls == null) {
                                return null;
                            }
                            const classes = ([]);
                            let superclass = cls.getSuperclass();
                            while ((superclass != null)) {
                                {
                                    /* add */ (classes.push(superclass) > 0);
                                    superclass = superclass.getSuperclass();
                                }
                            }
                            ;
                            return classes;
                        }
                        static getAllInterfaces$java_lang_Class(cls) {
                            if (cls == null) {
                                return null;
                            }
                            const interfacesFound = ([]);
                            ClassUtils.getAllInterfaces$java_lang_Class$java_util_List(cls, interfacesFound);
                            return interfacesFound;
                        }
                        static getAllInterfaces$java_lang_Class$java_util_List(cls, interfacesFound) {
                            while ((cls != null)) {
                                {
                                    const interfaces = cls.getInterfaces();
                                    for (let i = 0; i < interfaces.length; i++) {
                                        {
                                            if (!(interfacesFound.indexOf((interfaces[i])) >= 0)) {
                                                /* add */ (interfacesFound.push(interfaces[i]) > 0);
                                                ClassUtils.getAllInterfaces$java_lang_Class$java_util_List(interfaces[i], interfacesFound);
                                            }
                                        }
                                        ;
                                    }
                                    cls = cls.getSuperclass();
                                }
                            }
                            ;
                        }
                        /**
                         * Get the interfaces for the specified class.
                         *
                         * @param {*} cls  the class to look up, may be <code>null</code>
                         * @param {Array} interfacesFound the <code>Set</code> of interfaces for the class
                         * @private
                         */
                        static getAllInterfaces(cls, interfacesFound) {
                            if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(cls))) || cls === null) && ((interfacesFound != null && (interfacesFound instanceof Array)) || interfacesFound === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getAllInterfaces$java_lang_Class$java_util_List(cls, interfacesFound);
                            }
                            else if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(cls))) || cls === null) && interfacesFound === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getAllInterfaces$java_lang_Class(cls);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Given a <code>List</code> of class names, this method converts them into classes.</p>
                         *
                         * <p>A new <code>List</code> is returned. If the class name cannot be found, <code>null</code>
                         * is stored in the <code>List</code>. If the class name in the <code>List</code> is
                         * <code>null</code>, <code>null</code> is stored in the output <code>List</code>.</p>
                         *
                         * @param {Array} classNames  the classNames to change
                         * @return {Array} a <code>List</code> of Class objects corresponding to the class names,
                         * <code>null</code> if null input
                         * @throws ClassCastException if classNames contains a non String entry
                         */
                        static convertClassNamesToClasses(classNames) {
                            if (classNames == null) {
                                return null;
                            }
                            const classes = ([]);
                            for (const it = ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(classNames); it.hasNext();) {
                                {
                                    const className = it.next();
                                    try {
                                        /* add */ (classes.push(/* forName */ eval(className)) > 0);
                                    }
                                    catch (ex) {
                                        /* add */ (classes.push(null) > 0);
                                    }
                                }
                                ;
                            }
                            return classes;
                        }
                        /**
                         * <p>Given a <code>List</code> of <code>Class</code> objects, this method converts
                         * them into class names.</p>
                         *
                         * <p>A new <code>List</code> is returned. <code>null</code> objects will be copied into
                         * the returned list as <code>null</code>.</p>
                         *
                         * @param {Array} classes  the classes to change
                         * @return {Array} a <code>List</code> of class names corresponding to the Class objects,
                         * <code>null</code> if null input
                         * @throws ClassCastException if <code>classes</code> contains a non-<code>Class</code> entry
                         */
                        static convertClassesToClassNames(classes) {
                            if (classes == null) {
                                return null;
                            }
                            const classNames = ([]);
                            for (const it = ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(classes); it.hasNext();) {
                                {
                                    const cls = it.next();
                                    if (cls == null) {
                                        /* add */ (classNames.push(null) > 0);
                                    }
                                    else {
                                        /* add */ (classNames.push(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls)) > 0);
                                    }
                                }
                                ;
                            }
                            return classNames;
                        }
                        static isAssignable$java_lang_Class_A$java_lang_Class_A(classArray, toClassArray) {
                            return ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(classArray, toClassArray, false);
                        }
                        static isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(classArray, toClassArray, autoboxing) {
                            if (org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$java_lang_Object_A$java_lang_Object_A(classArray, toClassArray) === false) {
                                return false;
                            }
                            if (classArray == null) {
                                classArray = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                            }
                            if (toClassArray == null) {
                                toClassArray = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                            }
                            for (let i = 0; i < classArray.length; i++) {
                                {
                                    if (ClassUtils.isAssignable$java_lang_Class$java_lang_Class$boolean(classArray[i], toClassArray[i], autoboxing) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if an array of Classes can be assigned to another array of Classes.</p>
                         *
                         * <p>This method calls {@link #isAssignable(Class, Class) isAssignable} for each
                         * Class pair in the input arrays. It can be used to check if a set of arguments
                         * (the first parameter) are suitably compatible with a set of method parameter types
                         * (the second parameter).</p>
                         *
                         * <p>Unlike the {@link Class#isAssignableFrom(Class)} method, this
                         * method takes into account widenings of primitive classes and
                         * <code>null</code>s.</p>
                         *
                         * <p>Primitive widenings allow an int to be assigned to a <code>long</code>,
                         * <code>float</code> or <code>double</code>. This method returns the correct
                         * result for these cases.</p>
                         *
                         * <p><code>Null</code> may be assigned to any reference type. This method will
                         * return <code>true</code> if <code>null</code> is passed in and the toClass is
                         * non-primitive.</p>
                         *
                         * <p>Specifically, this method tests whether the type represented by the
                         * specified <code>Class</code> parameter can be converted to the type
                         * represented by this <code>Class</code> object via an identity conversion
                         * widening primitive or widening reference conversion. See
                         * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
                         * sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
                         *
                         * @param {java.lang.Class[]} classArray  the array of Classes to check, may be <code>null</code>
                         * @param {java.lang.Class[]} toClassArray  the array of Classes to try to assign into, may be <code>null</code>
                         * @param {boolean} autoboxing  whether to use implicit autoboxing/unboxing between primitives and wrappers
                         * @return {boolean} <code>true</code> if assignment possible
                         * @since 2.5
                         */
                        static isAssignable(classArray, toClassArray, autoboxing) {
                            if (((classArray != null && classArray instanceof Array && (classArray.length == 0 || classArray[0] == null || (classArray[0] != null && (classArray[0]["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(classArray[0]))))) || classArray === null) && ((toClassArray != null && toClassArray instanceof Array && (toClassArray.length == 0 || toClassArray[0] == null || (toClassArray[0] != null && (toClassArray[0]["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(toClassArray[0]))))) || toClassArray === null) && ((typeof autoboxing === 'boolean') || autoboxing === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(classArray, toClassArray, autoboxing);
                            }
                            else if (((classArray != null && (classArray["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(classArray))) || classArray === null) && ((toClassArray != null && (toClassArray["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(toClassArray))) || toClassArray === null) && ((typeof autoboxing === 'boolean') || autoboxing === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class$java_lang_Class$boolean(classArray, toClassArray, autoboxing);
                            }
                            else if (((classArray != null && classArray instanceof Array && (classArray.length == 0 || classArray[0] == null || (classArray[0] != null && (classArray[0]["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(classArray[0]))))) || classArray === null) && ((toClassArray != null && toClassArray instanceof Array && (toClassArray.length == 0 || toClassArray[0] == null || (toClassArray[0] != null && (toClassArray[0]["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(toClassArray[0]))))) || toClassArray === null) && autoboxing === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A(classArray, toClassArray);
                            }
                            else if (((classArray != null && (classArray["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(classArray))) || classArray === null) && ((toClassArray != null && (toClassArray["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(toClassArray))) || toClassArray === null) && autoboxing === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class$java_lang_Class(classArray, toClassArray);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static isAssignable$java_lang_Class$java_lang_Class(cls, toClass) {
                            return ClassUtils.isAssignable$java_lang_Class$java_lang_Class$boolean(cls, toClass, false);
                        }
                        static isAssignable$java_lang_Class$java_lang_Class$boolean(cls, toClass, autoboxing) {
                            if (toClass == null) {
                                return false;
                            }
                            if (cls == null) {
                                return !( /* isPrimitive */(toClass === '__erasedPrimitiveType__'));
                            }
                            if (autoboxing) {
                                if ( /* isPrimitive */(cls === '__erasedPrimitiveType__') && !(toClass === '__erasedPrimitiveType__')) {
                                    cls = ClassUtils.primitiveToWrapper(cls);
                                    if (cls == null) {
                                        return false;
                                    }
                                }
                                if ( /* isPrimitive */(toClass === '__erasedPrimitiveType__') && !(cls === '__erasedPrimitiveType__')) {
                                    cls = ClassUtils.wrapperToPrimitive(cls);
                                    if (cls == null) {
                                        return false;
                                    }
                                }
                            }
                            if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(cls, toClass)) {
                                return true;
                            }
                            if ( /* isPrimitive */(cls === '__erasedPrimitiveType__')) {
                                if ( /* isPrimitive */(toClass === '__erasedPrimitiveType__') === false) {
                                    return false;
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE, cls)) {
                                    return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, toClass);
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE, cls)) {
                                    return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, toClass);
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.BooleanHelper.TYPE, cls)) {
                                    return false;
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, cls)) {
                                    return false;
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE, cls)) {
                                    return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, toClass);
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.CharacterHelper.TYPE, cls)) {
                                    return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, toClass);
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.ShortHelper.TYPE, cls)) {
                                    return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, toClass);
                                }
                                if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.ByteHelper.TYPE, cls)) {
                                    return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.ShortHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE, toClass) || /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE, toClass);
                                }
                                return false;
                            }
                            return toClass.isAssignableFrom(cls);
                        }
                        /**
                         * <p>Converts the specified primitive Class object to its corresponding
                         * wrapper Class object.</p>
                         *
                         * <p>NOTE: From v2.2, this method handles <code>Void.TYPE</code>,
                         * returning <code>Void.TYPE</code>.</p>
                         *
                         * @param {*} cls  the class to convert, may be null
                         * @return {*} the wrapper class for <code>cls</code> or <code>cls</code> if
                         * <code>cls</code> is not a primitive. <code>null</code> if null input.
                         * @since 2.1
                         */
                        static primitiveToWrapper(cls) {
                            let convertedClass = cls;
                            if (cls != null && /* isPrimitive */ (cls === '__erasedPrimitiveType__')) {
                                convertedClass = ((m, k) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        return m.entries[i].value;
                                    } return null; })(ClassUtils.primitiveWrapperMap_$LI$(), cls);
                            }
                            return convertedClass;
                        }
                        /**
                         * <p>Converts the specified array of primitive Class objects to an array of
                         * its corresponding wrapper Class objects.</p>
                         *
                         * @param {java.lang.Class[]} classes  the class array to convert, may be null or empty
                         * @return {java.lang.Class[]} an array which contains for each given class, the wrapper class or
                         * the original class if class is not a primitive. <code>null</code> if null input.
                         * Empty array if an empty array passed in.
                         * @since 2.1
                         */
                        static primitivesToWrappers(classes) {
                            if (classes == null) {
                                return null;
                            }
                            if (classes.length === 0) {
                                return classes;
                            }
                            const convertedClasses = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(classes.length);
                            for (let i = 0; i < classes.length; i++) {
                                {
                                    convertedClasses[i] = ClassUtils.primitiveToWrapper(classes[i]);
                                }
                                ;
                            }
                            return convertedClasses;
                        }
                        /**
                         * <p>Converts the specified wrapper class to its corresponding primitive
                         * class.</p>
                         *
                         * <p>This method is the counter part of <code>primitiveToWrapper()</code>.
                         * If the passed in class is a wrapper class for a primitive type, this
                         * primitive type will be returned (e1.g. <code>Integer.TYPE</code> for
                         * <code>Integer.class</code>). For other classes, or if the parameter is
                         * <b>null</b>, the return value is <b>null</b>.</p>
                         *
                         * @param {*} cls the class to convert, may be <b>null</b>
                         * @return {*} the corresponding primitive type if <code>cls</code> is a
                         * wrapper class, <b>null</b> otherwise
                         * @see #primitiveToWrapper(Class)
                         * @since 2.4
                         */
                        static wrapperToPrimitive(cls) {
                            return ((m, k) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    return m.entries[i].value;
                                } return null; })(ClassUtils.wrapperPrimitiveMap_$LI$(), cls);
                        }
                        /**
                         * <p>Converts the specified array of wrapper Class objects to an array of
                         * its corresponding primitive Class objects.</p>
                         *
                         * <p>This method invokes <code>wrapperToPrimitive()</code> for each element
                         * of the passed in array.</p>
                         *
                         * @param {java.lang.Class[]} classes  the class array to convert, may be null or empty
                         * @return {java.lang.Class[]} an array which contains for each given class, the primitive class or
                         * <b>null</b> if the original class is not a wrapper class. <code>null</code> if null input.
                         * Empty array if an empty array passed in.
                         * @see #wrapperToPrimitive(Class)
                         * @since 2.4
                         */
                        static wrappersToPrimitives(classes) {
                            if (classes == null) {
                                return null;
                            }
                            if (classes.length === 0) {
                                return classes;
                            }
                            const convertedClasses = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(classes.length);
                            for (let i = 0; i < classes.length; i++) {
                                {
                                    convertedClasses[i] = ClassUtils.wrapperToPrimitive(classes[i]);
                                }
                                ;
                            }
                            return convertedClasses;
                        }
                        /**
                         * <p>Is the specified class an inner class or static nested class.</p>
                         *
                         * @param {*} cls  the class to check, may be null
                         * @return {boolean} <code>true</code> if the class is an inner or static nested class,
                         * false if not or <code>null</code>
                         */
                        static isInnerClass(cls) {
                            if (cls == null) {
                                return false;
                            }
                            return /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls).indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR) >= 0;
                        }
                        static getClass$java_lang_ClassLoader$java_lang_String$boolean(classLoader, className, initialize) {
                            try {
                                let clazz;
                                if ( /* containsKey */((m, k) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        return true;
                                    } return false; })(ClassUtils.abbreviationMap_$LI$(), className)) {
                                    const clsName = "[" + /* get */ ((m, k) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            return m.entries[i].value;
                                        } return null; })(ClassUtils.abbreviationMap_$LI$(), className);
                                    clazz = /* forName */ eval(clsName).getComponentType();
                                }
                                else {
                                    clazz = /* forName */ eval(ClassUtils.toCanonicalName(className));
                                }
                                return clazz;
                            }
                            catch (ex) {
                                const lastDotIndex = className.lastIndexOf(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                                if (lastDotIndex !== -1) {
                                    try {
                                        return this.constructor;
                                    }
                                    catch (ex2) {
                                    }
                                }
                                throw ex;
                            }
                        }
                        /**
                         * Returns the class represented by <code>className</code> using the
                         * <code>classLoader</code>.  This implementation supports the syntaxes
                         * "<code>java.util.Map.Entry[]</code>", "<code>java.util.Map$Entry[]</code>",
                         * "<code>[Ljava.util.Map.Entry;</code>", and "<code>[Ljava.util.Map$Entry;</code>".
                         *
                         * @param {java.lang.ClassLoader} classLoader  the class loader to use to load the class
                         * @param {string} className  the class name
                         * @param {boolean} initialize  whether the class must be initialized
                         * @return {*} the class represented by <code>className</code> using the <code>classLoader</code>
                         * @throws ClassNotFoundException if the class is not found
                         */
                        static getClass(classLoader, className, initialize) {
                            if (((classLoader != null && classLoader instanceof java.lang.ClassLoader) || classLoader === null) && ((typeof className === 'string') || className === null) && ((typeof initialize === 'boolean') || initialize === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_ClassLoader$java_lang_String$boolean(classLoader, className, initialize);
                            }
                            else if (((classLoader != null && classLoader instanceof java.lang.ClassLoader) || classLoader === null) && ((typeof className === 'string') || className === null) && initialize === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_ClassLoader$java_lang_String(classLoader, className);
                            }
                            else if (((typeof classLoader === 'string') || classLoader === null) && ((typeof className === 'boolean') || className === null) && initialize === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_String$boolean(classLoader, className);
                            }
                            else if (((typeof classLoader === 'string') || classLoader === null) && className === undefined && initialize === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_String(classLoader);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static getClass$java_lang_ClassLoader$java_lang_String(classLoader, className) {
                            return this.constructor;
                        }
                        static getClass$java_lang_String(className) {
                            return this.constructor;
                        }
                        static getClass$java_lang_String$boolean(className, initialize) {
                            const contextCL = java.lang.Thread.currentThread().getContextClassLoader();
                            const loader = contextCL == null ? ClassUtils.getClassLoader() : contextCL;
                            return this.constructor;
                        }
                        /**
                         * <p>Returns the desired Method much like <code>Class.getMethod</code>, however
                         * it ensures that the returned Method is from a public class or interface and not
                         * from an anonymous inner class. This means that the Method is invokable and
                         * doesn't fall foul of Java bug
                         * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957">4071957</a>).
                         *
                         * <code>Set set = Collections.unmodifiableSet(...);
                         * Method method = ClassUtils.getPublicMethod(set.getClass(), "isEmpty",  new Class[0]);
                         * Object result = method.invoke(set, new Object[]);</code>
                         * </p>
                         *
                         * @param {*} cls  the class to check, not null
                         * @param {string} methodName  the name of the method
                         * @param {java.lang.Class[]} parameterTypes  the list of parameters
                         * @return {{ owner: any, name: string, fn : Function }} the method
                         * @throws NullPointerException if the class is null
                         * @throws SecurityException if a a security violation occured
                         * @throws NoSuchMethodException if the method is not found in the given class
                         * or if the metothod doen't conform with the requirements
                         */
                        static getPublicMethod(cls, methodName, parameterTypes) {
                            const declaredMethod = ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                return { owner: c, name: p, fn: c.prototype[p] };
                            else
                                return null; })(cls, methodName);
                            if (java.lang.reflect.Modifier.isPublic(/* getDeclaringClass */ declaredMethod.owner.getModifiers())) {
                                return declaredMethod;
                            }
                            const candidateClasses = ([]);
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(candidateClasses, ClassUtils.getAllInterfaces$java_lang_Class(cls));
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(candidateClasses, ClassUtils.getAllSuperclasses(cls));
                            for (const it = ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(candidateClasses); it.hasNext();) {
                                {
                                    const candidateClass = it.next();
                                    if (!java.lang.reflect.Modifier.isPublic(candidateClass.getModifiers())) {
                                        continue;
                                    }
                                    let candidateMethod;
                                    try {
                                        candidateMethod = /* getMethod */ ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                            return { owner: c, name: p, fn: c.prototype[p] };
                                        else
                                            return null; })(candidateClass, methodName);
                                    }
                                    catch (ex) {
                                        continue;
                                    }
                                    if (java.lang.reflect.Modifier.isPublic(/* getDeclaringClass */ candidateMethod.owner.getModifiers())) {
                                        return candidateMethod;
                                    }
                                }
                                ;
                            }
                            throw Object.defineProperty(new Error("Can\'t find a public method for " + methodName + " " + org.openprovenance.apache.commons.lang.ArrayUtils.toString$java_lang_Object(parameterTypes)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NoSuchMethodException', 'java.lang.Object', 'java.lang.ReflectiveOperationException', 'java.lang.Exception'] });
                        }
                        /**
                         * Converts a class name to a JLS style class name.
                         *
                         * @param {string} className  the class name
                         * @return {string} the converted name
                         * @private
                         */
                        /*private*/ static toCanonicalName(className) {
                            className = org.openprovenance.apache.commons.lang.StringUtils.deleteWhitespace(className);
                            if (className == null) {
                                throw new org.openprovenance.apache.commons.lang.NullArgumentException("className");
                            }
                            else if ( /* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(className, "[]")) {
                                const classNameBuffer = new org.openprovenance.apache.commons.lang.text.StrBuilder();
                                while (( /* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(className, "[]"))) {
                                    {
                                        className = className.substring(0, className.length - 2);
                                        classNameBuffer.append$java_lang_String("[");
                                    }
                                }
                                ;
                                const abbreviation = ((m, k) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        return m.entries[i].value;
                                    } return null; })(ClassUtils.abbreviationMap_$LI$(), className);
                                if (abbreviation != null) {
                                    classNameBuffer.append$java_lang_String(abbreviation);
                                }
                                else {
                                    classNameBuffer.append$java_lang_String("L").append$java_lang_String(className).append$java_lang_String(";");
                                }
                                className = classNameBuffer.toString();
                            }
                            return className;
                        }
                        /**
                         * <p>Converts an array of <code>Object</code> in to an array of <code>Class</code> objects.
                         * If any of these objects is null, a null element will be inserted into the array.</p>
                         *
                         * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
                         *
                         * @param {java.lang.Object[]} array an <code>Object</code> array
                         * @return {java.lang.Class[]} a <code>Class</code> array, <code>null</code> if null array input
                         * @since 2.4
                         */
                        static toClass(array) {
                            if (array == null) {
                                return null;
                            }
                            else if (array.length === 0) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                            }
                            const classes = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(array.length);
                            for (let i = 0; i < array.length; i++) {
                                {
                                    classes[i] = array[i] == null ? null : array[i].constructor;
                                }
                                ;
                            }
                            return classes;
                        }
                        static getShortCanonicalName$java_lang_Object$java_lang_String(object, valueIfNull) {
                            if (object == null) {
                                return valueIfNull;
                            }
                            return ClassUtils.getShortCanonicalName$java_lang_String(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor));
                        }
                        /**
                         * <p>Gets the canonical name minus the package name for an <code>Object</code>.</p>
                         *
                         * @param {*} object  the class to get the short name for, may be null
                         * @param {string} valueIfNull  the value to return if null
                         * @return {string} the canonical name of the object without the package name, or the null value
                         * @since 2.4
                         */
                        static getShortCanonicalName(object, valueIfNull) {
                            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortCanonicalName$java_lang_Object$java_lang_String(object, valueIfNull);
                            }
                            else if (((object != null && (object["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(object))) || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortCanonicalName$java_lang_Class(object);
                            }
                            else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getShortCanonicalName$java_lang_String(object);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static getShortCanonicalName$java_lang_Class(cls) {
                            if (cls == null) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            return ClassUtils.getShortCanonicalName$java_lang_String(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
                        }
                        static getShortCanonicalName$java_lang_String(canonicalName) {
                            return ClassUtils.getShortClassName$java_lang_String(ClassUtils.getCanonicalName(canonicalName));
                        }
                        static getPackageCanonicalName$java_lang_Object$java_lang_String(object, valueIfNull) {
                            if (object == null) {
                                return valueIfNull;
                            }
                            return ClassUtils.getPackageCanonicalName$java_lang_String(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor));
                        }
                        /**
                         * <p>Gets the package name from the canonical name of an <code>Object</code>.</p>
                         *
                         * @param {*} object  the class to get the package name for, may be null
                         * @param {string} valueIfNull  the value to return if null
                         * @return {string} the package name of the object, or the null value
                         * @since 2.4
                         */
                        static getPackageCanonicalName(object, valueIfNull) {
                            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getPackageCanonicalName$java_lang_Object$java_lang_String(object, valueIfNull);
                            }
                            else if (((object != null && (object["__class"] != null || ((t) => { try {
                                new t;
                                return true;
                            }
                            catch (_a) {
                                return false;
                            } })(object))) || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getPackageCanonicalName$java_lang_Class(object);
                            }
                            else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                                return org.openprovenance.apache.commons.lang.ClassUtils.getPackageCanonicalName$java_lang_String(object);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static getPackageCanonicalName$java_lang_Class(cls) {
                            if (cls == null) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            return ClassUtils.getPackageCanonicalName$java_lang_String(/* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
                        }
                        static getPackageCanonicalName$java_lang_String(canonicalName) {
                            return ClassUtils.getPackageName$java_lang_String(ClassUtils.getCanonicalName(canonicalName));
                        }
                        /**
                         * <p>Converts a given name of class into canonical format.
                         * If name of class is not a name of array class it returns
                         * unchanged name.</p>
                         * <p>Example:
                         * <ul>
                         * <li><code>getCanonicalName("[I") = "int[]"</code></li>
                         * <li><code>getCanonicalName("[Ljava.lang.String;") = "java.lang.String[]"</code></li>
                         * <li><code>getCanonicalName("java.lang.String") = "java.lang.String"</code></li>
                         * </ul>
                         * </p>
                         *
                         * @param {string} className the name of class
                         * @return {string} canonical form of class name
                         * @since 2.4
                         * @private
                         */
                        /*private*/ static getCanonicalName(className) {
                            className = org.openprovenance.apache.commons.lang.StringUtils.deleteWhitespace(className);
                            if (className == null) {
                                return null;
                            }
                            else {
                                let dim = 0;
                                while (( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(className, "["))) {
                                    {
                                        dim++;
                                        className = className.substring(1);
                                    }
                                }
                                ;
                                if (dim < 1) {
                                    return className;
                                }
                                else {
                                    if ( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(className, "L")) {
                                        className = className.substring(1, /* endsWith */ ((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(className, ";") ? className.length - 1 : className.length);
                                    }
                                    else {
                                        if (className.length > 0) {
                                            className = ((m, k) => { if (m.entries == null)
                                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                    return m.entries[i].value;
                                                } return null; })(ClassUtils.reverseAbbreviationMap_$LI$(), className.substring(0, 1));
                                        }
                                    }
                                    const canonicalClassNameBuffer = new org.openprovenance.apache.commons.lang.text.StrBuilder(className);
                                    for (let i = 0; i < dim; i++) {
                                        {
                                            canonicalClassNameBuffer.append$java_lang_String("[]");
                                        }
                                        ;
                                    }
                                    return canonicalClassNameBuffer.toString();
                                }
                            }
                        }
                    }
                    ClassUtils.__static_initialized = false;
                    /**
                     * <p>The package separator character: <code>'&#x2e;' == {@value}</code>.</p>
                     */
                    ClassUtils.PACKAGE_SEPARATOR_CHAR = '.';
                    /**
                     * <p>The inner class separator character: <code>'$' == {@value}</code>.</p>
                     */
                    ClassUtils.INNER_CLASS_SEPARATOR_CHAR = '$';
                    lang.ClassUtils = ClassUtils;
                    ClassUtils["__class"] = "org.openprovenance.apache.commons.lang.ClassUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
org.openprovenance.apache.commons.lang.ClassUtils.__static_initialize();
