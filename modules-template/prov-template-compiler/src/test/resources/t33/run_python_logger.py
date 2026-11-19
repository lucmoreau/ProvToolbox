# T33: the generated Python logger of the T36 fixture (order_note is an @escape field) writes records, absent values included.
# usage: python3 run_python_logger.py <python source root>...   Prints "absent: <record>" and "present: <record>".
import sys
for p in sys.argv[1:]:
    sys.path.insert(0, p)
from org.example.t36.catalogue.client.logger.Logger import Logger
print('absent: ' + Logger.logApproval(None, 'ex:agent', None, None, 'o1', None, None, 3))
print('present: ' + Logger.logApproval('a1', 'ex:agent', 'Ann', 'ref-1', 'o1', 'a, "note"', '{}', 3))
