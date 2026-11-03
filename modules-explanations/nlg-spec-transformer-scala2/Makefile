XPLAN_JSON := $(shell find src/test/resources/nlg/templates -name *.json | grep -v todelete | grep -v template-library | grep -v provbasic.json  | grep -v provbasic2.json| grep -v profile1 | grep -v dictionary-loan)
CHECK_JSON := $(patsubst src/test/resources/nlg/templates/%, check.all/%, $(XPLAN_JSON))

go:
	echo $(XPLAN_JSON)
	echo $(CHECK_JSON)

check.all/loan/%.json: src/test/resources/nlg/templates/loan/%.json
	$(MAKE) FILE=src/test/resources/nlg/templates/loan/$* schema.test

check.all/provbasic/%.json: src/test/resources/nlg/templates/provbasic/%.json
	$(MAKE) FILE=src/test/resources/nlg/templates/provbasic/$* schema.test

check.all: $(CHECK_JSON)

t1:
	$(MAKE) FILE=loan/human.decision1.json schema.test

t2:
	$(MAKE) FILE=loan/responsibility6.json schema.test

schema.test:
	ajv -s src/test/resources/jsonschema/xplan.json -d $(FILE)




