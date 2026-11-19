# EXPERIMENTAL: in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality; this module may change without notice.
import json
from decimal import Decimal

from past.exception.IllegalArgumentException import IllegalArgumentException


class JsonObject(dict):
    """An ordered JSON object (dict keeps insertion order), with the Map methods the generated code calls."""

    def put(self, key, value):
        self[key] = value
        return self

    def containsKey(self, key):
        return key in self


def _int(text):
    n = int(text)
    # as Java reads it: an integer beyond a long is a double
    return n if -9223372036854775808 <= n <= 9223372036854775807 else float(text)


def _bad_constant(name):
    raise ValueError("not JSON: " + name)


def _pairs(pairs):
    m = JsonObject()
    for k, v in pairs:
        m[k] = v          # a repeated key keeps its first place and its last value
    return m


_decoder = json.JSONDecoder(object_pairs_hook=_pairs, parse_float=float, parse_int=_int,
                            parse_constant=_bad_constant, strict=True)


def _refused(why):
    return IllegalArgumentException("T36: " + why)


def _not_a(key, what):
    return _refused("the member " + key + " is not " + what)


class JsonText:
    """T36: JSON text for the values a template keeps inside a text variable (an @inJson carrier), called by the typed
    beans the template compiler generates for a template's subtypes.

    Python counterpart of org.openprovenance.prov.utils.JsonText (prov-model); both must give the same text for the same
    values: the conformance vectors of the compiler's tests (t36/json-text-vectors.json) are run against each.

    Numbers as RFC 8785 writes them (the ECMAScript Number to string rule), strings escaped as RFC 8785 escapes them, no
    space between tokens, and the members of an object in the order of the map (not sorted).
    """

    @staticmethod
    def newObject():
        return JsonObject()

    # ---- writing ------------------------------------------------------------------------------------------------------

    @staticmethod
    def write(m):
        if m is None:
            raise _refused("JSON text of a null map")
        return JsonText.toText(m)

    @staticmethod
    def toText(value):
        out = []
        JsonText._write_value(out, value)
        return "".join(out)

    @staticmethod
    def text(value):
        """The text of a number or a boolean as the carrier writes it, a string as itself, None as None (T36 part E: the conversion "as text" of a flow)."""
        if value is None:
            return None
        if isinstance(value, str):
            return value
        if isinstance(value, float):
            return JsonText._plain(JsonText.toText(value))
        if isinstance(value, (bool, int)):
            return JsonText.toText(value)
        raise _refused("a value of that kind has no text")

    @staticmethod
    def _plain(text):
        """T42: a number's text without its exponent: the decimal point of RFC 8785's text ('1.2345e+25', '-5e-324') moved by the exponent."""
        e = text.find("e")
        if e < 0:
            return text
        mantissa, exp = text[:e], int(text[e + 1:])
        negative = mantissa.startswith("-")
        if negative:
            mantissa = mantissa[1:]
        dot = mantissa.find(".")
        digits = mantissa if dot < 0 else mantissa[:dot] + mantissa[dot + 1:]
        point = (len(mantissa) if dot < 0 else dot) + exp           # where the decimal point falls in the digits
        sign = "-" if negative else ""
        if point >= len(digits):
            return sign + digits + "0" * (point - len(digits))
        if point > 0:
            return sign + digits[:point] + "." + digits[point:]
        return sign + "0." + "0" * (-point) + digits

    @staticmethod
    def _write_value(out, v):
        if v is None:
            out.append("null")
        elif v is True:
            out.append("true")
        elif v is False:
            out.append("false")
        elif isinstance(v, int):
            out.append(str(v))
        elif isinstance(v, float):
            out.append(JsonText._number(v))
        elif isinstance(v, str):
            JsonText._write_string(out, v)
        elif isinstance(v, dict):
            out.append("{")
            first = True
            for k, x in v.items():
                if not isinstance(k, str):
                    raise _refused("JSON object key is not a string: " + str(k))
                if not first:
                    out.append(",")
                first = False
                JsonText._write_string(out, k)
                out.append(":")
                JsonText._write_value(out, x)
            out.append("}")
        elif isinstance(v, (list, tuple)):
            out.append("[")
            first = True
            for x in v:
                if not first:
                    out.append(",")
                first = False
                JsonText._write_value(out, x)
            out.append("]")
        else:
            raise _refused("not a JSON value: " + type(v).__name__)

    _SHORT = {'"': '\\"', "\\": "\\\\", "\b": "\\b", "\f": "\\f", "\n": "\\n", "\r": "\\r", "\t": "\\t"}

    @staticmethod
    def _write_string(out, s):
        out.append('"')
        for c in s:
            short = JsonText._SHORT.get(c)
            if short is not None:
                out.append(short)
            elif ord(c) < 0x20:
                out.append("\\u%04x" % ord(c))
            else:
                out.append(c)
        out.append('"')

    @staticmethod
    def _number(d):
        if d != d or d in (float("inf"), float("-inf")):
            raise _refused("not a JSON number: " + repr(d))
        if d == 0:
            return "0"
        dec = Decimal(repr(abs(d))).normalize()      # repr is the shortest text that reads back as d
        t = dec.as_tuple()
        digits = "".join(str(x) for x in t.digits)
        k = len(digits)
        n = k + t.exponent                           # the value is 0.<digits> x 10^n
        sign = "-" if d < 0 else ""
        if k <= n <= 21:
            return sign + digits + "0" * (n - k)
        if 0 < n <= 21:
            return sign + digits[:n] + "." + digits[n:]
        if -6 < n <= 0:
            return sign + "0." + "0" * (-n) + digits
        e = n - 1
        return sign + digits[0] + ("." + digits[1:] if k > 1 else "") + "e" + ("-" if e < 0 else "+") + str(abs(e))

    # ---- reading ------------------------------------------------------------------------------------------------------

    @staticmethod
    def read(text):
        """The object a text holds; an absent text (None or blank) is an empty object; any other JSON value is refused."""
        if text is None or text.strip() == "":
            return JsonObject()
        v = JsonText.parse(text)
        if not isinstance(v, dict):
            raise _refused("JSON text is not an object")
        return v

    @staticmethod
    def parse(text):
        # Python's json accepts only these four white space characters, as Java's parser does
        try:
            return _decoder.decode(text)
        except (ValueError, RecursionError) as e:
            raise _refused("JSON text: " + str(e))

    # ---- what the typed beans use -------------------------------------------------------------------------------------

    @staticmethod
    def merge(declared, others, where):
        """The declared members first, then the other keys; a key in both is refused."""
        m = JsonObject(declared)
        if others is not None:
            for k, v in others.items():
                if k in declared:
                    raise _refused(where + ": the key " + k + " is declared, it cannot also be among the other keys")
                m[k] = v
        return m

    @staticmethod
    def others(obj, declared):
        m = JsonObject(obj)
        for k in declared:
            m.pop(k, None)
        return m

    @staticmethod
    def getString(m, key):
        v = m.get(key)
        if v is None:
            return None
        if not isinstance(v, str):
            raise _not_a(key, "a string")
        return v

    @staticmethod
    def getJson(m, key):
        v = m.get(key)
        return None if v is None else JsonText.toText(v)

    @staticmethod
    def getBoolean(m, key):
        v = m.get(key)
        if v is None:
            return None
        if not isinstance(v, bool):
            raise _not_a(key, "true or false")
        return v

    @staticmethod
    def getLong(m, key):
        v = m.get(key)
        if v is None:
            return None
        if isinstance(v, int) and not isinstance(v, bool):
            return v
        if isinstance(v, float) and v == int(v) and abs(v) < 9.007199254740992e15:
            return int(v)
        raise _not_a(key, "an integer")

    @staticmethod
    def getInteger(m, key):
        v = JsonText.getLong(m, key)
        if v is None:
            return None
        if v < -2147483648 or v > 2147483647:
            raise _not_a(key, "an integer that fits an int")
        return v

    @staticmethod
    def getDouble(m, key):
        v = m.get(key)
        if v is None:
            return None
        if isinstance(v, bool) or not isinstance(v, (int, float)):
            raise _not_a(key, "a number")
        return float(v)

    @staticmethod
    def getFloat(m, key):
        return JsonText.getDouble(m, key)

    @staticmethod
    def same(actual, constant):
        """Whether a value is a constant: equal, numbers by value; a boolean is not a number."""
        if isinstance(actual, bool) or isinstance(constant, bool):
            return isinstance(actual, bool) and isinstance(constant, bool) and actual == constant
        return actual == constant
