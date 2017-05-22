package com.jkb.support.photopicker.bean;

import java.io.Serializable;

/**
 * 照片属性
 */
public class Photo implements Serializable {

    private int id;
    private String path;
    private long size;//byte 字节
    private boolean isSelected;//是否被选中

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public Photo(int id, String path, long size) {
        this.id = id;
        this.path = path;
        this.size = size;
    }

    public Photo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;
        Photo photo = (Photo) o;
        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
