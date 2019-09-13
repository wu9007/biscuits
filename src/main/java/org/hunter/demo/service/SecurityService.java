package org.hunter.demo.service;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface SecurityService {

    String generateToken(String avatar, String password) throws SQLException;
}
