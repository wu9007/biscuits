package org.hunter.demo.repository.impl;

import org.hunter.demo.model.Commodity;
import org.hunter.demo.model.Order;
import org.hunter.demo.repository.OrderRepository;
import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Modern;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.skeleton.annotation.Track;
import org.hunter.skeleton.constant.OperateEnum;
import org.hunter.skeleton.repository.AbstractRepository;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujianchuan
 */
@Component
public class OrderRepositoryImpl extends AbstractRepository implements OrderRepository {
    @Override
    @Track(data = "#order", operateName = "保存单据", operator = "#operator", operate = OperateEnum.ADD)
    public int save(Order order, String operator) throws SQLException, IllegalAccessException {
        return this.getSession().save(order, true);
    }

    @Override
    @Track(data = "#order", operateName = "更新单据", operator = "#operator", operate = OperateEnum.EDIT)
    public int update(Order order, String operator) throws SQLException, IllegalAccessException {
        return this.getSession().update(order, true);
    }

    @Override
    public Order findOne(String uuid) throws SQLException {
        return (Order) this.getSession().findDirect(Order.class, uuid);
    }
}
