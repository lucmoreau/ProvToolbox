#include <jni.h>

#ifndef CHECK_THROW
#define CHECK_THROW()                                                                                                                                \
    if ((*env)->ExceptionCheck(env)) {                                                                                                               \
        printf("JNI error at %s:%d\n", __FILE__, __LINE__);                                                                                          \
        exit(1);                                                                                                                                     \
    }
#endif

#ifndef _Included_org_example_native_embedding_CallbackTrampoline
#define _Included_org_example_native_embedding_CallbackTrampoline
#ifdef __cplusplus
extern "C" {
#endif

struct javavm_and_env {
    JavaVM *vm;
    JNIEnv *env;
};

struct javavm_and_env create_java_vm(int, char **);

#ifdef __cplusplus
}
#endif
#endif
