package org.fs.sync.rest.component;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class MapMessageBodyReader implements MessageBodyReader {
    @Override
    public boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return java.util.Map.class.isAssignableFrom(type);
    }

    @Override
    public Object readFrom(Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        IOUtils.copy(entityStream, bo);
        return JSON.parseObject(new String(bo.toByteArray(), "UTF-8"), java.util.Map.class);
    }
}
