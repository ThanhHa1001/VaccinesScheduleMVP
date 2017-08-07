package com.pimo.thea.vaccinesschedulemvp.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.utils.FileHelper;
import com.pimo.thea.vaccinesschedulemvp.view.DialogLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thea on 8/5/2017.
 */

public class OnboardingPresenter implements OnboardingContract.Presenter {

    private VaccinesScheduleRepository repository;
    private OnboardingContract.View view;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isAcceptTerms = false;

    public OnboardingPresenter(Context context, VaccinesScheduleRepository repository, OnboardingContract.View view) {
        this.context = context;
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void bind() {
        getAcceptTerms();
    }

    @Override
    public void getAcceptTerms() {
        sharedPreferences = context.getSharedPreferences("use_of_the_terms", Context.MODE_PRIVATE);
        isAcceptTerms = sharedPreferences.getBoolean(context.getString(R.string.onboarding_accept_terms_key), false);

        if (isAcceptTerms) {
            view.showHome();
        }
    }

    @Override
    public void acceptTerms() {
        editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.onboarding_accept_terms_key), true);
        editor.apply();

        insertListMoreInformation();
    }

    private void insertListMoreInformation() {
        new ReadCopyInsertDBFromAssets().execute("vaccine.json");
    }

    private class ReadCopyInsertDBFromAssets extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showDialogLoading();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String json = null;
            try {
                InputStream inputStream = context.getAssets().open(strings[0]);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                Log.e("OnboardingPresenter", "Problem reading the diseases from file json.", ex);
            }

            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(json)) {
                return null;
            }

            try {
                JSONObject baseJsonObject = new JSONObject(json);
                JSONArray vaccineArray = baseJsonObject.optJSONArray("details");

                for (int i = 0; i < vaccineArray.length(); i++) {
                    //  Get a single diseases at position i within the list diseases
                    JSONObject currentDisease = vaccineArray.getJSONObject(i);
                    JSONObject properties = currentDisease.getJSONObject("properties");

                    if (currentDisease.getString("type").equals("diseases")) {
                        Disease disease = new Disease(
                                properties.getString("name"),
                                properties.getString("description"),
                                properties.getString("code"),
                                properties.getString("injectionSchedule")
                        );
                        repository.insertDisease(disease);

                    } else if (currentDisease.getString("type").equals("vaccine")) {
                        Vaccine vaccine = new Vaccine(
                                properties.getString("name"),
                                properties.getString("definition"),
                                properties.getString("code"),
                                properties.getString("after_injection_reaction")
                        );
                        repository.insertVaccine(vaccine);

                    } else if (currentDisease.getString("type").equals("childcare")) {
                        Childcare childcare = new Childcare(
                                properties.getString("title"),
                                properties.getString("content"),
                                properties.getString("note")
                        );
                        repository.insertChildcare(childcare);
                    }
                }
            } catch (JSONException e) {
                // If an error is thrown when executing any of the above statements in the "try" block,
                // catch the exception here, so the app doesn't crash. Print a log message
                // with the message from the exception.
                Log.e("OnboardingPresenter", "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            view.dismissDialogLoading();
            view.showHome();
        }
    }
}
