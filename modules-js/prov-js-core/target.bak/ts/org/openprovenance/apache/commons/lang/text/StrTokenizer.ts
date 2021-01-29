/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.text {
    /**
     * Constructs a tokenizer splitting on the specified delimiter character
     * and handling quotes using the specified quote character.
     * 
     * @param {string} input  the string which is to be parsed
     * @param {string} delim  the field delimiter character
     * @param {string} quote  the field quoted string character
     * @class
     * @author Apache Software Foundation
     */
    export class StrTokenizer {
        static __static_initialized: boolean = false;
        static __static_initialize() { if (!StrTokenizer.__static_initialized) { StrTokenizer.__static_initialized = true; StrTokenizer.__static_initializer_0(); } }

        static CSV_TOKENIZER_PROTOTYPE: StrTokenizer; public static CSV_TOKENIZER_PROTOTYPE_$LI$(): StrTokenizer { StrTokenizer.__static_initialize();  return StrTokenizer.CSV_TOKENIZER_PROTOTYPE; }

        static TSV_TOKENIZER_PROTOTYPE: StrTokenizer; public static TSV_TOKENIZER_PROTOTYPE_$LI$(): StrTokenizer { StrTokenizer.__static_initialize();  return StrTokenizer.TSV_TOKENIZER_PROTOTYPE; }

        static  __static_initializer_0() {
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$().setDelimiterMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.commaMatcher());
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$().setQuoteMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.doubleQuoteMatcher());
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$().setIgnoredMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher());
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$().setTrimmerMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.trimMatcher());
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$().setEmptyTokenAsNull(false);
            StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$().setIgnoreEmptyTokens(false);
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$().setDelimiterMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.tabMatcher());
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$().setQuoteMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.doubleQuoteMatcher());
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$().setIgnoredMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher());
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$().setTrimmerMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.trimMatcher());
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$().setEmptyTokenAsNull(false);
            StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$().setIgnoreEmptyTokens(false);
        }

        /**
         * The text to work on.
         */
        /*private*/ chars: string[];

        /**
         * The parsed tokens
         */
        /*private*/ tokens: string[];

        /**
         * The current iteration position
         */
        /*private*/ tokenPos: number;

        /**
         * The delimiter matcher
         */
        /*private*/ delimMatcher: org.openprovenance.apache.commons.lang.text.StrMatcher;

        /**
         * The quote matcher
         */
        /*private*/ quoteMatcher: org.openprovenance.apache.commons.lang.text.StrMatcher;

        /**
         * The ignored matcher
         */
        /*private*/ ignoredMatcher: org.openprovenance.apache.commons.lang.text.StrMatcher;

        /**
         * The trimmer matcher
         */
        /*private*/ trimmerMatcher: org.openprovenance.apache.commons.lang.text.StrMatcher;

        /**
         * Whether to return empty tokens as null
         */
        /*private*/ emptyAsNull: boolean;

        /**
         * Whether to ignore empty tokens
         */
        /*private*/ ignoreEmptyTokens: boolean;

        /**
         * Returns a clone of <code>CSV_TOKENIZER_PROTOTYPE</code>.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} a clone of <code>CSV_TOKENIZER_PROTOTYPE</code>.
         * @private
         */
        /*private*/ static getCSVClone(): StrTokenizer {
            return <StrTokenizer>/* clone *//* clone */((o: any) => { if (o.clone != undefined) { return (<any>o).clone(); } else { let clone = Object.create(o); for(let p in o) { if (o.hasOwnProperty(p)) clone[p] = o[p]; } return clone; } })(StrTokenizer.CSV_TOKENIZER_PROTOTYPE_$LI$());
        }

        public static getCSVInstance$(): StrTokenizer {
            return StrTokenizer.getCSVClone();
        }

        public static getCSVInstance$java_lang_String(input: string): StrTokenizer {
            const tok: StrTokenizer = StrTokenizer.getCSVClone();
            tok.reset$java_lang_String(input);
            return tok;
        }

        /**
         * Gets a new tokenizer instance which parses Comma Separated Value strings
         * initializing it with the given input.  The default for CSV processing
         * will be trim whitespace from both ends (which can be overridden with
         * the setTrimmer method).
         * 
         * @param {string} input  the text to parse
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} a new tokenizer instance which parses Comma Separated Value strings
         */
        public static getCSVInstance(input?: any): any {
            if (((typeof input === 'string') || input === null)) {
                return <any>org.openprovenance.apache.commons.lang.text.StrTokenizer.getCSVInstance$java_lang_String(input);
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null)) {
                return <any>org.openprovenance.apache.commons.lang.text.StrTokenizer.getCSVInstance$char_A(input);
            } else if (input === undefined) {
                return <any>org.openprovenance.apache.commons.lang.text.StrTokenizer.getCSVInstance$();
            } else throw new Error('invalid overload');
        }

        public static getCSVInstance$char_A(input: string[]): StrTokenizer {
            const tok: StrTokenizer = StrTokenizer.getCSVClone();
            tok.reset$char_A(input);
            return tok;
        }

        /**
         * Returns a clone of <code>TSV_TOKENIZER_PROTOTYPE</code>.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} a clone of <code>TSV_TOKENIZER_PROTOTYPE</code>.
         * @private
         */
        /*private*/ static getTSVClone(): StrTokenizer {
            return <StrTokenizer>/* clone *//* clone */((o: any) => { if (o.clone != undefined) { return (<any>o).clone(); } else { let clone = Object.create(o); for(let p in o) { if (o.hasOwnProperty(p)) clone[p] = o[p]; } return clone; } })(StrTokenizer.TSV_TOKENIZER_PROTOTYPE_$LI$());
        }

        public static getTSVInstance$(): StrTokenizer {
            return StrTokenizer.getTSVClone();
        }

        public static getTSVInstance$java_lang_String(input: string): StrTokenizer {
            const tok: StrTokenizer = StrTokenizer.getTSVClone();
            tok.reset$java_lang_String(input);
            return tok;
        }

        /**
         * Gets a new tokenizer instance which parses Tab Separated Value strings.
         * The default for CSV processing will be trim whitespace from both ends
         * (which can be overridden with the setTrimmer method).
         * @param {string} input  the string to parse
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} a new tokenizer instance which parses Tab Separated Value strings.
         */
        public static getTSVInstance(input?: any): any {
            if (((typeof input === 'string') || input === null)) {
                return <any>org.openprovenance.apache.commons.lang.text.StrTokenizer.getTSVInstance$java_lang_String(input);
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null)) {
                return <any>org.openprovenance.apache.commons.lang.text.StrTokenizer.getTSVInstance$char_A(input);
            } else if (input === undefined) {
                return <any>org.openprovenance.apache.commons.lang.text.StrTokenizer.getTSVInstance$();
            } else throw new Error('invalid overload');
        }

        public static getTSVInstance$char_A(input: string[]): StrTokenizer {
            const tok: StrTokenizer = StrTokenizer.getTSVClone();
            tok.reset$char_A(input);
            return tok;
        }

        public constructor(input?: any, delim?: any, quote?: any) {
            if (((typeof input === 'string') || input === null) && ((delim != null && delim instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || delim === null) && ((quote != null && quote instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || quote === null)) {
                let __args = arguments;
                {
                    let __args = arguments;
                    {
                        let __args = arguments;
                        if (this.chars === undefined) { this.chars = null; } 
                        if (this.tokens === undefined) { this.tokens = null; } 
                        if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                        this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                        this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.emptyAsNull = false;
                        this.ignoreEmptyTokens = true;
                        if (input != null){
                            this.chars = /* toCharArray */(input).split('');
                        } else {
                            this.chars = null;
                        }
                    }
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    (() => {
                        this.setDelimiterMatcher(delim);
                    })();
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setQuoteMatcher(quote);
                })();
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null) && ((delim != null && delim instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || delim === null) && ((quote != null && quote instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || quote === null)) {
                let __args = arguments;
                {
                    let __args = arguments;
                    {
                        let __args = arguments;
                        if (this.chars === undefined) { this.chars = null; } 
                        if (this.tokens === undefined) { this.tokens = null; } 
                        if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                        this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                        this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.emptyAsNull = false;
                        this.ignoreEmptyTokens = true;
                        this.chars = input;
                    }
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    (() => {
                        this.setDelimiterMatcher(delim);
                    })();
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setQuoteMatcher(quote);
                })();
            } else if (((typeof input === 'string') || input === null) && ((typeof delim === 'string') || delim === null) && ((typeof quote === 'string') || quote === null)) {
                let __args = arguments;
                {
                    let __args = arguments;
                    {
                        let __args = arguments;
                        if (this.chars === undefined) { this.chars = null; } 
                        if (this.tokens === undefined) { this.tokens = null; } 
                        if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                        this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                        this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.emptyAsNull = false;
                        this.ignoreEmptyTokens = true;
                        if (input != null){
                            this.chars = /* toCharArray */(input).split('');
                        } else {
                            this.chars = null;
                        }
                    }
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    (() => {
                        this.setDelimiterChar(delim);
                    })();
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setQuoteChar(quote);
                })();
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null) && ((typeof delim === 'string') || delim === null) && ((typeof quote === 'string') || quote === null)) {
                let __args = arguments;
                {
                    let __args = arguments;
                    {
                        let __args = arguments;
                        if (this.chars === undefined) { this.chars = null; } 
                        if (this.tokens === undefined) { this.tokens = null; } 
                        if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                        this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                        this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                        this.emptyAsNull = false;
                        this.ignoreEmptyTokens = true;
                        this.chars = input;
                    }
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    (() => {
                        this.setDelimiterChar(delim);
                    })();
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setQuoteChar(quote);
                })();
            } else if (((typeof input === 'string') || input === null) && ((typeof delim === 'string') || delim === null) && quote === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    if (input != null){
                        this.chars = /* toCharArray */(input).split('');
                    } else {
                        this.chars = null;
                    }
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setDelimiterString(delim);
                })();
            } else if (((typeof input === 'string') || input === null) && ((delim != null && delim instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || delim === null) && quote === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    if (input != null){
                        this.chars = /* toCharArray */(input).split('');
                    } else {
                        this.chars = null;
                    }
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setDelimiterMatcher(delim);
                })();
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null) && ((typeof delim === 'string') || delim === null) && quote === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    this.chars = input;
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setDelimiterString(delim);
                })();
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null) && ((delim != null && delim instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || delim === null) && quote === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    this.chars = input;
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setDelimiterMatcher(delim);
                })();
            } else if (((typeof input === 'string') || input === null) && ((typeof delim === 'string') || delim === null) && quote === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    if (input != null){
                        this.chars = /* toCharArray */(input).split('');
                    } else {
                        this.chars = null;
                    }
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setDelimiterChar(delim);
                })();
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null) && ((typeof delim === 'string') || delim === null) && quote === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    if (this.chars === undefined) { this.chars = null; } 
                    if (this.tokens === undefined) { this.tokens = null; } 
                    if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                    this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                    this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                    this.emptyAsNull = false;
                    this.ignoreEmptyTokens = true;
                    this.chars = input;
                }
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                (() => {
                    this.setDelimiterChar(delim);
                })();
            } else if (((typeof input === 'string') || input === null) && delim === undefined && quote === undefined) {
                let __args = arguments;
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                if (input != null){
                    this.chars = /* toCharArray */(input).split('');
                } else {
                    this.chars = null;
                }
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null) && delim === undefined && quote === undefined) {
                let __args = arguments;
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                this.chars = input;
            } else if (input === undefined && delim === undefined && quote === undefined) {
                let __args = arguments;
                if (this.chars === undefined) { this.chars = null; } 
                if (this.tokens === undefined) { this.tokens = null; } 
                if (this.tokenPos === undefined) { this.tokenPos = 0; } 
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.splitMatcher();
                this.quoteMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.ignoredMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.trimmerMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
                this.emptyAsNull = false;
                this.ignoreEmptyTokens = true;
                this.chars = null;
            } else throw new Error('invalid overload');
        }

        /**
         * Gets the number of tokens found in the String.
         * 
         * @return {number} the number of matched tokens
         */
        public size(): number {
            this.checkTokenized();
            return this.tokens.length;
        }

        /**
         * Gets the next token from the String.
         * Equivalent to {@link #next()} except it returns null rather than
         * throwing {@link NoSuchElementException} when no tokens remain.
         * 
         * @return {string} the next sequential token, or null when no more tokens are found
         */
        public nextToken(): string {
            if (this.hasNext()){
                return this.tokens[this.tokenPos++];
            }
            return null;
        }

        /**
         * Gets the previous token from the String.
         * 
         * @return {string} the previous sequential token, or null when no more tokens are found
         */
        public previousToken(): string {
            if (this.hasPrevious()){
                return this.tokens[--this.tokenPos];
            }
            return null;
        }

        /**
         * Gets a copy of the full token list as an independent modifiable array.
         * 
         * @return {java.lang.String[]} the tokens as a String array
         */
        public getTokenArray(): string[] {
            this.checkTokenized();
            return <string[]>/* clone */this.tokens.slice(0);
        }

        /**
         * Gets a copy of the full token list as an independent modifiable list.
         * 
         * @return {Array} the tokens as a String array
         */
        public getTokenList(): Array<any> {
            this.checkTokenized();
            const list: Array<any> = <any>([]);
            for(let i: number = 0; i < this.tokens.length; i++) {{
                /* add */(list.push(this.tokens[i])>0);
            };}
            return list;
        }

        public reset$(): StrTokenizer {
            this.tokenPos = 0;
            this.tokens = null;
            return this;
        }

        public reset$java_lang_String(input: string): StrTokenizer {
            this.reset$();
            if (input != null){
                this.chars = /* toCharArray */(input).split('');
            } else {
                this.chars = null;
            }
            return this;
        }

        /**
         * Reset this tokenizer, giving it a new input string to parse.
         * In this manner you can re-use a tokenizer with the same settings
         * on multiple input lines.
         * 
         * @param {string} input  the new string to tokenize, null sets no text to parse
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public reset(input?: any): any {
            if (((typeof input === 'string') || input === null)) {
                return <any>this.reset$java_lang_String(input);
            } else if (((input != null && input instanceof <any>Array && (input.length == 0 || input[0] == null ||(typeof input[0] === 'string'))) || input === null)) {
                return <any>this.reset$char_A(input);
            } else if (input === undefined) {
                return <any>this.reset$();
            } else throw new Error('invalid overload');
        }

        public reset$char_A(input: string[]): StrTokenizer {
            this.reset$();
            this.chars = input;
            return this;
        }

        /**
         * Checks whether there are any more tokens.
         * 
         * @return {boolean} true if there are more tokens
         */
        public hasNext(): boolean {
            this.checkTokenized();
            return this.tokenPos < this.tokens.length;
        }

        /**
         * Gets the next token.
         * 
         * @return {*} the next String token
         * @throws NoSuchElementException if there are no more elements
         */
        public next(): any {
            if (this.hasNext()){
                return this.tokens[this.tokenPos++];
            }
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.util.NoSuchElementException','java.lang.Exception'] });
        }

        /**
         * Gets the index of the next token to return.
         * 
         * @return {number} the next token index
         */
        public nextIndex(): number {
            return this.tokenPos;
        }

        /**
         * Checks whether there are any previous tokens that can be iterated to.
         * 
         * @return {boolean} true if there are previous tokens
         */
        public hasPrevious(): boolean {
            this.checkTokenized();
            return this.tokenPos > 0;
        }

        /**
         * Gets the token previous to the last returned token.
         * 
         * @return {*} the previous token
         */
        public previous(): any {
            if (this.hasPrevious()){
                return this.tokens[--this.tokenPos];
            }
            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.util.NoSuchElementException','java.lang.Exception'] });
        }

        /**
         * Gets the index of the previous token.
         * 
         * @return {number} the previous token index
         */
        public previousIndex(): number {
            return this.tokenPos - 1;
        }

        /**
         * Unsupported ListIterator operation.
         * 
         * @throws UnsupportedOperationException always
         */
        public remove() {
            throw Object.defineProperty(new Error("remove() is unsupported"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * Unsupported ListIterator operation.
         * @param {*} obj this parameter ignored.
         * @throws UnsupportedOperationException always
         */
        public set(obj: any) {
            throw Object.defineProperty(new Error("set() is unsupported"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * Unsupported ListIterator operation.
         * @param {*} obj this parameter ignored.
         * @throws UnsupportedOperationException always
         */
        public add(obj: any) {
            throw Object.defineProperty(new Error("add() is unsupported"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * Checks if tokenization has been done, and if not then do it.
         * @private
         */
        /*private*/ checkTokenized() {
            if (this.tokens == null){
                if (this.chars == null){
                    const split: Array<any> = this.tokenize(null, 0, 0);
                    this.tokens = <string[]>/* toArray */split.slice(0);
                } else {
                    const split: Array<any> = this.tokenize(this.chars, 0, this.chars.length);
                    this.tokens = <string[]>/* toArray */split.slice(0);
                }
            }
        }

        /**
         * Internal method to performs the tokenization.
         * <p>
         * Most users of this class do not need to call this method. This method
         * will be called automatically by other (public) methods when required.
         * <p>
         * This method exists to allow subclasses to add code before or after the
         * tokenization. For example, a subclass could alter the character array,
         * offset or count to be parsed, or call the tokenizer multiple times on
         * multiple strings. It is also be possible to filter the results.
         * <p>
         * <code>StrTokenizer</code> will always pass a zero offset and a count
         * equal to the length of the array to this method, however a subclass
         * may pass other values, or even an entirely different array.
         * 
         * @param {char[]} chars  the character array being tokenized, may be null
         * @param {number} offset  the start position within the character array, must be valid
         * @param {number} count  the number of characters to tokenize, must be valid
         * @return {Array} the modifiable list of String tokens, unmodifiable if null array or zero count
         */
        tokenize(chars: string[], offset: number, count: number): Array<any> {
            if (chars == null || count === 0){
                return java.util.Collections.EMPTY_LIST;
            }
            const buf: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder();
            const tokens: Array<any> = <any>([]);
            let pos: number = offset;
            while((pos >= 0 && pos < count)) {{
                pos = this.readNextToken(chars, pos, count, buf, tokens);
                if (pos >= count){
                    this.addToken(tokens, "");
                }
            }};
            return tokens;
        }

        /**
         * Adds a token to a list, paying attention to the parameters we've set.
         * 
         * @param {Array} list  the list to add to
         * @param {string} tok  the token to add
         * @private
         */
        /*private*/ addToken(list: Array<any>, tok: string) {
            if (tok == null || tok.length === 0){
                if (this.isIgnoreEmptyTokens()){
                    return;
                }
                if (this.isEmptyTokenAsNull()){
                    tok = null;
                }
            }
            /* add */(list.push(tok)>0);
        }

        /**
         * Reads character by character through the String to get the next token.
         * 
         * @param {char[]} chars  the character array being tokenized
         * @param {number} start  the first character of field
         * @param {number} len  the length of the character array being tokenized
         * @param {org.openprovenance.apache.commons.lang.text.StrBuilder} workArea  a temporary work area
         * @param {Array} tokens  the list of parsed tokens
         * @return {number} the starting position of the next field (the character
         * immediately after the delimiter), or -1 if end of string found
         * @private
         */
        /*private*/ readNextToken(chars: string[], start: number, len: number, workArea: org.openprovenance.apache.commons.lang.text.StrBuilder, tokens: Array<any>): number {
            while((start < len)) {{
                const removeLen: number = Math.max(this.getIgnoredMatcher().isMatch$char_A$int$int$int(chars, start, start, len), this.getTrimmerMatcher().isMatch$char_A$int$int$int(chars, start, start, len));
                if (removeLen === 0 || this.getDelimiterMatcher().isMatch$char_A$int$int$int(chars, start, start, len) > 0 || this.getQuoteMatcher().isMatch$char_A$int$int$int(chars, start, start, len) > 0){
                    break;
                }
                start += removeLen;
            }};
            if (start >= len){
                this.addToken(tokens, "");
                return -1;
            }
            const delimLen: number = this.getDelimiterMatcher().isMatch$char_A$int$int$int(chars, start, start, len);
            if (delimLen > 0){
                this.addToken(tokens, "");
                return start + delimLen;
            }
            const quoteLen: number = this.getQuoteMatcher().isMatch$char_A$int$int$int(chars, start, start, len);
            if (quoteLen > 0){
                return this.readWithQuotes(chars, start + quoteLen, len, workArea, tokens, start, quoteLen);
            }
            return this.readWithQuotes(chars, start, len, workArea, tokens, 0, 0);
        }

        /**
         * Reads a possibly quoted string token.
         * 
         * @param {char[]} chars  the character array being tokenized
         * @param {number} start  the first character of field
         * @param {number} len  the length of the character array being tokenized
         * @param {org.openprovenance.apache.commons.lang.text.StrBuilder} workArea  a temporary work area
         * @param {Array} tokens  the list of parsed tokens
         * @param {number} quoteStart  the start position of the matched quote, 0 if no quoting
         * @param {number} quoteLen  the length of the matched quote, 0 if no quoting
         * @return {number} the starting position of the next field (the character
         * immediately after the delimiter, or if end of string found,
         * then the length of string
         * @private
         */
        /*private*/ readWithQuotes(chars: string[], start: number, len: number, workArea: org.openprovenance.apache.commons.lang.text.StrBuilder, tokens: Array<any>, quoteStart: number, quoteLen: number): number {
            workArea.clear();
            let pos: number = start;
            let quoting: boolean = (quoteLen > 0);
            let trimStart: number = 0;
            while((pos < len)) {{
                if (quoting){
                    if (this.isQuote(chars, pos, len, quoteStart, quoteLen)){
                        if (this.isQuote(chars, pos + quoteLen, len, quoteStart, quoteLen)){
                            workArea.append$char_A$int$int(chars, pos, quoteLen);
                            pos += (quoteLen * 2);
                            trimStart = workArea.size();
                            continue;
                        }
                        quoting = false;
                        pos += quoteLen;
                        continue;
                    }
                    workArea.append$char(chars[pos++]);
                    trimStart = workArea.size();
                } else {
                    const delimLen: number = this.getDelimiterMatcher().isMatch$char_A$int$int$int(chars, pos, start, len);
                    if (delimLen > 0){
                        this.addToken(tokens, workArea.substring$int$int(0, trimStart));
                        return pos + delimLen;
                    }
                    if (quoteLen > 0){
                        if (this.isQuote(chars, pos, len, quoteStart, quoteLen)){
                            quoting = true;
                            pos += quoteLen;
                            continue;
                        }
                    }
                    const ignoredLen: number = this.getIgnoredMatcher().isMatch$char_A$int$int$int(chars, pos, start, len);
                    if (ignoredLen > 0){
                        pos += ignoredLen;
                        continue;
                    }
                    const trimmedLen: number = this.getTrimmerMatcher().isMatch$char_A$int$int$int(chars, pos, start, len);
                    if (trimmedLen > 0){
                        workArea.append$char_A$int$int(chars, pos, trimmedLen);
                        pos += trimmedLen;
                        continue;
                    }
                    workArea.append$char(chars[pos++]);
                    trimStart = workArea.size();
                }
            }};
            this.addToken(tokens, workArea.substring$int$int(0, trimStart));
            return -1;
        }

        /**
         * Checks if the characters at the index specified match the quote
         * already matched in readNextToken().
         * 
         * @param {char[]} chars  the character array being tokenized
         * @param {number} pos  the position to check for a quote
         * @param {number} len  the length of the character array being tokenized
         * @param {number} quoteStart  the start position of the matched quote, 0 if no quoting
         * @param {number} quoteLen  the length of the matched quote, 0 if no quoting
         * @return {boolean} true if a quote is matched
         * @private
         */
        /*private*/ isQuote(chars: string[], pos: number, len: number, quoteStart: number, quoteLen: number): boolean {
            for(let i: number = 0; i < quoteLen; i++) {{
                if ((pos + i) >= len || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[pos + i]) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[quoteStart + i])){
                    return false;
                }
            };}
            return true;
        }

        /**
         * Gets the field delimiter matcher.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} the delimiter matcher in use
         */
        public getDelimiterMatcher(): org.openprovenance.apache.commons.lang.text.StrMatcher {
            return this.delimMatcher;
        }

        /**
         * Sets the field delimiter matcher.
         * <p>
         * The delimitier is used to separate one token from another.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrMatcher} delim  the delimiter matcher to use
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setDelimiterMatcher(delim: org.openprovenance.apache.commons.lang.text.StrMatcher): StrTokenizer {
            if (delim == null){
                this.delimMatcher = org.openprovenance.apache.commons.lang.text.StrMatcher.noneMatcher();
            } else {
                this.delimMatcher = delim;
            }
            return this;
        }

        /**
         * Sets the field delimiter character.
         * 
         * @param {string} delim  the delimiter character to use
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setDelimiterChar(delim: string): StrTokenizer {
            return this.setDelimiterMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.charMatcher(delim));
        }

        /**
         * Sets the field delimiter string.
         * 
         * @param {string} delim  the delimiter string to use
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setDelimiterString(delim: string): StrTokenizer {
            return this.setDelimiterMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.stringMatcher(delim));
        }

        /**
         * Gets the quote matcher currently in use.
         * <p>
         * The quote character is used to wrap data between the tokens.
         * This enables delimiters to be entered as data.
         * The default value is '"' (double quote).
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} the quote matcher in use
         */
        public getQuoteMatcher(): org.openprovenance.apache.commons.lang.text.StrMatcher {
            return this.quoteMatcher;
        }

        /**
         * Set the quote matcher to use.
         * <p>
         * The quote character is used to wrap data between the tokens.
         * This enables delimiters to be entered as data.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrMatcher} quote  the quote matcher to use, null ignored
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setQuoteMatcher(quote: org.openprovenance.apache.commons.lang.text.StrMatcher): StrTokenizer {
            if (quote != null){
                this.quoteMatcher = quote;
            }
            return this;
        }

        /**
         * Sets the quote character to use.
         * <p>
         * The quote character is used to wrap data between the tokens.
         * This enables delimiters to be entered as data.
         * 
         * @param {string} quote  the quote character to use
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setQuoteChar(quote: string): StrTokenizer {
            return this.setQuoteMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.charMatcher(quote));
        }

        /**
         * Gets the ignored character matcher.
         * <p>
         * These characters are ignored when parsing the String, unless they are
         * within a quoted region.
         * The default value is not to ignore anything.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} the ignored matcher in use
         */
        public getIgnoredMatcher(): org.openprovenance.apache.commons.lang.text.StrMatcher {
            return this.ignoredMatcher;
        }

        /**
         * Set the matcher for characters to ignore.
         * <p>
         * These characters are ignored when parsing the String, unless they are
         * within a quoted region.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrMatcher} ignored  the ignored matcher to use, null ignored
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setIgnoredMatcher(ignored: org.openprovenance.apache.commons.lang.text.StrMatcher): StrTokenizer {
            if (ignored != null){
                this.ignoredMatcher = ignored;
            }
            return this;
        }

        /**
         * Set the character to ignore.
         * <p>
         * This character is ignored when parsing the String, unless it is
         * within a quoted region.
         * 
         * @param {string} ignored  the ignored character to use
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setIgnoredChar(ignored: string): StrTokenizer {
            return this.setIgnoredMatcher(org.openprovenance.apache.commons.lang.text.StrMatcher.charMatcher(ignored));
        }

        /**
         * Gets the trimmer character matcher.
         * <p>
         * These characters are trimmed off on each side of the delimiter
         * until the token or quote is found.
         * The default value is not to trim anything.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} the trimmer matcher in use
         */
        public getTrimmerMatcher(): org.openprovenance.apache.commons.lang.text.StrMatcher {
            return this.trimmerMatcher;
        }

        /**
         * Sets the matcher for characters to trim.
         * <p>
         * These characters are trimmed off on each side of the delimiter
         * until the token or quote is found.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrMatcher} trimmer  the trimmer matcher to use, null ignored
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setTrimmerMatcher(trimmer: org.openprovenance.apache.commons.lang.text.StrMatcher): StrTokenizer {
            if (trimmer != null){
                this.trimmerMatcher = trimmer;
            }
            return this;
        }

        /**
         * Gets whether the tokenizer currently returns empty tokens as null.
         * The default for this property is false.
         * 
         * @return {boolean} true if empty tokens are returned as null
         */
        public isEmptyTokenAsNull(): boolean {
            return this.emptyAsNull;
        }

        /**
         * Sets whether the tokenizer should return empty tokens as null.
         * The default for this property is false.
         * 
         * @param {boolean} emptyAsNull  whether empty tokens are returned as null
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setEmptyTokenAsNull(emptyAsNull: boolean): StrTokenizer {
            this.emptyAsNull = emptyAsNull;
            return this;
        }

        /**
         * Gets whether the tokenizer currently ignores empty tokens.
         * The default for this property is true.
         * 
         * @return {boolean} true if empty tokens are not returned
         */
        public isIgnoreEmptyTokens(): boolean {
            return this.ignoreEmptyTokens;
        }

        /**
         * Sets whether the tokenizer should ignore and not return empty tokens.
         * The default for this property is true.
         * 
         * @param {boolean} ignoreEmptyTokens  whether empty tokens are not returned
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} this, to enable chaining
         */
        public setIgnoreEmptyTokens(ignoreEmptyTokens: boolean): StrTokenizer {
            this.ignoreEmptyTokens = ignoreEmptyTokens;
            return this;
        }

        /**
         * Gets the String content that the tokenizer is parsing.
         * 
         * @return {string} the string content being parsed
         */
        public getContent(): string {
            if (this.chars == null){
                return null;
            }
            return this.chars.join('');
        }

        /**
         * Creates a new instance of this Tokenizer. The new instance is reset so
         * that it will be at the start of the token list.
         * If a {@link CloneNotSupportedException} is caught, return <code>null</code>.
         * 
         * @return {*} a new instance of this Tokenizer which has been reset.
         */
        public clone(): any {
            try {
                return this.cloneReset();
            } catch(ex) {
                return null;
            }
        }

        /**
         * Creates a new instance of this Tokenizer. The new instance is reset so that
         * it will be at the start of the token list.
         * 
         * @return {*} a new instance of this Tokenizer which has been reset.
         * @throws CloneNotSupportedException if there is a problem cloning
         */
        cloneReset(): any {
            const cloned: StrTokenizer = <StrTokenizer>/* clone *//* clone */((o: any) => { let clone = Object.create(o); for(let p in o) { if (o.hasOwnProperty(p)) clone[p] = o[p]; } return clone; })(this);
            if (cloned.chars != null){
                cloned.chars = <string[]>/* clone */cloned.chars.slice(0);
            }
            cloned.reset$();
            return cloned;
        }

        /**
         * Gets the String content that the tokenizer is parsing.
         * 
         * @return {string} the string content being parsed
         */
        public toString(): string {
            if (this.tokens == null){
                return "StrTokenizer[not tokenized yet]";
            }
            return "StrTokenizer" + /* implicit toString */ (a => a?'['+a.join(', ')+']':'null')(this.getTokenList());
        }
    }
    StrTokenizer["__class"] = "org.openprovenance.apache.commons.lang.text.StrTokenizer";
    StrTokenizer["__interfaces"] = ["java.lang.Cloneable","java.util.Iterator","java.util.ListIterator"];


}


org.openprovenance.apache.commons.lang.text.StrTokenizer.__static_initialize();
