package org.hunter.demo.repository.impl;

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

/**
 * @author wujianchuan
 */
@Component
public class OrderRepositoryImpl extends AbstractRepository implements OrderRepository {
    @Override
    @Track(data = "#order", operateName = "更新单据", operator = "#operator", operate = OperateEnum.EDIT)
    public int updateOrder(Order order, String orderType, String operator) throws SQLException {
        Criteria criteria = this.getSession().createCriteria(Order.class);
        criteria.add(Modern.set("type", orderType))
                .add(Restrictions.equ("uuid", order.getUuid()));
        return criteria.update();
    }

    @Override
    public Order findOne(String uuid) throws SQLException {
        return (Order) this.getSession().findDirect(Order.class, uuid);
    }
}
