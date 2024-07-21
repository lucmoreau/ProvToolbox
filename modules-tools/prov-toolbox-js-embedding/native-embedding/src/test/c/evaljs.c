#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <nativejavavm.h>

int main(int argc, char **argv) {

    struct javavm_and_env jvmenv = create_java_vm(argc, argv);
    JavaVM *vm = jvmenv.vm;
    JNIEnv *env = jvmenv.env;
    if (vm == NULL) {
        exit(1);
    }

    // Lookup classes
    jclass t_api = (*env)->FindClass(env, "org/example/native_embedding/NativeAPI");
    CHECK_THROW()

    // Lookup methods
    jmethodID m_api_setup = (*env)->GetStaticMethodID(env, t_api, "setup", "()V");
    CHECK_THROW()
    jmethodID m_api_tearDown = (*env)->GetStaticMethodID(env, t_api, "tearDown", "()V");
    CHECK_THROW()
    jmethodID m_api_eval = (*env)->GetStaticMethodID(env, t_api, "eval", "(Ljava/lang/String;)V");
    CHECK_THROW()

    // Create polyglot context
    (*env)->CallStaticObjectMethodA(env, t_api, m_api_setup, NULL);
    CHECK_THROW()

    // Call the "eval" API method, pass a JS source that prints "Hello from JS" to the API method.
    // The JS source is passed as jstring (jobject) obtained by the NewStringUTF JNI method.
    jvalue args[1] = { 0 };
    jstring jsSource = (*env)->NewStringUTF(env, "print('Hello from JS')");
    args[0].l = jsSource; // l in jvalue is for jobject
    (*env)->CallStaticVoidMethodA(env, t_api, m_api_eval, args);
    CHECK_THROW()

    // Close polyglot context
    (*env)->CallStaticVoidMethodA(env, t_api, m_api_tearDown, args);
    CHECK_THROW()

    (*vm)->DestroyJavaVM(vm);
    return 0;
}
