package org.hunter.skeleton.filter;

import org.hunter.skeleton.config.FilterPathConfig;
import org.hunter.skeleton.utils.PathMatcher;
import org.hunter.skeleton.utils.TokenUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author wujianchuan
 */
@Component
@Order(-2147483647)
public class TokenFilter implements Filter {
    private static final String OPTIONS = "OPTIONS";
    private Set<String> excludeUrlPatterns = new LinkedHashSet<>();
    private final FilterPathConfig filterPathConfig;
    private final TokenUtil tokenUtil;

    public TokenFilter(FilterPathConfig filterPathConfig, TokenUtil tokenUtil) {
        this.filterPathConfig = filterPathConfig;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.excludeUrlPatterns.addAll(Arrays.asList(this.filterPathConfig.getExcludeUrlPatterns().trim().split(",")));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();
        if (!matchExclude(path)) {
            final String authHeader = request.getHeader("authorization");
            if (OPTIONS.equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                chain.doFilter(req, res);
            } else {
                String avatar = this.getAvatar(authHeader);
                // TODO 权限认证
                request.setAttribute("claims", avatar);
                chain.doFilter(req, res);
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    private String getAvatar(String authHeader) throws ServletException {
        if (authHeader == null || !authHeader.startsWith(this.tokenUtil.getTokenConfig().getTokenHead())) {
            throw new ServletException("Missing or invalid Authorization header");
        }
        final String token = authHeader.substring(7);
        return this.tokenUtil.getAvatarByToken(token);
    }

    private boolean matchExclude(String path) {
        PathMatcher pathMatcher = new PathMatcher();
        for (String excludeUrlPattern : this.excludeUrlPatterns) {
            if (pathMatcher.matching(excludeUrlPattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        this.excludeUrlPatterns.clear();
    }
}
