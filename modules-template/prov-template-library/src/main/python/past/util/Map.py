

class Map:
    def __init__(self):
        self._dict = {}

    def put(self, k, v):
        self._dict[k]=v
        return self  # Allow method chaining

    def toString(self):
        return self._dict

    def clear(self):
        self._dict = []

    def get(self, k):
        #print ("Getting key:", k)
        return self._dict[k]

    def containsKey(self, k):
        return k in self._dict





