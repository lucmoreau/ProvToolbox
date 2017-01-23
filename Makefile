
### See http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin
### make dry
### make rel.clean release-oss-sign
### make release:perform


install-sign:
	mvn clean install -P release

release-oss-sign:
	mvn release:prepare
dry:
	mvn release:prepare -DdryRun=true

rel.clean:
	mvn release:clean

RELEASE_VERSION=ProvToolbox-0.7.3

release.doc:
	rm -r -f target/$(RELEASE_VERSION)
	mkdir -p target
	cd target; git clone git@github.com:lucmoreau/ProvToolbox.git  $(RELEASE_VERSION)
	cd target/$(RELEASE_VERSION); git checkout tags/$(RELEASE_VERSION); mvn site-deploy
	echo "rename scp://openprovenance@websites1.ecs.soton.ac.uk:/home/openprovenance/openprovenance.org/htdocs/java/site/prov to version number"
	cd target/$(RELEASE_VERSION); scp toolbox/target/provconvert-0.7.3.dmg openprovenance@websites1.ecs.soton.ac.uk:/home/openprovenance/openprovenance.org/htdocs/java/installer




yum:
	sudo yum install -y repolist disabled toolbox/target/rpm/provconvert/RPMS/noarch/provconvert-*.noarch.rpm

PROVCONVERT=toolbox/target/appassembler/bin/provconvert

SRC_PROVNS=$(wildcard prov-n/target/*.provn)
PROVNS=$(basename $(notdir $(SRC_PROVNS)))

TEST_DIRS=$(addprefix target/testcases/test-, $(PROVNS))
TEST_PROVNS=$(addsuffix .provn, $(join $(addsuffix /, $(TEST_DIRS)) , $(PROVNS)))
TEST_JSONS=$(addsuffix .json, $(join $(addsuffix /, $(TEST_DIRS)) , $(PROVNS)))
TEST_TTLS=$(addsuffix .ttl, $(join $(addsuffix /, $(TEST_DIRS)) , $(PROVNS)))
TEST_TRIGS=$(addsuffix .trig, $(join $(addsuffix /, $(TEST_DIRS)) , $(PROVNS)))
TEST_PROVXS=$(addsuffix .provx, $(join $(addsuffix /, $(TEST_DIRS)) , $(PROVNS)))


.provn.json:
	provconvert -infile $< -outfile $<.json

$(TEST_JSONS): %.json: %.provn
	-$(PROVCONVERT) -infile $< -outfile $@
$(TEST_TTLS): %.ttl: %.provn
	-$(PROVCONVERT) -infile $< -outfile $@
$(TEST_TRIGS): %.trig: %.provn
	-$(PROVCONVERT) -infile $< -outfile $@
$(TEST_PROVXS): %.provx: %.provn
	-$(PROVCONVERT) -infile $< -outfile $@

$(TEST_PROVNS):
	cp prov-n/target/$(notdir $@) $@


testcases:
	rm -r -f target/testcases
	mkdir -p target/testcases
	mkdir $(TEST_DIRS)
	$(MAKE) testcases.files.json
	$(MAKE) testcases.files.ttl
	$(MAKE) testcases.files.trig
	$(MAKE) testcases.files.provx
	tar zcvf target/ssi-testcases.tar.gz target/testcases

testcases.files.json: $(TEST_JSONS)

testcases.files.ttl: $(TEST_TTLS)

testcases.files.trig: $(TEST_TRIGS)

testcases.files.provx: $(TEST_PROVXS)


