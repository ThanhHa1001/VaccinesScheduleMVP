package com.pimo.thea.vaccinesschedulemvp.utils;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/1/2017.
 */

public class VaccineHelper {
    public static final String VACCINE_LAO = "VCBCG";
    public static final String VACCINE_VIEM_GAN_B = "VCVIEMGANB";
    public static final String VACCINE_QUINVAXEM = "VCQUINVAXEM";
    public static final String VACCINE_BAI_LIET = "VCOPV";
    public static final String VACCINE_SOI = "VCSOI";
    public static final String VACCINE_SOI_RUBELLA = "VCSOIRUBELLA";
    public static final String VACCINE_TIEM_NHAC_BACHHAU_UONVAN_HOGA = "VCDPT";
    public static final String VACCINE_VIEM_NAO_NHAT_BAN = "VCJE";
    public static final String VACCINE_TA = "VCTA";
    public static final String VACCINE_THUON_HAN = "VCTHUONGHAN";

    public static int getIconResourcesInjectNotDone(String vaccineCode) {
        if (vaccineCode.equals(VACCINE_LAO)) {
            return R.drawable.lao_hienlanh;
        } else if (vaccineCode.equals(VACCINE_VIEM_GAN_B)) {
            return R.drawable.viemgan_b_hienlanh;
        } else if (vaccineCode.equals(VACCINE_QUINVAXEM)) {
            return R.drawable.quinvaxem_hienlanh;
        } else if (vaccineCode.equals(VACCINE_BAI_LIET)) {
            return R.drawable.bailiet_hienlanh;
        } else if (vaccineCode.equals(VACCINE_SOI)) {
            return R.drawable.soi_hienlanh;
        } else if (vaccineCode.equals(VACCINE_SOI_RUBELLA)) {
            return R.drawable.soi_rubella_hienlanh;
        } else if (vaccineCode.equals(VACCINE_TIEM_NHAC_BACHHAU_UONVAN_HOGA)) {
            return R.drawable.bachhau_uonvan_hoga_hienlanh;
        } else if (vaccineCode.equals(VACCINE_VIEM_NAO_NHAT_BAN)) {
            return R.drawable.viemnaonhatban_hienlanh;
        } else if (vaccineCode.equals(VACCINE_TA)) {
            return R.drawable.ta_hienlanh;
        } else {
            return R.drawable.thuonghan_hienlanh;
        }
    }

    public static int getIconResourcesInjectDone(String vaccineCode) {
        if (vaccineCode.equals(VACCINE_LAO)) {
            return R.drawable.lao_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_VIEM_GAN_B)) {
            return R.drawable.viemgan_b_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_QUINVAXEM)) {
            return R.drawable.quinvaxem_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_BAI_LIET)) {
            return R.drawable.bailiet_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_SOI)) {
            return R.drawable.soi_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_SOI_RUBELLA)) {
            return R.drawable.soi_rubella_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_TIEM_NHAC_BACHHAU_UONVAN_HOGA)) {
            return R.drawable.bachhau_uonvan_hoga_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_VIEM_NAO_NHAT_BAN)) {
            return R.drawable.viemnaonhatban_yeuduoi;
        } else if (vaccineCode.equals(VACCINE_TA)) {
            return R.drawable.ta_yeuduoi;
        } else {
            return R.drawable.thuonghan_yeuduoi;
        }
    }
}
