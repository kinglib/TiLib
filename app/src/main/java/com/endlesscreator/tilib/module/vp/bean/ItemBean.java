package com.endlesscreator.tilib.module.vp.bean;

public class ItemBean {
    private int id;
    private String text;

    public ItemBean() {
    }

    public ItemBean(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
