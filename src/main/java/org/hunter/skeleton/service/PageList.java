package org.hunter.skeleton.service;

import java.util.List;

/**
 * @author wujianchuan
 */
public class PageList {
    private List result;
    private Long count;

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
