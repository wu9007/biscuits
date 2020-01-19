package org.hv.biscuits.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.controller.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import java.io.IOException;

abstract class AbstractResponseFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractResponseFilter.class);

    void refreshToken(ServletResponse response, String token) throws IOException {
        LOGGER.info("The token should be refresh.");
        Body responseBody = Body.success().token(token).resend();
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    void reLogin(ServletResponse response, String message) throws IOException {
        LOGGER.info(message);
        Body responseBody = Body.warning().reLogin();
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    void refuseWithMessage(ServletResponse response, String title, String message) throws IOException {
        LOGGER.info(message);
        Body responseBody = Body.error().title(title).message(message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
