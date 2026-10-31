# Direct JDBC mode against HTTP, 2026-10-31-direct

Direct: 2026-10-31-direct.csv. HTTP: 2026-10-31.csv 2026-10-31-jitfix.csv 2026-10-31-postfix.csv 

Pairs: direct post+key ~ HTTP baseline with its key (B=1), direct post ~ HTTP nokey or keyless baseline (B>1), direct combined ~ HTTP combined; same family, N and B; HTTP runs with POOL >= N only; the default relation writers only (runs varying WRITERS left out); smoke reports left out. The service's share = 1 - HTTP/direct records/s.

| fam | direct | HTTP | N | B | direct records/s | HTTP records/s | HTTP/direct | direct rows/rec | HTTP rows/rec | direct PG ms/rec | HTTP PG ms/rec | direct p50 ms/tx | HTTP p50 ms/msg | HTTP svc ms/rec | direct client ms/rec | HTTP source |
|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| fs | post+key | baseline | 1 | 1 | 509.1 | 491.6 | 0.97 | 9.01 | 9.04 | 0.52 | 0.537 | 2.01 | 2.06 | 0.432 | 0.227 | 2026-10-31-postfix.csv |
| fs | combined | combined | 1 | 1 | 932.1 | 883.2 | 0.95 | 7.99 | 7.97 | 0.441 | 0.46 | 1.14 | 1.21 | 0.265 | 0.169 | 2026-10-31-postfix.csv |
| fs | post+key | baseline | 2 | 1 | 965.1 | 993.2 | 1.03 | 8.99 | 9.01 | 0.54 | 0.526 | 2.1 | 2.14 | 0.249 | 0.177 | 2026-10-31-postfix.csv |
| fs | post+key | baseline | 4 | 1 | 1404.3 | 1541.3 | 1.10 | 9.01 | 9.01 | 0.646 | 0.601 | 2.83 | 2.73 | 0.254 | 0.191 | 2026-10-31-postfix.csv |
| fs | post+key | baseline | 8 | 1 | 2294.3 | 2305.9 | 1.01 | 9.0 | 9.02 | 0.674 | 0.662 | 3.65 | 3.7 | 0.26 | 0.181 | 2026-10-31-postfix.csv |
| fs | post+key | baseline | 16 | 1 | 2227.5 | 2889.7 | 1.30 | 9.03 | 9.01 | 0.787 | 0.838 | 9.28 | 5.92 | 0.31 | 0.208 | 2026-10-31-postfix.csv |
| fs | post+key | baseline | 32 | 1 | 3767.7 | 3795.7 | 1.01 | 9.01 | 9.04 | 1.018 | 0.96 | 8.65 | 9.03 | 0.317 | 0.213 | 2026-10-31-jitfix.csv |
| fs | combined | combined | 2 | 1 | 1738.9 | 1546.5 | 0.89 | 8.01 | 8.03 | 0.427 | 0.469 | 1.25 | 1.38 | 0.213 | 0.125 | 2026-10-31-postfix.csv |
| fs | combined | combined | 4 | 1 | 2676.6 | 2204.0 | 0.82 | 8.0 | 7.99 | 0.444 | 0.523 | 1.63 | 1.88 | 0.222 | 0.114 | 2026-10-31-postfix.csv |
| fs | combined | combined | 8 | 1 | 3330.0 | 3653.0 | 1.10 | 8.01 | 7.98 | 0.52 | 0.492 | 2.36 | 2.37 | 0.183 | 0.129 | 2026-10-31-postfix.csv |
| fs | combined | combined | 16 | 1 | 5496.4 | 4020.2 | 0.73 | 8.0 | 8.04 | 0.49 | 0.64 | 3.12 | 4.04 | 0.214 | 0.115 | 2026-10-31-postfix.csv |
| fs | combined | combined | 32 | 1 | 7530.4 | 6579.0 | 0.87 | 8.0 | 8.02 | 0.544 | 0.617 | 4.61 | 5.3 | 0.185 | 0.117 | 2026-10-31-jitfix.csv |
| tr | post+key | baseline | 16 | 1 | 2693.4 | 2897.2 | 1.08 | 13.2 | 13.22 | 0.917 | 0.862 | 6.6 | 6.34 | 0.296 | 0.241 | 2026-10-31-postfix.csv |
| tr | combined | combined | 16 | 1 | 5183.5 | 5024.7 | 0.97 | 12.21 | 12.16 | 0.581 | 0.617 | 3.56 | 3.73 | 0.19 | 0.123 | 2026-10-31-postfix.csv |

## Three ways to write the same records

records/s at the same family, N and B: JSONL (the bindings appended to local files, a force per batch, a file per writer), direct (no web service), HTTP (the service). '' = not run.

| fam | N | B | JSONL | direct insert | direct post+key | direct combined | HTTP baseline | HTTP combined | JSONL B/rec |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| fs | 1 | 1 | 282.9 | 1197.6 | 509.1 | 932.1 | 491.6 | 883.2 | 314 |
| fs | 2 | 1 | 266.5 | 529.6 | 965.1 | 1738.9 | 993.2 | 1546.5 | 314 |
| fs | 4 | 1 | 328.9 | 3201.3 | 1404.3 | 2676.6 | 1541.3 | 2204.0 | 314 |
| fs | 8 | 1 | 434.4 | 6049.7 | 2294.3 | 3330.0 | 2305.9 | 3653.0 | 317 |
| fs | 16 | 1 | 532.6 | 7987.4 | 2227.5 | 5496.4 | 2889.7 | 4020.2 | 316 |
| fs | 32 | 1 | 473.0 | 9371.1 | 3767.7 | 7530.4 | 3795.7 | 6579.0 | 317 |
| fs | 64 | 1 | 521.5 | 12589.0 | 5272.8 | 9363.2 |  |  | 317 |
| fs | 16 | 10 | 5069.2 | 24178.3 | 3586.2 | 8053.9 |  |  | 327 |
| fs | 16 | 100 | 38980.1 | 28786.8 | 3354.2 | 9372.8 |  |  | 334 |
| tr | 1 | 1 | 349.9 | 1160.9 | 444.2 | 762.3 |  |  | 222 |
| tr | 16 | 1 | 512.9 | 7270.7 | 2693.4 | 5183.5 | 2897.2 | 5024.7 | 225 |
