package com.xxii.stan.xxii_nt;

import android.graphics.drawable.Drawable;

public class AppList {
/* Définition de la liste nom app + icone */
    private String name;
    private String cachedir;
    Drawable icon;

    public AppList(String name, String cachedir, Drawable icon) {
        this.name = name;
        this.cachedir = cachedir;
        this.icon = icon;

    }

    public String getName() {
        return name;
    }
    public String getCacheDir() { return cachedir; }
    public Drawable getIcon() {
        return icon;
    }
}
