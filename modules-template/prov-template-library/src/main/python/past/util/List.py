

class List(list):
    def __init__(self):
        self._list = []

    def put(self, k, v):
        self._list[k]=v
        return self  # Allow method chaining

    def toString(self):
        return self._list

    def clear(self):
        self._list = []

    def append(self, o):
        self._list.append(o)
        return self  # Allow method chaining

    def size(self):
        return len(self._list)

    def get(self, k):
        return self._list[k]

    def __iter__(self):
        yield from self._list


