import re

from past.exception.IllegalArgumentException import IllegalArgumentException


class Pseudonyms:
    """T31 (R9, R16): the client check of a pseudonymised template variable, called by generated code.

    Python counterpart of org.openprovenance.prov.utils.Pseudonyms (prov-model); both must give the same
    answers (conformance vectors in the compiler's tests). The pattern comes from the generated code, where it is fixed
    at generation. re.fullmatch, not search: a pattern's $ would otherwise accept a trailing newline, which Java,
    JavaScript, Rust and PostgreSQL refuse.
    """

    _compiled = {}

    @staticmethod
    def matches(value, pattern):
        if value is None:
            return True
        p = Pseudonyms._compiled.get(pattern)
        if p is None:
            p = re.compile(pattern)
            Pseudonyms._compiled[pattern] = p
        return p.fullmatch(value) is not None

    @staticmethod
    def check(value, pattern, template, variable, method):
        """Refuses a value not in the method's form; the message never carries the value."""
        if not Pseudonyms.matches(value, pattern):
            raise IllegalArgumentException("T31: template " + template + ", variable " + variable
                                           + ": value refused, not in the form of @pseudonym(" + method + ")")
