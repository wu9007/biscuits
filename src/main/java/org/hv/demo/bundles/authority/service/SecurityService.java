package org.hv.demo.bundles.authority.service;

import org.hv.biscuits.spine.model.User;

import java.sql.SQLException;

/**
 * @author leyan95
 */
public interface SecurityService {

    String generateToken(String avatar, String password) throws SQLException;

    User register(String avatar, String name, String password) throws SQLException, IllegalAccessException;
}
