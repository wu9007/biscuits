package org.hv.biscuits.service;

import org.hv.pocket.model.AbstractEntity;

import java.util.List;

/**
 * @author wujianchuan
 */
public class PageList<T> {
    private List<T> result;
    private Long count;

    public PageList() {
    }

    private PageList(List<T> result, Long count) {
        this.result = result;
        this.count = count;
    }

    public static <T extends AbstractEntity> PageList<T> newInstance(List<T> result, long count) {
        return new PageList<T>(result, count);
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
