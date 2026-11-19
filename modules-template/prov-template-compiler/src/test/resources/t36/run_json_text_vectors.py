"""T36: runs json-text-vectors.json against the Python port of JsonText.

usage: python3 -I run_json_text_vectors.py <python source dir of prov-template-library> <vectors file>
Prints one line per failure and, last, 'vectors: <n> run, <m> failed'. Exit status 0 when none failed.
"""
import json
import sys

sys.path.insert(0, sys.argv[1])
from org.openprovenance.prov.utils.JsonText import JsonText  # noqa: E402
from past.exception.IllegalArgumentException import IllegalArgumentException  # noqa: E402

vectors = json.load(open(sys.argv[2], encoding="utf-8"))
run = 0
failed = 0


def fail(title, why):
    global failed
    failed += 1
    print("FAIL " + title + ": " + why)


for e in vectors["canonical"]:
    run += 1
    try:
        got = JsonText.write(JsonText.read(e["input"]))
    except Exception as x:  # noqa: BLE001
        fail(e["title"], "raised " + repr(x))
        continue
    if got != e["output"]:
        fail(e["title"], "wrote " + got + ", expected " + e["output"])

for e in vectors["refused"]:
    run += 1
    try:
        JsonText.read(e["input"])
        fail(e["title"], "accepted")
    except IllegalArgumentException:
        pass

for e in vectors["getters"]:
    run += 1
    obj = JsonText.read(e["input"])
    method = getattr(JsonText, e["method"])
    if e.get("refused"):
        try:
            method(obj, e["key"])
            fail(e["title"], "accepted")
        except IllegalArgumentException:
            pass
        continue
    try:
        got = method(obj, e["key"])
    except Exception as x:  # noqa: BLE001
        fail(e["title"], "raised " + repr(x))
        continue
    want = e["result"]
    ok = (got is None) if want is None else (got == want and isinstance(got, bool) == isinstance(want, bool))
    if not ok:
        fail(e["title"], "got " + repr(got) + ", expected " + repr(want))

for e in vectors["text"]:
    run += 1
    value = JsonText.parse(e["input"])
    if e.get("refused"):
        try:
            JsonText.text(value)
            fail(e["title"], "accepted")
        except IllegalArgumentException:
            pass
        continue
    got = JsonText.text(value)
    if got != e["result"]:
        fail(e["title"], "got " + repr(got) + ", expected " + repr(e["result"]))

import struct  # noqa: E402

for e in vectors["numbers"]:
    run += 1
    d = struct.unpack(">d", struct.pack(">Q", int(e["bits"], 16)))[0]
    got = JsonText.toText(d)
    if got != e["canonical"]:
        fail(e["title"], "wrote " + got + ", expected " + e["canonical"])
    got = JsonText.text(d)
    if got != e["text"]:
        fail(e["title"], "text " + got + ", expected " + e["text"])

print("vectors: %d run, %d failed" % (run, failed))
sys.exit(1 if failed else 0)
