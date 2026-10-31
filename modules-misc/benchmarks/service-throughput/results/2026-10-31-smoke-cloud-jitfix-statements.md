
### r1793446579 — fs baseline N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 66 | 112.6 | 1.706 | 198 | 14.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 242 | 94.1 | 0.389 | 242 | 12.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1163 | 77.2 | 0.066 | 1163 | 9.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 180 | 73.7 | 0.409 | 180 | 9.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 153 | 63.4 | 0.414 | 153 | 8.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 85 | 40.2 | 0.473 | 85 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 87 | 38.2 | 0.439 | 87 | 4.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 638 | 34.5 | 0.054 | 638 | 4.4 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |

### r1793446616 — fs combined N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 97 | 170.4 | 1.756 | 291 | 20.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 267 | 101.9 | 0.382 | 267 | 12.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 191 | 81.5 | 0.426 | 191 | 9.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 192 | 76.0 | 0.396 | 192 | 9.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 849 | 44.4 | 0.052 | 849 | 5.3 | `INSERT INTO __PROV_ASSOCIATION (activity, activity_rel, agent, agent_rel, plan, plan_rel, template, template_id, rel) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9)` |
| 98 | 44.3 | 0.452 | 98 | 5.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 60 | 43.9 | 0.731 | 41 | 5.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 104 | 42.4 | 0.407 | 104 | 5.0 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### r1793446651 — tr baseline N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 67 | 117.7 | 1.757 | 201 | 13.8 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 54 | 96.4 | 1.785 | 162 | 11.3 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 1155 | 79.8 | 0.069 | 1155 | 9.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 170 | 71.1 | 0.418 | 170 | 8.3 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 127 | 55.9 | 0.441 | 127 | 6.5 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 113 | 46.9 | 0.415 | 113 | 5.5 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 90 | 34.9 | 0.388 | 90 | 4.1 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 65 | 29.0 | 0.446 | 65 | 3.4 | `WITH insertion_result AS ( select * from insert_agent_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissi` |

### r1793446687 — tr combined N=2 B=1 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 84 | 160.5 | 1.910 | 252 | 14.7 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 80 | 155.4 | 1.943 | 240 | 14.2 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 236 | 93.5 | 0.396 | 236 | 8.5 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 61 | 83.2 | 1.365 | 39 | 7.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 169 | 73.2 | 0.433 | 169 | 6.7 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 170 | 67.9 | 0.400 | 170 | 6.2 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 168 | 67.1 | 0.400 | 168 | 6.1 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 93 | 44.6 | 0.480 | 93 | 4.1 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |

### r1793446721 — fs baseline N=2 B=10 POOL=4

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 140 | 223.5 | 1.597 | 420 | 18.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1910 | 112.7 | 0.059 | 1910 | 9.5 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 260 | 103.1 | 0.397 | 260 | 8.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 280 | 102.5 | 0.366 | 280 | 8.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 260 | 93.2 | 0.358 | 260 | 7.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 190 | 80.4 | 0.423 | 190 | 6.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 160 | 64.1 | 0.400 | 160 | 5.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 1100 | 52.8 | 0.048 | 1100 | 4.5 | `--- org.openprovenance.templates.fs.FileTransforming SELECT template.* FROM file_transforming as template LEFT JOIN record_index ON record_index.key=template` |
