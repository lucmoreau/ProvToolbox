/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.builder {
    /**
     * <p>Constructor for EqualsBuilder.</p>
     * 
     * <p>Starts off assuming that equals is <code>true</code>.</p>
     * @see Object#equals(Object)
     * @class
     * @author Apache Software Foundation
     */
    export class EqualsBuilder {
        /**
         * If the fields tested are equals.
         * The default value is <code>true</code>.
         */
        /*private*/ __isEquals: boolean;

        public constructor() {
            this.__isEquals = true;
        }

        public static reflectionEquals$java_lang_Object$java_lang_Object(lhs: any, rhs: any): boolean {
            return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, false, null, null);
        }

        public static reflectionEquals$java_lang_Object$java_lang_Object$java_util_Collection(lhs: any, rhs: any, excludeFields: Array<any>): boolean {
            return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$java_lang_String_A(lhs, rhs, org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toNoNullStringArray$java_util_Collection(excludeFields));
        }

        public static reflectionEquals$java_lang_Object$java_lang_Object$java_lang_String_A(lhs: any, rhs: any, excludeFields: string[]): boolean {
            return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, false, null, excludeFields);
        }

        public static reflectionEquals$java_lang_Object$java_lang_Object$boolean(lhs: any, rhs: any, testTransients: boolean): boolean {
            return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, null, null);
        }

        public static reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class(lhs: any, rhs: any, testTransients: boolean, reflectUpToClass: any): boolean {
            return EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, reflectUpToClass, null);
        }

        public static reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs: any, rhs: any, testTransients: boolean, reflectUpToClass: any, excludeFields: string[]): boolean {
            if (lhs === rhs){
                return true;
            }
            if (lhs == null || rhs == null){
                return false;
            }
            const lhsClass: any = (<any>lhs.constructor);
            const rhsClass: any = (<any>rhs.constructor);
            let testClass: any;
            if (/* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(lhsClass, rhs)){
                testClass = lhsClass;
                if (!/* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(rhsClass, lhs)){
                    testClass = rhsClass;
                }
            } else if (/* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(rhsClass, lhs)){
                testClass = rhsClass;
                if (!/* isInstance */((c:any,o:any) => { if(typeof c === 'string') return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0); else if(typeof c === 'function') return (o instanceof c) || (o.constructor && o.constructor === c); })(lhsClass, rhs)){
                    testClass = lhsClass;
                }
            } else {
                return false;
            }
            const equalsBuilder: EqualsBuilder = new EqualsBuilder();
            try {
                EqualsBuilder.reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                while((testClass.getSuperclass() != null && testClass !== reflectUpToClass)) {{
                    testClass = testClass.getSuperclass();
                    EqualsBuilder.reflectionAppend(lhs, rhs, testClass, equalsBuilder, testTransients, excludeFields);
                }};
            } catch(e) {
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
        public static reflectionEquals(lhs?: any, rhs?: any, testTransients?: any, reflectUpToClass?: any, excludeFields?: any): any {
            if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null) && ((excludeFields != null && excludeFields instanceof <any>Array && (excludeFields.length == 0 || excludeFields[0] == null ||(typeof excludeFields[0] === 'string'))) || excludeFields === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(lhs, rhs, testTransients, reflectUpToClass, excludeFields);
            } else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null) && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean$java_lang_Class(lhs, rhs, testTransients, reflectUpToClass);
            } else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((testTransients != null && (testTransients instanceof Array)) || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$java_util_Collection(lhs, rhs, testTransients);
            } else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((testTransients != null && testTransients instanceof <any>Array && (testTransients.length == 0 || testTransients[0] == null ||(typeof testTransients[0] === 'string'))) || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$java_lang_String_A(lhs, rhs, testTransients);
            } else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && ((typeof testTransients === 'boolean') || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object$boolean(lhs, rhs, testTransients);
            } else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null) && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder.reflectionEquals$java_lang_Object$java_lang_Object(lhs, rhs);
            } else throw new Error('invalid overload');
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
        /*private*/ static reflectionAppend(lhs: any, rhs: any, clazz: any, builder: EqualsBuilder, useTransients: boolean, excludeFields: string[]) {
            const fields: java.lang.reflect.Field[] = clazz.getDeclaredFields();
            java.lang.reflect.AccessibleObject.setAccessible(fields, true);
            for(let i: number = 0; i < fields.length && builder.__isEquals; i++) {{
                const f: java.lang.reflect.Field = fields[i];
                if (!org.openprovenance.apache.commons.lang.ArrayUtils.contains$java_lang_Object_A$java_lang_Object(excludeFields, /* getName */f.name) && (/* getName */f.name.indexOf('$') === -1) && (useTransients || !java.lang.reflect.Modifier.isTransient(f.getModifiers())) && (!java.lang.reflect.Modifier.isStatic(f.getModifiers()))){
                    try {
                        builder.append$java_lang_Object$java_lang_Object(/* get */lhs[f.name], /* get */rhs[f.name]);
                    } catch(e) {
                        throw Object.defineProperty(new Error("Unexpected IllegalAccessException"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.VirtualMachineError','java.lang.InternalError','java.lang.Error','java.lang.Object'] });
                    }
                }
            };}
        }

        /**
         * <p>Adds the result of <code>super.equals()</code> to this builder.</p>
         * 
         * @param {boolean} superEquals  the result of calling <code>super.equals()</code>
         * @return {org.openprovenance.apache.commons.lang.builder.EqualsBuilder} EqualsBuilder - used to chain calls.
         * @since 2.0
         */
        public appendSuper(superEquals: boolean): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = superEquals;
            return this;
        }

        public append$java_lang_Object$java_lang_Object(lhs: any, rhs: any): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            const lhsClass: any = (<any>lhs.constructor);
            if (!lhsClass.isArray()){
                this.__isEquals = /* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(lhs,rhs));
            } else if ((<any>lhs.constructor) !== (<any>rhs.constructor)){
                this.setEquals(false);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'number')){
                this.append$long_A$long_A(<number[]>lhs, <number[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'number')){
                this.append$int_A$int_A(<number[]>lhs, <number[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'number')){
                this.append$short_A$short_A(<number[]>lhs, <number[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'string')){
                this.append$char_A$char_A(<string[]>lhs, <string[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'number')){
                this.append$byte_A$byte_A(<number[]>lhs, <number[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'number')){
                this.append$double_A$double_A(<number[]>lhs, <number[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'number')){
                this.append$float_A$float_A(<number[]>lhs, <number[]>rhs);
            } else if (lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||typeof lhs[0] === 'boolean')){
                this.append$boolean_A$boolean_A(<boolean[]>lhs, <boolean[]>rhs);
            } else {
                this.append$java_lang_Object_A$java_lang_Object_A(<any[]>lhs, <any[]>rhs);
            }
            return this;
        }

        public append$long$long(lhs: number, rhs: number): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = (lhs === rhs);
            return this;
        }

        public append$int$int(lhs: number, rhs: number): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = (lhs === rhs);
            return this;
        }

        public append$short$short(lhs: number, rhs: number): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = (lhs === rhs);
            return this;
        }

        public append$char$char(lhs: string, rhs: string): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(lhs) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(rhs));
            return this;
        }

        public append$byte$byte(lhs: number, rhs: number): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = (lhs === rhs);
            return this;
        }

        public append$double$double(lhs: number, rhs: number): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            return this.append$long$long(/* doubleToLongBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })((<any>Math).fround(lhs)), /* doubleToLongBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })((<any>Math).fround(rhs)));
        }

        public append$float$float(lhs: number, rhs: number): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            return this.append$int$int(/* floatToIntBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })(lhs), /* floatToIntBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })(rhs));
        }

        public append$boolean$boolean(lhs: boolean, rhs: boolean): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            this.__isEquals = (lhs === rhs);
            return this;
        }

        public append$java_lang_Object_A$java_lang_Object_A(lhs: any[], rhs: any[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$java_lang_Object$java_lang_Object(lhs[i], rhs[i]);
            };}
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
        public append(lhs?: any, rhs?: any): any {
            if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(lhs[0] != null))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(rhs[0] != null))) || rhs === null)) {
                return <any>this.append$java_lang_Object_A$java_lang_Object_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'number'))) || rhs === null)) {
                return <any>this.append$long_A$long_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'number'))) || rhs === null)) {
                return <any>this.append$int_A$int_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'number'))) || rhs === null)) {
                return <any>this.append$short_A$short_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'string'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'string'))) || rhs === null)) {
                return <any>this.append$char_A$char_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'number'))) || rhs === null)) {
                return <any>this.append$byte_A$byte_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'number'))) || rhs === null)) {
                return <any>this.append$double_A$double_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'number'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'number'))) || rhs === null)) {
                return <any>this.append$float_A$float_A(lhs, rhs);
            } else if (((lhs != null && lhs instanceof <any>Array && (lhs.length == 0 || lhs[0] == null ||(typeof lhs[0] === 'boolean'))) || lhs === null) && ((rhs != null && rhs instanceof <any>Array && (rhs.length == 0 || rhs[0] == null ||(typeof rhs[0] === 'boolean'))) || rhs === null)) {
                return <any>this.append$boolean_A$boolean_A(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>this.append$short$short(lhs, rhs);
            } else if (((typeof lhs === 'string') || lhs === null) && ((typeof rhs === 'string') || rhs === null)) {
                return <any>this.append$char$char(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>this.append$byte$byte(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>this.append$int$int(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>this.append$long$long(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>this.append$float$float(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>this.append$double$double(lhs, rhs);
            } else if (((typeof lhs === 'boolean') || lhs === null) && ((typeof rhs === 'boolean') || rhs === null)) {
                return <any>this.append$boolean$boolean(lhs, rhs);
            } else if (((lhs != null) || lhs === null) && ((rhs != null) || rhs === null)) {
                return <any>this.append$java_lang_Object$java_lang_Object(lhs, rhs);
            } else throw new Error('invalid overload');
        }

        public append$long_A$long_A(lhs: number[], rhs: number[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$long$long(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$int_A$int_A(lhs: number[], rhs: number[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$int$int(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$short_A$short_A(lhs: number[], rhs: number[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$short$short(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$char_A$char_A(lhs: string[], rhs: string[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$char$char(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$byte_A$byte_A(lhs: number[], rhs: number[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$byte$byte(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$double_A$double_A(lhs: number[], rhs: number[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$double$double(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$float_A$float_A(lhs: number[], rhs: number[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$float$float(lhs[i], rhs[i]);
            };}
            return this;
        }

        public append$boolean_A$boolean_A(lhs: boolean[], rhs: boolean[]): EqualsBuilder {
            if (this.__isEquals === false){
                return this;
            }
            if (lhs === rhs){
                return this;
            }
            if (lhs == null || rhs == null){
                this.setEquals(false);
                return this;
            }
            if (lhs.length !== rhs.length){
                this.setEquals(false);
                return this;
            }
            for(let i: number = 0; i < lhs.length && this.__isEquals; ++i) {{
                this.append$boolean$boolean(lhs[i], rhs[i]);
            };}
            return this;
        }

        /**
         * <p>Returns <code>true</code> if the fields that have been checked
         * are all equal.</p>
         * 
         * @return {boolean} boolean
         */
        public isEquals(): boolean {
            return this.__isEquals;
        }

        /**
         * Sets the <code>isEquals</code> value.
         * 
         * @param {boolean} isEquals The value to set.
         * @since 2.1
         */
        setEquals(isEquals: boolean) {
            this.__isEquals = isEquals;
        }

        /**
         * Reset the EqualsBuilder so you can use the same object again
         * @since 2.5
         */
        public reset() {
            this.__isEquals = true;
        }
    }
    EqualsBuilder["__class"] = "org.openprovenance.apache.commons.lang.builder.EqualsBuilder";

}

