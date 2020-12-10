package org.hv.biscuits.service;

import java.util.List;

/**
 * @author leyan95
 */
public class PageList<T> {
    private List<T> result;
    private Number count;

    public PageList() {
    }

    private PageList(List<T> result, Number count) {
        this.result = result;
        this.count = count;
    }

    public static <T> PageList<T> newInstance(List<T> result, Number count) {
        return new PageList<T>(result, count);
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Number getCount() {
        return count;
    }

    public void setCount(Number count) {
        this.count = count;
    }
}
