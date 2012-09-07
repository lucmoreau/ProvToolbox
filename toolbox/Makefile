


t1:
	target/appassembler/bin/provconvert -asn2turtle ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.ttl
	target/appassembler/bin/provconvert -asn2xml ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.xml
	target/appassembler/bin/provconvert -asn2dot ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.dot target/w3c-publication1.pdf ../dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


t2:
	target/appassembler/bin/provconvert -asn2turtle ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.ttl
	target/appassembler/bin/provconvert -asn2xml ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.xml
	target/appassembler/bin/provconvert -asn2dot ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.dot target/w3c-publication2.pdf ../dot/src/main/resources/defaultConfigWithRoleNoLabel.xml

t3:
	target/appassembler/bin/provconvert -asn2turtle ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.ttl
	target/appassembler/bin/provconvert -asn2xml ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.xml
	target/appassembler/bin/provconvert -asn2dot ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.dot target/w3c-publication3.pdf ../dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


fam:
	target/appassembler/bin/provconvert -asn2dot ../prov-n/src/test/resources/prov/prov-family.pn target/prov-family.dot target/prov-family.pdf ../dot/src/main/resources/configFamily.xml
	target/appassembler/bin/provconvert -asn2xml ../prov-n/src/test/resources/prov/prov-family.pn target/prov-family.xml
	cat target/prov-family.dot | sed -e 's/rankdir/size="8.5,11"; label="The PROV Family of Specifications\\nLuc Moreau"; labelloc="top"; labeljust="left"; fontsize="36"; rankdir/' | sed -e 's/OPMGraph/ProvFamily/' > target/prov-family-key.dot
	dot -Tps2 target/prov-family-key.dot -o target/prov-family-key.ps
	epstopdf  target/prov-family-key.ps  -o=target/prov-family-key.pdf


fam2:
	target/appassembler/bin/provconvert -asn2dot ../prov-n/src/test/resources/prov/prov-family2.pn target/prov-family2.dot target/prov-family2.pdf ../dot/src/main/resources/configFamily.xml
	target/appassembler/bin/provconvert -asn2xml ../prov-n/src/test/resources/prov/prov-family2.pn target/prov-family2.xml
	cat target/prov-family2.dot | sed -e 's/rankdir/size="8.5,11"; label="The PROV Family of Specifications\\nLuc Moreau"; labelloc="top"; labeljust="left"; fontsize="36"; rankdir/' | sed -e 's/OPMGraph/ProvFamily/' > target/prov-family2-key.dot
	dot -Tps2 target/prov-family2-key.dot -o target/prov-family2-key.ps
	epstopdf target/prov-family2-key.ps  -o=target/prov-family2-key.pdf

html:
	target/appassembler/bin/provconvert -asn2html ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.html
	cat src/main/resources/header.html > target/foo.html
	cat target/w3c-publication1.html >> target/foo.html
	cat src/main/resources/footer.html >> target/foo.html



b1:
	target/appassembler/bin/provconvert -asn2xml ../prov-n/src/test/resources/prov/bundles1.pn target/bundles1.xml


pc1:
	target/appassembler/bin/provconvert -xml2asn ../prov-xml/target/pc1-full.xml target/pc1-full.provn





# provide absolute arguments
note:
	target/appassembler/bin/provconvert -asn2turtle `pwd`/../prov-n/src/test/resources/prov/w3c-publication1.prov-asn `pwd`/target/w3c-publication1.ttl

