package org.hv.biscuits.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * @author wujianchuan
 */
public abstract class AbstractRocketMqListener<T> implements RocketMQListener<MessageExt> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @SuppressWarnings("unchecked")
    private final Class<T> tType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public void onMessage(MessageExt message) {
        try {
            T obj = objectMapper.readValue(message.getBody(), tType);
            this.execute(obj);
        } catch (IOException e) {
            logger.error("反序列化失败 {} to Class: {}", new String(message.getBody()), tType.getName());
        }
    }

    /**
     * 拿到消息执行对应逻辑
     *
     * @param message 消息
     */
    public abstract void execute(T message);
}
