package com.getui.checkwork.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by wang on 16/6/30.
 */
public class PermissionsCheckUtils {
    // 判断是否缺少权限
    // public static boolean lacksPermission(Context mContext, String permission) {
    // // LogUtils.d("lacksPermission:" + (ContextCompat.checkSelfPermission(mContext, permission)
    // // != PackageManager.PERMISSION_GRANTED));
    // // return ContextCompat.checkSelfPermission(mContext, permission) !=
    // // PackageManager.PERMISSION_GRANTED;
    // return lacksPermission(mContext,permission);
    // }

    // 判断权限集合
    // public static boolean lacksPermissions(Context mContext, String... permissions) {
    // for (String permission : permissions) {
    // if (lacksPermission(mContext, permission)) {
    // return true;
    // }
    // }
    // return false;
    // }


    public static boolean lacksPermission(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        boolean isPermission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, context.getPackageName()));
        LogUtils.d("isPermission:" + isPermission);
        return !isPermission;
    }

    // 含有全部的权限
    public static boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
