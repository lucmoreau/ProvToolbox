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
import java.util.TimeZone;

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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.openprovenance.prov.model.exception.UncheckedException;


public class Utility {

    static Logger logger= LogManager.getLogger(Utility.class);
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;

    public Utility() {
        this(DateTimeOption.PRESERVE,null);
    }
    public Utility(DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
    }

    public static void warn(String s) {
        logger.warn(s);
    }

    public static void warn(Exception e) {
        logger.warn(e.getMessage());
    }

    static class ParserWithErrorHandling extends PROV_NParser {
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

    public CommonTree convertSyntaxTreeToTree(String file)  {
        PROV_NParser parser=getParserForFile(file);
        return convertSyntaxTreeToTree(parser);
    }


    public CommonTree convertSyntaxTreeToTree(InputStream is){
        PROV_NParser parser=getParserForStream(is);
        return convertSyntaxTreeToTree(parser);
    }

    private CommonTree convertSyntaxTreeToTree(PROV_NParser parser){
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
        return new TreeTraversal(pFactory,pFactory, dateTimeOption, optionalTimeZone).convert(tree);
    }



    public String convertBeanToHTML(final Document doc, ProvFactory pFactory) {
        StringWriter writer=new StringWriter();
        NotationConstructor nc=new HTMLConstructor(writer);
        BeanTraversal bt=new BeanTraversal(nc, pFactory);
        bt.doAction(withImpliedNamespaces(doc, pFactory));
        nc.flush();
        String s=writer.toString();
        nc.close();
        return s;
    }

    public Object convertSyntaxTreeToJavaBean(String file, ProvFactory pFactory) {
        CommonTree tree= convertSyntaxTreeToTree(file);
        return convertTreeToJavaBean(tree,pFactory);
    }

    /** A conversion function that copies a Java Bean deeply.
     * @param doc a Document to convert
     * @param pFactory a ProvFactory
     * @return a copy of the Document
     * */
    public Object convertJavaBeanToJavaBean(Document doc, ProvFactory pFactory) {
        BeanTraversal bt=new BeanTraversal(pFactory, pFactory);
        return bt.doAction(doc);
    }



    public String convertBeanToSyntaxTree(final Document doc, ProvFactory pFactory) {
        StringWriter writer=new StringWriter();
        NotationConstructor nc=new NotationConstructor(writer);
        BeanTraversal bt=new BeanTraversal(nc, pFactory);
        bt.doAction(withImpliedNamespaces(doc, pFactory));
        nc.flush();
        String s=writer.toString();
        nc.close();
        return s;
    }




    public void convertBeanToSyntaxTree(final Document doc, Writer writer, ProvFactory pFactory) {
        NotationConstructor nc=new NotationConstructor(writer);
        BeanTraversal bt=new BeanTraversal(nc, pFactory);
        bt.doAction(withImpliedNamespaces(doc, pFactory));
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

    static final String PROVEXT_PREFIX = "provext";

    /**
     * The document as PROV-N writes it: with provext declared when a qualified specialization, membership or
     * alternate is written as a provext statement, and openprov when its attributes are written, unless the
     * document declares them itself. A document built in memory, by a template for instance, need not declare
     * either; undeclared, what is written could not be read back. The document given is left as it is.
     */
    public Document withImpliedNamespaces(Document doc, ProvFactory pFactory) {
        ProvUtilities u = new ProvUtilities();
        boolean provext = false, openprov = false;
        List<Statement> statements = new LinkedList<>(u.getStatement(doc));
        List<Bundle> bundles = u.getNamedBundle(doc);
        for (Bundle b : bundles) statements.addAll(u.getStatement(b));
        for (Statement s : statements) {
            provext |= isWrittenAsProvextStatement(s, pFactory);
            openprov |= hasOpenprovAttribute(s);
        }
        Namespace ns = doc.getNamespace();
        boolean declaresProvext = ns != null && ns.getPrefixes().containsKey(PROVEXT_PREFIX);
        boolean declaresOpenprov = ns != null && ns.getPrefixes().containsKey(NamespacePrefixMapper.OPENPROV_PREFIX);
        if ((!provext || declaresProvext) && (!openprov || declaresOpenprov)) return doc;
        Namespace declared = ns == null ? new Namespace() : new Namespace(ns);
        if (provext && !declaresProvext) declared.register(PROVEXT_PREFIX, NamespacePrefixMapper.PROV_EXT_NS);
        if (openprov && !declaresOpenprov) declared.register(NamespacePrefixMapper.OPENPROV_PREFIX, NamespacePrefixMapper.OPENPROV_NS);
        return pFactory.newDocument(declared, u.getStatement(doc), bundles);
    }

    /** As {@link NotationConstructor} decides: a qualified specialization, membership or alternate with an identifier or attributes. */
    static boolean isWrittenAsProvextStatement(Statement s, ProvFactory pFactory) {
        if (!(s instanceof QualifiedSpecializationOf || s instanceof QualifiedHadMember || s instanceof QualifiedAlternateOf)) return false;
        return ((Identifiable) s).getId() != null || !pFactory.getAttributes(s).isEmpty();
    }

    static boolean hasOpenprovAttribute(Statement s) {
        if (!(s instanceof HasOther)) return false;
        for (Other o : ((HasOther) s).getOther()) {
            if (NamespacePrefixMapper.OPENPROV_NS.equals(o.getElementName().getNamespaceURI())) return true;
        }
        return false;
    }

    public void writeDocument(Document doc, String filename, ProvFactory pFactory){
        String s= convertBeanToSyntaxTree(doc, pFactory);
        writeTextToFile(s,filename);
    }


    public void writeDocument(Document doc, OutputStream os, ProvFactory pFactory){
        Writer writer=new OutputStreamWriter(os);
        convertBeanToSyntaxTree(doc, writer, pFactory);

    }

    public Document readDocument(String filename, ProvFactory pFactory)  {
        CommonTree tree = convertSyntaxTreeToTree(filename);
        Object doc=convertTreeToJavaBean(tree,pFactory);
        return (Document)doc;
    }

    public Document readDocument(InputStream is, ProvFactory pFactory)  {
        CommonTree tree = convertSyntaxTreeToTree(is);
        Object doc=convertTreeToJavaBean(tree,pFactory);
        return (Document)doc;
    }



}





