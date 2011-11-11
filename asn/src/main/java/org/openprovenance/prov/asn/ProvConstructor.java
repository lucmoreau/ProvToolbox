package org.openprovenance.prov.asn;

import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.Activity;

import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import  org.antlr.runtime.Token;
import  org.antlr.runtime.tree.Tree;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;


public  class ProvConstructor {
    ProvFactory pFactory;
    
    public ProvConstructor(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }
            

    public String getTokenString(Tree t) {
        if (t==null) return null;
        return ((CommonTree)t).getToken().getText();
    }
    public void walkToken(String token) {
        System.out.print(token);
    }
    public String convertToken(String token) {
        System.out.print(token);
        return token;
    }
    
    public void walk(Tree ast) {
        switch(ast.getType()) {
		case ASNParser.ACTIVITY:
			System.out.print("ACTIVITY ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			walk(ast.getChild(4));
			System.out.print(";\n");
			break;
		case ASNParser.ENTITY:
			System.out.print("entity ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			System.out.print(";\n");
			break;
		case ASNParser.CONTAINER:
			System.out.print("container ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    walk(ast.getChild(i));
			}
			System.out.print(";\n");
			break;
		case ASNParser.ATTRIBUTES:
			System.out.print("ATTRIBUTES ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    walk(ast.getChild(i));
			}
			break;
		case ASNParser.ID:
			System.out.print("ID ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.ATTRIBUTE:
			System.out.print("ATTRIBUTE ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.START:
			System.out.print("START ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.END:
			System.out.print("END ");
			walkToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			break;
		case ASNParser.A:
            if (ast.getChildCount()==0) {
                walkToken(null);
            } else {
                walk(ast.getChild(0));
            }
			System.out.print(" ");
			break;
		case ASNParser.STRING:
			System.out.print("STRING ");
			walk(ast.getChild(0));
			System.out.print(" ");
			break;
		case ASNParser.USED:
			System.out.print("USED ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			System.out.print(";\n");
			break;
		case ASNParser.WGB:
			System.out.print("WGB ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			System.out.print(";\n");
			break;
		case ASNParser.WDF:
			System.out.print("WDF ");
			walk(ast.getChild(0));
			walk(ast.getChild(1));
			walk(ast.getChild(2));
			walk(ast.getChild(3));
			walk(ast.getChild(4));
			System.out.print(";\n");
			break;


            // ...handle every other possible node type in the AST...
        }
    }

    public Object convert(Tree ast) {
        switch(ast.getType()) {
		case ASNParser.ACTIVITY:
			System.out.print("ACTIVITY ");
			String id=(String)convert(ast.getChild(0));
			convert(ast.getChild(1));
			convert(ast.getChild(2));
			convert(ast.getChild(3));
			convert(ast.getChild(4));
            Activity a=pFactory.newActivity(id);
			System.out.print(";\n");
			return a;
		case ASNParser.ENTITY:
			System.out.print("entity ");
			convert(ast.getChild(0));
			convert(ast.getChild(1));
			System.out.print(";\n");
			return null;
		case ASNParser.CONTAINER:
			System.out.print("container ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    convert(ast.getChild(i));
			}
			System.out.print(";\n");
			return null;
		case ASNParser.ATTRIBUTES:
			System.out.print("ATTRIBUTES ");
			for (int i=0; i< ast.getChildCount(); i++) {
			    convert(ast.getChild(i));
			}
			return null;
		case ASNParser.ID:
			return convertToken(getTokenString(ast.getChild(0)));
		case ASNParser.ATTRIBUTE:
			System.out.print("ATTRIBUTE ");
			convertToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			return null;
		case ASNParser.START:
			System.out.print("START ");
			convertToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			return null;
		case ASNParser.END:
			System.out.print("END ");
			convertToken(getTokenString(ast.getChild(0)));
			System.out.print(" ");
			return null;
		case ASNParser.A:
            if (ast.getChildCount()==0) {
                convertToken(null);
            } else {
                convert(ast.getChild(0));
            }
			System.out.print(" ");
			return null;
		case ASNParser.STRING:
			System.out.print("STRING ");
			convert(ast.getChild(0));
			System.out.print(" ");
			return null;
		case ASNParser.USED:
			System.out.print("USED ");
			convert(ast.getChild(0));
			convert(ast.getChild(1));
			convert(ast.getChild(2));
			convert(ast.getChild(3));
			System.out.print(";\n");
			return null;
		case ASNParser.WGB:
			System.out.print("WGB ");
			convert(ast.getChild(0));
			convert(ast.getChild(1));
			convert(ast.getChild(2));
			convert(ast.getChild(3));
			System.out.print(";\n");
			return null;
		case ASNParser.WDF:
			System.out.print("WDF ");
			convert(ast.getChild(0));
			convert(ast.getChild(1));
			convert(ast.getChild(2));
			convert(ast.getChild(3));
			convert(ast.getChild(4));
			System.out.print(";\n");
			return null;


            // ...handle every other possible node type in the AST...
        }
        return null;
    }



}

