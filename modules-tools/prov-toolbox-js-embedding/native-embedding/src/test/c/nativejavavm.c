#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <nativejavavm.h>

char *getOptionString(char *optionParam, char *optionValue) {
    const int optionStringLength = strnlen(optionParam, 8191) + strnlen(optionValue, 8191);
    char *optionString = calloc(optionStringLength + 1, sizeof(char));
    strncpy(optionString, optionParam, optionStringLength);
    strncat(optionString, optionValue, strnlen(optionValue, 8191));
    return optionString;
}

struct javavm_and_env create_java_vm(int argc, char **argv) {
    JavaVM *vm = NULL;
    JNIEnv *env = NULL;
    struct javavm_and_env jvmenv = { 0 };

    if (argc < 5) {
        printf("Expecting at least 4 arguments starting with: Java Module Path, Main Module, Java Home, and Java Library Path!\n");
        return jvmenv;
    }

    printf("Java Module Path: %s\n", argv[1]);
    printf("Main Module: %s\n", argv[2]);
    printf("Java Home: %s\n", argv[3]);
    printf("Java Library Path: %s\n", argv[4]);
    fflush(stdout);

    // Create Java VM through JNI. Works both with the JVM library and the library built by Native Image.
    // For the JVM library the module related parameters and the java.home parameter are necessary.
    // For the library built by Native Image, these parameters are redundant, because no module loading occurs
    // on SVM used by the Native Image, everything is already built into the library.
    // The library built by Native Image also supports multiple VMs per process, which is
    // not possible with the JVM library.

    JavaVMInitArgs vm_args;
    JavaVMOption options[4] = { 0 };
    char *optionModulePath = getOptionString("--module-path=", argv[1]);
    options[0].optionString = optionModulePath;
    char *optionMainModule = getOptionString("-Djdk.module.main=", argv[2]);
    options[1].optionString = optionMainModule;
    char *optionJavaHome = getOptionString("-Djava.home=", argv[3]);
    options[2].optionString = optionJavaHome;
    char *optionJavaLibraryPath = getOptionString("-Djava.library.path=", argv[4]);
    options[3].optionString = optionJavaLibraryPath;

    vm_args.version = JNI_VERSION_10;
    vm_args.nOptions = 4;
    vm_args.options = options;
    vm_args.ignoreUnrecognized = false;

    int res = JNI_CreateJavaVM(&vm, (void **) (&env), &vm_args);
    if (res != JNI_OK) {
        printf("Cannot create Java VM\n");
        return jvmenv;
    }

    free(optionModulePath);
    free(optionMainModule);
    free(optionJavaHome);
    free(optionJavaLibraryPath);

    jvmenv.vm = vm;
    jvmenv.env = env;
    return jvmenv;
}
