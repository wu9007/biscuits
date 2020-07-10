package org.hv.biscuits.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * @author wujianchuan
 */
public class CommonObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1850180683383928301L;
    private static final CommonObjectMapper INSTANCE = new CommonObjectMapper();

    private CommonObjectMapper() {
        super();
        this.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static CommonObjectMapper getInstance() {
        return INSTANCE;
    }
}
