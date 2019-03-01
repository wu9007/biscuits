package org.hunter.skeleton.controller;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wujianchuan 2019/3/1
 * @version 1.0
 */
public class FilterView implements Serializable {
    private static final long serialVersionUID = 8598653671877785474L;
    private static final String KEY_WORD = "keyWord";
    private Map<String, Object> filter;
    private Integer limit;
    private Integer page;
    private Integer start;
    private Integer total;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getKeyWord(){
        return (String) this.getFilter().get(KEY_WORD);
    }
}
