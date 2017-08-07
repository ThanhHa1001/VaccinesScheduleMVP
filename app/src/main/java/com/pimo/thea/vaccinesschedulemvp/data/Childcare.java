package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 6/30/2017.
 */

public class Childcare {
    private long childcareId;
    private String title;
    private String content;
    private String note;

    public Childcare(long childcareId, String title, String content, String note) {
        this.childcareId = childcareId;
        this.title = title;
        this.content = content;
        this.note = note;
    }

    public Childcare(String title, String content, String note) {
        this.title = title;
        this.content = content;
        this.note = note;
    }

    public long getChildcareId() {
        return childcareId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getNote() {
        return note;
    }

    public void setChildcareId(long childcareId) {
        this.childcareId = childcareId;
    }
}
