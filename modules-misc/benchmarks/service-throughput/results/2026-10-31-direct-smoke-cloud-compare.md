# Direct JDBC mode against HTTP, 2026-10-31-direct-smoke-cloud

Direct: 2026-10-31-direct-smoke-cloud.csv. HTTP: 2026-10-31-smoke-cloud-jitfix.csv 

Pairs: direct post+key ~ HTTP baseline with its key (B=1), direct post ~ HTTP nokey or keyless baseline (B>1), direct combined ~ HTTP combined; same family, N and B; HTTP runs with POOL >= N only. The service's share = 1 - HTTP/direct records/s.

| fam | direct | HTTP | N | B | direct records/s | HTTP records/s | HTTP/direct | direct rows/rec | HTTP rows/rec | direct PG ms/rec | HTTP PG ms/rec | direct p50 ms/tx | HTTP p50 ms/msg | HTTP svc ms/rec | direct client ms/rec | HTTP source |
|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| fs | post+key | baseline | 2 | 1 | 288.4 | 145.2 | 0.50 | 8.96 | 8.42 | 2.956 | 3.459 | 7.36 | 14.78 | 10.413 | 3.9 | 2026-10-31-smoke-cloud-jitfix.csv |
| fs | combined | combined | 2 | 1 | 419.7 | 179.0 | 0.43 | 7.96 | 7.33 | 2.359 | 2.818 | 5.19 | 12.73 | 8.049 | 2.905 | 2026-10-31-smoke-cloud-jitfix.csv |
| tr | post+key | baseline | 2 | 1 | 328.8 | 144.1 | 0.44 | 13.21 | 12.36 | 3.045 | 3.661 | 6.83 | 16.79 | 9.983 | 1.223 | 2026-10-31-smoke-cloud-jitfix.csv |
| tr | combined | combined | 2 | 1 | 559.7 | 206.3 | 0.37 | 12.23 | 11.25 | 2.216 | 2.768 | 4.26 | 12.43 | 6.051 | 0.871 | 2026-10-31-smoke-cloud-jitfix.csv |
| fs | post | baseline | 2 | 10 | 458.5 | 237.3 | 0.52 | 9.0 | 9.51 | 2.299 | 3.083 | 44.6 | 93.98 | 4.372 | 1.092 | 2026-10-31-smoke-cloud-jitfix.csv |
