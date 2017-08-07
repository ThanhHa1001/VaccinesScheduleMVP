package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 6/30/2017.
 */

public class InjVaccine {
    private long id;
    private String title;
    private String lotNumber;
    private long expDate;
    private String doctorInjected;
    private String nurseInjected;
    private String placeOfVaccination;
    private long injScheduleId;

    public InjVaccine(long id, String title, String lotNumber, long expDate, String doctorInjected, String nurseInjected, String placeOfVaccination, long injScheduleId) {
        this.id = id;
        this.title = title;
        this.lotNumber = lotNumber;
        this.expDate = expDate;
        this.doctorInjected = doctorInjected;
        this.nurseInjected = nurseInjected;
        this.placeOfVaccination = placeOfVaccination;
        this.injScheduleId = injScheduleId;
    }

    public InjVaccine(String title, String lotNumber, long expDate, String doctorInjected, String nurseInjected, String placeOfVaccination, long injScheduleId) {
        this.title = title;
        this.lotNumber = lotNumber;
        this.expDate = expDate;
        this.doctorInjected = doctorInjected;
        this.nurseInjected = nurseInjected;
        this.placeOfVaccination = placeOfVaccination;
        this.injScheduleId = injScheduleId;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public long getExpDate() {
        return expDate;
    }

    public String getDoctorInjected() {
        return doctorInjected;
    }

    public String getNurseInjected() {
        return nurseInjected;
    }

    public String getPlaceOfVaccination() {
        return placeOfVaccination;
    }

    public long getInjScheduleId() {
        return injScheduleId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
