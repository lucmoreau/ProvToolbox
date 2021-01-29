/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
    /**
     * Default constructor.
     * @class
     */
    export class Entities {
        static __static_initialized: boolean = false;
        static __static_initialize() { if (!Entities.__static_initialized) { Entities.__static_initialized = true; Entities.__static_initializer_0(); Entities.__static_initializer_1(); Entities.__static_initializer_2(); } }

        static BASIC_ARRAY: string[][]; public static BASIC_ARRAY_$LI$(): string[][] { Entities.__static_initialize(); if (Entities.BASIC_ARRAY == null) { Entities.BASIC_ARRAY = [["quot", "34"], ["amp", "38"], ["lt", "60"], ["gt", "62"]]; }  return Entities.BASIC_ARRAY; }

        static APOS_ARRAY: string[][]; public static APOS_ARRAY_$LI$(): string[][] { Entities.__static_initialize(); if (Entities.APOS_ARRAY == null) { Entities.APOS_ARRAY = [["apos", "39"]]; }  return Entities.APOS_ARRAY; }

        static ISO8859_1_ARRAY: string[][]; public static ISO8859_1_ARRAY_$LI$(): string[][] { Entities.__static_initialize(); if (Entities.ISO8859_1_ARRAY == null) { Entities.ISO8859_1_ARRAY = [["nbsp", "160"], ["iexcl", "161"], ["cent", "162"], ["pound", "163"], ["curren", "164"], ["yen", "165"], ["brvbar", "166"], ["sect", "167"], ["uml", "168"], ["copy", "169"], ["ordf", "170"], ["laquo", "171"], ["not", "172"], ["shy", "173"], ["reg", "174"], ["macr", "175"], ["deg", "176"], ["plusmn", "177"], ["sup2", "178"], ["sup3", "179"], ["acute", "180"], ["micro", "181"], ["para", "182"], ["middot", "183"], ["cedil", "184"], ["sup1", "185"], ["ordm", "186"], ["raquo", "187"], ["frac14", "188"], ["frac12", "189"], ["frac34", "190"], ["iquest", "191"], ["Agrave", "192"], ["Aacute", "193"], ["Acirc", "194"], ["Atilde", "195"], ["Auml", "196"], ["Aring", "197"], ["AElig", "198"], ["Ccedil", "199"], ["Egrave", "200"], ["Eacute", "201"], ["Ecirc", "202"], ["Euml", "203"], ["Igrave", "204"], ["Iacute", "205"], ["Icirc", "206"], ["Iuml", "207"], ["ETH", "208"], ["Ntilde", "209"], ["Ograve", "210"], ["Oacute", "211"], ["Ocirc", "212"], ["Otilde", "213"], ["Ouml", "214"], ["times", "215"], ["Oslash", "216"], ["Ugrave", "217"], ["Uacute", "218"], ["Ucirc", "219"], ["Uuml", "220"], ["Yacute", "221"], ["THORN", "222"], ["szlig", "223"], ["agrave", "224"], ["aacute", "225"], ["acirc", "226"], ["atilde", "227"], ["auml", "228"], ["aring", "229"], ["aelig", "230"], ["ccedil", "231"], ["egrave", "232"], ["eacute", "233"], ["ecirc", "234"], ["euml", "235"], ["igrave", "236"], ["iacute", "237"], ["icirc", "238"], ["iuml", "239"], ["eth", "240"], ["ntilde", "241"], ["ograve", "242"], ["oacute", "243"], ["ocirc", "244"], ["otilde", "245"], ["ouml", "246"], ["divide", "247"], ["oslash", "248"], ["ugrave", "249"], ["uacute", "250"], ["ucirc", "251"], ["uuml", "252"], ["yacute", "253"], ["thorn", "254"], ["yuml", "255"]]; }  return Entities.ISO8859_1_ARRAY; }

        static HTML40_ARRAY: string[][]; public static HTML40_ARRAY_$LI$(): string[][] { Entities.__static_initialize(); if (Entities.HTML40_ARRAY == null) { Entities.HTML40_ARRAY = [["fnof", "402"], ["Alpha", "913"], ["Beta", "914"], ["Gamma", "915"], ["Delta", "916"], ["Epsilon", "917"], ["Zeta", "918"], ["Eta", "919"], ["Theta", "920"], ["Iota", "921"], ["Kappa", "922"], ["Lambda", "923"], ["Mu", "924"], ["Nu", "925"], ["Xi", "926"], ["Omicron", "927"], ["Pi", "928"], ["Rho", "929"], ["Sigma", "931"], ["Tau", "932"], ["Upsilon", "933"], ["Phi", "934"], ["Chi", "935"], ["Psi", "936"], ["Omega", "937"], ["alpha", "945"], ["beta", "946"], ["gamma", "947"], ["delta", "948"], ["epsilon", "949"], ["zeta", "950"], ["eta", "951"], ["theta", "952"], ["iota", "953"], ["kappa", "954"], ["lambda", "955"], ["mu", "956"], ["nu", "957"], ["xi", "958"], ["omicron", "959"], ["pi", "960"], ["rho", "961"], ["sigmaf", "962"], ["sigma", "963"], ["tau", "964"], ["upsilon", "965"], ["phi", "966"], ["chi", "967"], ["psi", "968"], ["omega", "969"], ["thetasym", "977"], ["upsih", "978"], ["piv", "982"], ["bull", "8226"], ["hellip", "8230"], ["prime", "8242"], ["Prime", "8243"], ["oline", "8254"], ["frasl", "8260"], ["weierp", "8472"], ["image", "8465"], ["real", "8476"], ["trade", "8482"], ["alefsym", "8501"], ["larr", "8592"], ["uarr", "8593"], ["rarr", "8594"], ["darr", "8595"], ["harr", "8596"], ["crarr", "8629"], ["lArr", "8656"], ["uArr", "8657"], ["rArr", "8658"], ["dArr", "8659"], ["hArr", "8660"], ["forall", "8704"], ["part", "8706"], ["exist", "8707"], ["empty", "8709"], ["nabla", "8711"], ["isin", "8712"], ["notin", "8713"], ["ni", "8715"], ["prod", "8719"], ["sum", "8721"], ["minus", "8722"], ["lowast", "8727"], ["radic", "8730"], ["prop", "8733"], ["infin", "8734"], ["ang", "8736"], ["and", "8743"], ["or", "8744"], ["cap", "8745"], ["cup", "8746"], ["int", "8747"], ["there4", "8756"], ["sim", "8764"], ["cong", "8773"], ["asymp", "8776"], ["ne", "8800"], ["equiv", "8801"], ["le", "8804"], ["ge", "8805"], ["sub", "8834"], ["sup", "8835"], ["sube", "8838"], ["supe", "8839"], ["oplus", "8853"], ["otimes", "8855"], ["perp", "8869"], ["sdot", "8901"], ["lceil", "8968"], ["rceil", "8969"], ["lfloor", "8970"], ["rfloor", "8971"], ["lang", "9001"], ["rang", "9002"], ["loz", "9674"], ["spades", "9824"], ["clubs", "9827"], ["hearts", "9829"], ["diams", "9830"], ["OElig", "338"], ["oelig", "339"], ["Scaron", "352"], ["scaron", "353"], ["Yuml", "376"], ["circ", "710"], ["tilde", "732"], ["ensp", "8194"], ["emsp", "8195"], ["thinsp", "8201"], ["zwnj", "8204"], ["zwj", "8205"], ["lrm", "8206"], ["rlm", "8207"], ["ndash", "8211"], ["mdash", "8212"], ["lsquo", "8216"], ["rsquo", "8217"], ["sbquo", "8218"], ["ldquo", "8220"], ["rdquo", "8221"], ["bdquo", "8222"], ["dagger", "8224"], ["Dagger", "8225"], ["permil", "8240"], ["lsaquo", "8249"], ["rsaquo", "8250"], ["euro", "8364"]]; }  return Entities.HTML40_ARRAY; }

        /**
         * <p>
         * The set of entities supported by standard XML.
         * </p>
         */
        public static XML: Entities; public static XML_$LI$(): Entities { Entities.__static_initialize();  return Entities.XML; }

        /**
         * <p>
         * The set of entities supported by HTML 3.2.
         * </p>
         */
        public static HTML32: Entities; public static HTML32_$LI$(): Entities { Entities.__static_initialize();  return Entities.HTML32; }

        /**
         * <p>
         * The set of entities supported by HTML 4.0.
         * </p>
         */
        public static HTML40: Entities; public static HTML40_$LI$(): Entities { Entities.__static_initialize();  return Entities.HTML40; }

        static  __static_initializer_0() {
            const xml: Entities = new Entities();
            xml.addEntities(Entities.BASIC_ARRAY_$LI$());
            xml.addEntities(Entities.APOS_ARRAY_$LI$());
            Entities.XML = xml;
        }

        static  __static_initializer_1() {
            const html32: Entities = new Entities();
            html32.addEntities(Entities.BASIC_ARRAY_$LI$());
            html32.addEntities(Entities.ISO8859_1_ARRAY_$LI$());
            Entities.HTML32 = html32;
        }

        static  __static_initializer_2() {
            const html40: Entities = new Entities();
            Entities.fillWithHtml40Entities(html40);
            Entities.HTML40 = html40;
        }

        /**
         * <p>
         * Fills the specified entities instance with HTML 40 entities.
         * </p>
         * 
         * @param {org.openprovenance.apache.commons.lang.Entities} entities
         * the instance to be filled.
         */
        static fillWithHtml40Entities(entities: Entities) {
            entities.addEntities(Entities.BASIC_ARRAY_$LI$());
            entities.addEntities(Entities.ISO8859_1_ARRAY_$LI$());
            entities.addEntities(Entities.HTML40_ARRAY_$LI$());
        }

        /*private*/ map: Entities.EntityMap;

        public constructor(emap?: any) {
            if (((emap != null && (emap.constructor != null && emap.constructor["__interfaces"] != null && emap.constructor["__interfaces"].indexOf("org.openprovenance.apache.commons.lang.Entities.EntityMap") >= 0)) || emap === null)) {
                let __args = arguments;
                if (this.map === undefined) { this.map = null; } 
                this.map = emap;
            } else if (emap === undefined) {
                let __args = arguments;
                if (this.map === undefined) { this.map = null; } 
                this.map = new Entities.LookupEntityMap();
            } else throw new Error('invalid overload');
        }

        /**
         * <p>
         * Adds entities to this generalEntity.
         * </p>
         * 
         * @param {java.lang.String[][]} entityArray
         * array of entities to be added
         */
        public addEntities(entityArray: string[][]) {
            for(let i: number = 0; i < entityArray.length; ++i) {{
                this.addEntity(entityArray[i][0], /* parseInt */parseInt(entityArray[i][1]));
            };}
        }

        /**
         * <p>
         * Add an generalEntity to this generalEntity.
         * </p>
         * 
         * @param {string} name
         * name of the generalEntity
         * @param {number} value
         * vale of the generalEntity
         */
        public addEntity(name: string, value: number) {
            this.map.add(name, value);
        }

        /**
         * <p>
         * Returns the name of the generalEntity identified by the specified value.
         * </p>
         * 
         * @param {number} value
         * the value to locate
         * @return {string} generalEntity name associated with the specified value
         */
        public entityName(value: number): string {
            return this.map.name(value);
        }

        /**
         * <p>
         * Returns the value of the generalEntity identified by the specified name.
         * </p>
         * 
         * @param {string} name
         * the name to locate
         * @return {number} generalEntity value associated with the specified name
         */
        public entityValue(name: string): number {
            return this.map.value(name);
        }

        public escape$java_lang_String(str: string): string {
            const stringWriter: java.io.StringWriter = this.createStringWriter(str);
            try {
                this.escape$java_io_Writer$java_lang_String(stringWriter, str);
            } catch(e) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(e);
            }
            return stringWriter.toString();
        }

        public escape$java_io_Writer$java_lang_String(writer: java.io.Writer, str: string) {
            const len: number = str.length;
            for(let i: number = 0; i < len; i++) {{
                const c: string = str.charAt(i);
                const entityName: string = this.entityName((c).charCodeAt(0));
                if (entityName == null){
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(c) > 127){
                        writer.write("&#");
                        writer.write(/* toString */(''+(c)));
                        writer.write((';').charCodeAt(0));
                    } else {
                        writer.write((c).charCodeAt(0));
                    }
                } else {
                    writer.write(('&').charCodeAt(0));
                    writer.write(entityName);
                    writer.write((';').charCodeAt(0));
                }
            };}
        }

        /**
         * <p>
         * Escapes the characters in the <code>String</code> passed and writes the result to the <code>Writer</code>
         * passed.
         * </p>
         * 
         * @param {java.io.Writer} writer
         * The <code>Writer</code> to write the results of the escaping to. Assumed to be a non-null value.
         * @param {string} str
         * The <code>String</code> to escape. Assumed to be a non-null value.
         * @throws IOException
         * when <code>Writer</code> passed throws the exception from calls to the {@link Writer#write(int)}
         * methods.
         * 
         * @see #escape(String)
         * @see Writer
         */
        public escape(writer?: any, str?: any) {
            if (((writer != null && writer instanceof <any>java.io.Writer) || writer === null) && ((typeof str === 'string') || str === null)) {
                return <any>this.escape$java_io_Writer$java_lang_String(writer, str);
            } else if (((typeof writer === 'string') || writer === null) && str === undefined) {
                return <any>this.escape$java_lang_String(writer);
            } else throw new Error('invalid overload');
        }

        public unescape$java_lang_String(str: string): string {
            const firstAmp: number = str.indexOf('&');
            if (firstAmp < 0){
                return str;
            } else {
                const stringWriter: java.io.StringWriter = this.createStringWriter(str);
                try {
                    this.doUnescape(stringWriter, str, firstAmp);
                } catch(e) {
                    throw new org.openprovenance.apache.commons.lang.UnhandledException(e);
                }
                return stringWriter.toString();
            }
        }

        /**
         * Make the StringWriter 10% larger than the source String to avoid growing the writer
         * 
         * @param {string} str The source string
         * @return {java.io.StringWriter} A newly created StringWriter
         * @private
         */
        createStringWriter(str: string): java.io.StringWriter {
            return new java.io.StringWriter((<number>(str.length + (str.length * 0.1))|0));
        }

        public unescape$java_io_Writer$java_lang_String(writer: java.io.Writer, str: string) {
            const firstAmp: number = str.indexOf('&');
            if (firstAmp < 0){
                writer.write(str);
                return;
            } else {
                this.doUnescape(writer, str, firstAmp);
            }
        }

        /**
         * <p>
         * Unescapes the escaped entities in the <code>String</code> passed and writes the result to the
         * <code>Writer</code> passed.
         * </p>
         * 
         * @param {java.io.Writer} writer
         * The <code>Writer</code> to write the results to; assumed to be non-null.
         * @param {string} str
         * The source <code>String</code> to unescape; assumed to be non-null.
         * @throws IOException
         * when <code>Writer</code> passed throws the exception from calls to the {@link Writer#write(int)}
         * methods.
         * 
         * @see #escape(String)
         * @see Writer
         */
        public unescape(writer?: any, str?: any) {
            if (((writer != null && writer instanceof <any>java.io.Writer) || writer === null) && ((typeof str === 'string') || str === null)) {
                return <any>this.unescape$java_io_Writer$java_lang_String(writer, str);
            } else if (((typeof writer === 'string') || writer === null) && str === undefined) {
                return <any>this.unescape$java_lang_String(writer);
            } else throw new Error('invalid overload');
        }

        /**
         * Underlying unescape method that allows the optimisation of not starting from the 0 index again.
         * 
         * @param {java.io.Writer} writer
         * The <code>Writer</code> to write the results to; assumed to be non-null.
         * @param {string} str
         * The source <code>String</code> to unescape; assumed to be non-null.
         * @param {number} firstAmp
         * The <code>int</code> index of the first ampersand in the source String.
         * @throws IOException
         * when <code>Writer</code> passed throws the exception from calls to the {@link Writer#write(int)}
         * methods.
         * @private
         */
        doUnescape(writer: java.io.Writer, str: string, firstAmp: number) {
            writer.write(str, 0, firstAmp);
            const len: number = str.length;
            for(let i: number = firstAmp; i < len; i++) {{
                const c: string = str.charAt(i);
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(c) == '&'.charCodeAt(0)){
                    const nextIdx: number = i + 1;
                    const semiColonIdx: number = str.indexOf(';', nextIdx);
                    if (semiColonIdx === -1){
                        writer.write((c).charCodeAt(0));
                        continue;
                    }
                    const amphersandIdx: number = str.indexOf('&', i + 1);
                    if (amphersandIdx !== -1 && amphersandIdx < semiColonIdx){
                        writer.write((c).charCodeAt(0));
                        continue;
                    }
                    const entityContent: string = str.substring(nextIdx, semiColonIdx);
                    let entityValue: number = -1;
                    const entityContentLen: number = entityContent.length;
                    if (entityContentLen > 0){
                        if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(entityContent.charAt(0)) == '#'.charCodeAt(0)){
                            if (entityContentLen > 1){
                                const isHexChar: string = entityContent.charAt(1);
                                try {
                                    switch((isHexChar).charCodeAt(0)) {
                                    case 88 /* 'X' */:
                                    case 120 /* 'x' */:
                                        {
                                            entityValue = /* parseInt */parseInt(entityContent.substring(2), 16);
                                            break;
                                        };
                                    default:
                                        {
                                            entityValue = /* parseInt */parseInt(entityContent.substring(1), 10);
                                        };
                                    }
                                    if (entityValue > 65535){
                                        entityValue = -1;
                                    }
                                } catch(e) {
                                    entityValue = -1;
                                }
                            }
                        } else {
                            entityValue = this.entityValue(entityContent);
                        }
                    }
                    if (entityValue === -1){
                        writer.write(('&').charCodeAt(0));
                        writer.write(entityContent);
                        writer.write((';').charCodeAt(0));
                    } else {
                        writer.write(entityValue);
                    }
                    i = semiColonIdx;
                } else {
                    writer.write((c).charCodeAt(0));
                }
            };}
        }
    }
    Entities["__class"] = "org.openprovenance.apache.commons.lang.Entities";


    export namespace Entities {

        export interface EntityMap {
            /**
             * <p>
             * Add an entry to this generalEntity map.
             * </p>
             * 
             * @param {string} name
             * the generalEntity name
             * @param {number} value
             * the generalEntity value
             */
            add(name: string, value: number);

            /**
             * <p>
             * Returns the name of the generalEntity identified by the specified value.
             * </p>
             * 
             * @param {number} value
             * the value to locate
             * @return {string} generalEntity name associated with the specified value
             */
            name(value: number): string;

            /**
             * <p>
             * Returns the value of the generalEntity identified by the specified name.
             * </p>
             * 
             * @param {string} name
             * the name to locate
             * @return {number} generalEntity value associated with the specified name
             */
            value(name: string): number;
        }

        export class PrimitiveEntityMap implements Entities.EntityMap {
            mapNameToValue: any;

            mapValueToName: org.openprovenance.apache.commons.lang.IntHashMap;

            /**
             * {@inheritDoc}
             * @param {string} name
             * @param {number} value
             */
            public add(name: string, value: number) {
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.mapNameToValue, name, new Number(value).valueOf());
                this.mapValueToName.put(value, name);
            }

            /**
             * {@inheritDoc}
             * @param {number} value
             * @return {string}
             */
            public name(value: number): string {
                return <string>this.mapValueToName.get(value);
            }

            /**
             * {@inheritDoc}
             * @param {string} name
             * @return {number}
             */
            public value(name: string): number {
                const value: any = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.mapNameToValue, name);
                if (value == null){
                    return -1;
                }
                return /* intValue */((<number>value)|0);
            }

            constructor() {
                this.mapNameToValue = <any>({});
                this.mapValueToName = new org.openprovenance.apache.commons.lang.IntHashMap();
            }
        }
        PrimitiveEntityMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.PrimitiveEntityMap";
        PrimitiveEntityMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];



        export abstract class MapIntMap implements org.openprovenance.apache.commons.lang.Entities.EntityMap {
            mapNameToValue: any;

            mapValueToName: any;

            constructor(nameToValue: any, valueToName: any) {
                if (this.mapNameToValue === undefined) { this.mapNameToValue = null; }
                if (this.mapValueToName === undefined) { this.mapValueToName = null; }
                this.mapNameToValue = nameToValue;
                this.mapValueToName = valueToName;
            }

            /**
             * {@inheritDoc}
             * @param {string} name
             * @param {number} value
             */
            public add(name: string, value: number) {
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.mapNameToValue, name, new Number(value).valueOf());
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.mapValueToName, new Number(value).valueOf(), name);
            }

            /**
             * {@inheritDoc}
             * @param {number} value
             * @return {string}
             */
            public name(value: number): string {
                return <string>/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.mapValueToName, new Number(value).valueOf());
            }

            /**
             * {@inheritDoc}
             * @param {string} name
             * @return {number}
             */
            public value(name: string): number {
                const value: any = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.mapNameToValue, name);
                if (value == null){
                    return -1;
                }
                return /* intValue */((<number>value)|0);
            }
        }
        MapIntMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.MapIntMap";
        MapIntMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];



        /**
         * Constructs a new instance of <code>ArrayEntityMap</code> specifying the size by which the array should
         * grow.
         * 
         * @param {number} growBy
         * array will be initialized to and will grow by this amount
         * @class
         */
        export class ArrayEntityMap implements Entities.EntityMap {
            growBy: number;

            size: number;

            names: string[];

            values: number[];

            public constructor(growBy?: any) {
                if (((typeof growBy === 'number') || growBy === null)) {
                    let __args = arguments;
                    if (this.growBy === undefined) { this.growBy = 0; } 
                    if (this.names === undefined) { this.names = null; } 
                    if (this.values === undefined) { this.values = null; } 
                    this.size = 0;
                    this.growBy = growBy;
                    this.names = (s => { let a=[]; while(s-->0) a.push(null); return a; })(growBy);
                    this.values = (s => { let a=[]; while(s-->0) a.push(0); return a; })(growBy);
                } else if (growBy === undefined) {
                    let __args = arguments;
                    if (this.growBy === undefined) { this.growBy = 0; } 
                    if (this.names === undefined) { this.names = null; } 
                    if (this.values === undefined) { this.values = null; } 
                    this.size = 0;
                    this.growBy = 100;
                    this.names = (s => { let a=[]; while(s-->0) a.push(null); return a; })(this.growBy);
                    this.values = (s => { let a=[]; while(s-->0) a.push(0); return a; })(this.growBy);
                } else throw new Error('invalid overload');
            }

            /**
             * {@inheritDoc}
             * @param {string} name
             * @param {number} value
             */
            public add(name: string, value: number) {
                this.ensureCapacity(this.size + 1);
                this.names[this.size] = name;
                this.values[this.size] = value;
                this.size++;
            }

            /**
             * Verifies the capacity of the generalEntity array, adjusting the size if necessary.
             * 
             * @param {number} capacity
             * size the array should be
             */
            ensureCapacity(capacity: number) {
                if (capacity > this.names.length){
                    const newSize: number = Math.max(capacity, this.size + this.growBy);
                    const newNames: string[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })(newSize);
                    /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.names, 0, newNames, 0, this.size);
                    this.names = newNames;
                    const newValues: number[] = (s => { let a=[]; while(s-->0) a.push(0); return a; })(newSize);
                    /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.values, 0, newValues, 0, this.size);
                    this.values = newValues;
                }
            }

            /**
             * {@inheritDoc}
             * @param {number} value
             * @return {string}
             */
            public name(value: number): string {
                for(let i: number = 0; i < this.size; ++i) {{
                    if (this.values[i] === value){
                        return this.names[i];
                    }
                };}
                return null;
            }

            /**
             * {@inheritDoc}
             * @param {string} name
             * @return {number}
             */
            public value(name: string): number {
                for(let i: number = 0; i < this.size; ++i) {{
                    if (this.names[i] === name){
                        return this.values[i];
                    }
                };}
                return -1;
            }
        }
        ArrayEntityMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.ArrayEntityMap";
        ArrayEntityMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];



        export class LookupEntityMap extends Entities.PrimitiveEntityMap {
            __lookupTable: string[];

            static LOOKUP_TABLE_SIZE: number = 256;

            /**
             * {@inheritDoc}
             * @param {number} value
             * @return {string}
             */
            public name(value: number): string {
                if (value < LookupEntityMap.LOOKUP_TABLE_SIZE){
                    return this.lookupTable()[value];
                }
                return super.name(value);
            }

            /**
             * <p>
             * Returns the lookup table for this generalEntity map. The lookup table is created if it has not been previously.
             * </p>
             * 
             * @return {java.lang.String[]} the lookup table
             * @private
             */
            lookupTable(): string[] {
                if (this.__lookupTable == null){
                    this.createLookupTable();
                }
                return this.__lookupTable;
            }

            /**
             * <p>
             * Creates an generalEntity lookup table of LOOKUP_TABLE_SIZE elements, initialized with generalEntity names.
             * </p>
             * @private
             */
            createLookupTable() {
                this.__lookupTable = (s => { let a=[]; while(s-->0) a.push(null); return a; })(LookupEntityMap.LOOKUP_TABLE_SIZE);
                for(let i: number = 0; i < LookupEntityMap.LOOKUP_TABLE_SIZE; ++i) {{
                    this.__lookupTable[i] = super.name(i);
                };}
            }

            constructor() {
                super();
                if (this.__lookupTable === undefined) { this.__lookupTable = null; }
            }
        }
        LookupEntityMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.LookupEntityMap";
        LookupEntityMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];



        /**
         * Constructs a new instance of <code>HashEntityMap</code>.
         * @class
         * @extends org.openprovenance.apache.commons.lang.Entities.MapIntMap
         */
        export class HashEntityMap extends Entities.MapIntMap {
            public constructor() {
                super(<any>({}), <any>({}));
            }
        }
        HashEntityMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.HashEntityMap";
        HashEntityMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];



        /**
         * Constructs a new instance of <code>TreeEntityMap</code>.
         * @class
         * @extends org.openprovenance.apache.commons.lang.Entities.MapIntMap
         */
        export class TreeEntityMap extends Entities.MapIntMap {
            public constructor() {
                super(<any>({}), <any>({}));
            }
        }
        TreeEntityMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.TreeEntityMap";
        TreeEntityMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];



        /**
         * Constructs a new instance of <code>ArrayEntityMap</code> specifying the size by which the underlying array
         * should grow.
         * 
         * @param {number} growBy
         * array will be initialized to and will grow by this amount
         * @class
         * @extends org.openprovenance.apache.commons.lang.Entities.ArrayEntityMap
         */
        export class BinaryEntityMap extends Entities.ArrayEntityMap {
            public constructor(growBy?: any) {
                if (((typeof growBy === 'number') || growBy === null)) {
                    let __args = arguments;
                    super(growBy);
                } else if (growBy === undefined) {
                    let __args = arguments;
                    super();
                } else throw new Error('invalid overload');
            }

            /**
             * Performs a binary search of the generalEntity array for the specified key. This method is based on code in
             * {@link java.util.Arrays}.
             * 
             * @param {number} key
             * the key to be found
             * @return {number} the index of the generalEntity array matching the specified key
             * @private
             */
            binarySearch(key: number): number {
                let low: number = 0;
                let high: number = this.size - 1;
                while((low <= high)) {{
                    const mid: number = (low + high) >>> 1;
                    const midVal: number = this.values[mid];
                    if (midVal < key){
                        low = mid + 1;
                    } else if (midVal > key){
                        high = mid - 1;
                    } else {
                        return mid;
                    }
                }};
                return -(low + 1);
            }

            /**
             * {@inheritDoc}
             * @param {string} name
             * @param {number} value
             */
            public add(name: string, value: number) {
                this.ensureCapacity(this.size + 1);
                let insertAt: number = this.binarySearch(value);
                if (insertAt > 0){
                    return;
                }
                insertAt = -(insertAt + 1);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.values, insertAt, this.values, insertAt + 1, this.size - insertAt);
                this.values[insertAt] = value;
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.names, insertAt, this.names, insertAt + 1, this.size - insertAt);
                this.names[insertAt] = name;
                this.size++;
            }

            /**
             * {@inheritDoc}
             * @param {number} value
             * @return {string}
             */
            public name(value: number): string {
                const index: number = this.binarySearch(value);
                if (index < 0){
                    return null;
                }
                return this.names[index];
            }
        }
        BinaryEntityMap["__class"] = "org.openprovenance.apache.commons.lang.Entities.BinaryEntityMap";
        BinaryEntityMap["__interfaces"] = ["org.openprovenance.apache.commons.lang.Entities.EntityMap"];


    }

}


org.openprovenance.apache.commons.lang.Entities.__static_initialize();
