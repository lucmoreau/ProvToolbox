
PROVCONVERT=target/appassembler/bin/provconvert


rpm:
	mvn rpm:rpm

dmg:
	mvn package osxappbundle:bundle


t1:
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn -outfile target/w3c-publication1.ttl
	$(PROVCONVERT) -provn2xml ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.xml
	$(PROVCONVERT) -provn2dot ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.dot target/w3c-publication1.pdf ../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


t2:
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn -outfile target/w3c-publication2.ttl
	$(PROVCONVERT) -provn2xml ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.xml
	$(PROVCONVERT) -provn2dot ../prov-n/src/test/resources/prov/w3c-publication2.prov-asn target/w3c-publication2.dot target/w3c-publication2.pdf ../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml

t3:
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn -outfile target/w3c-publication3.ttl
	$(PROVCONVERT) -provn2xml ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.xml
	$(PROVCONVERT) -provn2dot ../prov-n/src/test/resources/prov/w3c-publication3.prov-asn target/w3c-publication3.dot target/w3c-publication3.pdf ../prov-dot/src/main/resources/defaultConfigWithRoleNoLabel.xml


fam:
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.pdf
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.provx
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.dot
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.ttl
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family.provn -outfile  target/prov-family.rdf
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family-graphics.provn -outfile  target/prov-family.svg

expfam:
	cp ../prov-n/src/test/resources/prov/prov-family.provn ~/luc-svn/papers/prov-wg/hg/provenance
	cp target/prov-family.* ~/luc-svn/papers/prov-wg/hg/provenance

#	cat target/prov-family.dot | sed -e 's/rankdir/size="8.5,11"; label="The PROV Family of Specifications\\nLuc Moreau"; labelloc="top"; labeljust="left"; fontsize="36"; rankdir/' | sed -e 's/OPMGraph/ProvFamily/' > target/prov-family-key.dot
#	dot -Tps2 target/prov-family-key.dot -o target/prov-family-key.ps
#	epstopdf  target/prov-family-key.ps  -o=target/prov-family-key.pdf




fam2:
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family2.pn -outfile target/prov-family2.pdf
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family2.pn -outfile target/prov-family2.dot
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/prov-family2.pn -outfile target/prov-family2.xml
	cat target/prov-family2.dot | sed -e 's/rankdir/size="8.5,11"; label="The PROV Family of Specifications\\nLuc Moreau"; labelloc="top"; labeljust="left"; fontsize="36"; rankdir/' | sed -e 's/OPMGraph/ProvFamily/' > target/prov-family2-key.dot
	dot -Tps2 target/prov-family2-key.dot -o target/prov-family2-key.ps
	epstopdf target/prov-family2-key.ps  -o=target/prov-family2-key.pdf

html:
	$(PROVCONVERT) -provn2html ../prov-n/src/test/resources/prov/w3c-publication1.prov-asn target/w3c-publication1.html
	cat src/main/resources/header.html > target/foo.html
	cat target/w3c-publication1.html >> target/foo.html
	cat src/main/resources/footer.html >> target/foo.html


# TODO: bundle missing
b1:
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.provn
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.xml
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.trig
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.json
	$(PROVCONVERT) -infile ../prov-n/src/test/resources/prov/bundles1.provn -outfile target/bundles1.pdf


pc1:
	$(PROVCONVERT) -infile ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.provn
	$(PROVCONVERT) -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.ttl
	$(PROVCONVERT) -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.xml
	$(PROVCONVERT) -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.json
	$(PROVCONVERT) -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.pdf


blog:
	$(PROVCONVERT) -provn2dot ../prov-n/src/test/resources/prov/example-blog1.provn target/blog1.dot target/blog1.pdf ../prov-dot/src/main/resources/configFamily.xml
	$(PROVCONVERT) -provn2dot ../prov-n/src/test/resources/prov/example-blog2.provn target/blog2.dot target/blog2.pdf ../prov-dot/src/main/resources/configFamily.xml



# provide absolute arguments
note:
	$(PROVCONVERT) -provn2turtle `pwd`/../prov-n/src/test/resources/prov/w3c-publication1.prov-asn `pwd`/target/w3c-publication1.ttl


jpl:
	@echo "------"
	$(PROVCONVERT) -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.xml
	@echo "------"
	$(PROVCONVERT) -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.provn
	@echo "------"
	$(PROVCONVERT) -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.ttl
	@echo "------"
	$(PROVCONVERT) -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.json
	@echo "------"
	$(PROVCONVERT) -infile /home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf -outfile target/jpl.pdf
	@echo "------"
#
# TODO
#	$(PROVCONVERT) -xml2dot target/jpl.xml target/jpl.dot target/jpl.pdf ../prov-dot/src/main/resources/configFamily.xml

#http://mojo.codehaus.org/license-maven-plugin/index.html
license:
	mvn license:add-third-party

missing:
	mvn license:add-third-party -Dlicense.useMissingFile


exp10:
	$(PROVCONVERT) -infile ../prov-template/src/test/resources/template10.provn -outfile target/expanded10.provn -bindings ../prov-template/src/test/resources/bindings10.provn

exp20:
	$(PROVCONVERT) -infile ../prov-template/src/test/resources/template20.provn -outfile target/expanded20.provn -bindings ../prov-template/target/bindings20.provn

exp21:
	$(PROVCONVERT) -infile ../prov-template/src/test/resources/template21.provn -outfile target/expanded21.provn -bindings ../prov-template/target/bindings21.provn


rand1:
	$(PROVCONVERT) -generator 50:4:entity -outfile target/random1.svg

rand2:
	$(PROVCONVERT) -generator 50:4:entity:-6386918869486095322 -outfile target/random2.svg


merge:
	printf "file, ../prov-xml/target/activity7.xml, xml\n../prov-xml/target/bundle1.xml\n" |	$(PROVCONVERT) -merge - -outfile target/merge1.svg
	printf "file, ../prov-xml/target/activity7.xml, xml\n../prov-xml/target/bundle1.xml\n" |	$(PROVCONVERT) -merge - -flatten -outfile target/merge2.svg
	printf "file, ../prov-xml/target/activity1.xml, xml\n../prov-xml/target/bundle1.xml\n" |	$(PROVCONVERT) -merge - -flatten -outfile target/merge3.provn

comp1:
	$(PROVCONVERT) -infile ../prov-template/src/test/resources/template10.provn -compare ../prov-template/src/test/resources/template10.provn -outcompare target/compare1.txt

test.attributes:
	ls src/test/resources/attributes/test-activity*.provn | $(PROVCONVERT) -merge - -outfile target/merged-activity.provn
	cat target/merged-activity.provn

issue.209:
	echo "=== from provn ==="
	curl --silent https://gist.githubusercontent.com/lucmoreau/588fdaeca5eb271cc6d0cd86816bea00/raw/ff621ab4bbb07d20585331be2c434e4bd575a8c8/date_with_tz_offset.provn | $(PROVCONVERT) -infile - -informat provn -outfile - -outformat provn
	curl --silent https://gist.githubusercontent.com/lucmoreau/588fdaeca5eb271cc6d0cd86816bea00/raw/ff621ab4bbb07d20585331be2c434e4bd575a8c8/date_with_tz_offset.provn | $(PROVCONVERT) -infile - -informat provn -outfile - -outformat jsonld
	echo "=== from jsonld ==="
	curl --silent https://gist.githubusercontent.com/lucmoreau/28632ef5bbe37b109300870c445772cc/raw/a711ebff4c433dd2f655bc4772e07b1e2809289e/gistfile1.jsonld | $(PROVCONVERT) -infile - -informat jsonld -outfile - -outformat provn
	curl --silent https://gist.githubusercontent.com/lucmoreau/28632ef5bbe37b109300870c445772cc/raw/a711ebff4c433dd2f655bc4772e07b1e2809289e/gistfile1.jsonld | $(PROVCONVERT) -infile - -informat jsonld -outfile - -outformat provn -dateTime PRESERVE
	curl --silent https://gist.githubusercontent.com/lucmoreau/28632ef5bbe37b109300870c445772cc/raw/a711ebff4c433dd2f655bc4772e07b1e2809289e/gistfile1.jsonld | $(PROVCONVERT) -infile - -informat jsonld -outfile - -outformat provn -dateTime UTC
	curl --silent https://gist.githubusercontent.com/lucmoreau/28632ef5bbe37b109300870c445772cc/raw/a711ebff4c433dd2f655bc4772e07b1e2809289e/gistfile1.jsonld | $(PROVCONVERT) -infile - -informat jsonld -outfile - -outformat provn -dateTime SYSTEM

mvn.test: test.issue.209.jsonld test.issue.209.json test.issue.209.provx test.issue.209.provn

test.issue.209.jsonld:
	@echo "- jsonld input: testing dateTime PRESERVE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].startTime=="2023-09-08T20:12:45.109-04:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T20:35:06.793-02:00"' > /dev/null
	@echo "- jsonld input: testing dateTime UTC"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime UTC | jq --exit-status '.["@graph"][0].startTime=="2023-09-09T00:12:45.109Z"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime UTC | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T22:35:06.793Z"' > /dev/null
	@echo "- jsonld input: testing dateTime TIMEZONE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Japan               | jq --exit-status '.["@graph"][0].startTime=="2023-09-09T09:12:45.109+09:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Morocco             | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T22:35:06.793Z"'        > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Europe/Rome         | jq --exit-status '.["@graph"][0].endTime=="2023-10-16T00:35:06.793+02:00"'   > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone America/New_York    | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T18:35:06.793-04:00"'   > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Australia/Melbourne | jq --exit-status '.["@graph"][0].endTime=="2023-10-16T09:35:06.793+11:00"'   > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.jsonld -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Asia/Shanghai       | jq --exit-status '.["@graph"][0].endTime=="2023-10-16T06:35:06.793+08:00"'   > /dev/null

test.issue.209.json:
	@echo "- json input: testing dateTime PRESERVE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime PRESERVE | jq --exit-status '.activity["ex:a"]["prov:startTime"]=="2023-09-08T20:12:45.109-04:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime PRESERVE | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-15T20:35:06.793-02:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].startTime=="2023-09-08T20:12:45.109-04:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T20:35:06.793-02:00"' > /dev/null
	@echo "- json input: testing dateTime UTC"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime UTC | jq --exit-status '.activity["ex:a"]["prov:startTime"]=="2023-09-09T00:12:45.109Z"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime UTC | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-15T22:35:06.793Z"' > /dev/null
	@echo "- json input: testing dateTime TIMEZONE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime TIMEZONE -timeZone Japan               | jq --exit-status '.activity["ex:a"]["prov:startTime"]=="2023-09-09T09:12:45.109+09:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime TIMEZONE -timeZone Morocco             | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-15T22:35:06.793Z"'        > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime TIMEZONE -timeZone Europe/Rome         | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-16T00:35:06.793+02:00"'   > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime TIMEZONE -timeZone America/New_York    | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-15T18:35:06.793-04:00"'   > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime TIMEZONE -timeZone Australia/Melbourne | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-16T09:35:06.793+11:00"'   > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.json -outfile - -outformat json -dateTime TIMEZONE -timeZone Asia/Shanghai       | jq --exit-status '.activity["ex:a"]["prov:endTime"]=="2023-10-16T06:35:06.793+08:00"'   > /dev/null

test.issue.209.provx:
	@echo "- provx input: testing dateTime PRESERVE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provx -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].startTime=="2023-09-08T20:12:45.109-04:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provx -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T20:35:06.793-02:00"' > /dev/null
	@echo "- jsonld input: testing dateTime UTC"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provx -outfile - -outformat jsonld -dateTime UTC | jq --exit-status '.["@graph"][0].startTime=="2023-09-09T00:12:45.109Z"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provx -outfile - -outformat jsonld -dateTime UTC | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T22:35:06.793Z"' > /dev/null
	@echo "- jsonld input: testing dateTime TIMEZONE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provx -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Japan               | jq --exit-status '.["@graph"][0].startTime=="2023-09-09T09:12:45.109+09:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provx -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Morocco             | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T22:35:06.793Z"'        > /dev/null

test.issue.209.provn:
	@echo "TODO provn input: testing dateTime PRESERVE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provn -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].startTime=="2023-09-08T20:12:45.109-04:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provn -outfile - -outformat jsonld -dateTime PRESERVE | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T20:35:06.793-02:00"' > /dev/null
	@echo "- jsonld input: testing dateTime UTC"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provn -outfile - -outformat jsonld -dateTime UTC | jq --exit-status '.["@graph"][0].startTime=="2023-09-09T00:12:45.109Z"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provn -outfile - -outformat jsonld -dateTime UTC | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T22:35:06.793Z"' > /dev/null
	@echo "- jsonld input: testing dateTime TIMEZONE"
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provn -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Japan               | jq --exit-status '.["@graph"][0].startTime=="2023-09-09T09:12:45.109+09:00"' > /dev/null
	@$(PROVCONVERT) -infile src/test/resources/dates/date_with_tz_offset.provn -outfile - -outformat jsonld -dateTime TIMEZONE -timeZone Morocco             | jq --exit-status '.["@graph"][0].endTime=="2023-10-15T22:35:06.793Z"'        > /dev/null
