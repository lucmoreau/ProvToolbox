
from org.example.templates.block.client.common.Template_blockBuilder import Template_blockBuilder
from org.example.templates.block.client.common.Template_blockBean import Template_blockBean
from org.example.templates.block.logger.Logger import Logger

import sys
import json

print(sys.path)

from org.openprovenance.apache.commons.lang.StringEscapeUtils import StringEscapeUtils



if __name__ == "__main__":

    print("in main")


    bean=Template_blockBean()
    bean.operation='operation'
    bean.operation_type='operation_type'
    bean.agent='agent'
    bean.consumed_value1='10'
    bean.consumed2='consumed2' 
    bean.consumed_value2_IGNORE='15'
    bean.produced='produced'
    bean.produced_value='25'
    bean.produced_type='java.lang.Integer'
    print(bean)


    builder=Template_blockBuilder()
    result=bean.process(builder.args2csv())
    print(result)


    print(Logger.logTemplate_block('operation','operation_type','agent','consumed1','10','consumed2','15','produced','java.lang.Integer','25'))
    bean=Logger.beanTemplate_block('operation','operation_type','agent','consumed1','10','consumed2','15','produced','java.lang.Integer','25')
    print(bean)
    
    #print(json.dumps(bean))


