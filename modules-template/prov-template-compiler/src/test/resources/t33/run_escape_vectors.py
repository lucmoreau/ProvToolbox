# T33: runs escape-csv-vectors.json against the Python port of StringEscapeUtils.escapeCsv.
# usage: python3 run_escape_vectors.py <python source root holding org/openprovenance/apache/commons/lang> <vectors file>
# Prints one line per failure and, last, 'escape vectors: <n> run, <m> failed'. Exit status 0 when none failed.
import json, sys
sys.path.insert(0, sys.argv[1])
from org.openprovenance.apache.commons.lang.StringEscapeUtils import StringEscapeUtils
vectors = json.load(open(sys.argv[2], encoding='utf-8'))['vectors']
failed = 0
for v in vectors:
    got = StringEscapeUtils.escapeCsv(v['input'])
    if got != v['output']:
        failed += 1
        print('FAIL %s: gave %r, expected %r' % (v['title'], got, v['output']))
if StringEscapeUtils.escapeCsv(None) is not None:
    failed += 1
    print('FAIL null: escapeCsv(None) is not None')
print('escape vectors: %d run, %d failed' % (len(vectors) + 1, failed))
sys.exit(1 if failed else 0)
