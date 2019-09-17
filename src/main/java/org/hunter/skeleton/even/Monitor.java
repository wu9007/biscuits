package org.hunter.skeleton.even;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface Monitor {
    /**
     * get event source id.
     *
     * @return even source id.
     */
    String[] evenSourceIds();

    /**
     * execute something.
     *
     * @param args arguments.
     */
    void execute(Object... args) throws SQLException, Exception;
}
