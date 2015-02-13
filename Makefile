
install-sign:
	mvn clean install -P release

release-oss-sign:
	mvn release:prepare 
