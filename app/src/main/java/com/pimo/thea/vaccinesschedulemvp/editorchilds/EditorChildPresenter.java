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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.pimo.thea.vaccinesschedulemvp.R;
import com.pimo.thea.vaccinesschedulemvp.data.Child;
import com.pimo.thea.vaccinesschedulemvp.data.InjSchedule;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleDataSource;
import com.pimo.thea.vaccinesschedulemvp.data.source.VaccinesScheduleRepository;
import com.pimo.thea.vaccinesschedulemvp.data.source.local.VaccinesScheduleLocalContract;
import com.pimo.thea.vaccinesschedulemvp.receiver.InjectionAlarmReceiver;
import com.pimo.thea.vaccinesschedulemvp.utils.DateTimeHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.FileHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.PermissionHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.PhotoHelper;
import com.pimo.thea.vaccinesschedulemvp.utils.VaccineHelper;
import com.pimo.thea.vaccinesschedulemvp.view.DialogWarning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by thea on 7/10/2017.
 */

public class EditorChildPresenter implements EditorChildContract.Presenter {

    private static final String LOG_TAG = "EditorChildPresenter";
    private VaccinesScheduleRepository repository;
    private InjectionAlarmReceiver alarmReceiver;
    private long childId = -1;
    private long tobChild;
    private long dobChild;
    private long dobChildNotChanged;

    // hien thi anh o truong hop editChild
    private String avatarUrlFromCropImage = null;
    // tranh lap lai viec goi method setImageAvatar do sau khi crop image setImageAvatar da dc goi,
    // o phuong thuc bind() editChild se goi setImageAvatar mot lan nua --> lap lai --> ton ram
    private boolean imageAvatarLoaded = false;
    private Calendar calendar;
    private File outputPhotoFile;

    private EditorChildContract.View view;
    private Activity activity;

    public EditorChildPresenter(long childId, VaccinesScheduleRepository repository, EditorChildContract.View view, Activity activity) {
        this.childId = childId;
        this.repository = repository;
        this.view = view;
        this.activity = activity;
        calendar = Calendar.getInstance();
    }

    private boolean isNewChild() {
        return childId == -1;
    }

    @Override
    public void bind() {
        if (!isNewChild()) {
            editChild();
        }
    }

    @Override
    public void saveChild(String fullname, String gender, String tob, String dob, String newBornWeight, String newBornHeight, String presentWeight, String presentHeight, String avatarUrl) {
        if (isNewChild()) {
            createChild(fullname, gender, tob, dob, newBornWeight, newBornHeight, presentWeight, presentHeight, avatarUrl);
        } else {
            updateChild(fullname, gender, tob, dob, newBornWeight, newBornHeight, presentWeight, presentHeight, avatarUrl);
        }
    }

    @Override
    public void deleteChild(long childId) {
        repository.deleteChild(childId);
        repository.refreshChildList();

        view.showHome();
    }

    @Override
    public void editChild() {
        if (isNewChild()) {
            throw new RuntimeException("editChild() was called but child is new");
        }

        repository.getChildWithNextInject(childId, new VaccinesScheduleDataSource.GetChildCallback() {
            @Override
            public void onChildLoaded(Child child) {
                if (view.isActive()) {
                    String avatarUrl = child.getAvatarUrl();
                    int gender = child.getGender();
                    if (avatarUrl == null && avatarUrlFromCropImage == null) {
                        int avatarDrawable;
                        if (gender == VaccinesScheduleLocalContract.ChildEntry.GENDER_MALE) {
                            avatarDrawable = R.drawable.ic_face_black_male_24dp;
                        } else {
                            avatarDrawable = R.drawable.ic_face_black_24dp;
                        }
                        Log.d("EditorChildPresenter", "---------setImageAvatar---------DRAWABLE");
                        view.setImageAvatar(avatarDrawable);
                    } else {
                        if (!imageAvatarLoaded) {
                            avatarUrlFromCropImage = avatarUrl;
                            Log.d("EditorChildPresenter", "---------setImageAvatar---------FILE");
                            view.setImageAvatar(new File(avatarUrlFromCropImage));
                        }
                    }

                    view.setFullname(child.getFullName());

                    long tobForEdt = child.getTimeOfBirth();
                    if (tobForEdt != 0) {
                        view.setTimeForEdtTob(child.getTimeOfBirth());
                    }

                    dobChildNotChanged = child.getDateOfBirth();
                    view.setDateForEdtDob(dobChildNotChanged);

                    view.setGender(gender);

                    float newBornWeightForEdt = child.getNewBornWeight();
                    if (newBornWeightForEdt != 0) {
                        view.setNewBornWeight(newBornWeightForEdt);
                    }

                    int newBornHeightForEdt = child.getNewBornHeight();
                    if (newBornHeightForEdt != 0) {
                        view.setNewBornHeight(child.getNewBornHeight());
                    }

                    float presentWeightForEdt = child.getPresentWeight();
                    if (presentWeightForEdt != 0) {
                        view.setPresentWeight(presentWeightForEdt);
                    }

                    int presentHeightForEdt = child.getPresentHeight();
                    if (presentHeightForEdt != 0) {
                        view.setPresentHeight(presentHeightForEdt);
                    }
                }
            }

            @Override
            public void onDataChildNotAvailable() {
                if (view.isActive()) {
                    view.showEmptyChildError();
                }
            }
        });
    }

    @Override
    public void requestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == EditorChildFragment.REQUEST_STORAGE) {
            if (PermissionHelper.verifyPermissions(grantResults)) {
            } else {
                view.showPermissionStorageNotGranted();
            }
        }
    }

    @Override
    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoHelper.ACTION_CAPTURE_PHOTO) {
            if (resultCode == RESULT_OK) {
                PhotoHelper.galleryAddPhoto(activity, outputPhotoFile.getAbsolutePath());
                cropImage();
            }
        } else if (requestCode == PhotoHelper.ACTION_GALLERY_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    new ReadAndCopyPhoto().execute(data);
                }
            }
        } else if (requestCode == PhotoHelper.ACTION_CROP_PHOTO) {
            if (resultCode == RESULT_OK) {
                Log.d("EditorChildPresenter", "---------setImageAvatar---------FILE---OnActivityResult");
                view.setImageAvatar(outputPhotoFile);
                avatarUrlFromCropImage = outputPhotoFile.toString();
                imageAvatarLoaded = true;
                outputPhotoFile = null;
            }
        }
    }

    @Override
    public void chooseImage() {
        final CharSequence[] items = {activity.getString(R.string.capture_photo),
                activity.getString(R.string.choose_from_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(activity.getString(R.string.capture_photo))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermission()) {
                            requestStoragePermission();
                        } else {
                            if (PhotoHelper.isIntentAvailable(activity, MediaStore.ACTION_IMAGE_CAPTURE)) {
                                takePictureIntent();
                            }
                        }
                    } else {
                        if (PhotoHelper.isIntentAvailable(activity, MediaStore.ACTION_IMAGE_CAPTURE)) {
                            takePictureIntent();
                        }
                    }
                } else if (items[item].equals(activity.getString(R.string.choose_from_gallery))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkPermission()) {
                            requestStoragePermission();
                        } else {
                            takePictureGallery();
                        }
                    } else {
                        takePictureGallery();
                    }
                }
            }
        });
        builder.show();
    }

    private void cropImage() {
        Log.d(LOG_TAG, "Start crop photo");
        final ArrayList<CroppingOption> croppingOptions = new ArrayList<>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        int size = list.size();

        if (size == 0) {
            view.showCropImageUnavailable();
        } else {
            Uri photoUri = Uri.fromFile(outputPhotoFile);
            Log.d(LOG_TAG, photoUri + "");
            intent.setData(photoUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputPhotoFile);
            if (size == 1) {
                ResolveInfo resolveInfo = list.get(0);
                view.showCropImageDefault(intent, resolveInfo, PhotoHelper.ACTION_CROP_PHOTO);
            } else {
                for (ResolveInfo resolveInfo : list) {
                    CroppingOption croppingOption = new CroppingOption();
                    croppingOption.setTitle(activity.getPackageManager().getApplicationLabel(resolveInfo.activityInfo.applicationInfo));
                    croppingOption.setIcon(activity.getPackageManager().getApplicationIcon(resolveInfo.activityInfo.applicationInfo));
                    croppingOption.setAppItent(new Intent(intent));
                    croppingOption.getAppItent().setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                    croppingOptions.add(croppingOption);
                }
                view.showCropOptionImage(croppingOptions, PhotoHelper.ACTION_CROP_PHOTO, outputPhotoFile);
            }
        }
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            view.showPermissionStorage();
        } else {
            view.showPermissionStorageInSettings();
        }
    }

    private void takePictureIntent() {
        createOutputPhotoFile();
        view.showCamera(outputPhotoFile);
    }

    private void takePictureGallery() {
        createOutputPhotoFile();
        view.showPictureFromStorage();
    }

    private void createOutputPhotoFile() {
        if (outputPhotoFile == null) {
            try {
                outputPhotoFile = PhotoHelper.setupPhotoFile("VaccinesScheduleMVP");
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error file", e);
                outputPhotoFile = null;
            }
        }
    }

    @Override
    public void setupTimePickerDialogTobChild() {
        TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                tobChild = calendar.getTimeInMillis();

                view.setTimeForEdtTob(tobChild);
            }
        };
        view.showDatePickerDialogTobChild(callback, calendar);
    }

    @Override
    public void setupDatePickerDialogDobChild() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dobChild = calendar.getTimeInMillis();

                view.setDateForEdtDob(dobChild);
            }
        };

        Calendar calendarForDatePickerDialog = calendar;
        long currentTime = System.currentTimeMillis();
        long changeTime = calendar.getActualMinimum(Calendar.YEAR);
        if (isNewChild()) {
            currentTime = System.currentTimeMillis();
            calendar.setTimeInMillis(currentTime);
            int currentYear = calendar.get(Calendar.YEAR);

            int changeYear = currentYear - 3;
            int changeMonth = calendar.get(Calendar.MONTH);
            int changeDay = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.YEAR, changeYear);
            calendar.set(Calendar.MONTH, changeMonth);
            calendar.set(Calendar.DAY_OF_MONTH, changeDay);
            changeTime = calendar.getTimeInMillis();

            calendarForDatePickerDialog.set(Calendar.YEAR, currentYear);
        }
        view.showDatePickerDialogDobChild(callback, calendarForDatePickerDialog, changeTime, currentTime);
    }

    private void createChild(String fullname, String gender, String tob, String dob, String newBornWeight, String newBornHeight, String presentWeight, String presentHeight, String avatarUrl) {
        Log.d("EditorChildPresenter", "Name child: " + fullname + " gender: " + gender
                + " tob: " + tob + " dob: " + dob
                + " new born weight: " + newBornWeight + " new born height: " + newBornHeight
                + " present weight: " + presentWeight + " present height: " + presentHeight
                + " avatar url: " + avatarUrl);

        Log.d("EditorChildPresenter", "Name child: " + fullname);
        if (fullname.isEmpty() || fullname.length() == 0) {
            view.showRequireFullname();
            return;
        }

        Log.d("EditorChildPresenter", "DOB: " + dob);
        long dateOfBirth = Long.parseLong(dob);
        if (dateOfBirth == 0) {
            view.showRequireDOB();
            return;
        }

        Log.d("EditorChildPresenter", "Gender: " + gender);
        int genderChild = Integer.parseInt(gender);
        if (genderChild == VaccinesScheduleLocalContract.ChildEntry.GENDER_UNKNOWN) {
            view.showRequireGender();
            return;
        }

        long tobChild = Long.parseLong(tob);
        float newBornWeightChild = 0F;
        if (isNotEmpty(newBornWeight)) {
            newBornWeightChild = Float.parseFloat(newBornWeight);
        }

        float presentWeightChild = 0F;
        if (isNotEmpty(presentWeight)) {
            presentWeightChild = Float.parseFloat(presentWeight);
        }

        int newBornHeightChild = 0;
        if (isNotEmpty(newBornHeight)) {
            newBornHeightChild = Integer.parseInt(newBornHeight);
        }

        int presentHeightChild = 0;
        if (isNotEmpty(presentHeight)) {
            presentHeightChild = Integer.parseInt(presentHeight);
        }

        Child child = new Child(fullname,
                genderChild,
                tobChild, dobChild,
                newBornWeightChild, newBornHeightChild, presentWeightChild, presentHeightChild,
                avatarUrl);

        long rowEffected = repository.insertChild(child); // == child id

        repository.refreshChildList();

        insertInjSchedule(rowEffected);

        setAlarmForEachInjection(activity, fullname, rowEffected);

        view.showChildList();

    }

    private void updateChild(String fullname, String gender, String tob, String dob, String newBornWeight, String newBornHeight, String presentWeight, String presentHeight, String avatarUrl) {
        if (fullname.isEmpty() || fullname.length() == 0) {
            view.showRequireFullname();
            return;
        }

        Log.d("EditorChildPresenter", "Gender: " + gender);
        int genderChild = Integer.parseInt(gender);
        if (genderChild == VaccinesScheduleLocalContract.ChildEntry.GENDER_UNKNOWN) {
            view.showRequireGender();
            return;
        }

        long tobChild = Long.parseLong(tob);
        float newBornWeightChild = 0F;
        if (isNotEmpty(newBornWeight)) {
            newBornWeightChild = Float.parseFloat(newBornWeight);
        }

        float presentWeightChild = 0F;
        if (isNotEmpty(presentWeight)) {
            presentWeightChild = Float.parseFloat(presentWeight);
        }

        int newBornHeightChild = 0;
        if (isNotEmpty(newBornHeight)) {
            newBornHeightChild = Integer.parseInt(newBornHeight);
        }

        int presentHeightChild = 0;
        if (isNotEmpty(presentHeight)) {
            presentHeightChild = Integer.parseInt(presentHeight);
        }

        long dateOfBirth = Long.parseLong(dob);
        if (dobChildNotChanged != dateOfBirth) {
            final Child child = new Child(childId,
                    fullname,
                    genderChild,
                    tobChild, dateOfBirth,
                    newBornWeightChild, newBornHeightChild, presentWeightChild, presentHeightChild,
                    avatarUrl);

            Log.d("EditorChildPresenter", "changed");
            final DialogWarning dialogWarning = new DialogWarning(activity);
            dialogWarning.show();
            dialogWarning.setOnButtonClickListener(new DialogWarning.OnButtonClickListener() {
                @Override
                public void onBtnYesClickListener() {
                    Log.d("EditorChildPresenter", "changed - Yes");
                    dialogWarning.dismiss();

                    repository.updateChild(child);

                    cancelAlarmForEachInjection(activity, child.getId());
                    repository.deleteInjScheduleByChildId(child.getId());

                    repository.refreshInjSchedules();

                    insertInjSchedule(child.getId());
                    setAlarmForEachInjection(activity, child.getFullName(), child.getId());

                    repository.refreshChildList();

                    view.showChildList();
                }

                @Override
                public void onBtnNotYesClickListener() {
                    Log.d("EditorChildPresenter", "changed - Not yes");
                    dialogWarning.dismiss();
                    dobChild = dobChildNotChanged;
                    view.setDateForEdtDob(dobChildNotChanged);
                }
            });
            return;
        }
        Log.d("EditorChildPresenter", "update - Da nhap du cac truong");

        Child child = new Child(childId,
                fullname,
                genderChild,
                tobChild, dateOfBirth,
                newBornWeightChild, newBornHeightChild, presentWeightChild, presentHeightChild,
                avatarUrl);
        repository.updateChild(child);

        repository.refreshChildList();
        repository.refreshInjSchedules();

        view.showChildList();
    }

    private boolean isNotEmpty(String string) {
        return string.length() > 0;
    }

    private void insertInjSchedule(long childID) {
        long dayInjectionCalculated;

        /**
         * Create object injSchedule
         * Value default: isInject = 0; isNotify = 0; note = ''
         */
        InjSchedule injSchedule = new InjSchedule();
        injSchedule.setChildId(childID);

        /**
         * Insert information injection schedule for new born child
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.NOT_WEEK);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_lao));
        injSchedule.setShotNumber(1);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_LAO);
        repository.insertInjSchedule(injSchedule);

        injSchedule.setTitle(activity.getString(R.string.vaccine_viem_gan_B));
        injSchedule.setShotNumber(1);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_VIEM_GAN_B);
        repository.insertInjSchedule(injSchedule);


        /**
         * Insert information injection schedule for 2 months child
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.TWO_MONTHS);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_quinvaxem));
        injSchedule.setShotNumber(1);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_QUINVAXEM);
        repository.insertInjSchedule(injSchedule);

        injSchedule.setTitle(activity.getString(R.string.vaccine_bai_liet));
        injSchedule.setShotNumber(1);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_BAI_LIET);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 3 months child
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.THREE_MONTHS);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_quinvaxem));
        injSchedule.setShotNumber(2);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_QUINVAXEM);
        repository.insertInjSchedule(injSchedule);

        injSchedule.setTitle(activity.getString(R.string.vaccine_bai_liet));
        injSchedule.setShotNumber(2);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_BAI_LIET);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 4 months child
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.FOUR_MONTHS);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_quinvaxem));
        injSchedule.setShotNumber(3);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_QUINVAXEM);
        repository.insertInjSchedule(injSchedule);

        injSchedule.setTitle(activity.getString(R.string.vaccine_bai_liet));
        injSchedule.setShotNumber(3);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_BAI_LIET);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 9 months child
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.NINE_MONTHS);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_soi));
        injSchedule.setShotNumber(1);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_SOI);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 18 months child (1 year 6 months)
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.ONE_YEAR_SIX_MONTHS);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_soi_rubbela));
        injSchedule.setShotNumber(2);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_SOI_RUBELLA);
        repository.insertInjSchedule(injSchedule);

        injSchedule.setTitle(activity.getString(R.string.vaccine_tiem_nhac_bachau_hoga_uonvan));
        injSchedule.setShotNumber(2);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_TIEM_NHAC_BACHHAU_UONVAN_HOGA);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 1 - 5 years child
         */
        injSchedule.setTitle(activity.getString(R.string.vaccine_viem_nao_nhat_ban));
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_VIEM_NAO_NHAT_BAN);

        long dayInjectionVaccineJE1 = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.ONE_YEAR);
        injSchedule.setDayInjection(dayInjectionVaccineJE1);
        injSchedule.setShotNumber(1);
        repository.insertInjSchedule(injSchedule);

        long dayInjectionVaccineJE2 = DateTimeHelper.setTimeForDayInjection(dayInjectionVaccineJE1, DateTimeHelper.TWO_WEEKS);
        injSchedule.setDayInjection(dayInjectionVaccineJE2);
        injSchedule.setShotNumber(2);
        repository.insertInjSchedule(injSchedule);

        long dayInjectionVaccineJE3 = DateTimeHelper.setTimeForDayInjection(dayInjectionVaccineJE2, DateTimeHelper.ONE_YEAR);
        injSchedule.setDayInjection(dayInjectionVaccineJE3);
        injSchedule.setShotNumber(3);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 2 - 5 years child
         */
        injSchedule.setTitle(activity.getString(R.string.vaccine_ta));
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_TA);

        long dayInjectionVaccineCholera1 = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.TWO_YEARS);
        injSchedule.setDayInjection(dayInjectionVaccineCholera1);
        injSchedule.setShotNumber(1);
        repository.insertInjSchedule(injSchedule);

        long dayInjectionVaccineCholera2 = DateTimeHelper.setTimeForDayInjection(dayInjectionVaccineCholera1, DateTimeHelper.TWO_MONTHS);
        injSchedule.setDayInjection(dayInjectionVaccineCholera2);
        injSchedule.setShotNumber(2);
        repository.insertInjSchedule(injSchedule);

        /**
         * Insert information injection schedule for 3 - 5 years child
         */
        dayInjectionCalculated = DateTimeHelper.setTimeForDayInjection(dobChild, DateTimeHelper.THREE_YEARS);
        injSchedule.setDayInjection(dayInjectionCalculated);

        injSchedule.setTitle(activity.getString(R.string.vaccine_thuong_han));
        injSchedule.setShotNumber(1);
        injSchedule.setVaccineCode(VaccineHelper.VACCINE_THUON_HAN);
        repository.insertInjSchedule(injSchedule);
    }

    @Override
    public void validateFullname(TextInputEditText edtFullname) {
        if (edtFullname.getText().toString().trim().isEmpty()) {
            view.showRequireFullname();
        } else {
            view.hideRequireFullname();
        }
    }

    @Override
    public void validateDOB(TextInputEditText edtDOB) {
        if (edtDOB.getText().toString().trim().isEmpty()) {
            view.showRequireDOB();
        } else {
            view.hideRequireDOB();
        }
    }

    @Override
    public void validateGender(int gender) {
        if (gender == VaccinesScheduleLocalContract.ChildEntry.GENDER_UNKNOWN) {
            view.showRequireGender();
        } else {
            view.hideRequireGender();
        }
    }

    private class ReadAndCopyPhoto extends AsyncTask<Intent, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showDialogLoading();
        }

        @Override
        protected Void doInBackground(Intent... intents) {
            Uri selectedImage = intents[0].getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Log.d(LOG_TAG, picturePath + "");
            FileHelper.copyFile(picturePath, outputPhotoFile.getAbsolutePath());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.dismissDialogLoading();
            cropImage();
        }
    }

    private void setAlarmForEachInjection(Context context, String nameChild, long childId) {
        if (alarmReceiver == null) {
            alarmReceiver = new InjectionAlarmReceiver();
        }

        alarmReceiver.setAlarmByChildId(context, nameChild, childId);
    }

    private void cancelAlarmForEachInjection(Context context, long childId) {
        if (alarmReceiver == null) {
            alarmReceiver = new InjectionAlarmReceiver();
        }
        alarmReceiver.cancelAlarmByChildId(context, childId);
    }

}
