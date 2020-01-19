package org.hv.biscuits.filter;

import org.hv.pocket.query.SQLQuery;
import org.hv.pocket.session.Session;
import org.hv.pocket.session.SessionFactory;

import javax.servlet.ServletException;
import java.sql.SQLException;

/**
 * @author wujianchuan
 */
abstract class AbstractPathFilter extends AbstractResponseFilter {
    private volatile Session session;

    boolean canPass(String avatar, String bundleId, String actionId) throws ServletException {
        this.openSession();
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
                "   (T8.IS_MANAGER = 1 AND T8.CODE = :USER_CODE) " +
                " OR " +
                "   (T1.BUNDLE_ID = :BUNDLE_ID AND T1.ACTION_ID = :ACTION_ID AND T8.CODE = :USER_CODE)";

        SQLQuery query = this.session.createSQLQuery(sql)
                .mapperColumn("uuid")
                .setParameter("BUNDLE_ID", bundleId)
                .setParameter("ACTION_ID", actionId)
                .setParameter("USER_CODE", avatar);
        try {
            return query.list().size() > 0;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        } finally {
            this.closeSession();
        }
    }

    boolean freePath(String bundleId) throws ServletException {
        this.openSession();
        String sql = "SELECT   " +
                "   UUID    " +
                "FROM   " +
                "   T_BUNDLE    " +
                "WHERE   " +
                "   WITH_AUTH = 0    " +
                "   AND BUNDLE_ID = :BUNDLE_ID ";

        SQLQuery query = this.session.createSQLQuery(sql)
                .mapperColumn("uuid")
                .setParameter("BUNDLE_ID", bundleId);
        try {
            return query.list().size() > 0;
        } catch (SQLException e) {
            throw new ServletException(e.getMessage());
        } finally {
            this.closeSession();
        }
    }

    private void openSession() {
        if (this.session == null) {
            synchronized (this) {
                if (this.session == null) {
                    this.session = SessionFactory.getSession("biscuits");
                }
            }
        }
        if (this.session.getClosed()) {
            synchronized (this) {
                if (this.session.getClosed()) {
                    this.session.open();
                }
            }
        }
    }

    private void closeSession() {
        if (!this.session.getClosed()) {
            synchronized (this) {
                if (!this.session.getClosed()) {
                    this.session.close();
                }
            }
        }
    }
}
