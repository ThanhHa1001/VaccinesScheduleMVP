package com.pimo.thea.vaccinesschedulemvp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by thea on 7/21/2017.
 */

public class PhotoHelper {
    public static final int ACTION_CAPTURE_PHOTO = 300;
    public static final int ACTION_GALLERY_PHOTO = 301;
    public static final int ACTION_CROP_PHOTO = 302;
    private static final String LOG_TAG = "PhotoHelper";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String INTENT_ACTION_MEDIA_SCANNER_SCAN_FILE = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static File setupPhotoFile(String albumName) throws IOException {
        Log.d(LOG_TAG, "setupPhotoFile");
        File f = createImageFile(albumName);
        return f;
    }

    public static File createImageFile(String albumName) throws IOException {
        Log.d(LOG_TAG, "createImageFile");
        String timeStamp = DateTimeHelper.sdfTimeStamp.format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp;
        File albumFile = getAlbumDir(albumName);
        File imageFile = new File(albumFile, imageFileName + JPEG_FILE_SUFFIX);
        return imageFile;
    }

    private static File getAlbumDir(String albumName) {
        Log.d(LOG_TAG, "getAlbumDir");
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    albumName);
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(LOG_TAG, "Failed to create directory");
                    }
                }
            }
        } else {
            Log.v(LOG_TAG, "External storage is not mounted READ/WRITE");
        }
        return storageDir;
    }

    public static Uri galleryAddPhoto(Context context, String currentPhotoPath) {
        Log.d(LOG_TAG, "galleryAddPhoto");
        Intent mediaScanIntent = new Intent(INTENT_ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
        return contentUri;
    }
}
