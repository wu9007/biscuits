package org.hv.biscuits.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author wujianchuan
 */
@Component("objectMapper")
@Primary
public class BiscuitsObjectMapper extends ObjectMapper {
    public BiscuitsObjectMapper() {
        super();
        this.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }
}
