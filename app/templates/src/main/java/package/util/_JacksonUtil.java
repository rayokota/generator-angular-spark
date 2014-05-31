package <%= packageName %>.util;

<% if (entities.length > 0) { %>import <%= packageName %>.models.*;<% } %>

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.common.base.Throwables;
import java.io.IOException;

public class JacksonUtil {

    public static ObjectMapper newObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GuavaModule());
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new AfterburnerModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    public static <T> T readValue(String src, Class<T> valueType) {
        try {
            return newObjectMapper().readValue(src, valueType);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
