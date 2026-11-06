
### r1793958860 — fs baseline N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 749 | 302.4 | 0.404 | 2247 | 17.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2240 | 185.4 | 0.083 | 2240 | 10.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 11079 | 139.4 | 0.013 | 11079 | 8.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1492 | 131.0 | 0.088 | 1492 | 7.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1458 | 130.8 | 0.090 | 1458 | 7.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 754 | 88.1 | 0.117 | 754 | 5.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 148 | 86.4 | 0.584 | 148 | 5.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 732 | 77.0 | 0.105 | 732 | 4.5 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### r1793958909 — fs combined N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 299 | 746.4 | 2.496 | 150 | 21.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1711 | 564.0 | 0.330 | 5133 | 16.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4887 | 315.8 | 0.065 | 4887 | 9.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1711 | 257.9 | 0.151 | 5133 | 7.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 3267 | 242.5 | 0.074 | 3267 | 7.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3225 | 224.9 | 0.070 | 3225 | 6.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1653 | 148.7 | 0.090 | 1653 | 4.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1655 | 147.0 | 0.089 | 1655 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793959228 — fs baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4913 | 2676.0 | 0.545 | 14739 | 16.2 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 4913 | 2351.0 | 0.479 | 14739 | 14.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 14782 | 1652.5 | 0.112 | 14782 | 10.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 73888 | 1236.2 | 0.017 | 73888 | 7.5 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9907 | 1127.7 | 0.114 | 9907 | 6.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9712 | 1056.4 | 0.109 | 9712 | 6.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4924 | 666.7 | 0.135 | 4924 | 4.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4882 | 656.5 | 0.134 | 4882 | 4.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793959278 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 6622 | 5711.7 | 0.863 | 19866 | 22.2 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 302 | 4519.8 | 14.966 | 153 | 17.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 6622 | 3438.3 | 0.519 | 19866 | 13.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 19486 | 2363.3 | 0.121 | 19486 | 9.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 12903 | 1720.4 | 0.133 | 12903 | 6.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 13116 | 1695.2 | 0.129 | 13116 | 6.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 6447 | 991.2 | 0.154 | 6447 | 3.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 6318 | 945.1 | 0.150 | 6318 | 3.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793959595 — fs baseline N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 688 | 263.6 | 0.383 | 2064 | 15.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2104 | 193.0 | 0.092 | 2104 | 11.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10368 | 153.4 | 0.015 | 10368 | 9.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1405 | 138.0 | 0.098 | 1405 | 8.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1340 | 131.6 | 0.098 | 1340 | 7.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 680 | 82.2 | 0.121 | 680 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 147 | 77.1 | 0.524 | 147 | 4.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 666 | 72.7 | 0.109 | 666 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793959646 — fs combined N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 296 | 681.2 | 2.301 | 149 | 21.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1456 | 505.9 | 0.347 | 4368 | 15.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4390 | 322.1 | 0.073 | 4390 | 9.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2998 | 242.9 | 0.081 | 2998 | 7.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3044 | 235.4 | 0.077 | 3044 | 7.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1456 | 198.0 | 0.136 | 4368 | 6.1 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1452 | 143.9 | 0.099 | 1452 | 4.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1432 | 134.4 | 0.094 | 1432 | 4.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793959964 — fs baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4874 | 2717.7 | 0.558 | 14622 | 16.4 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 4874 | 2314.4 | 0.475 | 14622 | 14.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 14781 | 1616.3 | 0.109 | 14781 | 9.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 73305 | 1265.9 | 0.017 | 73305 | 7.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9830 | 1124.6 | 0.114 | 9830 | 6.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9648 | 1078.4 | 0.112 | 9648 | 6.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4951 | 689.7 | 0.139 | 4951 | 4.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4892 | 636.9 | 0.130 | 4892 | 3.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793960013 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 9757 | 9712.2 | 0.995 | 29271 | 31.2 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 303 | 5020.8 | 16.570 | 154 | 16.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 9757 | 3775.1 | 0.387 | 29271 | 12.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 28635 | 2544.7 | 0.089 | 28635 | 8.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 18931 | 1834.6 | 0.097 | 18931 | 5.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 19077 | 1733.2 | 0.091 | 19077 | 5.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 9551 | 1153.8 | 0.121 | 9551 | 3.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 9672 | 1060.7 | 0.110 | 9672 | 3.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793960329 — fs baseline N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 796 | 272.2 | 0.342 | 2388 | 16.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2569 | 188.7 | 0.073 | 2569 | 11.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1749 | 141.1 | 0.081 | 1749 | 8.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 12411 | 128.9 | 0.010 | 12411 | 7.8 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1642 | 128.7 | 0.078 | 1642 | 7.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 148 | 82.9 | 0.560 | 148 | 5.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 847 | 82.5 | 0.097 | 847 | 5.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 796 | 71.1 | 0.089 | 2388 | 4.3 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |

### r1793960379 — fs combined N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 299 | 770.6 | 2.577 | 150 | 22.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1666 | 526.2 | 0.316 | 4998 | 15.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5262 | 322.0 | 0.061 | 5262 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1666 | 242.5 | 0.146 | 4998 | 7.2 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 3478 | 242.4 | 0.070 | 3478 | 7.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3497 | 226.8 | 0.065 | 3497 | 6.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1822 | 155.0 | 0.085 | 1822 | 4.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1801 | 144.9 | 0.080 | 1801 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793960699 — fs baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4677 | 3038.8 | 0.650 | 14031 | 18.0 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 4677 | 2480.3 | 0.530 | 14031 | 14.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 13997 | 1553.5 | 0.111 | 13997 | 9.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 69685 | 1180.0 | 0.017 | 69685 | 7.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9256 | 1130.0 | 0.122 | 9256 | 6.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9239 | 1086.7 | 0.118 | 9239 | 6.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4591 | 712.5 | 0.155 | 4591 | 4.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 154 | 657.1 | 4.267 | 154 | 3.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### r1793960749 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7122 | 5936.4 | 0.834 | 21366 | 22.9 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 304 | 4141.6 | 13.624 | 155 | 16.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 7122 | 3672.7 | 0.516 | 21366 | 14.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 21599 | 2475.4 | 0.115 | 21599 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 14135 | 1748.5 | 0.124 | 14135 | 6.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 14266 | 1718.3 | 0.120 | 14266 | 6.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 7064 | 1061.8 | 0.150 | 7064 | 4.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7181 | 988.2 | 0.138 | 7181 | 3.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793961066 — fs baseline N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 670 | 257.3 | 0.384 | 2010 | 17.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2044 | 159.7 | 0.078 | 2044 | 11.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10140 | 119.5 | 0.012 | 10140 | 8.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1436 | 119.3 | 0.083 | 1436 | 8.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1357 | 118.3 | 0.087 | 1357 | 8.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 148 | 68.3 | 0.461 | 148 | 4.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 702 | 67.6 | 0.096 | 702 | 4.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 620 | 67.3 | 0.109 | 620 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### r1793961116 — fs combined N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 296 | 737.5 | 2.491 | 149 | 22.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1488 | 517.2 | 0.348 | 4464 | 15.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4443 | 327.5 | 0.074 | 4443 | 9.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2852 | 236.3 | 0.083 | 2852 | 7.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2803 | 228.6 | 0.082 | 2803 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1488 | 217.3 | 0.146 | 4464 | 6.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1489 | 142.4 | 0.096 | 1489 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1446 | 139.4 | 0.096 | 1446 | 4.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### r1793961435 — fs baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4512 | 2611.3 | 0.579 | 13536 | 15.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 4512 | 2427.4 | 0.538 | 13536 | 14.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 13528 | 1559.4 | 0.115 | 13528 | 9.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 67549 | 1281.2 | 0.019 | 67549 | 7.7 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 8946 | 1100.0 | 0.123 | 8946 | 6.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8971 | 1057.1 | 0.118 | 8971 | 6.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4551 | 655.8 | 0.144 | 4551 | 4.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4486 | 637.0 | 0.142 | 4486 | 3.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793961485 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 8548 | 8210.9 | 0.961 | 25644 | 27.9 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 304 | 5116.4 | 16.830 | 155 | 17.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 8548 | 3786.3 | 0.443 | 25644 | 12.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 25503 | 2424.3 | 0.095 | 25503 | 8.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 17286 | 1835.7 | 0.106 | 17286 | 6.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 16999 | 1689.9 | 0.099 | 16999 | 5.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8562 | 1088.9 | 0.127 | 8562 | 3.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 8416 | 984.8 | 0.117 | 8416 | 3.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793961802 — fs baseline N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 667 | 273.6 | 0.410 | 2001 | 16.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2058 | 181.8 | 0.088 | 2058 | 11.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10255 | 143.4 | 0.014 | 10255 | 8.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1361 | 130.5 | 0.096 | 1361 | 7.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1345 | 127.6 | 0.095 | 1345 | 7.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 720 | 82.3 | 0.114 | 720 | 5.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 739 | 79.2 | 0.107 | 739 | 4.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 148 | 79.1 | 0.535 | 148 | 4.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### r1793961852 — fs combined N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1496 | 522.3 | 0.349 | 4488 | 16.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 295 | 514.9 | 1.746 | 148 | 16.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 4441 | 318.1 | 0.072 | 4441 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2861 | 233.9 | 0.082 | 2861 | 7.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2932 | 221.4 | 0.076 | 2932 | 7.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1496 | 163.5 | 0.109 | 4488 | 5.3 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1522 | 146.8 | 0.096 | 1522 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1455 | 133.5 | 0.092 | 1455 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793962171 — fs baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5490 | 3037.6 | 0.553 | 16470 | 18.1 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 5490 | 2340.6 | 0.426 | 16470 | 13.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 16565 | 1600.4 | 0.097 | 16565 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 82956 | 1181.2 | 0.014 | 82956 | 7.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 11308 | 1172.6 | 0.104 | 11308 | 7.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 11030 | 1092.3 | 0.099 | 11030 | 6.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5464 | 682.9 | 0.125 | 5464 | 4.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5612 | 655.0 | 0.117 | 5612 | 3.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### r1793962221 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 9480 | 8725.3 | 0.920 | 28440 | 29.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 309 | 4934.4 | 15.969 | 159 | 16.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 9480 | 3610.5 | 0.381 | 28440 | 12.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 28885 | 2446.6 | 0.085 | 28885 | 8.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 19536 | 1837.6 | 0.094 | 19536 | 6.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 19301 | 1696.6 | 0.088 | 19301 | 5.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 9713 | 1112.9 | 0.115 | 9713 | 3.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 9771 | 1038.7 | 0.106 | 9771 | 3.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
