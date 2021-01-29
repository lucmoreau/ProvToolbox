/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.text {
    /**
     * Constructor that creates an empty builder the specified initial capacity.
     * 
     * @param {number} initialCapacity  the initial capacity, zero or less will be converted to 32
     * @class
     * @author Apache Software Foundation
     */
    export class StrBuilder {
        /**
         * The extra capacity for new builders.
         */
        static CAPACITY: number = 32;

        /**
         * Required for serialization support.
         * 
         * @see java.io.Serializable
         */
        static serialVersionUID: number = 7628716375283629643;

        /**
         * Internal data storage.
         */
        buffer: string[];

        /**
         * Current size of the buffer.
         */
        __size: number;

        /**
         * The new line.
         */
        /*private*/ newLine: string;

        /**
         * The null text.
         */
        /*private*/ nullText: string;

        public constructor(str?: any) {
            if (((typeof str === 'string') || str === null)) {
                let __args = arguments;
                if (this.buffer === undefined) { this.buffer = null; } 
                if (this.__size === undefined) { this.__size = 0; } 
                if (this.newLine === undefined) { this.newLine = null; } 
                if (this.nullText === undefined) { this.nullText = null; } 
                if (str == null){
                    this.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(StrBuilder.CAPACITY);
                } else {
                    this.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(str.length + StrBuilder.CAPACITY);
                    this.append$java_lang_String(str);
                }
            } else if (((typeof str === 'number') || str === null)) {
                let __args = arguments;
                let initialCapacity: any = __args[0];
                if (this.buffer === undefined) { this.buffer = null; } 
                if (this.__size === undefined) { this.__size = 0; } 
                if (this.newLine === undefined) { this.newLine = null; } 
                if (this.nullText === undefined) { this.nullText = null; } 
                if (initialCapacity <= 0){
                    initialCapacity = StrBuilder.CAPACITY;
                }
                this.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(initialCapacity);
            } else if (str === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    let initialCapacity: any = StrBuilder.CAPACITY;
                    if (this.buffer === undefined) { this.buffer = null; } 
                    if (this.__size === undefined) { this.__size = 0; } 
                    if (this.newLine === undefined) { this.newLine = null; } 
                    if (this.nullText === undefined) { this.nullText = null; } 
                    if (initialCapacity <= 0){
                        initialCapacity = StrBuilder.CAPACITY;
                    }
                    this.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(initialCapacity);
                }
                if (this.buffer === undefined) { this.buffer = null; } 
                if (this.__size === undefined) { this.__size = 0; } 
                if (this.newLine === undefined) { this.newLine = null; } 
                if (this.nullText === undefined) { this.nullText = null; } 
            } else throw new Error('invalid overload');
        }

        /**
         * Gets the text to be appended when a new line is added.
         * 
         * @return {string} the new line text, null means use system default
         */
        public getNewLineText(): string {
            return this.newLine;
        }

        /**
         * Sets the text to be appended when a new line is added.
         * 
         * @param {string} newLine  the new line text, null means use system default
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public setNewLineText(newLine: string): StrBuilder {
            this.newLine = newLine;
            return this;
        }

        /**
         * Gets the text to be appended when null is added.
         * 
         * @return {string} the null text, null means no append
         */
        public getNullText(): string {
            return this.nullText;
        }

        /**
         * Sets the text to be appended when null is added.
         * 
         * @param {string} nullText  the null text, null means no append
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public setNullText(nullText: string): StrBuilder {
            if (nullText != null && nullText.length === 0){
                nullText = null;
            }
            this.nullText = nullText;
            return this;
        }

        /**
         * Gets the length of the string builder.
         * 
         * @return {number} the length
         */
        public length(): number {
            return this.__size;
        }

        /**
         * Updates the length of the builder by either dropping the last characters
         * or adding filler of unicode zero.
         * 
         * @param {number} length  the length to set to, must be zero or positive
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if the length is negative
         */
        public setLength(length: number): StrBuilder {
            if (length < 0){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length < this.__size){
                this.__size = length;
            } else if (length > this.__size){
                this.ensureCapacity(length);
                const oldEnd: number = this.__size;
                const newEnd: number = length;
                this.__size = length;
                for(let i: number = oldEnd; i < newEnd; i++) {{
                    this.buffer[i] = '\u0000';
                };}
            }
            return this;
        }

        /**
         * Gets the current size of the internal character array buffer.
         * 
         * @return {number} the capacity
         */
        public capacity(): number {
            return this.buffer.length;
        }

        /**
         * Checks the capacity and ensures that it is at least the size specified.
         * 
         * @param {number} capacity  the capacity to ensure
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public ensureCapacity(capacity: number): StrBuilder {
            if (capacity > this.buffer.length){
                const old: string[] = this.buffer;
                this.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(capacity * 2);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(old, 0, this.buffer, 0, this.__size);
            }
            return this;
        }

        /**
         * Minimizes the capacity to the actual length of the string.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public minimizeCapacity(): StrBuilder {
            if (this.buffer.length > this.length()){
                const old: string[] = this.buffer;
                this.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(this.length());
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(old, 0, this.buffer, 0, this.__size);
            }
            return this;
        }

        /**
         * Gets the length of the string builder.
         * <p>
         * This method is the same as {@link #length()} and is provided to match the
         * API of Collections.
         * 
         * @return {number} the length
         */
        public size(): number {
            return this.__size;
        }

        /**
         * Checks is the string builder is empty (convenience Collections API style method).
         * <p>
         * This method is the same as checking {@link #length()} and is provided to match the
         * API of Collections.
         * 
         * @return {boolean} <code>true</code> if the size is <code>0</code>.
         */
        public isEmpty(): boolean {
            return this.__size === 0;
        }

        /**
         * Clears the string builder (convenience Collections API style method).
         * <p>
         * This method does not reduce the size of the internal character buffer.
         * To do that, call <code>clear()</code> followed by {@link #minimizeCapacity()}.
         * <p>
         * This method is the same as {@link #setLength(int)} called with zero
         * and is provided to match the API of Collections.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public clear(): StrBuilder {
            this.__size = 0;
            return this;
        }

        /**
         * Gets the character at the specified index.
         * 
         * @see #setCharAt(int, char)
         * @see #deleteCharAt(int)
         * @param {number} index  the index to retrieve, must be valid
         * @return {string} the character at the index
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        public charAt(index: number): string {
            if (index < 0 || index >= this.length()){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            return this.buffer[index];
        }

        /**
         * Sets the character at the specified index.
         * 
         * @see #charAt(int)
         * @see #deleteCharAt(int)
         * @param {number} index  the index to set
         * @param {string} ch  the new character
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        public setCharAt(index: number, ch: string): StrBuilder {
            if (index < 0 || index >= this.length()){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            this.buffer[index] = ch;
            return this;
        }

        /**
         * Deletes the character at the specified index.
         * 
         * @see #charAt(int)
         * @see #setCharAt(int, char)
         * @param {number} index  the index to delete
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        public deleteCharAt(index: number): StrBuilder {
            if (index < 0 || index >= this.__size){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            this.deleteImpl(index, index + 1, 1);
            return this;
        }

        public toCharArray$(): string[] {
            if (this.__size === 0){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CHAR_ARRAY_$LI$();
            }
            const chars: string[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })(this.__size);
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, 0, chars, 0, this.__size);
            return chars;
        }

        public toCharArray$int$int(startIndex: number, endIndex: number): string[] {
            endIndex = this.validateRange(startIndex, endIndex);
            const len: number = endIndex - startIndex;
            if (len === 0){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CHAR_ARRAY_$LI$();
            }
            const chars: string[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })(len);
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, startIndex, chars, 0, len);
            return chars;
        }

        /**
         * Copies part of the builder's character array into a new character array.
         * 
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} endIndex  the end index, exclusive, must be valid except that
         * if too large it is treated as end of string
         * @return {char[]} a new array that holds part of the contents of the builder
         * @throws IndexOutOfBoundsException if startIndex is invalid,
         * or if endIndex is invalid (but endIndex greater than size is valid)
         */
        public toCharArray(startIndex?: any, endIndex?: any): any {
            if (((typeof startIndex === 'number') || startIndex === null) && ((typeof endIndex === 'number') || endIndex === null)) {
                return <any>this.toCharArray$int$int(startIndex, endIndex);
            } else if (startIndex === undefined && endIndex === undefined) {
                return <any>this.toCharArray$();
            } else throw new Error('invalid overload');
        }

        public getChars$char_A(destination: string[]): string[] {
            const len: number = this.length();
            if (destination == null || destination.length < len){
                destination = (s => { let a=[]; while(s-->0) a.push(null); return a; })(len);
            }
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, 0, destination, 0, len);
            return destination;
        }

        public getChars$int$int$char_A$int(startIndex: number, endIndex: number, destination: string[], destinationIndex: number) {
            if (startIndex < 0){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (endIndex < 0 || endIndex > this.length()){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (startIndex > endIndex){
                throw Object.defineProperty(new Error("end < start"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
        }

        /**
         * Copies the character array into the specified array.
         * 
         * @param {number} startIndex  first index to copy, inclusive, must be valid
         * @param {number} endIndex  last index, exclusive, must be valid
         * @param {char[]} destination  the destination array, must not be null or too small
         * @param {number} destinationIndex  the index to start copying in destination
         * @throws NullPointerException if the array is null
         * @throws IndexOutOfBoundsException if any index is invalid
         */
        public getChars(startIndex?: any, endIndex?: any, destination?: any, destinationIndex?: any) {
            if (((typeof startIndex === 'number') || startIndex === null) && ((typeof endIndex === 'number') || endIndex === null) && ((destination != null && destination instanceof <any>Array && (destination.length == 0 || destination[0] == null ||(typeof destination[0] === 'string'))) || destination === null) && ((typeof destinationIndex === 'number') || destinationIndex === null)) {
                return <any>this.getChars$int$int$char_A$int(startIndex, endIndex, destination, destinationIndex);
            } else if (((startIndex != null && startIndex instanceof <any>Array && (startIndex.length == 0 || startIndex[0] == null ||(typeof startIndex[0] === 'string'))) || startIndex === null) && endIndex === undefined && destination === undefined && destinationIndex === undefined) {
                return <any>this.getChars$char_A(startIndex);
            } else throw new Error('invalid overload');
        }

        /**
         * Appends the new line string to this string builder.
         * <p>
         * The new line string can be altered using {@link #setNewLineText(String)}.
         * This might be used to force the output to always use Unix line endings
         * even when on Windows.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public appendNewLine(): StrBuilder {
            if (this.newLine == null){
                this.append$java_lang_String(org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$());
                return this;
            }
            return this.append$java_lang_String(this.newLine);
        }

        /**
         * Appends the text representing <code>null</code> to this string builder.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public appendNull(): StrBuilder {
            if (this.nullText == null){
                return this;
            }
            return this.append$java_lang_String(this.nullText);
        }

        public append$java_lang_Object(obj: any): StrBuilder {
            if (obj == null){
                return this.appendNull();
            }
            return this.append$java_lang_String(obj.toString());
        }

        public append$java_lang_String(str: string): StrBuilder {
            if (str == null){
                return this.appendNull();
            }
            const strLen: number = str.length;
            if (strLen > 0){
                const len: number = this.length();
                this.ensureCapacity(len + strLen);
                /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, 0, strLen, this.buffer, len);
                this.__size += strLen;
            }
            return this;
        }

        public append$java_lang_String$int$int(str: string, startIndex: number, length: number): StrBuilder {
            if (str == null){
                return this.appendNull();
            }
            if (startIndex < 0 || startIndex > str.length){
                throw Object.defineProperty(new Error("startIndex must be valid"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length < 0 || (startIndex + length) > str.length){
                throw Object.defineProperty(new Error("length must be valid"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length > 0){
                const len: number = this.length();
                this.ensureCapacity(len + length);
                /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, startIndex, startIndex + length, this.buffer, len);
                this.__size += length;
            }
            return this;
        }

        /**
         * Appends part of a string to this string builder.
         * Appending null will call {@link #appendNull()}.
         * 
         * @param {string} str  the string to append
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} length  the length to append, must be valid
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public append(str?: any, startIndex?: any, length?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.append$java_lang_String$int$int(str, startIndex, length);
            } else if (((str != null && (str instanceof Object)) || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.append$java_lang_StringBuffer$int$int(str, startIndex, length);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrBuilder) || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.append$org_openprovenance_apache_commons_lang_text_StrBuilder$int$int(str, startIndex, length);
            } else if (((str != null && str instanceof <any>Array && (str.length == 0 || str[0] == null ||(typeof str[0] === 'string'))) || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.append$char_A$int$int(str, startIndex, length);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$java_lang_String(str);
            } else if (((str != null && (str instanceof Object)) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$java_lang_StringBuffer(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrBuilder) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$org_openprovenance_apache_commons_lang_text_StrBuilder(str);
            } else if (((str != null && str instanceof <any>Array && (str.length == 0 || str[0] == null ||(typeof str[0] === 'string'))) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$char_A(str);
            } else if (((typeof str === 'boolean') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$boolean(str);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$char(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$int(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$long(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$float(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$double(str);
            } else if (((str != null) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.append$java_lang_Object(str);
            } else throw new Error('invalid overload');
        }

        public append$java_lang_StringBuffer(str: { str: string, toString: Function }): StrBuilder {
            if (str == null){
                return this.appendNull();
            }
            const strLen: number = /* length */str.str.length;
            if (strLen > 0){
                const len: number = this.length();
                this.ensureCapacity(len + strLen);
                str.getChars(0, strLen, this.buffer, len);
                this.__size += strLen;
            }
            return this;
        }

        public append$java_lang_StringBuffer$int$int(str: { str: string, toString: Function }, startIndex: number, length: number): StrBuilder {
            if (str == null){
                return this.appendNull();
            }
            if (startIndex < 0 || startIndex > /* length */str.str.length){
                throw Object.defineProperty(new Error("startIndex must be valid"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length < 0 || (startIndex + length) > /* length */str.str.length){
                throw Object.defineProperty(new Error("length must be valid"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length > 0){
                const len: number = this.length();
                this.ensureCapacity(len + length);
                str.getChars(startIndex, startIndex + length, this.buffer, len);
                this.__size += length;
            }
            return this;
        }

        public append$org_openprovenance_apache_commons_lang_text_StrBuilder(str: StrBuilder): StrBuilder {
            if (str == null){
                return this.appendNull();
            }
            const strLen: number = str.length();
            if (strLen > 0){
                const len: number = this.length();
                this.ensureCapacity(len + strLen);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(str.buffer, 0, this.buffer, len, strLen);
                this.__size += strLen;
            }
            return this;
        }

        public append$org_openprovenance_apache_commons_lang_text_StrBuilder$int$int(str: StrBuilder, startIndex: number, length: number): StrBuilder {
            if (str == null){
                return this.appendNull();
            }
            if (startIndex < 0 || startIndex > str.length()){
                throw Object.defineProperty(new Error("startIndex must be valid"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length < 0 || (startIndex + length) > str.length()){
                throw Object.defineProperty(new Error("length must be valid"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length > 0){
                const len: number = this.length();
                this.ensureCapacity(len + length);
                str.getChars$int$int$char_A$int(startIndex, startIndex + length, this.buffer, len);
                this.__size += length;
            }
            return this;
        }

        public append$char_A(chars: string[]): StrBuilder {
            if (chars == null){
                return this.appendNull();
            }
            const strLen: number = chars.length;
            if (strLen > 0){
                const len: number = this.length();
                this.ensureCapacity(len + strLen);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(chars, 0, this.buffer, len, strLen);
                this.__size += strLen;
            }
            return this;
        }

        public append$char_A$int$int(chars: string[], startIndex: number, length: number): StrBuilder {
            if (chars == null){
                return this.appendNull();
            }
            if (startIndex < 0 || startIndex > chars.length){
                throw Object.defineProperty(new Error("Invalid startIndex: " + length), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length < 0 || (startIndex + length) > chars.length){
                throw Object.defineProperty(new Error("Invalid length: " + length), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length > 0){
                const len: number = this.length();
                this.ensureCapacity(len + length);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(chars, startIndex, this.buffer, len, length);
                this.__size += length;
            }
            return this;
        }

        public append$boolean(value: boolean): StrBuilder {
            if (value){
                this.ensureCapacity(this.__size + 4);
                this.buffer[this.__size++] = 't';
                this.buffer[this.__size++] = 'r';
                this.buffer[this.__size++] = 'u';
                this.buffer[this.__size++] = 'e';
            } else {
                this.ensureCapacity(this.__size + 5);
                this.buffer[this.__size++] = 'f';
                this.buffer[this.__size++] = 'a';
                this.buffer[this.__size++] = 'l';
                this.buffer[this.__size++] = 's';
                this.buffer[this.__size++] = 'e';
            }
            return this;
        }

        public append$char(ch: string): StrBuilder {
            const len: number = this.length();
            this.ensureCapacity(len + 1);
            this.buffer[this.__size++] = ch;
            return this;
        }

        public append$int(value: number): StrBuilder {
            return this.append$java_lang_String(/* valueOf */String(value).toString());
        }

        public append$long(value: number): StrBuilder {
            return this.append$java_lang_String(/* valueOf */String(value).toString());
        }

        public append$float(value: number): StrBuilder {
            return this.append$java_lang_String(/* valueOf */String(value).toString());
        }

        public append$double(value: number): StrBuilder {
            return this.append$java_lang_String(/* valueOf */String(value).toString());
        }

        public appendln$java_lang_Object(obj: any): StrBuilder {
            return this.append$java_lang_Object(obj).appendNewLine();
        }

        public appendln$java_lang_String(str: string): StrBuilder {
            return this.append$java_lang_String(str).appendNewLine();
        }

        public appendln$java_lang_String$int$int(str: string, startIndex: number, length: number): StrBuilder {
            return this.append$java_lang_String$int$int(str, startIndex, length).appendNewLine();
        }

        /**
         * Appends part of a string followed by a new line to this string builder.
         * Appending null will call {@link #appendNull()}.
         * 
         * @param {string} str  the string to append
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} length  the length to append, must be valid
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @since 2.3
         */
        public appendln(str?: any, startIndex?: any, length?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.appendln$java_lang_String$int$int(str, startIndex, length);
            } else if (((str != null && (str instanceof Object)) || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.appendln$java_lang_StringBuffer$int$int(str, startIndex, length);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrBuilder) || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.appendln$org_openprovenance_apache_commons_lang_text_StrBuilder$int$int(str, startIndex, length);
            } else if (((str != null && str instanceof <any>Array && (str.length == 0 || str[0] == null ||(typeof str[0] === 'string'))) || str === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.appendln$char_A$int$int(str, startIndex, length);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$java_lang_String(str);
            } else if (((str != null && (str instanceof Object)) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$java_lang_StringBuffer(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrBuilder) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$org_openprovenance_apache_commons_lang_text_StrBuilder(str);
            } else if (((str != null && str instanceof <any>Array && (str.length == 0 || str[0] == null ||(typeof str[0] === 'string'))) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$char_A(str);
            } else if (((typeof str === 'boolean') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$boolean(str);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$char(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$int(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$long(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$float(str);
            } else if (((typeof str === 'number') || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$double(str);
            } else if (((str != null) || str === null) && startIndex === undefined && length === undefined) {
                return <any>this.appendln$java_lang_Object(str);
            } else throw new Error('invalid overload');
        }

        public appendln$java_lang_StringBuffer(str: { str: string, toString: Function }): StrBuilder {
            return this.append$java_lang_StringBuffer(str).appendNewLine();
        }

        public appendln$java_lang_StringBuffer$int$int(str: { str: string, toString: Function }, startIndex: number, length: number): StrBuilder {
            return this.append$java_lang_StringBuffer$int$int(str, startIndex, length).appendNewLine();
        }

        public appendln$org_openprovenance_apache_commons_lang_text_StrBuilder(str: StrBuilder): StrBuilder {
            return this.append$org_openprovenance_apache_commons_lang_text_StrBuilder(str).appendNewLine();
        }

        public appendln$org_openprovenance_apache_commons_lang_text_StrBuilder$int$int(str: StrBuilder, startIndex: number, length: number): StrBuilder {
            return this.append$org_openprovenance_apache_commons_lang_text_StrBuilder$int$int(str, startIndex, length).appendNewLine();
        }

        public appendln$char_A(chars: string[]): StrBuilder {
            return this.append$char_A(chars).appendNewLine();
        }

        public appendln$char_A$int$int(chars: string[], startIndex: number, length: number): StrBuilder {
            return this.append$char_A$int$int(chars, startIndex, length).appendNewLine();
        }

        public appendln$boolean(value: boolean): StrBuilder {
            return this.append$boolean(value).appendNewLine();
        }

        public appendln$char(ch: string): StrBuilder {
            return this.append$char(ch).appendNewLine();
        }

        public appendln$int(value: number): StrBuilder {
            return this.append$int(value).appendNewLine();
        }

        public appendln$long(value: number): StrBuilder {
            return this.append$long(value).appendNewLine();
        }

        public appendln$float(value: number): StrBuilder {
            return this.append$float(value).appendNewLine();
        }

        public appendln$double(value: number): StrBuilder {
            return this.append$double(value).appendNewLine();
        }

        public appendAll$java_lang_Object_A(array: any[]): StrBuilder {
            if (array != null && array.length > 0){
                for(let i: number = 0; i < array.length; i++) {{
                    this.append$java_lang_Object(array[i]);
                };}
            }
            return this;
        }

        /**
         * Appends each item in an array to the builder without any separators.
         * Appending a null array will have no effect.
         * Each object is appended using {@link #append(Object)}.
         * 
         * @param {java.lang.Object[]} array  the array to append
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @since 2.3
         */
        public appendAll(array?: any): any {
            if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(array[0] != null))) || array === null)) {
                return <any>this.appendAll$java_lang_Object_A(array);
            } else if (((array != null && (array instanceof Array)) || array === null)) {
                return <any>this.appendAll$java_util_Collection(array);
            } else if (((array != null && (array instanceof Object)) || array === null)) {
                return <any>this.appendAll$java_util_Iterator(array);
            } else throw new Error('invalid overload');
        }

        public appendAll$java_util_Collection(coll: Array<any>): StrBuilder {
            if (coll != null && /* size */(<number>coll.length) > 0){
                const it: any = /* iterator */((a) => { var i = 0; return { next: function() { return i<a.length?a[i++]:null; }, hasNext: function() { return i<a.length; }}})(coll);
                while((it.hasNext())) {{
                    this.append$java_lang_Object(it.next());
                }};
            }
            return this;
        }

        public appendAll$java_util_Iterator(it: any): StrBuilder {
            if (it != null){
                while((it.hasNext())) {{
                    this.append$java_lang_Object(it.next());
                }};
            }
            return this;
        }

        public appendWithSeparators$java_lang_Object_A$java_lang_String(array: any[], separator: string): StrBuilder {
            if (array != null && array.length > 0){
                separator = (separator == null ? "" : separator);
                this.append$java_lang_Object(array[0]);
                for(let i: number = 1; i < array.length; i++) {{
                    this.append$java_lang_String(separator);
                    this.append$java_lang_Object(array[i]);
                };}
            }
            return this;
        }

        /**
         * Appends an array placing separators between each value, but
         * not before the first or after the last.
         * Appending a null array will have no effect.
         * Each object is appended using {@link #append(Object)}.
         * 
         * @param {java.lang.Object[]} array  the array to append
         * @param {string} separator  the separator to use, null means no separator
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public appendWithSeparators(array?: any, separator?: any): any {
            if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(array[0] != null))) || array === null) && ((typeof separator === 'string') || separator === null)) {
                return <any>this.appendWithSeparators$java_lang_Object_A$java_lang_String(array, separator);
            } else if (((array != null && (array instanceof Array)) || array === null) && ((typeof separator === 'string') || separator === null)) {
                return <any>this.appendWithSeparators$java_util_Collection$java_lang_String(array, separator);
            } else if (((array != null && (array instanceof Object)) || array === null) && ((typeof separator === 'string') || separator === null)) {
                return <any>this.appendWithSeparators$java_util_Iterator$java_lang_String(array, separator);
            } else throw new Error('invalid overload');
        }

        public appendWithSeparators$java_util_Collection$java_lang_String(coll: Array<any>, separator: string): StrBuilder {
            if (coll != null && /* size */(<number>coll.length) > 0){
                separator = (separator == null ? "" : separator);
                const it: any = /* iterator */((a) => { var i = 0; return { next: function() { return i<a.length?a[i++]:null; }, hasNext: function() { return i<a.length; }}})(coll);
                while((it.hasNext())) {{
                    this.append$java_lang_Object(it.next());
                    if (it.hasNext()){
                        this.append$java_lang_String(separator);
                    }
                }};
            }
            return this;
        }

        public appendWithSeparators$java_util_Iterator$java_lang_String(it: any, separator: string): StrBuilder {
            if (it != null){
                separator = (separator == null ? "" : separator);
                while((it.hasNext())) {{
                    this.append$java_lang_Object(it.next());
                    if (it.hasNext()){
                        this.append$java_lang_String(separator);
                    }
                }};
            }
            return this;
        }

        public appendSeparator$java_lang_String(separator: string): StrBuilder {
            return this.appendSeparator$java_lang_String$java_lang_String(separator, null);
        }

        public appendSeparator$java_lang_String$java_lang_String(standard: string, defaultIfEmpty: string): StrBuilder {
            const str: string = this.isEmpty() ? defaultIfEmpty : standard;
            if (str != null){
                this.append$java_lang_String(str);
            }
            return this;
        }

        /**
         * Appends one of both separators to the StrBuilder.
         * If the builder is currently empty it will append the defaultIfEmpty-separator
         * Otherwise it will append the standard-separator
         * 
         * Appending a null separator will have no effect.
         * The separator is appended using {@link #append(String)}.
         * <p>
         * This method is for example useful for constructing queries
         * <pre>
         * StrBuilder whereClause = new StrBuilder();
         * if(searchCommand.getPriority() != null) {
         * whereClause.appendSeparator(" and", " where");
         * whereClause.append(" priority = ?")
         * }
         * if(searchCommand.getComponent() != null) {
         * whereClause.appendSeparator(" and", " where");
         * whereClause.append(" component = ?")
         * }
         * selectClause.append(whereClause)
         * </pre>
         * 
         * @param {string} standard the separator if builder is not empty, null means no separator
         * @param {string} defaultIfEmpty the separator if builder is empty, null means no separator
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @since 2.5
         */
        public appendSeparator(standard?: any, defaultIfEmpty?: any): any {
            if (((typeof standard === 'string') || standard === null) && ((typeof defaultIfEmpty === 'string') || defaultIfEmpty === null)) {
                return <any>this.appendSeparator$java_lang_String$java_lang_String(standard, defaultIfEmpty);
            } else if (((typeof standard === 'string') || standard === null) && ((typeof defaultIfEmpty === 'string') || defaultIfEmpty === null)) {
                return <any>this.appendSeparator$char$char(standard, defaultIfEmpty);
            } else if (((typeof standard === 'string') || standard === null) && ((typeof defaultIfEmpty === 'number') || defaultIfEmpty === null)) {
                return <any>this.appendSeparator$java_lang_String$int(standard, defaultIfEmpty);
            } else if (((typeof standard === 'string') || standard === null) && ((typeof defaultIfEmpty === 'number') || defaultIfEmpty === null)) {
                return <any>this.appendSeparator$char$int(standard, defaultIfEmpty);
            } else if (((typeof standard === 'string') || standard === null) && defaultIfEmpty === undefined) {
                return <any>this.appendSeparator$java_lang_String(standard);
            } else if (((typeof standard === 'string') || standard === null) && defaultIfEmpty === undefined) {
                return <any>this.appendSeparator$char(standard);
            } else throw new Error('invalid overload');
        }

        public appendSeparator$char(separator: string): StrBuilder {
            if (this.size() > 0){
                this.append$char(separator);
            }
            return this;
        }

        public appendSeparator$char$char(standard: string, defaultIfEmpty: string): StrBuilder {
            if (this.size() > 0){
                this.append$char(standard);
            } else {
                this.append$char(defaultIfEmpty);
            }
            return this;
        }

        public appendSeparator$java_lang_String$int(separator: string, loopIndex: number): StrBuilder {
            if (separator != null && loopIndex > 0){
                this.append$java_lang_String(separator);
            }
            return this;
        }

        public appendSeparator$char$int(separator: string, loopIndex: number): StrBuilder {
            if (loopIndex > 0){
                this.append$char(separator);
            }
            return this;
        }

        /**
         * Appends the pad character to the builder the specified number of times.
         * 
         * @param {number} length  the length to append, negative means no append
         * @param {string} padChar  the character to append
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public appendPadding(length: number, padChar: string): StrBuilder {
            if (length >= 0){
                this.ensureCapacity(this.__size + length);
                for(let i: number = 0; i < length; i++) {{
                    this.buffer[this.__size++] = padChar;
                };}
            }
            return this;
        }

        public appendFixedWidthPadLeft$java_lang_Object$int$char(obj: any, width: number, padChar: string): StrBuilder {
            if (width > 0){
                this.ensureCapacity(this.__size + width);
                let str: string = (obj == null ? this.getNullText() : obj.toString());
                if (str == null){
                    str = "";
                }
                const strLen: number = str.length;
                if (strLen >= width){
                    /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, strLen - width, strLen, this.buffer, this.__size);
                } else {
                    const padLen: number = width - strLen;
                    for(let i: number = 0; i < padLen; i++) {{
                        this.buffer[this.__size + i] = padChar;
                    };}
                    /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, 0, strLen, this.buffer, this.__size + padLen);
                }
                this.__size += width;
            }
            return this;
        }

        public appendFixedWidthPadLeft$int$int$char(value: number, width: number, padChar: string): StrBuilder {
            return this.appendFixedWidthPadLeft$java_lang_Object$int$char(/* valueOf */String(value).toString(), width, padChar);
        }

        /**
         * Appends an object to the builder padding on the left to a fixed width.
         * The <code>String.valueOf</code> of the <code>int</code> value is used.
         * If the formatted value is larger than the length, the left hand side is lost.
         * 
         * @param {number} value  the value to append
         * @param {number} width  the fixed field width, zero or negative has no effect
         * @param {string} padChar  the pad character to use
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public appendFixedWidthPadLeft(value?: any, width?: any, padChar?: any): any {
            if (((typeof value === 'number') || value === null) && ((typeof width === 'number') || width === null) && ((typeof padChar === 'string') || padChar === null)) {
                return <any>this.appendFixedWidthPadLeft$int$int$char(value, width, padChar);
            } else if (((value != null) || value === null) && ((typeof width === 'number') || width === null) && ((typeof padChar === 'string') || padChar === null)) {
                return <any>this.appendFixedWidthPadLeft$java_lang_Object$int$char(value, width, padChar);
            } else throw new Error('invalid overload');
        }

        public appendFixedWidthPadRight$java_lang_Object$int$char(obj: any, width: number, padChar: string): StrBuilder {
            if (width > 0){
                this.ensureCapacity(this.__size + width);
                let str: string = (obj == null ? this.getNullText() : obj.toString());
                if (str == null){
                    str = "";
                }
                const strLen: number = str.length;
                if (strLen >= width){
                    /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, 0, width, this.buffer, this.__size);
                } else {
                    const padLen: number = width - strLen;
                    /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, 0, strLen, this.buffer, this.__size);
                    for(let i: number = 0; i < padLen; i++) {{
                        this.buffer[this.__size + strLen + i] = padChar;
                    };}
                }
                this.__size += width;
            }
            return this;
        }

        public appendFixedWidthPadRight$int$int$char(value: number, width: number, padChar: string): StrBuilder {
            return this.appendFixedWidthPadRight$java_lang_Object$int$char(/* valueOf */String(value).toString(), width, padChar);
        }

        /**
         * Appends an object to the builder padding on the right to a fixed length.
         * The <code>String.valueOf</code> of the <code>int</code> value is used.
         * If the object is larger than the length, the right hand side is lost.
         * 
         * @param {number} value  the value to append
         * @param {number} width  the fixed field width, zero or negative has no effect
         * @param {string} padChar  the pad character to use
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public appendFixedWidthPadRight(value?: any, width?: any, padChar?: any): any {
            if (((typeof value === 'number') || value === null) && ((typeof width === 'number') || width === null) && ((typeof padChar === 'string') || padChar === null)) {
                return <any>this.appendFixedWidthPadRight$int$int$char(value, width, padChar);
            } else if (((value != null) || value === null) && ((typeof width === 'number') || width === null) && ((typeof padChar === 'string') || padChar === null)) {
                return <any>this.appendFixedWidthPadRight$java_lang_Object$int$char(value, width, padChar);
            } else throw new Error('invalid overload');
        }

        public insert$int$java_lang_Object(index: number, obj: any): StrBuilder {
            if (obj == null){
                return this.insert$int$java_lang_String(index, this.nullText);
            }
            return this.insert$int$java_lang_String(index, obj.toString());
        }

        public insert$int$java_lang_String(index: number, str: string): StrBuilder {
            this.validateIndex(index);
            if (str == null){
                str = this.nullText;
            }
            const strLen: number = (str == null ? 0 : str.length);
            if (strLen > 0){
                const newSize: number = this.__size + strLen;
                this.ensureCapacity(newSize);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, index, this.buffer, index + strLen, this.__size - index);
                this.__size = newSize;
                /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(str, 0, strLen, this.buffer, index);
            }
            return this;
        }

        public insert$int$char_A(index: number, chars: string[]): StrBuilder {
            this.validateIndex(index);
            if (chars == null){
                return this.insert$int$java_lang_String(index, this.nullText);
            }
            const len: number = chars.length;
            if (len > 0){
                this.ensureCapacity(this.__size + len);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, index, this.buffer, index + len, this.__size - index);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(chars, 0, this.buffer, index, len);
                this.__size += len;
            }
            return this;
        }

        public insert$int$char_A$int$int(index: number, chars: string[], offset: number, length: number): StrBuilder {
            this.validateIndex(index);
            if (chars == null){
                return this.insert$int$java_lang_String(index, this.nullText);
            }
            if (offset < 0 || offset > chars.length){
                throw Object.defineProperty(new Error("Invalid offset: " + offset), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length < 0 || offset + length > chars.length){
                throw Object.defineProperty(new Error("Invalid length: " + length), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (length > 0){
                this.ensureCapacity(this.__size + length);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, index, this.buffer, index + length, this.__size - index);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(chars, offset, this.buffer, index, length);
                this.__size += length;
            }
            return this;
        }

        /**
         * Inserts part of the character array into this builder.
         * Inserting null will use the stored null text value.
         * 
         * @param {number} index  the index to add at, must be valid
         * @param {char[]} chars  the char array to insert
         * @param {number} offset  the offset into the character array to start at, must be valid
         * @param {number} length  the length of the character array part to copy, must be positive
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if any index is invalid
         */
        public insert(index?: any, chars?: any, offset?: any, length?: any): any {
            if (((typeof index === 'number') || index === null) && ((chars != null && chars instanceof <any>Array && (chars.length == 0 || chars[0] == null ||(typeof chars[0] === 'string'))) || chars === null) && ((typeof offset === 'number') || offset === null) && ((typeof length === 'number') || length === null)) {
                return <any>this.insert$int$char_A$int$int(index, chars, offset, length);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'string') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$java_lang_String(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((chars != null && chars instanceof <any>Array && (chars.length == 0 || chars[0] == null ||(typeof chars[0] === 'string'))) || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$char_A(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'boolean') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$boolean(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'string') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$char(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'number') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$int(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'number') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$long(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'number') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$float(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((typeof chars === 'number') || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$double(index, chars);
            } else if (((typeof index === 'number') || index === null) && ((chars != null) || chars === null) && offset === undefined && length === undefined) {
                return <any>this.insert$int$java_lang_Object(index, chars);
            } else throw new Error('invalid overload');
        }

        public insert$int$boolean(index: number, value: boolean): StrBuilder {
            this.validateIndex(index);
            if (value){
                this.ensureCapacity(this.__size + 4);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, index, this.buffer, index + 4, this.__size - index);
                this.buffer[index++] = 't';
                this.buffer[index++] = 'r';
                this.buffer[index++] = 'u';
                this.buffer[index] = 'e';
                this.__size += 4;
            } else {
                this.ensureCapacity(this.__size + 5);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, index, this.buffer, index + 5, this.__size - index);
                this.buffer[index++] = 'f';
                this.buffer[index++] = 'a';
                this.buffer[index++] = 'l';
                this.buffer[index++] = 's';
                this.buffer[index] = 'e';
                this.__size += 5;
            }
            return this;
        }

        public insert$int$char(index: number, value: string): StrBuilder {
            this.validateIndex(index);
            this.ensureCapacity(this.__size + 1);
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, index, this.buffer, index + 1, this.__size - index);
            this.buffer[index] = value;
            this.__size++;
            return this;
        }

        public insert$int$int(index: number, value: number): StrBuilder {
            return this.insert$int$java_lang_String(index, /* valueOf */String(value).toString());
        }

        public insert$int$long(index: number, value: number): StrBuilder {
            return this.insert$int$java_lang_String(index, /* valueOf */String(value).toString());
        }

        public insert$int$float(index: number, value: number): StrBuilder {
            return this.insert$int$java_lang_String(index, /* valueOf */String(value).toString());
        }

        public insert$int$double(index: number, value: number): StrBuilder {
            return this.insert$int$java_lang_String(index, /* valueOf */String(value).toString());
        }

        /**
         * Internal method to delete a range without validation.
         * 
         * @param {number} startIndex  the start index, must be valid
         * @param {number} endIndex  the end index (exclusive), must be valid
         * @param {number} len  the length, must be valid
         * @throws IndexOutOfBoundsException if any index is invalid
         * @private
         */
        deleteImpl(startIndex: number, endIndex: number, len: number) {
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, endIndex, this.buffer, startIndex, this.__size - endIndex);
            this.__size -= len;
        }

        /**
         * Deletes the characters between the two specified indices.
         * 
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} endIndex  the end index, exclusive, must be valid except
         * that if too large it is treated as end of string
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        public delete(startIndex: number, endIndex: number): StrBuilder {
            endIndex = this.validateRange(startIndex, endIndex);
            const len: number = endIndex - startIndex;
            if (len > 0){
                this.deleteImpl(startIndex, endIndex, len);
            }
            return this;
        }

        public deleteAll$char(ch: string): StrBuilder {
            for(let i: number = 0; i < this.__size; i++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch)){
                    const start: number = i;
                    while((++i < this.__size)) {{
                        if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch)){
                            break;
                        }
                    }};
                    const len: number = i - start;
                    this.deleteImpl(start, i, len);
                    i -= len;
                }
            };}
            return this;
        }

        public deleteFirst$char(ch: string): StrBuilder {
            for(let i: number = 0; i < this.__size; i++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch)){
                    this.deleteImpl(i, i + 1, 1);
                    break;
                }
            };}
            return this;
        }

        public deleteAll$java_lang_String(str: string): StrBuilder {
            const len: number = (str == null ? 0 : str.length);
            if (len > 0){
                let index: number = this.indexOf$java_lang_String$int(str, 0);
                while((index >= 0)) {{
                    this.deleteImpl(index, index + len, len);
                    index = this.indexOf$java_lang_String$int(str, index);
                }};
            }
            return this;
        }

        /**
         * Deletes the string wherever it occurs in the builder.
         * 
         * @param {string} str  the string to delete, null causes no action
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public deleteAll(str?: any): any {
            if (((typeof str === 'string') || str === null)) {
                return <any>this.deleteAll$java_lang_String(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null)) {
                return <any>this.deleteAll$org_openprovenance_apache_commons_lang_text_StrMatcher(str);
            } else if (((typeof str === 'string') || str === null)) {
                return <any>this.deleteAll$char(str);
            } else throw new Error('invalid overload');
        }

        public deleteFirst$java_lang_String(str: string): StrBuilder {
            const len: number = (str == null ? 0 : str.length);
            if (len > 0){
                const index: number = this.indexOf$java_lang_String$int(str, 0);
                if (index >= 0){
                    this.deleteImpl(index, index + len, len);
                }
            }
            return this;
        }

        /**
         * Deletes the string wherever it occurs in the builder.
         * 
         * @param {string} str  the string to delete, null causes no action
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public deleteFirst(str?: any): any {
            if (((typeof str === 'string') || str === null)) {
                return <any>this.deleteFirst$java_lang_String(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null)) {
                return <any>this.deleteFirst$org_openprovenance_apache_commons_lang_text_StrMatcher(str);
            } else if (((typeof str === 'string') || str === null)) {
                return <any>this.deleteFirst$char(str);
            } else throw new Error('invalid overload');
        }

        public deleteAll$org_openprovenance_apache_commons_lang_text_StrMatcher(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher): StrBuilder {
            return this.replace$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, null, 0, this.__size, -1);
        }

        public deleteFirst$org_openprovenance_apache_commons_lang_text_StrMatcher(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher): StrBuilder {
            return this.replace$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, null, 0, this.__size, 1);
        }

        replaceImpl$int$int$int$java_lang_String$int(startIndex: number, endIndex: number, removeLen: number, insertStr: string, insertLen: number) {
            const newSize: number = this.__size - removeLen + insertLen;
            if (insertLen !== removeLen){
                this.ensureCapacity(newSize);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, endIndex, this.buffer, startIndex + insertLen, this.__size - endIndex);
                this.__size = newSize;
            }
            if (insertLen > 0){
                /* getChars */((a, s, e, d, l) => { d.splice.apply(d, [l, e-s].concat(<any>a.substring(s, e).split(''))); })(insertStr, 0, insertLen, this.buffer, startIndex);
            }
        }

        public replace$int$int$java_lang_String(startIndex: number, endIndex: number, replaceStr: string): StrBuilder {
            endIndex = this.validateRange(startIndex, endIndex);
            const insertLen: number = (replaceStr == null ? 0 : replaceStr.length);
            this.replaceImpl$int$int$int$java_lang_String$int(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
            return this;
        }

        public replaceAll$char$char(search: string, replace: string): StrBuilder {
            if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(search) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(replace)){
                for(let i: number = 0; i < this.__size; i++) {{
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(search)){
                        this.buffer[i] = replace;
                    }
                };}
            }
            return this;
        }

        public replaceFirst$char$char(search: string, replace: string): StrBuilder {
            if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(search) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(replace)){
                for(let i: number = 0; i < this.__size; i++) {{
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(search)){
                        this.buffer[i] = replace;
                        break;
                    }
                };}
            }
            return this;
        }

        public replaceAll$java_lang_String$java_lang_String(searchStr: string, replaceStr: string): StrBuilder {
            const searchLen: number = (searchStr == null ? 0 : searchStr.length);
            if (searchLen > 0){
                const replaceLen: number = (replaceStr == null ? 0 : replaceStr.length);
                let index: number = this.indexOf$java_lang_String$int(searchStr, 0);
                while((index >= 0)) {{
                    this.replaceImpl$int$int$int$java_lang_String$int(index, index + searchLen, searchLen, replaceStr, replaceLen);
                    index = this.indexOf$java_lang_String$int(searchStr, index + replaceLen);
                }};
            }
            return this;
        }

        /**
         * Replaces the search string with the replace string throughout the builder.
         * 
         * @param {string} searchStr  the search string, null causes no action to occur
         * @param {string} replaceStr  the replace string, null is equivalent to an empty string
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public replaceAll(searchStr?: any, replaceStr?: any): any {
            if (((typeof searchStr === 'string') || searchStr === null) && ((typeof replaceStr === 'string') || replaceStr === null)) {
                return <any>this.replaceAll$java_lang_String$java_lang_String(searchStr, replaceStr);
            } else if (((searchStr != null && searchStr instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || searchStr === null) && ((typeof replaceStr === 'string') || replaceStr === null)) {
                return <any>this.replaceAll$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String(searchStr, replaceStr);
            } else if (((typeof searchStr === 'string') || searchStr === null) && ((typeof replaceStr === 'string') || replaceStr === null)) {
                return <any>this.replaceAll$char$char(searchStr, replaceStr);
            } else throw new Error('invalid overload');
        }

        public replaceFirst$java_lang_String$java_lang_String(searchStr: string, replaceStr: string): StrBuilder {
            const searchLen: number = (searchStr == null ? 0 : searchStr.length);
            if (searchLen > 0){
                const index: number = this.indexOf$java_lang_String$int(searchStr, 0);
                if (index >= 0){
                    const replaceLen: number = (replaceStr == null ? 0 : replaceStr.length);
                    this.replaceImpl$int$int$int$java_lang_String$int(index, index + searchLen, searchLen, replaceStr, replaceLen);
                }
            }
            return this;
        }

        /**
         * Replaces the first instance of the search string with the replace string.
         * 
         * @param {string} searchStr  the search string, null causes no action to occur
         * @param {string} replaceStr  the replace string, null is equivalent to an empty string
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public replaceFirst(searchStr?: any, replaceStr?: any): any {
            if (((typeof searchStr === 'string') || searchStr === null) && ((typeof replaceStr === 'string') || replaceStr === null)) {
                return <any>this.replaceFirst$java_lang_String$java_lang_String(searchStr, replaceStr);
            } else if (((searchStr != null && searchStr instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || searchStr === null) && ((typeof replaceStr === 'string') || replaceStr === null)) {
                return <any>this.replaceFirst$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String(searchStr, replaceStr);
            } else if (((typeof searchStr === 'string') || searchStr === null) && ((typeof replaceStr === 'string') || replaceStr === null)) {
                return <any>this.replaceFirst$char$char(searchStr, replaceStr);
            } else throw new Error('invalid overload');
        }

        public replaceAll$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher, replaceStr: string): StrBuilder {
            return this.replace$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, replaceStr, 0, this.__size, -1);
        }

        public replaceFirst$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher, replaceStr: string): StrBuilder {
            return this.replace$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, replaceStr, 0, this.__size, 1);
        }

        public replace$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher, replaceStr: string, startIndex: number, endIndex: number, replaceCount: number): StrBuilder {
            endIndex = this.validateRange(startIndex, endIndex);
            return this.replaceImpl$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, replaceStr, startIndex, endIndex, replaceCount);
        }

        /**
         * Advanced search and replaces within the builder using a matcher.
         * <p>
         * Matchers can be used to perform advanced behaviour.
         * For example you could write a matcher to delete all occurances
         * where the character 'a' is followed by a number.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrMatcher} matcher  the matcher to use to find the deletion, null causes no action
         * @param {string} replaceStr  the string to replace the match with, null is a delete
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} endIndex  the end index, exclusive, must be valid except
         * that if too large it is treated as end of string
         * @param {number} replaceCount  the number of times to replace, -1 for replace all
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if start index is invalid
         */
        public replace(matcher?: any, replaceStr?: any, startIndex?: any, endIndex?: any, replaceCount?: any): any {
            if (((matcher != null && matcher instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || matcher === null) && ((typeof replaceStr === 'string') || replaceStr === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof endIndex === 'number') || endIndex === null) && ((typeof replaceCount === 'number') || replaceCount === null)) {
                return <any>this.replace$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, replaceStr, startIndex, endIndex, replaceCount);
            } else if (((typeof matcher === 'number') || matcher === null) && ((typeof replaceStr === 'number') || replaceStr === null) && ((typeof startIndex === 'string') || startIndex === null) && endIndex === undefined && replaceCount === undefined) {
                return <any>this.replace$int$int$java_lang_String(matcher, replaceStr, startIndex);
            } else throw new Error('invalid overload');
        }

        public replaceImpl$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher, replaceStr: string, from: number, to: number, replaceCount: number): StrBuilder {
            if (matcher == null || this.__size === 0){
                return this;
            }
            const replaceLen: number = (replaceStr == null ? 0 : replaceStr.length);
            const buf: string[] = this.buffer;
            for(let i: number = from; i < to && replaceCount !== 0; i++) {{
                const removeLen: number = matcher.isMatch$char_A$int$int$int(buf, i, from, to);
                if (removeLen > 0){
                    this.replaceImpl$int$int$int$java_lang_String$int(i, i + removeLen, removeLen, replaceStr, replaceLen);
                    to = to - removeLen + replaceLen;
                    i = i + replaceLen - 1;
                    if (replaceCount > 0){
                        replaceCount--;
                    }
                }
            };}
            return this;
        }

        /**
         * Replaces within the builder using a matcher.
         * <p>
         * Matchers can be used to perform advanced behaviour.
         * For example you could write a matcher to delete all occurances
         * where the character 'a' is followed by a number.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrMatcher} matcher  the matcher to use to find the deletion, null causes no action
         * @param {string} replaceStr  the string to replace the match with, null is a delete
         * @param {number} from  the start index, must be valid
         * @param {number} to  the end index (exclusive), must be valid
         * @param {number} replaceCount  the number of times to replace, -1 for replace all
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         * @throws IndexOutOfBoundsException if any index is invalid
         * @private
         */
        public replaceImpl(matcher?: any, replaceStr?: any, from?: any, to?: any, replaceCount?: any): any {
            if (((matcher != null && matcher instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || matcher === null) && ((typeof replaceStr === 'string') || replaceStr === null) && ((typeof from === 'number') || from === null) && ((typeof to === 'number') || to === null) && ((typeof replaceCount === 'number') || replaceCount === null)) {
                return <any>this.replaceImpl$org_openprovenance_apache_commons_lang_text_StrMatcher$java_lang_String$int$int$int(matcher, replaceStr, from, to, replaceCount);
            } else if (((typeof matcher === 'number') || matcher === null) && ((typeof replaceStr === 'number') || replaceStr === null) && ((typeof from === 'number') || from === null) && ((typeof to === 'string') || to === null) && ((typeof replaceCount === 'number') || replaceCount === null)) {
                return <any>this.replaceImpl$int$int$int$java_lang_String$int(matcher, replaceStr, from, to, replaceCount);
            } else throw new Error('invalid overload');
        }

        /**
         * Reverses the string builder placing each character in the opposite index.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public reverse(): StrBuilder {
            if (this.__size === 0){
                return this;
            }
            const half: number = (this.__size / 2|0);
            const buf: string[] = this.buffer;
            for(let leftIdx: number = 0, rightIdx: number = this.__size - 1; leftIdx < half; leftIdx++, rightIdx--) {{
                const swap: string = buf[leftIdx];
                buf[leftIdx] = buf[rightIdx];
                buf[rightIdx] = swap;
            };}
            return this;
        }

        /**
         * Trims the builder by removing characters less than or equal to a space
         * from the beginning and end.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrBuilder} this, to enable chaining
         */
        public trim(): StrBuilder {
            if (this.__size === 0){
                return this;
            }
            let len: number = this.__size;
            const buf: string[] = this.buffer;
            let pos: number = 0;
            while((pos < len && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(buf[pos]) <= ' '.charCodeAt(0))) {{
                pos++;
            }};
            while((pos < len && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(buf[len - 1]) <= ' '.charCodeAt(0))) {{
                len--;
            }};
            if (len < this.__size){
                this.delete(len, this.__size);
            }
            if (pos > 0){
                this.delete(0, pos);
            }
            return this;
        }

        /**
         * Checks whether this builder starts with the specified string.
         * <p>
         * Note that this method handles null input quietly, unlike String.
         * 
         * @param {string} str  the string to search for, null returns false
         * @return {boolean} true if the builder starts with the string
         */
        public startsWith(str: string): boolean {
            if (str == null){
                return false;
            }
            const len: number = str.length;
            if (len === 0){
                return true;
            }
            if (len > this.__size){
                return false;
            }
            for(let i: number = 0; i < len; i++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(i))){
                    return false;
                }
            };}
            return true;
        }

        /**
         * Checks whether this builder ends with the specified string.
         * <p>
         * Note that this method handles null input quietly, unlike String.
         * 
         * @param {string} str  the string to search for, null returns false
         * @return {boolean} true if the builder ends with the string
         */
        public endsWith(str: string): boolean {
            if (str == null){
                return false;
            }
            const len: number = str.length;
            if (len === 0){
                return true;
            }
            if (len > this.__size){
                return false;
            }
            let pos: number = this.__size - len;
            for(let i: number = 0; i < len; i++, pos++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[pos]) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(i))){
                    return false;
                }
            };}
            return true;
        }

        public substring$int(start: number): string {
            return this.substring$int$int(start, this.__size);
        }

        public substring$int$int(startIndex: number, endIndex: number): string {
            endIndex = this.validateRange(startIndex, endIndex);
            return this.buffer.join('').substr(startIndex, endIndex - startIndex);
        }

        /**
         * Extracts a portion of this string builder as a string.
         * <p>
         * Note: This method treats an endIndex greater than the length of the
         * builder as equal to the length of the builder, and continues
         * without error, unlike StringBuffer or String.
         * 
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} endIndex  the end index, exclusive, must be valid except
         * that if too large it is treated as end of string
         * @return {string} the new string
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        public substring(startIndex?: any, endIndex?: any): any {
            if (((typeof startIndex === 'number') || startIndex === null) && ((typeof endIndex === 'number') || endIndex === null)) {
                return <any>this.substring$int$int(startIndex, endIndex);
            } else if (((typeof startIndex === 'number') || startIndex === null) && endIndex === undefined) {
                return <any>this.substring$int(startIndex);
            } else throw new Error('invalid overload');
        }

        /**
         * Extracts the leftmost characters from the string builder without
         * throwing an exception.
         * <p>
         * This method extracts the left <code>length</code> characters from
         * the builder. If this many characters are not available, the whole
         * builder is returned. Thus the returned string may be shorter than the
         * length requested.
         * 
         * @param {number} length  the number of characters to extract, negative returns empty string
         * @return {string} the new string
         */
        public leftString(length: number): string {
            if (length <= 0){
                return "";
            } else if (length >= this.__size){
                return this.buffer.join('').substr(0, this.__size);
            } else {
                return this.buffer.join('').substr(0, length);
            }
        }

        /**
         * Extracts the rightmost characters from the string builder without
         * throwing an exception.
         * <p>
         * This method extracts the right <code>length</code> characters from
         * the builder. If this many characters are not available, the whole
         * builder is returned. Thus the returned string may be shorter than the
         * length requested.
         * 
         * @param {number} length  the number of characters to extract, negative returns empty string
         * @return {string} the new string
         */
        public rightString(length: number): string {
            if (length <= 0){
                return "";
            } else if (length >= this.__size){
                return this.buffer.join('').substr(0, this.__size);
            } else {
                return this.buffer.join('').substr(this.__size - length, length);
            }
        }

        /**
         * Extracts some characters from the middle of the string builder without
         * throwing an exception.
         * <p>
         * This method extracts <code>length</code> characters from the builder
         * at the specified index.
         * If the index is negative it is treated as zero.
         * If the index is greater than the builder size, it is treated as the builder size.
         * If the length is negative, the empty string is returned.
         * If insufficient characters are available in the builder, as much as possible is returned.
         * Thus the returned string may be shorter than the length requested.
         * 
         * @param {number} index  the index to start at, negative means zero
         * @param {number} length  the number of characters to extract, negative returns empty string
         * @return {string} the new string
         */
        public midString(index: number, length: number): string {
            if (index < 0){
                index = 0;
            }
            if (length <= 0 || index >= this.__size){
                return "";
            }
            if (this.__size <= index + length){
                return this.buffer.join('').substr(index, this.__size - index);
            } else {
                return this.buffer.join('').substr(index, length);
            }
        }

        public contains$char(ch: string): boolean {
            const thisBuf: string[] = this.buffer;
            for(let i: number = 0; i < this.__size; i++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(thisBuf[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch)){
                    return true;
                }
            };}
            return false;
        }

        public contains$java_lang_String(str: string): boolean {
            return this.indexOf$java_lang_String$int(str, 0) >= 0;
        }

        /**
         * Checks if the string builder contains the specified string.
         * 
         * @param {string} str  the string to find
         * @return {boolean} true if the builder contains the string
         */
        public contains(str?: any): any {
            if (((typeof str === 'string') || str === null)) {
                return <any>this.contains$java_lang_String(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null)) {
                return <any>this.contains$org_openprovenance_apache_commons_lang_text_StrMatcher(str);
            } else if (((typeof str === 'string') || str === null)) {
                return <any>this.contains$char(str);
            } else throw new Error('invalid overload');
        }

        public contains$org_openprovenance_apache_commons_lang_text_StrMatcher(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher): boolean {
            return this.indexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(matcher, 0) >= 0;
        }

        public indexOf$char(ch: string): number {
            return this.indexOf$char$int(ch, 0);
        }

        public indexOf$char$int(ch: string, startIndex: number): number {
            startIndex = (startIndex < 0 ? 0 : startIndex);
            if (startIndex >= this.__size){
                return -1;
            }
            const thisBuf: string[] = this.buffer;
            for(let i: number = startIndex; i < this.__size; i++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(thisBuf[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch)){
                    return i;
                }
            };}
            return -1;
        }

        public indexOf$java_lang_String(str: string): number {
            return this.indexOf$java_lang_String$int(str, 0);
        }

        public indexOf$java_lang_String$int(str: string, startIndex: number): number {
            startIndex = (startIndex < 0 ? 0 : startIndex);
            if (str == null || startIndex >= this.__size){
                return -1;
            }
            const strLen: number = str.length;
            if (strLen === 1){
                return this.indexOf$char$int(str.charAt(0), startIndex);
            }
            if (strLen === 0){
                return startIndex;
            }
            if (strLen > this.__size){
                return -1;
            }
            const thisBuf: string[] = this.buffer;
            const len: number = this.__size - strLen + 1;
            outer: for(let i: number = startIndex; i < len; i++) {{
                for(let j: number = 0; j < strLen; j++) {{
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(j)) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(thisBuf[i + j])){
                        continue outer;
                    }
                };}
                return i;
            };}
            return -1;
        }

        /**
         * Searches the string builder to find the first reference to the specified
         * string starting searching from the given index.
         * <p>
         * Note that a null input string will return -1, whereas the JDK throws an exception.
         * 
         * @param {string} str  the string to find, null returns -1
         * @param {number} startIndex  the index to start at, invalid index rounded to edge
         * @return {number} the first index of the string, or -1 if not found
         */
        public indexOf(str?: any, startIndex?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof startIndex === 'number') || startIndex === null)) {
                return <any>this.indexOf$java_lang_String$int(str, startIndex);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null) && ((typeof startIndex === 'number') || startIndex === null)) {
                return <any>this.indexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(str, startIndex);
            } else if (((typeof str === 'string') || str === null) && ((typeof startIndex === 'number') || startIndex === null)) {
                return <any>this.indexOf$char$int(str, startIndex);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined) {
                return <any>this.indexOf$java_lang_String(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null) && startIndex === undefined) {
                return <any>this.indexOf$org_openprovenance_apache_commons_lang_text_StrMatcher(str);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined) {
                return <any>this.indexOf$char(str);
            } else throw new Error('invalid overload');
        }

        public indexOf$org_openprovenance_apache_commons_lang_text_StrMatcher(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher): number {
            return this.indexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(matcher, 0);
        }

        public indexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher, startIndex: number): number {
            startIndex = (startIndex < 0 ? 0 : startIndex);
            if (matcher == null || startIndex >= this.__size){
                return -1;
            }
            const len: number = this.__size;
            const buf: string[] = this.buffer;
            for(let i: number = startIndex; i < len; i++) {{
                if (matcher.isMatch$char_A$int$int$int(buf, i, startIndex, len) > 0){
                    return i;
                }
            };}
            return -1;
        }

        public lastIndexOf$char(ch: string): number {
            return this.lastIndexOf$char$int(ch, this.__size - 1);
        }

        public lastIndexOf$char$int(ch: string, startIndex: number): number {
            startIndex = (startIndex >= this.__size ? this.__size - 1 : startIndex);
            if (startIndex < 0){
                return -1;
            }
            for(let i: number = startIndex; i >= 0; i--) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i]) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch)){
                    return i;
                }
            };}
            return -1;
        }

        public lastIndexOf$java_lang_String(str: string): number {
            return this.lastIndexOf$java_lang_String$int(str, this.__size - 1);
        }

        public lastIndexOf$java_lang_String$int(str: string, startIndex: number): number {
            startIndex = (startIndex >= this.__size ? this.__size - 1 : startIndex);
            if (str == null || startIndex < 0){
                return -1;
            }
            const strLen: number = str.length;
            if (strLen > 0 && strLen <= this.__size){
                if (strLen === 1){
                    return this.lastIndexOf$char$int(str.charAt(0), startIndex);
                }
                outer: for(let i: number = startIndex - strLen + 1; i >= 0; i--) {{
                    for(let j: number = 0; j < strLen; j++) {{
                        if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(j)) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.buffer[i + j])){
                            continue outer;
                        }
                    };}
                    return i;
                };}
            } else if (strLen === 0){
                return startIndex;
            }
            return -1;
        }

        /**
         * Searches the string builder to find the last reference to the specified
         * string starting searching from the given index.
         * <p>
         * Note that a null input string will return -1, whereas the JDK throws an exception.
         * 
         * @param {string} str  the string to find, null returns -1
         * @param {number} startIndex  the index to start at, invalid index rounded to edge
         * @return {number} the last index of the string, or -1 if not found
         */
        public lastIndexOf(str?: any, startIndex?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof startIndex === 'number') || startIndex === null)) {
                return <any>this.lastIndexOf$java_lang_String$int(str, startIndex);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null) && ((typeof startIndex === 'number') || startIndex === null)) {
                return <any>this.lastIndexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(str, startIndex);
            } else if (((typeof str === 'string') || str === null) && ((typeof startIndex === 'number') || startIndex === null)) {
                return <any>this.lastIndexOf$char$int(str, startIndex);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined) {
                return <any>this.lastIndexOf$java_lang_String(str);
            } else if (((str != null && str instanceof <any>org.openprovenance.apache.commons.lang.text.StrMatcher) || str === null) && startIndex === undefined) {
                return <any>this.lastIndexOf$org_openprovenance_apache_commons_lang_text_StrMatcher(str);
            } else if (((typeof str === 'string') || str === null) && startIndex === undefined) {
                return <any>this.lastIndexOf$char(str);
            } else throw new Error('invalid overload');
        }

        public lastIndexOf$org_openprovenance_apache_commons_lang_text_StrMatcher(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher): number {
            return this.lastIndexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(matcher, this.__size);
        }

        public lastIndexOf$org_openprovenance_apache_commons_lang_text_StrMatcher$int(matcher: org.openprovenance.apache.commons.lang.text.StrMatcher, startIndex: number): number {
            startIndex = (startIndex >= this.__size ? this.__size - 1 : startIndex);
            if (matcher == null || startIndex < 0){
                return -1;
            }
            const buf: string[] = this.buffer;
            const endIndex: number = startIndex + 1;
            for(let i: number = startIndex; i >= 0; i--) {{
                if (matcher.isMatch$char_A$int$int$int(buf, i, 0, endIndex) > 0){
                    return i;
                }
            };}
            return -1;
        }

        /**
         * Creates a tokenizer that can tokenize the contents of this builder.
         * <p>
         * This method allows the contents of this builder to be tokenized.
         * The tokenizer will be setup by default to tokenize on space, tab,
         * newline and formfeed (as per StringTokenizer). These values can be
         * changed on the tokenizer class, before retrieving the tokens.
         * <p>
         * The returned tokenizer is linked to this builder. You may intermix
         * calls to the buider and tokenizer within certain limits, however
         * there is no synchronization. Once the tokenizer has been used once,
         * it must be {@link StrTokenizer#reset() reset} to pickup the latest
         * changes in the builder. For example:
         * <pre>
         * StrBuilder b = new StrBuilder();
         * b.append("a b ");
         * StrTokenizer t = b.asTokenizer();
         * String[] tokens1 = t.getTokenArray();  // returns a,b
         * b.append("c d ");
         * String[] tokens2 = t.getTokenArray();  // returns a,b (c and d ignored)
         * t.reset();              // reset causes builder changes to be picked up
         * String[] tokens3 = t.getTokenArray();  // returns a,b,c,d
         * </pre>
         * In addition to simply intermixing appends and tokenization, you can also
         * call the set methods on the tokenizer to alter how it tokenizes. Just
         * remember to call reset when you want to pickup builder changes.
         * <p>
         * Calling {@link StrTokenizer#reset(String)} or {@link StrTokenizer#reset(char[])}
         * with a non-null value will break the link with the builder.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrTokenizer} a tokenizer that is linked to this builder
         */
        public asTokenizer(): org.openprovenance.apache.commons.lang.text.StrTokenizer {
            return new StrBuilder.StrBuilderTokenizer(this);
        }

        /**
         * Gets the contents of this builder as a Reader.
         * <p>
         * This method allows the contents of the builder to be read
         * using any standard method that expects a Reader.
         * <p>
         * To use, simply create a <code>StrBuilder</code>, populate it with
         * data, call <code>asReader</code>, and then read away.
         * <p>
         * The internal character array is shared between the builder and the reader.
         * This allows you to append to the builder after creating the reader,
         * and the changes will be picked up.
         * Note however, that no synchronization occurs, so you must perform
         * all operations with the builder and the reader in one thread.
         * <p>
         * The returned reader supports marking, and ignores the flush method.
         * 
         * @return {{ str: string, cursor: number }} a reader that reads from this builder
         */
        public asReader(): { str: string, cursor: number } {
            return (() => { let __o : any = new StrBuilder.StrBuilderReader(); __o.__delegate = new StrBuilder.StrBuilderReader(this); return __o; })();
        }

        /**
         * Gets this builder as a Writer that can be written to.
         * <p>
         * This method allows you to populate the contents of the builder
         * using any standard method that takes a Writer.
         * <p>
         * To use, simply create a <code>StrBuilder</code>,
         * call <code>asWriter</code>, and populate away. The data is available
         * at any time using the methods of the <code>StrBuilder</code>.
         * <p>
         * The internal character array is shared between the builder and the writer.
         * This allows you to intermix calls that append to the builder and
         * write using the writer and the changes will be occur correctly.
         * Note however, that no synchronization occurs, so you must perform
         * all operations with the builder and the writer in one thread.
         * <p>
         * The returned writer ignores the close and flush methods.
         * 
         * @return {java.io.Writer} a writer that populates this builder
         */
        public asWriter(): java.io.Writer {
            return (() => { let __o : any = new StrBuilder.StrBuilderWriter(); __o.__delegate = new StrBuilder.StrBuilderWriter(this); return __o; })();
        }

        /**
         * Checks the contents of this builder against another to see if they
         * contain the same character content ignoring case.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrBuilder} other  the object to check, null returns false
         * @return {boolean} true if the builders contain the same characters in the same order
         */
        public equalsIgnoreCase(other: StrBuilder): boolean {
            if (this === other){
                return true;
            }
            if (this.__size !== other.__size){
                return false;
            }
            const thisBuf: string[] = this.buffer;
            const otherBuf: string[] = other.buffer;
            for(let i: number = this.__size - 1; i >= 0; i--) {{
                const c1: string = thisBuf[i];
                const c2: string = otherBuf[i];
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(c1) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(c2) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(/* toUpperCase */c1.toUpperCase()) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(/* toUpperCase */c2.toUpperCase())){
                    return false;
                }
            };}
            return true;
        }

        public equals$org_openprovenance_apache_commons_lang_text_StrBuilder(other: StrBuilder): boolean {
            if (this === other){
                return true;
            }
            if (this.__size !== other.__size){
                return false;
            }
            const thisBuf: string[] = this.buffer;
            const otherBuf: string[] = other.buffer;
            for(let i: number = this.__size - 1; i >= 0; i--) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(thisBuf[i]) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(otherBuf[i])){
                    return false;
                }
            };}
            return true;
        }

        /**
         * Checks the contents of this builder against another to see if they
         * contain the same character content.
         * 
         * @param {org.openprovenance.apache.commons.lang.text.StrBuilder} other  the object to check, null returns false
         * @return {boolean} true if the builders contain the same characters in the same order
         */
        public equals(other?: any): any {
            if (((other != null && other instanceof <any>org.openprovenance.apache.commons.lang.text.StrBuilder) || other === null)) {
                return <any>this.equals$org_openprovenance_apache_commons_lang_text_StrBuilder(other);
            } else if (((other != null) || other === null)) {
                return <any>this.equals$java_lang_Object(other);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(obj: any): boolean {
            if (obj != null && obj instanceof <any>org.openprovenance.apache.commons.lang.text.StrBuilder){
                return this.equals$org_openprovenance_apache_commons_lang_text_StrBuilder(<StrBuilder>obj);
            }
            return false;
        }

        /**
         * Gets a suitable hash code for this builder.
         * 
         * @return {number} a hash code
         */
        public hashCode(): number {
            const buf: string[] = this.buffer;
            let hash: number = 0;
            for(let i: number = this.__size - 1; i >= 0; i--) {{
                hash = 31 * hash + (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(buf[i]);
            };}
            return hash;
        }

        /**
         * Gets a String version of the string builder, creating a new instance
         * each time the method is called.
         * <p>
         * Note that unlike StringBuffer, the string version returned is
         * independent of the string builder.
         * 
         * @return {string} the builder as a String
         */
        public toString(): string {
            return this.buffer.join('').substr(0, this.__size);
        }

        /**
         * Gets a StringBuffer version of the string builder, creating a
         * new instance each time the method is called.
         * 
         * @return {{ str: string, toString: Function }} the builder as a StringBuffer
         */
        public toStringBuffer(): { str: string, toString: Function } {
            return /* append */(sb => { sb.str += (<any>this.buffer).substr(0, this.__size); return sb; })({ str: "", toString: function() { return this.str; } });
        }

        /**
         * Clone this object.
         * 
         * @return {*} a clone of this object
         * @throws CloneNotSupportedException if clone is not supported
         * @since 2.6
         */
        public clone(): any {
            const clone: StrBuilder = <StrBuilder>/* clone *//* clone */((o: any) => { let clone = Object.create(o); for(let p in o) { if (o.hasOwnProperty(p)) clone[p] = o[p]; } return clone; })(this);
            clone.buffer = (s => { let a=[]; while(s-->0) a.push(null); return a; })(this.buffer.length);
            /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(this.buffer, 0, clone.buffer, 0, this.buffer.length);
            return clone;
        }

        /**
         * Validates parameters defining a range of the builder.
         * 
         * @param {number} startIndex  the start index, inclusive, must be valid
         * @param {number} endIndex  the end index, exclusive, must be valid except
         * that if too large it is treated as end of string
         * @return {number} the new string
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        validateRange(startIndex: number, endIndex: number): number {
            if (startIndex < 0){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (endIndex > this.__size){
                endIndex = this.__size;
            }
            if (startIndex > endIndex){
                throw Object.defineProperty(new Error("end < start"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            return endIndex;
        }

        /**
         * Validates parameters defining a single index in the builder.
         * 
         * @param {number} index  the index, must be valid
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        validateIndex(index: number) {
            if (index < 0 || index > this.__size){
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.StringIndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }
    }
    StrBuilder["__class"] = "org.openprovenance.apache.commons.lang.text.StrBuilder";
    StrBuilder["__interfaces"] = ["java.lang.Cloneable"];



    export namespace StrBuilder {

        /**
         * Inner class to allow StrBuilder to operate as a tokenizer.
         * @extends org.openprovenance.apache.commons.lang.text.StrTokenizer
         * @class
         */
        export class StrBuilderTokenizer extends org.openprovenance.apache.commons.lang.text.StrTokenizer {
            public __parent: any;
            constructor(__parent: any) {
                super();
                this.__parent = __parent;
            }

            /**
             * {@inheritDoc}
             * @param {char[]} chars
             * @param {number} offset
             * @param {number} count
             * @return {Array}
             */
            tokenize(chars: string[], offset: number, count: number): Array<any> {
                if (chars == null){
                    return super.tokenize(this.__parent.buffer, 0, this.size());
                } else {
                    return super.tokenize(chars, offset, count);
                }
            }

            /**
             * {@inheritDoc}
             * @return {string}
             */
            public getContent(): string {
                const str: string = super.getContent();
                if (str == null){
                    return this.toString();
                } else {
                    return str;
                }
            }
        }
        StrBuilderTokenizer["__class"] = "org.openprovenance.apache.commons.lang.text.StrBuilder.StrBuilderTokenizer";
        StrBuilderTokenizer["__interfaces"] = ["java.lang.Cloneable","java.util.Iterator","java.util.ListIterator"];



        /**
         * Inner class to allow StrBuilder to operate as a writer.
         * @extends { str: string, cursor: number }
         * @class
         */
        export class StrBuilderReader {
            public __parent: any;
            /**
             * The current stream position.
             */
            pos: number;

            /**
             * The last mark position.
             */
            __mark: number;

            constructor(__parent: any) {
                this.__parent = __parent;
                if (this.pos === undefined) { this.pos = 0; }
                if (this.__mark === undefined) { this.__mark = 0; }
            }

            /**
             * {@inheritDoc}
             */
            public close() {
            }

            public read$(): number {
                if (this.ready() === false){
                    return -1;
                }
                return (this.__parent.charAt(this.pos++)).charCodeAt(0);
            }

            public read$char_A$int$int(b: string[], off: number, len: number): number {
                if (off < 0 || len < 0 || off > b.length || (off + len) > b.length || (off + len) < 0){
                    throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IndexOutOfBoundsException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
                }
                if (len === 0){
                    return 0;
                }
                if (this.pos >= this.__parent.size()){
                    return -1;
                }
                if (this.pos + len > this.__parent.size()){
                    len = this.__parent.size() - this.pos;
                }
                this.__parent.getChars$int$int$char_A$int(this.pos, this.pos + len, b, off);
                this.pos += len;
                return len;
            }

            /**
             * {@inheritDoc}
             * @param {char[]} b
             * @param {number} off
             * @param {number} len
             * @return {number}
             */
            public read(b?: any, off?: any, len?: any): any {
                if (((b != null && b instanceof <any>Array && (b.length == 0 || b[0] == null ||(typeof b[0] === 'string'))) || b === null) && ((typeof off === 'number') || off === null) && ((typeof len === 'number') || len === null)) {
                    return <any>this.read$char_A$int$int(b, off, len);
                } else if (b === undefined && off === undefined && len === undefined) {
                    return <any>this.read$();
                } else throw new Error('invalid overload');
            }

            /**
             * {@inheritDoc}
             * @param {number} n
             * @return {number}
             */
            public skip(n: number): number {
                if (this.pos + n > this.__parent.size()){
                    n = this.__parent.size() - this.pos;
                }
                if (n < 0){
                    return 0;
                }
                this.pos += n;
                return n;
            }

            /**
             * {@inheritDoc}
             * @return {boolean}
             */
            public ready(): boolean {
                return this.pos < this.__parent.size();
            }

            /**
             * {@inheritDoc}
             * @return {boolean}
             */
            public markSupported(): boolean {
                return true;
            }

            /**
             * {@inheritDoc}
             * @param {number} readAheadLimit
             */
            public mark(readAheadLimit: number) {
                this.__mark = this.pos;
            }

            /**
             * {@inheritDoc}
             */
            public reset() {
                this.pos = this.__mark;
            }
        }
        StrBuilderReader["__class"] = "org.openprovenance.apache.commons.lang.text.StrBuilder.StrBuilderReader";
        StrBuilderReader["__interfaces"] = ["java.io.Closeable","java.lang.Readable","java.lang.AutoCloseable"];



        /**
         * Inner class to allow StrBuilder to operate as a writer.
         * @extends java.io.Writer
         * @class
         */
        export class StrBuilderWriter {
            public __parent: any;
            constructor(__parent: any) {
                this.__parent = __parent;
            }

            /**
             * {@inheritDoc}
             */
            public close() {
            }

            /**
             * {@inheritDoc}
             */
            public flush() {
            }

            public write$int(c: number) {
                this.append$char(String.fromCharCode(c));
            }

            public write$char_A(cbuf: string[]) {
                this.append$char_A(cbuf);
            }

            public write$char_A$int$int(cbuf: string[], off: number, len: number) {
                this.append$char_A$int$int(cbuf, off, len);
            }

            /**
             * {@inheritDoc}
             * @param {char[]} cbuf
             * @param {number} off
             * @param {number} len
             */
            public write(cbuf?: any, off?: any, len?: any) {
                if (((cbuf != null && cbuf instanceof <any>Array && (cbuf.length == 0 || cbuf[0] == null ||(typeof cbuf[0] === 'string'))) || cbuf === null) && ((typeof off === 'number') || off === null) && ((typeof len === 'number') || len === null)) {
                    return <any>this.write$char_A$int$int(cbuf, off, len);
                } else if (((typeof cbuf === 'string') || cbuf === null) && ((typeof off === 'number') || off === null) && ((typeof len === 'number') || len === null)) {
                    return <any>this.write$java_lang_String$int$int(cbuf, off, len);
                } else if (((cbuf != null && cbuf instanceof <any>Array && (cbuf.length == 0 || cbuf[0] == null ||(typeof cbuf[0] === 'string'))) || cbuf === null) && off === undefined && len === undefined) {
                    return <any>this.write$char_A(cbuf);
                } else if (((typeof cbuf === 'string') || cbuf === null) && off === undefined && len === undefined) {
                    return <any>this.write$java_lang_String(cbuf);
                } else if (((typeof cbuf === 'number') || cbuf === null) && off === undefined && len === undefined) {
                    return <any>this.write$int(cbuf);
                } else throw new Error('invalid overload');
            }

            public write$java_lang_String(str: string) {
                this.append$java_lang_String(str);
            }

            public write$java_lang_String$int$int(str: string, off: number, len: number) {
                this.append$java_lang_String$int$int(str, off, len);
            }
        }
        StrBuilderWriter["__class"] = "org.openprovenance.apache.commons.lang.text.StrBuilder.StrBuilderWriter";
        StrBuilderWriter["__interfaces"] = ["java.lang.Appendable","java.io.Closeable","java.lang.AutoCloseable","java.io.Flushable"];


    }

}

