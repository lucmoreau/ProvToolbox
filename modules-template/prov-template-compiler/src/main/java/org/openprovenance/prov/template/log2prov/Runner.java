package org.openprovenance.prov.template.log2prov;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerUtil;

public class Runner {
    
    final static boolean debug=false;
    
    public Runner (ProvFactory pf) {
        this.pf=pf;
    }

    public static void main (String [] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String in=args[0];
        String out=args[1];
        String flag=args[2];
        if (debug) System.out.println("Start main"); 
        new Runner(org.openprovenance.prov.xml.ProvFactory.getFactory()).processLogWithTime(in, out, flag); 
        if (debug) System.out.println("End main"); 
    }

    final private ProvFactory pf;
    
    final CompilerUtil cu=new CompilerUtil();
    
    
    public void processLogWithTime(String in, String out, String flag) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        long currentTime1=System.currentTimeMillis();
        long currentTime2=processLog(in, out, flag); 
        long currentTime3=System.currentTimeMillis();
        if (debug) System.out.println("Expand/merge time " + (currentTime2-currentTime1)); 
        if (debug) System.out.println("Save time         " + (currentTime3-currentTime2));
    }

    public long processLog(String in, String out, String flag) throws FileNotFoundException,
                                                               IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        org.openprovenance.prov.model.IndexedDocument iDoc=new org.openprovenance.prov.model.IndexedDocument(pf,pf.newDocument(),true);


        if (debug) System.out.println("IN: " + in);
        InputStream is= ("-".equals(in))? System.in: new FileInputStream(in);

        DocumentProcessor dp=null;
        if ("-merge".equals(flag)) dp=new DocumentMergerProcessor(iDoc);
        if ("-nomerge".equals(flag)) dp=new DocumentConcatenatorProcessor(pf.newDocument());;
        if ("-discard".equals(flag)) dp=new DocumentDiscarderProcessor(pf.newDocument());;

        FileBuilder.reader(is,dp);

        is.close();



        long currentTime2=System.currentTimeMillis();
        cu.writeDocument(out,dp.getDocument());
        return currentTime2;
    }
}
