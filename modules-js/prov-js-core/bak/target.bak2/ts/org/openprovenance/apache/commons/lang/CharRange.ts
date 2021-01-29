/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
    /**
     * <p>Constructs a <code>CharRange</code> over a set of characters,
     * optionally negating the range.</p>
     * 
     * <p>A negated range includes everything except that defined by the
     * start and end characters.</p>
     * 
     * <p>If start and end are in the wrong order, they are reversed.
     * Thus <code>a-e1</code> is the same as <code>e1-a</code>.</p>
     * 
     * @param {string} start  first character, inclusive, in this range
     * @param {string} end  last character, inclusive, in this range
     * @param {boolean} negated  true to express everything except the range
     * @class
     * @author Apache Software Foundation
     */
    export class CharRange {
        /**
         * Required for serialization support. Lang version 2.0.
         * 
         * @see Serializable
         */
        static serialVersionUID: number = 8270183163158333422;

        /**
         * The first character, inclusive, in the range.
         */
        /*private*/ start: string;

        /**
         * The last character, inclusive, in the range.
         */
        /*private*/ end: string;

        /**
         * True if the range is everything except the characters specified.
         */
        /*private*/ negated: boolean;

        /**
         * Cached toString.
         */
        /*private*/ iToString: string;

        /**
         * <p>Constructs a <code>CharRange</code> over a single character.</p>
         * 
         * @param {string} ch  only character in this range
         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
         * @see CharRange#CharRange(char, char, boolean)
         * @since 2.5
         */
        public static is(ch: string): CharRange {
            return new CharRange(ch, ch, false);
        }

        /**
         * <p>Constructs a negated <code>CharRange</code> over a single character.</p>
         * 
         * @param {string} ch  only character in this range
         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
         * @see CharRange#CharRange(char, char, boolean)
         * @since 2.5
         */
        public static isNot(ch: string): CharRange {
            return new CharRange(ch, ch, true);
        }

        /**
         * <p>Constructs a <code>CharRange</code> over a set of characters.</p>
         * 
         * @param {string} start  first character, inclusive, in this range
         * @param {string} end  last character, inclusive, in this range
         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
         * @see CharRange#CharRange(char, char, boolean)
         * @since 2.5
         */
        public static isIn(start: string, end: string): CharRange {
            return new CharRange(start, end, false);
        }

        /**
         * <p>Constructs a negated <code>CharRange</code> over a set of characters.</p>
         * 
         * @param {string} start  first character, inclusive, in this range
         * @param {string} end  last character, inclusive, in this range
         * @return {org.openprovenance.apache.commons.lang.CharRange} the new CharRange object
         * @see CharRange#CharRange(char, char, boolean)
         * @since 2.5
         */
        public static isNotIn(start: string, end: string): CharRange {
            return new CharRange(start, end, true);
        }

        public constructor(start?: any, end?: any, negated?: any) {
            if (((typeof start === 'string') || start === null) && ((typeof end === 'string') || end === null) && ((typeof negated === 'boolean') || negated === null)) {
                let __args = arguments;
                if (this.start === undefined) { this.start = null; } 
                if (this.end === undefined) { this.end = null; } 
                if (this.negated === undefined) { this.negated = false; } 
                if (this.iToString === undefined) { this.iToString = null; } 
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(start) > (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(end)){
                    const temp: string = start;
                    start = end;
                    end = temp;
                }
                this.start = start;
                this.end = end;
                this.negated = negated;
            } else if (((typeof start === 'string') || start === null) && ((typeof end === 'boolean') || end === null) && negated === undefined) {
                let __args = arguments;
                let ch: any = __args[0];
                let negated: any = __args[1];
                {
                    let __args = arguments;
                    let start: any = ch;
                    let end: any = ch;
                    if (this.start === undefined) { this.start = null; } 
                    if (this.end === undefined) { this.end = null; } 
                    if (this.negated === undefined) { this.negated = false; } 
                    if (this.iToString === undefined) { this.iToString = null; } 
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(start) > (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(end)){
                        const temp: string = start;
                        start = end;
                        end = temp;
                    }
                    this.start = start;
                    this.end = end;
                    this.negated = negated;
                }
                if (this.start === undefined) { this.start = null; } 
                if (this.end === undefined) { this.end = null; } 
                if (this.negated === undefined) { this.negated = false; } 
                if (this.iToString === undefined) { this.iToString = null; } 
            } else if (((typeof start === 'string') || start === null) && ((typeof end === 'string') || end === null) && negated === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    let negated: any = false;
                    if (this.start === undefined) { this.start = null; } 
                    if (this.end === undefined) { this.end = null; } 
                    if (this.negated === undefined) { this.negated = false; } 
                    if (this.iToString === undefined) { this.iToString = null; } 
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(start) > (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(end)){
                        const temp: string = start;
                        start = end;
                        end = temp;
                    }
                    this.start = start;
                    this.end = end;
                    this.negated = negated;
                }
                if (this.start === undefined) { this.start = null; } 
                if (this.end === undefined) { this.end = null; } 
                if (this.negated === undefined) { this.negated = false; } 
                if (this.iToString === undefined) { this.iToString = null; } 
            } else if (((typeof start === 'string') || start === null) && end === undefined && negated === undefined) {
                let __args = arguments;
                let ch: any = __args[0];
                {
                    let __args = arguments;
                    let start: any = ch;
                    let end: any = ch;
                    let negated: any = false;
                    if (this.start === undefined) { this.start = null; } 
                    if (this.end === undefined) { this.end = null; } 
                    if (this.negated === undefined) { this.negated = false; } 
                    if (this.iToString === undefined) { this.iToString = null; } 
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(start) > (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(end)){
                        const temp: string = start;
                        start = end;
                        end = temp;
                    }
                    this.start = start;
                    this.end = end;
                    this.negated = negated;
                }
                if (this.start === undefined) { this.start = null; } 
                if (this.end === undefined) { this.end = null; } 
                if (this.negated === undefined) { this.negated = false; } 
                if (this.iToString === undefined) { this.iToString = null; } 
            } else throw new Error('invalid overload');
        }

        /**
         * <p>Gets the start character for this character range.</p>
         * 
         * @return {string} the start char (inclusive)
         */
        public getStart(): string {
            return this.start;
        }

        /**
         * <p>Gets the end character for this character range.</p>
         * 
         * @return {string} the end char (inclusive)
         */
        public getEnd(): string {
            return this.end;
        }

        /**
         * <p>Is this <code>CharRange</code> negated.</p>
         * 
         * <p>A negated range includes everything except that defined by the
         * start and end characters.</p>
         * 
         * @return {boolean} <code>true</code> is negated
         */
        public isNegated(): boolean {
            return this.negated;
        }

        public contains$char(ch: string): boolean {
            return ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) >= (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) <= (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end)) !== this.negated;
        }

        public contains$org_openprovenance_apache_commons_lang_CharRange(range: CharRange): boolean {
            if (range == null){
                throw Object.defineProperty(new Error("The Range must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (this.negated){
                if (range.negated){
                    return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) >= (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(range.start) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end) <= (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(range.end);
                }
                return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(range.end) < (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(range.start) > (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end);
            }
            if (range.negated){
                return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) == 0 && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE);
            }
            return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) <= (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(range.start) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end) >= (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(range.end);
        }

        /**
         * <p>Are all the characters of the passed in range contained in
         * this range.</p>
         * 
         * @param {org.openprovenance.apache.commons.lang.CharRange} range  the range to check against
         * @return {boolean} <code>true</code> if this range entirely contains the input range
         * @throws IllegalArgumentException if <code>null</code> input
         */
        public contains(range?: any): any {
            if (((range != null && range instanceof <any>org.openprovenance.apache.commons.lang.CharRange) || range === null)) {
                return <any>this.contains$org_openprovenance_apache_commons_lang_CharRange(range);
            } else if (((typeof range === 'string') || range === null)) {
                return <any>this.contains$char(range);
            } else throw new Error('invalid overload');
        }

        /**
         * <p>Compares two CharRange objects, returning true if they represent
         * exactly the same range of characters defined in the same way.</p>
         * 
         * @param {*} obj  the object to compare to
         * @return {boolean} true if equal
         */
        public equals(obj: any): boolean {
            if (obj === this){
                return true;
            }
            if ((obj != null && obj instanceof <any>org.openprovenance.apache.commons.lang.CharRange) === false){
                return false;
            }
            const other: CharRange = <CharRange>obj;
            return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(other.start) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(other.end) && this.negated === other.negated;
        }

        /**
         * <p>Gets a hashCode compatible with the equals method.</p>
         * 
         * @return {number} a suitable hashCode
         */
        public hashCode(): number {
            return 83 + (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) + 7 * (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end) + (this.negated ? 1 : 0);
        }

        /**
         * <p>Gets a string representation of the character range.</p>
         * 
         * @return {string} string representation of this range
         */
        public toString(): string {
            if (this.iToString == null){
                const buf: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder(4);
                if (this.isNegated()){
                    buf.append$char('^');
                }
                buf.append$char(this.start);
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.start) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.end)){
                    buf.append$char('-');
                    buf.append$char(this.end);
                }
                this.iToString = buf.toString();
            }
            return this.iToString;
        }

        /**
         * <p>Returns an iterator which can be used to walk through the characters described by this range.</p>
         * 
         * <p>#NotThreadSafe# the iterator is not threadsafe</p>
         * @return {*} an iterator to the chars represented by this range
         * @since 2.5
         */
        public iterator(): any {
            return new CharRange.CharacterIterator(this);
        }
    }
    CharRange["__class"] = "org.openprovenance.apache.commons.lang.CharRange";
    CharRange["__interfaces"] = ["java.io.Serializable"];



    export namespace CharRange {

        /**
         * Character {@link Iterator}.
         * <p>#NotThreadSafe#</p>
         * @class
         */
        export class CharacterIterator {
            /**
             * The current character
             */
            current: string;

            range: org.openprovenance.apache.commons.lang.CharRange;

            __hasNext: boolean;

            constructor(r: org.openprovenance.apache.commons.lang.CharRange) {
                if (this.current === undefined) { this.current = null; }
                if (this.range === undefined) { this.range = null; }
                if (this.__hasNext === undefined) { this.__hasNext = false; }
                this.range = r;
                this.__hasNext = true;
                if (this.range.negated){
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.start) == 0){
                        if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.end) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE)){
                            this.__hasNext = false;
                        } else {
                            this.current = String.fromCharCode(((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.end) + 1));
                        }
                    } else {
                        this.current = String.fromCharCode(0);
                    }
                } else {
                    this.current = this.range.start;
                }
            }

            /**
             * Prepare the next character in the range.
             * @private
             */
            prepareNext() {
                if (this.range.negated){
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.current) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE)){
                        this.__hasNext = false;
                    } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.current) + 1 == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.start)){
                        if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.end) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(javaemul.internal.CharacterHelper.MAX_VALUE)){
                            this.__hasNext = false;
                        } else {
                            this.current = String.fromCharCode(((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.end) + 1));
                        }
                    } else {
                        this.current = String.fromCharCode(((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.current) + 1));
                    }
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.current) < (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.range.end)){
                    this.current = String.fromCharCode(((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.current) + 1));
                } else {
                    this.__hasNext = false;
                }
            }

            /**
             * Has the iterator not reached the end character yet?
             * 
             * @return {boolean} <code>true</code> if the iterator has yet to reach the character date
             */
            public hasNext(): boolean {
                return this.__hasNext;
            }

            /**
             * Return the next character in the iteration
             * 
             * @return {*} <code>Character</code> for the next character
             */
            public next(): any {
                if (this.__hasNext === false){
                    throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.util.NoSuchElementException','java.lang.Exception'] });
                }
                const cur: string = this.current;
                this.prepareNext();
                return <string>new String(cur);
            }

            /**
             * Always throws UnsupportedOperationException.
             * 
             * @throws UnsupportedOperationException
             * @see Iterator#remove()
             */
            public remove() {
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }
        CharacterIterator["__class"] = "org.openprovenance.apache.commons.lang.CharRange.CharacterIterator";
        CharacterIterator["__interfaces"] = ["java.util.Iterator"];


    }

}

