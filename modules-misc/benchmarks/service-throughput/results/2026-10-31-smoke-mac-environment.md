| | |
|---|---|
| date | 2026-10-31T07:51:40.187520Z[Europe/London] |
| OS | Mac OS X 26.5.2 aarch64 |
| CPU | Apple M4 Max |
| cores | 16 physical, 16 logical |
| RAM | 48 GiB |
| storage | File System Personality: APFS; Protocol: Apple Fabric; Solid State: Yes |
| Docker | server 29.7.2, VM Docker Desktop kernel 6.12.76-linuxkit, 16 CPUs, 8216018944 bytes RAM, cgroup v2, storage overlay2 |
| PostgreSQL storage | named Docker volume provbench-pgdata inside the Docker VM (not a bind mount): pg_test_fsync measures the VM's disk |
| pg_test_fsync | fdatasync 10918.572 ops/sec 92 usecs/op (full output: results/pg_test_fsync-Mac.txt) |
| PostgreSQL | PostgreSQL 16.15 (Debian 16.15-1.pgdg13+2) on aarch64-unknown-linux-gnu, compiled by gcc (Debian 14.2.0-19) 14.2.0, 64-bit |
| PostgreSQL settings (non-default) | dynamic_shared_memory_type=posix, listen_addresses=*, max_connections=200, max_wal_size=1024MB, min_wal_size=80MB, pg_stat_statements.max=10000, shared_buffers=1310728kB, shared_preload_libraries=pg_stat_statements, track_io_timing=on |
| ProvToolbox | bench/service-throughput fa0551de5ef892147ec20c3f6e3efd7f561051b8 |
| Java (service and harness) | openjdk version "19.0.1" 2022-10-18 / OpenJDK Runtime Environment Temurin-19.0.1+10 (build 19.0.1+10) / OpenJDK 64-Bit Server VM Temurin-19.0.1+10 (build 19.0.1+10, mixed mode) |
| service JVM flags | -Xms2g -Xmx2g -XX:+UseG1GC; Jetty max threads 200 (embedded Jetty 11, ServiceLauncher) |
| template library | provenance-templates-library 0.1.0-SNAPSHOT; composite size 3 |
