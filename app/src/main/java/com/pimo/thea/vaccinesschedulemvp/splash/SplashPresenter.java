package com.pimo.thea.vaccinesschedulemvp.splash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thea on 8/5/2017.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private static final String PRE_IS_ACCEPT_TERMS = "is_accept_terms";
    private VaccinesScheduleRepository repository;
    private SplashContract.View view;
    private Context context;

    public SplashPresenter(Context context, VaccinesScheduleRepository repository, SplashContract.View view) {
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
        boolean isAcceptTerms =
                PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PRE_IS_ACCEPT_TERMS, false);
        if (isAcceptTerms) {
            view.showHomeActivity();
        } else {
            view.hideImageViewIcon();
            view.showUseOfTheTerms();
        }
    }

    @Override
    public void acceptTerms() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(PRE_IS_ACCEPT_TERMS, true);
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
                Log.e("SplashPresenter", "Problem reading the diseases from file json.", ex);
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
                Log.e("SplashPresenter", "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            view.dismissDialogLoading();
            view.showHomeActivity();
        }
    }
}
