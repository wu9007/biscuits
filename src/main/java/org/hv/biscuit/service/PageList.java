package org.hv.biscuit.service;

import java.util.List;

/**
 * @author wujianchuan
 */
public class PageList {
    private List result;
    private Long count;

    public PageList() {
    }

    private PageList(List result, Long count) {
        this.result = result;
        this.count = count;
    }

    public static PageList newInstance(List result, long count) {
        return new PageList(result, count);
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
