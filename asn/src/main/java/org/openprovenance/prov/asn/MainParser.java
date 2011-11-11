package org.openprovenance.prov.asn;

import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import  org.antlr.runtime.Token;
import  org.antlr.runtime.tree.Tree;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;


public  class MainParser {
    static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
            public Object create(Token payload) {
                return new CommonTree(payload);
            }
        };
    
    public ASNParser getParserForFile(String file) throws java.io.IOException, Throwable {
        CharStream input = new ANTLRFileStream(file);
        ASNLexer lex = new ASNLexer(input);
        CommonTokenStream tokens = new  CommonTokenStream(lex);
        ASNParser parser = new ASNParser(tokens);

        return parser;
    }

    public void walk(Tree ast) {
	switch(ast.getType()) {
		case ASNParser.ACTIVITY:
			System.out.print("activity ");
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			System.out.print(";\n\n");
			break;
		case ASNParser.ENTITY:
			System.out.print("entity ");
			walk(ast.getChild(1));
			System.out.print(";\n\n");
			break;
		case ASNParser.CONTAINER:
			System.out.print("container ");
			for (int i=1; i< ast.getChildCount(); i++) {
			    walk(ast.getChild(i));
			}
			System.out.print(";\n\n");
			break;


       // ...handle every other possible node type in the AST...
	}
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

    public static void main(String[] args)  {
        try {
            MainParser p=new MainParser();
            ASNParser parser=p.getParserForFile(args[0]);

            parser.setTreeAdaptor(adaptor);
            ASNParser.container_return ret = parser.container();
            CommonTree tree = (CommonTree)ret.getTree();

            p.printTree(tree,1);
            
            System.out.println(tree.toStringTree());

	    p.walk(tree);

        }  catch(Throwable t) {
            System.out.println("exception: "+t);
            t.printStackTrace();
        }
    }
}