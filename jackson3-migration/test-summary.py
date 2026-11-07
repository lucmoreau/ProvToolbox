#!/usr/bin/env python3
"""
Summarise test results per module from one or more Maven build logs
(see docs/JACKSON3_MIGRATION.md, step B2).

Usage:
    python3 jackson3-migration/test-summary.py build.log [more.log ...] > summary.tsv
    python3 jackson3-migration/test-summary.py --diff before.tsv after.tsv

For each module it reports the surefire totals (run/failures/errors/skipped) and the
ScalaTest totals (succeeded/failed/aborted). Later logs override earlier ones for the same
module, so a resumed build (-rf) can be passed after the first log.
"""
import re
import sys

BUILDING = re.compile(r'^\[INFO\] Building (.+?) [0-9][^ ]*-SNAPSHOT')
SUREFIRE = re.compile(r'^\[(?:INFO|WARNING|ERROR)\] Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)$')
SCALATEST = re.compile(r'^Tests: succeeded (\d+), failed (\d+), canceled (\d+), ignored (\d+), pending (\d+)')


def parse(paths):
    res = {}
    for p in paths:
        module = None
        for line in open(p, errors='replace'):
            line = line.rstrip('\n')
            m = BUILDING.match(line)
            if m:
                module = m.group(1).strip().lstrip('|-> ').strip()
                res[module] = {'surefire': '-', 'scalatest': '-'}
                continue
            if module is None:
                continue
            m = SUREFIRE.match(line)
            if m:
                res[module]['surefire'] = 'run=%s fail=%s err=%s skip=%s' % m.groups()
                continue
            m = SCALATEST.match(line)
            if m:
                res[module]['scalatest'] = 'ok=%s fail=%s' % m.groups()[:2]
                continue
            if 'RUN ABORTED' in line:
                res[module]['scalatest'] = 'ABORTED'
    return res


def main():
    if sys.argv[1] == '--diff':
        a = dict(l.rstrip('\n').split('\t', 1) for l in open(sys.argv[2]) if '\t' in l)
        b = dict(l.rstrip('\n').split('\t', 1) for l in open(sys.argv[3]) if '\t' in l)
        same = True
        for k in sorted(set(a) | set(b)):
            if a.get(k) != b.get(k):
                same = False
                print('%s\n   before: %s\n   after:  %s' % (k, a.get(k), b.get(k)))
        print('IDENTICAL' if same else 'DIFFERENCES ABOVE')
        return
    for k, v in sorted(parse(sys.argv[1:]).items()):
        if v['surefire'] != '-' or v['scalatest'] != '-':
            print('%s\t%s | %s' % (k, v['surefire'], v['scalatest']))


if __name__ == '__main__':
    main()
