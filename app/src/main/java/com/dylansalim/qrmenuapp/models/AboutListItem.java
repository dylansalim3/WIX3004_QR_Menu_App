package com.dylansalim.qrmenuapp.models;

import android.graphics.drawable.Drawable;

public class AboutListItem {
    private Drawable icon;
    private String title;
    private String[] desc;

    public AboutListItem(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public AboutListItem(Drawable icon, String title, String[] desc) {
        this.icon = icon;
        this.title = title;
        this.desc = desc;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getDesc() {
        return desc;
    }

    public void setDesc(String[] desc) {
        this.desc = desc;
    }
}
