package com.pimo.thea.vaccinesschedulemvp.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.DiseaseEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.InjectionScheduleEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.InjectionVaccineEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.ChildEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.ChildcareEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.VaccineEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.HealthFeedEntry;

/**
 * Created by thea on 6/30/2017.
 */

public class VaccinesScheduleLocalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Baby.db";
    private static final int DATABASE_VERSION = 1;

    private static final String PRIMARY_KEY_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";


    /**
     * Create a String that contains the SQL statement to create the child table
     */
    private static final String SQL_CREATE_CHILD_TABLE = "CREATE TABLE " + ChildEntry.TABLE_NAME + " ("
            + ChildEntry.COLUMN_CHILD_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_FULL_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_GENDER + INTEGER_TYPE + NOT_NULL + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH + INTEGER_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH + INTEGER_TYPE + NOT_NULL + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT + REAL_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT + INTEGER_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT + REAL_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT + INTEGER_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_CHILD_AVATAR_URL + TEXT_TYPE
            + ");";

    /**
     * Create a String that contains the SQL statement to create the childcare table
     */
    private static final String SQL_CREATE_CHILDCARE_TABLE = "CREATE TABLE " + ChildcareEntry.TABLE_NAME + " ("
            + ChildcareEntry.COLUMN_CHILDCARE_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + ChildcareEntry.COLUMN_CHILDCARE_TITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + ChildcareEntry.COLUMN_CHILDCARE_CONTENT + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + ChildcareEntry.COLUMN_CHILDCARE_NOTE + TEXT_TYPE
            + ");";


    /**
     * Create a String that contains the SQL statement to create the Disease table
     */
    private static final String SQL_CREATE_DISEASE_TABLE = "CREATE TABLE " + DiseaseEntry.TABLE_NAME + " ("
            + DiseaseEntry.COLUMN_DISEASE_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + DiseaseEntry.COLUMN_DISEASE_CODE + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + DiseaseEntry.COLUMN_DISEASE_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + DiseaseEntry.COLUMN_DISEASE_DESCRIPTION + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + DiseaseEntry.COLUMN_DISEASE_INJECTION_SCHEDULE + TEXT_TYPE + NOT_NULL
            + ");";

    /**
     * Create a String that contains the SQL statement to create the Injection_schedule table
     */
    private static final String SQL_CREATE_INJECTION_SCHEDULE_TABLE = "CREATE TABLE " + InjectionScheduleEntry.TABLE_NAME + " ("
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT + BOOLEAN_TYPE + NOT_NULL + " DEFAULT 0" + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY + BOOLEAN_TYPE + NOT_NULL + " DEFAULT 0" + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION + INTEGER_TYPE + NOT_NULL + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER + INTEGER_TYPE + NOT_NULL + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE + TEXT_TYPE + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE + TEXT_TYPE + COMMA_SEP
            + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID + INTEGER_TYPE + NOT_NULL
            + ");";

    /**
     * Create a String that contains the SQL statement to create the Injection_vaccine table
     */
    private static final String SQL_CREATE_INJECTION_VACCINE_TABLE = "CREATE TABLE " + InjectionVaccineEntry.TABLE_NAME + " ("
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_LOT_NUMBER + TEXT_TYPE + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_EXP_DATE + INTEGER_TYPE + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_DOCTOR_INJECTED + TEXT_TYPE + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_NURSE_INJECTED + TEXT_TYPE + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_PLACE_OF_VACCINATION + TEXT_TYPE + COMMA_SEP
            + InjectionVaccineEntry.COLUMN_INJ_VACCINE_INJ_SCHEDULE_ID + INTEGER_TYPE + NOT_NULL
            + ");";

    /**
     * Create a String that contains the SQL statement to create the vaccine table
     */
    private static final String SQL_CREATE_VACCINE_TABLE = "CREATE TABLE " + VaccineEntry.TABLE_NAME + " ("
            + VaccineEntry.COLUMN_VACCINE_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + VaccineEntry.COLUMN_VACCINE_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + VaccineEntry.COLUMN_VACCINE_DEFINITION + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + VaccineEntry.COLUMN_VACCINE_CODE + TEXT_TYPE + NOT_NULL + COMMA_SEP
            + VaccineEntry.COLUMN_VACCINE_AFTER_INJECTION_REACTION + TEXT_TYPE
            + ");";

    /**
     * Create s String that contains the SQl statement to create the health feed
     */
    private static final String SQL_CREATE_HEALTH_FEED = "CREATE TABLE " + HealthFeedEntry.TABLE_NAME + " ("
            + HealthFeedEntry.COLUMN_HEALTH_FEED_ID + INTEGER_TYPE + PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP
            + HealthFeedEntry.COLUMN_HEALTH_FEED_URL + TEXT_TYPE + COMMA_SEP
            + HealthFeedEntry.COLUMN_HEALTH_FEED_TITLE_ASK + TEXT_TYPE + COMMA_SEP
            + HealthFeedEntry.COLUMN_HEALTH_FEED_CONTENT_ASK + TEXT_TYPE + COMMA_SEP
            + HealthFeedEntry.COLUMN_HEALTH_FEED_CONTENT_ANSWER + TEXT_TYPE
            + ");";

    public VaccinesScheduleLocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DISEASE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INJECTION_SCHEDULE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INJECTION_VACCINE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHILD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHILDCARE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VACCINE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HEALTH_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Not required as at version 1
    }
}
