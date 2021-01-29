/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.builder {
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
    export abstract class ToStringStyle {
        /**
         * The default toString style. Using the Using the {@code Person}
         * example from {@link ToStringBuilder}, the output would look like this:
         * 
         * <pre>
         * Person@182f0db[name=John Doe,age=33,smoker=false]
         * </pre>
         */
        public static DEFAULT_STYLE: ToStringStyle; public static DEFAULT_STYLE_$LI$(): ToStringStyle { if (ToStringStyle.DEFAULT_STYLE == null) { ToStringStyle.DEFAULT_STYLE = new ToStringStyle.DefaultToStringStyle(); }  return ToStringStyle.DEFAULT_STYLE; }

        /**
         * The multi line toString style. Using the Using the {@code Person}
         * example from {@link ToStringBuilder}, the output would look like this:
         * 
         * <pre>
         * Person@182f0db[
         * name=John Doe
         * age=33
         * smoker=false
         * ]
         * </pre>
         */
        public static MULTI_LINE_STYLE: ToStringStyle; public static MULTI_LINE_STYLE_$LI$(): ToStringStyle { if (ToStringStyle.MULTI_LINE_STYLE == null) { ToStringStyle.MULTI_LINE_STYLE = new ToStringStyle.MultiLineToStringStyle(); }  return ToStringStyle.MULTI_LINE_STYLE; }

        /**
         * The no field names toString style. Using the Using the
         * {@code Person} example from {@link ToStringBuilder}, the output
         * would look like this:
         * 
         * <pre>
         * Person@182f0db[John Doe,33,false]
         * </pre>
         */
        public static NO_FIELD_NAMES_STYLE: ToStringStyle; public static NO_FIELD_NAMES_STYLE_$LI$(): ToStringStyle { if (ToStringStyle.NO_FIELD_NAMES_STYLE == null) { ToStringStyle.NO_FIELD_NAMES_STYLE = new ToStringStyle.NoFieldNameToStringStyle(); }  return ToStringStyle.NO_FIELD_NAMES_STYLE; }

        /**
         * The short prefix toString style. Using the {@code Person} example
         * from {@link ToStringBuilder}, the output would look like this:
         * 
         * <pre>
         * Person[name=John Doe,age=33,smoker=false]
         * </pre>
         * 
         * @since 2.1
         */
        public static SHORT_PREFIX_STYLE: ToStringStyle; public static SHORT_PREFIX_STYLE_$LI$(): ToStringStyle { if (ToStringStyle.SHORT_PREFIX_STYLE == null) { ToStringStyle.SHORT_PREFIX_STYLE = new ToStringStyle.ShortPrefixToStringStyle(); }  return ToStringStyle.SHORT_PREFIX_STYLE; }

        /**
         * The simple toString style. Using the Using the {@code Person}
         * example from {@link ToStringBuilder}, the output would look like this:
         * 
         * <pre>
         * John Doe,33,false
         * </pre>
         */
        public static SIMPLE_STYLE: ToStringStyle; public static SIMPLE_STYLE_$LI$(): ToStringStyle { if (ToStringStyle.SIMPLE_STYLE == null) { ToStringStyle.SIMPLE_STYLE = new ToStringStyle.SimpleToStringStyle(); }  return ToStringStyle.SIMPLE_STYLE; }

        /**
         * <p>
         * A registry of objects used by {@code reflectionToString} methods
         * to detect cyclical object references and avoid infinite loops.
         * </p>
         */
        static REGISTRY: java.lang.ThreadLocal<any>; public static REGISTRY_$LI$(): java.lang.ThreadLocal<any> { if (ToStringStyle.REGISTRY == null) { ToStringStyle.REGISTRY = <any>(new java.lang.ThreadLocal()); }  return ToStringStyle.REGISTRY; }

        /**
         * <p>
         * Returns the registry of objects being traversed by the {@code reflectionToString}
         * methods in the current thread.
         * </p>
         * 
         * @return {*} Set the registry of objects being traversed
         */
        static getRegistry(): any {
            return <any><any>/* get */((tlObj: any) => {    if (tlObj.___value) { return tlObj.___value }     else { return tlObj.___value = tlObj.initialValue() }   })(ToStringStyle.REGISTRY_$LI$());
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
        static isRegistered(value: any): boolean {
            const m: any = ToStringStyle.getRegistry();
            return m != null && /* containsKey */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return true; } return false; })(<any>m, value);
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
        static register(value: any) {
            if (value != null){
                let m: any = ToStringStyle.getRegistry();
                if (m == null){
                    m = <any>({});
                    ToStringStyle.REGISTRY_$LI$().set(m);
                }
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>m, value, null);
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
        static unregister(value: any) {
            if (value != null){
                const m: any = ToStringStyle.getRegistry();
                if (m != null){
                    /* remove */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries.splice(i,1)[0]; } })(<any>m, value);
                    if (/* isEmpty */((m) => { if(m.entries==null) m.entries=[]; return m.entries.length == 0; })(<any>m)){
                        ToStringStyle.REGISTRY_$LI$().set(null);
                    }
                }
            }
        }

        /**
         * Whether to use the field names, the default is {@code true}.
         */
        /*private*/ useFieldNames: boolean;

        /**
         * Whether to use the class name, the default is {@code true}.
         */
        /*private*/ useClassName: boolean;

        /**
         * Whether to use short class names, the default is {@code false}.
         */
        /*private*/ useShortClassName: boolean;

        /**
         * Whether to use the identity hash code, the default is {@code true}.
         */
        /*private*/ useIdentityHashCode: boolean;

        /**
         * The content start {@code '['}.
         */
        /*private*/ contentStart: string;

        /**
         * The content end {@code ']'}.
         */
        /*private*/ contentEnd: string;

        /**
         * The field name value separator {@code '='}.
         */
        /*private*/ fieldNameValueSeparator: string;

        /**
         * Whether the field separator should be added before any other fields.
         */
        /*private*/ fieldSeparatorAtStart: boolean;

        /**
         * Whether the field separator should be added after any other fields.
         */
        /*private*/ fieldSeparatorAtEnd: boolean;

        /**
         * The field separator {@code ','}.
         */
        /*private*/ fieldSeparator: string;

        /**
         * The array start {@code ''}.
         */
        /*private*/ arrayStart: string;

        /**
         * The array separator {@code ','}.
         */
        /*private*/ arraySeparator: string;

        /**
         * The detail for array content.
         */
        /*private*/ arrayContentDetail: boolean;

        /**
         * The array end {@code '}'}.
         */
        /*private*/ arrayEnd: string;

        /**
         * The value to use when fullDetail is {@code null},
         * the default value is {@code true}.
         */
        /*private*/ defaultFullDetail: boolean;

        /**
         * The {@code null} text {@code '&lt;null&gt;'}.
         */
        /*private*/ nullText: string;

        /**
         * The summary size text start {@code '&lt;size'}.
         */
        /*private*/ sizeStartText: string;

        /**
         * The summary size text start {@code '&gt;'}.
         */
        /*private*/ sizeEndText: string;

        /**
         * The summary object text start {@code '&lt;'}.
         */
        /*private*/ summaryObjectStartText: string;

        /**
         * The summary object text start {@code '&gt;'}.
         */
        /*private*/ summaryObjectEndText: string;

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
        public appendSuper(buffer: { str: string, toString: Function }, superToString: string) {
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
        public appendToString(buffer: { str: string, toString: Function }, toString: string) {
            if (toString != null){
                const pos1: number = toString.indexOf(this.contentStart) + this.contentStart.length;
                const pos2: number = toString.lastIndexOf(this.contentEnd);
                if (pos1 !== pos2 && pos1 >= 0 && pos2 >= 0){
                    const data: string = toString.substring(pos1, pos2);
                    if (this.fieldSeparatorAtStart){
                        this.removeLastFieldSeparator(buffer);
                    }
                    /* append */(sb => { sb.str += <any>data; return sb; })(buffer);
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
        public appendStart(buffer: { str: string, toString: Function }, object: any) {
            if (object != null){
                this.appendClassName(buffer, object);
                this.appendIdentityHashCode(buffer, object);
                this.appendContentStart(buffer);
                if (this.fieldSeparatorAtStart){
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
        public appendEnd(buffer: { str: string, toString: Function }, object: any) {
            if (this.fieldSeparatorAtEnd === false){
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
        removeLastFieldSeparator(buffer: { str: string, toString: Function }) {
            const len: number = /* length */buffer.str.length;
            const sepLen: number = this.fieldSeparator.length;
            if (len > 0 && sepLen > 0 && len >= sepLen){
                let match: boolean = true;
                for(let i: number = 0; i < sepLen; i++) {{
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(/* charAt */buffer.str.charAt(len - 1 - i)) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.fieldSeparator.charAt(sepLen - 1 - i))){
                        match = false;
                        break;
                    }
                };}
                if (match){
                    /* setLength */((sb, length) => sb.str = sb.str.substring(0, length))(buffer, len - sepLen);
                }
            }
        }

        public append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, value: any, fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (value == null){
                this.appendNullText(buffer, fieldName);
            } else {
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
        appendInternal(buffer: { str: string, toString: Function }, fieldName: string, value: any, detail: boolean) {
            if (ToStringStyle.isRegistered(value) && !((typeof value === 'number') || (typeof value === 'boolean') || (typeof value === 'string'))){
                this.appendCyclicObject(buffer, fieldName, value);
                return;
            }
            ToStringStyle.register(value);
            try {
                if (value != null && (value instanceof Array)){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Collection(buffer, fieldName, <Array<any>><any>value);
                    } else {
                        this.appendSummarySize(buffer, fieldName, /* size */(<number>(<Array<any>><any>value).length));
                    }
                } else if (value != null && (value instanceof Object)){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Map(buffer, fieldName, <any><any>value);
                    } else {
                        this.appendSummarySize(buffer, fieldName, /* size */((m) => { if(m.entries==null) m.entries=[]; return m.entries.length; })(<any>(<any><any>value)));
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'number')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, <number[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, <number[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'number')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, <number[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, <number[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'number')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, <number[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, <number[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'number')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, <number[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, <number[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'string')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, <string[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, <string[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'number')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, <number[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, <number[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'number')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, <number[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, <number[]>value);
                    }
                } else if (value != null && value instanceof <any>Array && (value.length == 0 || value[0] == null ||typeof value[0] === 'boolean')){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, <boolean[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, <boolean[]>value);
                    }
                } else if ((<any>value.constructor).isArray()){
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, <any[]>value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, <any[]>value);
                    }
                } else {
                    if (detail){
                        this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, value);
                    } else {
                        this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, value);
                    }
                }
            } finally {
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
        appendCyclicObject(buffer: { str: string, toString: Function }, fieldName: string, value: any) {
            org.openprovenance.apache.commons.lang.ObjectUtils.identityToString$java_lang_StringBuffer$java_lang_Object(buffer, value);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer: { str: string, toString: Function }, fieldName: string, value: any) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Collection(buffer: { str: string, toString: Function }, fieldName: string, coll: Array<any>) {
            /* append */(sb => { sb.str += <any>coll; return sb; })(buffer);
        }

        /**
         * <p>Append to the {@code toString} a {@code Collection}.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         * @param {string} fieldName  the field name, typically not used as already appended
         * @param {Array} coll  the {@code Collection} to add to the
         * {@code toString}, not {@code null}
         */
        public appendDetail(buffer?: any, fieldName?: any, coll?: any) {
            if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && (coll instanceof Array)) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Collection(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && (coll instanceof Object)) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Map(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(coll[0] != null))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'number'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'number'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'number'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'number'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'string'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'number'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'number'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null && coll instanceof <any>Array && (coll.length == 0 || coll[0] == null ||(typeof coll[0] === 'boolean'))) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'string') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'number') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof coll === 'boolean') || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, coll);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((coll != null) || coll === null)) {
                return <any>this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, coll);
            } else throw new Error('invalid overload');
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$java_util_Map(buffer: { str: string, toString: Function }, fieldName: string, map: any) {
            /* append */(sb => { sb.str += <any>map; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer: { str: string, toString: Function }, fieldName: string, value: any) {
            /* append */(sb => { sb.str += <any>this.summaryObjectStartText; return sb; })(buffer);
            /* append */(sb => { sb.str += <any>this.getShortClassName((<any>value.constructor)); return sb; })(buffer);
            /* append */(sb => { sb.str += <any>this.summaryObjectEndText; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$long(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$int(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$short(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$byte(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$char(buffer: { str: string, toString: Function }, fieldName: string, value: string) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer: { str: string, toString: Function }, fieldName: string, value: string) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$double(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$float(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer: { str: string, toString: Function }, fieldName: string, value: number) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$boolean(buffer: { str: string, toString: Function }, fieldName: string, value: boolean) {
            this.appendFieldStart(buffer, fieldName);
            this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, value);
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer: { str: string, toString: Function }, fieldName: string, value: boolean) {
            /* append */(sb => { sb.str += <any>value; return sb; })(buffer);
        }

        public append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: any[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array);
            } else {
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
        public append(buffer?: any, fieldName?: any, array?: any, fullDetail?: any) {
            if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(array[0] != null))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$java_lang_Object_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'string'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'boolean'))) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null) || array === null) && ((typeof fullDetail === 'boolean') || fullDetail === null)) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$java_lang_Object$java_lang_Boolean(buffer, fieldName, array, fullDetail);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'string') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'number') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((typeof array === 'boolean') || array === null) && fullDetail === undefined) {
                return <any>this.append$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, array);
            } else throw new Error('invalid overload');
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer: { str: string, toString: Function }, fieldName: string, array: any[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                const item: any = array[i];
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                if (item == null){
                    this.appendNullText(buffer, fieldName);
                } else {
                    this.appendInternal(buffer, fieldName, item, this.arrayContentDetail);
                }
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
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
        reflectionAppendArrayDetail(buffer: { str: string, toString: Function }, fieldName: string, array: any) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            const length: number = /* getLength */array.length;
            for(let i: number = 0; i < length; i++) {{
                const item: any = /* get */array[i];
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                if (item == null){
                    this.appendNullText(buffer, fieldName);
                } else {
                    this.appendInternal(buffer, fieldName, item, this.arrayContentDetail);
                }
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        public appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer: { str: string, toString: Function }, fieldName: string, array: any[]) {
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
        public appendSummary(buffer?: any, fieldName?: any, array?: any) {
            if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(array[0] != null))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'string'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'number'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'boolean'))) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array);
            } else if (((buffer != null && (buffer instanceof Object)) || buffer === null) && ((typeof fieldName === 'string') || fieldName === null) && ((array != null) || array === null)) {
                return <any>this.appendSummary$java_lang_StringBuffer$java_lang_String$java_lang_Object(buffer, fieldName, array);
            } else throw new Error('invalid overload');
        }

        public append$java_lang_StringBuffer$java_lang_String$long_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: number[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$long_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$long(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$long_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$int_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: number[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$int_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$int(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$int_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$short_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: number[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$short_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$short(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$short_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$byte_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: number[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$byte_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$byte(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$byte_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$char_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: string[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$char_A(buffer: { str: string, toString: Function }, fieldName: string, array: string[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$char(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$char_A(buffer: { str: string, toString: Function }, fieldName: string, array: string[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$double_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: number[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$double_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$double(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$double_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$float_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: number[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$float_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$float(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$float_A(buffer: { str: string, toString: Function }, fieldName: string, array: number[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        public append$java_lang_StringBuffer$java_lang_String$boolean_A$java_lang_Boolean(buffer: { str: string, toString: Function }, fieldName: string, array: boolean[], fullDetail: boolean) {
            this.appendFieldStart(buffer, fieldName);
            if (array == null){
                this.appendNullText(buffer, fieldName);
            } else if (this.isFullDetail(fullDetail)){
                this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array);
            } else {
                this.appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer, fieldName, array);
            }
            this.appendFieldEnd(buffer, fieldName);
        }

        appendDetail$java_lang_StringBuffer$java_lang_String$boolean_A(buffer: { str: string, toString: Function }, fieldName: string, array: boolean[]) {
            /* append */(sb => { sb.str += <any>this.arrayStart; return sb; })(buffer);
            for(let i: number = 0; i < array.length; i++) {{
                if (i > 0){
                    /* append */(sb => { sb.str += <any>this.arraySeparator; return sb; })(buffer);
                }
                this.appendDetail$java_lang_StringBuffer$java_lang_String$boolean(buffer, fieldName, array[i]);
            };}
            /* append */(sb => { sb.str += <any>this.arrayEnd; return sb; })(buffer);
        }

        appendSummary$java_lang_StringBuffer$java_lang_String$boolean_A(buffer: { str: string, toString: Function }, fieldName: string, array: boolean[]) {
            this.appendSummarySize(buffer, fieldName, array.length);
        }

        /**
         * <p>Append to the {@code toString} the class name.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         * @param {*} object  the {@code Object} whose name to output
         */
        appendClassName(buffer: { str: string, toString: Function }, object: any) {
            if (this.useClassName && object != null){
                ToStringStyle.register(object);
                if (this.useShortClassName){
                    /* append */(sb => { sb.str += <any>this.getShortClassName((<any>object.constructor)); return sb; })(buffer);
                } else {
                    /* append */(sb => { sb.str += <any>/* getName */(c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])((<any>object.constructor)); return sb; })(buffer);
                }
            }
        }

        /**
         * <p>Append the {@link System#identityHashCode(Object)}.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         * @param {*} object  the {@code Object} whose id to output
         */
        appendIdentityHashCode(buffer: { str: string, toString: Function }, object: any) {
            if (this.isUseIdentityHashCode() && object != null){
                ToStringStyle.register(object);
                /* append */(sb => { sb.str += <any>'@'; return sb; })(buffer);
                /* append */(sb => { sb.str += <any>javaemul.internal.IntegerHelper.toHexString(java.lang.System.identityHashCode(object)); return sb; })(buffer);
            }
        }

        /**
         * <p>Append to the {@code toString} the content start.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         */
        appendContentStart(buffer: { str: string, toString: Function }) {
            /* append */(sb => { sb.str += <any>this.contentStart; return sb; })(buffer);
        }

        /**
         * <p>Append to the {@code toString} the content end.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         */
        appendContentEnd(buffer: { str: string, toString: Function }) {
            /* append */(sb => { sb.str += <any>this.contentEnd; return sb; })(buffer);
        }

        /**
         * <p>Append to the {@code toString} an indicator for {@code null}.</p>
         * 
         * <p>The default indicator is {@code '&lt;null&gt;'}.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         * @param {string} fieldName  the field name, typically not used as already appended
         */
        appendNullText(buffer: { str: string, toString: Function }, fieldName: string) {
            /* append */(sb => { sb.str += <any>this.nullText; return sb; })(buffer);
        }

        /**
         * <p>Append to the {@code toString} the field separator.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         */
        appendFieldSeparator(buffer: { str: string, toString: Function }) {
            /* append */(sb => { sb.str += <any>this.fieldSeparator; return sb; })(buffer);
        }

        /**
         * <p>Append to the {@code toString} the field start.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         * @param {string} fieldName  the field name
         */
        appendFieldStart(buffer: { str: string, toString: Function }, fieldName: string) {
            if (this.useFieldNames && fieldName != null){
                /* append */(sb => { sb.str += <any>fieldName; return sb; })(buffer);
                /* append */(sb => { sb.str += <any>this.fieldNameValueSeparator; return sb; })(buffer);
            }
        }

        /**
         * <p>Append to the {@code toString}  the field end.</p>
         * 
         * @param {{ str: string, toString: Function }} buffer  the {@code StringBuffer} to populate
         * @param {string} fieldName  the field name, typically not used as already appended
         */
        appendFieldEnd(buffer: { str: string, toString: Function }, fieldName: string) {
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
        appendSummarySize(buffer: { str: string, toString: Function }, fieldName: string, size: number) {
            /* append */(sb => { sb.str += <any>this.sizeStartText; return sb; })(buffer);
            /* append */(sb => { sb.str += <any>size; return sb; })(buffer);
            /* append */(sb => { sb.str += <any>this.sizeEndText; return sb; })(buffer);
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
        isFullDetail(fullDetailRequest: boolean): boolean {
            if (fullDetailRequest == null){
                return this.defaultFullDetail;
            }
            return /* booleanValue */fullDetailRequest;
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
        getShortClassName(cls: any): string {
            return org.openprovenance.apache.commons.lang.ClassUtils.getShortClassName$java_lang_Class(cls);
        }

        /**
         * <p>Gets whether to use the class name.</p>
         * 
         * @return {boolean} the current useClassName flag
         */
        isUseClassName(): boolean {
            return this.useClassName;
        }

        /**
         * <p>Sets whether to use the class name.</p>
         * 
         * @param {boolean} useClassName  the new useClassName flag
         */
        setUseClassName(useClassName: boolean) {
            this.useClassName = useClassName;
        }

        /**
         * <p>Gets whether to output short or long class names.</p>
         * 
         * @return {boolean} the current useShortClassName flag
         * @since 2.0
         */
        isUseShortClassName(): boolean {
            return this.useShortClassName;
        }

        /**
         * <p>Gets whether to output short or long class names.</p>
         * 
         * @return {boolean} the current shortClassName flag
         * @deprecated Use {@link #isUseShortClassName()}
         * Method will be removed in Commons Lang 3.0.
         */
        isShortClassName(): boolean {
            return this.useShortClassName;
        }

        /**
         * <p>Sets whether to output short or long class names.</p>
         * 
         * @param {boolean} useShortClassName  the new useShortClassName flag
         * @since 2.0
         */
        setUseShortClassName(useShortClassName: boolean) {
            this.useShortClassName = useShortClassName;
        }

        /**
         * <p>Sets whether to output short or long class names.</p>
         * 
         * @param {boolean} shortClassName  the new shortClassName flag
         * @deprecated Use {@link #setUseShortClassName(boolean)}
         * Method will be removed in Commons Lang 3.0.
         */
        setShortClassName(shortClassName: boolean) {
            this.useShortClassName = shortClassName;
        }

        /**
         * <p>Gets whether to use the identity hash code.</p>
         * 
         * @return {boolean} the current useIdentityHashCode flag
         */
        isUseIdentityHashCode(): boolean {
            return this.useIdentityHashCode;
        }

        /**
         * <p>Sets whether to use the identity hash code.</p>
         * 
         * @param {boolean} useIdentityHashCode  the new useIdentityHashCode flag
         */
        setUseIdentityHashCode(useIdentityHashCode: boolean) {
            this.useIdentityHashCode = useIdentityHashCode;
        }

        /**
         * <p>Gets whether to use the field names passed in.</p>
         * 
         * @return {boolean} the current useFieldNames flag
         */
        isUseFieldNames(): boolean {
            return this.useFieldNames;
        }

        /**
         * <p>Sets whether to use the field names passed in.</p>
         * 
         * @param {boolean} useFieldNames  the new useFieldNames flag
         */
        setUseFieldNames(useFieldNames: boolean) {
            this.useFieldNames = useFieldNames;
        }

        /**
         * <p>Gets whether to use full detail when the caller doesn't
         * specify.</p>
         * 
         * @return {boolean} the current defaultFullDetail flag
         */
        isDefaultFullDetail(): boolean {
            return this.defaultFullDetail;
        }

        /**
         * <p>Sets whether to use full detail when the caller doesn't
         * specify.</p>
         * 
         * @param {boolean} defaultFullDetail  the new defaultFullDetail flag
         */
        setDefaultFullDetail(defaultFullDetail: boolean) {
            this.defaultFullDetail = defaultFullDetail;
        }

        /**
         * <p>Gets whether to output array content detail.</p>
         * 
         * @return {boolean} the current array content detail setting
         */
        isArrayContentDetail(): boolean {
            return this.arrayContentDetail;
        }

        /**
         * <p>Sets whether to output array content detail.</p>
         * 
         * @param {boolean} arrayContentDetail  the new arrayContentDetail flag
         */
        setArrayContentDetail(arrayContentDetail: boolean) {
            this.arrayContentDetail = arrayContentDetail;
        }

        /**
         * <p>Gets the array start text.</p>
         * 
         * @return {string} the current array start text
         */
        getArrayStart(): string {
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
        setArrayStart(arrayStart: string) {
            if (arrayStart == null){
                arrayStart = "";
            }
            this.arrayStart = arrayStart;
        }

        /**
         * <p>Gets the array end text.</p>
         * 
         * @return {string} the current array end text
         */
        getArrayEnd(): string {
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
        setArrayEnd(arrayEnd: string) {
            if (arrayEnd == null){
                arrayEnd = "";
            }
            this.arrayEnd = arrayEnd;
        }

        /**
         * <p>Gets the array separator text.</p>
         * 
         * @return {string} the current array separator text
         */
        getArraySeparator(): string {
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
        setArraySeparator(arraySeparator: string) {
            if (arraySeparator == null){
                arraySeparator = "";
            }
            this.arraySeparator = arraySeparator;
        }

        /**
         * <p>Gets the content start text.</p>
         * 
         * @return {string} the current content start text
         */
        getContentStart(): string {
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
        setContentStart(contentStart: string) {
            if (contentStart == null){
                contentStart = "";
            }
            this.contentStart = contentStart;
        }

        /**
         * <p>Gets the content end text.</p>
         * 
         * @return {string} the current content end text
         */
        getContentEnd(): string {
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
        setContentEnd(contentEnd: string) {
            if (contentEnd == null){
                contentEnd = "";
            }
            this.contentEnd = contentEnd;
        }

        /**
         * <p>Gets the field name value separator text.</p>
         * 
         * @return {string} the current field name value separator text
         */
        getFieldNameValueSeparator(): string {
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
        setFieldNameValueSeparator(fieldNameValueSeparator: string) {
            if (fieldNameValueSeparator == null){
                fieldNameValueSeparator = "";
            }
            this.fieldNameValueSeparator = fieldNameValueSeparator;
        }

        /**
         * <p>Gets the field separator text.</p>
         * 
         * @return {string} the current field separator text
         */
        getFieldSeparator(): string {
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
        setFieldSeparator(fieldSeparator: string) {
            if (fieldSeparator == null){
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
        isFieldSeparatorAtStart(): boolean {
            return this.fieldSeparatorAtStart;
        }

        /**
         * <p>Sets whether the field separator should be added at the start
         * of each buffer.</p>
         * 
         * @param {boolean} fieldSeparatorAtStart  the fieldSeparatorAtStart flag
         * @since 2.0
         */
        setFieldSeparatorAtStart(fieldSeparatorAtStart: boolean) {
            this.fieldSeparatorAtStart = fieldSeparatorAtStart;
        }

        /**
         * <p>Gets whether the field separator should be added at the end
         * of each buffer.</p>
         * 
         * @return {boolean} fieldSeparatorAtEnd flag
         * @since 2.0
         */
        isFieldSeparatorAtEnd(): boolean {
            return this.fieldSeparatorAtEnd;
        }

        /**
         * <p>Sets whether the field separator should be added at the end
         * of each buffer.</p>
         * 
         * @param {boolean} fieldSeparatorAtEnd  the fieldSeparatorAtEnd flag
         * @since 2.0
         */
        setFieldSeparatorAtEnd(fieldSeparatorAtEnd: boolean) {
            this.fieldSeparatorAtEnd = fieldSeparatorAtEnd;
        }

        /**
         * <p>Gets the text to output when {@code null} found.</p>
         * 
         * @return {string} the current text to output when null found
         */
        getNullText(): string {
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
        setNullText(nullText: string) {
            if (nullText == null){
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
        getSizeStartText(): string {
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
        setSizeStartText(sizeStartText: string) {
            if (sizeStartText == null){
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
        getSizeEndText(): string {
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
        setSizeEndText(sizeEndText: string) {
            if (sizeEndText == null){
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
        getSummaryObjectStartText(): string {
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
        setSummaryObjectStartText(summaryObjectStartText: string) {
            if (summaryObjectStartText == null){
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
        getSummaryObjectEndText(): string {
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
        setSummaryObjectEndText(summaryObjectEndText: string) {
            if (summaryObjectEndText == null){
                summaryObjectEndText = "";
            }
            this.summaryObjectEndText = summaryObjectEndText;
        }
    }
    ToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle";
    ToStringStyle["__interfaces"] = ["java.io.Serializable"];



    export namespace ToStringStyle {

        /**
         * <p>Default {@code ToStringStyle}.</p>
         * 
         * <p>This is an inner class rather than using
         * {@code StandardToStringStyle} to ensure its immutability.</p>
         * @extends org.openprovenance.apache.commons.lang.builder.ToStringStyle
         * @class
         */
        export class DefaultToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
            /**
             * Required for serialization support.
             * 
             * @see Serializable
             */
            static serialVersionUID: number = 1;

            constructor() {
                super();
            }

            /**
             * <p>Ensure {@code Singleton} after serialization.</p>
             * 
             * @return {*} the singleton
             * @private
             */
            readResolve(): any {
                return org.openprovenance.apache.commons.lang.builder.ToStringStyle.DEFAULT_STYLE_$LI$();
            }
        }
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
        export class NoFieldNameToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
            static serialVersionUID: number = 1;

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
            readResolve(): any {
                return org.openprovenance.apache.commons.lang.builder.ToStringStyle.NO_FIELD_NAMES_STYLE_$LI$();
            }
        }
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
        export class ShortPrefixToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
            static serialVersionUID: number = 1;

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
            readResolve(): any {
                return org.openprovenance.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE_$LI$();
            }
        }
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
        export class SimpleToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
            static serialVersionUID: number = 1;

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
            readResolve(): any {
                return org.openprovenance.apache.commons.lang.builder.ToStringStyle.SIMPLE_STYLE_$LI$();
            }
        }
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
        export class MultiLineToStringStyle extends org.openprovenance.apache.commons.lang.builder.ToStringStyle {
            static serialVersionUID: number = 1;

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
            readResolve(): any {
                return org.openprovenance.apache.commons.lang.builder.ToStringStyle.MULTI_LINE_STYLE_$LI$();
            }
        }
        MultiLineToStringStyle["__class"] = "org.openprovenance.apache.commons.lang.builder.ToStringStyle.MultiLineToStringStyle";
        MultiLineToStringStyle["__interfaces"] = ["java.io.Serializable"];


    }

}

