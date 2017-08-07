package com.pimo.thea.vaccinesschedulemvp.utils;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/6/2017.
 */

public class ChildcareHelper {

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static int getChildcareDrawableId(int id) {
        switch (id) {
            case 1:
                return R.drawable.ic_childcare_1;
            case 2:
                return R.drawable.ic_childcare_2;
            case 3:
                return R.drawable.ic_childcare_3;
            case 4:
                return R.drawable.ic_childcare_4;
            case 5:
                return R.drawable.ic_childcare_5;
            case 6:
                return R.drawable.ic_childcare_6;
            case 7:
                return R.drawable.ic_childcare_7;
            case 8:
                return R.drawable.ic_childcare_8;
            case 9:
                return R.drawable.ic_childcare_9;
            case 10:
                return R.drawable.ic_childcare_10;
            case 11:
                return R.drawable.ic_childcare_11;
            case 12:
            default:
                return R.drawable.ic_childcare_12;
        }
    }
}
