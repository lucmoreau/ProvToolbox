

test.node:
	npm test


test.py:
	export PYTHONPATH=target/generated-python:src/main/python; python3 src/test/python/test.py


compile.plead.templates:
	 mvn exec:exec@compile-template-plead

transpile.code:
	 export JAVA_HOME=/usr/local/java/jdk-12.0.1.jdk/Contents/Home/; mvn -f pom12.xml jsweet:jsweet

