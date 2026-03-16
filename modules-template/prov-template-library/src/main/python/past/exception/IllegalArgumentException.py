

class IllegalArgumentException (Exception):
    """Exception raised for illegal arguments."""

    def __init__(self, message="Illegal argument provided."):
        self.message = message
        super().__init__(self.message)


