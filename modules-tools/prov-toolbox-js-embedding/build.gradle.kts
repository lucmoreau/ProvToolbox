import java.nio.file.*

plugins {
    id("java")
    id("application")
    id("org.graalvm.buildtools.native") version "0.10.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

group = "org.example"
version = "1.0-SNAPSHOT"
description = "embedding"

var graalVMVersion: String = "24.0.1";
dependencies {
    implementation("org.graalvm.polyglot:polyglot:$graalVMVersion")
    implementation("org.graalvm.polyglot:js:$graalVMVersion")
    testImplementation("junit:junit:4.13.2")
}

val isGraalVM = Files.exists(Paths.get("${System.getProperty("java.home")}/lib/graalvm"))

val jvmDefaultArgs: List<String> = if (isGraalVM) {
    /*
     * No JVM arguments needed when running with a GraalVM JDK
     * JVMCI is already enabled and it comes with the Graal compiler preconfigured
     */
   listOf<String>()
} else {
    /*
     * Path used when running on a different JDK than GraalVM.
     * This profile may be removed if you are always running with a
     * GraalVM JDK or if you don't want to use the optimizing runtime.
     *
     * Note: Using this path unlocks experimental options and is therefore
     * not recommended for production use.
     */
    configurations {
        create("compilerClasspath") {
            isCanBeResolved = true
        }
    }
    dependencies {
        "compilerClasspath"("org.graalvm.compiler:compiler:$graalVMVersion")
    }
    val compilerDependencies = configurations.getByName("compilerClasspath").filter { it.name.endsWith(".jar") }  // Filter out POMs
    listOf("-XX:+UnlockExperimentalVMOptions", "-XX:+EnableJVMCI", "--upgrade-module-path=${compilerDependencies.asPath}")
}

application {
    mainModule.set("embedding")
    mainClass.set("org.example.embedding.Main")
    applicationDefaultJvmArgs = jvmDefaultArgs
}

tasks.withType<Test> {
    useJUnit()
    jvmArgs(jvmDefaultArgs)
}
