package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 6/30/2017.
 */

public class Disease {
    private long diseaseId;
    private String name;
    private String description;
    private String code;
    private String diseaseInjectonSchedule;

    public Disease(long diseaseId, String name, String description, String code, String diseaseInjectonSchedule) {
        this.diseaseId = diseaseId;
        this.name = name;
        this.description = description;
        this.code = code;
        this.diseaseInjectonSchedule = diseaseInjectonSchedule;
    }

    public Disease(String name, String description, String code, String diseaseInjectonSchedule) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.diseaseInjectonSchedule = diseaseInjectonSchedule;
    }

    public long getDiseaseId() {
        return diseaseId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getDiseaseInjectonSchedule() {
        return diseaseInjectonSchedule;
    }

    public void setDiseaseId(long diseaseId) {
        this.diseaseId = diseaseId;
    }
}
