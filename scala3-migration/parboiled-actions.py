#!/usr/bin/env python3
"""
Fix #1 of SCALA3_MIGRATION.md: in a parboiled2 parser, rewrite every action `~> f`
(where f is a function-typed val/def, not a lambda) into a lambda of the right arity,
e.g. `~> ((a1, a2) => f(a1, a2))`. The arity is read from the declaration
`val f: (A, B) => R` / `def f: A => R` found in the given .scala files.

Usage: python3 scala3-migration/parboiled-actions.py Parser.scala [OtherDecls.scala ...]
The first file is rewritten in place; all files are searched for declarations.
"""
import re
import sys

ACTION = re.compile(r'~>(\s*)([A-Za-z_]\w*)\b(?!\s*[(\[.])')


def arity(name, decls):
    m = re.search(r'(?:val|def)\s+%s\s*:\s*' % re.escape(name), decls)
    if not m:
        sys.exit("no declaration 'val/def %s: ... => ...' found; pass the file declaring it" % name)
    t = decls[m.end():]
    if t[0] != '(':
        return 1
    depth = 0
    commas = 0
    for i, c in enumerate(t):
        if c in '([':
            depth += 1
        elif c in ')]':
            depth -= 1
            if depth == 0:
                return 0 if t[1:i].strip() == '' else commas + 1
        elif c == ',' and depth == 1:
            commas += 1
    sys.exit("cannot parse type of " + name)


def main():
    target = sys.argv[1]
    decls = ''.join(open(f).read() for f in sys.argv[1:])
    s = open(target).read()

    def rep(m):
        n = m.group(2)
        a = arity(n, decls)
        args = ', '.join('a%d' % i for i in range(1, a + 1))
        lhs = '()' if a == 0 else ('a1' if a == 1 else '(%s)' % args)
        return '~>%s(%s => %s(%s))' % (m.group(1), lhs, n, args)

    s2, k = ACTION.subn(rep, s)
    open(target, 'w').write(s2)
    print("rewrote %d parboiled2 action(s) in %s" % (k, target))


if __name__ == '__main__':
    main()
