
from org.openprovenance.prov.template.library.plead.logger.Logger import Logger
from org.openprovenance.prov.template.library.plead.client.common.Plead_approvingBuilder import Plead_approvingBuilder
from org.openprovenance.prov.template.library.plead.client.common.Plead_approvingBean    import Plead_approvingBean

import sys
import json

#print(sys.path)

from org.openprovenance.apache.commons.lang.StringEscapeUtils import StringEscapeUtils



if __name__ == "__main__":

    print("in main")


    bean=Plead_approvingBean()
    bean.isA=bean.isA
    bean.approval_record=21
    bean.pipeline=100
    bean.approved_pipeline=9999
    bean.score=21
    bean.organization='kcl'
    bean.manager='tdh'
    bean.approving=245
    bean.signature='sig'
    bean.path='/home/plead/workflow/123'
    bean.time='2023-11-10T14:09:55.265Z'
    bean.start='2023-11-10T14:09:55.265Z'
    bean.end='2023-11-10T14:09:55.265Z'
    print(bean)

    print(json.dumps(vars(bean)))

    print('-----')


    builder=Plead_approvingBuilder()
    result=bean.process(builder.args2csv())
    print(result)

    print('-----')

    print(Logger.logPlead_approving(9999,'approved_pipeline',100,21,0.123,'kcl','tdh',245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z'))
    bean2=Logger.beanPlead_approving(9999,'approved_pipeline',100,21,0.234,'kcl','tdh',245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z')

    print(json.dumps(vars(bean2)))





