
### d1793452398-01 — fs direct insert key=off readback= hash= relations= N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1391 | 398.5 | 0.286 | 4173 | 30.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4288 | 221.8 | 0.052 | 4288 | 16.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2763 | 163.5 | 0.059 | 2763 | 12.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2909 | 160.6 | 0.055 | 2909 | 12.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1428 | 104.8 | 0.073 | 1428 | 8.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1352 | 92.9 | 0.069 | 1352 | 7.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1409 | 76.3 | 0.054 | 1409 | 5.8 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 1357 | 75.2 | 0.055 | 1357 | 5.7 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452398-02 — fs direct post key=on readback=on hash=update relations=inline N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 487 | 155.0 | 0.318 | 1461 | 17.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1625 | 106.6 | 0.066 | 1625 | 12.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 7654 | 75.5 | 0.010 | 7654 | 8.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 978 | 72.4 | 0.074 | 978 | 8.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1034 | 70.1 | 0.068 | 1034 | 8.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 547 | 48.1 | 0.088 | 547 | 5.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 500 | 41.0 | 0.082 | 500 | 4.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 509 | 36.6 | 0.072 | 509 | 4.2 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452398-03 — fs direct post key=off readback=off hash=off relations=async N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 912 | 278.0 | 0.305 | 2736 | 20.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2622 | 153.8 | 0.059 | 2622 | 11.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1711 | 116.3 | 0.068 | 1711 | 8.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 79 | 115.0 | 1.456 | 40 | 8.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1739 | 109.2 | 0.063 | 1739 | 8.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 912 | 82.5 | 0.090 | 2736 | 6.1 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 906 | 73.1 | 0.081 | 906 | 5.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 894 | 69.6 | 0.078 | 894 | 5.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452398-04 — tr direct insert key=off readback= hash= relations= N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1197 | 364.3 | 0.304 | 3591 | 23.3 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 1146 | 339.3 | 0.296 | 3438 | 21.7 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 3600 | 191.8 | 0.053 | 3600 | 12.3 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2426 | 141.1 | 0.058 | 2426 | 9.0 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2403 | 130.3 | 0.054 | 2403 | 8.3 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 2434 | 129.9 | 0.053 | 2434 | 8.3 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 1241 | 95.5 | 0.077 | 1241 | 6.1 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |
| 1219 | 85.7 | 0.070 | 1219 | 5.5 | `WITH insertion_result AS ( select * from insert_packing ($1,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz)) INSERT INTO record_index(key,table_name,principal,su` |

### d1793452398-05 — tr direct post key=on readback=on hash=update relations=inline N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 377 | 138.9 | 0.368 | 1131 | 13.9 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 358 | 133.9 | 0.374 | 1074 | 13.4 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 1039 | 73.1 | 0.070 | 1039 | 7.3 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7316 | 71.1 | 0.010 | 7316 | 7.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 794 | 56.6 | 0.071 | 794 | 5.7 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 707 | 54.8 | 0.078 | 707 | 5.5 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 697 | 51.5 | 0.074 | 697 | 5.2 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 378 | 40.8 | 0.108 | 378 | 4.1 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |

### d1793452398-06 — tr direct post key=off readback=off hash=off relations=async N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 697 | 243.2 | 0.349 | 2091 | 14.5 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 678 | 239.2 | 0.353 | 2034 | 14.3 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 79 | 220.0 | 2.785 | 40 | 13.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2027 | 126.0 | 0.062 | 2027 | 7.5 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1338 | 92.0 | 0.069 | 1338 | 5.5 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1380 | 85.9 | 0.062 | 1380 | 5.1 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 1356 | 85.3 | 0.063 | 1356 | 5.1 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 680 | 59.8 | 0.088 | 680 | 3.6 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |

### d1793452398-07 — fs direct insert key=off readback= hash= relations= N=2 B=10 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5980 | 1318.3 | 0.220 | 17940 | 32.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 17600 | 714.6 | 0.041 | 17600 | 17.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 11190 | 515.0 | 0.046 | 11190 | 12.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10860 | 458.3 | 0.042 | 10860 | 11.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5760 | 311.7 | 0.054 | 5760 | 7.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5650 | 296.0 | 0.052 | 5650 | 7.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 5770 | 236.1 | 0.041 | 5770 | 5.8 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 5210 | 220.3 | 0.042 | 5210 | 5.4 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452398-08 — fs direct post key=off readback=on hash=update relations=inline N=2 B=10 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 730 | 206.3 | 0.283 | 2190 | 20.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2010 | 106.8 | 0.053 | 2010 | 10.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1440 | 84.2 | 0.058 | 1440 | 8.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10700 | 79.8 | 0.007 | 10700 | 7.8 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1260 | 70.5 | 0.056 | 1260 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 860 | 58.0 | 0.067 | 860 | 5.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 730 | 55.3 | 0.076 | 2190 | 5.4 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 680 | 46.9 | 0.069 | 680 | 4.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
