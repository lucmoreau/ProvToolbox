
go:
	mvn compile exec:java

JVM_ARGS=-Xms1g -Xmx16g
DATE=`date "+%Y-%m-%d-%H-%M"`


bench1:
	java $(JVM_ARGS) -jar target/benchmarks.jar -f 1 -wi 3 -i 5  -bm all -tu us -rf csv -rff target/bench-$(DATE).csv -o target/bench-$(DATE).txt

bench2:
	java $(JVM_ARGS) -jar target/benchmarks.jar -f 2 -wi 3 -i 5  -bm all -tu us -rf csv -rff target/bench-$(DATE).csv -o target/bench-$(DATE).txt




quick:
	java -jar target/benchmarks.jar -f 1 -wi 0 -i 1  -bm all -tu us -rf csv -rff target/bench1-quick-$(DATE).csv -o target/bench1-quick-$(DATE).txt $(TESTS)
