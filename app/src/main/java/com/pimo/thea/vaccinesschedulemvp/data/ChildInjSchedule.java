package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 8/5/2017.
 */

public class ChildInjSchedule {
    private long childId;
    private long injScheduleId;
    private String childName;
    private String vaccineName;
    private long dayInjection;

    public ChildInjSchedule(long childId, long injScheduleId, String childName, String vaccineName, long dayInjection) {
        this.childId = childId;
        this.injScheduleId = injScheduleId;
        this.childName = childName;
        this.vaccineName = vaccineName;
        this.dayInjection = dayInjection;
    }

    public long getChildId() {
        return childId;
    }

    public long getInjScheduleId() {
        return injScheduleId;
    }

    public String getChildName() {
        return childName;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public long getDayInjection() {
        return dayInjection;
    }
}
