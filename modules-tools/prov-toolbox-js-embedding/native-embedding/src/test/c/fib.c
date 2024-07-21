#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <nativejavavm.h>

int main(int argc, char **argv) {

    if (argc != 6) {
        printf("Expecting exactly 5 arguments with an index into the fibonacci sequence as the 5th argument!\n");
        exit(1);
    }

    struct javavm_and_env jvmenv = create_java_vm(argc, argv);
    JavaVM *vm = jvmenv.vm;
    JNIEnv *env = jvmenv.env;
    if (vm == NULL) {
        exit(1);
    }

    int n = atoi(argv[5]);

    // Lookup classes
    jclass t_api = (*env)->FindClass(env, "org/example/native_embedding/NativeAPI");
    CHECK_THROW()

    // Lookup methods
    jmethodID m_api_setup = (*env)->GetStaticMethodID(env, t_api, "setup", "()V");
    CHECK_THROW()
    jmethodID m_api_tearDown = (*env)->GetStaticMethodID(env, t_api, "tearDown", "()V");
    CHECK_THROW()
    jmethodID m_api_fibonacci = (*env)->GetStaticMethodID(env, t_api, "fibonacci", "(I)J");
    CHECK_THROW()

    // Create polyglot context
    (*env)->CallStaticObjectMethodA(env, t_api, m_api_setup, NULL);
    CHECK_THROW()

    // Call the "fibonacci" API method, pass n as integer to the API method,
    // the n-th fibonacci sequence element is returned as jlong.
    jvalue args[1] = { 0 };
    args[0].i = n; // i in jvalue is for int
    jlong fib = (*env)->CallStaticLongMethodA(env, t_api, m_api_fibonacci, args);
    CHECK_THROW()

    printf("Fibonacci number no. %d = %lld\n", n, (long long int) fib);

    // Close polyglot context
    (*env)->CallStaticVoidMethodA(env, t_api, m_api_tearDown, args);
    CHECK_THROW()

    (*vm)->DestroyJavaVM(vm);
    return 0;
}
