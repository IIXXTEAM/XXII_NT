package com.xxii.stan.xxii_nt;

import android.graphics.drawable.Drawable;

public class AppList {
/* Définition de la liste nom app + icone */
    private String name;
    Drawable icon;

    public AppList(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }
}
