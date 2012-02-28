package org.openprovenance.prov.datalog;
import  org.openprovenance.prov.asn.Utility;
import  org.openprovenance.prov.asn.TreeTraversal;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Hashtable;

import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import  org.antlr.runtime.Token;
import  org.antlr.runtime.tree.Tree;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.Container;
import org.openprovenance.prov.xml.BeanTraversal;


public  class DataLogUtility extends Utility {

    public String convertTreeToDatalog(CommonTree tree) {
        Object o=new TreeTraversal(new DataLogConstructor()).convert(tree);
        return (String)o;
    }


    static public String asn2datalog(String file, String file2) throws java.io.IOException, javax.xml.bind.JAXBException, Throwable {

	DataLogUtility u=new DataLogUtility();
        CommonTree tree = u.convertASNToTree(file);

        String s=u.convertTreeToDatalog(tree);

	return s;

    }


}





