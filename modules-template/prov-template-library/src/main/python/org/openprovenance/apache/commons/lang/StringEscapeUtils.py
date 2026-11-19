

class StringEscapeUtils:
    """The part of Apache Commons Lang's StringEscapeUtils (2.4) that the generated code calls, as Java gives it."""

    CSV_SEARCH_CHARS = ',"\r\n'

    @staticmethod
    def escapeCsv(x):
        """Escapes a string for a CSV column: unchanged unless it holds a comma, a double quote, a carriage return or a line feed, in which
        case it is enclosed in double quotes, each double quote doubled. None is None (Java's null). The conformance vectors are
        escape-csv-vectors.json of prov-template-compiler's tests, run by Java, Python and the JSweet candy."""
        if x is None:
            return None
        if not isinstance(x, str):
            x = str(x)
        if not any(c in x for c in StringEscapeUtils.CSV_SEARCH_CHARS):
            return x
        return '"' + x.replace('"', '""') + '"'
