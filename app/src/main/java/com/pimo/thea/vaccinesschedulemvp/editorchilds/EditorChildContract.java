package com.pimo.thea.vaccinesschedulemvp.editorchilds;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.pimo.thea.vaccinesschedulemvp.BasePresenter;
import com.pimo.thea.vaccinesschedulemvp.data.Child;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thea on 7/10/2017.
 */

public class EditorChildContract {

    interface View {

        void showPermissionStorage();

        void showPermissionStorageInSettings();

        void showPermissionStorageNotGranted();

        void showCamera(File outputPhotoFile);

        void showPictureFromStorage();

        void showCropImageUnavailable();

        void showCropImageDefault(Intent intent, ResolveInfo resolveInfo, int requestCodeCropPhoto);

        void showCropOptionImage(ArrayList<CroppingOption> croppingOptions, int requestCodeCropPhoto, File outputPhotoFile);

        void setImageAvatar(File fileImage);

        void setImageAvatar(int drawableImage);

        void showRequireFullname();

        void hideRequireFullname();

        void showRequireDOB();

        void hideRequireDOB();

        void showRequireGender();

        void hideRequireGender();

        void showChildList();

        void showHome();

        void setFullname(String fullname);

        void setGender(int gender);

        void setNewBornWeight(float newBornWeight);

        void setNewBornHeight(int newBornHeight);

        void setPresentWeight(float presentWeight);

        void setPresentHeight(int presentHeight);

        void showDatePickerDialogTobChild(TimePickerDialog.OnTimeSetListener callback, Calendar calendar);

        void setTimeForEdtTob(long tob);

        void showDatePickerDialogDobChild(DatePickerDialog.OnDateSetListener callback, Calendar calendar, long minDate, long maxDate);

        void setDateForEdtDob(long dob);

        void showEmptyChildError();

        void showDialogLoading();

        void dismissDialogLoading();

        boolean isActive();
    }

    interface Presenter extends BasePresenter{

        void saveChild(String fullname,
                       String gender,
                       String tob, String dob,
                       String newBornWeight, String newBornHeight, String presentWeight, String presentHeight,
                       String avatarUrl);

        void deleteChild(long childId);

        void editChild();

        void requestPermissionResult(int requestCode, String[] permissions, int[] grantResults);

        void activityResult(int requestCode, int resultCode, Intent data);

        void chooseImage();

        void setupTimePickerDialogTobChild();

        void setupDatePickerDialogDobChild();

        void validateFullname(TextInputEditText edtFullname);

        void validateDOB(TextInputEditText edtDOB);

        void validateGender(int gender);
    }
}
