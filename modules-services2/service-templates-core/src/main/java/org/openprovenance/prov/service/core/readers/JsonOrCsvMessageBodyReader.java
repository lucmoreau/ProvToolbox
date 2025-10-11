package org.openprovenance.prov.service.core.readers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.openprovenance.prov.model.interop.InteropMediaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.service.core.TemplateService.APPLICATION_VND_KCL_PROV_TEMPLATE_JSON;
@Provider
@Consumes({InteropMediaType.MEDIA_TEXT_CSV, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON})
public class JsonOrCsvMessageBodyReader implements MessageBodyReader<JsonOrCsv> {

    public JsonOrCsvMessageBodyReader() {
        System.out.println("JsonOrCsvMessageBodyReader ....");
    }

    public String trimCharSet(MediaType mediaType) {
        String med=mediaType.toString();
        int ind=med.indexOf(";");
        if (ind>0) med=med.substring(0,ind);
        return med;
    }



    ObjectMapper om=new ObjectMapper();
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        final boolean b = (mediaType.toString().startsWith(InteropMediaType.MEDIA_TEXT_CSV))
                || (mediaType.toString().startsWith(APPLICATION_VND_KCL_PROV_TEMPLATE_JSON));
        return b;
    }

    @Override
    public JsonOrCsv readFrom(Class<JsonOrCsv> aClass,
                                  Type type,
                                  Annotation[] annotations,
                                  MediaType mediaType,
                                  MultivaluedMap<String, String> multivaluedMap,
                                  InputStream inputStream) throws IOException, WebApplicationException {
        final String mediaString = trimCharSet(mediaType);

        if (mediaString.equals(InteropMediaType.MEDIA_TEXT_CSV)) {
            CSVParser parser = CSVParser.parse(inputStream, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            JsonOrCsv res = new JsonOrCsv();
            res.csv = parser;
            res.json = null;
            return res;
        }
        if (mediaString.equals(APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)) {
            List<Map<String,Object>> maps=om.readValue(inputStream, new TypeReference<>() {});
            JsonOrCsv res = new JsonOrCsv();
            res.csv = null;
            res.json = maps;
            return res;
        }
        return null;
    }
}
