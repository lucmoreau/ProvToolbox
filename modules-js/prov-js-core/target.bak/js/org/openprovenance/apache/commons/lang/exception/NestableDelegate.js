/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var apache;
        (function (apache) {
            var commons;
            (function (commons) {
                var lang;
                (function (lang) {
                    var exception;
                    (function (exception) {
                        /**
                         * Constructs a new <code>NestableDelegate</code> instance to manage the
                         * specified <code>Nestable</code>.
                         *
                         * @param {*} nestable the Nestable implementation (<i>must</i> extend
                         * {@link Throwable})
                         * @since 2.0
                         * @class
                         * @author Apache Software Foundation
                         */
                        class NestableDelegate {
                            constructor(nestable) {
                                this.nestable = null;
                                if (nestable != null && (nestable["__classes"] && nestable["__classes"].indexOf("java.lang.Throwable") >= 0) || nestable != null && nestable instanceof Error) {
                                    this.nestable = nestable;
                                }
                                else {
                                    throw Object.defineProperty(new Error(NestableDelegate.MUST_BE_THROWABLE), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                            }
                            getMessage$int(index) {
                                const t = this.getThrowable(index);
                                if ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                    return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                else if (typeof c === 'function')
                                    return (o instanceof c) || (o.constructor && o.constructor === c); })("org.openprovenance.apache.commons.lang.exception.Nestable", t)) {
                                    return t['getMessage$int'](0);
                                }
                                return t.message;
                            }
                            getMessage$java_lang_String(baseMsg) {
                                const nestedCause = org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getCause$java_lang_Throwable(this.nestable);
                                const causeMsg = nestedCause == null ? null : nestedCause.message;
                                if (nestedCause == null || causeMsg == null) {
                                    return baseMsg;
                                }
                                if (baseMsg == null) {
                                    return causeMsg;
                                }
                                return baseMsg + ": " + causeMsg;
                            }
                            /**
                             * Returns the full message contained by the <code>Nestable</code> and any nested <code>Throwable</code>s.
                             *
                             * @param {string} baseMsg
                             * the base message to use when creating the full message. Should be generally be called via
                             * <code>nestableHelper.getMessage(super.getMessage())</code>, where <code>super</code> is an
                             * instance of {@link Throwable}.
                             * @return {string} The concatenated message for this and all nested <code>Throwable</code>s
                             * @since 2.0
                             */
                            getMessage(baseMsg) {
                                if (((typeof baseMsg === 'string') || baseMsg === null)) {
                                    return this.getMessage$java_lang_String(baseMsg);
                                }
                                else if (((typeof baseMsg === 'number') || baseMsg === null)) {
                                    return this.getMessage$int(baseMsg);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            /**
                             * Returns the error message of this and any nested <code>Throwable</code>s in an array of Strings, one element
                             * for each message. Any <code>Throwable</code> not containing a message is represented in the array by a null.
                             * This has the effect of cause the length of the returned array to be equal to the result of the
                             * {@link #getThrowableCount()} operation.
                             *
                             * @return {java.lang.String[]} the error messages
                             * @since 2.0
                             */
                            getMessages() {
                                const throwables = this.getThrowables();
                                const msgs = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(throwables.length);
                                for (let i = 0; i < throwables.length; i++) {
                                    {
                                        msgs[i] = ( /* isInstance */((c, o) => { if (typeof c === 'string')
                                            return (o.constructor && o.constructor["__interfaces"] && o.constructor["__interfaces"].indexOf(c) >= 0) || (o["__interfaces"] && o["__interfaces"].indexOf(c) >= 0);
                                        else if (typeof c === 'function')
                                            return (o instanceof c) || (o.constructor && o.constructor === c); })("org.openprovenance.apache.commons.lang.exception.Nestable", throwables[i]) ? throwables[i]['getMessage$int'](0) : throwables[i].message);
                                    }
                                    ;
                                }
                                return msgs;
                            }
                            /**
                             * Returns the <code>Throwable</code> in the chain of
                             * <code>Throwable</code>s at the specified index, numbered from 0.
                             *
                             * @param {number} index the index, numbered from 0, of the <code>Throwable</code> in
                             * the chain of <code>Throwable</code>s
                             * @return {Error} the <code>Throwable</code>
                             * @throws IndexOutOfBoundsException if the <code>index</code> argument is
                             * negative or not less than the count of <code>Throwable</code>s in the
                             * chain
                             * @since 2.0
                             */
                            getThrowable(index) {
                                if (index === 0) {
                                    return this.nestable;
                                }
                                const throwables = this.getThrowables();
                                return throwables[index];
                            }
                            /**
                             * Returns the number of <code>Throwable</code>s contained in the
                             * <code>Nestable</code> contained by this delegate.
                             *
                             * @return {number} the throwable count
                             * @since 2.0
                             */
                            getThrowableCount() {
                                return org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getThrowableCount(this.nestable);
                            }
                            /**
                             * Returns this delegate's <code>Nestable</code> and any nested
                             * <code>Throwable</code>s in an array of <code>Throwable</code>s, one
                             * element for each <code>Throwable</code>.
                             *
                             * @return {java.lang.Throwable[]} the <code>Throwable</code>s
                             * @since 2.0
                             */
                            getThrowables() {
                                return org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getThrowables(this.nestable);
                            }
                            /**
                             * Returns the index, numbered from 0, of the first <code>Throwable</code>
                             * that matches the specified type, or a subclass, in the chain of <code>Throwable</code>s
                             * with an index greater than or equal to the specified index.
                             * The method returns -1 if the specified type is not found in the chain.
                             * <p>
                             * NOTE: From v2.1, we have clarified the <code>Nestable</code> interface
                             * such that this method matches subclasses.
                             * If you want to NOT match subclasses, please use
                             * {@link ExceptionUtils#indexOfThrowable(Throwable, Class, int)}
                             * (which is avaiable in all versions of lang).
                             * An alternative is to use the public static flag {@link #matchSubclasses}
                             * on <code>NestableDelegate</code>, however this is not recommended.
                             *
                             * @param {*} type  the type to find, subclasses match, null returns -1
                             * @param {number} fromIndex the index, numbered from 0, of the starting position in
                             * the chain to be searched
                             * @return {number} index of the first occurrence of the type in the chain, or -1 if
                             * the type is not found
                             * @throws IndexOutOfBoundsException if the <code>fromIndex</code> argument
                             * is negative or not less than the count of <code>Throwable</code>s in the
                             * chain
                             * @since 2.0
                             */
                            indexOfThrowable(type, fromIndex) {
                                if (type == null) {
                                    return -1;
                                }
                                if (fromIndex < 0) {
                                    throw Object.defineProperty(new Error("The start index was out of bounds: " + fromIndex), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                }
                                const throwables = org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getThrowables(this.nestable);
                                if (fromIndex >= throwables.length) {
                                    throw Object.defineProperty(new Error("The start index was out of bounds: " + fromIndex + " >= " + throwables.length), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                }
                                if (NestableDelegate.matchSubclasses) {
                                    for (let i = fromIndex; i < throwables.length; i++) {
                                        {
                                            if (type.isAssignableFrom(throwables[i].constructor)) {
                                                return i;
                                            }
                                        }
                                        ;
                                    }
                                }
                                else {
                                    for (let i = fromIndex; i < throwables.length; i++) {
                                        {
                                            if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(type, throwables[i].constructor)) {
                                                return i;
                                            }
                                        }
                                        ;
                                    }
                                }
                                return -1;
                            }
                            printStackTrace$() {
                                this.printStackTrace$java_io_PrintStream(java.lang.System.err);
                            }
                            printStackTrace$java_io_PrintStream(out) {
                                {
                                    const pw = new java.io.PrintWriter(out, false);
                                    this.printStackTrace$java_io_PrintWriter(pw);
                                    pw.flush();
                                }
                                ;
                            }
                            /**
                             * Prints the stack trace of this exception to the specified
                             * stream.
                             *
                             * @param {java.io.PrintStream} out <code>PrintStream</code> to use for output.
                             * @see #printStackTrace(PrintWriter)
                             */
                            printStackTrace(out) {
                                if (((out != null && out instanceof java.io.PrintStream) || out === null)) {
                                    return this.printStackTrace$java_io_PrintStream(out);
                                }
                                else if (((out != null && out instanceof java.io.PrintWriter) || out === null)) {
                                    return this.printStackTrace$java_io_PrintWriter(out);
                                }
                                else if (out === undefined) {
                                    return this.printStackTrace$();
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            printStackTrace$java_io_PrintWriter(out) {
                                let throwable = this.nestable;
                                if (org.openprovenance.apache.commons.lang.exception.ExceptionUtils.isThrowableNested()) {
                                    if (throwable != null && (throwable.constructor != null && throwable.constructor["__interfaces"] != null && throwable.constructor["__interfaces"].indexOf("org.openprovenance.apache.commons.lang.exception.Nestable") >= 0)) {
                                        throwable.printPartialStackTrace(out);
                                    }
                                    else {
                                        console.error(throwable.message, throwable);
                                    }
                                    return;
                                }
                                const stacks = ([]);
                                while ((throwable != null)) {
                                    {
                                        const st = this.getStackFrames(throwable);
                                        /* add */ (stacks.push(st) > 0);
                                        throwable = org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getCause$java_lang_Throwable(throwable);
                                    }
                                }
                                ;
                                let separatorLine = "Caused by: ";
                                if (!NestableDelegate.topDown) {
                                    separatorLine = "Rethrown as: ";
                                    /* reverse */ stacks.reverse();
                                }
                                if (NestableDelegate.__trimStackFrames) {
                                    this.trimStackFrames(stacks);
                                }
                                {
                                    for (const iter = ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(stacks); iter.hasNext();) {
                                        {
                                            const st = iter.next();
                                            for (let i = 0, len = st.length; i < len; i++) {
                                                {
                                                    out.println(st[i]);
                                                }
                                                ;
                                            }
                                            if (iter.hasNext()) {
                                                out.print(separatorLine);
                                            }
                                        }
                                        ;
                                    }
                                }
                                ;
                            }
                            /**
                             * Captures the stack trace associated with the specified
                             * <code>Throwable</code> object, decomposing it into a list of
                             * stack frames.
                             *
                             * @param {Error} t The <code>Throwable</code>.
                             * @return  {java.lang.String[]} An array of strings describing each stack frame.
                             * @since 2.0
                             */
                            getStackFrames(t) {
                                const sw = new java.io.StringWriter();
                                const pw = new java.io.PrintWriter(sw, true);
                                if (t != null && (t.constructor != null && t.constructor["__interfaces"] != null && t.constructor["__interfaces"].indexOf("org.openprovenance.apache.commons.lang.exception.Nestable") >= 0)) {
                                    t.printPartialStackTrace(pw);
                                }
                                else {
                                    console.error(t.message, t);
                                }
                                return org.openprovenance.apache.commons.lang.exception.ExceptionUtils.getStackFrames$java_lang_String(/* toString */ sw.getBuffer().str);
                            }
                            /**
                             * Trims the stack frames. The first set is left untouched. The rest
                             * of the frames are truncated from the bottom by comparing with
                             * one just on top.
                             *
                             * @param {Array} stacks The list containing String[] elements
                             * @since 2.0
                             */
                            trimStackFrames(stacks) {
                                for (const size = stacks.length, i = size - 1; i > 0; i--) {
                                    {
                                        const curr = stacks[i];
                                        const next = stacks[i - 1];
                                        const currList = ( /* asList */curr.slice(0).slice(0));
                                        const nextList = ( /* asList */next.slice(0).slice(0));
                                        org.openprovenance.apache.commons.lang.exception.ExceptionUtils.removeCommonFrames(currList, nextList);
                                        const trimmed = curr.length - /* size */ currList.length;
                                        if (trimmed > 0) {
                                            /* add */ (currList.push("\t... " + trimmed + " more") > 0);
                                            /* set */ (stacks[i] = /* toArray */ currList.slice(0));
                                        }
                                    }
                                    ;
                                }
                            }
                        }
                        /**
                         * Required for serialization support.
                         *
                         * @see Serializable
                         */
                        NestableDelegate.serialVersionUID = 1;
                        /**
                         * Constructor error message.
                         */
                        NestableDelegate.MUST_BE_THROWABLE = "The Nestable implementation passed to the NestableDelegate(Nestable) constructor must extend java.lang.Throwable";
                        /**
                         * Whether to print the stack trace top-down.
                         * This public flag may be set by calling code, typically in initialisation.
                         * This exists for backwards compatability, setting it to false will return
                         * the library to v1.0 behaviour (but will affect all users of the library
                         * in the classloader).
                         * @since 2.0
                         */
                        NestableDelegate.topDown = true;
                        /**
                         * Whether to trim the repeated stack trace.
                         * This public flag may be set by calling code, typically in initialisation.
                         * This exists for backwards compatability, setting it to false will return
                         * the library to v1.0 behaviour (but will affect all users of the library
                         * in the classloader).
                         * @since 2.0
                         */
                        NestableDelegate.__trimStackFrames = true;
                        /**
                         * Whether to match subclasses via indexOf.
                         * This public flag may be set by calling code, typically in initialisation.
                         * This exists for backwards compatability, setting it to false will return
                         * the library to v2.0 behaviour (but will affect all users of the library
                         * in the classloader).
                         * @since 2.1
                         */
                        NestableDelegate.matchSubclasses = true;
                        exception.NestableDelegate = NestableDelegate;
                        NestableDelegate["__class"] = "org.openprovenance.apache.commons.lang.exception.NestableDelegate";
                        NestableDelegate["__interfaces"] = ["java.io.Serializable"];
                    })(exception = lang.exception || (lang.exception = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
