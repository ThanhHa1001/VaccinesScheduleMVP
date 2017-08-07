package com.pimo.thea.vaccinesschedulemvp.utils;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/6/2017.
 */

public class DiseaseHelper {
    public static final String DISEASE_LAO = "BL";
    public static final String DISEASE_VIEM_GAN_B = "BVGB";
    public static final String DISEASE_BACH_HAU = "BBH";
    public static final String DISEASE_HO_GA = "BHG";
    public static final String DISEASE_UON_VAN = "BUV";
    public static final String DISEASE_HIB = "BH";
    public static final String DISEASE_BAI_LIET = "BBL";
    public static final String DISEASE_SOI = "BS";
    public static final String DISEASE_VIEM_NAO_NHAT_BAN = "BVNNB";
    public static final String DISEASE_TA = "BT";
    public static final String DISEASE_THUON_HAN = "BTH";

    public static int getIconResourcesDiseaseCode(String diseaseCode) {
        if (diseaseCode.equals(DISEASE_LAO)) {
            return R.drawable.lao_hienlanh;
        } else if (diseaseCode.equals(DISEASE_VIEM_GAN_B)) {
            return R.drawable.viemgan_b_hienlanh;
        } else if (diseaseCode.equals(DISEASE_BACH_HAU)) {
            return R.drawable.bachhau_hienlanh;
        } else if (diseaseCode.equals(DISEASE_HO_GA)) {
            return R.drawable.hoga_hienlanh;
        } else if (diseaseCode.equals(DISEASE_UON_VAN)) {
            return R.drawable.uonvan_hienlanh;
        } else if (diseaseCode.equals(DISEASE_HIB)) {
            return R.drawable.soi_hienlanh;
        } else if (diseaseCode.equals(DISEASE_BAI_LIET)) {
            return R.drawable.bailiet_hienlanh;
        } else if (diseaseCode.equals(DISEASE_SOI)) {
            return R.drawable.soi_hienlanh;
        } else if (diseaseCode.equals(DISEASE_VIEM_NAO_NHAT_BAN)) {
            return R.drawable.viemnaonhatban_hienlanh;
        } else if (diseaseCode.equals(DISEASE_TA)) {
            return R.drawable.ta_hienlanh;
        } else {
            return R.drawable.thuonghan_hienlanh;
        }
    }
}
