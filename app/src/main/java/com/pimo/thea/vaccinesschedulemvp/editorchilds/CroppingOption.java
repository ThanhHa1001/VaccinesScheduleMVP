package com.pimo.thea.vaccinesschedulemvp.editorchilds;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by thea on 7/23/2017.
 */

public class CroppingOption {
    private CharSequence title;
    private Drawable icon;
    private Intent appItent;

    public CroppingOption() {
    }

    public CroppingOption(CharSequence title, Drawable icon, Intent appItent) {
        this.title = title;
        this.icon = icon;
        this.appItent = appItent;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Intent getAppItent() {
        return appItent;
    }

    public void setAppItent(Intent appItent) {
        this.appItent = appItent;
    }
}
