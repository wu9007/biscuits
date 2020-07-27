package org.hv.biscuits.controller;

import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;
import org.hv.pocket.criteria.Sort;
import org.hv.pocket.model.AbstractEntity;
import org.hv.pocket.model.MapperFactory;
import org.hv.pocket.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * @author leyan95 2019/3/1
 * @version 1.0
 */
public class FilterView implements Serializable {
    private static final long serialVersionUID = 8598653671877785474L;
    private final Logger logger = LoggerFactory.getLogger(FilterView.class);

    private static final Map<String, BiFunction<String, Object, Restrictions>> RESTRICTIONS_FACTORY = new ConcurrentHashMap<>(4);

    static {
        RESTRICTIONS_FACTORY.put(Operate.EQU, (source, target) -> {
            if (target != null) {
                return Restrictions.equ(source, target);
            } else {
                return Restrictions.isNull(source);
            }
        });
        RESTRICTIONS_FACTORY.put(Operate.NO_EQU, Restrictions::ne);
        RESTRICTIONS_FACTORY.put(Operate.GT, Restrictions::gt);
        RESTRICTIONS_FACTORY.put(Operate.GTE, Restrictions::gte);
        RESTRICTIONS_FACTORY.put(Operate.LT, Restrictions::lt);
        RESTRICTIONS_FACTORY.put(Operate.LTE, Restrictions::lte);
        RESTRICTIONS_FACTORY.put(Operate.IN, (source, target) -> Restrictions.in(source, (List<?>) target));
        RESTRICTIONS_FACTORY.put(Operate.NOT_IN, (source, target) -> Restrictions.notIn(source, (List<?>) target));
        RESTRICTIONS_FACTORY.put(Operate.LIKE, (source, target) -> Restrictions.like(source, "%" + target + "%"));
        RESTRICTIONS_FACTORY.put(Operate.IS_NULL, (source, target) -> Restrictions.isNull(source));
        RESTRICTIONS_FACTORY.put(Operate.IS_NOT_NULL, (source, target) -> Restrictions.isNotNull(source));
    }

    private Map<String, Object> filter;
    private Integer limit;
    private Integer page;
    private Integer start;
    private List<Filter> filters;
    private List<FilterSort> sorts;

    public <T extends AbstractEntity> Criteria createCriteria(Session session, Class<T> clazz) {
        Criteria criteria = session.createCriteria(clazz);
        if (filters != null && filters.size() > 0) {
            for (Filter item : filters) {
                String[] fileNames = item.getKey().split("\\.");
                // 默认操作为 `Operate.EQU`
                String operate = item.getOperate();
                if (fileNames.length == 1) {
                    if (operate != null && operate.trim().length() > 0) {
                        criteria.add(RESTRICTIONS_FACTORY.get(item.getOperate()).apply(item.getKey(), item.getValue()));
                    } else {
                        criteria.add(RESTRICTIONS_FACTORY.get(Operate.EQU).apply(item.getKey(), item.getValue()));
                    }
                } else if (fileNames.length == 2) {
                    List<String> bridgeValues = new ArrayList<>();
                    // 从明细中进行过滤
                    String detailFieldName = fileNames[0];
                    String detailFilterFieldName = fileNames[1];
                    Class<? extends AbstractEntity> detailClazz = MapperFactory.getDetailClass(clazz.getName(), detailFieldName);
                    Criteria detailCriteria = session.createCriteria(detailClazz);
                    if (operate != null && operate.trim().length() > 0) {
                        detailCriteria.add(RESTRICTIONS_FACTORY.get(item.getOperate()).apply(detailFilterFieldName, item.getValue()));
                    } else {
                        detailCriteria.add(RESTRICTIONS_FACTORY.get(Operate.EQU).apply(detailFilterFieldName, item.getValue()));
                    }
                    String bridgeField = MapperFactory.getOneToMayDownFieldName(clazz.getName(), detailFieldName);
                    Field mainField = MapperFactory.getField(detailClazz.getName(), bridgeField);
                    mainField.setAccessible(true);
                    for (AbstractEntity entity : detailCriteria.list()) {
                        String value = null;
                        try {
                            value = (String) mainField.get(entity);
                        } catch (IllegalAccessException e) {
                            logger.error(e.getMessage());
                        }
                        if (!bridgeValues.contains(value)) {
                            bridgeValues.add(value);
                        }
                    }
                    if (bridgeValues.size() > 0) {
                        String upBridgeFieldName = MapperFactory.getManyToOneUpField(detailClazz.getName(), clazz.getName());
                        criteria.add(Restrictions.in(upBridgeFieldName, bridgeValues));
                    }
                }
            }
        }
        if (this.sorts != null && this.sorts.size() > 0) {
            this.sorts.stream().sorted(Comparator.comparing(FilterSort::getIdx)).forEach((order) -> {
                if (FilterSortDirection.ASC.equals(order.getDirection())) {
                    criteria.add(Sort.asc(order.getKey()));
                } else {
                    criteria.add(Sort.desc(order.getKey()));
                }
            });
        }
        // 默认UUID降序
        criteria.add(Sort.desc("uuid"));
        if (this.getLimit() != null && this.getStart() != null) {
            criteria.limit(this.getStart(), this.getLimit());
        }
        return criteria;
    }

    public FilterView addFilter(Filter filter) {
        this.filters.add(filter);
        return this;
    }

    public Filter createEmptyFilter() {
        return new Filter();
    }

    public Filter createFilter(String key, Object value, String operate) {
        return new Filter(key, value, operate, null);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public List<FilterSort> getSorts() {
        return sorts;
    }

    public void setSorts(List<FilterSort> sorts) {
        this.sorts = sorts;
    }

    public interface Operate {
        String EQU = "equ";
        String NO_EQU = "ne";
        String GT = "gt";
        String GTE = "gte";
        String LT = "lt";
        String LTE = "lte";
        String IN = "in";
        String NOT_IN = "notIn";
        String LIKE = "like";
        String IS_NULL = "isNull";
        String IS_NOT_NULL = "isNotNull";
    }
}
