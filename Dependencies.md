# Toolbox
```
[INFO] org.openprovenance.prov:provconvert:jar:2.0.0-SNAPSHOT
[INFO] +- org.openprovenance.prov:prov-n:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-model:jar:2.0.0-SNAPSHOT:compile
[INFO] |  |  +- org.apache.commons:commons-lang3:jar:3.9:compile
[INFO] |  |  +- jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.2:compile
[INFO] |  |  |  \- jakarta.activation:jakarta.activation-api:jar:1.2.1:compile
[INFO] |  |  +- org.apache.commons:commons-collections4:jar:4.4:compile
[INFO] |  |  \- commons-io:commons-io:jar:2.8.0:compile
[INFO] |  +- org.antlr:antlr-runtime:jar:3.4:compile
[INFO] |  |  \- antlr:antlr:jar:2.7.7:compile
[INFO] |  \- org.antlr:stringtemplate:jar:4.0.2:compile
[INFO] +- org.openprovenance.prov:prov-interop:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-jsonld:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-jsonld-xml:jar:2.0.0-SNAPSHOT:compile
[INFO] |  |  \- com.fasterxml.jackson.dataformat:jackson-dataformat-xml:jar:2.14.2:compile
[INFO] |  |     +- org.codehaus.woodstox:stax2-api:jar:4.2.1:compile
[INFO] |  |     \- com.fasterxml.woodstox:woodstox-core:jar:6.5.0:compile
[INFO] |  +- commons-cli:commons-cli:jar:1.5.0:compile
[INFO] |  \- jakarta.ws.rs:jakarta.ws.rs-api:jar:3.1.0:compile
[INFO] +- org.openprovenance.prov:prov-dot:jar:2.0.0-SNAPSHOT:compile
[INFO] +- org.openprovenance.prov:prov-template:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.14.2:compile
[INFO] |  \- com.fasterxml.jackson.core:jackson-databind:jar:2.14.2:compile
[INFO] |     \- com.fasterxml.jackson.core:jackson-core:jar:2.14.2:compile
[INFO] +- org.openprovenance.prov:prov-template-compiler:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- com.squareup:javapoet:jar:1.13.0:compile
[INFO] |  +- org.apache.maven:maven-model:jar:3.6.1:compile
[INFO] |  |  \- org.codehaus.plexus:plexus-utils:jar:3.2.0:compile
[INFO] |  +- org.apache.commons:commons-text:jar:1.10.0:compile
[INFO] |  +- com.networknt:json-schema-validator:jar:1.0.52:compile
[INFO] |  +- org.apache.logging.log4j:log4j-slf4j-impl:jar:2.17.1:compile
[INFO] |  \- org.apache.commons:commons-csv:jar:1.10.0:compile
[INFO] +- org.openprovenance.prov:prov-generator:jar:2.0.0-SNAPSHOT:compile
[INFO] +- org.slf4j:slf4j-nop:jar:2.0.7:compile
[INFO] |  \- org.slf4j:slf4j-api:jar:2.0.7:compile
[INFO] +- org.codehaus.izpack:izpack-wrapper:jar:5.1.3:provided
[INFO] +- junit:junit:jar:4.13.2:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.17.1:compile
[INFO] \- org.apache.logging.log4j:log4j-api:jar:2.17.1:compile
```

# Service
```
[INFO] org.openprovenance.prov:service-translator:war:2.0.0-SNAPSHOT
[INFO] +- org.openprovenance.prov:prov-interop-light:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-interop:jar:2.0.0-SNAPSHOT:compile
[INFO] |  |  \- commons-cli:commons-cli:jar:1.5.0:compile
[INFO] |  +- org.openprovenance.prov:prov-jsonld:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-jsonld-xml:jar:2.0.0-SNAPSHOT:compile
[INFO] |  |  \- com.fasterxml.jackson.dataformat:jackson-dataformat-xml:jar:2.14.2:compile
[INFO] |  |     +- org.codehaus.woodstox:stax2-api:jar:4.2.1:compile
[INFO] |  |     \- com.fasterxml.woodstox:woodstox-core:jar:6.5.0:compile
[INFO] |  +- org.openprovenance.prov:prov-dot:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-template:jar:2.0.0-SNAPSHOT:compile
[INFO] |  |  \- com.fasterxml.jackson.core:jackson-annotations:jar:2.14.2:compile
[INFO] |  +- org.openprovenance.prov:prov-n:jar:2.0.0-SNAPSHOT:compile
[INFO] |  |  +- org.antlr:antlr-runtime:jar:3.4:compile
[INFO] |  |  |  \- antlr:antlr:jar:2.7.7:compile
[INFO] |  |  \- org.antlr:stringtemplate:jar:4.0.2:compile
[INFO] |  \- org.openprovenance.prov:prov-generator:jar:2.0.0-SNAPSHOT:compile
[INFO] +- org.openprovenance.prov:prov-service-core:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-model:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-log:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-storage-api:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.openprovenance.prov:prov-storage-filesystem:jar:2.0.0-SNAPSHOT:compile
[INFO] |  +- org.jboss.resteasy:resteasy-multipart-provider:jar:6.2.5.Final:compile
[INFO] |  |  +- org.jboss.resteasy:resteasy-jaxb-provider:jar:6.2.5.Final:compile
[INFO] |  |  |  +- org.glassfish.jaxb:codemodel:jar:4.0.3:compile
[INFO] |  |  |  +- org.glassfish.jaxb:jaxb-core:jar:4.0.3:compile
[INFO] |  |  |  +- org.glassfish.jaxb:jaxb-jxc:jar:4.0.3:compile
[INFO] |  |  |  +- org.glassfish.jaxb:jaxb-runtime:jar:4.0.3:compile
[INFO] |  |  |  +- org.glassfish.jaxb:txw2:jar:4.0.3:compile
[INFO] |  |  |  +- org.glassfish.jaxb:jaxb-xjc:jar:4.0.3:compile
[INFO] |  |  |  +- org.glassfish.jaxb:xsom:jar:4.0.3:compile
[INFO] |  |  |  +- com.sun.istack:istack-commons-runtime:jar:4.1.2:compile
[INFO] |  |  |  +- com.sun.istack:istack-commons-tools:jar:4.1.2:compile
[INFO] |  |  |  +- com.sun.xml.bind.external:relaxng-datatype:jar:4.0.3:compile
[INFO] |  |  |  \- com.sun.xml.bind.external:rngom:jar:4.0.3:compile
[INFO] |  |  +- jakarta.mail:jakarta.mail-api:jar:2.1.2:compile
[INFO] |  |  +- org.eclipse.angus:angus-mail:jar:1.0.0:compile
[INFO] |  |  +- org.apache.james:apache-mime4j-dom:jar:0.8.9:compile
[INFO] |  |  |  \- org.apache.james:apache-mime4j-core:jar:0.8.9:compile
[INFO] |  |  \- org.apache.james:apache-mime4j-storage:jar:0.8.9:compile
[INFO] |  \- com.fasterxml.jackson.core:jackson-databind:jar:2.14.2:compile
[INFO] |     \- com.fasterxml.jackson.core:jackson-core:jar:2.14.2:compile
[INFO] +- org.openprovenance.prov:prov-service-translation:jar:2.0.0-SNAPSHOT:compile
[INFO] +- org.openprovenance.prov:prov-storage-index-redis:jar:2.0.0-SNAPSHOT:compile
[INFO] |  \- redis.clients:jedis:jar:2.8.1:compile
[INFO] |     \- org.apache.commons:commons-pool2:jar:2.4.2:compile
[INFO] +- org.openprovenance.prov:prov-model:jar:tests:2.0.0-SNAPSHOT:test
[INFO] |  +- org.apache.commons:commons-lang3:jar:3.9:compile
[INFO] |  +- jakarta.xml.bind:jakarta.xml.bind-api:jar:2.3.2:compile
[INFO] |  |  \- jakarta.activation:jakarta.activation-api:jar:1.2.1:compile
[INFO] |  \- org.apache.commons:commons-collections4:jar:4.4:compile
[INFO] +- javax.json:javax.json-api:jar:1.1.2:compile
[INFO] +- org.apache.logging.log4j:log4j-slf4j-impl:jar:2.17.1:compile
[INFO] |  \- org.slf4j:slf4j-api:jar:1.7.25:compile
[INFO] +- org.jboss.resteasy:resteasy-servlet-initializer:jar:6.2.5.Final:compile
[INFO] |  +- org.jboss.resteasy:resteasy-core:jar:6.2.5.Final:compile
[INFO] |  |  +- jakarta.annotation:jakarta.annotation-api:jar:2.1.1:compile
[INFO] |  |  +- org.jboss:jandex:jar:2.4.3.Final:compile
[INFO] |  |  +- org.eclipse.angus:angus-activation:jar:1.0.0:compile
[INFO] |  |  +- jakarta.validation:jakarta.validation-api:jar:3.0.2:compile
[INFO] |  |  \- com.ibm.async:asyncutil:jar:0.1.0:compile
[INFO] |  +- jakarta.ws.rs:jakarta.ws.rs-api:jar:3.1.0:compile
[INFO] |  \- org.jboss.logging:jboss-logging:jar:3.5.3.Final:compile
[INFO] +- org.jboss.resteasy:resteasy-client:jar:6.2.5.Final:test
[INFO] |  +- org.jboss.resteasy:resteasy-client-api:jar:6.2.5.Final:test
[INFO] |  +- org.jboss.resteasy:resteasy-core-spi:jar:6.2.5.Final:compile
[INFO] |  +- commons-codec:commons-codec:jar:1.15:test
[INFO] |  \- org.reactivestreams:reactive-streams:jar:1.0.4:compile
[INFO] +- commons-io:commons-io:jar:2.8.0:compile
[INFO] +- org.quartz-scheduler:quartz:jar:2.3.2:compile
[INFO] |  +- com.mchange:c3p0:jar:0.9.5.4:compile
[INFO] |  +- com.mchange:mchange-commons-java:jar:0.2.15:compile
[INFO] |  \- com.zaxxer:HikariCP-java7:jar:2.4.13:compile
[INFO] +- com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:jar:2.14.2:compile
[INFO] |  +- com.fasterxml.jackson.jaxrs:jackson-jaxrs-base:jar:2.14.2:compile
[INFO] |  \- com.fasterxml.jackson.module:jackson-module-jaxb-annotations:jar:2.14.2:compile
[INFO] +- jakarta.servlet:jakarta.servlet-api:jar:5.0.0:provided
[INFO] +- io.swagger.core.v3:swagger-jaxrs2:jar:2.2.15:compile
[INFO] |  +- io.github.classgraph:classgraph:jar:4.8.154:compile
[INFO] |  +- org.javassist:javassist:jar:3.29.2-GA:compile
[INFO] |  +- io.swagger.core.v3:swagger-models:jar:2.2.15:compile
[INFO] |  +- org.yaml:snakeyaml:jar:2.0:compile
[INFO] |  +- io.swagger.core.v3:swagger-annotations:jar:2.2.15:compile
[INFO] |  \- io.swagger.core.v3:swagger-integration:jar:2.2.15:compile
[INFO] |     \- io.swagger.core.v3:swagger-core:jar:2.2.15:compile
[INFO] |        +- com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:jar:2.15.1:compile
[INFO] |        \- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:jar:2.15.1:compile
[INFO] +- org.webjars:swagger-ui:jar:4.1.3:compile
[INFO] +- org.webjars:jquery:jar:3.6.2:compile
[INFO] +- org.webjars:bootstrap-table:jar:1.9.1-1:compile
[INFO] +- org.webjars:bootstrap:jar:3.4.0:compile
[INFO] +- org.webjars:font-awesome:jar:4.7.0:compile
[INFO] +- org.webjars.bowergithub.ajaxorg:ace-builds:jar:1.4.5:compile
[INFO] +- org.apache.httpcomponents:httpclient:jar:4.5.3:test
[INFO] |  +- org.apache.httpcomponents:httpcore:jar:4.4.6:test
[INFO] |  \- commons-logging:commons-logging:jar:1.2:test
[INFO] +- org.eclipse.jetty:jetty-util:jar:11.0.16:compile
[INFO] +- org.eclipse.microprofile.config:microprofile-config-api:jar:3.0.3:compile
[INFO] +- junit:junit:jar:4.13.2:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.17.1:compile
[INFO] \- org.apache.logging.log4j:log4j-api:jar:2.17.1:compile
```
