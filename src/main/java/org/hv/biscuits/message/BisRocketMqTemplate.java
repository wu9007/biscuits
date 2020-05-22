package org.hv.biscuits.message;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.hv.biscuits.constant.BiscuitsHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static org.hv.biscuits.constant.BiscuitsHttpHeaders.BUSINESS_DEPARTMENT_NAME;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.TRACE_ID;
import static org.hv.biscuits.constant.BiscuitsHttpHeaders.USER_NAME;

/**
 * @author leyan95
 */
@ExtRocketMQTemplateConfiguration
public class BisRocketMqTemplate extends RocketMQTemplate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected Message<?> doConvert(Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) {
        Message<?> message = super.doConvert(payload, headers, postProcessor);
        MessageBuilder<?> builder = MessageBuilder.fromMessage(message);
        builder.setHeaderIfAbsent(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.TEXT_PLAIN);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes != null) {
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
            String transactionId = (String) httpServletRequest.getAttribute(BiscuitsHttpHeaders.TRANSACTION_ID);
            String traceId = httpServletRequest.getHeader(TRACE_ID);
            String userName = httpServletRequest.getHeader(USER_NAME);
            String businessDepartmentName = httpServletRequest.getHeader(BUSINESS_DEPARTMENT_NAME);
            try {
                if (traceId != null && userName != null) {
                    builder.setHeader(TRACE_ID, traceId)
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .setHeader(USER_NAME, URLDecoder.decode(userName, "UTF-8"));
                    if (businessDepartmentName != null) {
                        builder.setHeader(BUSINESS_DEPARTMENT_NAME, URLDecoder.decode(businessDepartmentName, "UTF-8"));
                    }
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("解析头信息失败 {}: {}, {}: {} ", USER_NAME, userName, BUSINESS_DEPARTMENT_NAME, businessDepartmentName);
            }
        }
        return builder.build();
    }
}
