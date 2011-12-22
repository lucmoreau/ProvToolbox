package org.openprovenance.prov.asn;
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


public  class Utility {

    public ASNParser getParserForFile(String file) throws java.io.IOException, Throwable {
        CharStream input = new ANTLRFileStream(file);
        ASNLexer lex = new ASNLexer(input);
        CommonTokenStream tokens = new  CommonTokenStream(lex);
        ASNParser parser = new ASNParser(tokens);

        return parser;
    }

    static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
            public Object create(Token payload) {
                return new CommonTree(payload);
            }
        };

    public CommonTree convertASNToTree(String file) throws java.io.IOException, Throwable {
        ASNParser parser=getParserForFile(file);

        parser.setTreeAdaptor(adaptor);
        ASNParser.container_return ret = parser.container();
        CommonTree tree = (CommonTree)ret.getTree();
        return tree;
    }

    public Object convertTreeToJavaBean(CommonTree tree) {
        Object o=new TreeTraversal(new ProvConstructor(ProvFactory.getFactory())).convert(tree);
        return o;
    }

    public String convertTreeToASN(CommonTree tree) {
        Object o=new TreeTraversal(new ASNConstructor()).convert(tree);
        return (String)o;
    }

    public Object convertASNToJavaBean(String file) throws java.io.IOException, Throwable {
        CommonTree tree=convertASNToTree(file);
        Object o=convertTreeToJavaBean(tree);
        return o;
    }

    /** A conversion function that copies a Java Bean deeply. */
    public Object convertJavaBeanToJavaBean(Container c) {
        ProvConstructor pc=new ProvConstructor(new ProvFactory(c.getNss()));
        pc.namespaceTable.putAll(c.getNss());
        BeanTraversal bt=new BeanTraversal(new BeanTreeConstructor(pc));
        Object o=bt.convert(c);
        return o;
    }

    
    public String convertASNToASN(String file) throws java.io.IOException, Throwable {
        CommonTree tree=convertASNToTree(file);
        Object o=convertTreeToASN(tree);
        return (String)o;
    }

    public String convertBeanToASN(Container c) {
        BeanTraversal bt=new BeanTraversal(new BeanTreeConstructor(new ASNConstructor()));
        Object o=bt.convert(c);
        return (String)o;
    }

    /* from http://www.antlr.org/wiki/display/ANTLR3/Interfacing+AST+with+Java */
    public void printTree(CommonTree t, int indent) {
        if ( t != null ) {
            StringBuffer sb = new StringBuffer(indent);
            for ( int i = 0; i < indent; i++ )
                sb = sb.append("   ");
            for ( int i = 0; i < t.getChildCount(); i++ ) {
                System.out.println("" + i + sb.toString() + t.getChild(i).toString());
                printTree((CommonTree)t.getChild(i), indent+1);
            }
        }
    }


}





