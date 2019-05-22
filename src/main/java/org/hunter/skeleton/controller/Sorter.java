package org.hunter.skeleton.controller;

import java.io.Serializable;

/**
 * @author wujianchuan
 */
public class Sorter implements Serializable {

    private static final long serialVersionUID = -8202664360779536038L;
    private String property;
    private String direction;

    public Sorter() {
    }

    public Sorter(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
