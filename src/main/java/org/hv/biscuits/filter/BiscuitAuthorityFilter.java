package org.hv.biscuits.filter;

import io.jsonwebtoken.Claims;
import org.apache.http.HttpStatus;
import org.hv.biscuits.config.TokenConfig;
import org.hv.biscuits.config.FilterPathConfig;
import org.hv.biscuits.utils.PathMatcher;
import org.hv.biscuits.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
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

import static org.hv.biscuits.utils.TokenUtil.CLAIM_KEY_AUTH;
import static org.hv.biscuits.utils.TokenUtil.CLAIM_KEY_AVATAR;

/**
 * @author wujianchuan
 */
@Component
@Order(Integer.MIN_VALUE + 1)
public class BiscuitAuthorityFilter extends AbstractPathFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(BiscuitAuthorityFilter.class);
    private static final String OPTIONS = "OPTIONS";
    private Set<String> excludeUrlPatterns = new LinkedHashSet<>();
    private final FilterPathConfig filterPathConfig;
    private final TokenUtil tokenUtil;

    private String tokenHead;

    public BiscuitAuthorityFilter(FilterPathConfig filterPathConfig, TokenConfig tokenConfig, TokenUtil tokenUtil) {
        this.filterPathConfig = filterPathConfig;
        this.tokenUtil = tokenUtil;
        this.tokenHead = tokenConfig.getTokenHead();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        String excludeUrlStr = this.filterPathConfig.getExcludeUrlPatterns();
        if (excludeUrlStr != null && excludeUrlStr.length() > 0) {
            this.excludeUrlPatterns.addAll(Arrays.asList(this.filterPathConfig.getExcludeUrlPatterns().trim().split(",")));
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();
        String[] splitPath = path.split("/");
        String bundleId = splitPath[splitPath.length - 2];
        String actionId = splitPath[splitPath.length - 1];
        boolean filterTurnOn = this.filterPathConfig.getTurnOn() == null || this.filterPathConfig.getTurnOn();
        if (!filterTurnOn || this.freePath(bundleId) || matchExclude(path)) {
            freeRequest(req, res, chain, request, response);
            return;
        }
        if (OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        } else {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token == null || !token.startsWith(tokenHead)) {
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                super.reLogin(response, "Missing or invalid Authorization header");
                return;
            }
            Claims claims = tokenUtil.getClaimsFromToken(token.replace(tokenHead, ""));
            if (claims == null) {
                super.reLogin(response, "Login status timed out");
                return;
            }
            if (tokenUtil.shouldRefresh(claims)) {
                super.refreshToken(response, tokenUtil.refreshToken(claims));
                return;
            }
            String avatar = (String) claims.get(CLAIM_KEY_AVATAR);
            if (avatar == null) {
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                super.reLogin(response, "Login user not found");
                return;
            }
            String ownAuthIdsStr = (String) claims.get(CLAIM_KEY_AUTH);
            if (ownAuthIdsStr == null || !this.canPass(avatar, bundleId, actionId)) {
                log.warn("{} has not authorized to pass {}", avatar, path);
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                super.refuseWithMessage(response, "没有权限", "您没有访问权限");
                return;
            }
            request.setAttribute("avatar", avatar);
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        this.excludeUrlPatterns.clear();
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

    private void freeRequest(ServletRequest req, ServletResponse res, FilterChain chain, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(tokenHead)) {
            Claims claims = tokenUtil.getClaimsFromToken(token.replace(tokenHead, ""));

            if (claims != null && !tokenUtil.shouldRefresh(claims)) {
                String avatar = (String) claims.get(CLAIM_KEY_AVATAR);
                request.setAttribute("avatar", avatar);
            }
        }
        chain.doFilter(req, res);
    }
}
