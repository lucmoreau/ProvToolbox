package org.openprovenance.prov.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

@Provider
public class MapMessageBodyReader implements MessageBodyReader<Map> {
	static Logger logger = LogManager.getLogger(MessageBodyReader.class);

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
							  Annotation[] annotations, MediaType mediaType) {
		logger.debug("media "+ mediaType.toString());
		return "application/json".equals(mediaType.toString());
	}

	@Override
	public Map readFrom(Class<Map> type, Type genericType,
								Annotation[] annotations, MediaType mediaType,
								MultivaluedMap<String, String> httpHeaders,
								InputStream is) throws IOException,
			WebApplicationException {

		return new ObjectMapper().readValue(is,Map.class);

	}

}
