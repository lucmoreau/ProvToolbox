/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
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
    export class ClassUtils {
        static __static_initialized: boolean = false;
        static __static_initialize() { if (!ClassUtils.__static_initialized) { ClassUtils.__static_initialized = true; ClassUtils.__static_initializer_0(); ClassUtils.__static_initializer_1(); ClassUtils.__static_initializer_2(); } }

        /**
         * <p>The package separator character: <code>'&#x2e;' == {@value}</code>.</p>
         */
        public static PACKAGE_SEPARATOR_CHAR: string = '.';

        /**
         * <p>The package separator String: <code>"&#x2e;"</code>.</p>
         */
        public static PACKAGE_SEPARATOR: string; public static PACKAGE_SEPARATOR_$LI$(): string { ClassUtils.__static_initialize(); if (ClassUtils.PACKAGE_SEPARATOR == null) { ClassUtils.PACKAGE_SEPARATOR = /* valueOf */String(ClassUtils.PACKAGE_SEPARATOR_CHAR).toString(); }  return ClassUtils.PACKAGE_SEPARATOR; }

        /**
         * <p>The inner class separator character: <code>'$' == {@value}</code>.</p>
         */
        public static INNER_CLASS_SEPARATOR_CHAR: string = '$';

        /**
         * <p>The inner class separator String: <code>"$"</code>.</p>
         */
        public static INNER_CLASS_SEPARATOR: string; public static INNER_CLASS_SEPARATOR_$LI$(): string { ClassUtils.__static_initialize(); if (ClassUtils.INNER_CLASS_SEPARATOR == null) { ClassUtils.INNER_CLASS_SEPARATOR = /* valueOf */String(ClassUtils.INNER_CLASS_SEPARATOR_CHAR).toString(); }  return ClassUtils.INNER_CLASS_SEPARATOR; }

        /**
         * Maps primitive <code>Class</code>es to their corresponding wrapper <code>Class</code>.
         */
        static primitiveWrapperMap: any; public static primitiveWrapperMap_$LI$(): any { ClassUtils.__static_initialize(); if (ClassUtils.primitiveWrapperMap == null) { ClassUtils.primitiveWrapperMap = <any>({}); }  return ClassUtils.primitiveWrapperMap; }

        static  __static_initializer_0() {
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.BooleanHelper.TYPE, Boolean);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.ByteHelper.TYPE, Number);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.CharacterHelper.TYPE, String);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.ShortHelper.TYPE, Number);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.IntegerHelper.TYPE, Number);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.LongHelper.TYPE, Number);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.DoubleHelper.TYPE, Number);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), javaemul.internal.FloatHelper.TYPE, Number);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.primitiveWrapperMap_$LI$(), void.TYPE, void.TYPE);
        }

        /**
         * Maps wrapper <code>Class</code>es to their corresponding primitive types.
         */
        static wrapperPrimitiveMap: any; public static wrapperPrimitiveMap_$LI$(): any { ClassUtils.__static_initialize(); if (ClassUtils.wrapperPrimitiveMap == null) { ClassUtils.wrapperPrimitiveMap = <any>({}); }  return ClassUtils.wrapperPrimitiveMap; }

        static  __static_initializer_1() {
            for(const it: any = /* iterator */((a) => { var i = 0; return { next: function() { return i<a.length?a[i++]:null; }, hasNext: function() { return i<a.length; }}})(/* keySet */((m) => { let r=[]; if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) r.push(m.entries[i].key); return r; })(<any>ClassUtils.primitiveWrapperMap_$LI$())); it.hasNext(); ) {{
                const primitiveClass: any = <any>it.next();
                const wrapperClass: any = <any>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.primitiveWrapperMap_$LI$(), primitiveClass);
                if (!/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(primitiveClass,wrapperClass))){
                    /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.wrapperPrimitiveMap_$LI$(), wrapperClass, primitiveClass);
                }
            };}
        }

        /**
         * Maps a primitive class name to its corresponding abbreviation used in array class names.
         */
        static abbreviationMap: any; public static abbreviationMap_$LI$(): any { ClassUtils.__static_initialize(); if (ClassUtils.abbreviationMap == null) { ClassUtils.abbreviationMap = <any>({}); }  return ClassUtils.abbreviationMap; }

        /**
         * Maps an abbreviation used in array class names to corresponding primitive class name.
         */
        static reverseAbbreviationMap: any; public static reverseAbbreviationMap_$LI$(): any { ClassUtils.__static_initialize(); if (ClassUtils.reverseAbbreviationMap == null) { ClassUtils.reverseAbbreviationMap = <any>({}); }  return ClassUtils.reverseAbbreviationMap; }

        /**
         * Add primitive type abbreviation to maps of abbreviations.
         * 
         * @param {string} primitive Canonical name of primitive type
         * @param {string} abbreviation Corresponding abbreviation of primitive type
         * @private
         */
        /*private*/ static addAbbreviation(primitive: string, abbreviation: string) {
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.abbreviationMap_$LI$(), primitive, abbreviation);
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>ClassUtils.reverseAbbreviationMap_$LI$(), abbreviation, primitive);
        }

        static  __static_initializer_2() {
            ClassUtils.addAbbreviation("int", "I");
            ClassUtils.addAbbreviation("boolean", "Z");
            ClassUtils.addAbbreviation("float", "F");
            ClassUtils.addAbbreviation("long", "J");
            ClassUtils.addAbbreviation("short", "S");
            ClassUtils.addAbbreviation("byte", "B");
            ClassUtils.addAbbreviation("double", "D");
            ClassUtils.addAbbreviation("char", "C");
        }

        public constructor() {
        }

        public static getShortClassName$java_lang_Object$java_lang_String(object: any, valueIfNull: string): string {
            if (object == null){
                return valueIfNull;
            }
            return ClassUtils.getShortClassName$java_lang_Class((<any>object.constructor));
        }

        /**
         * <p>Gets the class name minus the package name for an <code>Object</code>.</p>
         * 
         * @param {*} object  the class to get the short name for, may be null
         * @param {string} valueIfNull  the value to return if null
         * @return {string} the class name of the object without the package name, or the null value
         */
        public static getShortClassName(object?: any, valueIfNull?: any): any {
            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Object$java_lang_String(object, valueIfNull);
            } else if (((object != null && (object["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(object))) || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Class(object);
            } else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_String(object);
            } else throw new Error('invalid overload');
        }

        public static getShortClassName$java_lang_Class(cls: any): string {
            if (cls == null){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            return ClassUtils.getShortClassName$java_lang_String(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
        }

        public static getShortClassName$java_lang_String(className: string): string {
            if (className == null){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            if (className.length === 0){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            const arrayPrefix: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder();
            if (/* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(className, "[")){
                while(((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(className.charAt(0)) == '['.charCodeAt(0))) {{
                    className = className.substring(1);
                    arrayPrefix.append$java_lang_String("[]");
                }};
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(className.charAt(0)) == 'L'.charCodeAt(0) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(className.charAt(className.length - 1)) == ';'.charCodeAt(0)){
                    className = className.substring(1, className.length - 1);
                }
            }
            if (/* containsKey */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return true; } return false; })(<any>ClassUtils.reverseAbbreviationMap_$LI$(), className)){
                className = <string>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.reverseAbbreviationMap_$LI$(), className);
            }
            const lastDotIdx: number = className.lastIndexOf(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            const innerIdx: number = className.indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR, lastDotIdx === -1 ? 0 : lastDotIdx + 1);
            let out: string = className.substring(lastDotIdx + 1);
            if (innerIdx !== -1){
                out = /* replace */out.split(ClassUtils.INNER_CLASS_SEPARATOR_CHAR).join(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            }
            return out + arrayPrefix;
        }

        public static getPackageName$java_lang_Object$java_lang_String(object: any, valueIfNull: string): string {
            if (object == null){
                return valueIfNull;
            }
            return ClassUtils.getPackageName$java_lang_Class((<any>object.constructor));
        }

        /**
         * <p>Gets the package name of an <code>Object</code>.</p>
         * 
         * @param {*} object  the class to get the package name for, may be null
         * @param {string} valueIfNull  the value to return if null
         * @return {string} the package name of the object, or the null value
         */
        public static getPackageName(object?: any, valueIfNull?: any): any {
            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getPackageName$java_lang_Object$java_lang_String(object, valueIfNull);
            } else if (((object != null && (object["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(object))) || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getPackageName$java_lang_Class(object);
            } else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getPackageName$java_lang_String(object);
            } else throw new Error('invalid overload');
        }

        public static getPackageName$java_lang_Class(cls: any): string {
            if (cls == null){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            return ClassUtils.getPackageName$java_lang_String(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
        }

        public static getPackageName$java_lang_String(className: string): string {
            if (className == null || className.length === 0){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            while(((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(className.charAt(0)) == '['.charCodeAt(0))) {{
                className = className.substring(1);
            }};
            if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(className.charAt(0)) == 'L'.charCodeAt(0) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(className.charAt(className.length - 1)) == ';'.charCodeAt(0)){
                className = className.substring(1);
            }
            const i: number = className.lastIndexOf(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            if (i === -1){
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
        public static getAllSuperclasses(cls: any): Array<any> {
            if (cls == null){
                return null;
            }
            const classes: Array<any> = <any>([]);
            let superclass: any = cls.getSuperclass();
            while((superclass != null)) {{
                /* add */(classes.push(superclass)>0);
                superclass = superclass.getSuperclass();
            }};
            return classes;
        }

        public static getAllInterfaces$java_lang_Class(cls: any): Array<any> {
            if (cls == null){
                return null;
            }
            const interfacesFound: Array<any> = <any>([]);
            ClassUtils.getAllInterfaces$java_lang_Class$java_util_List(cls, interfacesFound);
            return interfacesFound;
        }

        public static getAllInterfaces$java_lang_Class$java_util_List(cls: any, interfacesFound: Array<any>) {
            while((cls != null)) {{
                const interfaces: any[] = cls.getInterfaces();
                for(let i: number = 0; i < interfaces.length; i++) {{
                    if (!/* contains */(interfacesFound.indexOf(<any>(interfaces[i])) >= 0)){
                        /* add */(interfacesFound.push(interfaces[i])>0);
                        ClassUtils.getAllInterfaces$java_lang_Class$java_util_List(interfaces[i], interfacesFound);
                    }
                };}
                cls = cls.getSuperclass();
            }};
        }

        /**
         * Get the interfaces for the specified class.
         * 
         * @param {*} cls  the class to look up, may be <code>null</code>
         * @param {Array} interfacesFound the <code>Set</code> of interfaces for the class
         * @private
         */
        public static getAllInterfaces(cls?: any, interfacesFound?: any) {
            if (((cls != null && (cls["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(cls))) || cls === null) && ((interfacesFound != null && (interfacesFound instanceof Array)) || interfacesFound === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getAllInterfaces$java_lang_Class$java_util_List(cls, interfacesFound);
            } else if (((cls != null && (cls["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(cls))) || cls === null) && interfacesFound === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getAllInterfaces$java_lang_Class(cls);
            } else throw new Error('invalid overload');
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
        public static convertClassNamesToClasses(classNames: Array<any>): Array<any> {
            if (classNames == null){
                return null;
            }
            const classes: Array<any> = <any>([]);
            for(const it: any = /* iterator */((a) => { var i = 0; return { next: function() { return i<a.length?a[i++]:null; }, hasNext: function() { return i<a.length; }}})(classNames); it.hasNext(); ) {{
                const className: string = <string>it.next();
                try {
                    /* add */(classes.push(/* forName */eval(className))>0);
                } catch(ex) {
                    /* add */(classes.push(null)>0);
                }
            };}
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
        public static convertClassesToClassNames(classes: Array<any>): Array<any> {
            if (classes == null){
                return null;
            }
            const classNames: Array<any> = <any>([]);
            for(const it: any = /* iterator */((a) => { var i = 0; return { next: function() { return i<a.length?a[i++]:null; }, hasNext: function() { return i<a.length; }}})(classes); it.hasNext(); ) {{
                const cls: any = <any>it.next();
                if (cls == null){
                    /* add */(classNames.push(null)>0);
                } else {
                    /* add */(classNames.push(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls))>0);
                }
            };}
            return classNames;
        }

        public static isAssignable$java_lang_Class_A$java_lang_Class_A(classArray: any[], toClassArray: any[]): boolean {
            return ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(classArray, toClassArray, false);
        }

        public static isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(classArray: any[], toClassArray: any[], autoboxing: boolean): boolean {
            if (org.openprovenance.apache.commons.lang.ArrayUtils.isSameLength$java_lang_Object_A$java_lang_Object_A(classArray, toClassArray) === false){
                return false;
            }
            if (classArray == null){
                classArray = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
            }
            if (toClassArray == null){
                toClassArray = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
            }
            for(let i: number = 0; i < classArray.length; i++) {{
                if (ClassUtils.isAssignable$java_lang_Class$java_lang_Class$boolean(classArray[i], toClassArray[i], autoboxing) === false){
                    return false;
                }
            };}
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
        public static isAssignable(classArray?: any, toClassArray?: any, autoboxing?: any): any {
            if (((classArray != null && classArray instanceof <any>Array && (classArray.length == 0 || classArray[0] == null ||(classArray[0] != null && (classArray[0]["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classArray[0]))))) || classArray === null) && ((toClassArray != null && toClassArray instanceof <any>Array && (toClassArray.length == 0 || toClassArray[0] == null ||(toClassArray[0] != null && (toClassArray[0]["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(toClassArray[0]))))) || toClassArray === null) && ((typeof autoboxing === 'boolean') || autoboxing === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(classArray, toClassArray, autoboxing);
            } else if (((classArray != null && (classArray["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classArray))) || classArray === null) && ((toClassArray != null && (toClassArray["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(toClassArray))) || toClassArray === null) && ((typeof autoboxing === 'boolean') || autoboxing === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class$java_lang_Class$boolean(classArray, toClassArray, autoboxing);
            } else if (((classArray != null && classArray instanceof <any>Array && (classArray.length == 0 || classArray[0] == null ||(classArray[0] != null && (classArray[0]["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classArray[0]))))) || classArray === null) && ((toClassArray != null && toClassArray instanceof <any>Array && (toClassArray.length == 0 || toClassArray[0] == null ||(toClassArray[0] != null && (toClassArray[0]["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(toClassArray[0]))))) || toClassArray === null) && autoboxing === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A(classArray, toClassArray);
            } else if (((classArray != null && (classArray["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classArray))) || classArray === null) && ((toClassArray != null && (toClassArray["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(toClassArray))) || toClassArray === null) && autoboxing === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class$java_lang_Class(classArray, toClassArray);
            } else throw new Error('invalid overload');
        }

        public static isAssignable$java_lang_Class$java_lang_Class(cls: any, toClass: any): boolean {
            return ClassUtils.isAssignable$java_lang_Class$java_lang_Class$boolean(cls, toClass, false);
        }

        public static isAssignable$java_lang_Class$java_lang_Class$boolean(cls: any, toClass: any, autoboxing: boolean): boolean {
            if (toClass == null){
                return false;
            }
            if (cls == null){
                return !(/* isPrimitive */(toClass === <any>'__erasedPrimitiveType__'));
            }
            if (autoboxing){
                if (/* isPrimitive */(cls === <any>'__erasedPrimitiveType__') && !/* isPrimitive */(toClass === <any>'__erasedPrimitiveType__')){
                    cls = ClassUtils.primitiveToWrapper(cls);
                    if (cls == null){
                        return false;
                    }
                }
                if (/* isPrimitive */(toClass === <any>'__erasedPrimitiveType__') && !/* isPrimitive */(cls === <any>'__erasedPrimitiveType__')){
                    cls = ClassUtils.wrapperToPrimitive(cls);
                    if (cls == null){
                        return false;
                    }
                }
            }
            if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(cls,toClass))){
                return true;
            }
            if (/* isPrimitive */(cls === <any>'__erasedPrimitiveType__')){
                if (/* isPrimitive */(toClass === <any>'__erasedPrimitiveType__') === false){
                    return false;
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE,cls))){
                    return /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,toClass));
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE,cls))){
                    return /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,toClass));
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.BooleanHelper.TYPE,cls))){
                    return false;
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,cls))){
                    return false;
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE,cls))){
                    return /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,toClass));
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.CharacterHelper.TYPE,cls))){
                    return /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,toClass));
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.ShortHelper.TYPE,cls))){
                    return /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,toClass));
                }
                if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.ByteHelper.TYPE,cls))){
                    return /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.ShortHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.IntegerHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.LongHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.FloatHelper.TYPE,toClass)) || /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(javaemul.internal.DoubleHelper.TYPE,toClass));
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
        public static primitiveToWrapper(cls: any): any {
            let convertedClass: any = cls;
            if (cls != null && /* isPrimitive */(cls === <any>'__erasedPrimitiveType__')){
                convertedClass = <any>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.primitiveWrapperMap_$LI$(), cls);
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
        public static primitivesToWrappers(classes: any[]): any[] {
            if (classes == null){
                return null;
            }
            if (classes.length === 0){
                return classes;
            }
            const convertedClasses: any[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })(classes.length);
            for(let i: number = 0; i < classes.length; i++) {{
                convertedClasses[i] = ClassUtils.primitiveToWrapper(classes[i]);
            };}
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
        public static wrapperToPrimitive(cls: any): any {
            return <any>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.wrapperPrimitiveMap_$LI$(), cls);
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
        public static wrappersToPrimitives(classes: any[]): any[] {
            if (classes == null){
                return null;
            }
            if (classes.length === 0){
                return classes;
            }
            const convertedClasses: any[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })(classes.length);
            for(let i: number = 0; i < classes.length; i++) {{
                convertedClasses[i] = ClassUtils.wrapperToPrimitive(classes[i]);
            };}
            return convertedClasses;
        }

        /**
         * <p>Is the specified class an inner class or static nested class.</p>
         * 
         * @param {*} cls  the class to check, may be null
         * @return {boolean} <code>true</code> if the class is an inner or static nested class,
         * false if not or <code>null</code>
         */
        public static isInnerClass(cls: any): boolean {
            if (cls == null){
                return false;
            }
            return /* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls).indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR) >= 0;
        }

        public static getClass$java_lang_ClassLoader$java_lang_String$boolean(classLoader: java.lang.ClassLoader, className: string, initialize: boolean): any {
            try {
                let clazz: any;
                if (/* containsKey */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return true; } return false; })(<any>ClassUtils.abbreviationMap_$LI$(), className)){
                    const clsName: string = "[" + /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.abbreviationMap_$LI$(), className);
                    clazz = /* forName */eval(clsName).getComponentType();
                } else {
                    clazz = /* forName */eval(ClassUtils.toCanonicalName(className));
                }
                return clazz;
            } catch(ex) {
                const lastDotIndex: number = className.lastIndexOf(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                if (lastDotIndex !== -1){
                    try {
                        return (<any>this.constructor);
                    } catch(ex2) {
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
        public static getClass(classLoader?: any, className?: any, initialize?: any): any {
            if (((classLoader != null && classLoader instanceof <any>java.lang.ClassLoader) || classLoader === null) && ((typeof className === 'string') || className === null) && ((typeof initialize === 'boolean') || initialize === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_ClassLoader$java_lang_String$boolean(classLoader, className, initialize);
            } else if (((classLoader != null && classLoader instanceof <any>java.lang.ClassLoader) || classLoader === null) && ((typeof className === 'string') || className === null) && initialize === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_ClassLoader$java_lang_String(classLoader, className);
            } else if (((typeof classLoader === 'string') || classLoader === null) && ((typeof className === 'boolean') || className === null) && initialize === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_String$boolean(classLoader, className);
            } else if (((typeof classLoader === 'string') || classLoader === null) && className === undefined && initialize === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getClass$java_lang_String(classLoader);
            } else throw new Error('invalid overload');
        }

        public static getClass$java_lang_ClassLoader$java_lang_String(classLoader: java.lang.ClassLoader, className: string): any {
            return (<any>this.constructor);
        }

        public static getClass$java_lang_String(className: string): any {
            return (<any>this.constructor);
        }

        public static getClass$java_lang_String$boolean(className: string, initialize: boolean): any {
            const contextCL: java.lang.ClassLoader = java.lang.Thread.currentThread().getContextClassLoader();
            const loader: java.lang.ClassLoader = contextCL == null ? ClassUtils.getClassLoader() : contextCL;
            return (<any>this.constructor);
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
        public static getPublicMethod(cls: any, methodName: string, parameterTypes: any[]): { owner: any, name: string, fn : Function } {
            const declaredMethod: { owner: any, name: string, fn : Function } = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })(cls,methodName);
            if (java.lang.reflect.Modifier.isPublic(/* getDeclaringClass */declaredMethod.owner.getModifiers())){
                return declaredMethod;
            }
            const candidateClasses: Array<any> = <any>([]);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(candidateClasses, ClassUtils.getAllInterfaces$java_lang_Class(cls));
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(candidateClasses, ClassUtils.getAllSuperclasses(cls));
            for(const it: any = /* iterator */((a) => { var i = 0; return { next: function() { return i<a.length?a[i++]:null; }, hasNext: function() { return i<a.length; }}})(candidateClasses); it.hasNext(); ) {{
                const candidateClass: any = <any>it.next();
                if (!java.lang.reflect.Modifier.isPublic(candidateClass.getModifiers())){
                    continue;
                }
                let candidateMethod: { owner: any, name: string, fn : Function };
                try {
                    candidateMethod = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })(candidateClass,methodName);
                } catch(ex) {
                    continue;
                }
                if (java.lang.reflect.Modifier.isPublic(/* getDeclaringClass */candidateMethod.owner.getModifiers())){
                    return candidateMethod;
                }
            };}
            throw Object.defineProperty(new Error("Can\'t find a public method for " + methodName + " " + org.openprovenance.apache.commons.lang.ArrayUtils.toString$java_lang_Object(parameterTypes)), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NoSuchMethodException','java.lang.Object','java.lang.ReflectiveOperationException','java.lang.Exception'] });
        }

        /**
         * Converts a class name to a JLS style class name.
         * 
         * @param {string} className  the class name
         * @return {string} the converted name
         * @private
         */
        /*private*/ static toCanonicalName(className: string): string {
            className = org.openprovenance.apache.commons.lang.StringUtils.deleteWhitespace(className);
            if (className == null){
                throw new org.openprovenance.apache.commons.lang.NullArgumentException("className");
            } else if (/* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(className, "[]")){
                const classNameBuffer: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder();
                while((/* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(className, "[]"))) {{
                    className = className.substring(0, className.length - 2);
                    classNameBuffer.append$java_lang_String("[");
                }};
                const abbreviation: string = <string>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.abbreviationMap_$LI$(), className);
                if (abbreviation != null){
                    classNameBuffer.append$java_lang_String(abbreviation);
                } else {
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
        public static toClass(array: any[]): any[] {
            if (array == null){
                return null;
            } else if (array.length === 0){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
            }
            const classes: any[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })(array.length);
            for(let i: number = 0; i < array.length; i++) {{
                classes[i] = array[i] == null ? null : (<any>array[i].constructor);
            };}
            return classes;
        }

        public static getShortCanonicalName$java_lang_Object$java_lang_String(object: any, valueIfNull: string): string {
            if (object == null){
                return valueIfNull;
            }
            return ClassUtils.getShortCanonicalName$java_lang_String(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])((<any>object.constructor)));
        }

        /**
         * <p>Gets the canonical name minus the package name for an <code>Object</code>.</p>
         * 
         * @param {*} object  the class to get the short name for, may be null
         * @param {string} valueIfNull  the value to return if null
         * @return {string} the canonical name of the object without the package name, or the null value
         * @since 2.4
         */
        public static getShortCanonicalName(object?: any, valueIfNull?: any): any {
            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getShortCanonicalName$java_lang_Object$java_lang_String(object, valueIfNull);
            } else if (((object != null && (object["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(object))) || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getShortCanonicalName$java_lang_Class(object);
            } else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getShortCanonicalName$java_lang_String(object);
            } else throw new Error('invalid overload');
        }

        public static getShortCanonicalName$java_lang_Class(cls: any): string {
            if (cls == null){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            return ClassUtils.getShortCanonicalName$java_lang_String(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
        }

        public static getShortCanonicalName$java_lang_String(canonicalName: string): string {
            return ClassUtils.getShortClassName$java_lang_String(ClassUtils.getCanonicalName(canonicalName));
        }

        public static getPackageCanonicalName$java_lang_Object$java_lang_String(object: any, valueIfNull: string): string {
            if (object == null){
                return valueIfNull;
            }
            return ClassUtils.getPackageCanonicalName$java_lang_String(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])((<any>object.constructor)));
        }

        /**
         * <p>Gets the package name from the canonical name of an <code>Object</code>.</p>
         * 
         * @param {*} object  the class to get the package name for, may be null
         * @param {string} valueIfNull  the value to return if null
         * @return {string} the package name of the object, or the null value
         * @since 2.4
         */
        public static getPackageCanonicalName(object?: any, valueIfNull?: any): any {
            if (((object != null) || object === null) && ((typeof valueIfNull === 'string') || valueIfNull === null)) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getPackageCanonicalName$java_lang_Object$java_lang_String(object, valueIfNull);
            } else if (((object != null && (object["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(object))) || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getPackageCanonicalName$java_lang_Class(object);
            } else if (((typeof object === 'string') || object === null) && valueIfNull === undefined) {
                return <any>org.openprovenance.apache.commons.lang.ClassUtils.getPackageCanonicalName$java_lang_String(object);
            } else throw new Error('invalid overload');
        }

        public static getPackageCanonicalName$java_lang_Class(cls: any): string {
            if (cls == null){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            return ClassUtils.getPackageCanonicalName$java_lang_String(/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls));
        }

        public static getPackageCanonicalName$java_lang_String(canonicalName: string): string {
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
        /*private*/ static getCanonicalName(className: string): string {
            className = org.openprovenance.apache.commons.lang.StringUtils.deleteWhitespace(className);
            if (className == null){
                return null;
            } else {
                let dim: number = 0;
                while((/* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(className, "["))) {{
                    dim++;
                    className = className.substring(1);
                }};
                if (dim < 1){
                    return className;
                } else {
                    if (/* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(className, "L")){
                        className = className.substring(1, /* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(className, ";") ? className.length - 1 : className.length);
                    } else {
                        if (className.length > 0){
                            className = <string>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ClassUtils.reverseAbbreviationMap_$LI$(), className.substring(0, 1));
                        }
                    }
                    const canonicalClassNameBuffer: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder(className);
                    for(let i: number = 0; i < dim; i++) {{
                        canonicalClassNameBuffer.append$java_lang_String("[]");
                    };}
                    return canonicalClassNameBuffer.toString();
                }
            }
        }
    }
    ClassUtils["__class"] = "org.openprovenance.apache.commons.lang.ClassUtils";

}


org.openprovenance.apache.commons.lang.ClassUtils.__static_initialize();
