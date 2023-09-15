package org.openprovenance.prov.service.summary;

import jakarta.ws.rs.Consumes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.vanilla.ProvFactory;

import org.openprovenance.prov.scala.immutable.Document;

@Provider
@Consumes({ InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION,
	        InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG,
	        InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON })
abstract public class DocumentMessageBodyReader implements MessageBodyReader<Document> {
    
    final static Class<Document> classDocument=Document.class;
    final boolean copy=true;
    
    
    public DocumentMessageBodyReader () {
        System.out.println("*********** DocumentMessageBodyReader  ************");
    }




    @Override
    public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        return arg0==classDocument;
    }

    @Override
    public Document readFrom(Class<Document> arg0,
                             Type arg1,
                             Annotation[] arg2,
                             MediaType media,
                             MultivaluedMap<String, String> arg4,
                             InputStream in) throws IOException, WebApplicationException {
        ProvFactory pf=ProvFactory.getFactory();
        System.out.println(" ---- readFrom doc " + media);

        InteropFramework intF=new InteropFramework(pf);
        org.openprovenance.prov.model.Document d=intF.readDocument(in, intF.mimeTypeRevMap.get(media.toString()), "http://ignore.com/");
        org.openprovenance.prov.model.Document d2=org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(d);
        return (Document)d2;
    }
    
    protected void writeInternal(Document doc, OutputStream out, String typeString)throws IOException 
    {
        ProvFactory pf=ProvFactory.getFactory();
        InteropFramework intF=new InteropFramework(pf);
        org.openprovenance.prov.model.Document toConvert = (copy)? pf.newDocument(doc) : doc;
        intF.writeDocument(out, jakarta.ws.rs.core.MediaType.valueOf(typeString), toConvert);   
    }

}
