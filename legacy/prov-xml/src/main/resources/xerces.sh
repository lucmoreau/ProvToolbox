#!/bin/sh

CLASSPATH=$CLASSPATH:/usr/local/java/xerces-2_11_0-xml-schema-1.1-beta/xercesImpl.jar:/usr/local/java/xerces-2_11_0-xml-schema-1.1-beta/xercesSamples.jar:/usr/local/java/xerces-2_11_0-xml-schema-1.1-beta/org.eclipse.wst.xml.xpath2.processor_1.1.0.jar:/usr/local/java/xerces-2_11_0-xml-schema-1.1-beta/resolver.jar:/usr/local/java/xerces-2_11_0-xml-schema-1.1-beta/serializer.jar:/usr/local/java/xerces-2_11_0-xml-schema-1.1-beta/xml-apis.jar

export CLASSPATH

echo "java jaxp.SourceValidator -xsd11 $*"
java jaxp.SourceValidator -xsd11 $*



