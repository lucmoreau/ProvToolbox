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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;


/**
 * Reading and writing PROV-N.
 * <p>
 * Documents are read with the ANTLR 4 parser (ProvN.g4), via {@link ProvNReader}: a document that is not valid
 * PROV-N is rejected with a {@link ProvNSyntaxException} (see {@link ProvDeserialiser} for how this parser differs from
 * the ANTLR 3 one, used up to 2.3.0).
 */
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

    /** Reads a PROV-N document from a file, with the ANTLR 4 parser (see {@link ProvNReader#readDocument(String)}). */
    public Object convertSyntaxTreeToJavaBean(String file, ProvFactory pFactory) {
        return readDocument(file, pFactory);
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

    /**
     * Reads a PROV-N document from a file, with the ANTLR 4 parser (see {@link ProvNReader#readDocument(String)}).
     * @throws ProvNSyntaxException if the file does not hold a syntactically valid PROV-N document
     */
    public Document readDocument(String filename, ProvFactory pFactory)  {
        return new ProvNReader(pFactory, dateTimeOption, optionalTimeZone).readDocument(filename);
    }

    /**
     * Reads a PROV-N document from a stream, with the ANTLR 4 parser (see {@link ProvNReader#readDocument(InputStream)}).
     * @throws ProvNSyntaxException if the stream does not hold a syntactically valid PROV-N document
     */
    public Document readDocument(InputStream is, ProvFactory pFactory)  {
        return new ProvNReader(pFactory, dateTimeOption, optionalTimeZone).readDocument(is);
    }



}





