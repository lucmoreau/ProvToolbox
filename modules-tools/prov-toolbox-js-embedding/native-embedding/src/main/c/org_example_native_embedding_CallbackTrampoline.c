#include <stdio.h>
#include <org_example_native_embedding_CallbackTrampoline.h>

JNIEXPORT jobject JNICALL Java_org_example_native_1embedding_CallbackTrampoline_jumpToCallback
  (JNIEnv *env, jclass clazz, jlong callback_ptr, jobject arg) {
    jobject (*callback)(JNIEnv *, jobject);
    callback = (jobject(*)(JNIEnv *, jobject)) callback_ptr;
    return callback(env, arg);
}
