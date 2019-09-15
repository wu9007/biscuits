package org.hunter.skeleton.filter;

import org.hunter.pocket.query.SQLQuery;
import org.hunter.pocket.session.Session;
import org.hunter.pocket.session.SessionFactory;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author wujianchuan
 */
@Component
@Order(Integer.MIN_VALUE + 1)
public class SkeletonAuthorityFilter implements Filter {
    private static final String OPTIONS = "OPTIONS";
    private Set<String> excludeUrlPatterns = new LinkedHashSet<>();
    private final FilterPathConfig filterPathConfig;
    private final TokenUtil tokenUtil;
    private volatile Session session;

    public SkeletonAuthorityFilter(FilterPathConfig filterPathConfig, TokenUtil tokenUtil) {
        this.filterPathConfig = filterPathConfig;
        this.tokenUtil = tokenUtil;
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
        if (!matchExclude(path)) {
            final String authHeader = request.getHeader("authorization");
            if (OPTIONS.equals(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                chain.doFilter(req, res);
            } else {
                String avatar = this.getAvatar(authHeader);
                if (!this.canPass(avatar, path)) {
                    throw new ServletException(String.format("You are not authorized to pass %s", path));
                }
                request.setAttribute("avatar", avatar);
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

    private boolean canPass(String avatar, String path) throws ServletException {
        String[] splitPath = path.split("/");
        String bundleId = splitPath[splitPath.length - 2];
        String actionId = splitPath[splitPath.length - 1];

        if (this.session == null) {
            synchronized (this) {
                if (this.session == null) {
                    this.session = SessionFactory.getSession("skeleton");
                    this.session.open();
                }
            }
        }
        String sql = "SELECT   " +
                "   T6.UUID    " +
                "FROM   " +
                "   T_MAPPER T1   " +
                "   LEFT JOIN T_AUTH_MAPPER T2 ON T1.UUID = T2.MAPPER_UUID   " +
                "   LEFT JOIN T_AUTHORITY T3 ON T2.AUTH_UUID = T3.UUID   " +
                "   LEFT JOIN T_ROLE_AUTH T4 ON T3.UUID = T4.AUTH_UUID   " +
                "   LEFT JOIN T_ROLE T5 ON T4.ROLE_UUID = T5.UUID   " +
                "   LEFT JOIN T_BUNDLE T6 ON T1.BUNDLE_UUID = T6.UUID   " +
                "   LEFT JOIN T_USER_ROLE T7 ON T5.UUID = T7.ROLE_UUID   " +
                "   LEFT JOIN T_USER T8 ON T7.USER_UUID = T8.UUID    " +
                "WHERE   " +
                "   T1.BUNDLE_ID = :BUNDLE_ID    " +
                "   AND T1.ACTION_ID = :ACTION_ID    " +
                "   AND T8.CODE = :USER_CODE UNION   " +
                "SELECT   " +
                "   UUID    " +
                "FROM   " +
                "   T_BUNDLE    " +
                "WHERE   " +
                "   WITH_AUTH = 0    " +
                "   AND BUNDLE_ID = :BUNDLE_ID ";

        SQLQuery query = this.session.createSQLQuery(sql)
                .mapperColumn("uuid")
                .setParameter("BUNDLE_ID", bundleId)
                .setParameter("ACTION_ID", "/" + actionId)
                .setParameter("USER_CODE", avatar);
        try {
            return query.list().size() > 0;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        }
    }

    @Override
    public void destroy() {
        this.excludeUrlPatterns.clear();
        if (this.session != null && !this.session.getClosed()) {
            this.session.close();
        }
    }
}
