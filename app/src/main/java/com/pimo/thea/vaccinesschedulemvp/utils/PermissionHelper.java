package com.pimo.thea.vaccinesschedulemvp.utils;

import android.content.pm.PackageManager;

/**
 * Created by thea on 7/21/2017.
 */

public class PermissionHelper {
    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
