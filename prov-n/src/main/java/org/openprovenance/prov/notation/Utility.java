package org.openprovenance.prov.notation;
import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import  org.antlr.runtime.Token;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.BeanTraversal;


public  class Utility {

    public PROV_NParser getParserForFile(String file) throws java.io.IOException, Throwable {
        CharStream input = new ANTLRFileStream(file);
        PROV_NLexer lex = new PROV_NLexer(input);
        CommonTokenStream tokens = new  CommonTokenStream(lex);
        PROV_NParser parser = new PROV_NParser(tokens);

        return parser;
    }

    static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
            public Object create(Token payload) {
                return new CommonTree(payload);
            }
        };

    public CommonTree convertASNToTree(String file) throws java.io.IOException, Throwable {
        PROV_NParser parser=getParserForFile(file);

        parser.setTreeAdaptor(adaptor);
        PROV_NParser.bundle_return ret = parser.bundle();
        CommonTree tree = (CommonTree)ret.getTree();
        return tree;
    }

    public Object convertTreeToJavaBean(CommonTree tree) {
	if (tree==null) return null;
        Object o=new TreeTraversal(new ProvConstructor(ProvFactory.getFactory())).convert(tree);
        return o;
    }

    public String convertTreeToASN(CommonTree tree) {
        Object o=new TreeTraversal(new NotationConstructor()).convert(tree);
        return (String)o;
    }


    public String convertTreeToHTML(CommonTree tree) {
        Object o=new TreeTraversal(new HTMLConstructor()).convert(tree);
        return (String)o;
    }

    public Object convertASNToJavaBean(String file) throws java.io.IOException, Throwable {
        CommonTree tree=convertASNToTree(file);
        Object o=convertTreeToJavaBean(tree);
        return o;
    }

    /** A conversion function that copies a Java Bean deeply. */
    public Object convertJavaBeanToJavaBean(Document c) {
        ProvFactory pFactory=new ProvFactory(c.getNss());
        ProvConstructor pc=new ProvConstructor(pFactory);
        pc.namespaceTable.putAll(c.getNss());
        BeanTraversal bt=new BeanTraversal(new BeanTreeConstructor(pFactory,pc));
        Object o=bt.convert(c);
        return o;
    }

    
    public String convertASNToASN(String file) throws java.io.IOException, Throwable {
        CommonTree tree=convertASNToTree(file);
        Object o=convertTreeToASN(tree);
        return (String)o;
    }

    public String convertBeanToASN(Document c) {
        BeanTraversal bt=new BeanTraversal(new BeanTreeConstructor(ProvFactory.getFactory(), new NotationConstructor()));
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





