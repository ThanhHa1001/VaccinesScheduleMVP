package com.pimo.thea.vaccinesschedulemvp.data;

/**
 * Created by thea on 6/30/2017.
 */

public class Vaccine {
    private long vaccineId;
    private String name;
    private String definition;
    private String code;
    private String afterInjectionReaction;

    public Vaccine(long vaccineId, String name, String definition, String code, String afterInjectionReaction) {
        this.vaccineId = vaccineId;
        this.name = name;
        this.definition = definition;
        this.code = code;
        this.afterInjectionReaction = afterInjectionReaction;
    }

    public Vaccine(String name, String definition, String code, String afterInjectionReaction) {
        this.name = name;
        this.definition = definition;
        this.code = code;
        this.afterInjectionReaction = afterInjectionReaction;
    }

    public long getVaccineId() {
        return vaccineId;
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public String getCode() {
        return code;
    }

    public String getAfterInjectionReaction() {
        return afterInjectionReaction;
    }

    public void setVaccineId(long vaccineId) {
        this.vaccineId = vaccineId;
    }
}
