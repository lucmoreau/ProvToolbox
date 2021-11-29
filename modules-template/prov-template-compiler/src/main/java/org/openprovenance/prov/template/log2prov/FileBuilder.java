package org.openprovenance.prov.template.log2prov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyBeanInterface;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyClientAccessorInterface;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyMakerInterface;
import org.openprovenance.prov.template.log2prov.interfaces.ProxySQLInterface;
import org.openprovenance.prov.template.types.TypesRecordProcessor;

abstract public class FileBuilder {
    static ObjectMapper mapper = new ObjectMapper();

    static Logger logger= LogManager.getLogger(FileBuilder.class);


    public static void reader(InputStream is, DocumentProcessor dp) {
        //BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        try {
            reader(is, dp, null);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("io problem", e);
        }
    }

    static ProxyManagement pm=new ProxyManagement();

    public static void reader(InputStream r, DocumentProcessor dp, RecordProcessor rp) throws IOException {
        reader(r,dp,rp,null);
    }
    public static void reader(InputStream r, DocumentProcessor dp, RecordProcessor rp, TypesRecordProcessor tp) throws IOException {

        CSVParser parser = CSVParser.parse(r, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records=parser.getRecords();

        Map<String, String> sqlInsert=new HashMap<>();

        final Map<QualifiedName, Set<String>> knownTypeMap = new HashMap<>();
        final Map<QualifiedName, Set<String>> unknownTypeMap = new HashMap<>();


        for (CSVRecord record: records) {
            int size=record.size();
            Object[] args=new Object[size];
            String methodName=null;
            for (int i=0; i<size; i++ ) {
                String s = record.get(i);
                if (i==0) {
                    methodName=s;
                }
                if (s==null || s.isEmpty()) {
                    args[i] = null;
                } else {
                    args[i] = s;
                }
            }
            FileBuilder builder=registry.get(methodName);
            Object remoteClientBuilder=clientRegistry.get(methodName);
            ProxySQLInterface clientBuilder=pm.facadeProxy(ProxySQLInterface.class,remoteClientBuilder);
            if (builder==null) {
                logger.info("unknown method " + methodName + " in "+ registry);
                // skip the ones we don't understand
                //return utils.composeResponseBadRequest("incorrect template " + method + " for builders " + builders, new UnsupportedOperationException(method));
            } else {
                if (dp!=null) dp.process(builder.make(args));
                if (rp!=null) {
                    processRecordForSql(rp, sqlInsert, args, methodName, clientBuilder);
                }
                if (tp!=null) {

                    ProxyMakerInterface makerBuilder=pm.facadeProxy(ProxyMakerInterface.class,builder);
                    Object typeManager=makerBuilder.getTypeManager(knownTypeMap,unknownTypeMap);

                    makerBuilder.make(args,typeManager);

                    tp.process(methodName,args);
                }
            }
        }
        if (dp!=null) dp.end();
        if (rp!=null) rp.end();
        if (tp!=null) tp.end(knownTypeMap,unknownTypeMap);
    }

    private static void processRecordForSql(RecordProcessor rp, Map<String, String> sqlInsert, Object[] args, String methodName, ProxySQLInterface clientBuilder) {
        Object bean2sqlFun= clientBuilder.bean2sql();
        String finalMethodName = methodName;
        String insert= sqlInsert.computeIfAbsent(methodName, (s -> {
            String tmp= clientBuilder.getSQLInsert();
            rp.insertHead(finalMethodName,tmp);
            return tmp;
        }));
        Object bean= clientBuilder.toBean(args);
        ProxyBeanInterface proxyBean=pm.facadeProxy(ProxyBeanInterface.class,bean);
        rp.process(methodName, (String)proxyBean.process(bean2sqlFun));
    }

    public static void Oldreader(BufferedReader r, DocumentProcessor dp) {
        String line;
        try {
            line = r.readLine();
            while(line!=null) {
                try {
                   // System.out.println(line);
                    Object[] objects = mapper.readValue(line, Object[].class);
                    String methodName=(String)objects[0];
                    FileBuilder builder=registry.get(methodName);
                    if (builder!=null) {
                        dp.process(builder.make(objects));
                        
                    } else {
                        System.out.println("unknown method " + methodName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                line= r.readLine();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }
    
    public abstract Document make(Object[] objects);

    private static HashMap<String,FileBuilder> registry= new HashMap<>();
    public static final HashMap<String,Object> clientRegistry= new HashMap<>();



    public static void register(FileBuilder builder) {
        registry.put(builder.getName(),builder);
        clientRegistry.put(builder.getName(), pm.facadeProxy(ProxyClientAccessorInterface.class,builder).getClientBuilder());
    }

    abstract public String getName();
    
    public static boolean registerBuilders(String[] builders, org.openprovenance.prov.model.ProvFactory pf) {
        boolean ok=true;
        for (String builder: builders) {
            Class<?> cl;
            try {
                cl = Class.forName(builder);
                cl.getDeclaredConstructor(org.openprovenance.prov.model.ProvFactory.class).newInstance(pf);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                ok=false;
                e.printStackTrace();
            }

        }
        return ok;
    }

    public Integer toInt(Object v) {
        if (v==null) return 0;
        if (v instanceof Integer) return (Integer)v;
        return Integer.valueOf(v.toString());
    }
    public Long toLong(Object v) {
        if (v==null) return 0L;
        if (v instanceof Long) return (Long)v;
        return Long.valueOf(v.toString());
    }
    public Float toFloat(Object v) {
        if (v==null) return 0.0f;
        if (v instanceof Float) return (Float) v;
        return Float.valueOf(v.toString());
    }
    public Double toDouble(Object v) {
        if (v==null) return 0.0;
        if (v instanceof Double) return (Double) v;
        return Double.valueOf(v.toString());
    }
    public Boolean toBoolean(Object v) {
        if (v==null) return false;
        if (v instanceof Boolean) return (Boolean) v;
        return Boolean.valueOf(v.toString());
    }
    public void register(Map<String, FileBuilder> m) {
        m.put(getName(),this);
    }

    private HashMap<String,String> variableMap=new HashMap<>();

    public void setVariableMap(HashMap<String,String> m) {
        this.variableMap=m;
    }
    public HashMap<String,String> getVariableMap() {
        return variableMap;
    }


}
