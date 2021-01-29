/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.exception {
    /**
     * An interface to be implemented by {@link Throwable}
     * extensions which would like to be able to nest root exceptions
     * inside themselves.
     * 
     * @author Daniel L. Rall
     * @author <a href="mailto:knielsen@apache.org">Kasper Nielsen</a>
     * @author <a href="mailto:steven@caswell.name">Steven Caswell</a>
     * @author Pete Gieser
     * @since 1.0
     * @version $Id: Nestable.java 512889 2007-02-28 18:18:20Z dlr $
     * @class
     */
    export interface Nestable {
        /**
         * Returns the reference to the exception or error that caused the
         * exception implementing the <code>Nestable</code> to be thrown.
         * 
         * @return {Error} throwable that caused the original exception
         */
        getCause(): Error;

        /**
         * Returns the error message of the <code>Throwable</code> in the chain
         * of <code>Throwable</code>s at the specified index, numbered from 0.
         * 
         * @param {number} index the index of the <code>Throwable</code> in the chain of
         * <code>Throwable</code>s
         * @return {string} the error message, or null if the <code>Throwable</code> at the
         * specified index in the chain does not contain a message
         * @throws IndexOutOfBoundsException if the <code>index</code> argument is
         * negative or not less than the count of <code>Throwable</code>s in the
         * chain
         */
        getMessage(index?: any): any;

        /**
         * Returns the error message of this and any nested <code>Throwable</code>s
         * in an array of Strings, one element for each message. Any
         * <code>Throwable</code> not containing a message is represented in the
         * array by a null. This has the effect of cause the length of the returned
         * array to be equal to the result of the {@link #getThrowableCount()}
         * operation.
         * 
         * @return {java.lang.String[]} the error messages
         */
        getMessages(): string[];

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
         */
        getThrowable(index: number): Error;

        /**
         * Returns the number of nested <code>Throwable</code>s represented by
         * this <code>Nestable</code>, including this <code>Nestable</code>.
         * 
         * @return {number} the throwable count
         */
        getThrowableCount(): number;

        /**
         * Returns this <code>Nestable</code> and any nested <code>Throwable</code>s
         * in an array of <code>Throwable</code>s, one element for each
         * <code>Throwable</code>.
         * 
         * @return {java.lang.Throwable[]} the <code>Throwable</code>s
         */
        getThrowables(): Error[];

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
         * 
         * @param {*} type  the type to find, subclasses match, null returns -1
         * @param {number} fromIndex the index, numbered from 0, of the starting position in
         * the chain to be searched
         * @return {number} index of the first occurrence of the type in the chain, or -1 if
         * the type is not found
         * @throws IndexOutOfBoundsException if the <code>fromIndex</code> argument
         * is negative or not less than the count of <code>Throwable</code>s in the
         * chain
         */
        indexOfThrowable(type?: any, fromIndex?: any): any;

        /**
         * Prints the stack trace of this exception to the specified print
         * writer.  Includes information from the exception, if any,
         * which caused this exception.
         * 
         * @param {java.io.PrintWriter} out <code>PrintWriter</code> to use for output.
         */
        printStackTrace(out?: any);

        /**
         * Prints the stack trace for this exception only--root cause not
         * included--using the provided writer.  Used by
         * {@link NestableDelegate} to write
         * individual stack traces to a buffer.  The implementation of
         * this method should call
         * <code>super.printStackTrace(out);</code> in most cases.
         * 
         * @param {java.io.PrintWriter} out The writer to use.
         */
        printPartialStackTrace(out: java.io.PrintWriter);
    }
}

