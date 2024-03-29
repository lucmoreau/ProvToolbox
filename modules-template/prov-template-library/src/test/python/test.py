
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
    bean.approved_pipeline='approved_pipeline'
    bean.approval_record=100
    bean.pipeline='pipeline'
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


    builder=Plead_approvingBuilder()
    result=bean.process(builder.args2csv())
    print(result)


    print(Logger.logPlead_approving('approved_pipeline',100,'pipeline',21,'kcl','tdh',245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z'))
    bean2=Logger.beanPlead_approving('approved_pipeline',100,'pipeline',21,'kcl','tdh',245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z')
    print(bean2)
    
    #print(json.dumps(bean))


