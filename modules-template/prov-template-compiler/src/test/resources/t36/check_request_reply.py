"""T36 part D: runs the Python the compiler emits for the request and the reply of a subtype, against a stand-in typed bean
(the classes only copy fields: they import no runtime class). usage: python3 -I check_request_reply.py <root of the emitted modules> <prov-template-library python dir>
Prints 'FAIL ...' for a wrong answer, 'BLOCKED T33 ...' for what the Python emitter's null-as-0 spoils, and last 'checks done, <n> failed'."""
import sys

sys.path.insert(0, sys.argv[2])
sys.path.insert(0, sys.argv[1])
from org.example.t36.approval.client.integrator.RepriceRequest import RepriceRequest  # noqa: E402
from org.example.t36.approval.client.integrator.RepriceReply import RepriceReply  # noqa: E402

bad = 0


def eq(title, got, want):
    global bad
    if got != want:
        bad += 1
        print("FAIL %s: %r != %r" % (title, got, want))


class Typed:
    """What the typed bean has: one field per value, and the map of other keys of the carrier `extra`."""

    def __init__(self):
        self.approver = None
        self.approver_ref = None
        self.order_note = None
        self.level = None
        self.priceBefore = None
        self.unitCode = None
        self.lineCount = None
        self.payload = None
        self.memo = None
        self.extraOther = {}
        self.customer_hashOther = {}


# the fields: the values of the request, and no other
eq("request fields", sorted(k for k in vars(RepriceRequest()) if not k.startswith("_")),
   ["extraOther", "memo", "order_note", "priceBefore", "unitCode"])
eq("reply fields", sorted(k for k in vars(RepriceReply()) if not k.startswith("_")), ["approver_ref", "lineCount"])

# into copies its fields and touches no other
t = Typed()
t.approver, t.level, t.payload = "ex:a", 3, "{}"
req = RepriceRequest(order_note="n", priceBefore=2.5, unitCode="EA", memo="m", extraOther={"colour": "red"})
out = req.into(t)
eq("into returns the bean it is given", out is t, True)
eq("copied", (t.order_note, t.priceBefore, t.unitCode, t.memo, t.extraOther), ("n", 2.5, "EA", "m", {"colour": "red"}))
eq("not touched", (t.approver, t.level, t.payload, t.approver_ref, t.lineCount), ("ex:a", 3, "{}", None, None))

# of(typed) gives back what into copied
back = RepriceRequest.of(t)
eq("of(into) of a request", vars(back), vars(req))

# a reply is the other values
rep = RepriceReply(approver_ref="r1", lineCount=4)
t2 = Typed()
t2.priceBefore = 9.0
rep.into(t2)
eq("reply copied", (t2.approver_ref, t2.lineCount), ("r1", 4))
eq("reply leaves the request's values", t2.priceBefore, 9.0)
eq("of(into) of a reply", vars(RepriceReply.of(t2)), vars(rep))

# an absent map of other keys leaves the typed bean's own: the guard `!= null` is written `!= 0` for Python (T33), so None is copied
t3 = Typed()
t3.extraOther = {"kept": 1}
RepriceRequest(priceBefore=1.0).into(t3)
if t3.extraOther is None:
    print("BLOCKED T33: an absent map of other keys is tested as `!= 0`, so None overwrites the typed bean's own map")
else:
    eq("absent map of other keys", t3.extraOther, {"kept": 1})

print("checks done, %d failed" % bad)
sys.exit(1 if bad else 0)
