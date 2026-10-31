# Direct JDBC mode against HTTP, 2026-10-31-direct

Direct: 2026-10-31-direct.csv. HTTP: 2026-10-31-jitfix.csv 

Pairs: direct post+key ~ HTTP baseline with its key (B=1), direct post ~ HTTP nokey or keyless baseline (B>1), direct combined ~ HTTP combined; same family, N and B; HTTP runs with POOL >= N only. The service's share = 1 - HTTP/direct records/s.

| fam | direct | HTTP | N | B | direct records/s | HTTP records/s | HTTP/direct | direct rows/rec | HTTP rows/rec | direct PG ms/rec | HTTP PG ms/rec | direct p50 ms/tx | HTTP p50 ms/msg | HTTP svc ms/rec | direct client ms/rec | HTTP source |
|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| fs | post+key | baseline | 8 | 1 | 2294.3 | 2278.1 | 0.99 | 9.0 | 9.02 | 0.674 | 0.672 | 3.65 | 3.72 | 0.261 | 0.181 | 2026-10-31-jitfix.csv |
| fs | post+key | baseline | 16 | 1 | 2227.5 | 2961.0 | 1.33 | 9.03 | 9.01 | 0.787 | 0.818 | 9.28 | 5.78 | 0.296 | 0.208 | 2026-10-31-jitfix.csv |
| fs | post+key | baseline | 32 | 1 | 3767.7 | 3795.7 | 1.01 | 9.01 | 9.04 | 1.018 | 0.96 | 8.65 | 9.03 | 0.317 | 0.213 | 2026-10-31-jitfix.csv |
| fs | combined | combined | 8 | 1 | 3330.0 | 3677.4 | 1.10 | 8.01 | 7.96 | 0.52 | 0.501 | 2.36 | 2.36 | 0.184 | 0.129 | 2026-10-31-jitfix.csv |
| fs | combined | combined | 16 | 1 | 5496.4 | 4907.3 | 0.89 | 8.0 | 8.01 | 0.49 | 0.565 | 3.12 | 3.53 | 0.188 | 0.115 | 2026-10-31-jitfix.csv |
| fs | combined | combined | 32 | 1 | 7530.4 | 6579.0 | 0.87 | 8.0 | 8.02 | 0.544 | 0.617 | 4.61 | 5.3 | 0.185 | 0.117 | 2026-10-31-jitfix.csv |
| tr | post+key | baseline | 16 | 1 | 2693.4 | 2838.6 | 1.05 | 13.2 | 13.28 | 0.917 | 0.889 | 6.6 | 6.47 | 0.309 | 0.241 | 2026-10-31-jitfix.csv |
| tr | combined | combined | 16 | 1 | 5183.5 | 5024.2 | 0.97 | 12.21 | 12.21 | 0.581 | 0.644 | 3.56 | 3.76 | 0.187 | 0.123 | 2026-10-31-jitfix.csv |
| fs | combined | combined | 32 | 1 | 7394.2 | 6579.0 | 0.89 | 7.99 | 8.02 | 0.537 | 0.617 | 4.72 | 5.3 | 0.185 | 0.113 | 2026-10-31-jitfix.csv |
| fs | combined | combined | 32 | 1 | 6300.2 | 6579.0 | 1.04 | 7.98 | 8.02 | 0.63 | 0.617 | 5.33 | 5.3 | 0.185 | 0.131 | 2026-10-31-jitfix.csv |
