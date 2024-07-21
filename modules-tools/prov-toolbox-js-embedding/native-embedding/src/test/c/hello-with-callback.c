#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <nativejavavm.h>

const char *get_string_chars(JNIEnv *env, jstring str) {
    jboolean isCopy;
    const char *chars = (*env)->GetStringUTFChars(env, str, &isCopy);
    CHECK_THROW()
    return chars;
}

void release_string_chars(JNIEnv *env, jstring str, const char *chars) {
    (*env)->ReleaseStringUTFChars(env, str, chars);
    CHECK_THROW()
}

jobject name_callback(JNIEnv *env, jobject arg) {
    char *name_chars = "Unknown";
    if (arg != NULL) {
        // The argument is of type org.graalvm.polyglot.Value, convert it to String by calling
        // the asString method on it
        jclass t_value = (*env)->FindClass(env, "org/graalvm/polyglot/Value");
        CHECK_THROW()
        jmethodID m_value_asString = (*env)->GetMethodID(env, t_value, "asString", "()Ljava/lang/String;");
        CHECK_THROW()
        jobject argString = (*env)->CallObjectMethodA(env, arg, m_value_asString, NULL);
        CHECK_THROW()
        if (argString != NULL) {
            const char *arg_chars = get_string_chars(env, argString);
            // In case the string is equal to "name" assign "Native Callback" to the variable
            // name_chars that will be used to create the value returned form this method
            if (strcmp(arg_chars, "name") == 0) {
                name_chars = "Native Callback";
            }
            release_string_chars(env, argString, arg_chars);
        }
    }
    // Create a jstring from name_chars and return it
    jstring name = (*env)->NewStringUTF(env, name_chars);
    CHECK_THROW()
    return name;
}

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
    jmethodID m_api_helloWithCallback = (*env)->GetStaticMethodID(env, t_api, "helloWithCallback", "(J)V");
    CHECK_THROW()

    // Create polyglot context
    (*env)->CallStaticObjectMethodA(env, t_api, m_api_setup, NULL);
    CHECK_THROW()

    // Call the "helloWithCallback" API method, pass the pointer to the callback function
    // as jlong to the API method.
    jvalue args[1] = { 0 };
    args[0].j = (jlong)(&name_callback); // j in jvalue is for jlong (long long int)
    (*env)->CallStaticObjectMethodA(env, t_api, m_api_helloWithCallback, args);
    CHECK_THROW()

    // Close polyglot context
    (*env)->CallStaticVoidMethodA(env, t_api, m_api_tearDown, args);
    CHECK_THROW()

    (*vm)->DestroyJavaVM(vm);
    return 0;
}
