/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.builder {
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
    export class HashCodeBuilder {
        /**
         * <p>
         * A registry of objects used by reflection methods to detect cyclical object references and avoid infinite loops.
         * </p>
         * 
         * @since 2.3
         */
        static REGISTRY: java.lang.ThreadLocal<any>; public static REGISTRY_$LI$(): java.lang.ThreadLocal<any> { if (HashCodeBuilder.REGISTRY == null) { HashCodeBuilder.REGISTRY = <any>(new java.lang.ThreadLocal()); }  return HashCodeBuilder.REGISTRY; }

        /**
         * <p>
         * Returns the registry of objects being traversed by the reflection methods in the current thread.
         * </p>
         * 
         * @return {Array} Set the registry of objects being traversed
         * @since 2.3
         */
        static getRegistry(): Array<any> {
            return <Array<any>><any>/* get */((tlObj: any) => {    if (tlObj.___value) { return tlObj.___value }     else { return tlObj.___value = tlObj.initialValue() }   })(HashCodeBuilder.REGISTRY_$LI$());
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
        static isRegistered(value: any): boolean {
            const registry: Array<any> = HashCodeBuilder.getRegistry();
            return registry != null && /* contains */(registry.indexOf(<any>(new org.openprovenance.apache.commons.lang.builder.IDKey(value))) >= 0);
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
        /*private*/ static reflectionAppend(object: any, clazz: any, builder: HashCodeBuilder, useTransients: boolean, excludeFields: string[]) {
            if (HashCodeBuilder.isRegistered(object)){
                return;
            }
            try {
                HashCodeBuilder.register(object);
                const fields: java.lang.reflect.Field[] = clazz.getDeclaredFields();
                java.lang.reflect.AccessibleObject.setAccessible(fields, true);
                for(let i: number = 0; i < fields.length; i++) {{
                    const field: java.lang.reflect.Field = fields[i];
                    if (!org.openprovenance.apache.commons.lang.ArrayUtils.contains$java_lang_Object_A$java_lang_Object(excludeFields, /* getName */field.name) && (/* getName */field.name.indexOf('$') === -1) && (useTransients || !java.lang.reflect.Modifier.isTransient(field.getModifiers())) && (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))){
                        try {
                            const fieldValue: any = /* get */object[field.name];
                            builder.append$java_lang_Object(fieldValue);
                        } catch(e) {
                            throw Object.defineProperty(new Error("Unexpected IllegalAccessException"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.VirtualMachineError','java.lang.InternalError','java.lang.Error','java.lang.Object'] });
                        }
                    }
                };}
            } finally {
                HashCodeBuilder.unregister(object);
            }
        }

        public static reflectionHashCode$int$int$java_lang_Object(initialNonZeroOddNumber: number, multiplierNonZeroOddNumber: number, object: any): number {
            return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null, null);
        }

        public static reflectionHashCode$int$int$java_lang_Object$boolean(initialNonZeroOddNumber: number, multiplierNonZeroOddNumber: number, object: any, testTransients: boolean): number {
            return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null, null);
        }

        public static reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class(initialNonZeroOddNumber: number, multiplierNonZeroOddNumber: number, object: any, testTransients: boolean, reflectUpToClass: any): number {
            return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, null);
        }

        public static reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber: number, multiplierNonZeroOddNumber: number, object: any, testTransients: boolean, reflectUpToClass: any, excludeFields: string[]): number {
            if (object == null){
                throw Object.defineProperty(new Error("The object to build a hash code for must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            const builder: HashCodeBuilder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            let clazz: any = (<any>object.constructor);
            HashCodeBuilder.reflectionAppend(object, clazz, builder, testTransients, excludeFields);
            while((clazz.getSuperclass() != null && clazz !== reflectUpToClass)) {{
                clazz = clazz.getSuperclass();
                HashCodeBuilder.reflectionAppend(object, clazz, builder, testTransients, excludeFields);
            }};
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
        public static reflectionHashCode(initialNonZeroOddNumber?: any, multiplierNonZeroOddNumber?: any, object?: any, testTransients?: any, reflectUpToClass?: any, excludeFields?: any): any {
            if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null) && ((excludeFields != null && excludeFields instanceof <any>Array && (excludeFields.length == 0 || excludeFields[0] == null ||(typeof excludeFields[0] === 'string'))) || excludeFields === null)) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass, excludeFields);
            } else if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && ((typeof testTransients === 'boolean') || testTransients === null) && ((reflectUpToClass != null && (reflectUpToClass["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(reflectUpToClass))) || reflectUpToClass === null) && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, reflectUpToClass);
            } else if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && ((typeof testTransients === 'boolean') || testTransients === null) && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients);
            } else if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null) && ((object != null) || object === null) && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object);
            } else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && ((multiplierNonZeroOddNumber != null && (multiplierNonZeroOddNumber instanceof Array)) || multiplierNonZeroOddNumber === null) && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object$java_util_Collection(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            } else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && ((multiplierNonZeroOddNumber != null && multiplierNonZeroOddNumber instanceof <any>Array && (multiplierNonZeroOddNumber.length == 0 || multiplierNonZeroOddNumber[0] == null ||(typeof multiplierNonZeroOddNumber[0] === 'string'))) || multiplierNonZeroOddNumber === null) && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object$java_lang_String_A(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            } else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'boolean') || multiplierNonZeroOddNumber === null) && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object$boolean(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            } else if (((initialNonZeroOddNumber != null) || initialNonZeroOddNumber === null) && multiplierNonZeroOddNumber === undefined && object === undefined && testTransients === undefined && reflectUpToClass === undefined && excludeFields === undefined) {
                return <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode$java_lang_Object(initialNonZeroOddNumber);
            } else throw new Error('invalid overload');
        }

        public static reflectionHashCode$java_lang_Object(object: any): number {
            return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(17, 37, object, false, null, null);
        }

        public static reflectionHashCode$java_lang_Object$boolean(object: any, testTransients: boolean): number {
            return HashCodeBuilder.reflectionHashCode$int$int$java_lang_Object$boolean$java_lang_Class$java_lang_String_A(17, 37, object, testTransients, null, null);
        }

        public static reflectionHashCode$java_lang_Object$java_util_Collection(object: any, excludeFields: Array<any>): number {
            return HashCodeBuilder.reflectionHashCode$java_lang_Object$java_lang_String_A(object, org.openprovenance.apache.commons.lang.builder.ReflectionToStringBuilder.toNoNullStringArray$java_util_Collection(excludeFields));
        }

        public static reflectionHashCode$java_lang_Object$java_lang_String_A(object: any, excludeFields: string[]): number {
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
        static register(value: any) {
            {
                if (HashCodeBuilder.getRegistry() == null){
                    HashCodeBuilder.REGISTRY_$LI$().set(<any>([]));
                }
            };
            /* add */((s, e) => { if(s.indexOf(e)==-1) { s.push(e); return true; } else { return false; } })(HashCodeBuilder.getRegistry(), new org.openprovenance.apache.commons.lang.builder.IDKey(value));
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
        static unregister(value: any) {
            let registry: Array<any> = HashCodeBuilder.getRegistry();
            if (registry != null){
                /* remove */(a => { let index = a.indexOf(new org.openprovenance.apache.commons.lang.builder.IDKey(value)); if(index>=0) { a.splice(index, 1); return true; } else { return false; }})(registry);
                {
                    registry = HashCodeBuilder.getRegistry();
                    if (registry != null && /* isEmpty */(registry.length == 0)){
                        HashCodeBuilder.REGISTRY_$LI$().set(null);
                    }
                };
            }
        }

        /**
         * Constant to use in building the hashCode.
         */
        /*private*/ iConstant: number;

        /**
         * Running total of the hashCode.
         */
        /*private*/ iTotal: number;

        public constructor(initialNonZeroOddNumber?: any, multiplierNonZeroOddNumber?: any) {
            if (((typeof initialNonZeroOddNumber === 'number') || initialNonZeroOddNumber === null) && ((typeof multiplierNonZeroOddNumber === 'number') || multiplierNonZeroOddNumber === null)) {
                let __args = arguments;
                if (this.iConstant === undefined) { this.iConstant = 0; } 
                this.iTotal = 0;
                if (initialNonZeroOddNumber === 0){
                    throw Object.defineProperty(new Error("HashCodeBuilder requires a non zero initial value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                }
                if (initialNonZeroOddNumber % 2 === 0){
                    throw Object.defineProperty(new Error("HashCodeBuilder requires an odd initial value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                }
                if (multiplierNonZeroOddNumber === 0){
                    throw Object.defineProperty(new Error("HashCodeBuilder requires a non zero multiplier"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                }
                if (multiplierNonZeroOddNumber % 2 === 0){
                    throw Object.defineProperty(new Error("HashCodeBuilder requires an odd multiplier"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                }
                this.iConstant = multiplierNonZeroOddNumber;
                this.iTotal = initialNonZeroOddNumber;
            } else if (initialNonZeroOddNumber === undefined && multiplierNonZeroOddNumber === undefined) {
                let __args = arguments;
                if (this.iConstant === undefined) { this.iConstant = 0; } 
                this.iTotal = 0;
                this.iConstant = 37;
                this.iTotal = 17;
            } else throw new Error('invalid overload');
        }

        public append$boolean(value: boolean): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + (value ? 0 : 1);
            return this;
        }

        public append$boolean_A(array: boolean[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$boolean(array[i]);
                };}
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
        public append(array?: any): any {
            if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'boolean'))) || array === null)) {
                return <any>this.append$boolean_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.append$byte_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'string'))) || array === null)) {
                return <any>this.append$char_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.append$double_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.append$float_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.append$int_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.append$long_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(array[0] != null))) || array === null)) {
                return <any>this.append$java_lang_Object_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.append$short_A(array);
            } else if (((typeof array === 'boolean') || array === null)) {
                return <any>this.append$boolean(array);
            } else if (((typeof array === 'number') || array === null)) {
                return <any>this.append$byte(array);
            } else if (((typeof array === 'string') || array === null)) {
                return <any>this.append$char(array);
            } else if (((typeof array === 'number') || array === null)) {
                return <any>this.append$short(array);
            } else if (((typeof array === 'number') || array === null)) {
                return <any>this.append$int(array);
            } else if (((typeof array === 'number') || array === null)) {
                return <any>this.append$long(array);
            } else if (((typeof array === 'number') || array === null)) {
                return <any>this.append$float(array);
            } else if (((typeof array === 'number') || array === null)) {
                return <any>this.append$double(array);
            } else if (((array != null) || array === null)) {
                return <any>this.append$java_lang_Object(array);
            } else throw new Error('invalid overload');
        }

        public append$byte(value: number): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + value;
            return this;
        }

        public append$byte_A(array: number[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$byte(array[i]);
                };}
            }
            return this;
        }

        public append$char(value: string): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(value);
            return this;
        }

        public append$char_A(array: string[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$char(array[i]);
                };}
            }
            return this;
        }

        public append$double(value: number): HashCodeBuilder {
            return this.append$long(/* doubleToLongBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })((<any>Math).fround(value)));
        }

        public append$double_A(array: number[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$double(array[i]);
                };}
            }
            return this;
        }

        public append$float(value: number): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + /* floatToIntBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })(value);
            return this;
        }

        public append$float_A(array: number[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$float(array[i]);
                };}
            }
            return this;
        }

        public append$int(value: number): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + value;
            return this;
        }

        public append$int_A(array: number[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$int(array[i]);
                };}
            }
            return this;
        }

        public append$long(value: number): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + ((<number>(value ^ (value >> 32))|0));
            return this;
        }

        public append$long_A(array: number[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$long(array[i]);
                };}
            }
            return this;
        }

        public append$java_lang_Object(object: any): HashCodeBuilder {
            if (object == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                if ((<any>object.constructor).isArray()){
                    if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'number')){
                        this.append$long_A(<number[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'number')){
                        this.append$int_A(<number[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'number')){
                        this.append$short_A(<number[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'string')){
                        this.append$char_A(<string[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'number')){
                        this.append$byte_A(<number[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'number')){
                        this.append$double_A(<number[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'number')){
                        this.append$float_A(<number[]>object);
                    } else if (object != null && object instanceof <any>Array && (object.length == 0 || object[0] == null ||typeof object[0] === 'boolean')){
                        this.append$boolean_A(<boolean[]>object);
                    } else {
                        this.append$java_lang_Object_A(<any[]>object);
                    }
                } else {
                    this.iTotal = this.iTotal * this.iConstant + /* hashCode */(<any>((o: any) => { if (o.hashCode) { return o.hashCode(); } else { return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0))|0, 0); }})(object));
                }
            }
            return this;
        }

        public append$java_lang_Object_A(array: any[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$java_lang_Object(array[i]);
                };}
            }
            return this;
        }

        public append$short(value: number): HashCodeBuilder {
            this.iTotal = this.iTotal * this.iConstant + value;
            return this;
        }

        public append$short_A(array: number[]): HashCodeBuilder {
            if (array == null){
                this.iTotal = this.iTotal * this.iConstant;
            } else {
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$short(array[i]);
                };}
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
        public appendSuper(superHashCode: number): HashCodeBuilder {
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
        public toHashCode(): number {
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
        public hashCode(): number {
            return this.toHashCode();
        }
    }
    HashCodeBuilder["__class"] = "org.openprovenance.apache.commons.lang.builder.HashCodeBuilder";

}

