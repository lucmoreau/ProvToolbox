
do.all:
	mkdir target
	$(MAKE) vis.doc1
	$(MAKE) vis.doc2
	$(MAKE) merge1-2
	$(MAKE) vis.doc1b1
	$(MAKE) vis.doc2b2
	$(MAKE) vis.doc2b1
	$(MAKE) merge1b1-2b2
	$(MAKE) merge1b1-2b1

conv1:
	provconvert -version

vis.doc1:
	provconvert -infile src/main/resources/doc1.provn -outfile target/doc1.png
vis.doc2:
	provconvert -infile src/main/resources/doc2.provn -outfile target/doc2.png

merge1-2:
	provconvert -merge src/main/resources/doc1-2-listing.txt -outfile target/doc1-2.provn
	provconvert -infile target/doc1-2.provn -outfile target/doc1-2.png


vis.doc1b1:
	provconvert -infile src/main/resources/doc1b1.provn -outfile target/doc1b1.png
vis.doc2b2:
	provconvert -infile src/main/resources/doc2b2.provn -outfile target/doc2b2.png
vis.doc2b1:
	provconvert -infile src/main/resources/doc2b1.provn -outfile target/doc2b1.png

merge1b1-2b2:
	provconvert -merge src/main/resources/doc1b1-2b2-listing.txt -outfile target/doc1b1-2b2.provn
	provconvert -infile target/doc1b1-2b2.provn -outfile target/doc1b1-2b2.png
	provconvert -merge src/main/resources/doc1b1-2b2-listing.txt -flatten -outfile target/doc1b1-2b2-flatten.provn
	provconvert -infile target/doc1b1-2b2-flatten.provn -outfile target/doc1b1-2b2-flatten.png

merge1b1-2b1:
	provconvert -merge src/main/resources/doc1b1-2b1-listing.txt -outfile target/doc1b1-2b1.provn
	provconvert -infile target/doc1b1-2b1.provn -outfile target/doc1b1-2b1.png
	provconvert -merge src/main/resources/doc1b1-2b1-listing.txt -flatten -outfile target/doc1b1-2b1-flatten.provn
	provconvert -infile target/doc1b1-2b1-flatten.provn -outfile target/doc1b1-2b1-flatten.png
