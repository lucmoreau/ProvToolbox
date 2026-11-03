# Toolbox
```
[INFO] org.openprovenance.prov:provconvert:jar:2.3.0-SNAPSHOT
[INFO] +- org.openprovenance.prov:prov-n:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-model:jar:2.3.0-SNAPSHOT:compile
[INFO] |  |  +- org.apache.commons:commons-lang3:jar:3.20.0:compile
[INFO] |  |  +- org.apache.commons:commons-collections4:jar:4.5.0:compile
[INFO] |  |  +- commons-io:commons-io:jar:2.21.0:compile
[INFO] |  |  \- jakarta.xml.bind:jakarta.xml.bind-api:jar:4.0.0:compile
[INFO] |  |     \- jakarta.activation:jakarta.activation-api:jar:2.1.0:compile
[INFO] |  +- org.antlr:antlr-runtime:jar:3.5.3:compile
[INFO] |  \- org.antlr:stringtemplate:jar:4.0.2:compile
[INFO] +- org.openprovenance.prov:prov-interop:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-jsonld:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- commons-cli:commons-cli:jar:1.11.0:compile
[INFO] |  \- jakarta.ws.rs:jakarta.ws.rs-api:jar:3.1.0:compile
[INFO] +- org.openprovenance.prov:prov-dot:jar:2.3.0-SNAPSHOT:compile
[INFO] +- org.openprovenance.prov:prov-template:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.22:compile
[INFO] |  +- tools.jackson.core:jackson-databind:jar:3.2.4:compile
[INFO] |  |  \- tools.jackson.core:jackson-core:jar:3.2.4:compile
[INFO] |  +- tools.jackson.dataformat:jackson-dataformat-yaml:jar:3.2.4:compile
[INFO] |  |  \- org.snakeyaml:snakeyaml-engine:jar:3.0.1:compile
[INFO] |  +- org.apache.commons:commons-text:jar:1.14.0:compile
[INFO] |  \- com.networknt:json-schema-validator:jar:3.0.8:compile
[INFO] |     \- com.ethlo.time:itu:jar:1.14.0:compile
[INFO] +- org.openprovenance.prov:prov-template-compiler:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- com.squareup:javapoet:jar:1.13.0:compile
[INFO] |  +- org.apache.maven:maven-model:jar:3.9.11:compile
[INFO] |  |  \- org.codehaus.plexus:plexus-utils:jar:3.6.0:compile
[INFO] |  +- org.apache.logging.log4j:log4j-slf4j-impl:jar:2.25.5:compile
[INFO] |  +- org.apache.commons:commons-csv:jar:1.14.1:compile
[INFO] |  |  \- commons-codec:commons-codec:jar:1.19.0:compile
[INFO] |  \- org.apache.jena:jena-core:jar:5.2.0:compile
[INFO] |     +- org.apache.jena:jena-base:jar:5.2.0:compile
[INFO] |     |  +- org.apache.commons:commons-compress:jar:1.27.1:compile
[INFO] |     |  +- com.github.ben-manes.caffeine:caffeine:jar:3.1.8:compile
[INFO] |     |  \- com.github.andrewoma.dexx:collection:jar:0.7:compile
[INFO] |     +- org.apache.jena:jena-iri:jar:5.2.0:compile
[INFO] |     \- org.roaringbitmap:RoaringBitmap:jar:1.3.0:compile
[INFO] +- org.openprovenance.prov:prov-template-library:jar:2.3.0-SNAPSHOT:compile
[INFO] |  \- org.openprovenance.prov:prov-template:zip:template-library:2.3.0-SNAPSHOT:compile
[INFO] +- org.openprovenance.prov:prov-generator:jar:2.3.0-SNAPSHOT:compile
[INFO] +- org.slf4j:slf4j-nop:jar:2.0.17:compile
[INFO] |  \- org.slf4j:slf4j-api:jar:2.0.17:compile
[INFO] +- org.codehaus.izpack:izpack-wrapper:jar:5.1.3:provided
[INFO] +- junit:junit:jar:4.13.2:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.25.5:compile
[INFO] \- org.apache.logging.log4j:log4j-api:jar:2.25.5:compile
```

# Service
```
[INFO] org.openprovenance.prov:service-translator:war:2.3.0-SNAPSHOT
[INFO] +- org.openprovenance.prov:prov-interop:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-jsonld:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-dot:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-n:jar:2.3.0-SNAPSHOT:compile
[INFO] |  |  +- org.antlr:antlr-runtime:jar:3.5.3:compile
[INFO] |  |  \- org.antlr:stringtemplate:jar:4.0.2:compile
[INFO] |  +- org.openprovenance.prov:prov-generator:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- commons-cli:commons-cli:jar:1.11.0:compile
[INFO] |  \- jakarta.ws.rs:jakarta.ws.rs-api:jar:3.1.0:compile
[INFO] +- org.openprovenance.prov:prov-template:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-model:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.22:compile
[INFO] |  +- tools.jackson.dataformat:jackson-dataformat-yaml:jar:3.2.4:compile
[INFO] |  |  \- org.snakeyaml:snakeyaml-engine:jar:3.0.1:compile
[INFO] |  +- org.apache.commons:commons-text:jar:1.14.0:compile
[INFO] |  \- com.networknt:json-schema-validator:jar:3.0.8:compile
[INFO] |     \- com.ethlo.time:itu:jar:1.14.0:compile
[INFO] +- org.openprovenance.prov:prov-service-core:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-log:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-storage-api:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-storage-filesystem:jar:2.3.0-SNAPSHOT:compile
[INFO] |  \- org.jboss.resteasy:resteasy-multipart-provider:jar:6.2.14.Final:compile
[INFO] |     +- org.jboss.resteasy:resteasy-jaxb-provider:jar:6.2.14.Final:compile
[INFO] |     |  +- org.glassfish.jaxb:codemodel:jar:4.0.6:compile
[INFO] |     |  +- org.glassfish.jaxb:jaxb-core:jar:4.0.6:compile
[INFO] |     |  +- org.glassfish.jaxb:jaxb-jxc:jar:4.0.6:compile
[INFO] |     |  +- org.glassfish.jaxb:jaxb-runtime:jar:4.0.6:compile
[INFO] |     |  +- org.glassfish.jaxb:txw2:jar:4.0.6:compile
[INFO] |     |  +- org.glassfish.jaxb:jaxb-xjc:jar:4.0.6:compile
[INFO] |     |  +- org.glassfish.jaxb:xsom:jar:4.0.6:compile
[INFO] |     |  +- com.sun.istack:istack-commons-runtime:jar:4.1.2:compile
[INFO] |     |  +- com.sun.istack:istack-commons-tools:jar:4.1.2:compile
[INFO] |     |  +- com.sun.xml.bind.external:relaxng-datatype:jar:4.0.6:compile
[INFO] |     |  \- com.sun.xml.bind.external:rngom:jar:4.0.6:compile
[INFO] |     +- jakarta.mail:jakarta.mail-api:jar:2.1.5:compile
[INFO] |     +- org.eclipse.angus:angus-mail:jar:2.0.5:compile
[INFO] |     +- org.apache.james:apache-mime4j-dom:jar:0.8.13:compile
[INFO] |     |  \- org.apache.james:apache-mime4j-core:jar:0.8.13:compile
[INFO] |     \- org.apache.james:apache-mime4j-storage:jar:0.8.13:compile
[INFO] +- org.openprovenance.prov:prov-service-translation:jar:2.3.0-SNAPSHOT:compile
[INFO] +- org.openprovenance.prov:prov-service-validation:jar:2.3.0-SNAPSHOT:compile
[INFO] |  \- org.openprovenance.prov:prov-validation:jar:2.3.0-SNAPSHOT:compile
[INFO] |     \- com.googlecode.matrix-toolkits-java:mtj:jar:0.9.14:compile
[INFO] |        \- com.googlecode.netlib-java:netlib-java:jar:0.9.3:compile
[INFO] |           \- net.sourceforge.f2j:arpack_combined_all:jar:0.1:compile
[INFO] +- org.openprovenance.prov:prov-storage-index-redis:jar:2.3.0-SNAPSHOT:compile
[INFO] |  \- redis.clients:jedis:jar:2.8.1:compile
[INFO] |     \- org.apache.commons:commons-pool2:jar:2.4.2:compile
[INFO] +- org.openprovenance.prov:prov-template-compiler:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- com.squareup:javapoet:jar:1.13.0:compile
[INFO] |  +- org.apache.maven:maven-model:jar:3.9.11:compile
[INFO] |  |  \- org.codehaus.plexus:plexus-utils:jar:3.6.0:compile
[INFO] |  +- org.apache.commons:commons-csv:jar:1.14.1:compile
[INFO] |  \- org.apache.jena:jena-core:jar:5.2.0:compile
[INFO] |     +- org.apache.jena:jena-base:jar:5.2.0:compile
[INFO] |     |  +- org.apache.commons:commons-compress:jar:1.27.1:compile
[INFO] |     |  +- com.github.ben-manes.caffeine:caffeine:jar:3.1.8:compile
[INFO] |     |  \- com.github.andrewoma.dexx:collection:jar:0.7:compile
[INFO] |     +- org.apache.jena:jena-iri:jar:5.2.0:compile
[INFO] |     \- org.roaringbitmap:RoaringBitmap:jar:1.3.0:compile
[INFO] +- org.openprovenance.prov:prov-storage-mongodb:jar:2.3.0-SNAPSHOT:compile
[INFO] |  \- org.mongojack:mongojack:jar:6.0.0:compile
[INFO] |     \- org.mongodb:mongodb-driver-sync:jar:5.6.2:compile
[INFO] |        +- org.mongodb:bson:jar:5.6.2:compile
[INFO] |        \- org.mongodb:mongodb-driver-core:jar:5.6.2:compile
[INFO] |           \- org.mongodb:bson-record-codec:jar:5.6.2:runtime
[INFO] +- org.openprovenance.prov:prov-template-library:jar:2.3.0-SNAPSHOT:compile
[INFO] |  \- org.openprovenance.prov:prov-template:zip:template-library:2.3.0-SNAPSHOT:compile
[INFO] +- org.apache.logging.log4j:log4j-slf4j-impl:jar:2.25.5:compile
[INFO] |  \- org.slf4j:slf4j-api:jar:1.7.36:compile
[INFO] +- org.jboss.resteasy:resteasy-servlet-initializer:jar:6.2.14.Final:compile
[INFO] |  +- org.jboss.resteasy:resteasy-core:jar:6.2.14.Final:compile
[INFO] |  |  +- jakarta.annotation:jakarta.annotation-api:jar:2.1.1:compile
[INFO] |  |  +- org.jboss:jandex:jar:2.4.5.Final:compile
[INFO] |  |  +- jakarta.activation:jakarta.activation-api:jar:2.1.4:compile
[INFO] |  |  +- org.eclipse.angus:angus-activation:jar:2.0.3:compile
[INFO] |  |  +- jakarta.validation:jakarta.validation-api:jar:3.0.2:compile
[INFO] |  |  \- com.ibm.async:asyncutil:jar:0.1.0:compile
[INFO] |  \- org.jboss.logging:jboss-logging:jar:3.5.3.Final:compile
[INFO] +- commons-io:commons-io:jar:2.21.0:compile
[INFO] +- org.quartz-scheduler:quartz:jar:2.5.1:compile
[INFO] |  \- jakarta.xml.bind:jakarta.xml.bind-api:jar:4.0.4:compile
[INFO] +- tools.jackson.core:jackson-databind:jar:3.2.4:compile
[INFO] |  \- tools.jackson.core:jackson-core:jar:3.2.4:compile
[INFO] +- jakarta.servlet:jakarta.servlet-api:jar:5.0.0:provided
[INFO] +- io.swagger.core.v3:swagger-rest:jar:3.0.0:compile
[INFO] |  +- io.github.classgraph:classgraph:jar:4.8.197:compile
[INFO] |  +- io.swagger.core.v3:swagger-models:jar:3.0.0:compile
[INFO] |  +- io.swagger.core.v3:swagger-annotations:jar:3.0.0:compile
[INFO] |  +- io.swagger.core.v3:swagger-integration:jar:3.0.0:compile
[INFO] |  |  \- io.swagger.core.v3:swagger-core:jar:3.0.0:compile
[INFO] |  +- tools.jackson.jakarta.rs:jackson-jakarta-rs-json-provider:jar:3.2.4:compile
[INFO] |  |  \- tools.jackson.jakarta.rs:jackson-jakarta-rs-base:jar:3.2.4:compile
[INFO] |  \- tools.jackson.module:jackson-module-jakarta-xmlbind-annotations:jar:3.2.4:compile
[INFO] +- org.webjars:swagger-ui:jar:5.6.1:compile
[INFO] +- org.webjars:jquery:jar:3.7.1:compile
[INFO] +- org.webjars:bootstrap-table:jar:1.16.0:compile
[INFO] +- org.webjars:bootstrap:jar:5.3.8:compile
[INFO] +- org.webjars.npm:bootstrap-icons:jar:1.13.1:compile
[INFO] +- org.webjars:font-awesome:jar:4.7.0:compile
[INFO] +- org.webjars.bowergithub.ajaxorg:ace-builds:jar:1.4.5:compile
[INFO] +- org.openprovenance.prov:prov-model:jar:tests:2.3.0-SNAPSHOT:test
[INFO] |  +- org.apache.commons:commons-lang3:jar:3.20.0:compile
[INFO] |  \- org.apache.commons:commons-collections4:jar:4.5.0:compile
[INFO] +- org.jboss.resteasy:resteasy-client:jar:6.2.14.Final:test
[INFO] |  +- org.jboss.resteasy:resteasy-client-api:jar:6.2.14.Final:test
[INFO] |  +- org.jboss.resteasy:resteasy-core-spi:jar:6.2.14.Final:compile
[INFO] |  +- org.apache.httpcomponents:httpclient:jar:4.5.14:test
[INFO] |  |  +- org.apache.httpcomponents:httpcore:jar:4.4.16:test
[INFO] |  |  \- commons-logging:commons-logging:jar:1.2:test
[INFO] |  +- commons-codec:commons-codec:jar:1.15:compile
[INFO] |  \- org.reactivestreams:reactive-streams:jar:1.0.4:compile
[INFO] +- org.eclipse.jetty:jetty-util:jar:11.0.26:compile
[INFO] +- org.eclipse.microprofile.config:microprofile-config-api:jar:3.0.3:compile
[INFO] +- junit:junit:jar:4.13.2:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.25.5:compile
[INFO] \- org.apache.logging.log4j:log4j-api:jar:2.25.5:compile
```

# prov-scala
```
[INFO] org.openprovenance.prov:prov-model-scala:jar:2.3.0-SNAPSHOT
[INFO] +- org.openprovenance.prov:prov-model:jar:2.3.0-SNAPSHOT:compile
[INFO] |  +- org.apache.commons:commons-lang3:jar:3.20.0:compile
[INFO] |  +- org.apache.commons:commons-collections4:jar:4.5.0:compile
[INFO] |  +- commons-io:commons-io:jar:2.21.0:compile
[INFO] |  \- jakarta.xml.bind:jakarta.xml.bind-api:jar:4.0.0:compile
[INFO] |     \- jakarta.activation:jakarta.activation-api:jar:2.1.0:compile
[INFO] +- org.openprovenance.prov:prov-jsonld:jar:2.3.0-SNAPSHOT:compile
[INFO] +- tools.jackson.core:jackson-databind:jar:3.2.4:compile
[INFO] |  \- tools.jackson.core:jackson-core:jar:3.2.4:compile
[INFO] +- com.fasterxml.jackson.core:jackson-annotations:jar:2.22:compile
[INFO] +- org.scala-lang:scala3-library_3:jar:3.9.0:compile
[INFO] |  \- org.scala-lang:scala-library:jar:3.9.0:compile
[INFO] +- org.scalatest:scalatest_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-core_3:jar:3.2.19:test
[INFO] |  |  +- org.scalactic:scalactic_3:jar:3.2.19:test
[INFO] |  |  +- org.scalatest:scalatest-compatible:jar:3.2.19:test
[INFO] |  |  \- org.scala-lang.modules:scala-xml_3:jar:2.1.0:test
[INFO] |  +- org.scalatest:scalatest-featurespec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-flatspec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-freespec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-funsuite_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-funspec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-propspec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-refspec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-wordspec_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-diagrams_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-matchers-core_3:jar:3.2.19:test
[INFO] |  +- org.scalatest:scalatest-shouldmatchers_3:jar:3.2.19:test
[INFO] |  \- org.scalatest:scalatest-mustmatchers_3:jar:3.2.19:test
[INFO] +- org.parboiled:parboiled_3:jar:2.5.1:compile
[INFO] +- org.openprovenance.prov:prov-model:jar:tests:2.3.0-SNAPSHOT:test
[INFO] +- junit:junit:jar:4.13.2:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.25.5:compile
[INFO] \- org.apache.logging.log4j:log4j-api:jar:2.25.5:compile
```
