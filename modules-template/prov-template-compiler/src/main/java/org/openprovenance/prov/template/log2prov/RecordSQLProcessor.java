package org.openprovenance.prov.template.log2prov;

import java.util.HashMap;
import java.util.Map;

public class RecordSQLProcessor implements RecordProcessor {

    Map<String,String> headers=new HashMap<>();

    StringBuffer sb=new StringBuffer();

    String previous=null;

    public RecordSQLProcessor(String configuration) {
    }



    public void process(String methodName, String args) {

        if ((previous!=null) && (!(previous.equals(methodName)))) {
            sb.append("\n;\n");  // closing previous one.
            previous=null;
        }
        if ((previous!=null) && (previous.equals(methodName))) {
            sb.append(",\n");
        }
        if (previous==null) {
            sb.append(headers.get(methodName));
            sb.append(" VALUES \n");
            previous=methodName;
        }

        sb.append("  ");
        sb.append(args);
    }

    @Override
    public void insertHead(String finalMethodName, String head) {
        headers.put(finalMethodName,head);
    }

    @Override
    public StringBuffer getDocument() {
        return sb;
    }

    @Override
    public void initDb() {
        sb.append("--- init statement to come here\n");
    }

    @Override
    public void end() {
        if (previous!=null) {
            sb.append("\n;\n");
        }
        sb.append("--- end of generation\n");
    }
}
