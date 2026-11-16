"""T36 stage 5: reads the Turtle file the compiler wrote for the catalogue of T36SubtypeTest's fixture with rdflib, and checks
what it says. usage: python3 -I check_turtle.py <file.ttl> <namespace>
Prints 'FAIL ...' for each wrong answer and last 'checks done, <n> failed'; exit status 0 when none failed."""
import sys

from rdflib import Graph, Literal, Namespace, RDF, RDFS, OWL, XSD, URIRef

g = Graph()
g.parse(sys.argv[1], format="turtle")
N = Namespace(sys.argv[2])
TPL = Namespace("http://openprovenance.org/ns/tpl#")
bad = 0


def check(title, ok):
    global bad
    if not ok:
        bad += 1
        print("FAIL " + title)


def has(s, p, o):
    return (s, p, o) in g


def fixes(sub):
    res = {}
    for b in g.objects(sub, TPL.fixes):
        res[str(g.value(b, TPL.variable))] = g.value(b, TPL.value)
    return res


check("a template is a class", has(N.approval, RDF.type, OWL.Class))
check("it names its template", has(N.approval, TPL.fullyQualifiedName, Literal("t31.Approval")))
check("a subtype is a class", has(N.approval_big, RDF.type, OWL.Class))
check("a subtype is a subclass of its template", has(N.approval_big, RDFS.subClassOf, N.approval))
check("the other subtype too", has(N.approval_small, RDFS.subClassOf, N.approval))
check("a subtype is not the template", not has(N.approval, RDFS.subClassOf, N.approval_big))
check("big fixes approver_name, to a string that needs escaping", fixes(N.approval_big) == {"approver_name": Literal("A&B <Admin>")})
check("small fixes two values, each typed", fixes(N.approval_small) == {"approver_name": Literal("Other"), "level": Literal("1", datatype=XSD.int)})
check("big requires priceBefore", has(N.approval_big, TPL.requires, N["approval-priceBefore"]))
check("big may carry unitCode", has(N.approval_big, TPL.mayCarry, N["approval-unitCode"]))
check("big may carry payload", has(N.approval_big, TPL.mayCarry, N["approval-payload"]))
check("small requires lineCount", has(N.approval_small, TPL.requires, N["approval-lineCount"]))
check("a value that is required is not also optional", not has(N.approval_big, TPL.mayCarry, N["approval-priceBefore"]))
for v, rng in (("priceBefore", XSD.double), ("unitCode", XSD.string), ("lineCount", XSD.int), ("payload", RDF.JSON)):
    p = N["approval-" + v]
    check(v + " is a datatype property of the template", has(p, RDF.type, OWL.DatatypeProperty) and has(p, RDFS.domain, N.approval))
    check(v + " has its range", has(p, RDFS.range, rng))
    check(v + " is in its carrier", has(p, TPL.inJson, Literal("customer_hash")))
check("a key of its own is given", has(N["approval-unitCode"], TPL.key, Literal("unit")))
check("no key when it is the name", g.value(N["approval-priceBefore"], TPL.key) is None)
check("the documentation is a comment", has(N["approval-priceBefore"], RDFS.comment, Literal("Price")))
check("the vocabulary is declared", has(TPL.requires, RDF.type, OWL.ObjectProperty) and has(TPL.inJson, RDF.type, OWL.DatatypeProperty))
check("every term of the catalogue is in its namespace", all(str(s).startswith(sys.argv[2]) or str(s).startswith(str(TPL)) or not isinstance(s, URIRef) or str(s).startswith("http://www.w3.org/")
                                                          for s in g.subjects()))
print("checks done, %d failed" % bad)
sys.exit(1 if bad else 0)
