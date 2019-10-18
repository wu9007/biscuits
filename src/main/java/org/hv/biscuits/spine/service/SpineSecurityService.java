package org.hv.biscuits.spine.service;

import org.hv.biscuits.spine.model.User;

import java.sql.SQLException;

/**
 * @author wujianchuan
 */
public interface SpineSecurityService {

    String generateToken(String avatar, String password) throws SQLException;

    User register(String avatar, String name, String password) throws SQLException, IllegalAccessException;
}
