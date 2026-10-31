
### d1793446768-01 — fs direct insert key=off readback= hash= relations= N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 391 | 631.4 | 1.615 | 1173 | 28.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1189 | 401.2 | 0.337 | 1189 | 17.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 804 | 294.8 | 0.367 | 804 | 13.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 791 | 273.9 | 0.346 | 791 | 12.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 432 | 176.8 | 0.409 | 432 | 7.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 418 | 168.5 | 0.403 | 418 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 398 | 139.9 | 0.352 | 398 | 6.2 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 394 | 135.9 | 0.345 | 394 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793446768-02 — fs direct post key=on readback=on hash=update relations=inline N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 163 | 265.3 | 1.627 | 489 | 18.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 429 | 159.6 | 0.372 | 429 | 11.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2313 | 131.7 | 0.057 | 2313 | 9.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 318 | 122.4 | 0.385 | 318 | 8.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 309 | 117.0 | 0.378 | 309 | 8.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 156 | 71.6 | 0.459 | 156 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1407 | 62.6 | 0.045 | 1407 | 4.4 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
| 141 | 60.8 | 0.431 | 141 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793446768-03 — fs direct post key=off readback=off hash=off relations=async N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 236 | 437.2 | 1.852 | 708 | 19.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 697 | 308.8 | 0.443 | 697 | 13.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 440 | 205.3 | 0.467 | 440 | 9.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 414 | 186.1 | 0.450 | 414 | 8.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 77 | 164.3 | 2.134 | 41 | 7.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 227 | 118.2 | 0.521 | 227 | 5.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 213 | 113.1 | 0.531 | 213 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 215 | 97.1 | 0.452 | 215 | 4.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793446768-04 — tr direct insert key=off readback= hash= relations= N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 328 | 535.4 | 1.632 | 984 | 22.1 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 294 | 485.3 | 1.651 | 882 | 20.1 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 913 | 307.0 | 0.336 | 913 | 12.7 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 639 | 225.8 | 0.353 | 639 | 9.3 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 631 | 222.1 | 0.352 | 631 | 9.2 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 636 | 211.4 | 0.332 | 636 | 8.7 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 328 | 151.3 | 0.461 | 328 | 6.3 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |
| 314 | 127.5 | 0.406 | 314 | 5.3 | `WITH insertion_result AS ( select * from insert_packing ($1,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz)) INSERT INTO record_index(key,table_name,principal,su` |

### d1793446768-05 — tr direct post key=on readback=on hash=update relations=inline N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 155 | 249.1 | 1.607 | 465 | 15.5 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 119 | 182.4 | 1.533 | 357 | 11.4 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 2633 | 142.3 | 0.054 | 2633 | 8.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 385 | 128.9 | 0.335 | 385 | 8.0 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 269 | 93.9 | 0.349 | 269 | 5.9 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 249 | 85.4 | 0.343 | 249 | 5.3 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 253 | 82.0 | 0.324 | 253 | 5.1 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 125 | 56.5 | 0.452 | 125 | 3.5 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |

### d1793446768-06 — tr direct post key=off readback=off hash=off relations=async N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 224 | 403.3 | 1.800 | 672 | 14.3 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 220 | 383.1 | 1.741 | 660 | 13.6 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 78 | 332.8 | 4.266 | 40 | 11.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 639 | 240.0 | 0.376 | 639 | 8.5 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 469 | 176.5 | 0.376 | 469 | 6.2 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 462 | 167.4 | 0.362 | 462 | 5.9 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 426 | 161.4 | 0.379 | 426 | 5.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 251 | 120.8 | 0.481 | 251 | 4.3 | `WITH insertion_result AS ( select * from insert_packing ($1,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz)) INSERT INTO record_index(key,table_name,principal,su` |

### d1793446768-07 — fs direct insert key=off readback= hash= relations= N=2 B=10 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1430 | 1857.4 | 1.299 | 4290 | 34.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4250 | 904.7 | 0.213 | 4250 | 16.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2800 | 634.5 | 0.227 | 2800 | 11.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2650 | 586.1 | 0.221 | 2650 | 10.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1510 | 401.8 | 0.266 | 1510 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1330 | 370.8 | 0.279 | 1330 | 6.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1470 | 317.8 | 0.216 | 1470 | 5.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 1310 | 273.9 | 0.209 | 1310 | 5.1 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793446768-08 — fs direct post key=off readback=on hash=update relations=inline N=2 B=10 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 240 | 346.3 | 1.443 | 720 | 18.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 820 | 265.5 | 0.324 | 820 | 13.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 3680 | 174.8 | 0.048 | 3680 | 9.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 530 | 171.4 | 0.323 | 530 | 9.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 470 | 150.0 | 0.319 | 470 | 7.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 260 | 105.4 | 0.405 | 260 | 5.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 290 | 91.0 | 0.314 | 290 | 4.8 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 2260 | 87.1 | 0.039 | 2260 | 4.6 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
