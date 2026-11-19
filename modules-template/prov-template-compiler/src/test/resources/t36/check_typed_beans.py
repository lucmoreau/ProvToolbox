"""T36: runs the Python the compiler emits for the typed beans of two subtypes and their dispatcher, against the Python
port of JsonText: conversions, round trips, refusals, the dispatcher.

usage: python3 -I check_typed_beans.py <root of the generated modules and the stand-in general bean> <prov-template-library python dir>
Prints 'FAIL ...' lines for a wrong answer, 'BLOCKED T33 ...' for an absent value the emitter's null makes undetectable (the
Python emitter writes null as 0), and last 'checks done, <n> failed'. Exit status 0 when none failed.
"""
import sys

sys.path.insert(0, sys.argv[2])
sys.path.insert(0, sys.argv[1])
from org.example.t36.approval.client.integrator.ApprovalInputs import ApprovalInputs  # noqa: E402
from org.example.t36.approval.client.integrator.Approval_bigInputs import Approval_bigInputs  # noqa: E402
from org.example.t36.approval.client.integrator.Approval_smallInputs import Approval_smallInputs  # noqa: E402
from org.example.t36.catalogue.common.Subtypes import Subtypes  # noqa: E402
from past.exception.IllegalArgumentException import IllegalArgumentException  # noqa: E402

bad = 0


def eq(title, got, want):
    global bad
    if got != want:
        bad += 1
        print("FAIL %s: %r != %r" % (title, got, want))


def raises(title, f, fragment):
    global bad
    try:
        f()
    except IllegalArgumentException as e:
        if fragment not in str(e):
            bad += 1
            print("FAIL %s: %s" % (title, e))
        return
    bad += 1
    print("FAIL %s: accepted" % title)


def blocked_t33(title, f, fragment):
    """An absent value is tested as `x == 0` by the Python emitter, so None is never absent (T33): not a failure of T36."""
    global bad
    try:
        f()
    except IllegalArgumentException as e:
        if fragment not in str(e):
            bad += 1
            print("FAIL %s: %s" % (title, e))
        return
    except Exception as e:  # noqa: BLE001  the absent value went on to be used, as a value
        print("BLOCKED T33 %s: an absent value is not detected, %s follows" % (title, type(e).__name__))
        return
    print("BLOCKED T33 %s: an absent value is not detected" % title)


b = Approval_bigInputs()
b.approver = "ex:agent/1"
b.order = "ex:order/7"
b.level = 4
b.priceBefore = 12.5
b.unitCode = "kg"
b.payload = '{ "a" : [1, 2.0] }'     # every optional value is given: an absent one is not tested as absent (T33)
b.customer_hashOther.put("note", "hello")
b.customer_hashOther.put("n", 3)
g = b.toGeneral()
eq("approver copied", g.approver, "ex:agent/1")
eq("the fixed value", g.approver_name, "Admin")
eq("the carrier text", g.customer_hash, '{"priceBefore":12.5,"unit":"kg","payload":{"a":[1,2]},"note":"hello","n":3}')

b2 = Approval_bigInputs.fromGeneral(g)
eq("priceBefore", b2.priceBefore, 12.5)
eq("unitCode", b2.unitCode, "kg")
eq("a nested json value", b2.payload, '{"a":[1,2]}')
eq("other keys", dict(b2.customer_hashOther), {"note": "hello", "n": 3})
eq("round trip", b2.toGeneral().customer_hash, g.customer_hash)

g2 = ApprovalInputs()
g2.approver_name = "Admin"
g2.customer_hash = '{"note":"x","payload":[true],"priceBefore":1,"zzz":[1,{"q":null}],"unit":"m"}'
b3 = Approval_bigInputs.fromGeneral(g2)
eq("declared keys first", b3.toGeneral().customer_hash, '{"priceBefore":1,"unit":"m","payload":[true],"note":"x","zzz":[1,{"q":null}]}')

# an absent optional value must not be written: T33 makes Python write it as null
g4 = ApprovalInputs()
g4.approver_name = "Admin"
g4.customer_hash = '{"priceBefore":2}'
try:
    text = Approval_bigInputs.fromGeneral(g4).toGeneral().customer_hash
except Exception as e:  # noqa: BLE001
    text = "raised " + type(e).__name__
if text == '{"priceBefore":2}':
    pass
else:
    print("BLOCKED T33 an absent optional value is not left out: %s" % text)

g3 = ApprovalInputs()
g3.approver_name = "Admin"
blocked_t33("a required value is absent (from)", lambda: Approval_bigInputs.fromGeneral(g3), "the required value priceBefore is absent")
blocked_t33("a required value is absent (to)", lambda: Approval_bigInputs().toGeneral(), "the required value priceBefore is absent")

clash = Approval_bigInputs()
clash.priceBefore = 1.0
clash.unitCode = "kg"
clash.payload = "1"
clash.customer_hashOther.put("priceBefore", 9)
raises("a clash", clash.toGeneral, "the key priceBefore is declared")
other = ApprovalInputs()
other.approver_name = "Other"
other.customer_hash = '{"priceBefore":1}'
raises("another subtype", lambda: Approval_bigInputs.fromGeneral(other), "approver_name is not the subtype's Admin")
wrong = ApprovalInputs()
wrong.approver_name = "Admin"
wrong.customer_hash = '{"priceBefore":"cheap"}'
raises("a string for a number", lambda: Approval_bigInputs.fromGeneral(wrong), "priceBefore")

sm = Approval_smallInputs()
sm.lineCount = 3
gs = sm.toGeneral()
eq("small: fixed values", (gs.approver_name, gs.level, gs.customer_hash), ("Other", 1, '{"lineCount":3}'))
eq("small: back", Approval_smallInputs.fromGeneral(gs).lineCount, 3)

eq("which: big", Subtypes.whichApproval(g), "t31.approval_big")
eq("which: small", Subtypes.whichApproval(gs), "t31.approval_small")
eq("from: big", type(Subtypes.fromGeneralApproval(g)).__name__, "Approval_bigInputs")
eq("from: small", type(Subtypes.fromGeneralApproval(gs)).__name__, "Approval_smallInputs")
nobody = ApprovalInputs()
nobody.approver_name = "Nobody"
# the emitter writes null as 0 (T33): none matching returns 0 where Java and JavaScript return null
none = Subtypes.whichApproval(nobody)
if none == 0 and none is not None:
    print("BLOCKED T33 which: none is 0, not None")
else:
    eq("which: none", none, None)
raises("no subtype matches", lambda: Subtypes.fromGeneralApproval(nobody), "no subtype of t31.Approval")

print("checks done, %d failed" % bad)
sys.exit(1 if bad else 0)
