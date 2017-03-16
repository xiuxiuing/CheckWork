package com.getui.checkwork.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by wang on 16/8/24.
 */
public class SystemUtils {
    public static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Throwable t) {
            LogUtils.e(t);
        }
        return "";
    }
}
