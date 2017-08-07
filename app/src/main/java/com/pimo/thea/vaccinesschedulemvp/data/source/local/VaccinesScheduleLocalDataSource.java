package com.pimo.thea.vaccinesschedulemvp.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.ChildInjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;

import java.util.ArrayList;
import java.util.List;

import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.ChildEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.InjectionScheduleEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.InjectionVaccineEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.DiseaseEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.VaccineEntry;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract.ChildcareEntry;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;

/**
 * Created by thea on 6/30/2017.
 */

public class VaccinesScheduleLocalDataSource implements VaccinesScheduleDataSource {

    private static VaccinesScheduleLocalDataSource INSTANCE;

    private VaccinesScheduleLocalDbHelper localDbHelper;

    // Prevent direct instantiation
    private VaccinesScheduleLocalDataSource(Context context) {
        localDbHelper = new VaccinesScheduleLocalDbHelper(context);
    }

    public static VaccinesScheduleLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VaccinesScheduleLocalDataSource(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public void getChildList(LoadChildListCallback loadChildListCallback) {
        List<Child> childList = new ArrayList<>();

        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                ChildEntry.COLUMN_CHILD_ID,
                ChildEntry.COLUMN_CHILD_FULL_NAME,
                ChildEntry.COLUMN_CHILD_GENDER,
                ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH,
                ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH,
                ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT,
                ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT,
                ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT,
                ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT,
                ChildEntry.COLUMN_CHILD_AVATAR_URL
        };

        Cursor c = db.query(ChildEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_ID));
                String fullName = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_FULL_NAME));
                int gender = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_GENDER));
                long timeOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH));
                long dateOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH));
                float newBornWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT));
                int newBornHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT));
                float presentWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT));
                int presentHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT));
                String avatarUrl = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_AVATAR_URL));

                Child child = new Child(id, fullName, gender, timeOfBirth, dateOfBirth,
                        newBornWeight, newBornHeight, presentWeight, presentHeight, avatarUrl);
                childList.add(child);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (childList.isEmpty()) {
            loadChildListCallback.onDataChildNotAvailable();
        } else {
            loadChildListCallback.onChildLoaded(childList);
        }
    }

    @Override
    public void getChildListWithNextInject(LoadChildListCallback loadChildListCallback) {
        List<Child> childList = new ArrayList<>();

        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_ID,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_FULL_NAME,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_GENDER,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_AVATAR_URL,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION
        };

        String selection = InjectionScheduleEntry.TABLE_NAME
                + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION + " >= ? and "
                + InjectionScheduleEntry.TABLE_NAME
                + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT + " = ?";

        long today = DateTimeHelper.getCurrentToDayAt7Clock();

        String[] selectionArgs = {Long.toString(today),
                String.valueOf(InjectionScheduleEntry.IS_INJECT_NOT_DONE)};

        String groupBy = ChildEntry.TABLE_NAME
                + "." + ChildEntry.COLUMN_CHILD_ID;
        String having = "MIN("
                + InjectionScheduleEntry.TABLE_NAME
                + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION
                + ")";

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        /**
         * This is an inner join which looks like
         * child INNER JOIN injection_schedules ON child.child_id = injection_schedules.child_id
         */
        queryBuilder.setTables(
                ChildEntry.TABLE_NAME + " INNER JOIN "
                        + InjectionScheduleEntry.TABLE_NAME
                        + " ON " + ChildEntry.TABLE_NAME
                        + "." + ChildEntry.COLUMN_CHILD_ID
                        + " = " + InjectionScheduleEntry.TABLE_NAME
                        + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID
        );


        Cursor c = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                null);
        if (c != null || c.getCount() > 0) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_ID));
                String fullName = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_FULL_NAME));
                int gender = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_GENDER));
                long timeOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH));
                long dateOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH));
                float newBornWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT));
                int newBornHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT));
                float presentWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT));
                int presentHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT));
                String avatarUrl = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_AVATAR_URL));

                long nextInject = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION));

                Child child = new Child(id,
                        fullName,
                        gender,
                        timeOfBirth, dateOfBirth,
                        newBornWeight, newBornHeight,
                        presentWeight, presentHeight,
                        avatarUrl,
                        nextInject);

                childList.add(child);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (childList.isEmpty()) {
            loadChildListCallback.onDataChildNotAvailable();
        } else {
            loadChildListCallback.onChildLoaded(childList);
        }
    }

    @Override
    public void getChild(long childId, GetChildCallback getChildCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                ChildEntry.COLUMN_CHILD_ID,
                ChildEntry.COLUMN_CHILD_FULL_NAME,
                ChildEntry.COLUMN_CHILD_GENDER,
                ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH,
                ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH,
                ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT,
                ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT,
                ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT,
                ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT,
                ChildEntry.COLUMN_CHILD_AVATAR_URL
        };

        String selection = ChildEntry.COLUMN_CHILD_ID + " = ?";

        String[] selectionArgs = new String[]{String.valueOf(childId)};

        Cursor c = db.query(ChildEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Child child = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            long id = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_ID));
            String fullName = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_FULL_NAME));
            int gender = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_GENDER));
            long timeOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH));
            long dateOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH));
            float newBornWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT));
            int newBornHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT));
            float presentWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT));
            int presentHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT));
            String avatarUrl = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_AVATAR_URL));

            child = new Child(id, fullName, gender, timeOfBirth, dateOfBirth,
                    newBornWeight, newBornHeight, presentWeight, presentHeight, avatarUrl);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (child == null) {
            getChildCallback.onDataChildNotAvailable();
        } else {
            getChildCallback.onChildLoaded(child);
        }
    }

    @Override
    public void getChildWithNextInject(long childId, GetChildCallback getChildCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_ID,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_FULL_NAME,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_GENDER,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_AVATAR_URL,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION
        };

        String selection = InjectionScheduleEntry.TABLE_NAME
                + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION + " >= ? and "
                + ChildEntry.TABLE_NAME
                + "." + ChildEntry.COLUMN_CHILD_ID + " = ?";
        String groupBy = ChildEntry.TABLE_NAME
                + "." + ChildEntry.COLUMN_CHILD_ID;
        String having = "MIN("
                + InjectionScheduleEntry.TABLE_NAME
                + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION
                + ")";

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        /**
         * This is an inner join which looks like
         * child INNER JOIN injection_schedules ON child.child_id = injection_schedules.child_id
         */
        queryBuilder.setTables(
                ChildEntry.TABLE_NAME + " INNER JOIN "
                        + InjectionScheduleEntry.TABLE_NAME
                        + " ON " + ChildEntry.TABLE_NAME
                        + "." + ChildEntry.COLUMN_CHILD_ID
                        + " = " + InjectionScheduleEntry.TABLE_NAME
                        + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID
        );

        long today = DateTimeHelper.getCurrentToDayAt7Clock();

        String[] selectionArgs = {String.valueOf(childId), Long.toString(today)};

        Cursor c = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                null);

        Child child = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            long id = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_ID));
            String fullName = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_FULL_NAME));
            int gender = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_GENDER));
            long timeOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH));
            long dateOfBirth = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH));
            float newBornWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT));
            int newBornHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT));
            float presentWeight = c.getFloat(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT));
            int presentHeight = c.getInt(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT));
            String avatarUrl = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_AVATAR_URL));

            long nextInject = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION));

            child = new Child(id, fullName, gender, timeOfBirth, dateOfBirth,
                    newBornWeight, newBornHeight, presentWeight, presentHeight, avatarUrl, nextInject);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (child == null) {
            getChildCallback.onDataChildNotAvailable();
        } else {
            getChildCallback.onChildLoaded(child);
        }
    }

    @Override
    public long insertChild(Child child) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChildEntry.COLUMN_CHILD_FULL_NAME, child.getFullName());
        values.put(ChildEntry.COLUMN_CHILD_GENDER, child.getGender());
        values.put(ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH, child.getTimeOfBirth());
        values.put(ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH, child.getDateOfBirth());
        values.put(ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT, child.getNewBornWeight());
        values.put(ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT, child.getNewBornHeight());
        values.put(ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT, child.getPresentWeight());
        values.put(ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT, child.getPresentHeight());
        values.put(ChildEntry.COLUMN_CHILD_AVATAR_URL, child.getAvatarUrl());

        long rowEffected = db.insert(ChildEntry.TABLE_NAME, null, values);

        db.close();

        return rowEffected;
    }

    @Override
    public void updateChild(Child child) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ChildEntry.COLUMN_CHILD_FULL_NAME, child.getFullName());
        values.put(ChildEntry.COLUMN_CHILD_GENDER, child.getGender());
        values.put(ChildEntry.COLUMN_CHILD_TIME_OF_BIRTH, child.getTimeOfBirth());
        values.put(ChildEntry.COLUMN_CHILD_DATE_OF_BIRTH, child.getDateOfBirth());
        values.put(ChildEntry.COLUMN_CHILD_NEW_BORN_WEIGHT, child.getNewBornWeight());
        values.put(ChildEntry.COLUMN_CHILD_NEW_BORN_HEIGHT, child.getNewBornHeight());
        values.put(ChildEntry.COLUMN_CHILD_PRESENT_WEIGHT, child.getPresentWeight());
        values.put(ChildEntry.COLUMN_CHILD_PRESENT_HEIGHT, child.getPresentHeight());
        values.put(ChildEntry.COLUMN_CHILD_AVATAR_URL, child.getAvatarUrl());

        String selection = ChildEntry.COLUMN_CHILD_ID + " = ?";

        String[] selectionArgs = {String.valueOf(child.getId())};

        db.update(ChildEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteChild(long childId) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        String selection = ChildEntry.COLUMN_CHILD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(childId)};

        db.delete(ChildEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteAllChildList() {

    }

    @Override
    public void refreshChildList() {

    }

    @Override
    public void getChildcares(LoadChildcaresCallback loadChildcaresCallback) {
        List<Object> objects = new ArrayList<>();

        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                ChildcareEntry.COLUMN_CHILDCARE_ID,
                ChildcareEntry.COLUMN_CHILDCARE_TITLE,
                ChildcareEntry.COLUMN_CHILDCARE_CONTENT,
                ChildcareEntry.COLUMN_CHILDCARE_NOTE,
        };

        Cursor c = db.query(ChildcareEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_ID));
                String title = c.getString(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_TITLE));
                String content = c.getString(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_CONTENT));
                String note = c.getString(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_NOTE));

                Childcare childcare = new Childcare(id, title, content, note);
                objects.add(childcare);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (objects.isEmpty()) {
            loadChildcaresCallback.onDataChildcareNotAvailable();
        } else {
            loadChildcaresCallback.onChildcareLoaded(objects);
        }
    }

    @Override
    public void getChildcare(long childcareId, GetChildcareCallback getChildcareCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {ChildcareEntry.COLUMN_CHILDCARE_ID,
                ChildcareEntry.COLUMN_CHILDCARE_TITLE,
                ChildcareEntry.COLUMN_CHILDCARE_CONTENT,
                ChildcareEntry.COLUMN_CHILDCARE_NOTE};
        String selection = ChildcareEntry.COLUMN_CHILDCARE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(childcareId)};

        Cursor c = db.query(ChildcareEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Childcare childcare = null;

        if (c != null & c.getCount() > 0) {
            c.moveToFirst();
            long id = c.getLong(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_ID));
            String title = c.getString(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_TITLE));
            String content = c.getString(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_CONTENT));
            String note = c.getString(c.getColumnIndexOrThrow(ChildcareEntry.COLUMN_CHILDCARE_NOTE));

            childcare = new Childcare(id, title, content, note);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (childcare != null) {
            getChildcareCallback.onChildcareLoaded(childcare);
        } else {
            getChildcareCallback.onDataChildcareNotAvailable();
        }
    }

    @Override
    public long insertChildcare(Childcare childcare) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChildcareEntry.COLUMN_CHILDCARE_TITLE, childcare.getTitle());
        values.put(ChildcareEntry.COLUMN_CHILDCARE_CONTENT, childcare.getContent());
        values.put(ChildcareEntry.COLUMN_CHILDCARE_NOTE, childcare.getNote());

        long rowInserted = db.insert(ChildcareEntry.TABLE_NAME, null, values);

        db.close();

        return rowInserted;
    }

    @Override
    public void updateChildcare(Childcare childcare) {

    }

    @Override
    public void refreshChildcares() {

    }

    @Override
    public void getDiseases(LoadDiseasesCallback loadDiseasesCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                DiseaseEntry.COLUMN_DISEASE_ID,
                DiseaseEntry.COLUMN_DISEASE_NAME,
                DiseaseEntry.COLUMN_DISEASE_DESCRIPTION,
                DiseaseEntry.COLUMN_DISEASE_INJECTION_SCHEDULE,
                DiseaseEntry.COLUMN_DISEASE_CODE
        };

        List<Object> objects = new ArrayList<>();
        Cursor c = db.query(DiseaseEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                long dId = c.getLong(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_ID));
                String dName = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_NAME));
                String dDescription = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_DESCRIPTION));
                String dCode = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_CODE));
                String dInjectionSchedule = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_INJECTION_SCHEDULE));

                Disease disease = new Disease(dId, dName, dDescription, dCode, dInjectionSchedule);
                objects.add(disease);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (!objects.isEmpty()) {
            loadDiseasesCallback.onDiseaseLoaded(objects);
        } else {
            loadDiseasesCallback.onDataDiseaseNotAvailable();
        }
    }

    @Override
    public void getDisease(long diseaseId, GetDiseaseCallback getDiseaseCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {DiseaseEntry.COLUMN_DISEASE_ID,
                DiseaseEntry.COLUMN_DISEASE_NAME,
                DiseaseEntry.COLUMN_DISEASE_DESCRIPTION,
                DiseaseEntry.COLUMN_DISEASE_CODE,
                DiseaseEntry.COLUMN_DISEASE_INJECTION_SCHEDULE};
        String selection = DiseaseEntry.COLUMN_DISEASE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(diseaseId)};

        Cursor c = db.query(DiseaseEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Disease disease = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            long id = c.getLong(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_ID));
            String name = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_NAME));
            String description = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_DESCRIPTION));
            String code = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_CODE));
            String injectionSchedule = c.getString(c.getColumnIndexOrThrow(DiseaseEntry.COLUMN_DISEASE_INJECTION_SCHEDULE));

            disease = new Disease(id, name, description, code, injectionSchedule);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (disease != null) {
            getDiseaseCallback.onDiseaseLoaded(disease);
        } else {
            getDiseaseCallback.onDataDiseaseNotAvailable();
        }
    }

    @Override
    public long insertDisease(Disease disease) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DiseaseEntry.COLUMN_DISEASE_NAME, disease.getName());
        values.put(DiseaseEntry.COLUMN_DISEASE_DESCRIPTION, disease.getDescription());
        values.put(DiseaseEntry.COLUMN_DISEASE_INJECTION_SCHEDULE, disease.getDiseaseInjectonSchedule());
        values.put(DiseaseEntry.COLUMN_DISEASE_CODE, disease.getCode());

        long rowInserted = db.insert(DiseaseEntry.TABLE_NAME, null, values);

        db.close();

        return rowInserted;
    }

    @Override
    public void updateDisease(Disease disease) {

    }

    @Override
    public void refreshDiseases() {

    }

    @Override
    public void getInjSchedulesByChildIdAndChildDob(long childId, long childDob, LoadInjSchedulesByChildIdCallback loadInjSchedulesCallback) {
        List<InjSchedule> injScheduleList = new ArrayList<>();

        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID
        };

        String selection = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(childId)};
        String sortOrder = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION + " ASC";

        Cursor c = db.query(InjectionScheduleEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                long injSId = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID));
                String injSTitle = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE));
                int injSIsInject = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT));
                int injSIsNotify = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY));
                long injSDayInjection = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION));
                int injSShotNumber = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER));
                String injSNote = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE));
                String injSVaccineCode = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE));

                int ageMonths = DateTimeHelper.calculatorAgeMonthFollowDayInjection(childDob, injSDayInjection);

                injScheduleList.add(new InjSchedule(injSId,
                        injSTitle,
                        injSIsInject,
                        injSIsNotify,
                        injSDayInjection,
                        injSShotNumber,
                        injSNote,
                        injSVaccineCode,
                        childId,
                        ageMonths));
            }

            if (c != null) {
                c.close();
            }

            db.close();

            if (injScheduleList.isEmpty()) {
                loadInjSchedulesCallback.onDataInjScheduleNotAvailable();
            } else {
                loadInjSchedulesCallback.onInjSchedulesLoaded(injScheduleList);
            }
        }
    }

    @Override
    public void getInjSchedulesByChildId(long childId, LoadInjSchedulesByChildIdCallback loadInjSchedulesByChildIdCallback) {
        List<InjSchedule> injScheduleList = new ArrayList<>();

        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID
        };
        String selection = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(childId)};
        String sortOrder = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION + " ASC";
        Cursor c = db.query(InjectionScheduleEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                long injSId = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID));
                String injSTitle = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE));
                int injSIsInject = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT));
                int injSIsNotify = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY));
                long injSDayInjection = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION));
                int injSShotNumber = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER));
                String injSNote = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE));
                String injSVaccineCode = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE));

                injScheduleList.add(new InjSchedule(injSId,
                        injSTitle,
                        injSIsInject,
                        injSIsNotify,
                        injSDayInjection,
                        injSShotNumber,
                        injSNote,
                        injSVaccineCode,
                        childId));
            }

            if (c != null) {
                c.close();
            }

            db.close();

            if (injScheduleList.isEmpty()) {
                loadInjSchedulesByChildIdCallback.onDataInjScheduleNotAvailable();
            } else {
                loadInjSchedulesByChildIdCallback.onInjSchedulesLoaded(injScheduleList);
            }
        }
    }


    @Override
    public void getInjSchedule(long injScheduleID, GetInjScheduleCallback getInjScheduleCallback) {
        Log.d("VSLDataSource", " injScheduleId: " + injScheduleID);

        SQLiteDatabase db = localDbHelper.getReadableDatabase();


        String[] projection = {
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE,
                InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID
        };

        String selection = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(injScheduleID)};
        Cursor c = db.query(InjectionScheduleEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        InjSchedule injSchedule = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            String injSTitle = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE));
            int injSIsInject = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT));
            int injSIsNotify = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY));
            long injSDayInjection = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION));
            int injSShotNumber = c.getInt(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER));
            String injSNote = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE));
            String injSVaccineCode = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE));
            long injSChildId = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID));

            injSchedule = new InjSchedule(injScheduleID,
                    injSTitle,
                    injSIsInject,
                    injSIsNotify,
                    injSDayInjection,
                    injSShotNumber,
                    injSNote,
                    injSVaccineCode,
                    injSChildId);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (injSchedule == null) {
            getInjScheduleCallback.onDataInjScheduleNotAvailable();
        } else {
            getInjScheduleCallback.onInjScheduleLoaded(injSchedule);
        }
    }

    @Override
    public long insertInjSchedule(InjSchedule injSchedule) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE, injSchedule.getTitle());
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION, injSchedule.getDayInjection());
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_SHOT_NUMBER, injSchedule.getShotNumber());
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_VACCINE_CODE, injSchedule.getVaccineCode());
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID, injSchedule.getChildId());

        long rowEffected = db.insert(InjectionScheduleEntry.TABLE_NAME, null, values);

        db.close();

        return rowEffected;
    }

    @Override
    public void updateInjSchedule(InjSchedule injSchedule) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_INJECT, injSchedule.getIsInject());
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION, injSchedule.getDayInjection());
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_NOTE, injSchedule.getNote());

        String selection = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(injSchedule.getId())};

        db.update(InjectionScheduleEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public void updateInjScheduleNotified(long injScheduleId) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID, injScheduleId);
        values.put(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY, 1);

        String selection = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(injScheduleId)};

        db.update(InjectionScheduleEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public void deleteInjScheduleByChildId(long childId) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        String selection = InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID + " = ?";
        String[] selectionArgs = {String.valueOf(childId)};

        db.delete(InjectionScheduleEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void refreshInjSchedules() {

    }

    @Override
    public void getInjVaccines(LoadInjVaccinesCallback loadInjVaccinesCallback) {

    }

    @Override
    public void getInjVaccine(long injScheduleId, GetInjVaccineCallback getInjVaccineCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {InjectionVaccineEntry.COLUMN_INJ_VACCINE_ID,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_NAME,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_LOT_NUMBER,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_EXP_DATE,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_DOCTOR_INJECTED,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_NURSE_INJECTED,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_PLACE_OF_VACCINATION,
                InjectionVaccineEntry.COLUMN_INJ_VACCINE_INJ_SCHEDULE_ID
        };
        String selection = InjectionVaccineEntry.COLUMN_INJ_VACCINE_INJ_SCHEDULE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(injScheduleId)};

        Cursor c = db.query(InjectionVaccineEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        InjVaccine injVaccine = null;
        DatabaseUtils.dumpCursor(c);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            long injVId = c.getLong(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_ID));
            String injVTitle = c.getString(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_NAME));
            String injVLotNumber = c.getString(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_LOT_NUMBER));
            long injVExpDate = c.getLong(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_EXP_DATE));
            String injVDoctor = c.getString(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_DOCTOR_INJECTED));
            String injVNurse = c.getString(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_NURSE_INJECTED));
            String injVPlaceOfVaccination = c.getString(c.getColumnIndexOrThrow(InjectionVaccineEntry.COLUMN_INJ_VACCINE_PLACE_OF_VACCINATION));

            injVaccine = new InjVaccine(injVId,
                    injVTitle,
                    injVLotNumber,
                    injVExpDate,
                    injVDoctor,
                    injVNurse,
                    injVPlaceOfVaccination,
                    injScheduleId);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (injVaccine != null) {
            getInjVaccineCallback.onInjVaccineLoaded(injVaccine);
        } else {
            getInjVaccineCallback.onDataInjVaccineNotAvailable();
        }
    }

    @Override
    public long insertInjVaccine(InjVaccine injVaccine) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_NAME, injVaccine.getTitle());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_LOT_NUMBER, injVaccine.getLotNumber());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_EXP_DATE, injVaccine.getExpDate());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_DOCTOR_INJECTED, injVaccine.getDoctorInjected());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_NURSE_INJECTED, injVaccine.getNurseInjected());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_PLACE_OF_VACCINATION, injVaccine.getPlaceOfVaccination());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_INJ_SCHEDULE_ID, injVaccine.getInjScheduleId());

        long rowInserted = db.insert(InjectionVaccineEntry.TABLE_NAME, null, values);

        db.close();

        return rowInserted;
    }

    @Override
    public void updateInjVaccine(InjVaccine injVaccine) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_NAME, injVaccine.getTitle());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_LOT_NUMBER, injVaccine.getLotNumber());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_EXP_DATE, injVaccine.getExpDate());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_DOCTOR_INJECTED, injVaccine.getDoctorInjected());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_NURSE_INJECTED, injVaccine.getNurseInjected());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_PLACE_OF_VACCINATION, injVaccine.getPlaceOfVaccination());
        values.put(InjectionVaccineEntry.COLUMN_INJ_VACCINE_INJ_SCHEDULE_ID, injVaccine.getInjScheduleId());

        String selection = InjectionVaccineEntry.COLUMN_INJ_VACCINE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(injVaccine.getId())};

        db.update(InjectionVaccineEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteInjVaccineByInjScheduleId(long injScheduleId) {

    }

    @Override
    public void refreshInjVaccines() {

    }

    @Override
    public void getVaccines(LoadVaccinesCallback loadVaccinesCallback) {
        List<Object> objects = new ArrayList<>();
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                VaccineEntry.COLUMN_VACCINE_ID,
                VaccineEntry.COLUMN_VACCINE_NAME,
                VaccineEntry.COLUMN_VACCINE_DEFINITION,
                VaccineEntry.COLUMN_VACCINE_CODE,
                VaccineEntry.COLUMN_VACCINE_AFTER_INJECTION_REACTION
        };

        Cursor c = db.query(VaccineEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                DatabaseUtils.dumpCursor(c);
                long id = c.getLong(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_ID));
                String name = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_NAME));
                String definition = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_DEFINITION));
                String code = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_CODE));
                String afterInjectionReaction =
                        c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_AFTER_INJECTION_REACTION));

                Vaccine vaccine = new Vaccine(id, name, definition, code, afterInjectionReaction);
                objects.add(vaccine);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (objects.isEmpty()) {
            loadVaccinesCallback.onDataVaccineNotAvailable();
        } else {
            loadVaccinesCallback.onVaccineLoaded(objects);
        }
    }

    @Override
    public void getVaccine(long vaccineId, GetVaccineCallback getVaccineCallback) {
        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {VaccineEntry.COLUMN_VACCINE_ID,
                VaccineEntry.COLUMN_VACCINE_NAME,
                VaccineEntry.COLUMN_VACCINE_DEFINITION,
                VaccineEntry.COLUMN_VACCINE_CODE,
                VaccineEntry.COLUMN_VACCINE_AFTER_INJECTION_REACTION};
        String selection = VaccineEntry.COLUMN_VACCINE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(vaccineId)};

        Cursor c = db.query(VaccineEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Vaccine vaccine = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();

            long id = c.getLong(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_ID));
            String name = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_NAME));
            String definition = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_DEFINITION));
            String code = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_CODE));
            String afterInjectionReaction = c.getString(c.getColumnIndexOrThrow(VaccineEntry.COLUMN_VACCINE_AFTER_INJECTION_REACTION));

            vaccine = new Vaccine(id, name, definition, code, afterInjectionReaction);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (vaccine != null) {
            getVaccineCallback.onVaccineLoaded(vaccine);
        } else {
            getVaccineCallback.onDataVaccineNotAvailable();
        }
     }

    @Override
    public long insertVaccine(Vaccine vaccine) {
        SQLiteDatabase db = localDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VaccineEntry.COLUMN_VACCINE_NAME, vaccine.getName());
        values.put(VaccineEntry.COLUMN_VACCINE_DEFINITION, vaccine.getDefinition());
        values.put(VaccineEntry.COLUMN_VACCINE_CODE, vaccine.getCode());
        values.put(VaccineEntry.COLUMN_VACCINE_AFTER_INJECTION_REACTION, vaccine.getAfterInjectionReaction());

        long rowInserted = db.insert(VaccineEntry.TABLE_NAME, null, values);

        db.close();

        return rowInserted;
    }

    @Override
    public void updateVaccine(Vaccine vaccine) {

    }

    @Override
    public void refreshVaccines() {

    }

    @Override
    public void getChildInjScheduleList(LoadChildInjSchedulesCallback loadChildInjSchedulesCallback) {
        List<ChildInjSchedule> childInjSchedules = new ArrayList<>();

        SQLiteDatabase db = localDbHelper.getReadableDatabase();

        String[] projection = {
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_ID,
                ChildEntry.TABLE_NAME + "." + ChildEntry.COLUMN_CHILD_FULL_NAME,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION,
                InjectionScheduleEntry.TABLE_NAME + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE
        };

        String selection = InjectionScheduleEntry.TABLE_NAME
                + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_IS_NOTIFY + " = ?";

        String[] selectionArgs = {"0"};

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        /**
         * This is an inner join which looks like
         * child INNER JOIN injection_schedules ON child.child_id = injection_schedules.child_id
         */
        queryBuilder.setTables(
                ChildEntry.TABLE_NAME + " INNER JOIN "
                        + InjectionScheduleEntry.TABLE_NAME
                        + " ON " + ChildEntry.TABLE_NAME
                        + "." + ChildEntry.COLUMN_CHILD_ID
                        + " = " + InjectionScheduleEntry.TABLE_NAME
                        + "." + InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_CHILD_ID
        );


        Cursor c = queryBuilder.query(db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (c != null || c.getCount() > 0) {
            while (c.moveToNext()) {
                long childId = c.getLong(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_ID));
                String childName = c.getString(c.getColumnIndexOrThrow(ChildEntry.COLUMN_CHILD_FULL_NAME));
                long injScheduleId = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_ID));
                String vaccineName = c.getString(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_TITLE));
                long dayInjection = c.getLong(c.getColumnIndexOrThrow(InjectionScheduleEntry.COLUMN_INJ_SCHEDULE_DAY_INJECTION));

                ChildInjSchedule childInjSchedule =
                        new ChildInjSchedule(childId, injScheduleId, childName, vaccineName, dayInjection);
                childInjSchedules.add(childInjSchedule);
            }
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (childInjSchedules.isEmpty()) {
            loadChildInjSchedulesCallback.onDataChildInjScheduleNotAvailable();
        } else {
            loadChildInjSchedulesCallback.onChildInjSchedulesLoaded(childInjSchedules);
        }
    }
}
