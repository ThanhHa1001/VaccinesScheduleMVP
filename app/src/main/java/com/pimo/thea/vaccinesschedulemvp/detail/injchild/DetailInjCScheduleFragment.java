package com.pimo.thea.vaccinesschedulemvp.detail.injchild;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.remote.VaccinesScheduleRemoteDataSource;
import com.pimo.thea.vaccinesschedulemvp.detail.moreinformation.DetailMoreInformationActivity;
import com.pimo.thea.vaccinesschedulemvp.detail.moreinformation.DetailMoreInformationFragment;
import com.pimo.thea.vaccinesschedulemvp.listmoreinformation.ListMoreInformationFragment;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;

import java.util.Calendar;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjCScheduleFragment extends Fragment implements DetailInjCContract.ViewCSchedule, View.OnClickListener {

    private DetailInjCContract.PresenterCSchedule presenter;
    private long injectionId;

    private ImageView imgIcon;
    private TextView txtTitle;
    private TextView txtShotnumber;
    private CheckBox ckbIsInject;
    private EditText edtDayInjection;
    private EditText edtNote;
    private Button btnSave;

    private long dayInjection;

    public DetailInjCScheduleFragment() {

    }

    public static DetailInjCScheduleFragment newInstance() {
        return new DetailInjCScheduleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        injectionId = b.getLong(DetailInjCActivity.INJECTION_ID_ARGS);
        Log.d("DetailInjCSchedule", "inj schedule id: " + injectionId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_inj_child_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DetailInjCSchedulePresenter(injectionId,
                VaccinesScheduleRepository.getInstance(
                        VaccinesScheduleRemoteDataSource.getInstance(getContext()),
                        VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);

        imgIcon = (ImageView) view.findViewById(R.id.detail_inj_c_schedule_fragment_img_icon);

        txtTitle = (TextView) view.findViewById(R.id.detail_inj_c_schedule_fragment_txt_title);
        txtShotnumber = (TextView) view.findViewById(R.id.detail_inj_c_schedule_fragment_txt_shot_number);
        ckbIsInject = (CheckBox) view.findViewById(R.id.detail_inj_c_schedule_fragment_ckb_is_inject);
        edtDayInjection = (EditText) view.findViewById(R.id.detail_inj_c_schedule_fragment_edt_day_injection);
        edtDayInjection.setOnClickListener(this);
        edtNote = (EditText) view.findViewById(R.id.detail_inj_c_schedule_fragment_edt_note);

        btnSave = (Button) view.findViewById(R.id.detail_inj_c_schedule_fragment_btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void setImageIcon(int drawable) {
        Glide.with(getContext())
                .load(drawable)
                .fitCenter()
                .into(imgIcon);
    }

    @Override
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    @Override
    public void setShotNumber(int shotNumber) {
        String strShotNumber = getString(R.string.shot_number_inject, String.valueOf(shotNumber));
        txtShotnumber.setText(strShotNumber);
    }

    @Override
    public void setIsInject(int isInject) {
        if (isInject == VaccinesScheduleLocalContract.InjectionScheduleEntry.IS_INJECT_DONE) {
            ckbIsInject.setChecked(true);
        } else {
            ckbIsInject.setChecked(false);
        }
    }

    @Override
    public void showDatePickerDialogDobChild(DatePickerDialog.OnDateSetListener callback, Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void setDayInjection(long dayInjection) {
        edtDayInjection.setText(DateTimeHelper.sdfDateFull.format(dayInjection));
        this.dayInjection = dayInjection;
    }


    @Override
    public void setNote(String note) {
        edtNote.setText(note);
    }

    @Override
    public void showUpdateSuccessfully() {
        Toast.makeText(getContext(), getString(R.string.toast_update_successfully), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_inj_c_schedule_fragment_edt_day_injection:
                presenter.setupDatePickerDialogDobChild();
                break;
            case R.id.detail_inj_c_schedule_fragment_btn_save:
                updateInjection();
                break;
            default:
                break;

        }
    }

    private void showInformationVaccine() {

    }

    private void updateInjection() {
        int isInject = VaccinesScheduleLocalContract.InjectionScheduleEntry.IS_INJECT_NOT_DONE;
        if (ckbIsInject.isChecked()) {
            isInject = VaccinesScheduleLocalContract.InjectionScheduleEntry.IS_INJECT_DONE;
        }
        presenter.updateInjection(isInject, dayInjection, edtNote.getText().toString().trim());

        if (isInject == VaccinesScheduleLocalContract.InjectionScheduleEntry.IS_INJECT_NOT_DONE) {
            presenter.setAlarmOfDayInjectionChanged(getContext(), injectionId);
        }
    }
}
