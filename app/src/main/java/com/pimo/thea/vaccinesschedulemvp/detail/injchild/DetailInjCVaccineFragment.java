package com.pimo.thea.vaccinesschedulemvp.detail.injchild;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.InjVaccine;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;

import java.util.Calendar;

/**
 * Created by thea on 8/3/2017.
 */

public class DetailInjCVaccineFragment extends Fragment implements DetailInjCContract.ViewCVaccine, View.OnClickListener {
    private DetailInjCContract.PresenterCVaccine presenter;
    private long injectionId;

    private TextView txtTitle;
    private EditText edtLotNumber;
    private EditText edtExpDate;
    private EditText edtDoctorInjected;
    private EditText edtNurseInjected;
    private EditText edtPlaceOfVacation;
    private Button btnSave;

    private long expDate = -1L;

    public DetailInjCVaccineFragment() {

    }

    public static DetailInjCVaccineFragment newInstance() {
        return new DetailInjCVaccineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        injectionId = b.getLong(DetailInjCActivity.INJECTION_ID_ARGS);

        Log.d("DetailInjCVaccine", " injection id: " + injectionId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_inj_child_vaccine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DetailInjCVaccinePresenter(injectionId,
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this);

        txtTitle = (TextView) view.findViewById(R.id.detail_inj_c_vaccine_fragment_txt_title);
        edtLotNumber = (EditText) view.findViewById(R.id.detail_inj_c_vaccine_fragment_edt_lot_number);
        edtExpDate = (EditText) view.findViewById(R.id.detail_inj_c_vaccine_fragment_edt_exp_date);
        edtExpDate.setOnClickListener(this);
        edtDoctorInjected = (EditText) view.findViewById(R.id.detail_inj_c_vaccine_fragment_edt_doctor_injected);
        edtNurseInjected = (EditText) view.findViewById(R.id.detail_inj_c_vaccine_fragment_edt_nurse_injected);
        edtPlaceOfVacation = (EditText) view.findViewById(R.id.detail_inj_c_vaccine_fragment_edt_pov);
        btnSave = (Button) view.findViewById(R.id.detail_inj_c_vaccine_fragment_btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    @Override
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    @Override
    public void setLotNumber(String lotNumber) {
        edtLotNumber.setText(lotNumber);
    }

    @Override
    public void showDatePickerDialogExpDate(DatePickerDialog.OnDateSetListener callback, Calendar calendar) {
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
    public void setExpDate(long expDate) {
        if (expDate == -1L) {
            return;
        }
        String strExpDate = DateTimeHelper.sdfDateFull.format(expDate);
        edtExpDate.setText(strExpDate);
        this.expDate = expDate;
    }

    @Override
    public void setDoctorInjected(String doctorName) {
        edtDoctorInjected.setText(doctorName);
    }

    @Override
    public void setNurseInjected(String nurseName) {
        edtNurseInjected.setText(nurseName);
    }

    @Override
    public void setPlaceOfVaccination(String placeOfVaccination) {
        edtPlaceOfVacation.setText(placeOfVaccination);
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
            case R.id.detail_inj_c_vaccine_fragment_edt_exp_date:
                presenter.setupDatePickerDialogExpDate();
                break;
            case R.id.detail_inj_c_vaccine_fragment_btn_save:
                saveInjectionVaccine();
                break;
            default:
                break;
        }
    }

    private void saveInjectionVaccine() {
        String title = txtTitle.getText().toString();
        String lotNumber = edtLotNumber.getText().toString().trim();
        String doctor = edtDoctorInjected.getText().toString().trim();
        String nurse = edtNurseInjected.getText().toString().trim();
        String placeOfVaccination = edtPlaceOfVacation.getText().toString().trim();
        InjVaccine injVaccine = new InjVaccine(title,
                lotNumber,
                expDate,
                doctor,
                nurse,
                placeOfVaccination,
                injectionId);
        presenter.saveInjectionVaccine(injVaccine);
    }
}
