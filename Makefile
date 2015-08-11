
install-sign:
	mvn clean install -P release

release-oss-sign:
	mvn release:prepare 

yum:
	sudo yum install -y repolist disabled toolbox/target/rpm/toolbox/RPMS/noarch/toolbox-*.noarch.rpm

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
	$(PROVCONVERT) -infile $< -outfile $@
$(TEST_TTLS): %.ttl: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
$(TEST_TRIGS): %.trig: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
$(TEST_PROX): %.provx: %.provn
	$(PROVCONVERT) -infile $< -outfile $@

$(TEST_PROVNS):
	cp prov-n/target/$(notdir $@) $@


testcases:
	rm -r -f target/testcases;
	mkdir target/testcases
	mkdir $(TEST_DIRS)
	$(MAKE) testcases.files.json

testcases.files.json: $(TEST_JSONS)

testcases.files.ttl: $(TEST_TTLS)

testcases.files.trig: $(TEST_TRIGS)

testcases.files.prox: $(TEST_PROVX)


