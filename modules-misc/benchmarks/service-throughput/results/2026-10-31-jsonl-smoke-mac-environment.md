| | |
|---|---|
| date | 2026-10-31T14:14:27.102231Z[Europe/London] |
| OS | Mac OS X 26.5.2 aarch64 |
| CPU | Apple M4 Max |
| cores | 16 physical, 16 logical |
| RAM | 48 GiB |
| storage | File System Personality: APFS; Protocol: Apple Fabric; Solid State: Yes |
| Docker | server 29.7.2, VM Docker Desktop kernel 6.12.76-linuxkit, 16 CPUs, 8216018944 bytes RAM, cgroup v2, storage overlay2 |
| PostgreSQL storage | named Docker volume provbench-pgdata inside the Docker VM (not a bind mount): pg_test_fsync measures the VM's disk |
| pg_test_fsync | fdatasync 10918.572 ops/sec 92 usecs/op (full output: results/pg_test_fsync-Mac.txt) |
| PostgreSQL | not used (JSONL mode) |
| ProvToolbox | bench/service-throughput 70ee1c53bec1070274b3d406639658628e17527b |
| Java (service and harness) | openjdk version "19.0.1" 2022-10-18 / OpenJDK Runtime Environment Temurin-19.0.1+10 (build 19.0.1+10) / OpenJDK 64-Bit Server VM Temurin-19.0.1+10 (build 19.0.1+10, mixed mode) |
| service JVM flags | -Xms2g -Xmx2g -XX:+UseG1GC; Jetty max threads 200 (embedded Jetty 11, ServiceLauncher) |
| template library | provenance-templates-library 0.1.0-SNAPSHOT; composite size 3 |
| JSONL directory | /Users/luc/IdeaProjects/ProvToolbox-bench/modules-misc/benchmarks/service-throughput/target/jsonl (on the host, not in the Docker VM): Filesystem      Size    Used   Avail Capacity iused ifree %iused  Mounted on / /dev/disk3s5   1.8Ti   1.5Ti   280Gi    85%    6.6M  2.9G    0%   /System/Volumes/Data |
| JSONL client JVM | Eclipse Adoptium 19.0.1; the service JVM and PostgreSQL rows above do not apply (no service, no store) |
