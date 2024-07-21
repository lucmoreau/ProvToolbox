package org.example.native_embedding;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;

public class NativeAPI {
    private static Context context;

    /**
     * N-th fibonacci sequence element function JS source.
     */
    private static final Source FIB = Source.create("js", """
            function fibonacci(n) {
              if (n == 1 || n == 2) {
                return 1;
              }
              var a = 1;
              var b = 1;
              var c = 0;
              for (var i = 2; i < n; i++) {
                c = a + b;
                a = b;
                b = c;
              }
              return c;
            }
            """);


    private static Value fib;

    /**
     * API method - Create static polyglot context.
     */
    public static void setup() {
        context = Context.newBuilder().allowAllAccess(true).build();
    }

    /**
     * API method - Close static polyglot context and also null out the cached fibonacci function.
     */
    public static void tearDown() {
        if (context != null) {
            fib = null;
            context.close();
        }
    }

    /**
     * API method - Compute n-th element of the fibonacci sequence.
     *
     * @param n n
     * @return n-th element of the fibonacci sequence
     */
    public static long fibonacci(int n) {
        if (fib == null) {
            context.eval(FIB);
            fib = context.getBindings("js").getMember("fibonacci");
        }
        return fib.execute(n).asLong();
    }


    /**
     * API method - Prints "Hello from " plus the string returned by the native callback.
     *
     * @param nameCallbackFunctionPointer pointer to the native callback that accepts Value as argument and returns
     *                                    String.
     */
    public static void helloWithCallback(long nameCallbackFunctionPointer) {
        // Callback executable that uses helper library accessed by JNI that invokes the native callback at the
        // specified pointer.
        ProxyExecutable nameCallback = (args) -> CallbackTrampoline.jumpToCallback(nameCallbackFunctionPointer,
                args[0]);
        context.getBindings("js").putMember("nameCallback", nameCallback);
        context.eval("js", "print('Hello from ' + nameCallback('name'))");
    }

    /**
     * API method - Eval JS code passed as String.
     *
     * @param s JS code to evaluate.
     */
    public static void eval(String s) {
        context.eval("js", s);
    }
}
