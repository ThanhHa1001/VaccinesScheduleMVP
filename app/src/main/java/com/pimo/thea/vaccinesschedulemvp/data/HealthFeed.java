package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 8/9/2017.
 */

public class HealthFeed {
    private long id;
    private String url;
    private String titleAsk;
    private String contentAsk;
    private String contentAnswer;
    private boolean isBookmark;

    public HealthFeed(long id, String url, String titleAsk, String contentAsk, String contentAnswer, boolean isBookmark) {
        this.id = id;
        this.url = url;
        this.titleAsk = titleAsk;
        this.contentAsk = contentAsk;
        this.contentAnswer = contentAnswer;
        this.isBookmark = isBookmark;
    }

    public HealthFeed(String url, String titleAsk, String contentAsk, String contentAnswer, boolean isBookmark) {
        this.url = url;
        this.titleAsk = titleAsk;
        this.contentAsk = contentAsk;
        this.contentAnswer = contentAnswer;
        this.isBookmark = isBookmark;
    }

    public HealthFeed(String url, String titleAsk, String contentAnswer) {
        this.url = url;
        this.titleAsk = titleAsk;
        this.contentAnswer = contentAnswer;
        this.isBookmark = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitleAsk() {
        return titleAsk;
    }

    public String getContentAsk() {
        return contentAsk;
    }

    public String getContentAnswer() {
        return contentAnswer;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
}
