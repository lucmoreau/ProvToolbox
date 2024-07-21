package org.example.native_embedding;

import org.graalvm.polyglot.Value;

public class CallbackTrampoline {
    static {
        System.loadLibrary("CallbackTrampoline");
    }

    /**
     * Call the native jumpToCallback method in the CallbackTrampoline helper library
     * that invokes the native callback at the address specified by the callbackPointer
     * param.
     *
     * @param callbackPointer address of the native callback to call
     * @param arg argument to pass to the native callback
     * @return value returned from the native callback
     */
    static native Object jumpToCallback(long callbackPointer, Value arg);
}
