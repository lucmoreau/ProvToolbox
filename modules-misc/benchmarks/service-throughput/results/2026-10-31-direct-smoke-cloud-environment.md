| | |
|---|---|
| date | 2026-10-31T11:39:28.383837612Z[Etc/UTC] |
| OS | Linux 6.18.44-fc-v185 amd64 |
| CPU | Intel(R) Xeon(R) Processor @ 2.80GHz |
| cores | 4 available to the JVM |
| RAM | MemTotal: 16477352 kB |
| storage | Filesystem      Size  Used Avail Use% Mounted on / /dev/vda        252G   12G   27G  32% / |
| Docker | server 29.9.2, VM Ubuntu 24.04.5 LTS kernel 6.18.44-fc-v185, 4 CPUs, 16872808448 bytes RAM, cgroup v1, storage overlayfs |
| PostgreSQL storage | named Docker volume provbench-pgdata inside the Docker VM (not a bind mount): pg_test_fsync measures the VM's disk |
| pg_test_fsync | fdatasync 2291.963 ops/sec 436 usecs/op (full output: results/pg_test_fsync-vm.txt) |
| PostgreSQL | PostgreSQL 16.15 (Debian 16.15-1.pgdg13+2) on x86_64-pc-linux-gnu, compiled by gcc (Debian 14.2.0-19) 14.2.0, 64-bit |
| PostgreSQL settings (non-default) | dynamic_shared_memory_type=posix, listen_addresses=*, max_connections=200, max_wal_size=1024MB, min_wal_size=80MB, pg_stat_statements.max=10000, shared_buffers=1310728kB, shared_preload_libraries=pg_stat_statements, track_io_timing=on |
| ProvToolbox | bench/service-throughput e8bcb89760aeeb7e36a1ad1c83c51ca18b84f8a7 (+ uncommitted changes) |
| Java (service and harness) | openjdk version "21.0.12.1" 2026-08-18 / OpenJDK Runtime Environment (build 21.0.12.1+1-1-24.04.4-Ubuntu) / OpenJDK 64-Bit Server VM (build 21.0.12.1+1-1-24.04.4-Ubuntu, mixed mode, sharing) |
| service JVM flags | -Xms2g -Xmx2g -XX:+UseG1GC; Jetty max threads 200 (embedded Jetty 11, ServiceLauncher) |
| template library | provenance-templates-library 0.1.0-SNAPSHOT; composite size 3 |
| direct client JVM | Ubuntu 21.0.12.1; -Xms2g -Xmx2g -XX:+UseG1GC -Dbench.home=/home/user/ProvToolbox/modules-misc/benchmarks/service-throughput; the service JVM rows above do not apply (no service) |
