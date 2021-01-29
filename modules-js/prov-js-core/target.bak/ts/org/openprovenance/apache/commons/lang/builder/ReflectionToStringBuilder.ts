/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.builder {
    /**
     * Constructor.
     * 
     * @param {*} object
     * the Object to build a <code>toString</code> for
     * @param {org.openprovenance.apache.commons.lang.builder.ToStringStyle} style
     * the style of the <code>toString</code> to create, may be <code>null</code>
     * @param {{ str: string, toString: Function }} buffer
     * the <code>StringBuffer</code> to populate, may be <code>null</code>
     * @param {*} reflectUpToClass
     * the superclass to reflect up to (inclusive), may be <code>null</code>
     * @param {boolean} outputTransients
     * whether to include transient fields
     * @param {boolean} outputStatics
     * whether to include static fields
     * @since 2.1
     * @class
     * @extends org.openprovenance.apache.commons.lang.builder.ToStringBuilder
     * @author Apache Software Foundation
     */
    export class ReflectionToStringBuilder extends org.openprovenance.apache.commons.lang.builder.ToStringBuilder {
        public static toString$java_lang_Object(object: any): string {
            return ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, null, false, false, null);
        }

        public static toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle(object: any, style: org.openprovenance.apache.commons.lang.builder.ToStringStyle): string {
            return ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, style, false, false, null);
        }

        public static toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean(object: any, style: org.openprovenance.apache.commons.lang.builder.ToStringStyle, outputTransients: boolean): string {
            return ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, style, outputTransients, false, null);
        }

        public static toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean(object: any, style: org.openprovenance.apache.commons.lang.builder.ToStringStyle, outputTransients: boolean, outputStatics: boolean): string {
            return ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, style, outputTransients, outputStatics, null);
        }

        public static toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object: any, style: org.openprovenance.apache.commons.lang.builder.ToStringStyle, outputTransients: boolean, outputStatics: boolean, reflectUpToClass: any): string {
            return new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients, outputStatics).toString();
        }

        /**
         * <p>
         * Builds a <code>toString</code> value through reflection.
         * </p>
         * 
         * <p>
         * It uses <code>AccessibleObject.setAccessible</code> to gain access to private fields. This means that it will
         * throw a security exception if run under a security manager, if the permissions are not set up correctly. It is
         * also not as efficient as testing explicitly.
         * </p>
         * 
         * <p>
         * If the <code>outputTransients</code> is <code>true</code>, transient fields will be output, otherwise they
         * are ignored, as they are likely derived fields, and not part of the value of the Object.
         * </p>
         * 
         * <p>
         * If the <code>outputStatics</code> is <code>true</code>, static fields will be output, otherwise they are
         * ignored.
         * </p>
         * 
         * <p>
         * Superclass fields will be appended up to and including the specified superclass. A null superclass is treated as
         * <code>java.lang.Object</code>.
         * </p>
         * 
         * <p>
         * If the style is <code>null</code>, the default <code>ToStringStyle</code> is used.
         * </p>
         * 
         * @param {*} object
         * the Object to be output
         * @param {org.openprovenance.apache.commons.lang.builder.ToStringStyle} style
         * the style of the <code>toString</code> to create, may be <code>null</code>
         * @param {boolean} outputTransients
         * whether to include transient fields
         * @param {boolean} outputStatics
         * whether to include static fields
         * @param {*} reflectUpToClass
         * the superclass to reflect up to (inclusive), may be <code>null</code>
         * @return {string} the String result
         * @throws IllegalArgumentException
         * if the Object is <code>null</code>
         * @since 2.1
         */
        public static toString(object?: any, style?: any, outputTransients?: any, outputStatics?: any, reflectUpToClass?: any): any {
            if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && ((typeof outputStatics === 'boolean') || outputStatics === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean$java_lang_Class(object, style, outputTransients, outputStatics, reflectUpToClass);
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && ((outputStatics != null && (outputStatics["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(outputStatics))) || outputStatics === null) && reflectUpToClass === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$java_lang_Class(object, style, outputTransients, outputStatics);
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && ((typeof outputStatics === 'boolean') || outputStatics === null) && reflectUpToClass === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$boolean(object, style, outputTransients, outputStatics);
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && outputStatics === undefined && reflectUpToClass === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean(object, style, outputTransients);
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && outputTransients === undefined && outputStatics === undefined && reflectUpToClass === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle(object, style);
            } else if (((object != null) || object === null) && style === undefined && outputTransients === undefined && outputStatics === undefined && reflectUpToClass === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toString$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public static toString$java_lang_Object$org_openprovenance_apache_commons_lang_builder_ToStringStyle$boolean$java_lang_Class(object: any, style: org.openprovenance.apache.commons.lang.builder.ToStringStyle, outputTransients: boolean, reflectUpToClass: any): string {
            return new ReflectionToStringBuilder(object, style, null, reflectUpToClass, outputTransients).toString();
        }

        public static toStringExclude$java_lang_Object$java_lang_String(object: any, excludeFieldName: string): string {
            return ReflectionToStringBuilder.toStringExclude$java_lang_Object$java_lang_String_A(object, [excludeFieldName]);
        }

        /**
         * Builds a String for a toString method excluding the given field name.
         * 
         * @param {*} object
         * The object to "toString".
         * @param {string} excludeFieldName
         * The field name to exclude
         * @return {string} The toString value.
         */
        public static toStringExclude(object?: any, excludeFieldName?: any): any {
            if (((object != null) || object === null) && ((typeof excludeFieldName === 'string') || excludeFieldName === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toStringExclude$java_lang_Object$java_lang_String(object, excludeFieldName);
            } else if (((object != null) || object === null) && ((excludeFieldName != null && (excludeFieldName instanceof Array)) || excludeFieldName === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toStringExclude$java_lang_Object$java_util_Collection(object, excludeFieldName);
            } else if (((object != null) || object === null) && ((excludeFieldName != null && excludeFieldName instanceof <any>Array && (excludeFieldName.length == 0 || excludeFieldName[0] == null ||(typeof excludeFieldName[0] === 'string'))) || excludeFieldName === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toStringExclude$java_lang_Object$java_lang_String_A(object, excludeFieldName);
            } else throw new Error('invalid overload');
        }

        public static toStringExclude$java_lang_Object$java_util_Collection(object: any, excludeFieldNames: Array<any>): string {
            return ReflectionToStringBuilder.toStringExclude$java_lang_Object$java_lang_String_A(object, ReflectionToStringBuilder.toNoNullStringArray$java_util_Collection(excludeFieldNames));
        }

        public static toNoNullStringArray$java_util_Collection(collection: Array<any>): string[] {
            if (collection == null){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
            }
            return ReflectionToStringBuilder.toNoNullStringArray$java_lang_Object_A(/* toArray */collection.slice(0));
        }

        /**
         * Converts the given Collection into an array of Strings. The returned array does not contain <code>null</code>
         * entries. Note that {@link Arrays#sort(Object[])} will throw an {@link NullPointerException} if an array element
         * is <code>null</code>.
         * 
         * @param {Array} collection
         * The collection to convert
         * @return {java.lang.String[]} A new array of Strings.
         */
        public static toNoNullStringArray(collection?: any): any {
            if (((collection != null && (collection instanceof Array)) || collection === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toNoNullStringArray$java_util_Collection(collection);
            } else if (((collection != null && collection instanceof <any>Array && (collection.length == 0 || collection[0] == null ||(collection[0] != null))) || collection === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toNoNullStringArray$java_lang_Object_A(collection);
            } else throw new Error('invalid overload');
        }

        static toNoNullStringArray$java_lang_Object_A(array: any[]): string[] {
            const list: Array<any> = <any>([]);
            for(let i: number = 0; i < array.length; i++) {{
                const e: any = array[i];
                if (e != null){
                    /* add */(list.push(e.toString())>0);
                }
            };}
            return <string[]>/* toArray */((a1, a2) => { if(a1.length >= a2.length) { a1.length=0; a1.push.apply(a1, a2); return a1; } else { return a2.slice(0); } })(org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$(), list);
        }

        public static toStringExclude$java_lang_Object$java_lang_String_A(object: any, excludeFieldNames: string[]): string {
            return new ReflectionToStringBuilder(object).setExcludeFieldNames(excludeFieldNames).toString();
        }

        /**
         * Whether or not to append static fields.
         */
        /*private*/ appendStatics: boolean;

        /**
         * Whether or not to append transient fields.
         */
        /*private*/ appendTransients: boolean;

        /**
         * Which field names to exclude from output. Intended for fields like <code>"password"</code>.
         */
        /*private*/ excludeFieldNames: string[];

        /**
         * The last super class to stop appending fields for.
         */
        /*private*/ upToClass: any;

        public constructor(object?: any, style?: any, buffer?: any, reflectUpToClass?: any, outputTransients?: any, outputStatics?: any) {
            if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((buffer != null && (buffer instanceof Object)) || buffer === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && ((typeof outputStatics === 'boolean') || outputStatics === null)) {
                let __args = arguments;
                super(object, style, buffer);
                if (this.excludeFieldNames === undefined) { this.excludeFieldNames = null; } 
                this.appendStatics = false;
                this.appendTransients = false;
                this.upToClass = null;
                this.setUpToClass(reflectUpToClass);
                this.setAppendTransients(outputTransients);
                this.setAppendStatics(outputStatics);
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((buffer != null && (buffer instanceof Object)) || buffer === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null) && ((typeof outputTransients === 'boolean') || outputTransients === null) && outputStatics === undefined) {
                let __args = arguments;
                super(object, style, buffer);
                if (this.excludeFieldNames === undefined) { this.excludeFieldNames = null; } 
                this.appendStatics = false;
                this.appendTransients = false;
                this.upToClass = null;
                this.setUpToClass(reflectUpToClass);
                this.setAppendTransients(outputTransients);
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && ((buffer != null && (buffer instanceof Object)) || buffer === null) && reflectUpToClass === undefined && outputTransients === undefined && outputStatics === undefined) {
                let __args = arguments;
                super(object, style, buffer);
                if (this.excludeFieldNames === undefined) { this.excludeFieldNames = null; } 
                this.appendStatics = false;
                this.appendTransients = false;
                this.upToClass = null;
            } else if (((object != null) || object === null) && ((style != null && style instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringStyle) || style === null) && buffer === undefined && reflectUpToClass === undefined && outputTransients === undefined && outputStatics === undefined) {
                let __args = arguments;
                super(object, style);
                if (this.excludeFieldNames === undefined) { this.excludeFieldNames = null; } 
                this.appendStatics = false;
                this.appendTransients = false;
                this.upToClass = null;
            } else if (((object != null) || object === null) && style === undefined && buffer === undefined && reflectUpToClass === undefined && outputTransients === undefined && outputStatics === undefined) {
                let __args = arguments;
                super(object);
                if (this.excludeFieldNames === undefined) { this.excludeFieldNames = null; } 
                this.appendStatics = false;
                this.appendTransients = false;
                this.upToClass = null;
            } else throw new Error('invalid overload');
        }

        /**
         * Returns whether or not to append the given <code>Field</code>.
         * <ul>
         * <li>Transient fields are appended only if {@link #isAppendTransients()} returns <code>true</code>.
         * <li>Static fields are appended only if {@link #isAppendStatics()} returns <code>true</code>.
         * <li>Inner class fields are not appened.</li>
         * </ul>
         * 
         * @param {java.lang.reflect.Field} field
         * The Field to test.
         * @return {boolean} Whether or not to append the given <code>Field</code>.
         */
        accept(field: java.lang.reflect.Field): boolean {
            if (/* getName */field.name.indexOf(org.openprovenance.apache.commons.lang.ClassUtils.INNER_CLASS_SEPARATOR_CHAR) !== -1){
                return false;
            }
            if (java.lang.reflect.Modifier.isTransient(field.getModifiers()) && !this.isAppendTransients()){
                return false;
            }
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && !this.isAppendStatics()){
                return false;
            }
            if (this.getExcludeFieldNames() != null && java.util.Arrays.binarySearch(this.getExcludeFieldNames(), /* getName */field.name) >= 0){
                return false;
            }
            return true;
        }

        /**
         * <p>
         * Appends the fields and values defined by the given object of the given Class.
         * </p>
         * 
         * <p>
         * If a cycle is detected as an object is &quot;toString()'ed&quot;, such an object is rendered as if
         * <code>Object.toString()</code> had been called and not implemented by the object.
         * </p>
         * 
         * @param {*} clazz
         * The class of object parameter
         */
        appendFieldsIn(clazz: any) {
            if (clazz.isArray()){
                this.reflectionAppendArray(this.getObject());
                return;
            }
            const fields: java.lang.reflect.Field[] = clazz.getDeclaredFields();
            java.lang.reflect.AccessibleObject.setAccessible(fields, true);
            for(let i: number = 0; i < fields.length; i++) {{
                const field: java.lang.reflect.Field = fields[i];
                const fieldName: string = /* getName */field.name;
                if (this.accept(field)){
                    try {
                        const fieldValue: any = this.getValue(field);
                        this.append$java_lang_String$java_lang_Object(fieldName, fieldValue);
                    } catch(ex) {
                        throw Object.defineProperty(new Error("Unexpected IllegalAccessException: " + ex.message), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.VirtualMachineError','java.lang.InternalError','java.lang.Error','java.lang.Object'] });
                    }
                }
            };}
        }

        /**
         * @return {java.lang.String[]} Returns the excludeFieldNames.
         */
        public getExcludeFieldNames(): string[] {
            return this.excludeFieldNames;
        }

        /**
         * <p>
         * Gets the last super class to stop appending fields for.
         * </p>
         * 
         * @return {*} The last super class to stop appending fields for.
         */
        public getUpToClass(): any {
            return this.upToClass;
        }

        /**
         * <p>
         * Calls <code>java.lang.reflect.Field.get(Object)</code>.
         * </p>
         * 
         * @param {java.lang.reflect.Field} field
         * The Field to query.
         * @return {*} The Object from the given Field.
         * 
         * @throws IllegalArgumentException
         * see {@link Field#get(Object)}
         * @throws IllegalAccessException
         * see {@link Field#get(Object)}
         * 
         * @see Field#get(Object)
         */
        getValue(field: java.lang.reflect.Field): any {
            return /* get */this.getObject()[field.name];
        }

        /**
         * <p>
         * Gets whether or not to append static fields.
         * </p>
         * 
         * @return {boolean} Whether or not to append static fields.
         * @since 2.1
         */
        public isAppendStatics(): boolean {
            return this.appendStatics;
        }

        /**
         * <p>
         * Gets whether or not to append transient fields.
         * </p>
         * 
         * @return {boolean} Whether or not to append transient fields.
         */
        public isAppendTransients(): boolean {
            return this.appendTransients;
        }

        /**
         * <p>
         * Append to the <code>toString</code> an <code>Object</code> array.
         * </p>
         * 
         * @param {*} array
         * the array to add to the <code>toString</code>
         * @return {org.openprovenance.apache.commons.lang.builder.ToStringBuilder} this
         */
        public reflectionAppendArray(array: any): org.openprovenance.apache.commons.lang.builder.ToStringBuilder {
            this.getStyle().reflectionAppendArrayDetail(this.getStringBuffer(), null, array);
            return this;
        }

        /**
         * <p>
         * Sets whether or not to append static fields.
         * </p>
         * 
         * @param {boolean} appendStatics
         * Whether or not to append static fields.
         * @since 2.1
         */
        public setAppendStatics(appendStatics: boolean) {
            this.appendStatics = appendStatics;
        }

        /**
         * <p>
         * Sets whether or not to append transient fields.
         * </p>
         * 
         * @param {boolean} appendTransients
         * Whether or not to append transient fields.
         */
        public setAppendTransients(appendTransients: boolean) {
            this.appendTransients = appendTransients;
        }

        /**
         * Sets the field names to exclude.
         * 
         * @param {java.lang.String[]} excludeFieldNamesParam
         * The excludeFieldNames to excluding from toString or <code>null</code>.
         * @return {org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder} <code>this</code>
         */
        public setExcludeFieldNames(excludeFieldNamesParam: string[]): ReflectionToStringBuilder {
            if (excludeFieldNamesParam == null){
                this.excludeFieldNames = null;
            } else {
                this.excludeFieldNames = ReflectionToStringBuilder.toNoNullStringArray$java_lang_Object_A(excludeFieldNamesParam);
                /* sort */((l) => {l.sort(); })(this.excludeFieldNames);
            }
            return this;
        }

        /**
         * <p>
         * Sets the last super class to stop appending fields for.
         * </p>
         * 
         * @param {*} clazz
         * The last super class to stop appending fields for.
         */
        public setUpToClass(clazz: any) {
            if (clazz != null){
                const object: any = this.getObject();
                if (object != null && /* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(clazz, object) === false){
                    throw Object.defineProperty(new Error("Specified class is not a superclass of the object"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                }
            }
            this.upToClass = clazz;
        }

        /**
         * <p>
         * Gets the String built by this builder.
         * </p>
         * 
         * @return {string} the built string
         */
        public toString(): string {
            if (this.getObject() == null){
                return this.getStyle().getNullText();
            }
            let clazz: any = (<any>this.getObject().constructor);
            this.appendFieldsIn(clazz);
            while((clazz.getSuperclass() != null && clazz !== this.getUpToClass())) {{
                clazz = clazz.getSuperclass();
                this.appendFieldsIn(clazz);
            }};
            return super.toString();
        }
    }
    ReflectionToStringBuilder["__class"] = "org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder";

}

