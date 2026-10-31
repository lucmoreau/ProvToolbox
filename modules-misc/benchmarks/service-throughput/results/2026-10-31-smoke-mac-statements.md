
### m1793433100-01 — fs baseline N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 352 | 3585.0 | 10.185 | 1056 | 86.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1029 | 77.0 | 0.075 | 1029 | 1.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 5277 | 59.3 | 0.011 | 5277 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 711 | 57.3 | 0.081 | 711 | 1.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 709 | 54.3 | 0.077 | 709 | 1.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 362 | 34.2 | 0.095 | 362 | 0.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 376 | 30.5 | 0.081 | 376 | 0.7 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 334 | 29.7 | 0.089 | 334 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433100-02 — fs async N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 442 | 4487.7 | 10.153 | 1326 | 85.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1376 | 102.2 | 0.074 | 1376 | 2.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 919 | 75.4 | 0.082 | 919 | 1.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 6778 | 73.0 | 0.011 | 6778 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 917 | 71.1 | 0.078 | 917 | 1.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 79 | 56.1 | 0.710 | 40 | 1.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 467 | 45.2 | 0.097 | 467 | 0.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 425 | 38.3 | 0.090 | 425 | 0.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793433100-03 — tr baseline N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 219 | 2285.6 | 10.437 | 657 | 45.4 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 248 | 2257.0 | 9.101 | 744 | 44.8 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 729 | 56.6 | 0.078 | 729 | 1.1 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4695 | 53.5 | 0.011 | 4695 | 1.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 467 | 39.0 | 0.083 | 467 | 0.8 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 496 | 37.6 | 0.076 | 496 | 0.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 467 | 35.4 | 0.076 | 467 | 0.7 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 239 | 24.7 | 0.103 | 239 | 0.5 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |

### m1793433100-04 — tr async N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 329 | 3388.4 | 10.299 | 987 | 49.3 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 309 | 2779.9 | 8.997 | 927 | 40.4 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 79 | 97.4 | 1.233 | 40 | 1.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 993 | 77.5 | 0.078 | 993 | 1.1 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 6364 | 65.4 | 0.010 | 6364 | 1.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 663 | 51.5 | 0.078 | 663 | 0.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 613 | 49.3 | 0.080 | 613 | 0.7 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 586 | 44.0 | 0.075 | 586 | 0.6 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
