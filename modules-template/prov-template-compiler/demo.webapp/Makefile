jetty:
	 mvn jetty:run

run:
	mvn exec:java -Dexec.mainClass="Example" -Dexec.args="target/demo.csv"
	cat target/demo.csv

2prov:
	../target/libs/bin/example_dt target/demo.csv target/demo.svg


toprov:
	mvn exec:java -Dexec.mainClass="org.openprovenance.prov.interop.CommandLineArguments"  -Dexec.args="-infile target/demo.csv -log2prov org.example.templates.Init -outfile target/demo.svg"

toprov2:
	mvn exec:java -Dexec.mainClass="org.example.templates.Init"  -Dexec.args="target/demo.csv target/demo.svg -merge"

PSQL=/Library/PostgreSQL/11/bin/psql


todb:
	mvn exec:java -Dexec.mainClass="org.example.templates.Init"  -Dexec.args="target/demo.csv target/demo.svg -db target/demo.sql"
	cat src/main/resources/intro.sql > target/exec.sql
	cat ../target/libs/templates/templates_cli/src/main/resources/template.sql >> target/exec.sql
	cat target/demo.sql >> target/exec.sql
	$(PSQL) -h localhost -U postgres -d postgres -w < target/exec.sql
