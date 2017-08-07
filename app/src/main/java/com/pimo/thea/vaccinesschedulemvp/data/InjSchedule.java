package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 6/30/2017.
 */

public class InjSchedule {
    private long id;
    private String title;
    private int isInject;
    private int isNotify;
    private long dayInjection;
    private int shotNumber;
    private String note;
    private String vaccineCode;
    private long childId;
    private int ageMonths;

    public InjSchedule() {
        this.isInject = 0;
        this.isNotify = 0;
        this.note = "";
    }

    public InjSchedule(long id, String title, int isInject, int isNotify, long dayInjection, int shotNumber, String note, String vaccineCode, long childId) {
        this.id = id;
        this.title = title;
        this.isInject = isInject;
        this.isNotify = isNotify;
        this.dayInjection = dayInjection;
        this.shotNumber = shotNumber;
        this.note = note;
        this.vaccineCode = vaccineCode;
        this.childId = childId;
    }

    public InjSchedule(long id, String title, int isInject, int isNotify, long dayInjection, int shotNumber, String note, String vaccineCode, long childId, int ageMonths) {
        this.id = id;
        this.title = title;
        this.isInject = isInject;
        this.isNotify = isNotify;
        this.dayInjection = dayInjection;
        this.shotNumber = shotNumber;
        this.note = note;
        this.vaccineCode = vaccineCode;
        this.childId = childId;
        this.ageMonths = ageMonths;

    }

    public InjSchedule(String title, int isInject, int isNotify, long dayInjection, int shotNumber, String note, String vaccineCode, long childId) {
        this.title = title;
        this.isInject = isInject;
        this.isNotify = isNotify;
        this.dayInjection = dayInjection;
        this.shotNumber = shotNumber;
        this.note = note;
        this.vaccineCode = vaccineCode;
        this.childId = childId;
    }

    public InjSchedule(String title, long dayInjection, int shotNumber, String vaccineCode, long childId) {
        this.title = title;
        this.isInject = 0;
        this.isNotify = 0;
        this.dayInjection = dayInjection;
        this.shotNumber = shotNumber;
        this.note = "";
        this.vaccineCode = vaccineCode;
        this.childId = childId;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIsInject() {
        return isInject;
    }

    public int getIsNotify() {
        return isNotify;
    }

    public long getDayInjection() {
        return dayInjection;
    }

    public int getShotNumber() {
        return shotNumber;
    }

    public String getNote() {
        return note;
    }

    public String getVaccineCode() {
        return vaccineCode;
    }

    public long getChildId() {
        return childId;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsInject(int isInject) {
        this.isInject = isInject;
    }

    public void setIsNotify(int isNotify) {
        this.isNotify = isNotify;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDayInjection(long dayInjection) {
        this.dayInjection = dayInjection;
    }

    public void setShotNumber(int shotNumber) {
        this.shotNumber = shotNumber;
    }

    public void setVaccineCode(String vaccineCode) {
        this.vaccineCode = vaccineCode;
    }

    public void setChildId(long childId) {
        this.childId = childId;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public boolean isInjected() {
        return isInject == 1;
    }
}
