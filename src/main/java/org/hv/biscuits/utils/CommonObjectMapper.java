package org.hv.biscuits.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * @author wujianchuan
 */
public class CommonObjectMapper extends ObjectMapper {
    private static final CommonObjectMapper INSTANCE = new CommonObjectMapper();

    private CommonObjectMapper() {
        super();
        this.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    public static CommonObjectMapper getInstance() {
        return INSTANCE;
    }
}
