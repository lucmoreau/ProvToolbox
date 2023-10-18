
PROVCONVERT=../../../../../../modules-executable/toolbox/target/appassembler/bin/provconvert

M_DIR=./metatemplates
FILES_PIPELINE=$(M_DIR)/transforming1 $(M_DIR)/extending-collection $(M_DIR)/updating-entity

FILES=$(FILES_PIPELINE)

FILES_PNG=$(FILES:=.png)
FILES_SVG=$(FILES:=.svg)

all.png: $(FILES_PNG)
all.svg: $(FILES_SVG)

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
