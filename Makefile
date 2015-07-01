
install-sign:
	mvn clean install -P release

release-oss-sign:
	mvn release:prepare 

yum:
	sudo yum install -y repolist disabled toolbox/target/rpm/toolbox/RPMS/noarch/toolbox-*.noarch.rpm
