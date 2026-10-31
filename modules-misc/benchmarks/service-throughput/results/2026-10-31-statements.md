
### m1793433197-01 — fs baseline N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 724 | 6766.6 | 9.346 | 2172 | 86.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 158 | 137.6 | 0.871 | 146 | 1.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2077 | 124.2 | 0.060 | 2077 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10767 | 96.2 | 0.009 | 10767 | 1.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1449 | 95.4 | 0.066 | 1449 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1441 | 90.4 | 0.063 | 1441 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 750 | 61.7 | 0.082 | 750 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 724 | 56.0 | 0.077 | 2172 | 0.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |

### m1793433197-02 — fs noacl N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 743 | 6836.1 | 9.201 | 2229 | 86.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 162 | 147.3 | 0.909 | 145 | 1.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2183 | 123.5 | 0.057 | 2183 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 11066 | 101.8 | 0.009 | 11066 | 1.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1436 | 90.2 | 0.063 | 1436 | 1.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1500 | 89.1 | 0.059 | 1500 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 715 | 54.8 | 0.077 | 715 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 743 | 53.8 | 0.072 | 2229 | 0.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |

### m1793433197-03 — fs acl-index N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 740 | 6953.7 | 9.397 | 2220 | 86.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 169 | 152.3 | 0.901 | 147 | 1.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2123 | 120.5 | 0.057 | 2123 | 1.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10767 | 92.9 | 0.009 | 10767 | 1.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1415 | 91.1 | 0.064 | 1415 | 1.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1451 | 87.6 | 0.060 | 1451 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 740 | 56.0 | 0.076 | 2220 | 0.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 693 | 53.9 | 0.078 | 693 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-04 — fs nohash N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 829 | 7679.2 | 9.263 | 2487 | 86.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 185 | 273.8 | 1.480 | 146 | 3.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2370 | 136.6 | 0.058 | 2370 | 1.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1707 | 110.4 | 0.065 | 1707 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1701 | 103.7 | 0.061 | 1701 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 829 | 69.4 | 0.084 | 2487 | 0.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 867 | 65.3 | 0.075 | 867 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 818 | 64.6 | 0.079 | 818 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-05 — fs hash-header N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 753 | 7019.3 | 9.322 | 2259 | 83.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 186 | 263.0 | 1.414 | 145 | 3.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 68 | 243.9 | 3.586 | 68 | 2.9 | `INSERT INTO activity_log (bucket, kind, count) VALUES ($1, $2, $3) ON CONFLICT (bucket, kind) DO UPDATE SET count = activity_log.count + EXCLUDED.count` |
| 2228 | 135.8 | 0.061 | 2228 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1510 | 102.1 | 0.068 | 1510 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1532 | 98.1 | 0.064 | 1532 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 753 | 62.4 | 0.083 | 2259 | 0.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 773 | 60.5 | 0.078 | 773 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-06 — fs async N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 943 | 8998.0 | 9.542 | 2829 | 84.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 247 | 336.0 | 1.360 | 146 | 3.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2878 | 198.8 | 0.069 | 2878 | 1.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 14123 | 140.0 | 0.010 | 14123 | 1.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1864 | 133.1 | 0.071 | 1864 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1815 | 128.2 | 0.071 | 1815 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 943 | 96.5 | 0.102 | 2829 | 0.9 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 966 | 83.7 | 0.087 | 966 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-07 — fs nokey N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 666 | 6349.4 | 9.534 | 1998 | 85.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 177 | 175.7 | 0.992 | 145 | 2.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1963 | 113.3 | 0.058 | 1963 | 1.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10102 | 90.3 | 0.009 | 10102 | 1.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1367 | 89.2 | 0.065 | 1367 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1393 | 85.5 | 0.061 | 1393 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8134 | 71.5 | 0.009 | 8134 | 1.0 | `INSERT INTO __PROV_GENERATION (entity, entity_rel, activity, activity_rel, template, template_id, rel) VALUES ($1,$2,$3,$4,$5,$6,$7)` |
| 672 | 54.0 | 0.080 | 672 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-08 — fs key-noindex N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 696 | 6666.5 | 9.578 | 2088 | 86.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 166 | 138.9 | 0.837 | 145 | 1.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2049 | 117.8 | 0.058 | 2049 | 1.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10323 | 92.2 | 0.009 | 10323 | 1.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1382 | 87.8 | 0.064 | 1382 | 1.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 717 | 84.6 | 0.118 | 717 | 1.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1377 | 83.6 | 0.061 | 1377 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 696 | 53.1 | 0.076 | 2088 | 0.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |

### m1793433197-09 — fs nojit N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 860 | 240.2 | 0.279 | 2580 | 15.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 180 | 197.8 | 1.099 | 148 | 12.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2606 | 148.1 | 0.057 | 2606 | 9.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1760 | 111.6 | 0.063 | 1760 | 7.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 13068 | 109.8 | 0.008 | 13068 | 7.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1782 | 105.2 | 0.059 | 1782 | 6.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 860 | 74.3 | 0.086 | 2580 | 4.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 929 | 66.9 | 0.072 | 929 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-10 — fs combined N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1325 | 12417.8 | 9.372 | 3975 | 86.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 293 | 588.9 | 2.010 | 147 | 4.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3785 | 213.5 | 0.056 | 3785 | 1.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1325 | 165.4 | 0.125 | 3975 | 1.1 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 2568 | 163.8 | 0.064 | 2568 | 1.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2526 | 151.8 | 0.060 | 2526 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1304 | 101.5 | 0.078 | 1304 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1335 | 98.9 | 0.074 | 1335 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-11 — fs baseline N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3860 | 44910.9 | 11.635 | 11580 | 84.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3860 | 1415.1 | 0.367 | 11580 | 2.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11554 | 940.1 | 0.081 | 11554 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 57585 | 712.2 | 0.012 | 57585 | 1.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7701 | 681.8 | 0.089 | 7701 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7766 | 633.2 | 0.082 | 7766 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3758 | 395.9 | 0.105 | 3758 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 150 | 390.3 | 2.602 | 150 | 0.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### m1793433197-12 — fs noacl N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3869 | 44662.6 | 11.544 | 11607 | 85.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3869 | 1435.7 | 0.371 | 11607 | 2.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 11746 | 903.4 | 0.077 | 11746 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 58311 | 708.8 | 0.012 | 58311 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7832 | 656.6 | 0.084 | 7832 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7860 | 634.1 | 0.081 | 7860 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3909 | 403.8 | 0.103 | 3909 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 150 | 401.2 | 2.675 | 150 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### m1793433197-13 — fs nohash N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3958 | 45831.0 | 11.579 | 11874 | 86.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3958 | 1496.6 | 0.378 | 11874 | 2.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11935 | 938.9 | 0.079 | 11935 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 8006 | 680.8 | 0.085 | 8006 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8035 | 656.7 | 0.082 | 8035 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4069 | 421.9 | 0.104 | 4069 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3933 | 388.3 | 0.099 | 3933 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 144 | 378.0 | 2.625 | 144 | 0.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### m1793433197-14 — fs async N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4699 | 58118.3 | 12.368 | 14097 | 83.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 299 | 2366.7 | 7.915 | 150 | 3.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 4699 | 2212.0 | 0.471 | 14097 | 3.2 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 14005 | 1139.2 | 0.081 | 14005 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 9330 | 829.5 | 0.089 | 9330 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 70262 | 786.7 | 0.011 | 70262 | 1.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9299 | 779.3 | 0.084 | 9299 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4662 | 505.6 | 0.108 | 4662 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-15 — fs nokey N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3784 | 44317.6 | 11.712 | 11352 | 85.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3784 | 1514.2 | 0.400 | 11352 | 2.9 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11589 | 830.8 | 0.072 | 11589 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 57092 | 626.9 | 0.011 | 57092 | 1.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7609 | 619.6 | 0.081 | 7609 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7666 | 582.4 | 0.076 | 7666 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 152 | 432.7 | 2.847 | 152 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3758 | 363.5 | 0.097 | 3758 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-16 — fs nojit N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4627 | 2012.0 | 0.435 | 13881 | 18.4 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 4627 | 1610.2 | 0.348 | 13881 | 14.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 13485 | 990.8 | 0.073 | 13485 | 9.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 9148 | 749.8 | 0.082 | 9148 | 6.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 68757 | 723.2 | 0.011 | 68757 | 6.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9241 | 722.4 | 0.078 | 9241 | 6.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 151 | 504.2 | 3.339 | 151 | 4.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 4569 | 450.1 | 0.099 | 4569 | 4.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-17 — fs combined N=8 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5111 | 65343.8 | 12.785 | 15333 | 83.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5111 | 3113.2 | 0.609 | 15333 | 4.0 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 299 | 3005.2 | 10.051 | 150 | 3.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 15617 | 1242.4 | 0.080 | 15617 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10431 | 925.6 | 0.089 | 10431 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10366 | 864.4 | 0.083 | 10366 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5188 | 563.6 | 0.109 | 5188 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5211 | 523.2 | 0.100 | 5211 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-18 — fs baseline N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 710 | 6712.2 | 9.454 | 2130 | 86.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2106 | 133.5 | 0.063 | 2106 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1456 | 101.4 | 0.070 | 1456 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10538 | 96.9 | 0.009 | 10538 | 1.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1363 | 90.7 | 0.067 | 1363 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 147 | 71.9 | 0.489 | 147 | 0.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 695 | 60.5 | 0.087 | 695 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 718 | 57.7 | 0.080 | 718 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-19 — fs baseline N=2 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1455 | 14544.6 | 9.996 | 4365 | 86.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4364 | 294.2 | 0.067 | 4364 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1455 | 219.5 | 0.151 | 4365 | 1.3 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 21598 | 216.3 | 0.010 | 21598 | 1.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 2876 | 212.7 | 0.074 | 2876 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2833 | 200.5 | 0.071 | 2833 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 149 | 141.3 | 0.948 | 149 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1469 | 128.3 | 0.087 | 1469 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-20 — fs baseline N=4 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2398 | 25512.1 | 10.639 | 7194 | 85.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2398 | 556.5 | 0.232 | 7194 | 1.9 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 7359 | 535.6 | 0.073 | 7359 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 36486 | 404.6 | 0.011 | 36486 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 4920 | 391.4 | 0.080 | 4920 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4934 | 371.4 | 0.075 | 4934 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 150 | 250.9 | 1.673 | 150 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2390 | 232.7 | 0.097 | 2390 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-21 — fs baseline N=8 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3730 | 43811.3 | 11.746 | 11190 | 84.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3730 | 1394.6 | 0.374 | 11190 | 2.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11180 | 905.6 | 0.081 | 11180 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 56150 | 710.1 | 0.013 | 56150 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7641 | 667.7 | 0.087 | 7641 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7462 | 621.7 | 0.083 | 7462 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3651 | 395.5 | 0.108 | 3651 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 150 | 392.6 | 2.618 | 150 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### m1793433197-22 — fs baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5043 | 62637.5 | 12.421 | 15129 | 82.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5043 | 2635.0 | 0.523 | 15129 | 3.5 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 15481 | 1501.7 | 0.097 | 15481 | 2.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 76035 | 1226.2 | 0.016 | 76035 | 1.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 10126 | 1047.1 | 0.103 | 10126 | 1.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9946 | 995.8 | 0.100 | 9946 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5037 | 638.3 | 0.127 | 5037 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5055 | 604.5 | 0.120 | 5055 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-23 — fs baseline N=32 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4932 | 62292.2 | 12.630 | 14796 | 82.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4932 | 2661.2 | 0.540 | 14796 | 3.5 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 14983 | 1540.0 | 0.103 | 14983 | 2.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 74071 | 1181.2 | 0.016 | 74071 | 1.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9891 | 1070.8 | 0.108 | 9891 | 1.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9837 | 1020.3 | 0.104 | 9837 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 150 | 625.3 | 4.169 | 150 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 4814 | 619.9 | 0.129 | 4814 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-24 — fs combined N=1 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1044 | 10201.5 | 9.772 | 3132 | 85.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 293 | 489.3 | 1.670 | 147 | 4.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3254 | 208.4 | 0.064 | 3254 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2170 | 155.3 | 0.072 | 2170 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2200 | 148.7 | 0.068 | 2200 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1044 | 113.4 | 0.109 | 3132 | 0.9 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1168 | 96.4 | 0.082 | 1168 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1086 | 95.9 | 0.088 | 1086 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-25 — fs combined N=2 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2043 | 20866.2 | 10.214 | 6129 | 85.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 297 | 889.6 | 2.995 | 149 | 3.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 6100 | 397.1 | 0.065 | 6100 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2043 | 381.5 | 0.187 | 6129 | 1.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 4082 | 300.4 | 0.074 | 4082 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4054 | 280.2 | 0.069 | 4054 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1955 | 174.5 | 0.089 | 1955 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 2027 | 171.4 | 0.085 | 2027 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-26 — fs combined N=4 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3484 | 37949.6 | 10.893 | 10452 | 85.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 299 | 1651.3 | 5.523 | 150 | 3.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3484 | 1147.1 | 0.329 | 10452 | 2.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 10328 | 700.8 | 0.068 | 10328 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 6827 | 522.5 | 0.077 | 6827 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 6826 | 486.7 | 0.071 | 6826 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3390 | 322.2 | 0.095 | 3390 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3407 | 300.9 | 0.088 | 3407 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-27 — fs combined N=8 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5642 | 65067.6 | 11.533 | 16926 | 84.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5642 | 2941.3 | 0.521 | 16926 | 3.8 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 299 | 2760.3 | 9.232 | 150 | 3.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 17321 | 1216.7 | 0.070 | 17321 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 11655 | 919.3 | 0.079 | 11655 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 11565 | 861.8 | 0.075 | 11565 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5708 | 553.1 | 0.097 | 5708 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5726 | 520.0 | 0.091 | 5726 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-28 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 8074 | 97205.3 | 12.039 | 24222 | 81.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 8074 | 6674.1 | 0.827 | 24222 | 5.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 299 | 4196.3 | 14.035 | 150 | 3.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 23697 | 2091.2 | 0.088 | 23697 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 15959 | 1567.7 | 0.098 | 15959 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 16223 | 1491.9 | 0.092 | 16223 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8095 | 953.5 | 0.118 | 8095 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 8010 | 885.8 | 0.111 | 8010 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-29 — fs combined N=32 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7797 | 100982.3 | 12.951 | 23391 | 81.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 7797 | 6773.8 | 0.869 | 23391 | 5.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 302 | 4589.0 | 15.195 | 152 | 3.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 23220 | 2202.8 | 0.095 | 23220 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 15385 | 1617.8 | 0.105 | 15385 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 15572 | 1530.0 | 0.098 | 15572 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 7770 | 965.5 | 0.124 | 7770 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7517 | 898.3 | 0.120 | 7517 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-30 — fs baseline N=16 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 887 | 8625.7 | 9.725 | 2661 | 55.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4413 | 5722.7 | 1.297 | 8 | 36.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 13085 | 164.0 | 0.013 | 13085 | 1.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 2539 | 161.9 | 0.064 | 2539 | 1.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1710 | 120.5 | 0.070 | 1710 | 0.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1747 | 116.9 | 0.067 | 1747 | 0.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 887 | 82.4 | 0.093 | 2661 | 0.5 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 855 | 73.3 | 0.086 | 855 | 0.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-31 — fs baseline N=16 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2320 | 25530.2 | 11.004 | 6960 | 84.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 7115 | 566.3 | 0.080 | 7115 | 1.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2320 | 535.5 | 0.231 | 6960 | 1.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 35362 | 433.2 | 0.012 | 35362 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 4718 | 405.6 | 0.086 | 4718 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4699 | 390.8 | 0.083 | 4699 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 2357 | 252.3 | 0.107 | 2357 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 2402 | 237.7 | 0.099 | 2402 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-32 — fs baseline N=16 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3752 | 44098.1 | 11.753 | 11256 | 84.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3752 | 1447.5 | 0.386 | 11256 | 2.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11312 | 941.9 | 0.083 | 11312 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 56516 | 731.3 | 0.013 | 56516 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7547 | 688.2 | 0.091 | 7547 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7527 | 654.4 | 0.087 | 7527 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3822 | 423.0 | 0.111 | 3822 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 150 | 401.7 | 2.678 | 150 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |

### m1793433197-33 — fs baseline N=16 B=1 POOL=32

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5027 | 61163.8 | 12.167 | 15081 | 82.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5027 | 2608.7 | 0.519 | 15081 | 3.5 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 15269 | 1503.0 | 0.098 | 15269 | 2.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 75958 | 1203.3 | 0.016 | 75958 | 1.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 10039 | 1053.8 | 0.105 | 10039 | 1.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10163 | 1012.3 | 0.100 | 10163 | 1.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5021 | 628.0 | 0.125 | 5021 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5078 | 601.2 | 0.118 | 5078 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-34 — fs combined N=16 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1525 | 14155.7 | 9.282 | 4575 | 85.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 295 | 659.3 | 2.235 | 148 | 4.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 4814 | 278.4 | 0.058 | 4814 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1525 | 211.4 | 0.139 | 4575 | 1.3 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 3110 | 205.4 | 0.066 | 3110 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3199 | 195.1 | 0.061 | 3199 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1581 | 125.7 | 0.079 | 1581 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1487 | 111.6 | 0.075 | 1487 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-35 — fs combined N=16 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3589 | 38912.7 | 10.842 | 10767 | 85.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 299 | 1646.7 | 5.507 | 150 | 3.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3589 | 1168.1 | 0.325 | 10767 | 2.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 10651 | 723.2 | 0.068 | 10651 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 6992 | 531.4 | 0.076 | 6992 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7005 | 507.9 | 0.073 | 7005 | 1.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3578 | 335.0 | 0.094 | 3578 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3536 | 308.9 | 0.087 | 3536 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-36 — fs combined N=16 B=1 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5459 | 64528.6 | 11.821 | 16377 | 84.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5459 | 2919.6 | 0.535 | 16377 | 3.8 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 299 | 2702.9 | 9.040 | 150 | 3.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 16678 | 1236.1 | 0.074 | 16678 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 11242 | 934.6 | 0.083 | 11242 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 11251 | 887.9 | 0.079 | 11251 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5655 | 602.5 | 0.107 | 5655 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5679 | 541.1 | 0.095 | 5679 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-37 — fs combined N=16 B=1 POOL=32

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 8222 | 98532.6 | 11.984 | 24666 | 81.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 8222 | 6977.1 | 0.849 | 24666 | 5.8 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 302 | 4364.4 | 14.452 | 152 | 3.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 24528 | 2096.0 | 0.085 | 24528 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 16477 | 1578.2 | 0.096 | 16477 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 16387 | 1471.2 | 0.090 | 16387 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8208 | 971.6 | 0.118 | 8208 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 8250 | 885.6 | 0.107 | 8250 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-38 — fs baseline N=8 B=10 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4130 | 45870.5 | 11.107 | 12390 | 85.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4130 | 1616.7 | 0.391 | 12390 | 3.0 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 12490 | 842.7 | 0.067 | 12490 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 61360 | 618.8 | 0.010 | 61360 | 1.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 8030 | 593.0 | 0.074 | 8030 | 1.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7930 | 557.4 | 0.070 | 7930 | 1.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 145 | 407.6 | 2.811 | 145 | 0.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 4250 | 377.8 | 0.089 | 4250 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-39 — fs baseline N=8 B=100 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3400 | 37990.2 | 11.174 | 10200 | 83.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3400 | 1104.6 | 0.325 | 10200 | 2.4 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 16100 | 1023.1 | 0.064 | 16100 | 2.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 9600 | 670.2 | 0.070 | 9600 | 1.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 65000 | 629.7 | 0.010 | 65000 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 8900 | 592.4 | 0.067 | 8900 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4600 | 392.2 | 0.085 | 4600 | 0.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4500 | 366.5 | 0.081 | 4500 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-40 — fs combined N=8 B=10 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 6130 | 69575.5 | 11.350 | 18390 | 86.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 6130 | 3433.8 | 0.560 | 18390 | 4.3 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 18990 | 1273.7 | 0.067 | 18990 | 1.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 12180 | 908.7 | 0.075 | 12180 | 1.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 11810 | 838.4 | 0.071 | 11810 | 1.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 147 | 587.1 | 3.994 | 147 | 0.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 6340 | 569.1 | 0.090 | 6340 | 0.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 6050 | 515.0 | 0.085 | 6050 | 0.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-41 — fs combined N=8 B=100 POOL=8

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5800 | 64387.9 | 11.101 | 17400 | 85.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5800 | 3429.5 | 0.591 | 17400 | 4.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 19800 | 1275.2 | 0.064 | 19800 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 13900 | 947.4 | 0.068 | 13900 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 12700 | 914.6 | 0.072 | 12700 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 135 | 643.1 | 4.764 | 135 | 0.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 7700 | 622.9 | 0.081 | 7700 | 0.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 6900 | 589.6 | 0.085 | 6900 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### m1793433197-42 — tr baseline N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 445 | 4175.7 | 9.384 | 1335 | 48.1 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 426 | 3528.3 | 8.282 | 1278 | 40.7 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 169 | 202.4 | 1.198 | 144 | 2.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1315 | 81.0 | 0.062 | 1315 | 0.9 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8849 | 76.6 | 0.009 | 8849 | 0.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 922 | 60.8 | 0.066 | 922 | 0.7 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 890 | 57.1 | 0.064 | 890 | 0.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 882 | 54.0 | 0.061 | 882 | 0.6 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |

### m1793433197-43 — tr combined N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 809 | 7537.6 | 9.317 | 2427 | 45.6 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 847 | 6908.8 | 8.157 | 2541 | 41.8 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 292 | 880.9 | 3.017 | 146 | 5.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2553 | 151.5 | 0.059 | 2553 | 0.9 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1735 | 111.1 | 0.064 | 1735 | 0.7 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1671 | 100.7 | 0.060 | 1671 | 0.6 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 1643 | 96.4 | 0.059 | 1643 | 0.6 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 847 | 68.6 | 0.081 | 2541 | 0.4 | `SELECT * FROM (SELECT template.id AS key FROM unpacking_composite AS template WHERE template.id=$1) as combo LEFT JOIN unpacking_composite_linker AS linker ` |

### m1793433197-44 — tr baseline N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3714 | 45539.7 | 12.262 | 11142 | 46.0 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 3817 | 40376.8 | 10.578 | 11451 | 40.8 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 3817 | 1525.8 | 0.400 | 11451 | 1.5 | `SELECT * FROM (SELECT * FROM unpacking_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 AND recor` |
| 3714 | 1453.6 | 0.391 | 11142 | 1.5 | `SELECT * FROM (SELECT * FROM packing_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 AND record_` |
| 11149 | 1052.9 | 0.094 | 11149 | 1.1 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 75005 | 1052.4 | 0.014 | 75005 | 1.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7497 | 746.9 | 0.100 | 7497 | 0.8 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7571 | 719.7 | 0.095 | 7571 | 0.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |

### m1793433197-45 — tr combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5377 | 64277.1 | 11.954 | 16131 | 44.0 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 5609 | 59077.2 | 10.533 | 16827 | 40.4 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 285 | 6606.3 | 23.180 | 142 | 4.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 5609 | 3500.2 | 0.624 | 16827 | 2.4 | `SELECT * FROM (SELECT template.id AS key FROM unpacking_composite AS template WHERE template.id=$1) as combo LEFT JOIN unpacking_composite_linker AS linker ` |
| 5377 | 3300.7 | 0.614 | 16131 | 2.3 | `SELECT * FROM (SELECT template.id AS key FROM packing_composite AS template WHERE template.id=$1) as combo LEFT JOIN packing_composite_linker AS linker ON ` |
| 16264 | 1560.6 | 0.096 | 16264 | 1.1 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10918 | 1099.2 | 0.101 | 10918 | 0.8 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10994 | 1065.6 | 0.097 | 10994 | 0.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |

### m1793433197-46 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 8693 | 103849.8 | 11.946 | 26079 | 82.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 8693 | 7714.5 | 0.887 | 26079 | 6.1 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 298 | 4221.7 | 14.167 | 150 | 3.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 25678 | 2152.3 | 0.084 | 25678 | 1.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 17104 | 1570.4 | 0.092 | 17104 | 1.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 17096 | 1485.3 | 0.087 | 17096 | 1.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8499 | 954.9 | 0.112 | 8499 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 8691 | 910.1 | 0.105 | 8691 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433197-47 — fs combined N=16 B=1 POOL=16

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7719 | 96385.4 | 12.487 | 23157 | 81.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 7719 | 6483.4 | 0.840 | 23157 | 5.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 301 | 4262.8 | 14.162 | 151 | 3.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 22842 | 2121.2 | 0.093 | 22842 | 1.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 15393 | 1583.0 | 0.103 | 15393 | 1.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 15467 | 1503.3 | 0.097 | 15467 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 7591 | 942.6 | 0.124 | 7591 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7591 | 877.3 | 0.116 | 7591 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
