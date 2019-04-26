package org.hunter.demo.service;

import java.sql.SQLException;

public interface OrderService {

    int updateType(String type) throws SQLException;
}
