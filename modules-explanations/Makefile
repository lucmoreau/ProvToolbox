
WHERE=../modules-executable-provman
PROVMAN=$(WHERE)/target/appassembler/bin/provmanagement

EXPLANATIONS_DIR=prov-explanations
TEMPLATE=plead-profile1


xplain:
	$(PROVMAN) explain --infile $(EXPLANATIONS_DIR)/src/test/resources/plead/$(FILE).provn    --outfile $(EXPLANATIONS_DIR)/target/gdpr-test.provn --text $(EXPLANATIONS_DIR)/target/gdpr-test.json --language $(EXPLANATIONS_DIR)/src/main/resources/nlg/templates/plead/plead-template-library.json -s $(EXPLANATIONS_DIR)/target/simplenlg2.json --profile=$(PROFILE)  --templates=$(TEMPLATE) 

batch.xplain:
	@$(PROVMAN) explain --infile $(EXPLANATIONS_DIR)/src/test/resources/plead/$(FILE).provn    --outfile $(EXPLANATIONS_DIR)/target/gdpr-test.provn --text $(EXPLANATIONS_DIR)/target/gdpr-test.json --language $(EXPLANATIONS_DIR)/src/main/resources/nlg/templates/plead/plead-template-library.json -s $(EXPLANATIONS_DIR)/target/simplenlg2.json --profile=$(PROFILE)  --batch-templates=$(BATCH)

thump.batch.xplain:
	@$(PROVMAN) explain --infile $(EXPLANATIONS_DIR)/src/test/resources/thump/$(FILE).provn    --outfile $(EXPLANATIONS_DIR)/target/thump-test.provn --text $(EXPLANATIONS_DIR)/target/thump-test.json --language $(EXPLANATIONS_DIR)/src/main/resources/nlg/templates/thump/thump-template-library.json -s $(EXPLANATIONS_DIR)/target/simplenlg3.json --profile=$(PROFILE)  --batch-templates=$(BATCH)


# generic query, generic generation, TODO: english to improve
xp.human.decision:
	@$(MAKE) xplain FILE=128350251 TEMPLATE=plead.human.decision1



PROFILE=ln:datasubject-noun
PROFILE=ln:datasubject-person3-f
PROFILE=ln:datasubject-person2


xp.erasure.accept.a.v2:
	$(MAKE) xplain FILE=128350251-5.2-accept TEMPLATE=plead.erasure.accept2a
xp.erasure.accept.b.v2:
	$(MAKE) xplain FILE=128350251-5.2-accept TEMPLATE=plead.erasure.accept2b
xp.erasure.accept.v2:
	@$(MAKE) batch.xplain FILE=128350251-5.2-accept BATCH=[plead.erasure.accept2a][plead.erasure.accept2b]

xp.erasure.reject:
	@$(MAKE) batch.xplain FILE=128350251-5.2-reject BATCH=[plead.erasure.reject]


xp.rectification.accept.a.v2:
	$(MAKE) xplain FILE=128350251-4.2-accept TEMPLATE=plead.rectification.accept2a
xp.rectification.accept.b.v2:
	$(MAKE) xplain FILE=128350251-4.2-accept TEMPLATE=plead.rectification.accept2b
xp.rectification.accept.v2:
	@$(MAKE) batch.xplain FILE=128350251-4.2-accept BATCH=[plead.rectification.accept2a][plead.rectification.accept2b]

q.rectification.accept.b.v2:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/plead/128350251-4.2-accept.provn  --query prov-explanations/src/test/resources/query/plead/q-128350251-4.2-accept.txt --outfiles prov-explanations/target/loan2.provn

xp.rectification.reject:
	@$(MAKE) batch.xplain FILE=128350251-4.2-reject BATCH=[plead.rectification.reject]


xp.rectification.accept.v1:
	$(MAKE) xplain FILE=128350251-4.2 TEMPLATE=plead.rectification.accept1


xp.erasure.accept.v1:
	$(MAKE) xplain FILE=128350251-5.2 TEMPLATE=plead.erasure.accept1

display:
	@jq . prov-explanations/target/gdpr-test.json

display2:
	@jq . prov-explanations/target/thump-test.json

display.sa:
	@jq . prov-explanations/target/sa-test.json



xp.all:
	$(MAKE) xp.erasure.accept.v2 display
	$(MAKE) xp.erasure.reject display
	$(MAKE) xp.rectification.accept.v2 display
	$(MAKE) xp.rectification.reject display


plead.expand.old:
	provconvert -bindver 3 -infile prov-explanations/src/test/resources/plead/templates/old/128350251-4.2-accept.provn -bindings prov-explanations/src/test/resources/plead/bindings/old/b1.json -outfile prov-explanations/target/128350251-4.2-accept-old.provn

plead.expand.pdf:
	provconvert -infile prov-explanations/src/test/resources/plead/templates/application.provn -outfile prov-explanations/src/test/resources/plead/templates/application.pdf
	provconvert -infile prov-explanations/src/test/resources/plead/templates/rectification-4.2-accept.provn -outfile prov-explanations/src/test/resources/plead/templates/rectification-4.2-accept.pdf
	provconvert -infile prov-explanations/src/test/resources/plead/templates/rectification-4.2-reject.provn -outfile prov-explanations/src/test/resources/plead/templates/rectification-4.2-reject.pdf

plead.expand:
	$(MAKE) CASE=128350251 plead.expand.application
	$(MAKE) CASE=128350251 plead.expand.rectification-4.2-accept
	$(MAKE) CASE=128350251 plead.expand.rectification-4.2-reject
	ls prov-explanations/target/128350251-application.provn prov-explanations/target/128350251-rectification-4.2-accept.provn | provconvert -merge - -outfile prov-explanations/target/merged-accept-flatten.provn -flatten
	ls prov-explanations/target/128350251-application.provn prov-explanations/target/128350251-rectification-4.2-reject.provn | provconvert -merge - -outfile prov-explanations/target/merged-reject-flatten.provn -flatten
	ls prov-explanations/target/128350251-application.provn prov-explanations/target/128350251-rectification-4.2-accept.provn | provconvert -merge - -outfile prov-explanations/target/merged-accept.provn
	ls prov-explanations/target/128350251-application.provn prov-explanations/target/128350251-rectification-4.2-reject.provn | provconvert -merge - -outfile prov-explanations/target/merged-reject.provn
	provconvert -infile prov-explanations/target/merged-accept.provn -outfile prov-explanations/target/merged-accept.pdf
	provconvert -infile prov-explanations/target/merged-accept-flatten.provn -outfile prov-explanations/target/merged-accept-flatten.pdf
	provconvert -infile prov-explanations/target/merged-reject.provn -outfile prov-explanations/target/merged-reject.pdf
	provconvert -infile prov-explanations/target/merged-reject-flatten.provn -outfile prov-explanations/target/merged-reject-flatten.pdf


plead.expand.application:
	provconvert -bindver 3 -infile prov-explanations/src/test/resources/plead/templates/application.provn -bindings prov-explanations/src/test/resources/plead/bindings/$(CASE)-application.json -outfile prov-explanations/target/$(CASE)-application.provn
plead.expand.rectification-4.2-accept:
	provconvert -bindver 3 -infile prov-explanations/src/test/resources/plead/templates/rectification-4.2-accept.provn -bindings prov-explanations/src/test/resources/plead/bindings/$(CASE)-rectification-4.2-accept.json -outfile prov-explanations/target/$(CASE)-rectification-4.2-accept.provn
plead.expand.rectification-4.2-reject:
	provconvert -bindver 3 -infile prov-explanations/src/test/resources/plead/templates/rectification-4.2-reject.provn -bindings prov-explanations/src/test/resources/plead/bindings/$(CASE)-rectification-4.2-reject.json -outfile prov-explanations/target/$(CASE)-rectification-4.2-reject.provn


xp.thump.plan.failure.v1:
	@$(MAKE) thump.batch.xplain FILE=webist-rosplan BATCH=[thump.plan.actions1a][thump.plan.failure1a][thump.plan.failure1b][thump.plan.failure1c][thump.plan.failure1d][thump.plan.models1a][thump.plan.models1b][thump.plan.models1c]

plead.q.sa.an:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/AN.provn  --query prov-explanations/src/test/resources/query/sa/q-on-time-first-choice.txt --outfiles prov-explanations/target/an.pdf

plead.q.sa.an2:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/AN.provn --infiles '{"schools": "prov-explanations/src/test/resources/sa/schools.provn"}' --query prov-explanations/src/test/resources/query/sa/q-on-time-first-choice2.txt --outfiles prov-explanations/target/an.pdf


plead.q.sa.gh:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/GH.provn  --query prov-explanations/src/test/resources/query/sa/q-on-time-third-choice.txt --outfiles prov-explanations/target/gh.pdf

plead.q.sa.gh2:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/GH.provn --infiles '{"schools": "prov-explanations/src/test/resources/sa/schools.provn"}' --query prov-explanations/src/test/resources/query/sa/q-on-time-third-choice2.txt --outfiles prov-explanations/target/gh2.pdf

plead.q.sa.gh2a:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/GH.provn --infiles '{"schools": "prov-explanations/src/test/resources/sa/schools.provn"}' --query prov-explanations/src/test/resources/query/sa/q-on-time-third-choice2a.txt --outfiles prov-explanations/target/gh2a.pdf
plead.q.sa.gh2b:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/GH.provn --infiles '{"schools": "prov-explanations/src/test/resources/sa/schools.provn"}' --query prov-explanations/src/test/resources/query/sa/q-on-time-third-choice2b.txt --outfiles prov-explanations/target/gh2b.pdf

plead.q.sa.pr:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/PR-alt.provn  --query prov-explanations/src/test/resources/query/sa/q-on-time-alt-choice.txt --outfiles prov-explanations/target/pr-alt.pdf

plead.q.sa.pr2:
	$(PROVMAN) translate --infile prov-explanations/src/test/resources/sa/PR-alt.provn --infiles '{"schools": "prov-explanations/src/test/resources/sa/schools.provn"}' --query prov-explanations/src/test/resources/query/sa/q-on-time-alt-choice2.txt --outfiles prov-explanations/target/pr-alt2.provn,prov-explanations/target/pr-alt2.pdf


sa.batch.xplain:
	@$(PROVMAN) explain --infile $(EXPLANATIONS_DIR)/src/test/resources/sa/$(FILE).provn   --infiles '{"schools": "prov-explanations/src/test/resources/sa/schools.provn"}'  --outfile $(EXPLANATIONS_DIR)/target/sa-test.provn --text $(EXPLANATIONS_DIR)/target/sa-test.json --language $(EXPLANATIONS_DIR)/src/main/resources/nlg/templates/sa/sa-template-library.json -s $(EXPLANATIONS_DIR)/target/simplenlg.sa.json --profile=$(PROFILE)  --batch-templates=$(BATCH)

sa.demo:
	@$(MAKE) sa.on.time.first.choice2 display.sa


sa.on.time.first.choice2:
	@$(MAKE) sa.batch.xplain FILE=AN BATCH=[sa.on.time.first.choice2]



collabmap.batch.xplain:
	@$(PROVMAN) explain --infile $(EXPLANATIONS_DIR)/src/test/resources/collabmap/$(FILE).provn    --outfile $(EXPLANATIONS_DIR)/target/collabmap-test.provn --text $(EXPLANATIONS_DIR)/target/thump-test.json --language $(EXPLANATIONS_DIR)/src/main/resources/nlg/templates/collabmap/collabmap-template-library.json -s $(EXPLANATIONS_DIR)/target/simplenlg3.json --profile=$(PROFILE)  --batch-templates=$(BATCH)

collabmap.do.narrative1:
	$(PROVMAN) narrate --language=prov-explanations/src/main/resources/nlg/templates/collabmap/collabmap-template-library.json --infile prov-explanations/src/test/resources/collabmap/collabmap1.provn   --outfile prov-explanations/target/collabmap-narrative1.provn --matrix prov-explanations/target/collabmap-matrix.txt --text prov-explanations/target/collabmap-narrative1.txt --description prov-explanations/target/collabmap-narrative1-order.json --simplenlg prov-explanations/target/collabmap-nlg.json

collabmap.do.narrative2:
	$(PROVMAN) narrate --language=prov-explanations/src/main/resources/nlg/templates/collabmap/collabmap-template-library.json --infile prov-explanations/src/test/resources/collabmap/collabmap1.provn   --outfile prov-explanations/target/collabmap-narrative1.provn --matrix prov-explanations/target/collabmap-matrix.txt --text prov-explanations/target/collabmap-narrative1.txt --description prov-explanations/target/collabmap-narrative1-order.json --simplenlg prov-explanations/target/collabmap-nlg.json
