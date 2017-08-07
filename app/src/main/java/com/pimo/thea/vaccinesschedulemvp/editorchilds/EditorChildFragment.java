package com.pimo.thea.vaccinesschedulemvp.editorchilds;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalDataSource;
import com.pimo.thea.vaccinesschedulemvp.home.HomeActivity;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.PhotoHelper;
import com.pimo.thea.vaccinesschedulemvp.view.DialogLoading;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thea on 7/7/2017.
 */

public class EditorChildFragment extends Fragment implements EditorChildContract.View, View.OnClickListener {
    public static final String TAG = "EditorChildFragment";
    public static final String CHILD_ID_ARG = "childId";

    public static final int REQUEST_STORAGE = 1;
    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditorChildContract.Presenter presenter;

    private NestedScrollView nestedScrollView;
    private ImageView imgIcon;
    private TextView txtAddIcon;
    private TextInputLayout edtFullnameWrapper;
    private TextInputLayout edtDOBWrapper;
    private TextInputLayout edtGenderWrapper;
    private TextInputEditText edtFullname;
    private TextInputEditText edtTOB;
    private TextInputEditText edtDOB;
    private TextInputEditText edtNewBornWeight;
    private TextInputEditText edtNewBornHeight;
    private TextInputEditText edtPresentWeight;
    private TextInputEditText edtPresentHeight;
    private Spinner spGender;
    private DialogLoading dialogLoading;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int gender = VaccinesScheduleLocalContract.ChildEntry.GENDER_UNKNOWN;
    private long childId;
    private String avatarUrl;
    private long tobChild = 0L;
    private long dobChild = 0L;


    public EditorChildFragment() {

    }

    public static EditorChildFragment newInstance() {
        return new EditorChildFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        childId = bundle.getLong(CHILD_ID_ARG);
        Log.d(TAG, "child id: " + childId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new EditorChildPresenter(childId,
                VaccinesScheduleRepository.getInstance(VaccinesScheduleLocalDataSource.getInstance(getContext())),
                this,
                getActivity());

//        scrollView = (NestedScrollView) view.findViewById(R.id.editor_child_scroll_view);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.editor_child_nested_scroll_view);
        imgIcon = (ImageView) view.findViewById(R.id.editor_child_img_icon);
        txtAddIcon = (TextView) view.findViewById(R.id.editor_child_txt_add_img_icon);

        edtFullnameWrapper = (TextInputLayout) view.findViewById(R.id.editor_child_edt_full_name_wrapper);
        edtDOBWrapper = (TextInputLayout) view.findViewById(R.id.editor_child_edt_dob_wrapper);
        edtGenderWrapper = (TextInputLayout) view.findViewById(R.id.editor_child_sp_gender_wrapper);

        edtFullname = (TextInputEditText) view.findViewById(R.id.editor_child_edt_full_name);
        edtTOB = (TextInputEditText) view.findViewById(R.id.editor_child_edt_tob);
        edtDOB = (TextInputEditText) view.findViewById(R.id.editor_child_edt_dob);
        edtNewBornWeight = (TextInputEditText) view.findViewById(R.id.editor_child_edt_new_born_weight);
        edtNewBornHeight = (TextInputEditText) view.findViewById(R.id.editor_child_edt_new_born_height);
        edtPresentWeight = (TextInputEditText) view.findViewById(R.id.editor_child_edt_present_weight);
        edtPresentHeight = (TextInputEditText) view.findViewById(R.id.editor_child_edt_present_height);

        spGender = (Spinner) view.findViewById(R.id.editor_child_sp_gender);

        imgIcon.setOnClickListener(this);
        txtAddIcon.setOnClickListener(this);
        edtTOB.setOnClickListener(this);
        edtDOB.setOnClickListener(this);

        edtFullname.addTextChangedListener(new MyTextWatcher(edtFullname));
        edtDOB.addTextChangedListener(new MyTextWatcher(edtDOB));


        setupSpinnerGender();

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bind();
    }

    private void setupSpinnerGender() {
        final ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spGender.setAdapter(genderSpinnerAdapter);

        spGender.setSelection(0, false);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selection = (String) adapterView.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        gender = VaccinesScheduleLocalContract.ChildEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        gender = VaccinesScheduleLocalContract.ChildEntry.GENDER_FEMALE;
                    } else {
                        gender = VaccinesScheduleLocalContract.ChildEntry.GENDER_UNKNOWN;
                    }
                    presenter.validateGender(gender);
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = VaccinesScheduleLocalContract.ChildEntry.GENDER_UNKNOWN;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.editor_child_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.editor_child_menu_delete);
        if (childId == -1) {
            menuItem.setVisible(false);
        } else {
            menuItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editor_child_menu_save:
                hideKeyboard();
                presenter.saveChild(edtFullname.getText().toString(),
                        String.valueOf(gender),
                        String.valueOf(tobChild), String.valueOf(dobChild),
                        edtNewBornWeight.getText().toString(), edtNewBornHeight.getText().toString(),
                        edtPresentWeight.getText().toString(), edtPresentHeight.getText().toString(),
                        avatarUrl);
                return true;
            case R.id.editor_child_menu_delete:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showPermissionStorage() {
        Snackbar.make(nestedScrollView,
                getString(R.string.permission_storage_rationale),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.permission_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermissions(PERMISSIONS_STORAGE, REQUEST_STORAGE);
                    }
                })
                .show();
    }

    @Override
    public void showPermissionStorageInSettings() {
        Snackbar.make(nestedScrollView,
                getString(R.string.permission_storage_how_to_open_in_settings),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.permission_open_settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void showPermissionStorageNotGranted() {
        Snackbar.make(nestedScrollView,
                        getString(R.string.permission_not_granted),
                        Snackbar.LENGTH_SHORT)
                        .show();
    }

    @Override
    public void showCamera(File outputPhotoFile) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = Uri.fromFile(outputPhotoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePictureIntent, PhotoHelper.ACTION_CAPTURE_PHOTO);
    }

    @Override
    public void showPictureFromStorage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PhotoHelper.ACTION_GALLERY_PHOTO);
    }

    @Override
    public void showCropImageUnavailable() {
        Snackbar.make(nestedScrollView,
                getString(R.string.editor_child_crop_image_unavailable),
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showCropImageDefault(Intent intent, ResolveInfo resolveInfo, int requestCodeCropPhoto) {
        Intent intentCropApp = new Intent(intent);
        intentCropApp.setComponent(new ComponentName(resolveInfo.activityInfo.packageName,
                resolveInfo.activityInfo.name));
        startActivityForResult(intentCropApp, PhotoHelper.ACTION_CROP_PHOTO);
    }

    @Override
    public void showCropOptionImage(final ArrayList<CroppingOption> croppingOptions, final int requestCodeCropPhoto, final File outputPhotoFile) {
        final CroppingOptionAdapter croppingOptionAdapter = new CroppingOptionAdapter(getContext(), croppingOptions);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.choose_cropping_app))
                .setCancelable(false)
                .setAdapter(croppingOptionAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        startActivityForResult(croppingOptions.get(item).getAppItent(), requestCodeCropPhoto);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Uri photoUri = Uri.fromFile(outputPhotoFile);
                        if (photoUri != null) {
                            getContext().getContentResolver().delete(photoUri, null, null);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_alert_delete_confirmation));
        builder.setPositiveButton(getString(R.string.dialog_alert_delete_action_confirmation), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.deleteChild(childId);
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_alert_not_delete_action_confirmation), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void setImageAvatar(File fileImage) {
        Log.d(TAG, "---------setImageAvatar---------FILE");
        Glide.with(getActivity())
                .load(fileImage)
                .fitCenter()
                .into(imgIcon);
        avatarUrl = fileImage.toString();
    }

    @Override
    public void setImageAvatar(int drawableImage) {
        Log.d(TAG, "---------setImageAvatar---------DRAWABLE");
        Glide.with(getActivity())
                .load(drawableImage)
                .fitCenter()
                .into(imgIcon);
    }

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
//            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.requestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void showRequireFullname() {
        showKeyboard(edtFullname);
        edtFullnameWrapper.setError("Bạn chưa nhập tên cho bé");
    }

    @Override
    public void hideRequireFullname() {
        edtFullnameWrapper.setErrorEnabled(false);
    }

    @Override
    public void showRequireDOB() {
        Log.d(TAG, "Ban chua chon ngay sinh cho be");
        hideKeyboard();
        edtFullname.clearFocus();
        edtDOBWrapper.setError("Bạn chưa chọn ngày sinh cho bé");
    }

    @Override
    public void hideRequireDOB() {
        edtDOBWrapper.setErrorEnabled(false);
    }

    @Override
    public void showRequireGender() {
        edtGenderWrapper.setError("Bạn chưa chọn giới tính cho bé");
    }

    @Override
    public void hideRequireGender() {
        edtGenderWrapper.setErrorEnabled(false);
    }



    @Override
    public void showChildList() {
        Log.d(TAG, "show CHild list");
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public void setFullname(String fullname) {
        edtFullname.setText(fullname);
    }

    @Override
    public void setGender(int gender) {
        spGender.setSelection(gender);
    }

    @Override
    public void setNewBornWeight(float newBornWeight) {
        edtNewBornWeight.setText(String.valueOf(newBornWeight));
    }

    @Override
    public void setNewBornHeight(int newBornHeight) {
        edtNewBornHeight.setText(String.valueOf(newBornHeight));
    }

    @Override
    public void setPresentWeight(float presentWeight) {
        edtPresentWeight.setText(String.valueOf(presentWeight));
    }

    @Override
    public void setPresentHeight(int presentHeight) {
        edtPresentHeight.setText(String.valueOf(presentHeight));
    }

    @Override
    public void showDatePickerDialogTobChild(TimePickerDialog.OnTimeSetListener callback, Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                callback,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    @Override
    public void setTimeForEdtTob(long tob) {
        edtTOB.setText(String.valueOf(DateTimeHelper.sdfTimeHHmm.format(tob)));
        tobChild = tob;
    }

    @Override
    public void showDatePickerDialogDobChild(DatePickerDialog.OnDateSetListener callback, Calendar calendar, long minDate, long maxDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                callback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        datePickerDialog.show();
    }

    @Override
    public void setDateForEdtDob(long dob) {
        edtDOB.setText(String.valueOf(DateTimeHelper.sdfDateFull.format(dob)));
        dobChild = dob;
    }

    @Override
    public void showEmptyChildError() {
        Snackbar.make(getView(), "Dữ liệu bị lỗi trong quá trình load!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDialogLoading() {
        if (dialogLoading == null) {
            dialogLoading = new DialogLoading(getContext(), getString(R.string.dialog_loading_loading));
        } else {
            dialogLoading.setTitle(getString(R.string.dialog_loading_loading));
        }
        dialogLoading.show();
    }

    @Override
    public void dismissDialogLoading() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }

    private void showMessage() {
        Snackbar.make(nestedScrollView, getString(R.string.editor_child_validate), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editor_child_img_icon:
            case R.id.editor_child_txt_add_img_icon:
                presenter.chooseImage();
                break;
            case R.id.editor_child_edt_tob:
                presenter.setupTimePickerDialogTobChild();
                break;
            case R.id.editor_child_edt_dob:
                presenter.setupDatePickerDialogDobChild();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editor_child_edt_full_name:
                    presenter.validateFullname(edtFullname);
                    break;
                case R.id.editor_child_edt_dob:
                    presenter.validateDOB(edtDOB);
                    break;
            }
        }
    }
}
