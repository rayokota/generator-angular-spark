package <%= packageName %>.util;

import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(final Object model) throws Exception {
        return JacksonUtil.newObjectMapper().writeValueAsString(model);
    }
}
