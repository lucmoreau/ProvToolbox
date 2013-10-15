package org.openprovenance.prov.notation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import  org.antlr.runtime.CommonTokenStream;
import  org.antlr.runtime.ANTLRFileStream;
import  org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import  org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import  org.antlr.runtime.tree.CommonTree;
import  org.antlr.runtime.tree.CommonTreeAdaptor;
import  org.antlr.runtime.tree.TreeAdaptor;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.model.Document;


public  class Utility {
    
    class ParserWithErrorHandling extends PROV_NParser {
	public void reportError(RecognitionException re) {
	    super.reportError(re);
	    errors.add(re);
	}
	
	public List<RecognitionException> errors=new LinkedList<RecognitionException>();

	public ParserWithErrorHandling(TokenStream input) {
	    super(input);
	}
	
    }

    public PROV_NParser getParserForFile(String file) throws java.io.IOException, Throwable {
        CharStream input = new ANTLRFileStream(file);
        PROV_NLexer lex = new PROV_NLexer(input);
        CommonTokenStream tokens = new  CommonTokenStream(lex);
        //PROV_NParser parser = new PROV_NParser(tokens);
        PROV_NParser parser = new ParserWithErrorHandling(tokens);

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
        PROV_NParser.document_return ret = parser.document();
        CommonTree tree = (CommonTree)ret.getTree();
        return tree;
    }

   
    public Object convertTreeToJavaBean(CommonTree tree) {
  	if (tree==null) return null;
  	ProvFactory pFactory=new ProvFactory();
          Object o=new TreeTraversal(pFactory,pFactory).convert(tree);
          return o;
      }



    public String convertBeanToHTML(Document doc) {
	ProvFactory pFactory=new ProvFactory();
	StringWriter writer=new StringWriter();
	NotationConstructor nc=new HTMLConstructor(writer, pFactory);
        BeanTraversal bt=new BeanTraversal(nc, pFactory, new ValueConverter(pFactory));
        bt.convert(doc);
        nc.flush();
        String s=writer.toString();
        nc.close();
        return s;
    }
    
    public Object convertASNToJavaBean(String file) throws java.io.IOException, Throwable {
        CommonTree tree=convertASNToTree(file);
        Object o=convertTreeToJavaBean(tree);
        return o;
    }

    /** A conversion function that copies a Java Bean deeply. */
    public Object convertJavaBeanToJavaBean(Document doc) {
        ProvFactory pFactory=new ProvFactory(doc.getNss());
        BeanTraversal bt=new BeanTraversal(pFactory, pFactory, new ValueConverter(pFactory));
        Document o=bt.convert(doc);
        return o;
    }



    public String convertBeanToASN(Document doc) {
	ProvFactory pFactory=new ProvFactory();
	StringWriter writer=new StringWriter();
	NotationConstructor nc=new NotationConstructor(writer, pFactory);
        BeanTraversal bt=new BeanTraversal(nc, pFactory, new ValueConverter(pFactory));
        bt.convert(doc);
        nc.flush();
        String s=writer.toString();
        nc.close();
        return s;
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
    
    public void writeTextToFile(String text,
                                String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(text);
        }
        catch (IOException e) {
        }
        finally {
            try {
                if (writer != null)
                    writer.close( );
            }
            catch (IOException e) {}
        }
    }
    
    public void writeDocument(Document doc, String filename){
	String s=convertBeanToASN(doc);
	System.out.println("printing" + s);
        writeTextToFile(s,filename);
    }
    
    public Document readDocument(String filename) throws IOException, Throwable {
	 CommonTree tree = convertASNToTree(filename);
         Object doc=convertTreeToJavaBean(tree);
         return (Document)doc;
    }
    
    
 
}





