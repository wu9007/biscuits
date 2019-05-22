package org.hunter.skeleton.controller;

import org.hunter.pocket.criteria.Criteria;
import org.hunter.pocket.criteria.Restrictions;
import org.hunter.pocket.criteria.Sort;
import org.hunter.pocket.session.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
public class FilterView implements Serializable {
    private static final long serialVersionUID = 8598653671877785474L;

    private static final Map<String, Function<Filter, Restrictions>> RESTRICTIONS_FACTORY = new ConcurrentHashMap<>(4);

    static {
        RESTRICTIONS_FACTORY.put(Operate.EQU, (item) -> Restrictions.equ(item.getKey(), item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.NO_EQU, (item) -> Restrictions.ne(item.getKey(), item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.GT, (item) -> Restrictions.gt(item.getKey(), item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.GTE, (item) -> Restrictions.gte(item.getKey(), item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.LT, (item) -> Restrictions.lt(item.getKey(), item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.LTE, (item) -> Restrictions.lte(item.getKey(), item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.IN, (item) -> Restrictions.in(item.getKey(), (List) item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.NOT_IN, (item) -> Restrictions.notIn(item.getKey(), (List) item.getValue()));
        RESTRICTIONS_FACTORY.put(Operate.LIKE, (item) -> Restrictions.like(item.getKey(), "%" + item.getValue() + "%"));
        RESTRICTIONS_FACTORY.put(Operate.IS_NULL, (item) -> Restrictions.isNull(item.getKey()));
        RESTRICTIONS_FACTORY.put(Operate.IS_NOT_NULL, (item) -> Restrictions.isNotNull(item.getKey()));
    }

    private Map<String, Object> filter;
    private Map<String, Object> sort;
    private Integer limit;
    private Integer page;
    private Integer start;
    private List<Filter> filters;
    private List<Sorter> sorters;

    public Criteria createCriteria(Session session, Class clazz) {
        Criteria criteria = session.createCriteria(clazz);
        if (filters != null && filters.size() > 0) {
            for (Filter item : filters) {
                // 默认操作为 `Operate.EQU`
                if (item.getOperate() != null && item.getOperate().length() > 0) {
                    criteria.add(RESTRICTIONS_FACTORY.get(item.getOperate()).apply(item));
                } else {
                    criteria.add(RESTRICTIONS_FACTORY.get(Operate.EQU).apply(item));
                }
            }
        }
        if (sorters != null && sorters.size() > 0) {
            for (Sorter sorter : sorters) {
                if (SortDirection.ASC.equals(sorter.getDirection())) {
                    criteria.add(Sort.asc(sorter.getProperty()));
                } else {
                    criteria.add(Sort.desc(sorter.getProperty()));
                }
            }
        }
        // 默认UUID降序
        criteria.add(Sort.desc("uuid"));
        if (this.getLimit() != null && this.getStart() != null) {
            criteria.limit(this.getStart(), this.getLimit());
        }
        return criteria;
    }

    public Filter createEmptyFilter() {
        return new Filter();
    }

    public Filter createFilter(String key, Object value, String operate) {
        return new Filter(key, value, operate);
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

    public Map<String, Object> getSort() {
        return sort;
    }

    public void setSort(Map<String, Object> sort) {
        this.sort = sort;
    }

    public List<Sorter> getSorters() {
        return sorters;
    }

    public void setSorters(List<Sorter> sorters) {
        this.sorters = sorters;
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

    public interface SortDirection {
        String DESC = "desc";
        String ASC = "asc";
    }
}
