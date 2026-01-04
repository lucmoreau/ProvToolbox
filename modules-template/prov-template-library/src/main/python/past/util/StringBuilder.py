

class StringBuilder:
    def __init__(self):
        self._strings = []

    def append(self, s):
        self._strings.append(str(s))
        return self  # Allow method chaining

    def toString(self):
        return ''.join(self._strings)

    def clear(self):
        self._strings = []

    def length(self):
        return sum(len(s) for s in self._strings)


