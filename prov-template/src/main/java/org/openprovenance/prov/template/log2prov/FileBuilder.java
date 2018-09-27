package org.openprovenance.prov.template.log2prov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.openprovenance.prov.model.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class FileBuilder {
    static ObjectMapper mapper = new ObjectMapper();

    public static void reader(InputStream is, DocumentProcessor dp) {
        BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        reader(r, dp);
    }

    public static void reader(BufferedReader r, DocumentProcessor dp) {
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

    public static HashMap<String,FileBuilder> registry=new HashMap<String, FileBuilder>();
    
    public static void register(FileBuilder builder) {
        registry.put(builder.getName(),builder); 
    }

    abstract public String getName();
    
    public static boolean registerBuilders(String[] builders, org.openprovenance.prov.model.ProvFactory pf) {
        boolean ok=true;
        for (String builder: builders) {
            Class<?> cl;
            try {
                cl = Class.forName(builder);
                cl.getDeclaredConstructor(org.openprovenance.prov.model.ProvFactory.class).newInstance(pf);
            } catch (ClassNotFoundException e) {
                ok=false;
                e.printStackTrace();
            } catch (InstantiationException e) {
                ok=false;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                ok=false;
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                ok=false;
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                ok=false;
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                ok=false;
                e.printStackTrace();
            } catch (SecurityException e) {
                ok=false;
                e.printStackTrace();
            }

        }
        return ok;
    }



}
