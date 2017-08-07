package com.pimo.thea.vaccinesschedulemvp.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by thea on 6/30/2017.
 */

public class VaccinesScheduleLocalContract {

    // To prevent some from accidentally instantiating the contract class,
    // give it an empty constructor.
    private VaccinesScheduleLocalContract() {

    }

    public static abstract class ChildEntry implements BaseColumns {
        public final static String TABLE_NAME = "child";

        public final static String COLUMN_CHILD_ID = BaseColumns._ID;
        public final static String COLUMN_CHILD_FULL_NAME = "full_name";
        public final static String COLUMN_CHILD_GENDER = "gender";
        public final static String COLUMN_CHILD_TIME_OF_BIRTH = "time_of_birth";
        public final static String COLUMN_CHILD_DATE_OF_BIRTH = "date_of_birth";
        public final static String COLUMN_CHILD_NEW_BORN_WEIGHT = "new_born_weight";
        public final static String COLUMN_CHILD_NEW_BORN_HEIGHT = "new_born_height";
        public final static String COLUMN_CHILD_PRESENT_WEIGHT = "present_weight";
        public final static String COLUMN_CHILD_PRESENT_HEIGHT = "present_height";
        public final static String COLUMN_CHILD_AVATAR_URL = "avatar_url";

        public final static int GENDER_UNKNOWN = 0;
        public final static int GENDER_MALE = 1;
        public final static int GENDER_FEMALE = 2;

        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }

    public static abstract class ChildcareEntry implements BaseColumns {
        public final static String TABLE_NAME = "childcare";

        public final static String COLUMN_CHILDCARE_ID = BaseColumns._ID;
        public final static String COLUMN_CHILDCARE_TITLE = "title";
        public final static String COLUMN_CHILDCARE_CONTENT = "content";
        public final static String COLUMN_CHILDCARE_NOTE = "note";
    }

    public static abstract class DiseaseEntry implements BaseColumns {
        public final static String TABLE_NAME = "diseases";

        public final static String COLUMN_DISEASE_ID = BaseColumns._ID;
        public final static String COLUMN_DISEASE_NAME = "title";
        public final static String COLUMN_DISEASE_DESCRIPTION = "description";
        public final static String COLUMN_DISEASE_CODE = "code";
        public final static String COLUMN_DISEASE_INJECTION_SCHEDULE = "injection_schedule";
    }

    public static abstract class InjectionScheduleEntry implements BaseColumns {
        public final static String TABLE_NAME = "inj_schedules";

        public final static String COLUMN_INJ_SCHEDULE_ID = BaseColumns._ID;
        public final static String COLUMN_INJ_SCHEDULE_DAY_INJECTION = "day_injection";
        public static final String COLUMN_INJ_SCHEDULE_TITLE = "title";
        public static final String COLUMN_INJ_SCHEDULE_SHOT_NUMBER = "shot_number";
        public static final String COLUMN_INJ_SCHEDULE_NOTE = "note";
        public static final String COLUMN_INJ_SCHEDULE_CHILD_ID = "child_id";
        public static final String COLUMN_INJ_SCHEDULE_VACCINE_CODE = "vaccine_code";
        public static final String COLUMN_INJ_SCHEDULE_IS_INJECT = "is_inject";
        public static final String COLUMN_INJ_SCHEDULE_IS_NOTIFY = "is_notify";


        public static final int IS_INJECT_NOT_DONE = 0;
        public static final int IS_INJECT_DONE = 1;


        public static boolean isValidIsJnject(int inject) {
            if (inject == IS_INJECT_NOT_DONE || inject == IS_INJECT_DONE) {
                return true;
            }
            return false;
        }
    }

    public static abstract class InjectionVaccineEntry implements BaseColumns {
        public final static String TABLE_NAME = "inj_vaccines";

        public final static String COLUMN_INJ_VACCINE_ID = BaseColumns._ID;
        public final static String COLUMN_INJ_VACCINE_NAME = "name";
        public final static String COLUMN_INJ_VACCINE_LOT_NUMBER = "lot_number";
        public final static String COLUMN_INJ_VACCINE_EXP_DATE = "exp_date";
        public final static String COLUMN_INJ_VACCINE_DOCTOR_INJECTED = "doctor_injected";
        public final static String COLUMN_INJ_VACCINE_NURSE_INJECTED = "nurse_injected";
        public final static String COLUMN_INJ_VACCINE_PLACE_OF_VACCINATION = "place_of_vaccination";
        public final static String COLUMN_INJ_VACCINE_INJ_SCHEDULE_ID = "inj_schedule_id";
    }

    public static abstract class VaccineEntry implements BaseColumns {
        public final static String TABLE_NAME = "vaccines";

        public final static String COLUMN_VACCINE_ID = BaseColumns._ID;
        public final static String COLUMN_VACCINE_NAME = "title";
        public final static String COLUMN_VACCINE_DEFINITION = "definition";
        public final static String COLUMN_VACCINE_AFTER_INJECTION_REACTION = "after_injection_reaction";
        public final static String COLUMN_VACCINE_CODE = "code";
    }
}
