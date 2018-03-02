package org.fs.sync.rest.component;

import com.alibaba.fastjson.JSON;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class MapMessageBodyWriter implements MessageBodyWriter {
    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return java.util.Map.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        try {
            return JSON.toJSONString(o).getBytes("UTF-8").length;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        entityStream.write(JSON.toJSONString(o).getBytes("UTF-8"));
    }
}
