
#from org.openprovenance.prov.template.library.plead.client.logger.Logger import Logger


import json

import sys
sys.path.insert(0, "target/python")

#print(sys.path)

from org.openprovenance.prov.template.library.plead.client.logger.Logger import Logger
from org.openprovenance.prov.template.library.plead.client.common.Plead_approvingBean    import Plead_approvingBean
from org.openprovenance.prov.template.library.plead.client.common.Plead_approvingBuilder import Plead_approvingBuilder

from org.openprovenance.apache.commons.lang.StringEscapeUtils import StringEscapeUtils



if __name__ == "__main__":

    print("in main")


    bean=Plead_approvingBean()
  #  bean.isA=bean.isA
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


    print('----- aArgs2CsVConverter output -----')

    result=bean.process(builder.aArgs2CsVConverter)
    print(result)

    print('-----conversion: record to bean -----')

    print(builder.record2bean(['Plead_approving',100,100,21,18,0.234,112345,22,245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z']))

    print('----- conversion: record to bean, to JSON -----')

    print(builder.record2bean(['Plead_approving',100,100,21,18,0.234,112345,22,245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z']).toJSON())

    print('----- aArgs2RecordConverter output -----')

    result=bean.process(builder.aArgs2RecordConverter())
    print(result)

    print('----- various tables output -----')

    print(Plead_approvingBuilder.allTypes)

    print(builder.getSuccessors().toString())
    print(builder.getTypedSuccessors().toString())

    print('----- examplar -----')

    print(Plead_approvingBuilder.examplar().toJSON())

    print('----- examplar, to array, and the back to bean -----')

    examplar1=Plead_approvingBuilder.examplar()
    print(examplar1.toJSON())
    examplar1Array=examplar1.process(builder.aArgs2RecordConverter())
    print(examplar1Array)
    examplar2=builder.aRecord2BeanConverter(examplar1Array)
    print(examplar2.toJSON())


    print('----- array to csv output (2) -----')

    csv2=builder.aRecord2CsvConverter(examplar1Array)
    print(csv2)



    print('----- assigning unknown field -----')

    bean.luc="foo"

    print(json.dumps(vars(bean)))

    print('----- examplar to sql-----')

    print(examplar1.process(builder.bean2sql()))

    print(examplar1.process(builder.aBean2SqlConverter))

    print('----- conversion: array to csv -----')

    print(builder.processorConverter(builder.aArgs2CsVConverter)(examplar1Array))

    print('----- conversion: array to sql -----')

    print(builder.processorConverter(builder.bean2sql())(examplar1Array))

    print('----- Logger bean creation -----')

    print(Logger.logPlead_approving('approved_pipeline',100,21,18,0.123,'kcl','tdh',245,'sig','/home/plead/workflow/123','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z','2023-11-10T14:09:55.265Z'))







