


t1:
	target/appassembler/bin/provconvert -asn2rdf ../asn/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.ttl
	target/appassembler/bin/provconvert -asn2xml ../asn/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.xml
	target/appassembler/bin/provconvert -asn2dot ../asn/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.dot target/w3c-publication1.pdf ../dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


t2:
	target/appassembler/bin/provconvert -asn2rdf ../asn/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.ttl
	target/appassembler/bin/provconvert -asn2xml ../asn/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.xml
	target/appassembler/bin/provconvert -asn2dot ../asn/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.dot target/w3c-publication2.pdf ../dot/src/main/resources/defaultConfigWithRoleNoLabel.xml

t3:
	target/appassembler/bin/provconvert -asn2rdf ../asn/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.ttl
	target/appassembler/bin/provconvert -asn2xml ../asn/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.xml
	target/appassembler/bin/provconvert -asn2dot ../asn/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.dot target/w3c-publication3.pdf ../dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


fam:
	target/appassembler/bin/provconvert -asn2dot ../asn/src/test/resources/prov/prov-family.pn target/prov-family.dot target/prov-family.pdf ../dot/src/main/resources/configFamily.xml
	target/appassembler/bin/provconvert -asn2xml ../asn/src/test/resources/prov/prov-family.pn target/prov-family.xml
	cat target/prov-family.dot | sed -e 's/rankdir/size="8.5,11"; rankdir/' > target/prov-family2.dot
	dot -Tps2 target/prov-family2.dot -o target/prov-family2.ps
	epstopdf target/prov-family2.ps  -o=target/prov-family2.pdf






# provide absolute arguments
note:
	target/appassembler/bin/provconvert -asn2rdf `pwd`/../asn/src/test/resources/prov/w3c-publication1.prov-asn `pwd`/target/w3c-publication1.ttl

