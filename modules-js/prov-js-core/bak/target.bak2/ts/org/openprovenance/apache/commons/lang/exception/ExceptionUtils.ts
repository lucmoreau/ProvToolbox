/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.exception {
    /**
     * <p>
     * Public constructor allows an instance of <code>ExceptionUtils</code> to be created, although that is not
     * normally necessary.
     * </p>
     * @class
     * @author Apache Software Foundation
     */
    export class ExceptionUtils {
        static __static_initialized: boolean = false;
        static __static_initialize() { if (!ExceptionUtils.__static_initialized) { ExceptionUtils.__static_initialized = true; ExceptionUtils.__static_initializer_0(); } }

        /**
         * <p>Used when printing stack frames to denote the start of a
         * wrapped exception.</p>
         * 
         * <p>Package private for accessibility by test suite.</p>
         */
        static WRAPPED_MARKER: string = " [wrapped] ";

        static CAUSE_METHOD_NAMES_LOCK: any; public static CAUSE_METHOD_NAMES_LOCK_$LI$(): any { ExceptionUtils.__static_initialize(); if (ExceptionUtils.CAUSE_METHOD_NAMES_LOCK == null) { ExceptionUtils.CAUSE_METHOD_NAMES_LOCK = <any>new Object(); }  return ExceptionUtils.CAUSE_METHOD_NAMES_LOCK; }

        /**
         * <p>The names of methods commonly used to access a wrapped exception.</p>
         */
        static CAUSE_METHOD_NAMES: string[]; public static CAUSE_METHOD_NAMES_$LI$(): string[] { ExceptionUtils.__static_initialize(); if (ExceptionUtils.CAUSE_METHOD_NAMES == null) { ExceptionUtils.CAUSE_METHOD_NAMES = ["getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"]; }  return ExceptionUtils.CAUSE_METHOD_NAMES; }

        /**
         * <p>The Method object for Java 1.4 getCause.</p>
         */
        static THROWABLE_CAUSE_METHOD: { owner: any, name: string, fn : Function }; public static THROWABLE_CAUSE_METHOD_$LI$(): { owner: any, name: string, fn : Function } { ExceptionUtils.__static_initialize();  return ExceptionUtils.THROWABLE_CAUSE_METHOD; }

        /**
         * <p>The Method object for Java 1.4 initCause.</p>
         */
        static THROWABLE_INITCAUSE_METHOD: { owner: any, name: string, fn : Function }; public static THROWABLE_INITCAUSE_METHOD_$LI$(): { owner: any, name: string, fn : Function } { ExceptionUtils.__static_initialize();  return ExceptionUtils.THROWABLE_INITCAUSE_METHOD; }

        static  __static_initializer_0() {
            let causeMethod: { owner: any, name: string, fn : Function };
            try {
                causeMethod = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })("java.lang.Throwable","getCause");
            } catch(e) {
                causeMethod = null;
            }
            ExceptionUtils.THROWABLE_CAUSE_METHOD = causeMethod;
            try {
                causeMethod = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })("java.lang.Throwable","initCause");
            } catch(e) {
                causeMethod = null;
            }
            ExceptionUtils.THROWABLE_INITCAUSE_METHOD = causeMethod;
        }

        public constructor() {
        }

        /**
         * <p>Adds to the list of method names used in the search for <code>Throwable</code>
         * objects.</p>
         * 
         * @param {string} methodName  the methodName to add to the list, <code>null</code>
         * and empty strings are ignored
         * @since 2.0
         */
        public static addCauseMethodName(methodName: string) {
            if (org.openprovenance.apache.commons.lang.StringUtils.isNotEmpty(methodName) && !ExceptionUtils.isCauseMethodName(methodName)){
                const list: Array<any> = ExceptionUtils.getCauseMethodNameList();
                if (/* add */(list.push(methodName)>0)){
                    {
                        ExceptionUtils.CAUSE_METHOD_NAMES = ExceptionUtils.toArray(list);
                    };
                }
            }
        }

        /**
         * <p>Removes from the list of method names used in the search for <code>Throwable</code>
         * objects.</p>
         * 
         * @param {string} methodName  the methodName to remove from the list, <code>null</code>
         * and empty strings are ignored
         * @since 2.1
         */
        public static removeCauseMethodName(methodName: string) {
            if (org.openprovenance.apache.commons.lang.StringUtils.isNotEmpty(methodName)){
                const list: Array<any> = ExceptionUtils.getCauseMethodNameList();
                if (/* remove */(a => { let index = a.indexOf(methodName); if(index>=0) { a.splice(index, 1); return true; } else { return false; }})(list)){
                    {
                        ExceptionUtils.CAUSE_METHOD_NAMES = ExceptionUtils.toArray(list);
                    };
                }
            }
        }

        /**
         * <p>Sets the cause of a <code>Throwable</code> using introspection, allowing
         * source code compatibility between pre-1.4 and post-1.4 Java releases.</p>
         * 
         * <p>The typical use of this method is inside a constructor as in
         * the following example:</p>
         * 
         * <pre>
         * import org.apache.commons.lang.exception.ExceptionUtils;
         * 
         * public class MyException extends Exception {
         * 
         * public MyException(String msg) {
         * super(msg);
         * }
         * 
         * public MyException(String msg, Throwable cause) {
         * super(msg);
         * ExceptionUtils.setCause(this, cause);
         * }
         * }
         * </pre>
         * 
         * @param {Error} target  the target <code>Throwable</code>
         * @param {Error} cause  the <code>Throwable</code> to set in the target
         * @return {boolean} a <code>true</code> if the target has been modified
         * @since 2.2
         */
        public static setCause(target: Error, cause: Error): boolean {
            if (target == null){
                throw new org.openprovenance.apache.commons.lang.NullArgumentException("target");
            }
            const causeArgs: any[] = [cause];
            let modifiedTarget: boolean = false;
            if (ExceptionUtils.THROWABLE_INITCAUSE_METHOD_$LI$() != null){
                try {
                    /* invoke */ExceptionUtils.THROWABLE_INITCAUSE_METHOD_$LI$().fn.apply(target, [causeArgs]);
                    modifiedTarget = true;
                } catch(__e) {
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalAccessException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.reflect.InvocationTargetException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                }
            }
            try {
                const setCauseMethod: { owner: any, name: string, fn : Function } = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })((<any>target.constructor),"setCause");
                /* invoke */setCauseMethod.fn.apply(target, [causeArgs]);
                modifiedTarget = true;
            } catch(__e) {
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.NoSuchMethodException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalAccessException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.reflect.InvocationTargetException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
            }
            return modifiedTarget;
        }

        /**
         * Returns the given list as a <code>String[]</code>.
         * @param {Array} list a list to transform.
         * @return {java.lang.String[]} the given list as a <code>String[]</code>.
         * @private
         */
        /*private*/ static toArray(list: Array<any>): string[] {
            return <string[]>/* toArray */list.slice(0);
        }

        /**
         * Returns {@link #CAUSE_METHOD_NAMES} as a List.
         * 
         * @return {Array} {@link #CAUSE_METHOD_NAMES} as a List.
         * @private
         */
        /*private*/ static getCauseMethodNameList(): Array<any> {
            {
                return <any>(/* asList */ExceptionUtils.CAUSE_METHOD_NAMES_$LI$().slice(0).slice(0));
            };
        }

        /**
         * <p>Tests if the list of method names used in the search for <code>Throwable</code>
         * objects include the given name.</p>
         * 
         * @param {string} methodName  the methodName to search in the list.
         * @return {boolean} if the list of method names used in the search for <code>Throwable</code>
         * objects include the given name.
         * @since 2.1
         */
        public static isCauseMethodName(methodName: string): boolean {
            {
                return org.openprovenance.apache.commons.lang.ArrayUtils.indexOf$java_lang_Object_A$java_lang_Object(ExceptionUtils.CAUSE_METHOD_NAMES_$LI$(), methodName) >= 0;
            };
        }

        public static getCause$java_lang_Throwable(throwable: Error): Error {
            {
                return ExceptionUtils.getCause$java_lang_Throwable$java_lang_String_A(throwable, ExceptionUtils.CAUSE_METHOD_NAMES_$LI$());
            };
        }

        public static getCause$java_lang_Throwable$java_lang_String_A(throwable: Error, methodNames: string[]): Error {
            if (throwable == null){
                return null;
            }
            let cause: Error = ExceptionUtils.getCauseUsingWellKnownTypes(throwable);
            if (cause == null){
                if (methodNames == null){
                    {
                        methodNames = ExceptionUtils.CAUSE_METHOD_NAMES_$LI$();
                    };
                }
                for(let i: number = 0; i < methodNames.length; i++) {{
                    const methodName: string = methodNames[i];
                    if (methodName != null){
                        cause = ExceptionUtils.getCauseUsingMethodName(throwable, methodName);
                        if (cause != null){
                            break;
                        }
                    }
                };}
                if (cause == null){
                    cause = ExceptionUtils.getCauseUsingFieldName(throwable, "detail");
                }
            }
            return cause;
        }

        /**
         * <p>Introspects the <code>Throwable</code> to obtain the cause.</p>
         * 
         * <ol>
         * <li>Try known exception types.</li>
         * <li>Try the supplied array of method names.</li>
         * <li>Try the field 'detail'.</li>
         * </ol>
         * 
         * <p>A <code>null</code> set of method names means use the default set.
         * A <code>null</code> in the set of method names will be ignored.</p>
         * 
         * @param {Error} throwable  the throwable to introspect for a cause, may be null
         * @param {java.lang.String[]} methodNames  the method names, null treated as default set
         * @return {Error} the cause of the <code>Throwable</code>,
         * <code>null</code> if none found or null throwable input
         * @since 1.0
         */
        public static getCause(throwable?: any, methodNames?: any): any {
            if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((methodNames != null && methodNames instanceof <any>Array && (methodNames.length == 0 || methodNames[0] == null ||(typeof methodNames[0] === 'string'))) || methodNames === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getCause$java_lang_Throwable$java_lang_String_A(throwable, methodNames);
            } else if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && methodNames === undefined) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getCause$java_lang_Throwable(throwable);
            } else throw new Error('invalid overload');
        }

        /**
         * <p>Introspects the <code>Throwable</code> to obtain the root cause.</p>
         * 
         * <p>This method walks through the exception chain to the last element,
         * "root" of the tree, using {@link #getCause(Throwable)}, and
         * returns that exception.</p>
         * 
         * <p>From version 2.2, this method handles recursive cause structures
         * that might otherwise cause infinite loops. If the throwable parameter
         * has a cause of itself, then null will be returned. If the throwable
         * parameter cause chain loops, the last element in the chain before the
         * loop is returned.</p>
         * 
         * @param {Error} throwable  the throwable to get the root cause for, may be null
         * @return {Error} the root cause of the <code>Throwable</code>,
         * <code>null</code> if none found or null throwable input
         */
        public static getRootCause(throwable: Error): Error {
            const list: Array<any> = ExceptionUtils.getThrowableList(throwable);
            return (/* size */(<number>list.length) < 2 ? null : <Error>/* get */list[/* size */(<number>list.length) - 1]);
        }

        /**
         * <p>Finds a <code>Throwable</code> for known types.</p>
         * 
         * <p>Uses <code>instanceof</code> checks to examine the exception,
         * looking for well known types which could contain chained or
         * wrapped exceptions.</p>
         * 
         * @param {Error} throwable  the exception to examine
         * @return {Error} the wrapped exception, or <code>null</code> if not found
         * @private
         */
        /*private*/ static getCauseUsingWellKnownTypes(throwable: Error): Error {
            if (throwable != null && (throwable.constructor != null && throwable.constructor["__interfaces"] != null && throwable.constructor["__interfaces"].indexOf("org.openprovenance.apache.commons.lang.exception.Nestable") >= 0)){
                return (<org.openprovenance.apache.commons.lang.exception.Nestable><any>throwable).getCause();
            } else if (throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.sql.SQLException") >= 0)){
                return (<Error>throwable).getNextException();
            } else if (throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.reflect.InvocationTargetException") >= 0)){
                return (<Error>throwable).getTargetException();
            } else {
                return null;
            }
        }

        /**
         * <p>Finds a <code>Throwable</code> by method name.</p>
         * 
         * @param {Error} throwable  the exception to examine
         * @param {string} methodName  the name of the method to find and invoke
         * @return {Error} the wrapped exception, or <code>null</code> if not found
         * @private
         */
        /*private*/ static getCauseUsingMethodName(throwable: Error, methodName: string): Error {
            let method: { owner: any, name: string, fn : Function } = null;
            try {
                method = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })((<any>throwable.constructor),methodName);
            } catch(__e) {
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.NoSuchMethodException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.SecurityException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
            }
            if (method != null && "java.lang.Throwable".isAssignableFrom(method.getReturnType())){
                try {
                    return <Error>/* invoke */method.fn.apply(throwable, [org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$()]);
                } catch(__e) {
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalAccessException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalArgumentException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.reflect.InvocationTargetException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                }
            }
            return null;
        }

        /**
         * <p>Finds a <code>Throwable</code> by field name.</p>
         * 
         * @param {Error} throwable  the exception to examine
         * @param {string} fieldName  the name of the attribute to examine
         * @return {Error} the wrapped exception, or <code>null</code> if not found
         * @private
         */
        /*private*/ static getCauseUsingFieldName(throwable: Error, fieldName: string): Error {
            let field: java.lang.reflect.Field = null;
            try {
                field = /* getField */((c,p) => { return {owner:c,name:p}; })((<any>throwable.constructor),fieldName);
            } catch(__e) {
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.NoSuchFieldException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.SecurityException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
            }
            if (field != null && "java.lang.Throwable".isAssignableFrom(field.getType())){
                try {
                    return <Error>/* get */throwable[field.name];
                } catch(__e) {
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalAccessException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                    if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.IllegalArgumentException") >= 0)) {
                        const ignored: Error = <Error>__e;

                    }
                }
            }
            return null;
        }

        /**
         * <p>Checks if the Throwable class has a <code>getCause</code> method.</p>
         * 
         * <p>This is true for JDK 1.4 and above.</p>
         * 
         * @return {boolean} true if Throwable is nestable
         * @since 2.0
         */
        public static isThrowableNested(): boolean {
            return ExceptionUtils.THROWABLE_CAUSE_METHOD_$LI$() != null;
        }

        /**
         * <p>Checks whether this <code>Throwable</code> class can store a cause.</p>
         * 
         * <p>This method does <b>not</b> check whether it actually does store a cause.<p>
         * 
         * @param {Error} throwable  the <code>Throwable</code> to examine, may be null
         * @return {boolean} boolean <code>true</code> if nested otherwise <code>false</code>
         * @since 2.0
         */
        public static isNestedThrowable(throwable: Error): boolean {
            if (throwable == null){
                return false;
            }
            if (throwable != null && (throwable.constructor != null && throwable.constructor["__interfaces"] != null && throwable.constructor["__interfaces"].indexOf("org.openprovenance.apache.commons.lang.exception.Nestable") >= 0)){
                return true;
            } else if (throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.sql.SQLException") >= 0)){
                return true;
            } else if (throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.reflect.InvocationTargetException") >= 0)){
                return true;
            } else if (ExceptionUtils.isThrowableNested()){
                return true;
            }
            const cls: any = (<any>throwable.constructor);
            {
                for(let i: number = 0, isize: number = ExceptionUtils.CAUSE_METHOD_NAMES_$LI$().length; i < isize; i++) {{
                    try {
                        const method: { owner: any, name: string, fn : Function } = /* getMethod */((c,p) => { if(c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function') return {owner:c,name:p,fn:c.prototype[p]}; else return null; })(cls,ExceptionUtils.CAUSE_METHOD_NAMES_$LI$()[i]);
                        if (method != null && "java.lang.Throwable".isAssignableFrom(method.getReturnType())){
                            return true;
                        }
                    } catch(__e) {
                        if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.NoSuchMethodException") >= 0)) {
                            const ignored: Error = <Error>__e;

                        }
                        if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.SecurityException") >= 0)) {
                            const ignored: Error = <Error>__e;

                        }
                    }
                };}
            };
            try {
                const field: java.lang.reflect.Field = /* getField */((c,p) => { return {owner:c,name:p}; })(cls,"detail");
                if (field != null){
                    return true;
                }
            } catch(__e) {
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.NoSuchFieldException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
                if(__e != null && (__e["__classes"] && __e["__classes"].indexOf("java.lang.SecurityException") >= 0)) {
                    const ignored: Error = <Error>__e;

                }
            }
            return false;
        }

        /**
         * <p>Counts the number of <code>Throwable</code> objects in the
         * exception chain.</p>
         * 
         * <p>A throwable without cause will return <code>1</code>.
         * A throwable with one cause will return <code>2</code> and so on.
         * A <code>null</code> throwable will return <code>0</code>.</p>
         * 
         * <p>From version 2.2, this method handles recursive cause structures
         * that might otherwise cause infinite loops. The cause chain is
         * processed until the end is reached, or until the next item in the
         * chain is already in the result set.</p>
         * 
         * @param {Error} throwable  the throwable to inspect, may be null
         * @return {number} the count of throwables, zero if null input
         */
        public static getThrowableCount(throwable: Error): number {
            return /* size */(<number>ExceptionUtils.getThrowableList(throwable).length);
        }

        /**
         * <p>Returns the list of <code>Throwable</code> objects in the
         * exception chain.</p>
         * 
         * <p>A throwable without cause will return an array containing
         * one element - the input throwable.
         * A throwable with one cause will return an array containing
         * two elements. - the input throwable and the cause throwable.
         * A <code>null</code> throwable will return an array of size zero.</p>
         * 
         * <p>From version 2.2, this method handles recursive cause structures
         * that might otherwise cause infinite loops. The cause chain is
         * processed until the end is reached, or until the next item in the
         * chain is already in the result set.</p>
         * 
         * @see #getThrowableList(Throwable)
         * @param {Error} throwable  the throwable to inspect, may be null
         * @return {java.lang.Throwable[]} the array of throwables, never null
         */
        public static getThrowables(throwable: Error): Error[] {
            const list: Array<any> = ExceptionUtils.getThrowableList(throwable);
            return <Error[]>/* toArray */list.slice(0);
        }

        /**
         * <p>Returns the list of <code>Throwable</code> objects in the
         * exception chain.</p>
         * 
         * <p>A throwable without cause will return a list containing
         * one element - the input throwable.
         * A throwable with one cause will return a list containing
         * two elements. - the input throwable and the cause throwable.
         * A <code>null</code> throwable will return a list of size zero.</p>
         * 
         * <p>This method handles recursive cause structures that might
         * otherwise cause infinite loops. The cause chain is processed until
         * the end is reached, or until the next item in the chain is already
         * in the result set.</p>
         * 
         * @param {Error} throwable  the throwable to inspect, may be null
         * @return {Array} the list of throwables, never null
         * @since Commons Lang 2.2
         */
        public static getThrowableList(throwable: Error): Array<any> {
            const list: Array<any> = <any>([]);
            while((throwable != null && /* contains */(list.indexOf(<any>(throwable)) >= 0) === false)) {{
                /* add */(list.push(throwable)>0);
                throwable = ExceptionUtils.getCause$java_lang_Throwable(throwable);
            }};
            return list;
        }

        public static indexOfThrowable$java_lang_Throwable$java_lang_Class(throwable: Error, clazz: any): number {
            return ExceptionUtils.indexOf(throwable, clazz, 0, false);
        }

        public static indexOfThrowable$java_lang_Throwable$java_lang_Class$int(throwable: Error, clazz: any, fromIndex: number): number {
            return ExceptionUtils.indexOf(throwable, clazz, fromIndex, false);
        }

        /**
         * <p>Returns the (zero based) index of the first <code>Throwable</code>
         * that matches the specified type in the exception chain from
         * a specified index.
         * Subclasses of the specified class do not match - see
         * {@link #indexOfType(Throwable, Class, int)} for the opposite.</p>
         * 
         * <p>A <code>null</code> throwable returns <code>-1</code>.
         * A <code>null</code> type returns <code>-1</code>.
         * No match in the chain returns <code>-1</code>.
         * A negative start index is treated as zero.
         * A start index greater than the number of throwables returns <code>-1</code>.</p>
         * 
         * @param {Error} throwable  the throwable to inspect, may be null
         * @param {*} clazz  the class to search for, subclasses do not match, null returns -1
         * @param {number} fromIndex  the (zero based) index of the starting position,
         * negative treated as zero, larger than chain size returns -1
         * @return {number} the index into the throwable chain, -1 if no match or null input
         */
        public static indexOfThrowable(throwable?: any, clazz?: any, fromIndex?: any): any {
            if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((clazz != null && (clazz["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(clazz))) || clazz === null) && ((typeof fromIndex === 'number') || fromIndex === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.indexOfThrowable$java_lang_Throwable$java_lang_Class$int(throwable, clazz, fromIndex);
            } else if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((clazz != null && (clazz["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(clazz))) || clazz === null) && fromIndex === undefined) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.indexOfThrowable$java_lang_Throwable$java_lang_Class(throwable, clazz);
            } else throw new Error('invalid overload');
        }

        public static indexOfType$java_lang_Throwable$java_lang_Class(throwable: Error, type: any): number {
            return ExceptionUtils.indexOf(throwable, type, 0, true);
        }

        public static indexOfType$java_lang_Throwable$java_lang_Class$int(throwable: Error, type: any, fromIndex: number): number {
            return ExceptionUtils.indexOf(throwable, type, fromIndex, true);
        }

        /**
         * <p>Returns the (zero based) index of the first <code>Throwable</code>
         * that matches the specified type in the exception chain from
         * a specified index.
         * Subclasses of the specified class do match - see
         * {@link #indexOfThrowable(Throwable, Class)} for the opposite.</p>
         * 
         * <p>A <code>null</code> throwable returns <code>-1</code>.
         * A <code>null</code> type returns <code>-1</code>.
         * No match in the chain returns <code>-1</code>.
         * A negative start index is treated as zero.
         * A start index greater than the number of throwables returns <code>-1</code>.</p>
         * 
         * @param {Error} throwable  the throwable to inspect, may be null
         * @param {*} type  the type to search for, subclasses match, null returns -1
         * @param {number} fromIndex  the (zero based) index of the starting position,
         * negative treated as zero, larger than chain size returns -1
         * @return {number} the index into the throwable chain, -1 if no match or null input
         * @since 2.1
         */
        public static indexOfType(throwable?: any, type?: any, fromIndex?: any): any {
            if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((type != null && (type["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(type))) || type === null) && ((typeof fromIndex === 'number') || fromIndex === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.indexOfType$java_lang_Throwable$java_lang_Class$int(throwable, type, fromIndex);
            } else if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((type != null && (type["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(type))) || type === null) && fromIndex === undefined) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.indexOfType$java_lang_Throwable$java_lang_Class(throwable, type);
            } else throw new Error('invalid overload');
        }

        /**
         * <p>Worker method for the <code>indexOfType</code> methods.</p>
         * 
         * @param {Error} throwable  the throwable to inspect, may be null
         * @param {*} type  the type to search for, subclasses match, null returns -1
         * @param {number} fromIndex  the (zero based) index of the starting position,
         * negative treated as zero, larger than chain size returns -1
         * @param {boolean} subclass if <code>true</code>, compares with {@link Class#isAssignableFrom(Class)}, otherwise compares
         * using references
         * @return {number} index of the <code>type</code> within throwables nested withing the specified <code>throwable</code>
         * @private
         */
        /*private*/ static indexOf(throwable: Error, type: any, fromIndex: number, subclass: boolean): number {
            if (throwable == null || type == null){
                return -1;
            }
            if (fromIndex < 0){
                fromIndex = 0;
            }
            const throwables: Error[] = ExceptionUtils.getThrowables(throwable);
            if (fromIndex >= throwables.length){
                return -1;
            }
            if (subclass){
                for(let i: number = fromIndex; i < throwables.length; i++) {{
                    if (type.isAssignableFrom((<any>throwables[i].constructor))){
                        return i;
                    }
                };}
            } else {
                for(let i: number = fromIndex; i < throwables.length; i++) {{
                    if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(type,(<any>throwables[i].constructor)))){
                        return i;
                    }
                };}
            }
            return -1;
        }

        public static printRootCauseStackTrace$java_lang_Throwable(throwable: Error) {
            ExceptionUtils.printRootCauseStackTrace$java_lang_Throwable$java_io_PrintStream(throwable, java.lang.System.err);
        }

        public static printRootCauseStackTrace$java_lang_Throwable$java_io_PrintStream(throwable: Error, stream: java.io.PrintStream) {
            if (throwable == null){
                return;
            }
            if (stream == null){
                throw Object.defineProperty(new Error("The PrintStream must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            const trace: string[] = ExceptionUtils.getRootCauseStackTrace(throwable);
            for(let i: number = 0; i < trace.length; i++) {{
                stream.println(trace[i]);
            };}
            stream.flush();
        }

        /**
         * <p>Prints a compact stack trace for the root cause of a throwable.</p>
         * 
         * <p>The compact stack trace starts with the root cause and prints
         * stack frames up to the place where it was caught and wrapped.
         * Then it prints the wrapped exception and continues with stack frames
         * until the wrapper exception is caught and wrapped again, etc.</p>
         * 
         * <p>The output of this method is consistent across JDK versions.
         * Note that this is the opposite order to the JDK1.4 display.</p>
         * 
         * <p>The method is equivalent to <code>printStackTrace</code> for throwables
         * that don't have nested causes.</p>
         * 
         * @param {Error} throwable  the throwable to output, may be null
         * @param {java.io.PrintStream} stream  the stream to output to, may not be null
         * @throws IllegalArgumentException if the stream is <code>null</code>
         * @since 2.0
         */
        public static printRootCauseStackTrace(throwable?: any, stream?: any) {
            if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((stream != null && stream instanceof <any>java.io.PrintStream) || stream === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.printRootCauseStackTrace$java_lang_Throwable$java_io_PrintStream(throwable, stream);
            } else if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && ((stream != null && stream instanceof <any>java.io.PrintWriter) || stream === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.printRootCauseStackTrace$java_lang_Throwable$java_io_PrintWriter(throwable, stream);
            } else if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null) && stream === undefined) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.printRootCauseStackTrace$java_lang_Throwable(throwable);
            } else throw new Error('invalid overload');
        }

        public static printRootCauseStackTrace$java_lang_Throwable$java_io_PrintWriter(throwable: Error, writer: java.io.PrintWriter) {
            if (throwable == null){
                return;
            }
            if (writer == null){
                throw Object.defineProperty(new Error("The PrintWriter must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            const trace: string[] = ExceptionUtils.getRootCauseStackTrace(throwable);
            for(let i: number = 0; i < trace.length; i++) {{
                writer.println(trace[i]);
            };}
            writer.flush();
        }

        /**
         * <p>Creates a compact stack trace for the root cause of the supplied
         * <code>Throwable</code>.</p>
         * 
         * <p>The output of this method is consistent across JDK versions.
         * It consists of the root exception followed by each of its wrapping
         * exceptions separated by '[wrapped]'. Note that this is the opposite
         * order to the JDK1.4 display.</p>
         * 
         * @param {Error} throwable  the throwable to examine, may be null
         * @return {java.lang.String[]} an array of stack trace frames, never null
         * @since 2.0
         */
        public static getRootCauseStackTrace(throwable: Error): string[] {
            if (throwable == null){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
            }
            const throwables: Error[] = ExceptionUtils.getThrowables(throwable);
            const count: number = throwables.length;
            const frames: Array<any> = <any>([]);
            let nextTrace: Array<any> = ExceptionUtils.getStackFrameList(throwables[count - 1]);
            for(let i: number = count; --i >= 0; ) {{
                const trace: Array<any> = nextTrace;
                if (i !== 0){
                    nextTrace = ExceptionUtils.getStackFrameList(throwables[i - 1]);
                    ExceptionUtils.removeCommonFrames(trace, nextTrace);
                }
                if (i === count - 1){
                    /* add */(frames.push(throwables[i].toString())>0);
                } else {
                    /* add */(frames.push(ExceptionUtils.WRAPPED_MARKER + throwables[i].toString())>0);
                }
                for(let j: number = 0; j < /* size */(<number>trace.length); j++) {{
                    /* add */(frames.push(/* get */trace[j])>0);
                };}
            };}
            return <string[]>/* toArray */frames.slice(0);
        }

        /**
         * <p>Removes common frames from the cause trace given the two stack traces.</p>
         * 
         * @param {Array} causeFrames  stack trace of a cause throwable
         * @param {Array} wrapperFrames  stack trace of a wrapper throwable
         * @throws IllegalArgumentException if either argument is null
         * @since 2.0
         */
        public static removeCommonFrames(causeFrames: Array<any>, wrapperFrames: Array<any>) {
            if (causeFrames == null || wrapperFrames == null){
                throw Object.defineProperty(new Error("The List must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let causeFrameIndex: number = /* size */(<number>causeFrames.length) - 1;
            let wrapperFrameIndex: number = /* size */(<number>wrapperFrames.length) - 1;
            while((causeFrameIndex >= 0 && wrapperFrameIndex >= 0)) {{
                const causeFrame: string = <string>/* get */causeFrames[causeFrameIndex];
                const wrapperFrame: string = <string>/* get */wrapperFrames[wrapperFrameIndex];
                if (causeFrame === wrapperFrame){
                    /* remove */causeFrames.splice(causeFrameIndex, 1)[0];
                }
                causeFrameIndex--;
                wrapperFrameIndex--;
            }};
        }

        /**
         * <p>A way to get the entire nested stack-trace of an throwable.</p>
         * 
         * <p>The result of this method is highly dependent on the JDK version
         * and whether the exceptions override printStackTrace or not.</p>
         * 
         * @param {Error} throwable  the <code>Throwable</code> to be examined
         * @return {string} the nested stack trace, with the root cause first
         * @since 2.0
         */
        public static getFullStackTrace(throwable: Error): string {
            const sw: java.io.StringWriter = new java.io.StringWriter();
            const pw: java.io.PrintWriter = new java.io.PrintWriter(sw, true);
            const ts: Error[] = ExceptionUtils.getThrowables(throwable);
            for(let i: number = 0; i < ts.length; i++) {{
                console.error(ts[i].message, ts[i]);
                if (ExceptionUtils.isNestedThrowable(ts[i])){
                    break;
                }
            };}
            return /* toString */sw.getBuffer().str;
        }

        /**
         * <p>Gets the stack trace from a Throwable as a String.</p>
         * 
         * <p>The result of this method vary by JDK version as this method
         * uses {@link Throwable#printStackTrace(PrintWriter)}.
         * On JDK1.3 and earlier, the cause exception will not be shown
         * unless the specified throwable alters printStackTrace.</p>
         * 
         * @param {Error} throwable  the <code>Throwable</code> to be examined
         * @return {string} the stack trace as generated by the exception's
         * <code>printStackTrace(PrintWriter)</code> method
         */
        public static getStackTrace(throwable: Error): string {
            const sw: java.io.StringWriter = new java.io.StringWriter();
            const pw: java.io.PrintWriter = new java.io.PrintWriter(sw, true);
            console.error(throwable.message, throwable);
            return /* toString */sw.getBuffer().str;
        }

        public static getStackFrames$java_lang_Throwable(throwable: Error): string[] {
            if (throwable == null){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
            }
            return ExceptionUtils.getStackFrames$java_lang_String(ExceptionUtils.getStackTrace(throwable));
        }

        /**
         * <p>Captures the stack trace associated with the specified
         * <code>Throwable</code> object, decomposing it into a list of
         * stack frames.</p>
         * 
         * <p>The result of this method vary by JDK version as this method
         * uses {@link Throwable#printStackTrace(PrintWriter)}.
         * On JDK1.3 and earlier, the cause exception will not be shown
         * unless the specified throwable alters printStackTrace.</p>
         * 
         * @param {Error} throwable  the <code>Throwable</code> to examine, may be null
         * @return {java.lang.String[]} an array of strings describing each stack frame, never null
         */
        public static getStackFrames(throwable?: any): any {
            if (((throwable != null && (throwable["__classes"] && throwable["__classes"].indexOf("java.lang.Throwable") >= 0) || throwable != null && throwable instanceof <any>Error) || throwable === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getStackFrames$java_lang_Throwable(throwable);
            } else if (((typeof throwable === 'string') || throwable === null)) {
                return <any>org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getStackFrames$java_lang_String(throwable);
            } else throw new Error('invalid overload');
        }

        static getStackFrames$java_lang_String(stackTrace: string): string[] {
            const linebreak: string = org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$();
            const frames: java.util.StringTokenizer = new java.util.StringTokenizer(stackTrace, linebreak);
            const list: Array<any> = <any>([]);
            while((frames.hasMoreTokens())) {{
                /* add */(list.push(frames.nextToken())>0);
            }};
            return ExceptionUtils.toArray(list);
        }

        /**
         * <p>Produces a <code>List</code> of stack frames - the message
         * is not included. Only the trace of the specified exception is
         * returned, any caused by trace is stripped.</p>
         * 
         * <p>This works in most cases - it will only fail if the exception
         * message contains a line that starts with:
         * <code>&quot;&nbsp;&nbsp;&nbsp;at&quot;.</code></p>
         * 
         * @param {Error} t is any throwable
         * @return {Array} List of stack frames
         */
        static getStackFrameList(t: Error): Array<any> {
            const stackTrace: string = ExceptionUtils.getStackTrace(t);
            const linebreak: string = org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$();
            const frames: java.util.StringTokenizer = new java.util.StringTokenizer(stackTrace, linebreak);
            const list: Array<any> = <any>([]);
            let traceStarted: boolean = false;
            while((frames.hasMoreTokens())) {{
                const token: string = frames.nextToken();
                const at: number = token.indexOf("at");
                if (at !== -1 && token.substring(0, at).trim().length === 0){
                    traceStarted = true;
                    /* add */(list.push(token)>0);
                } else if (traceStarted){
                    break;
                }
            }};
            return list;
        }

        /**
         * Gets a short message summarising the exception.
         * <p>
         * The message returned is of the form
         * {ClassNameWithoutPackage}: {ThrowableMessage}
         * 
         * @param {Error} th  the throwable to get a message for, null returns empty string
         * @return {string} the message, non-null
         * @since Commons Lang 2.2
         */
        public static getMessage(th: Error): string {
            if (th == null){
                return "";
            }
            const clsName: string = org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Object$java_lang_String(th, null);
            const msg: string = th.message;
            return clsName + ": " + org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(msg);
        }

        /**
         * Gets a short message summarising the root cause exception.
         * <p>
         * The message returned is of the form
         * {ClassNameWithoutPackage}: {ThrowableMessage}
         * 
         * @param {Error} th  the throwable to get a message for, null returns empty string
         * @return {string} the message, non-null
         * @since Commons Lang 2.2
         */
        public static getRootCauseMessage(th: Error): string {
            let root: Error = ExceptionUtils.getRootCause(th);
            root = (root == null ? th : root);
            return ExceptionUtils.getMessage(root);
        }
    }
    ExceptionUtils["__class"] = "org.openprovenance.apache.commons.lang.exception.ExceptionUtils";

}


org.openprovenance.apache.commons.lang.exception.ExceptionUtils.__static_initialize();
