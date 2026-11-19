from past.util.Map import Map
from past.util.List import List


"""The rules of the flow org.example.t36flow.flows.RepriceFlow: one method for each value the application computes, with the beans in scope in the step that uses it.
A rule is the application's: the flow copies nothing for these values."""
class RepriceFlowRules:
    def __init__(self):
        pass

    def toJSON(self):
        import json
        d = {('__elements' if k == 'elements' else k): v
            for k, v in self.__dict__.items()}
        return json.dumps(
            d,
            default=lambda o: o.__dict__,
            sort_keys=False,
            indent=4)

    """The rule for agent, in the step ask."""
    def caller(self, asked, documentMade):
        pass

    """The rule for time, in the step ask."""
    def clock(self, asked, documentMade):
        pass

    """The rule for estimate, in the step record."""
    def estimate(self, asked, recorded, document, documentMade, line, lineMade):
        pass

    """The rule for erpId1, in the step record."""
    def version(self, asked, recorded, document, documentMade, line, lineMade):
        pass
