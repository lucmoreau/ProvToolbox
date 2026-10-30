
### m1793382158-01 — fs baseline N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 149 | 3265.2 | 21.914 | 447 | 83.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 438 | 88.1 | 0.201 | 438 | 2.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2261 | 77.8 | 0.034 | 2261 | 2.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 343 | 77.2 | 0.225 | 343 | 2.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 302 | 63.6 | 0.211 | 302 | 1.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1332 | 39.3 | 0.030 | 1332 | 1.0 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
| 141 | 35.2 | 0.250 | 141 | 0.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 146 | 35.1 | 0.241 | 146 | 0.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### m1793382158-02 — fs async N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 158 | 3912.3 | 24.761 | 474 | 84.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 452 | 98.5 | 0.218 | 452 | 2.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2349 | 90.0 | 0.038 | 2349 | 1.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 333 | 77.2 | 0.232 | 333 | 1.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 320 | 75.8 | 0.237 | 320 | 1.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1400 | 44.3 | 0.032 | 1400 | 1.0 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
| 156 | 43.9 | 0.282 | 156 | 0.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 172 | 40.3 | 0.235 | 172 | 0.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### m1793382158-03 — tr baseline N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 107 | 2174.0 | 20.318 | 321 | 43.8 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 103 | 2116.3 | 20.547 | 309 | 42.6 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 2282 | 79.7 | 0.035 | 2282 | 1.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 359 | 73.9 | 0.206 | 359 | 1.5 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 230 | 56.4 | 0.245 | 230 | 1.1 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 243 | 50.1 | 0.206 | 243 | 1.0 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 240 | 47.4 | 0.198 | 240 | 1.0 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 144 | 37.2 | 0.258 | 144 | 0.7 | `WITH insertion_result AS ( select * from insert_packing ($1,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz)) INSERT INTO record_index(key,table_name,principal,su` |

### m1793382158-04 — tr async N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 135 | 2834.7 | 20.998 | 405 | 45.6 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 139 | 2595.8 | 18.674 | 417 | 41.7 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 394 | 89.7 | 0.228 | 394 | 1.4 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2664 | 86.9 | 0.033 | 2664 | 1.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 67 | 80.9 | 1.208 | 40 | 1.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 289 | 60.9 | 0.211 | 289 | 1.0 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 250 | 54.3 | 0.217 | 250 | 0.9 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 268 | 53.3 | 0.199 | 268 | 0.9 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |

### r1793382264 — fs baseline N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 72 | 1468.4 | 20.394 | 216 | 80.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 286 | 54.4 | 0.190 | 286 | 3.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1190 | 42.1 | 0.035 | 1190 | 2.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 161 | 34.1 | 0.212 | 161 | 1.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 147 | 29.2 | 0.198 | 147 | 1.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 718 | 23.6 | 0.033 | 718 | 1.3 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
| 77 | 19.1 | 0.249 | 77 | 1.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 95 | 18.9 | 0.199 | 95 | 1.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### r1793382288 — fs combined N=1 B=1 POOL=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 123 | 2283.7 | 18.567 | 369 | 84.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 365 | 64.9 | 0.178 | 365 | 2.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 274 | 50.9 | 0.186 | 274 | 1.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 251 | 49.4 | 0.197 | 251 | 1.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 140 | 31.9 | 0.228 | 140 | 1.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 135 | 28.8 | 0.213 | 135 | 1.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1530 | 27.2 | 0.018 | 1530 | 1.0 | `INSERT INTO __PROV_ASSOCIATION (activity, activity_rel, agent, agent_rel, plan, plan_rel, template, template_id, rel) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9)` |
| 129 | 24.5 | 0.190 | 129 | 0.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### r1793382374 — fs nojit N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 208 | 199.1 | 0.957 | 624 | 19.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 576 | 119.7 | 0.208 | 576 | 11.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2982 | 101.8 | 0.034 | 2982 | 9.7 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 411 | 83.0 | 0.202 | 411 | 7.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 377 | 82.3 | 0.218 | 377 | 7.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1824 | 54.1 | 0.030 | 1824 | 5.2 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
| 218 | 53.8 | 0.247 | 218 | 5.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 196 | 49.3 | 0.251 | 196 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
