package com.pimo.thea.vaccinesschedulemvp.data.source.remote;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.HealthFeed;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by thea on 8/10/2017.
 */

public class VaccinesScheduleRemoteDataSource implements VaccinesScheduleDataSource {
    private static VaccinesScheduleRemoteDataSource INSTANCE;
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final String url = "http://tudu.com.vn/vn/suc-khoe-cua-be/";
    private final String prefix_url = "http://tudu.com.vn/";

    public static VaccinesScheduleRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VaccinesScheduleRemoteDataSource(context);
        }
        return INSTANCE;
    }

    private VaccinesScheduleRemoteDataSource(Context context) {

    }

    @Override
    public void getChildList(LoadChildListCallback loadChildListCallback) {

    }

    @Override
    public void getChildListWithNextInject(LoadChildListCallback loadChildListCallback) {

    }

    @Override
    public void getChild(long childId, GetChildCallback getChildCallback) {

    }

    @Override
    public void getChildWithNextInject(long childId, GetChildCallback getChildCallback) {

    }

    @Override
    public long insertChild(Child child) {
        return 0;
    }

    @Override
    public void updateChild(Child child) {

    }

    @Override
    public void deleteChild(long childId) {

    }

    @Override
    public void deleteAllChildList() {

    }

    @Override
    public void refreshChildList() {

    }

    @Override
    public void getChildcares(LoadChildcaresCallback loadChildcaresCallback) {

    }

    @Override
    public void getChildcare(long childcareId, GetChildcareCallback getChildcareCallback) {

    }

    @Override
    public long insertChildcare(Childcare childcare) {
        return 0;
    }

    @Override
    public void updateChildcare(Childcare childcare) {

    }

    @Override
    public void refreshChildcares() {

    }

    @Override
    public void getDiseases(LoadDiseasesCallback loadDiseasesCallback) {

    }

    @Override
    public void getDisease(long diseaseId, GetDiseaseCallback getDiseaseCallback) {

    }

    @Override
    public long insertDisease(Disease disease) {
        return 0;
    }

    @Override
    public void updateDisease(Disease disease) {

    }

    @Override
    public void refreshDiseases() {

    }

    @Override
    public void getInjSchedulesByChildIdAndChildDob(long childId, long childDob, LoadInjSchedulesByChildIdCallback loadInjSchedulesCallback) {

    }

    @Override
    public void getInjSchedulesByChildId(long childId, LoadInjSchedulesByChildIdCallback loadInjSchedulesByChildIdCallback) {

    }

    @Override
    public void getInjSchedule(long injScheduleID, GetInjScheduleCallback getInjScheduleCallback) {

    }

    @Override
    public long insertInjSchedule(InjSchedule injSchedule) {
        return 0;
    }

    @Override
    public void updateInjSchedule(InjSchedule injSchedule) {

    }

    @Override
    public void updateInjScheduleNotified(long injScheduleId) {

    }

    @Override
    public void deleteInjScheduleByChildId(long childId) {

    }

    @Override
    public void refreshInjSchedules() {

    }

    @Override
    public void getInjVaccines(LoadInjVaccinesCallback loadInjVaccinesCallback) {

    }

    @Override
    public void getInjVaccine(long injScheduleId, GetInjVaccineCallback getInjVaccineCallback) {

    }

    @Override
    public long insertInjVaccine(InjVaccine injVaccine) {
        return 0;
    }

    @Override
    public void updateInjVaccine(InjVaccine injVaccine) {

    }

    @Override
    public void deleteInjVaccineByInjScheduleId(long injScheduleId) {

    }

    @Override
    public void refreshInjVaccines() {

    }

    @Override
    public void getVaccines(LoadVaccinesCallback loadVaccinesCallback) {

    }

    @Override
    public void getVaccine(long vaccineId, GetVaccineCallback getVaccineCallback) {

    }

    @Override
    public long insertVaccine(Vaccine vaccine) {
        return 0;
    }

    @Override
    public void updateVaccine(Vaccine vaccine) {

    }

    @Override
    public void refreshVaccines() {

    }

    @Override
    public void getChildInjScheduleList(LoadChildInjSchedulesCallback loadChildInjSchedulesCallback) {

    }

    @Override
    public void getHealthFeeds(int numberPage, boolean isBookmark, final LoadHealthFeedsCallback loadHealthFeedsCallback) {
        // called when isBookmark = false
        Log.d("VSRemoteDataSource", "get health feeds");
        GetHealthFeedsFromUrl getHealthFeedsFromUrl = new GetHealthFeedsFromUrl(new GetListHealthFeedCallback() {
            @Override
            public void onListHealthFeed(List<HealthFeed> healthFeeds) {
                loadHealthFeedsCallback.onHealthFeedsLoaded(healthFeeds);
            }

            @Override
            public void onDataNotAvailable() {
                loadHealthFeedsCallback.onDataHealthFeedsNotAvailable();
            }
        });

        if (numberPage == 1) {
            getHealthFeedsFromUrl.execute(url);
            Log.d("VSRemoteDSource", "number page 1");
        } else {
            String changeUrl = url + String.valueOf(numberPage) + "/";
            Log.d("VSRemoteDSource", "number page: " + numberPage + "url : " + changeUrl);
            getHealthFeedsFromUrl.execute(changeUrl);
        }
    }

    @Override
    public void getHealthFeed(String url, final GetHealthFeedCallback getHealthFeedCallback) {
        Log.d("VSRemoteDataSource", "get a health feed");
        GetHealthFeedFromUrl getHealthFeedFromUrl = new GetHealthFeedFromUrl(new GetAHealthFeedCallback() {
            @Override
            public void onHealthFeed(HealthFeed healthFeed) {
                getHealthFeedCallback.onHealthFeedLoaded(healthFeed);
            }

            @Override
            public void onDataNotAvailable() {
                getHealthFeedCallback.onDataHealthFeedNotAvailable();
            }
        });
        getHealthFeedFromUrl.execute(url);
    }


    @Override
    public long insertHealthFeed(HealthFeed healthFeed) {
        return 0;
    }

    @Override
    public void deleteHealthFeed(HealthFeed healthFeed) {

    }

    @Override
    public void refreshHealthFeeds(boolean isBookmark) {

    }

    // AsyncTask get Data from network use OkHttp return list health feed
    private class GetHealthFeedsFromUrl extends AsyncTask<String, Void, List<HealthFeed>> {

        private GetListHealthFeedCallback getListHealthFeedCallback;

        public GetHealthFeedsFromUrl(GetListHealthFeedCallback getListHealthFeedCallback) {
            this.getListHealthFeedCallback = getListHealthFeedCallback;
        }

        @Override
        protected List<HealthFeed> doInBackground(String... strings) {
            String url = strings[0];
            List<HealthFeed> healthFeeds = new ArrayList<>();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (!response.isSuccessful()) {
                    Log.d("VSRemoteDataSource", "not success");
                    return null;
                }
                healthFeeds = getListHealthFeedFromString(response.body().string());

            } catch (IOException e) {
                Log.e("VSRemoteDataSource", "IOException: ", e);
                e.printStackTrace();
            }
            return healthFeeds;
        }

        @Override
        protected void onPostExecute(List<HealthFeed> healthFeeds) {
            super.onPostExecute(healthFeeds);
            if (healthFeeds.isEmpty() || healthFeeds.size() == 0) {
                getListHealthFeedCallback.onDataNotAvailable();
            } else {
                getListHealthFeedCallback.onListHealthFeed(healthFeeds);
            }
        }
    }

    // method use in AsyncTask GetHealthFeedsFromUrl to parse String receiver from response (of OkHttp library)
    // to list HealthFeed by Jsoup library
    private List<HealthFeed> getListHealthFeedFromString(String string) {
        List<HealthFeed> healthFeeds = new ArrayList<>();
        Document document = Jsoup.parse(string);
        Elements elements = document.select("div.article-list-style-detail");
        for (Element e : elements) {
            Element linkElement = e.select("a[href]").first();
            Element titleAskElement = e.getElementsByTag("a").first();

            String url = linkElement.attr("href");
            String titleAsk = titleAskElement.text();
            String contentAnswerNotFull = e.select("p").get(1).text() + "\n" + e.select("p").get(2).text();

            Log.d("VSRemoteDSource", " get List from String, url: " + url);

            HealthFeed healthFeed = new HealthFeed(url, titleAsk, contentAnswerNotFull);
            healthFeeds.add(healthFeed);
        }
        return healthFeeds;
    }

    //callback for AsyncTask GetHealthFeedsFromUrl
    private interface GetListHealthFeedCallback {
        void onListHealthFeed(List<HealthFeed> healthFeeds);

        void onDataNotAvailable();
    }

    // AsyncTask get Data from network use OkHttp return a health feed
    private class GetHealthFeedFromUrl extends AsyncTask<String, Void, HealthFeed> {

        private GetAHealthFeedCallback getAHealthFeedCallback;

        public GetHealthFeedFromUrl(GetAHealthFeedCallback getAHealthFeedCallback) {
            this.getAHealthFeedCallback = getAHealthFeedCallback;
        }

        @Override
        protected HealthFeed doInBackground(String... strings) {
            HealthFeed healthFeed = null;
            String url  = strings[0];
            String fullUrl = prefix_url + url;

            Request request = new Request.Builder()
                    .url(fullUrl)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                if (!response.isSuccessful()) {
                    Log.d("VSRemoteDataSource", "response is not successful");
                    return null;
                }

                healthFeed = getHealthFeedFromString(url, response.body().string());

            } catch (IOException e) {
                Log.e("VSRemoteDataSource", "IOException: ", e);
                e.printStackTrace();
            }

            return healthFeed;
        }

        @Override
        protected void onPostExecute(HealthFeed healthFeed) {
            super.onPostExecute(healthFeed);
            if (healthFeed != null) {
                getAHealthFeedCallback.onHealthFeed(healthFeed);
            } else {
                getAHealthFeedCallback.onDataNotAvailable();
            }
        }
    }

    // method use in AsyncTask GetHealthFeedFromUrl to parse String receiver from response (of OkHttp library)
    // to a HealthFeed by Jsoup library
    private HealthFeed getHealthFeedFromString(String url, String string) {
        Document document = Jsoup.parse(string);
        Element colarticlePage = document.select("div.colarticlepage").first();
        Element containerHome = colarticlePage.select("div.containerhome").first();
        Element content = containerHome.select("div#content").first();

        String tittleAsk = content.select("h1").first().text();

        Element contentRequest = content.select("div#content-request").first();
        Elements contentAsk = contentRequest.getElementsByTag("p");

        String strContentAsk = "";
        for (Element e : contentAsk) {
            strContentAsk = strContentAsk + e.text() + "\n";
        }

        Element contentReply = content.select("div#content-reply").first();
        Elements contentAnswer = contentReply.getElementsByTag("p");
        String strContentAnswer = "";
        for (Element e : contentAnswer) {
            strContentAnswer = strContentAnswer + e.text() + "\n";
        }

        return new HealthFeed(url, tittleAsk, strContentAsk, strContentAnswer, false);
    }

    //callback for AsyncTask GetHealthFeedFromUrl
    private interface GetAHealthFeedCallback {

        void onHealthFeed(HealthFeed healthFeed);

        void onDataNotAvailable();
    }
}
