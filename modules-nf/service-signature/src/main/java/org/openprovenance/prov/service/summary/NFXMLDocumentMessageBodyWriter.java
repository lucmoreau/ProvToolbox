package org.openprovenance.prov.service.summary;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.interop.Formats;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.ProvFactory;

import org.openprovenance.prov.scala.nf.DocumentProxyFromStatements;
import org.openprovenance.prov.scala.nf.xml.XmlNfBean;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


@Provider
@Produces({ InteropMediaType.MEDIA_TEXT_XML})
public class NFXMLDocumentMessageBodyWriter implements MessageBodyWriter<DocumentProxyFromStatements> {
    
    public String trimCharSet(MediaType mediaType) {
        String med=mediaType.toString();
        int ind=med.indexOf(";");
        if (ind>0) med=med.substring(0,ind);
        return med;
    }

    public NFXMLDocumentMessageBodyWriter() {
        System.out.println("*********** NF XML DocumentMessageBodyWriter  ************");
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        if (type==DocumentProxyFromStatements.class) return true;
        return false;
    }

    @Override
    public long getSize(DocumentProxyFromStatements t, Class<?> type, Type genericType,
    		Annotation[] annotations, MediaType mediaType) {
    	return -1;
    }
    
    TransformerFactory factory = TransformerFactory.newInstance();
    
    public Transformer getTransformer()  {
        StreamSource xslStream = new StreamSource(this.getClass().getClassLoader().getResourceAsStream("removewrapper.xslt"));
        Transformer transformer;
        try {
            transformer = factory.newTransformer(xslStream);
            System.out.println("***** Loading xsl file");
        } catch (TransformerConfigurationException e) {
            transformer=null;
            e.printStackTrace();
        }
        return transformer;
    }

    Transformer transformer=getTransformer();

    @Override
    /*
    public void writeTo(final DocumentProxyFromStatements doc, Class<?> type, Type genericType,
    		            Annotation[] annotations, MediaType mediaType,
    		            MultivaluedMap<String, Object> httpHeaders,
    		            OutputStream out) throws IOException, WebApplicationException {
        InteropFramework intF=new InteropFramework(ProvFactory.getFactory());
        ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));

        System.out.println(" ---- NF writeTo doc " + format + " " + mediaType);
        
        final PipedOutputStream pipedOut = new PipedOutputStream();
        PipedInputStream pipedIn = new PipedInputStream();
        try {
            pipedOut.connect(pipedIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ExecutorService service = Executors.newFixedThreadPool(1);

        service.execute(new Runnable() {
            @Override
            public void run() {
                String id="123";
                XmlNfBean.toXMLOutputStream(doc,pipedOut,id); 
                try {
                    pipedOut.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }});
        
         
        StreamSource inS = new StreamSource(pipedIn);
        StreamResult outS = new StreamResult(out);

        try {
            transformer.transform(inS, outS);
             
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/
    
    public void writeTo(DocumentProxyFromStatements doc, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream out) throws IOException, WebApplicationException {
        InteropFramework intF=new InteropFramework(ProvFactory.getFactory());
        Formats.ProvFormat format=intF.mimeTypeRevMap.get(trimCharSet(mediaType));

        System.out.println(" ---- NF writeTo doc " + format + " " + mediaType);
        
        String id="123";
        XmlNfBean.toXMLOutputStream(doc,out,id);

    }
}
