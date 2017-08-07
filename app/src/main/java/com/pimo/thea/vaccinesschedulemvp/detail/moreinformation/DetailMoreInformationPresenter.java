package com.pimo.thea.vaccinesschedulemvp.detail.moreinformation;

import android.content.Context;
import android.util.Log;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Childcare;
import com.pimo.thea.vaccinesschedulemvp.data.Disease;
import com.pimo.thea.vaccinesschedulemvp.data.Vaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.listmoreinformation.ListMoreInformationFragment;
import com.pimo.thea.vaccinesschedulemvp.utils.ChildcareHelper;

/**
 * Created by thea on 8/6/2017.
 */

public class DetailMoreInformationPresenter implements DetailMoreInformationContract.Presenter {

    private VaccinesScheduleRepository repository;
    private DetailMoreInformationContract.View view;
    private int request;
    private long objectId;
    private Context context;

    public DetailMoreInformationPresenter(Context context, int request, long objectId, VaccinesScheduleRepository repository, DetailMoreInformationContract.View view) {
        this.context = context;
        this.request = request;
        this.objectId = objectId;
        this.repository = repository;
        this.view = view;
    }
    @Override
    public void bind() {
        Log.d("DetailMIActivity", "request: " + request + " objectId: " + objectId);
        loadObject(request, objectId);
    }

    @Override
    public void loadObject(int request, long objectId) {
        if (request == -1 || objectId == -1L) {
            return;
        }

        if (request == ListMoreInformationFragment.DISEASE_REQUEST) {
            Log.d("DetailMIpresenter", "load disease");
            loadDiseaseById(objectId);
        } else if (request == ListMoreInformationFragment.VACCINE_REQUEST) {
            Log.d("DetailMIpresenter", "load vaccine");
            loadVaccineById(objectId);
        } else {
            Log.d("DetailMIpresenter", "load childcare");
            loadChildcareById(objectId);
        }
    }

    private void loadDiseaseById(long objectId) {
        repository.getDisease(objectId, new VaccinesScheduleDataSource.GetDiseaseCallback() {
            @Override
            public void onDiseaseLoaded(Disease disease) {
                if (!view.isActive()) {
                    return;
                }

                view.setTitle(disease.getName());
                view.setTitle1(context.getString(R.string.detail_m_i_disease_description));
                view.setContent1(disease.getDescription());
                view.setHideContent(false);
                view.setTitle2(context.getString(R.string.detail_m_i_disease_schedule));
                view.setContent2(disease.getDiseaseInjectonSchedule());

            }

            @Override
            public void onDataDiseaseNotAvailable() {
                view.showError();
            }
        });
    }

    private void loadVaccineById(long objectId) {
        repository.getVaccine(objectId, new VaccinesScheduleDataSource.GetVaccineCallback() {
            @Override
            public void onVaccineLoaded(Vaccine vaccine) {
                if (!view.isActive()) {
                    return;
                }

                view.setTitle(vaccine.getName());
                view.setTitle1(context.getString(R.string.detail_m_i_vaccine_definition));
                view.setContent1(vaccine.getDefinition());
                view.setHideContent(false);
                view.setTitle2(context.getString(R.string.detail_m_i_vaccine_after_inj_reaction));
                view.setContent2(vaccine.getAfterInjectionReaction());
            }

            @Override
            public void onDataVaccineNotAvailable() {
                view.showError();
            }
        });
    }

    private void loadChildcareById(long objectId) {
        repository.getChildcare(objectId, new VaccinesScheduleDataSource.GetChildcareCallback() {
            @Override
            public void onChildcareLoaded(Childcare childcare) {
                if (!view.isActive()) {
                    return;
                }

                view.setTitle(childcare.getTitle());
                view.setTitle1(context.getString(R.string.detail_m_i_childcare_content));

                String content1 = childcare.getContent();
                content1 = content1.replaceAll("0x1F342", ChildcareHelper.getEmojiByUnicode(0x1F342));
                content1 = content1.replaceAll("0x1F338", ChildcareHelper.getEmojiByUnicode(0x1F338));
                content1 = content1.replaceAll("0x1F4AE", ChildcareHelper.getEmojiByUnicode(0x1F4AE));
                view.setContent1(content1);

                String note = childcare.getNote();
                if (note.isEmpty() || note.length() == 0) {
                    view.setHideContent(true);
                } else {
                    view.setHideContent(false);
                    view.setTitle2(context.getString(R.string.detail_m_i_childcare_note));

                    note = note.replaceAll("0x1F342", ChildcareHelper.getEmojiByUnicode(0x1F342));
                    note = note.replaceAll("0x1F338", ChildcareHelper.getEmojiByUnicode(0x1F338));
                    note = note.replaceAll("0x1F4AE", ChildcareHelper.getEmojiByUnicode(0x1F4AE));
                    view.setContent2(note);
                }
            }

            @Override
            public void onDataChildcareNotAvailable() {
                view.showError();
            }
        });
    }
}
