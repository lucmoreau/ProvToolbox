| | |
|---|---|
| date | 2026-10-31T13:59:33.874273325Z[Etc/UTC] |
| OS | Linux 6.18.44-fc-v185 amd64 |
| CPU | Intel(R) Xeon(R) Processor @ 2.80GHz |
| cores | 4 available to the JVM |
| RAM | MemTotal: 16477352 kB |
| storage | Filesystem      Size  Used Avail Use% Mounted on / /dev/vda        252G   12G   27G  32% / |
| Docker | server , VM  kernel , 0 CPUs, 0 bytes RAM, cgroup v, storage 
Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running? |
| PostgreSQL storage | named Docker volume provbench-pgdata inside the Docker VM (not a bind mount): pg_test_fsync measures the VM's disk |
| pg_test_fsync | fdatasync 2291.963 ops/sec 436 usecs/op (full output: results/pg_test_fsync-vm.txt) |
| PostgreSQL | not used (JSONL mode) |
| ProvToolbox | bench/service-throughput fca84b7dade02ba2f2e24e5ee5bd428607d30806 (+ uncommitted changes) |
| Java (service and harness) | openjdk version "21.0.12.1" 2026-08-18 / OpenJDK Runtime Environment (build 21.0.12.1+1-1-24.04.4-Ubuntu) / OpenJDK 64-Bit Server VM (build 21.0.12.1+1-1-24.04.4-Ubuntu, mixed mode, sharing) |
| service JVM flags | -Xms2g -Xmx2g -XX:+UseG1GC; Jetty max threads 200 (embedded Jetty 11, ServiceLauncher) |
| template library | provenance-templates-library 0.1.0-SNAPSHOT; composite size 3 |
| JSONL directory | /home/user/ProvToolbox/modules-misc/benchmarks/service-throughput/target/jsonl (on the host, not in the Docker VM): Filesystem      Size  Used Avail Use% Mounted on / /dev/vda        252G   12G   27G  32% / |
| JSONL client JVM | Ubuntu 21.0.12.1; the service JVM and PostgreSQL rows above do not apply (no service, no store) |
