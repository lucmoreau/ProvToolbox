

class UnsupportedOperationException (Exception):
    """Exception raised for unsupported operations."""

    def __init__(self, message="Operation not supported"):
        self.message = message
        super().__init__(self.message)


