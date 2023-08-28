PROVCONVERT=provconvert

do.all:
	mkdir target
	$(MAKE) expand1
	$(MAKE) expand2
	$(MAKE) expand3
	$(MAKE) expand4
	$(MAKE) expand5


expand1:
	$(PROVCONVERT) -infile src/main/resources/template1.provn -bindings src/main/resources/binding1.ttl -outfile target/doc1.provn
	$(PROVCONVERT) -infile target/doc1.provn -outfile target/doc1.png
	$(PROVCONVERT) -infile src/main/resources/template1.provn -outfile target/template1.png

expand2:
	$(PROVCONVERT) -infile src/main/resources/template1.provn -bindings src/main/resources/binding2.ttl -outfile target/doc2.provn
	$(PROVCONVERT) -infile target/doc2.provn -outfile target/doc2.png


expand3:
	$(PROVCONVERT) -infile src/main/resources/template1.provn -bindings src/main/resources/binding3.ttl -outfile target/doc3.provn
	$(PROVCONVERT) -infile target/doc3.provn -outfile target/doc3.png


expand4:
	$(PROVCONVERT) -infile src/main/resources/template2.provn -bindings src/main/resources/binding4.ttl -outfile target/doc4.provn
	$(PROVCONVERT) -infile target/doc4.provn -outfile target/doc4.png
	$(PROVCONVERT) -infile src/main/resources/template2.provn -outfile target/template2.png


expand5:
	$(PROVCONVERT) -infile src/main/resources/template3.provn -bindings src/main/resources/binding4.ttl -outfile target/doc5.provn
	$(PROVCONVERT) -infile target/doc5.provn -outfile target/doc5.png
	$(PROVCONVERT) -infile src/main/resources/template3.provn -outfile target/template3.png
