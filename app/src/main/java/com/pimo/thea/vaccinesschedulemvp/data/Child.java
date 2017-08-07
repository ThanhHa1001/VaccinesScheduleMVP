package com.pimo.thea.vaccinesschedulemvp.data;

import android.os.Parcel;

import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;

/**
 * Created by thea on 6/30/2017.
 */

public class Child {
    private long id;
    private String fullName;
    private int gender;
    private long timeOfBirth;
    private long dateOfBirth;
    private float newBornWeight;
    private int newBornHeight;
    private float presentWeight;
    private int presentHeight;
    private String avatarUrl;

    // field when inner join two table
    private long nextInject;

    public Child(long id, String fullName, int gender, long timeOfBirth, long dateOfBirth, float newBornWeight, int newBornHeight, float presentWeight, int presentHeight, String avatarUrl) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.timeOfBirth = timeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.newBornWeight = newBornWeight;
        this.newBornHeight = newBornHeight;
        this.presentWeight = presentWeight;
        this.presentHeight = presentHeight;
        this.avatarUrl = avatarUrl;
    }

    public Child(String fullName, int gender, long timeOfBirth, long dateOfBirth, float newBornWeight, int newBornHeight, float presentWeight, int presentHeight, String avatarUrl) {
        this.fullName = fullName;
        this.gender = gender;
        this.timeOfBirth = timeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.newBornWeight = newBornWeight;
        this.newBornHeight = newBornHeight;
        this.presentWeight = presentWeight;
        this.presentHeight = presentHeight;
        this.avatarUrl = avatarUrl;
    }

    public Child(long id, String fullName, int gender, long timeOfBirth, long dateOfBirth, float newBornWeight, int newBornHeight, float presentWeight, int presentHeight, String avatarUrl, long nextInject) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.timeOfBirth = timeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.newBornWeight = newBornWeight;
        this.newBornHeight = newBornHeight;
        this.presentWeight = presentWeight;
        this.presentHeight = presentHeight;
        this.avatarUrl = avatarUrl;
        this.nextInject = nextInject;
    }

    public Child(long id, String fullName, int gender, long dateOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public Child(long id, String fullName, int gender, long dateOfBirth, String avatarUrl) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
    }

    public Child(String fullName, int gender, long dateOfBirth, String avatarUrl) {
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getGender() {
        return gender;
    }

    public long getTimeOfBirth() {
        return timeOfBirth;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public float getNewBornWeight() {
        return newBornWeight;
    }

    public int getNewBornHeight() {
        return newBornHeight;
    }

    public float getPresentWeight() {
        return presentWeight;
    }

    public int getPresentHeight() {
        return presentHeight;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public long getNextInject() {
        return nextInject;
    }

    public boolean isError() {
        return ((fullName == null || fullName.length() == 0)
        && (dateOfBirth == 0)
        && (!VaccinesScheduleLocalContract.ChildEntry.isValidGender(gender)));
    }
}
