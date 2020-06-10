package org.hv.biscuits.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hv.biscuits.constant.BiscuitsHttpHeaders;
import org.hv.biscuits.log.model.OrmLogView;
import org.hv.pocket.logger.PersistenceLogObserver;
import org.hv.pocket.logger.PersistenceLogSubject;
import org.hv.pocket.logger.view.PersistenceMirrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author wujianchuan
 */
@Component
public class PersistenceLogObserverImpl implements PersistenceLogObserver {
    private final Logger logger = LoggerFactory.getLogger(PersistenceLogObserverImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private LogQueue logQueue;
    @Resource(name = "ormLogThreadPool")
    private ExecutorService executorService;

    public PersistenceLogObserverImpl() {
        PersistenceLogSubject.getInstance().registerObserver(this);
    }

    @Override
    public void dealWithPersistenceLog(String s) {
        HttpServletRequest httpServletRequest = this.getHttpServletRequest();
        if (httpServletRequest != null) {
            executorService.execute(() -> {
                String transactionId = (String) httpServletRequest.getAttribute(BiscuitsHttpHeaders.TRANSACTION_ID);
                String persistenceId = (String) httpServletRequest.getAttribute(BiscuitsHttpHeaders.PERSISTENCE_ID);
                try {
                    PersistenceMirrorView persistenceMirrorView = objectMapper.readValue(s, PersistenceMirrorView.class);
                    OrmLogView ormLogView = new OrmLogView(transactionId, persistenceId, persistenceMirrorView);
                    logQueue.offerOrmLog(ormLogView);
                } catch (IOException e) {
                    logger.warn("反序列化失败： {}", s);
                }
            });
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        } else {
            return null;
        }
    }
}
