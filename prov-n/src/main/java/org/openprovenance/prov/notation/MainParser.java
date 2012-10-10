package org.openprovenance.prov.notation;

import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.Document;

import javax.xml.bind.JAXBException;
import java.io.File;

import  org.antlr.runtime.tree.CommonTree;



public  class MainParser {



    public static void main(String[] args)  {
        try {
            Utility u=new Utility();

            CommonTree tree = u.convertASNToTree(args[0]);

            u.printTree(tree,1);
            
            System.out.println(tree.toStringTree());

            @SuppressWarnings("unused")
            Object o1=new TreeTraversal(new NullConstructor()).convert(tree);

            Object o2=u.convertTreeToJavaBean(tree);

	    String o3=u.convertTreeToASN(tree);


            try {
                ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
                serial.serialiseDocument(new File(args[0] + ".xml"),(Document)o2,true);

                System.out.println("tree is " + o3);
            } catch (JAXBException e) {
                e.printStackTrace();
            }


        }  catch(Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
        }
    }
}