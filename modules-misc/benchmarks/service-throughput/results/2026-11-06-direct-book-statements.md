
### d1793958618 — fs direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1276 | 483.9 | 0.379 | 3828 | 28.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4013 | 290.8 | 0.072 | 4013 | 17.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2601 | 215.2 | 0.083 | 2601 | 12.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2649 | 197.6 | 0.075 | 2649 | 11.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1361 | 138.9 | 0.102 | 1361 | 8.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1321 | 119.3 | 0.090 | 1321 | 7.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1349 | 102.6 | 0.076 | 1349 | 6.1 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 1304 | 97.6 | 0.075 | 1304 | 5.8 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793958667 — fs direct insert key=off readback= hash= relations= N=1 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 12800 | 2798.8 | 0.219 | 38400 | 30.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 38400 | 1546.9 | 0.040 | 38400 | 17.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 23800 | 1148.3 | 0.048 | 23800 | 12.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 24600 | 1018.8 | 0.041 | 24600 | 11.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 13400 | 746.3 | 0.056 | 13400 | 8.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 11700 | 675.2 | 0.058 | 11700 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 13900 | 578.9 | 0.042 | 13900 | 6.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 11300 | 467.5 | 0.041 | 11300 | 5.2 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793958715 — fs direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 572 | 202.6 | 0.354 | 1716 | 18.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1764 | 124.4 | 0.070 | 1764 | 11.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1148 | 97.7 | 0.085 | 1148 | 8.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8593 | 91.1 | 0.011 | 8593 | 8.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1114 | 86.1 | 0.077 | 1114 | 7.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 598 | 55.4 | 0.093 | 598 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 558 | 48.8 | 0.087 | 558 | 4.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 585 | 45.1 | 0.077 | 585 | 4.1 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793958763 — fs direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1036 | 390.6 | 0.377 | 3108 | 18.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 318.0 | 1.598 | 100 | 15.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3125 | 233.7 | 0.075 | 3125 | 11.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2151 | 167.5 | 0.078 | 2151 | 7.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 2103 | 165.5 | 0.079 | 2103 | 7.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1094 | 107.7 | 0.098 | 1094 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1036 | 98.3 | 0.095 | 3108 | 4.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1114 | 98.3 | 0.088 | 1114 | 4.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793958811 — fs direct post key=off readback=off hash=off relations=async N=1 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2600 | 719.6 | 0.277 | 7800 | 22.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2600 | 551.0 | 0.212 | 7800 | 17.4 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 134 | 392.6 | 2.930 | 67 | 12.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 6600 | 333.1 | 0.050 | 6600 | 10.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 4400 | 231.1 | 0.053 | 4400 | 7.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3500 | 208.1 | 0.059 | 3500 | 6.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2000 | 125.4 | 0.063 | 2000 | 3.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 2400 | 116.8 | 0.049 | 2400 | 3.7 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793958987 — fs direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7528 | 3661.7 | 0.486 | 22584 | 27.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 22349 | 2442.9 | 0.109 | 22349 | 18.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 15132 | 1770.4 | 0.117 | 15132 | 13.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 15084 | 1602.5 | 0.106 | 15084 | 12.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 7459 | 1120.4 | 0.150 | 7459 | 8.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7421 | 927.9 | 0.125 | 7421 | 7.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 7411 | 838.2 | 0.113 | 7411 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 7551 | 812.8 | 0.108 | 7551 | 6.1 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793959035 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 90300 | 34304.0 | 0.380 | 270900 | 29.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 257600 | 23773.0 | 0.092 | 257600 | 20.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 170600 | 16071.8 | 0.094 | 170600 | 13.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 177600 | 14767.8 | 0.083 | 177600 | 12.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 81400 | 7830.2 | 0.096 | 81400 | 6.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 81700 | 7548.4 | 0.092 | 81700 | 6.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 83600 | 6583.3 | 0.079 | 83600 | 5.6 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 85800 | 6488.1 | 0.076 | 85800 | 5.5 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793959083 — fs direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3511 | 1911.0 | 0.544 | 10533 | 16.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3511 | 1247.4 | 0.355 | 10533 | 10.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 10481 | 1123.2 | 0.107 | 10481 | 9.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 52689 | 991.7 | 0.019 | 52689 | 8.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 6944 | 830.5 | 0.120 | 6944 | 7.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7102 | 770.5 | 0.108 | 7102 | 6.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3465 | 494.3 | 0.143 | 3465 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 42234 | 472.4 | 0.011 | 42234 | 4.1 | `INSERT INTO __PROV_ASSOCIATION (activity, activity_rel, agent, agent_rel, plan, plan_rel, template, template_id, rel) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9)` |

### d1793959132 — fs direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 6542 | 3775.1 | 0.577 | 19626 | 21.2 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 6542 | 2754.6 | 0.421 | 19626 | 15.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 206 | 2059.9 | 10.000 | 105 | 11.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 19660 | 1903.2 | 0.097 | 19660 | 10.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 13037 | 1309.1 | 0.100 | 13037 | 7.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 13265 | 1254.0 | 0.095 | 13265 | 7.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 6417 | 801.9 | 0.125 | 6417 | 4.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 6651 | 758.6 | 0.114 | 6651 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793959179 — fs direct post key=off readback=off hash=off relations=async N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 13900 | 17161.2 | 1.235 | 41700 | 40.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 13900 | 4874.3 | 0.351 | 41700 | 11.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 182 | 4162.6 | 22.871 | 91 | 9.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 44500 | 3311.8 | 0.074 | 44500 | 7.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 27600 | 2302.8 | 0.083 | 27600 | 5.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 28100 | 2178.8 | 0.078 | 28100 | 5.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 16500 | 1691.0 | 0.102 | 16500 | 4.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 14600 | 1377.6 | 0.094 | 14600 | 3.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793959355 — fs direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1436 | 471.8 | 0.329 | 4308 | 29.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4273 | 277.0 | 0.065 | 4273 | 17.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2759 | 197.7 | 0.072 | 2759 | 12.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2786 | 191.3 | 0.069 | 2786 | 11.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1471 | 133.5 | 0.091 | 1471 | 8.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1421 | 123.0 | 0.087 | 1421 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1484 | 102.4 | 0.069 | 1484 | 6.3 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 1403 | 99.6 | 0.071 | 1403 | 6.1 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793959403 — fs direct insert key=off readback= hash= relations= N=1 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 11700 | 2701.3 | 0.231 | 35100 | 30.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 36100 | 1576.5 | 0.044 | 36100 | 17.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 25500 | 1339.0 | 0.053 | 25500 | 14.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 21800 | 1014.7 | 0.047 | 21800 | 11.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 11300 | 659.5 | 0.058 | 11300 | 7.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 10600 | 640.6 | 0.060 | 10600 | 7.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 10700 | 508.6 | 0.048 | 10700 | 5.6 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 10700 | 504.9 | 0.047 | 10700 | 5.6 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793959451 — fs direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 592 | 223.2 | 0.377 | 1776 | 19.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1668 | 129.1 | 0.077 | 1668 | 11.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 8453 | 100.7 | 0.012 | 8453 | 8.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1113 | 93.7 | 0.084 | 1113 | 8.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1083 | 84.9 | 0.078 | 1083 | 7.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 548 | 54.6 | 0.100 | 548 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 568 | 52.4 | 0.092 | 568 | 4.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 592 | 44.8 | 0.076 | 592 | 4.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793959499 — fs direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 999 | 363.1 | 0.363 | 2997 | 16.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 198 | 309.2 | 1.562 | 100 | 14.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3037 | 231.7 | 0.076 | 3037 | 10.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2151 | 179.5 | 0.083 | 2151 | 8.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2018 | 170.6 | 0.085 | 2018 | 8.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1016 | 104.2 | 0.103 | 1016 | 4.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1039 | 100.4 | 0.097 | 1039 | 4.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 999 | 96.3 | 0.096 | 2997 | 4.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |

### d1793959547 — fs direct post key=off readback=off hash=off relations=async N=1 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1400 | 419.4 | 0.300 | 4200 | 13.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 142 | 397.5 | 2.799 | 70 | 13.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 5400 | 336.7 | 0.062 | 5400 | 11.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 4300 | 297.7 | 0.069 | 4300 | 9.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4100 | 282.7 | 0.069 | 4100 | 9.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1400 | 180.1 | 0.129 | 4200 | 6.0 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 2200 | 175.7 | 0.080 | 2200 | 5.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 2000 | 170.3 | 0.085 | 2000 | 5.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### d1793959723 — fs direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 9265 | 3450.4 | 0.372 | 27795 | 27.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 27710 | 2223.4 | 0.080 | 27710 | 18.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 18618 | 1631.7 | 0.088 | 18618 | 13.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 18594 | 1547.7 | 0.083 | 18594 | 12.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 9176 | 969.9 | 0.106 | 9176 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 9353 | 923.0 | 0.099 | 9353 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 9478 | 775.5 | 0.082 | 9478 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 9298 | 746.8 | 0.080 | 9298 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793959771 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 81200 | 28711.5 | 0.354 | 243600 | 26.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 238300 | 21071.2 | 0.088 | 238300 | 19.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 161800 | 15282.6 | 0.094 | 161800 | 14.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 159800 | 13976.5 | 0.087 | 159800 | 13.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 80500 | 8358.9 | 0.104 | 80500 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 77200 | 7523.9 | 0.097 | 77200 | 7.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 79300 | 6450.5 | 0.081 | 79300 | 6.0 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 77100 | 6271.5 | 0.081 | 77100 | 5.8 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793959819 — fs direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3748 | 1637.0 | 0.437 | 11244 | 15.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3748 | 1346.5 | 0.359 | 11244 | 12.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11465 | 1088.7 | 0.095 | 11465 | 10.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 57130 | 828.4 | 0.015 | 57130 | 7.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7749 | 797.3 | 0.103 | 7749 | 7.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7469 | 727.9 | 0.097 | 7469 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3883 | 479.7 | 0.124 | 3883 | 4.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3825 | 443.9 | 0.116 | 3825 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793959867 — fs direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 6225 | 3508.6 | 0.564 | 18675 | 20.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 6225 | 2746.1 | 0.441 | 18675 | 16.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 208 | 1979.2 | 9.515 | 109 | 11.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 18129 | 1729.4 | 0.095 | 18129 | 10.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 12436 | 1311.9 | 0.105 | 12436 | 7.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 12170 | 1208.5 | 0.099 | 12170 | 7.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 6055 | 772.0 | 0.128 | 6055 | 4.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 6143 | 728.8 | 0.119 | 6143 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793959915 — fs direct post key=off readback=off hash=off relations=async N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 12400 | 14714.3 | 1.187 | 37200 | 38.8 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 12400 | 4755.4 | 0.384 | 37200 | 12.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 174 | 3554.0 | 20.426 | 87 | 9.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 38000 | 3004.3 | 0.079 | 38000 | 7.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 25900 | 2245.7 | 0.087 | 25900 | 5.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 24200 | 2053.1 | 0.085 | 24200 | 5.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 13800 | 1462.9 | 0.106 | 13800 | 3.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 13500 | 1271.1 | 0.094 | 13500 | 3.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793960090 — fs direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1592 | 454.9 | 0.286 | 4776 | 30.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4816 | 244.9 | 0.051 | 4816 | 16.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 3120 | 184.8 | 0.059 | 3120 | 12.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3134 | 169.3 | 0.054 | 3134 | 11.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1576 | 115.0 | 0.073 | 1576 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1587 | 104.4 | 0.066 | 1587 | 7.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1584 | 86.3 | 0.054 | 1584 | 5.9 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 1600 | 85.3 | 0.053 | 1600 | 5.8 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793960138 — fs direct insert key=off readback= hash= relations= N=1 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 14000 | 2611.0 | 0.187 | 42000 | 29.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 40800 | 1427.6 | 0.035 | 40800 | 16.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 32200 | 1167.3 | 0.036 | 32200 | 13.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 28700 | 1148.7 | 0.040 | 28700 | 13.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 16300 | 746.1 | 0.046 | 16300 | 8.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 14900 | 697.5 | 0.047 | 14900 | 7.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 14700 | 518.8 | 0.035 | 14700 | 5.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 13900 | 507.3 | 0.036 | 13900 | 5.7 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793960186 — fs direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 643 | 205.2 | 0.319 | 1929 | 19.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1934 | 115.1 | 0.060 | 1934 | 11.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1352 | 90.3 | 0.067 | 1352 | 8.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9805 | 84.5 | 0.009 | 9805 | 8.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1268 | 79.5 | 0.063 | 1268 | 7.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 681 | 56.0 | 0.082 | 681 | 5.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 661 | 50.3 | 0.076 | 661 | 4.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 683 | 44.7 | 0.065 | 683 | 4.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793960234 — fs direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1034 | 390.5 | 0.378 | 3102 | 19.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 292.6 | 1.470 | 100 | 14.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3079 | 210.0 | 0.068 | 3079 | 10.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2001 | 155.2 | 0.078 | 2001 | 7.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2049 | 149.3 | 0.073 | 2049 | 7.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1015 | 94.6 | 0.093 | 1015 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1034 | 94.5 | 0.091 | 3102 | 4.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1031 | 88.3 | 0.086 | 1031 | 4.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793960281 — fs direct post key=off readback=off hash=off relations=async N=1 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2000 | 549.9 | 0.275 | 6000 | 18.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 134 | 410.9 | 3.066 | 67 | 13.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 5600 | 308.1 | 0.055 | 5600 | 10.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2000 | 296.8 | 0.148 | 6000 | 9.8 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 4700 | 267.3 | 0.057 | 4700 | 8.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4600 | 263.0 | 0.057 | 4600 | 8.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2600 | 185.5 | 0.071 | 2600 | 6.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 2500 | 131.1 | 0.052 | 2500 | 4.3 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793960457 — fs direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7856 | 3158.3 | 0.402 | 23568 | 27.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 23937 | 2021.8 | 0.084 | 23937 | 17.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 16235 | 1560.3 | 0.096 | 16235 | 13.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 15973 | 1444.7 | 0.090 | 15973 | 12.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 7781 | 895.3 | 0.115 | 7781 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7836 | 854.2 | 0.109 | 7836 | 7.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 8061 | 722.0 | 0.090 | 8061 | 6.3 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 7869 | 716.5 | 0.091 | 7869 | 6.2 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793960505 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 96200 | 35831.0 | 0.372 | 288600 | 27.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 288500 | 26646.4 | 0.092 | 288500 | 20.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 187000 | 17675.9 | 0.095 | 187000 | 13.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 194300 | 16695.0 | 0.086 | 194300 | 12.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 92200 | 9369.0 | 0.102 | 92200 | 7.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 92700 | 9225.1 | 0.100 | 92700 | 7.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 93600 | 7501.6 | 0.080 | 93600 | 5.7 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 93100 | 7477.9 | 0.080 | 93100 | 5.7 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793960553 — fs direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3255 | 1774.8 | 0.545 | 9765 | 16.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3255 | 1293.3 | 0.397 | 9765 | 11.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 9642 | 1136.8 | 0.118 | 9642 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 6541 | 886.2 | 0.135 | 6541 | 8.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 48678 | 849.5 | 0.017 | 48678 | 7.7 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 6457 | 781.2 | 0.121 | 6457 | 7.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3178 | 488.0 | 0.154 | 3178 | 4.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3282 | 465.5 | 0.142 | 3282 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793960602 — fs direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4610 | 2466.3 | 0.535 | 13830 | 17.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 4610 | 2312.7 | 0.502 | 13830 | 16.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 204 | 1931.2 | 9.467 | 104 | 13.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 13655 | 1453.7 | 0.106 | 13655 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 9443 | 1115.8 | 0.118 | 9443 | 7.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9234 | 1050.8 | 0.114 | 9234 | 7.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4744 | 655.6 | 0.138 | 4744 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4739 | 638.1 | 0.135 | 4739 | 4.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793960650 — fs direct post key=off readback=off hash=off relations=async N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 11400 | 15180.0 | 1.332 | 34200 | 36.0 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 11400 | 5628.1 | 0.494 | 34200 | 13.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 177 | 4752.9 | 26.852 | 88 | 11.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 34100 | 3360.6 | 0.099 | 34100 | 8.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 22800 | 2581.0 | 0.113 | 22800 | 6.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 24100 | 2551.1 | 0.106 | 24100 | 6.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 11700 | 1548.7 | 0.132 | 11700 | 3.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 9700 | 1251.0 | 0.129 | 9700 | 3.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793960826 — fs direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1403 | 430.8 | 0.307 | 4209 | 27.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4390 | 260.8 | 0.059 | 4390 | 16.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2948 | 202.3 | 0.069 | 2948 | 13.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2914 | 181.3 | 0.062 | 2914 | 11.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1478 | 125.7 | 0.085 | 1478 | 8.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1456 | 117.0 | 0.080 | 1456 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1440 | 99.7 | 0.069 | 1440 | 6.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 1518 | 97.6 | 0.064 | 1518 | 6.3 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793960874 — fs direct insert key=off readback= hash= relations= N=1 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 10800 | 2274.5 | 0.211 | 32400 | 25.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 36600 | 1470.2 | 0.040 | 36600 | 16.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 27600 | 1282.6 | 0.046 | 27600 | 14.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 28300 | 1193.5 | 0.042 | 28300 | 13.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 13300 | 757.2 | 0.057 | 13300 | 8.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 11800 | 622.2 | 0.053 | 11800 | 7.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 14300 | 607.5 | 0.042 | 14300 | 6.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 13400 | 554.0 | 0.041 | 13400 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793960922 — fs direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 599 | 201.6 | 0.337 | 1797 | 18.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1763 | 132.8 | 0.075 | 1763 | 12.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 8745 | 94.5 | 0.011 | 8745 | 8.5 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1153 | 94.3 | 0.082 | 1153 | 8.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1141 | 88.0 | 0.077 | 1141 | 7.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 576 | 56.5 | 0.098 | 576 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 566 | 51.2 | 0.091 | 566 | 4.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 581 | 43.5 | 0.075 | 581 | 3.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793960969 — fs direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 893 | 406.5 | 0.455 | 2679 | 18.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 300.1 | 1.508 | 100 | 13.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2756 | 242.6 | 0.088 | 2756 | 11.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1791 | 166.5 | 0.093 | 1791 | 7.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1719 | 166.4 | 0.097 | 1719 | 7.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 898 | 104.9 | 0.117 | 898 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 885 | 90.2 | 0.102 | 885 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 958 | 87.0 | 0.091 | 958 | 4.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793961017 — fs direct post key=off readback=off hash=off relations=async N=1 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1600 | 492.4 | 0.308 | 4800 | 15.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 141 | 440.0 | 3.120 | 70 | 14.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 5100 | 295.3 | 0.058 | 5100 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 4300 | 284.3 | 0.066 | 4300 | 9.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3400 | 267.2 | 0.079 | 3400 | 8.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3400 | 235.4 | 0.069 | 3400 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1600 | 218.0 | 0.136 | 4800 | 7.0 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 2300 | 159.7 | 0.069 | 2300 | 5.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### d1793961193 — fs direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 8118 | 3295.4 | 0.406 | 24354 | 27.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 24134 | 2161.1 | 0.090 | 24134 | 17.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 15988 | 1683.8 | 0.105 | 15988 | 13.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 15806 | 1492.1 | 0.094 | 15806 | 12.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8068 | 983.0 | 0.122 | 8068 | 8.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7941 | 873.7 | 0.110 | 7941 | 7.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 7980 | 770.7 | 0.097 | 7980 | 6.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 8060 | 741.2 | 0.092 | 8060 | 6.1 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793961241 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 67100 | 26019.5 | 0.388 | 201300 | 26.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 204400 | 19592.5 | 0.096 | 204400 | 19.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 141100 | 13595.2 | 0.096 | 141100 | 13.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 134300 | 12031.9 | 0.090 | 134300 | 12.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 71100 | 8131.1 | 0.114 | 71100 | 8.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 67800 | 7446.8 | 0.110 | 67800 | 7.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 69800 | 6296.5 | 0.090 | 69800 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 65900 | 5983.7 | 0.091 | 65900 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793961290 — fs direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3503 | 1608.3 | 0.459 | 10509 | 15.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3503 | 1283.9 | 0.367 | 10509 | 12.0 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 10801 | 1112.9 | 0.103 | 10801 | 10.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 53608 | 874.6 | 0.016 | 53608 | 8.2 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7188 | 813.1 | 0.113 | 7188 | 7.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7323 | 778.3 | 0.106 | 7323 | 7.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3576 | 466.3 | 0.130 | 3576 | 4.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3556 | 458.9 | 0.129 | 3556 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793961338 — fs direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5761 | 3314.9 | 0.575 | 17283 | 19.3 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 5761 | 2810.8 | 0.488 | 17283 | 16.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 200 | 2084.2 | 10.421 | 101 | 12.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 17575 | 1767.6 | 0.101 | 17575 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 11723 | 1259.6 | 0.107 | 11723 | 7.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 11606 | 1218.0 | 0.105 | 11606 | 7.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5933 | 778.6 | 0.131 | 5933 | 4.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5735 | 743.8 | 0.130 | 5735 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793961386 — fs direct post key=off readback=off hash=off relations=async N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 15300 | 18240.8 | 1.192 | 45900 | 41.4 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 15300 | 5787.8 | 0.378 | 45900 | 13.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 182 | 3894.5 | 21.399 | 90 | 8.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 42000 | 3266.9 | 0.078 | 42000 | 7.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 26700 | 2354.1 | 0.088 | 26700 | 5.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 27100 | 2244.6 | 0.083 | 27100 | 5.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 15300 | 1481.4 | 0.097 | 15300 | 3.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 14800 | 1477.4 | 0.100 | 14800 | 3.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### d1793961562 — fs direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1452 | 470.9 | 0.324 | 4356 | 29.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4312 | 273.1 | 0.063 | 4312 | 16.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2900 | 206.7 | 0.071 | 2900 | 12.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2883 | 196.1 | 0.068 | 2883 | 12.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1476 | 130.9 | 0.089 | 1476 | 8.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1443 | 118.8 | 0.082 | 1443 | 7.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1483 | 100.9 | 0.068 | 1483 | 6.2 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 1506 | 100.4 | 0.067 | 1506 | 6.2 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793961610 — fs direct insert key=off readback= hash= relations= N=1 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 14400 | 2940.8 | 0.204 | 43200 | 31.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 41000 | 1601.9 | 0.039 | 41000 | 17.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 28100 | 1212.0 | 0.043 | 28100 | 12.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 25600 | 1008.4 | 0.039 | 25600 | 10.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 14200 | 740.1 | 0.052 | 14200 | 7.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 13900 | 692.0 | 0.050 | 13900 | 7.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 14300 | 596.6 | 0.042 | 14300 | 6.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 14000 | 536.5 | 0.038 | 14000 | 5.7 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793961658 — fs direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 486 | 184.9 | 0.381 | 1458 | 15.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1627 | 134.8 | 0.083 | 1627 | 11.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 7847 | 102.7 | 0.013 | 7847 | 8.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1058 | 96.6 | 0.091 | 1058 | 8.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1094 | 96.0 | 0.088 | 1094 | 8.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 510 | 56.4 | 0.111 | 510 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 494 | 55.5 | 0.112 | 494 | 4.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1 | 54.1 | 54.056 | 1 | 4.5 | `SELECT count(*) FROM record_index` |

### d1793961705 — fs direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1026 | 419.9 | 0.409 | 3078 | 19.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 198 | 299.2 | 1.511 | 100 | 14.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3077 | 226.9 | 0.074 | 3077 | 10.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2077 | 167.8 | 0.081 | 2077 | 8.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2061 | 155.8 | 0.076 | 2061 | 7.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1026 | 98.2 | 0.096 | 3078 | 4.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 984 | 97.4 | 0.099 | 984 | 4.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1051 | 95.1 | 0.090 | 1051 | 4.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793961754 — fs direct post key=off readback=off hash=off relations=async N=1 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 159 | 548.5 | 3.450 | 75 | 16.7 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 1700 | 482.6 | 0.284 | 5100 | 14.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 6800 | 378.3 | 0.056 | 6800 | 11.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 5200 | 335.0 | 0.064 | 5200 | 10.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1700 | 259.4 | 0.153 | 5100 | 7.9 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 4500 | 258.4 | 0.057 | 4500 | 7.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 2800 | 194.0 | 0.069 | 2800 | 5.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3100 | 176.9 | 0.057 | 3100 | 5.4 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793961930 — fs direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 10252 | 3728.3 | 0.364 | 30756 | 28.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 30476 | 2331.0 | 0.076 | 30476 | 17.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 20693 | 1765.2 | 0.085 | 20693 | 13.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 20394 | 1639.6 | 0.080 | 20394 | 12.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 10185 | 1054.1 | 0.103 | 10185 | 7.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 10341 | 1019.3 | 0.099 | 10341 | 7.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 10418 | 830.1 | 0.080 | 10418 | 6.2 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 10080 | 815.1 | 0.081 | 10080 | 6.1 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793961979 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 115000 | 40410.5 | 0.351 | 345000 | 27.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 351500 | 29684.0 | 0.084 | 351500 | 20.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 232400 | 19594.5 | 0.084 | 232400 | 13.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 227600 | 17516.0 | 0.077 | 227600 | 12.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 115800 | 10813.4 | 0.093 | 115800 | 7.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 112000 | 10212.0 | 0.091 | 112000 | 7.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 112700 | 8158.5 | 0.072 | 112700 | 5.6 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 112200 | 7967.4 | 0.071 | 112200 | 5.5 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793962027 — fs direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3834 | 1675.4 | 0.437 | 11502 | 15.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3834 | 1537.7 | 0.401 | 11502 | 13.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 11621 | 1155.1 | 0.099 | 11621 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 57975 | 892.2 | 0.015 | 57975 | 8.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 7950 | 829.7 | 0.104 | 7950 | 7.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7669 | 773.5 | 0.101 | 7669 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3868 | 502.2 | 0.130 | 3868 | 4.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3892 | 469.7 | 0.121 | 3892 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793962074 — fs direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 6535 | 4098.2 | 0.627 | 19605 | 23.4 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 6535 | 2576.6 | 0.394 | 19605 | 14.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 204 | 2097.9 | 10.284 | 104 | 12.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 19572 | 1738.4 | 0.089 | 19572 | 9.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 13160 | 1291.0 | 0.098 | 13160 | 7.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 13069 | 1173.1 | 0.090 | 13069 | 6.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 6548 | 758.6 | 0.116 | 6548 | 4.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 6574 | 727.5 | 0.111 | 6574 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793962122 — fs direct post key=off readback=off hash=off relations=async N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 14900 | 18662.1 | 1.252 | 44700 | 41.2 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 14900 | 5546.8 | 0.372 | 44700 | 12.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 183 | 4080.0 | 22.295 | 92 | 9.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 45000 | 3326.6 | 0.074 | 45000 | 7.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 33600 | 2867.4 | 0.085 | 33600 | 6.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 30900 | 2446.7 | 0.079 | 30900 | 5.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 15900 | 1679.5 | 0.106 | 15900 | 3.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 14700 | 1369.5 | 0.093 | 14700 | 3.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
