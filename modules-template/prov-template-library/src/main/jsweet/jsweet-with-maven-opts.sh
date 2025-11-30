#!/bin/bash
echo "Setting MAVEN_OPTS for JSweet compilation"
export MAVEN_OPTS="--add-opens jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED  --add-opens jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED  --add-opens jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED  --add-opens jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED --add-opens jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-opens jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED  --add-opens jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED"
mvn -f ${project.build.directory}/jsweet/pom-jsweet.xml -Dlibrary.source=${project.build.directory}/generated-sources/${package.name.cli} jsweet:jsweet
