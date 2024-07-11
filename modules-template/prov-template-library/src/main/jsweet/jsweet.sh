#!/bin/bash
export JAVA_HOME="${java12.home}"

mvn -f ${project.build.directory}/jsweet/pom-jsweet.xml -Dlibrary.source=${project.build.directory}/generated-sources/${package.name.cli} jsweet:jsweet
