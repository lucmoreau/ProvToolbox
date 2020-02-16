
go:
	mvn compile exec:java

JVM_ARGS=-Xms1g -Xmx16g
DATE=`date "+%Y-%m-%d-%H-%M"`


bench1:
	java $(JVM_ARGS) -jar target/benchmarks.jar -f 1 -wi 3 -i 5  -bm all -tu us -rf csv -rff target/bench-$(DATE).csv -o target/bench-$(DATE).txt

bench2:
	java $(JVM_ARGS) -jar target/benchmarks.jar -f 2 -wi 3 -i 5  -bm all -tu us -rf csv -rff target/bench-$(DATE).csv -o target/bench-$(DATE).txt


plot.file:
	R --no-save --args target/bench-2020-02-16-18-02.csv target/foo.pdf   < src/main/R/plot.R



quick:
	java -jar target/benchmarks.jar -f 1 -wi 0 -i 1  -bm all -tu us -rf csv -rff target/bench1-quick-$(DATE).csv -o target/bench1-quick-$(DATE).txt $(TESTS)



PROFILE_PARAMS=1000000
PROFILE_KIND=bin
PROFILE_JAVA_PARAMS=-Xms1g -Xmx8g
profiler:
	java $(PROFILE_JAVA_PARAMS) -jar target/benchmarks.jar -pname=$(PROFILE_PARAMS) -pgraph=$(PROFILE_KIND) -jvmArgs " -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"  SerialisationBenchmarks.testDeepCopyScala

visual:
	jvisualvm

