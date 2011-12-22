package org.openprovenance.prov.asn;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.Container;

import javax.xml.bind.JAXBException;
import java.io.File;

import  org.antlr.runtime.tree.CommonTree;



public  class MainParser {



    public static void main(String[] args)  {
        try {
            Utility u=new Utility();

            CommonTree tree = u.parseASNTree(args[0]);

            u.printTree(tree,1);
            
            System.out.println(tree.toStringTree());


            Object o1=new TreeTraversal(new NullConstructor()).convert(tree);

            Object o2=u.convertToJavaBean(tree);

            String o3=u.convertToASN(tree);


            try {
                ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
                serial.serialiseContainer(new File(args[0] + ".xml"),(Container)o2,true);

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