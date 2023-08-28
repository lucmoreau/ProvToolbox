PROVCONVERT=provconvert

do.all:
	mkdir -p target
	$(MAKE) vis.doc1
	$(MAKE) vis.doc2
	$(MAKE) merge1-2
	$(MAKE) vis.doc1b1
	$(MAKE) vis.doc2b2
	$(MAKE) vis.doc2b1
	$(MAKE) merge1b1-2b2
	$(MAKE) merge1b1-2b1

conv1:
	$(PROVCONVERT) -version

vis.doc1:
	$(PROVCONVERT) -infile src/main/resources/doc1.provn -outfile target/doc1.png
vis.doc2:
	$(PROVCONVERT) -infile src/main/resources/doc2.provn -outfile target/doc2.png

merge1-2:
	$(PROVCONVERT) -merge src/main/resources/doc1-2-listing.txt -outfile target/doc1-2.provn
	$(PROVCONVERT) -infile target/doc1-2.provn -outfile target/doc1-2.png


vis.doc1b1:
	$(PROVCONVERT) -infile src/main/resources/doc1b1.provn -outfile target/doc1b1.png
vis.doc2b2:
	$(PROVCONVERT) -infile src/main/resources/doc2b2.provn -outfile target/doc2b2.png
vis.doc2b1:
	$(PROVCONVERT) -infile src/main/resources/doc2b1.provn -outfile target/doc2b1.png

merge1b1-2b2:
	$(PROVCONVERT) -merge src/main/resources/doc1b1-2b2-listing.txt -outfile target/doc1b1-2b2.provn
	$(PROVCONVERT) -infile target/doc1b1-2b2.provn -outfile target/doc1b1-2b2.png
	$(PROVCONVERT) -merge src/main/resources/doc1b1-2b2-listing.txt -flatten -outfile target/doc1b1-2b2-flatten.provn
	$(PROVCONVERT) -infile target/doc1b1-2b2-flatten.provn -outfile target/doc1b1-2b2-flatten.png

merge1b1-2b1:
	$(PROVCONVERT) -merge src/main/resources/doc1b1-2b1-listing.txt -outfile target/doc1b1-2b1.provn
	$(PROVCONVERT) -infile target/doc1b1-2b1.provn -outfile target/doc1b1-2b1.png
	$(PROVCONVERT) -merge src/main/resources/doc1b1-2b1-listing.txt -flatten -outfile target/doc1b1-2b1-flatten.provn
	$(PROVCONVERT) -infile target/doc1b1-2b1-flatten.provn -outfile target/doc1b1-2b1-flatten.png
