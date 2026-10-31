
### d1793452670-01 — fs direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1591 | 435.4 | 0.274 | 4773 | 31.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4781 | 230.6 | 0.048 | 4781 | 16.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 3229 | 174.1 | 0.054 | 3229 | 12.5 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3156 | 160.6 | 0.051 | 3156 | 11.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1551 | 104.0 | 0.067 | 1551 | 7.4 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1634 | 103.7 | 0.063 | 1634 | 7.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1630 | 82.8 | 0.051 | 1630 | 5.9 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 1608 | 82.6 | 0.051 | 1608 | 5.9 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452670-02 — fs direct insert key=on readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1521 | 413.7 | 0.272 | 4563 | 27.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4725 | 264.4 | 0.056 | 4725 | 17.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 3110 | 192.5 | 0.062 | 3110 | 12.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3164 | 185.6 | 0.059 | 3164 | 12.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1550 | 115.2 | 0.074 | 1550 | 7.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1621 | 114.5 | 0.071 | 1621 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 1568 | 94.3 | 0.060 | 1568 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 1574 | 91.5 | 0.058 | 1574 | 6.1 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-03 — fs direct post key=off readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 684 | 204.6 | 0.299 | 2052 | 19.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 1962 | 116.0 | 0.059 | 1962 | 10.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 9889 | 90.4 | 0.009 | 9889 | 8.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1325 | 88.6 | 0.067 | 1325 | 8.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1303 | 81.6 | 0.063 | 1303 | 7.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 618 | 51.9 | 0.084 | 618 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 624 | 47.1 | 0.075 | 624 | 4.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 684 | 45.2 | 0.066 | 2052 | 4.2 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |

### d1793452670-04 — fs direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 670 | 215.0 | 0.321 | 2010 | 19.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2058 | 126.9 | 0.062 | 2058 | 11.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1316 | 91.7 | 0.070 | 1316 | 8.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1400 | 90.7 | 0.065 | 1400 | 8.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 10182 | 89.8 | 0.009 | 10182 | 8.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 670 | 56.3 | 0.084 | 670 | 5.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 703 | 55.1 | 0.078 | 703 | 5.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 669 | 44.7 | 0.067 | 669 | 4.0 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452670-05 — fs direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1262 | 398.5 | 0.316 | 3786 | 18.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 352.8 | 1.773 | 100 | 16.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 3744 | 223.3 | 0.060 | 3744 | 10.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2484 | 170.7 | 0.069 | 2484 | 8.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2432 | 154.5 | 0.064 | 2432 | 7.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1262 | 132.9 | 0.105 | 3786 | 6.2 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 1233 | 102.6 | 0.083 | 1233 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1244 | 97.9 | 0.079 | 1244 | 4.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-06 — fs direct post key=off readback=off hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 670 | 201.4 | 0.301 | 2010 | 18.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2179 | 127.6 | 0.059 | 2179 | 11.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 10757 | 99.4 | 0.009 | 10757 | 8.9 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1451 | 98.8 | 0.068 | 1451 | 8.9 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1466 | 91.9 | 0.063 | 1466 | 8.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 744 | 61.5 | 0.083 | 744 | 5.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 723 | 54.5 | 0.075 | 723 | 4.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 777 | 50.3 | 0.065 | 777 | 4.5 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452670-07 — fs direct post key=off readback=on hash=header relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 758 | 213.6 | 0.282 | 2274 | 20.9 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2183 | 118.8 | 0.054 | 2183 | 11.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1503 | 92.2 | 0.061 | 1503 | 9.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1455 | 85.3 | 0.059 | 1455 | 8.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 772 | 58.9 | 0.076 | 772 | 5.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 741 | 52.8 | 0.071 | 741 | 5.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 758 | 50.0 | 0.066 | 2274 | 4.9 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 736 | 43.5 | 0.059 | 736 | 4.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452670-08 — fs direct post key=off readback=on hash=off relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 776 | 233.4 | 0.301 | 2328 | 21.3 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2204 | 134.7 | 0.061 | 2204 | 12.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1480 | 96.9 | 0.065 | 1480 | 8.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1454 | 88.7 | 0.061 | 1454 | 8.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 767 | 62.1 | 0.081 | 767 | 5.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 736 | 56.5 | 0.077 | 736 | 5.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 776 | 55.9 | 0.072 | 2328 | 5.1 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 767 | 49.8 | 0.065 | 767 | 4.5 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-09 — fs direct post key=off readback=on hash=update relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 854 | 265.0 | 0.310 | 2562 | 15.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 248.8 | 1.250 | 100 | 14.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2674 | 164.2 | 0.061 | 2674 | 9.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1770 | 122.4 | 0.069 | 1770 | 7.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1752 | 113.5 | 0.065 | 1752 | 6.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 13207 | 111.6 | 0.008 | 13207 | 6.7 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 921 | 78.7 | 0.085 | 921 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 887 | 71.8 | 0.081 | 887 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-10 — fs direct insert key=off readback= hash= relations= N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 696 | 251.4 | 0.361 | 2088 | 28.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 2170 | 157.8 | 0.073 | 2170 | 17.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1391 | 119.3 | 0.086 | 1391 | 13.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1408 | 106.6 | 0.076 | 1408 | 11.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 704 | 69.4 | 0.099 | 704 | 7.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 680 | 62.2 | 0.092 | 680 | 6.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 750 | 59.8 | 0.080 | 750 | 6.7 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 706 | 53.1 | 0.075 | 706 | 5.9 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452670-11 — fs direct insert key=off readback= hash= relations= N=4 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4308 | 1348.5 | 0.313 | 12924 | 29.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 12661 | 774.5 | 0.061 | 12661 | 17.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 8440 | 577.5 | 0.068 | 8440 | 12.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8445 | 545.7 | 0.065 | 8445 | 11.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4235 | 355.1 | 0.084 | 4235 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4383 | 349.1 | 0.080 | 4383 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 4364 | 290.0 | 0.066 | 4364 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 4272 | 275.4 | 0.064 | 4272 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-12 — fs direct insert key=off readback= hash= relations= N=8 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7938 | 2400.0 | 0.302 | 23814 | 28.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 24388 | 1493.2 | 0.061 | 24388 | 17.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 16330 | 1147.1 | 0.070 | 16330 | 13.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 16184 | 1053.8 | 0.065 | 16184 | 12.3 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8129 | 688.7 | 0.085 | 8129 | 8.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 8017 | 642.5 | 0.080 | 8017 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 8178 | 544.8 | 0.067 | 8178 | 6.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 8026 | 520.4 | 0.065 | 8026 | 6.1 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-13 — fs direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 10622 | 3623.7 | 0.341 | 31866 | 28.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 32121 | 2281.4 | 0.071 | 32121 | 17.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 21223 | 1696.6 | 0.080 | 21223 | 13.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 21311 | 1567.9 | 0.074 | 21311 | 12.2 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 10572 | 1030.3 | 0.097 | 10572 | 8.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 10808 | 974.0 | 0.090 | 10808 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 10742 | 817.4 | 0.076 | 10742 | 6.3 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 10496 | 772.3 | 0.074 | 10496 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-14 — fs direct insert key=off readback= hash= relations= N=32 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 12505 | 5336.8 | 0.427 | 37515 | 26.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 37470 | 3700.5 | 0.099 | 37470 | 18.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 24883 | 2692.5 | 0.108 | 24883 | 13.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 24997 | 2568.0 | 0.103 | 24997 | 12.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 12526 | 1571.7 | 0.125 | 12526 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 12554 | 1517.8 | 0.121 | 12554 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 12544 | 1277.6 | 0.102 | 12544 | 6.4 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 12450 | 1220.8 | 0.098 | 12450 | 6.1 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-15 — fs direct insert key=off readback= hash= relations= N=64 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 16776 | 10686.6 | 0.637 | 50328 | 23.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 50318 | 9506.1 | 0.189 | 50318 | 20.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 33372 | 6503.0 | 0.195 | 33372 | 14.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 33496 | 6013.0 | 0.180 | 33496 | 13.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 16961 | 3585.6 | 0.211 | 16961 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 16795 | 3443.4 | 0.205 | 16795 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 16791 | 3038.4 | 0.181 | 16791 | 6.6 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |
| 16990 | 3022.7 | 0.178 | 16990 | 6.6 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |

### d1793452670-16 — fs direct post key=on readback=on hash=update relations=inline N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1268 | 408.0 | 0.322 | 3804 | 17.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3888 | 274.2 | 0.071 | 3888 | 11.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2612 | 193.5 | 0.074 | 2612 | 8.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2564 | 180.3 | 0.070 | 2564 | 7.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 19304 | 178.5 | 0.009 | 19304 | 7.7 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 1268 | 156.0 | 0.123 | 3804 | 6.7 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 1254 | 113.5 | 0.090 | 1254 | 4.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1271 | 108.6 | 0.085 | 1271 | 4.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-17 — fs direct post key=on readback=on hash=update relations=inline N=4 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1881 | 673.6 | 0.358 | 5643 | 16.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 5597 | 425.5 | 0.076 | 5597 | 10.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 1881 | 367.7 | 0.195 | 5643 | 9.1 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 28089 | 326.6 | 0.012 | 28089 | 8.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 3701 | 313.2 | 0.085 | 3701 | 7.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 3751 | 306.1 | 0.082 | 3751 | 7.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 1922 | 199.6 | 0.104 | 1922 | 5.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 1885 | 186.1 | 0.099 | 1885 | 4.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-18 — fs direct post key=on readback=on hash=update relations=inline N=8 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3070 | 1142.8 | 0.372 | 9210 | 16.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 3070 | 848.7 | 0.276 | 9210 | 12.3 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 9252 | 714.8 | 0.077 | 9252 | 10.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 6184 | 534.2 | 0.086 | 6184 | 7.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 45902 | 518.0 | 0.011 | 45902 | 7.5 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 5994 | 486.5 | 0.081 | 5994 | 7.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3088 | 327.8 | 0.106 | 3088 | 4.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3059 | 295.0 | 0.096 | 3059 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-19 — fs direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2859 | 1238.0 | 0.433 | 8577 | 16.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 9072 | 855.6 | 0.094 | 9072 | 11.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 2859 | 848.2 | 0.297 | 8577 | 11.2 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 5984 | 601.3 | 0.100 | 5984 | 8.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 6022 | 570.4 | 0.095 | 6022 | 7.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 44573 | 548.4 | 0.012 | 44573 | 7.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 2990 | 351.7 | 0.118 | 2990 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3078 | 349.9 | 0.114 | 3078 | 4.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-20 — fs direct post key=on readback=on hash=update relations=inline N=32 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5001 | 2607.2 | 0.521 | 15003 | 14.4 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 5001 | 2478.7 | 0.496 | 15003 | 13.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 15132 | 1847.5 | 0.122 | 15132 | 10.2 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 75418 | 1467.3 | 0.019 | 75418 | 8.1 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 10187 | 1323.4 | 0.130 | 10187 | 7.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9756 | 1239.8 | 0.127 | 9756 | 6.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 5106 | 809.6 | 0.159 | 5106 | 4.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 5037 | 732.7 | 0.145 | 5037 | 4.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-21 — fs direct post key=on readback=on hash=update relations=inline N=64 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7042 | 6195.0 | 0.880 | 21126 | 16.8 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 7042 | 4419.9 | 0.628 | 21126 | 12.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 21118 | 3807.1 | 0.180 | 21118 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 105537 | 2966.3 | 0.028 | 105537 | 8.0 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 14288 | 2625.7 | 0.184 | 14288 | 7.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 13923 | 2525.8 | 0.181 | 13923 | 6.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 84207 | 1544.2 | 0.018 | 84207 | 4.2 | `INSERT INTO __PROV_ASSOCIATION (activity, activity_rel, agent, agent_rel, plan, plan_rel, template, template_id, rel) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9)` |
| 7043 | 1460.0 | 0.207 | 7043 | 4.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### d1793452670-22 — fs direct post key=off readback=off hash=off relations=async N=2 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2310 | 683.7 | 0.296 | 6930 | 17.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 668.0 | 3.357 | 100 | 17.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 2310 | 424.2 | 0.184 | 6930 | 11.0 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 7004 | 380.3 | 0.054 | 7004 | 9.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 4658 | 287.4 | 0.062 | 4658 | 7.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4625 | 266.9 | 0.058 | 4625 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 2387 | 181.1 | 0.076 | 2387 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 2312 | 163.6 | 0.071 | 2312 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-23 — fs direct post key=off readback=off hash=off relations=async N=4 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 3604 | 1073.2 | 0.298 | 10812 | 16.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 3604 | 1065.9 | 0.296 | 10812 | 16.4 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 989.3 | 4.971 | 100 | 15.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 10634 | 616.5 | 0.058 | 10634 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 7153 | 466.8 | 0.065 | 7153 | 7.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 7074 | 434.1 | 0.061 | 7074 | 6.7 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 3590 | 293.1 | 0.082 | 3590 | 4.5 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 3593 | 273.4 | 0.076 | 3593 | 4.2 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-24 — fs direct post key=off readback=off hash=off relations=async N=8 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4469 | 1940.8 | 0.434 | 13407 | 17.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 4469 | 1846.0 | 0.413 | 13407 | 16.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 207 | 1542.0 | 7.449 | 100 | 13.9 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 13275 | 1046.8 | 0.079 | 13275 | 9.4 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 8904 | 799.8 | 0.090 | 8904 | 7.2 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8815 | 760.0 | 0.086 | 8815 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4603 | 524.7 | 0.114 | 4603 | 4.7 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 4386 | 477.4 | 0.109 | 4386 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-25 — fs direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7358 | 4823.2 | 0.655 | 22074 | 26.1 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 7358 | 2682.0 | 0.364 | 22074 | 14.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 199 | 2306.3 | 11.590 | 100 | 12.5 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 22084 | 1718.4 | 0.078 | 22084 | 9.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 14478 | 1232.4 | 0.085 | 14478 | 6.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 14664 | 1179.3 | 0.080 | 14664 | 6.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 7425 | 782.5 | 0.105 | 7425 | 4.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 7250 | 709.6 | 0.098 | 7250 | 3.8 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-26 — fs direct post key=off readback=off hash=off relations=async N=32 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 10034 | 9680.5 | 0.965 | 30102 | 31.5 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 10034 | 3987.0 | 0.397 | 30102 | 13.0 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 200 | 3098.0 | 15.490 | 101 | 10.1 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 29928 | 2916.0 | 0.097 | 29928 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 20266 | 2104.4 | 0.104 | 20266 | 6.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 20121 | 2000.6 | 0.099 | 20121 | 6.5 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 10138 | 1269.6 | 0.125 | 10138 | 4.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 10079 | 1201.9 | 0.119 | 10079 | 3.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-27 — fs direct post key=off readback=off hash=off relations=async N=64 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 12608 | 18683.1 | 1.482 | 37824 | 29.7 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 12608 | 7939.6 | 0.630 | 37824 | 12.6 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 37332 | 7424.4 | 0.199 | 37332 | 11.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 24896 | 4901.7 | 0.197 | 24896 | 7.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 24831 | 4818.5 | 0.194 | 24831 | 7.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 200 | 4637.9 | 23.189 | 100 | 7.4 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 12503 | 2713.7 | 0.217 | 12503 | 4.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 12441 | 2683.9 | 0.216 | 12441 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-28 — fs direct insert key=off readback= hash= relations= N=16 B=10 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 33660 | 9993.8 | 0.297 | 100980 | 28.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 93390 | 6254.5 | 0.067 | 93390 | 17.7 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 64230 | 4752.1 | 0.074 | 64230 | 13.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 63150 | 4458.0 | 0.071 | 63150 | 12.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 31900 | 2794.4 | 0.088 | 31900 | 7.9 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 32510 | 2714.3 | 0.083 | 32510 | 7.7 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 32080 | 2187.7 | 0.068 | 32080 | 6.2 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 31800 | 2185.0 | 0.069 | 31800 | 6.2 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-29 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 37500 | 11427.3 | 0.305 | 112500 | 26.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 113700 | 7848.4 | 0.069 | 113700 | 17.9 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 82000 | 6331.8 | 0.077 | 82000 | 14.4 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 76500 | 5616.7 | 0.073 | 76500 | 12.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 38800 | 3552.5 | 0.092 | 38800 | 8.1 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 39300 | 3463.7 | 0.088 | 39300 | 7.9 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 41100 | 2898.4 | 0.071 | 41100 | 6.6 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 36800 | 2615.3 | 0.071 | 36800 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-30 — fs direct post key=on readback=on hash=update relations=inline N=16 B=10 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 5020 | 2426.7 | 0.483 | 15060 | 17.3 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 5020 | 2073.5 | 0.413 | 15060 | 14.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 14180 | 1339.3 | 0.094 | 14180 | 9.5 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 71960 | 1241.6 | 0.017 | 71960 | 8.8 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9790 | 969.4 | 0.099 | 9790 | 6.9 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 9330 | 931.8 | 0.100 | 9330 | 6.6 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 4970 | 576.8 | 0.116 | 4970 | 4.1 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 4510 | 562.9 | 0.125 | 4510 | 4.0 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### d1793452670-31 — fs direct post key=on readback=on hash=update relations=inline N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 4300 | 1934.5 | 0.450 | 12900 | 13.2 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 4300 | 1805.2 | 0.420 | 12900 | 12.3 | `SELECT * FROM (SELECT * FROM file_transforming_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 A` |
| 15200 | 1608.1 | 0.106 | 15200 | 11.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 70500 | 1373.5 | 0.019 | 70500 | 9.4 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 9400 | 1026.4 | 0.109 | 9400 | 7.0 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 9900 | 1017.3 | 0.103 | 9900 | 7.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 4900 | 632.1 | 0.129 | 4900 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 5100 | 589.8 | 0.116 | 5100 | 4.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-32 — fs direct post key=off readback=off hash=off relations=async N=16 B=10 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 10690 | 13559.3 | 1.268 | 32070 | 35.0 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 10690 | 5081.7 | 0.475 | 32070 | 13.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 200 | 4368.9 | 21.845 | 100 | 11.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 32650 | 3215.4 | 0.098 | 32650 | 8.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 21810 | 2378.7 | 0.109 | 21810 | 6.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 21360 | 2164.5 | 0.101 | 21360 | 5.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 10670 | 1399.9 | 0.131 | 10670 | 3.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 10220 | 1260.8 | 0.123 | 10220 | 3.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-33 — fs direct post key=off readback=off hash=off relations=async N=16 B=100 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 12700 | 16886.8 | 1.330 | 38100 | 38.8 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 12700 | 5449.3 | 0.429 | 38100 | 12.5 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 185 | 4920.7 | 26.598 | 93 | 11.3 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 40800 | 3501.9 | 0.086 | 40800 | 8.1 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 23500 | 2234.0 | 0.095 | 23500 | 5.1 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 24600 | 2229.7 | 0.091 | 24600 | 5.1 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 13600 | 1473.2 | 0.108 | 13600 | 3.4 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 12600 | 1451.0 | 0.115 | 12600 | 3.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |

### d1793452670-34 — fs direct insert key=off readback= hash= relations= N=16 B=10 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 40700 | 14929.1 | 0.367 | 122100 | 27.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 122170 | 10242.2 | 0.084 | 122170 | 18.6 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 82130 | 7613.8 | 0.093 | 82130 | 13.8 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 82250 | 7035.6 | 0.086 | 82250 | 12.8 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 40510 | 4315.2 | 0.107 | 40510 | 7.8 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 41790 | 4170.4 | 0.100 | 41790 | 7.6 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 40990 | 3330.2 | 0.081 | 40990 | 6.0 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 39990 | 3277.5 | 0.082 | 39990 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-35 — fs direct insert key=off readback= hash= relations= N=16 B=100 jdbc_batch=1

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 84100 | 33652.9 | 0.400 | 252300 | 26.7 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 254400 | 25233.7 | 0.099 | 254400 | 20.0 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 170200 | 17199.6 | 0.101 | 170200 | 13.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 168800 | 15936.8 | 0.094 | 168800 | 12.6 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 85200 | 9414.9 | 0.111 | 85200 | 7.5 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
| 82100 | 9214.9 | 0.112 | 82100 | 7.3 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 85400 | 7644.7 | 0.090 | 85400 | 6.1 | `WITH insertion_result AS ( select * from insert_file_validating ($1,$2,$3,$4::TEXT,$5,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO record` |
| 84500 | 7572.5 | 0.090 | 84500 | 6.0 | `WITH insertion_result AS ( select * from insert_file_training ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INTO re` |

### d1793452670-36 — tr direct insert key=off readback= hash= relations= N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 1188 | 443.8 | 0.374 | 3564 | 22.5 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 1146 | 442.8 | 0.386 | 3438 | 22.5 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 3429 | 242.1 | 0.071 | 3429 | 12.3 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2400 | 181.4 | 0.076 | 2400 | 9.2 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2313 | 166.5 | 0.072 | 2313 | 8.5 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 2330 | 164.9 | 0.071 | 2330 | 8.4 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 1108 | 110.0 | 0.099 | 1108 | 5.6 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |
| 1195 | 108.3 | 0.091 | 1195 | 5.5 | `WITH insertion_result AS ( select * from insert_packing ($1,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz)) INSERT INTO record_index(key,table_name,principal,su` |

### d1793452670-37 — tr direct insert key=off readback= hash= relations= N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 7256 | 3194.5 | 0.440 | 21768 | 21.5 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 7312 | 3143.5 | 0.430 | 21936 | 21.1 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 21555 | 1920.3 | 0.089 | 21555 | 12.9 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 14546 | 1377.7 | 0.095 | 14546 | 9.3 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 14570 | 1337.2 | 0.092 | 14570 | 9.0 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 14534 | 1327.8 | 0.091 | 14534 | 8.9 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 7392 | 925.1 | 0.125 | 7392 | 6.2 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |
| 7323 | 853.0 | 0.116 | 7323 | 5.7 | `WITH insertion_result AS ( select * from insert_packing ($1,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz)) INSERT INTO record_index(key,table_name,principal,su` |

### d1793452670-38 — tr direct post key=on readback=on hash=update relations=inline N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 441 | 168.1 | 0.381 | 1323 | 13.6 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 439 | 164.5 | 0.375 | 1317 | 13.3 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 1352 | 100.8 | 0.075 | 1352 | 8.2 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 8889 | 89.9 | 0.010 | 8889 | 7.3 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 911 | 73.9 | 0.081 | 911 | 6.0 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 913 | 67.1 | 0.074 | 913 | 5.4 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 891 | 65.9 | 0.074 | 891 | 5.3 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |
| 427 | 46.4 | 0.109 | 427 | 3.8 | `WITH insertion_result AS ( select * from insert_unpacking ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_key)` |

### d1793452670-39 — tr direct post key=on readback=on hash=update relations=inline N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 2772 | 1269.9 | 0.458 | 8316 | 11.5 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 2676 | 1256.9 | 0.470 | 8028 | 11.3 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 8100 | 804.2 | 0.099 | 8100 | 7.3 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 2772 | 778.2 | 0.281 | 8316 | 7.0 | `SELECT * FROM (SELECT * FROM packing_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 AND record_` |
| 2676 | 750.8 | 0.281 | 8028 | 6.8 | `SELECT * FROM (SELECT * FROM unpacking_composite AS template LEFT JOIN record_index ON record_index.key=template.id AND record_index.table_name=$1 AND recor` |
| 53902 | 735.1 | 0.014 | 53902 | 6.6 | `UPDATE record_index SET hash=$1 WHERE key=$2 AND principal=$3 AND table_name=$4` |
| 5398 | 573.5 | 0.106 | 5398 | 5.2 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 5303 | 527.3 | 0.099 | 5303 | 4.8 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |

### d1793452670-40 — tr direct post key=off readback=off hash=off relations=async N=1 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 197 | 602.4 | 3.058 | 99 | 21.8 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 791 | 331.9 | 0.420 | 2373 | 12.0 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 753 | 313.3 | 0.416 | 2259 | 11.3 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 2257 | 191.5 | 0.085 | 2257 | 6.9 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 1510 | 131.8 | 0.087 | 1510 | 4.8 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 747 | 130.1 | 0.174 | 747 | 4.7 | `WITH insertion_result AS ( select * from insert_agent_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissi` |
| 1513 | 128.9 | 0.085 | 1513 | 4.7 | `WITH insertion_result AS ( select * from insert_weighing ($1,$2,$3,$4,$5,$6,$7::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission` |
| 1573 | 128.8 | 0.082 | 1573 | 4.6 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |

### d1793452670-41 — tr direct post key=off readback=off hash=off relations=async N=16 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 201 | 4049.7 | 20.148 | 101 | 17.6 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 5217 | 2728.6 | 0.523 | 15651 | 11.9 | `SELECT * FROM (SELECT template.id AS key FROM packing_composite AS template WHERE template.id=$1) as combo LEFT JOIN packing_composite_linker AS linker ON ` |
| 5201 | 2685.0 | 0.516 | 15603 | 11.7 | `SELECT * FROM (SELECT template.id AS key FROM unpacking_composite AS template WHERE template.id=$1) as combo LEFT JOIN unpacking_composite_linker AS linker ` |
| 5201 | 2360.6 | 0.454 | 15603 | 10.3 | `---- query invoker for unpacking_composite (with Principal) WITH insertion_result AS ( ---- query invoker for unpacking_composite select * from insert` |
| 5217 | 2312.1 | 0.443 | 15651 | 10.1 | `---- query invoker for packing_composite (with Principal) WITH insertion_result AS ( ---- query invoker for packing_composite select * from insert_pac` |
| 15452 | 1484.8 | 0.096 | 15452 | 6.5 | `WITH insertion_result AS ( select * from insert_transporting ($1,$2,$3,$4,$5,$6::timestamptz)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10277 | 1032.7 | 0.100 | 10277 | 4.5 | `WITH insertion_result AS ( select * from insert_item_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 10363 | 998.4 | 0.096 | 10363 | 4.3 | `WITH insertion_result AS ( select * from insert_handingover ($1,$2,$3,$4,$5::timestamptz)) INSERT INTO record_index(key,table_name,principal,submission_ke` |

### d1793452670-42 — fs direct post key=off readback=off hash=off relations=async N=32 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 9857 | 9077.4 | 0.921 | 29571 | 28.4 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 9857 | 4406.4 | 0.447 | 29571 | 13.8 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 29589 | 3280.5 | 0.111 | 29589 | 10.3 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 201 | 3210.0 | 15.970 | 102 | 10.0 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 19891 | 2325.6 | 0.117 | 19891 | 7.3 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 19843 | 2250.8 | 0.113 | 19843 | 7.0 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 9795 | 1357.0 | 0.139 | 9795 | 4.2 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 9556 | 1279.7 | 0.134 | 9556 | 4.0 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |

### d1793452670-43 — fs direct post key=off readback=off hash=off relations=async N=32 B=1 jdbc_batch=0

| calls | total ms | mean ms | rows | % time | statement |
|---:|---:|---:|---:|---:|---|
| 8478 | 7098.5 | 0.837 | 25434 | 24.6 | `SELECT * FROM (SELECT template.id AS key FROM file_transforming_composite AS template WHERE template.id=$1) as combo LEFT JOIN file_transforming_composite_l` |
| 8478 | 4086.5 | 0.482 | 25434 | 14.1 | `---- query invoker for file_transforming_composite (with Principal) WITH insertion_result AS ( ---- query invoker for file_transforming_composite sele` |
| 25266 | 3132.0 | 0.124 | 25266 | 10.8 | `WITH insertion_result AS ( select * from insert_file_transforming ($1::TEXT,$2,$3,$4,$5::TEXT,$6::timestamptz,$7::timestamptz,$8::timestamptz)) INSERT INT` |
| 200 | 2652.0 | 13.260 | 100 | 9.2 | `SELECT $15 FROM ((SELECT $16 FROM __prov_generation WHERE template_id = $1 AND template = $2 LIMIT $17) UNION ALL (SELECT $18 FROM __prov_usage WHERE template_i` |
| 16854 | 2238.8 | 0.133 | 16854 | 7.7 | `WITH insertion_result AS ( select * from insert_file_init ($1::timestamptz,$2::TEXT,$3::TEXT)) INSERT INTO record_index(key,table_name,principal,submissio` |
| 16968 | 2148.4 | 0.127 | 16968 | 7.4 | `WITH insertion_result AS ( select * from insert_file_filtering ($1::TEXT,$2,$3,$4,$5,$6,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) INSERT` |
| 8223 | 1318.3 | 0.160 | 8223 | 4.6 | `WITH insertion_result AS ( select * from insert_file_approving ($1::TEXT,$2,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestamptz)) ` |
| 8223 | 1252.2 | 0.152 | 8223 | 4.3 | `WITH insertion_result AS ( select * from insert_file_splitting ($1::TEXT,$2::TEXT,$3,$4,$5,$6::TEXT,$7::TEXT,$8::timestamptz,$9::timestamptz,$10::timestam` |
