#!/usr/bin/env python3
"""
Compare the files written by tests (under target/) with a snapshot taken earlier
(see JACKSON3_MIGRATION.md, steps B2 and 5a).

Usage: python3 jackson3-migration/compare-outputs.py SNAPSHOT_DIR FILE_LIST [--strict]

FILE_LIST holds the relative paths (from the repository root) of the files in the snapshot.
By default the comparison ignores run-to-run noise: ISO date-times (tests use "now") and
UUIDs are replaced by placeholders. JSON files are compared after parsing, with arrays
sorted; other files are compared as a sorted multiset of lines (sets of attributes are
serialised in hash order, which depends on those values). --strict compares bytes.
Note: files that are random by design (prov-generator's graphs) always differ.
Prints the files that still differ and a per-module count.
"""
import collections
import json
import re
import sys

TS = re.compile(r'\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(:\d{2}(\.\d+)?)?(Z|[+-]\d{2}:?\d{2})?')
UUID = re.compile(r'[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}')


def scrub(text):
    return UUID.sub('<UUID>', TS.sub('<TIMESTAMP>', text))


def canon(x):
    """JSON value -> canonical form: placeholders, and arrays sorted (order is not
    significant for PROV attribute sets, and hash order changes with the values)."""
    if isinstance(x, dict):
        return {k: canon(v) for k, v in x.items()}
    if isinstance(x, list):
        return sorted((canon(v) for v in x), key=lambda v: json.dumps(v, sort_keys=True))
    if isinstance(x, str):
        return scrub(x)
    return x


def norm(data):
    text = data.decode('utf-8', 'replace')
    try:
        return json.dumps(canon(json.loads(text)), sort_keys=True)
    except ValueError:
        pass
    return sorted(l.strip().rstrip(',') for l in scrub(text).splitlines() if l.strip())


def main():
    snap, lst = sys.argv[1], sys.argv[2]
    strict = '--strict' in sys.argv
    per = collections.Counter()
    bad = []
    total = 0
    for f in (l.strip() for l in open(lst)):
        if not f:
            continue
        total += 1
        try:
            a = open(snap + '/' + f, 'rb').read()
        except OSError:
            continue
        try:
            b = open(f, 'rb').read()
        except OSError:
            bad.append('MISSING ' + f)
            per[f.split('/')[2] if f.count('/') > 2 else f] += 1
            continue
        if a == b or (not strict and norm(a) == norm(b)):
            continue
        bad.append('DIFF ' + f)
        per[f.split('/')[2] if f.count('/') > 2 else f] += 1
    for x in bad:
        print(x)
    print('# %d of %d files differ%s' % (len(bad), total, '' if strict else ' (after normalisation)'))
    for k, v in per.most_common():
        print('#   %5d %s' % (v, k))


if __name__ == '__main__':
    main()
