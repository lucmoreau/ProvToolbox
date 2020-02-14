package org.openprovenance.prov.notation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.apache.log4j.Logger;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.exception.UncheckedException;


public  class Utility {

    static Logger logger=Logger.getLogger(Utility.class);

    public static void warn(String s) {
        logger.warn(s);
    }

    public static void warn(Exception e) {
        logger.warn(e.getMessage());
    }

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

    public PROV_NParser getParserForFile(String file) {
        try {
            CharStream input = new ANTLRFileStream(file);
            return getParserForCharStream(input);
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public PROV_NParser getParserForStream(InputStream is) {
        try {
            CharStream input = new ANTLRInputStream(is);
            return getParserForCharStream(input);
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }
    public PROV_NParser getParserForCharStream(CharStream input) throws java.io.IOException {

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

    public CommonTree convertASNToTree(String file)  {
        PROV_NParser parser=getParserForFile(file);
        return convertASNToTree(parser);
    }


    public CommonTree convertASNToTree(InputStream is){
        PROV_NParser parser=getParserForStream(is);
        return convertASNToTree(parser);
    }

    private CommonTree convertASNToTree(PROV_NParser parser){
        parser.setTreeAdaptor(adaptor);
        PROV_NParser.document_return ret = null;
        try {
            ret = parser.document();
        } catch (RecognitionException e) {
            throw new UncheckedException(e);
        }
        CommonTree tree = (CommonTree)ret.getTree();
        return tree;
    }


    public Object convertTreeToJavaBean(CommonTree tree, ProvFactory pFactory) {
        if (tree==null) return null;
        Object o=new TreeTraversal(pFactory,pFactory).convert(tree);
        return o;
    }



    public String convertBeanToHTML(final Document doc, ProvFactory pFactory) {
        StringWriter writer=new StringWriter();
        NotationConstructor nc=new HTMLConstructor(writer);
        BeanTraversal bt=new BeanTraversal(nc, pFactory);
        bt.doAction(doc);
        nc.flush();
        String s=writer.toString();
        nc.close();
        return s;
    }

    public Object convertASNToJavaBean(String file, ProvFactory pFactory) {
        CommonTree tree=convertASNToTree(file);
        Object o=convertTreeToJavaBean(tree,pFactory);
        return o;
    }

    /** A conversion function that copies a Java Bean deeply. */
    public Object convertJavaBeanToJavaBean(Document doc, ProvFactory pFactory) {
        BeanTraversal bt=new BeanTraversal(pFactory, pFactory);
        Document o=bt.doAction(doc);
        return o;
    }



    public String convertBeanToASN(final Document doc, ProvFactory pFactory) {
        StringWriter writer=new StringWriter();
        NotationConstructor nc=new NotationConstructor(writer);
        BeanTraversal bt=new BeanTraversal(nc, pFactory);
        bt.doAction(doc);
        nc.flush();
        String s=writer.toString();
        nc.close();
        return s;
    }




    public void convertBeanToASN(final Document doc, Writer writer, ProvFactory pFactory) {
        NotationConstructor nc=new NotationConstructor(writer);
        BeanTraversal bt=new BeanTraversal(nc, pFactory);
        bt.doAction(doc);
        nc.flush();
        // nc.close();
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
        try {
            writeTextToFile(text, new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeTextToFile(String text,
                                Writer out) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(out);
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

    public void writeDocument(Document doc, String filename, ProvFactory pFactory){
        String s=convertBeanToASN(doc, pFactory);
        writeTextToFile(s,filename);
    }


    public void writeDocument(Document doc, OutputStream os, ProvFactory pFactory){
        Writer writer=new OutputStreamWriter(os);
        convertBeanToASN(doc, writer, pFactory);

    }

    public Document readDocument(String filename, ProvFactory pFactory)  {
        CommonTree tree = convertASNToTree(filename);
        Object doc=convertTreeToJavaBean(tree,pFactory);
        return (Document)doc;
    }

    public Document readDocument(InputStream is, ProvFactory pFactory)  {
        CommonTree tree = convertASNToTree(is);
        Object doc=convertTreeToJavaBean(tree,pFactory);
        return (Document)doc;
    }



}





