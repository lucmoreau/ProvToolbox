


t1:
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn -outfile target/w3c-publication1.ttl
	target/appassembler/bin/provconvert -provn2xml ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.xml
	target/appassembler/bin/provconvert -provn2dot ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.dot target/w3c-publication1.pdf ../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


t2:
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn -outfile target/w3c-publication2.ttl
	target/appassembler/bin/provconvert -provn2xml ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.xml
	target/appassembler/bin/provconvert -provn2dot ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.dot target/w3c-publication2.pdf ../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml

t3:
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn -outfile target/w3c-publication3.ttl
	target/appassembler/bin/provconvert -provn2xml ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.xml
	target/appassembler/bin/provconvert -provn2dot ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.dot target/w3c-publication3.pdf ../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


fam:
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.pdf 
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.provx
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.dot
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.ttl
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.rdf
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family-graphics.provn -outfile  target/prov-family.svg

expfam:
	cp ../prov-n/src/test/resources/prov/prov-family.provn ~/luc-svn/papers/prov-wg/hg/provenance
	cp target/prov-family.* ~/luc-svn/papers/prov-wg/hg/provenance

#	cat target/prov-family.dot | sed -e 's/rankdir/size="8.5,11"; label="The PROV Family of Specifications\\nLuc Moreau"; labelloc="top"; labeljust="left"; fontsize="36"; rankdir/' | sed -e 's/OPMGraph/ProvFamily/' > target/prov-family-key.dot
#	dot -Tps2 target/prov-family-key.dot -o target/prov-family-key.ps
#	epstopdf  target/prov-family-key.ps  -o=target/prov-family-key.pdf




fam2:
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family2.pn -outfile target/prov-family2.pdf
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family2.pn -outfile target/prov-family2.dot
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/prov-family2.pn -outfile target/prov-family2.xml
	cat target/prov-family2.dot | sed -e 's/rankdir/size="8.5,11"; label="The PROV Family of Specifications\\nLuc Moreau"; labelloc="top"; labeljust="left"; fontsize="36"; rankdir/' | sed -e 's/OPMGraph/ProvFamily/' > target/prov-family2-key.dot
	dot -Tps2 target/prov-family2-key.dot -o target/prov-family2-key.ps
	epstopdf target/prov-family2-key.ps  -o=target/prov-family2-key.pdf

html:
	target/appassembler/bin/provconvert -provn2html ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.html
	cat src/main/resources/header.html > target/foo.html
	cat target/w3c-publication1.html >> target/foo.html
	cat src/main/resources/footer.html >> target/foo.html


# TODO: bundle missing
b1:
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.provn
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.xml
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.trig
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.json
	target/appassembler/bin/provconvert -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.pdf


pc1:
	target/appassembler/bin/provconvert -infile ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.provn
	target/appassembler/bin/provconvert -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.ttl
	target/appassembler/bin/provconvert -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.xml
	target/appassembler/bin/provconvert -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.json
	target/appassembler/bin/provconvert -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.pdf


blog:
	target/appassembler/bin/provconvert -provn2dot ../prov-n/src/test/resources/prov/example-blog1.provn target/blog1.dot target/blog1.pdf ../prov-dot/src/main/resources/configFamily.xml 
	target/appassembler/bin/provconvert -provn2dot ../prov-n/src/test/resources/prov/example-blog2.provn target/blog2.dot target/blog2.pdf ../prov-dot/src/main/resources/configFamily.xml 



# provide absolute arguments
note:
	target/appassembler/bin/provconvert -provn2turtle `pwd`/../prov-n/src/test/resources/prov/w3c-publication1.prov-asn `pwd`/target/w3c-publication1.ttl


jpl:
	@echo "------"
	target/appassembler/bin/provconvert -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.xml
	@echo "------"
	target/appassembler/bin/provconvert -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.provn
	@echo "------"
	target/appassembler/bin/provconvert -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.ttl
	@echo "------"
	target/appassembler/bin/provconvert -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.json
	@echo "------"
	target/appassembler/bin/provconvert -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.pdf
	@echo "------"
#
# TODO
#	target/appassembler/bin/provconvert -xml2dot target/jpl.xml target/jpl.dot target/jpl.pdf ../prov-dot/src/main/resources/configFamily.xml 

