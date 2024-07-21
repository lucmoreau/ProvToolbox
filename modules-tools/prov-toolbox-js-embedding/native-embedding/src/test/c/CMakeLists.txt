cmake_minimum_required(VERSION 3.22)
project(native-embedding-test)
set(CMAKE_C_STANDARD 11)
set(CMAKE_CONFIGURATION_TYPES Release)
set(CMAKE_BUILD_TYPE Release)
if(NOT WIN32)
    set(C_FLAGS "-fPIC -g")
    set(CMAKE_C_FLAGS ${C_FLAGS})
endif()

# Fix Windows paths.
STRING(REGEX REPLACE "\\\\" "/" PROJECT_MAIN_DIR_FIXED $ENV{PROJECT_MAIN_DIR})
STRING(REGEX REPLACE "\\\\" "/" JAVA_HOME_FIXED $ENV{JAVA_HOME})
STRING(REGEX REPLACE "\\\\" "/" LIB_API_PATH_FIXED $ENV{LIB_API_PATH})

# Set paths.
set(HELPER_LIB_SRC_DIR ${PROJECT_MAIN_DIR_FIXED}/src/main/c)
set(TEST_SRC_DIR ${PROJECT_MAIN_DIR_FIXED}/src/test/c)
set(LIB_API_PATH ${LIB_API_PATH_FIXED})
set(JNI_INCLUDE_DIR ${JAVA_HOME_FIXED}/include)
set(JNI_PLATFORM_INCLUDE_DIR ${JAVA_HOME_FIXED}/include/$ENV{PLATFORM_NAME})

# Set API library name. The API library contains a JNI compatible Java VM implementation that exposes the API methods defined in the NativeAPI class.
set(APILIB $ENV{LIB_API_NAME})

# Add CallbackTrampoline helper library that contains the implementation of the native method in the CallbackTrampoline class.
add_library(CallbackTrampoline SHARED ${HELPER_LIB_SRC_DIR}/org_example_native_embedding_CallbackTrampoline.c)
target_include_directories(CallbackTrampoline PRIVATE ${JNI_INCLUDE_DIR})
target_include_directories(CallbackTrampoline PRIVATE ${JNI_PLATFORM_INCLUDE_DIR})
target_include_directories(CallbackTrampoline PRIVATE ${HELPER_LIB_SRC_DIR})

# Add nativejavavm static library used to call into the API library to create a Java VM via JNI.
add_library(nativejavavm STATIC ${TEST_SRC_DIR}/nativejavavm.c)
target_include_directories(nativejavavm PRIVATE ${JNI_INCLUDE_DIR})
target_include_directories(nativejavavm PRIVATE ${JNI_PLATFORM_INCLUDE_DIR})
target_include_directories(nativejavavm PRIVATE ${TEST_SRC_DIR})
target_link_libraries(nativejavavm ${APILIB})

# Add hello-with-callback executable that executes a simple JavaScript code with a callback.
add_executable(hello-with-callback ${TEST_SRC_DIR}/hello-with-callback.c)
target_include_directories(hello-with-callback PRIVATE ${JNI_INCLUDE_DIR})
target_include_directories(hello-with-callback PRIVATE ${JNI_PLATFORM_INCLUDE_DIR})
target_include_directories(hello-with-callback PRIVATE ${TEST_SRC_DIR})
target_link_directories(hello-with-callback PRIVATE ${LIB_API_PATH})
target_link_libraries(hello-with-callback ${APILIB})
target_link_libraries(hello-with-callback CallbackTrampoline)
target_link_libraries(hello-with-callback nativejavavm)

# Add fib executable that executes a JavaScript code that computes n-th element of the Fibonacci sequence.
add_executable(fib ${TEST_SRC_DIR}/fib.c)
target_include_directories(fib PRIVATE ${JNI_INCLUDE_DIR})
target_include_directories(fib PRIVATE ${JNI_PLATFORM_INCLUDE_DIR})
target_include_directories(fib PRIVATE ${TEST_SRC_DIR})
target_link_directories(fib PRIVATE ${LIB_API_PATH})
target_link_libraries(fib ${APILIB})
target_link_libraries(fib nativejavavm)

# Add evaljs executable that evaluates passed JavaScript source.
add_executable(evaljs ${TEST_SRC_DIR}/evaljs.c)
target_include_directories(evaljs PRIVATE ${JNI_INCLUDE_DIR})
target_include_directories(evaljs PRIVATE ${JNI_PLATFORM_INCLUDE_DIR})
target_include_directories(evaljs PRIVATE ${TEST_SRC_DIR})
target_link_directories(evaljs PRIVATE ${LIB_API_PATH})
target_link_libraries(evaljs ${APILIB})
target_link_libraries(evaljs nativejavavm)



