
PROVCONVERT=~/IdeaProjects/ProvToolbox/modules-executable/toolbox/target/appassembler/bin/provconvert


all: png ttl

png: consent-granted-v1.png consent-not-granted-v1.png consent-alt-granted-v1.png

ttl: consent-granted-v1.ttl consent-not-granted-v1.ttl consent-alt-granted-v1.ttl

%.png: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
%.svg: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
%.ttl: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
%.pdf: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
%.trig: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
%.dot: %.provn
	$(PROVCONVERT) -infile $< -outfile $@
