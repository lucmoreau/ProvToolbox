# Polyglot Embedding Demo with GraalVM 
Demonstration repository showing polyglot embedding with GraalVM using Maven and Gradle.
It contains a simple Main and Test class to bootstrap a polyglot embedding project.

For demonstration showing polyglot native embedding with GraalVM using Maven, see
the [Native embedding Maven project](native-embedding/README.md) located in the `native-embedding` subdirectory.

For more details on polyglot embedding please see the docs:
https://www.graalvm.org/latest/reference-manual/embed-languages/

## Setup

[Download](https://www.graalvm.org/downloads/) GraalVM and point the `JAVA_HOME` environment variable to it.

## Maven Usage

Download Maven or import as Maven project into your IDE.

* `mvn package` build using `javac`. Starting from GraalVM Polyglot API version 24.1.0, you can use `mvn -Pisolated package` to build with the native isolate version of a guest language. By default, only the native isolate library for the current platform is installed. To install native isolate libraries for all supported platforms, use `mvn -Pisolated -Disolated.all.platforms package`.
* `mvn test` to run the tests
* `mvn exec:exec` to run the application. Starting from GraalVM Polyglot API version 24.1.0, you can use `mvn -Pisolated exec:exec` to embed the native isolate version of a guest language.
* `mvn -Pnative package` to build a native-image
* `mvn -Passembly package` to build an uber JAR containing all dependencies using the Maven Assembly Plugin. The resulting JAR can be executed using `java -jar embedding-1.0-SNAPSHOT-jar-with-dependencies.jar`. We do recommend not using shading whenever possible.
* `mvn -Pshade package` to build an uber JAR containing all dependencies using the Maven Shade Plugin. The resulting JAR can be executed using `java -jar embedding-1.0-SNAPSHOT.jar`. We do recommend not using shading whenever possible.

Please see the [pom.xml](./pom.xml) file for further details on the configuration.

## Gradle Usage

Download Gradle or import as Maven project into your IDE.

* `gradle build` build using javac
* `gradle run` to run the Main application
* `gradle test` to run the tests
* `gradle nativeCompile` to build a native-image 
* `gradle nativeRun` to run the native image
  
Please see the [build.gradle.kts](./build.gradle.kts) file for further details on the configuration.