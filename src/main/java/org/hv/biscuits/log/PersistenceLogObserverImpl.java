package org.hv.biscuits.log;

import org.hv.pocket.logger.PersistenceLogObserver;
import org.hv.pocket.logger.PersistenceLogSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wujianchuan
 */
@Component
public class PersistenceLogObserverImpl implements PersistenceLogObserver {
    private final Logger logger = LoggerFactory.getLogger(PersistenceLogObserverImpl.class);

    public PersistenceLogObserverImpl() {
        PersistenceLogSubject.getInstance().registerObserver(this);
    }

    @Override
    public void dealWithPersistenceLog(String s) {
        HttpServletRequest httpServletRequest = this.getHttpServletRequest();
        logger.info(s);
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
