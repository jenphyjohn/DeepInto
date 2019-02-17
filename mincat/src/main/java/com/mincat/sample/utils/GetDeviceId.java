package com.mincat.sample.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * @author Mings
 * @描述 获取设备ID
 */

public class GetDeviceId {

    private static TelephonyManager tm;

    private static String DEVICE_ID;

    public static String getDeviceId(Context context) {

        try {
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                DEVICE_ID = tm.getDeviceId();
                return DEVICE_ID;
            }
            return DEVICE_ID;
        } catch (Exception e) {

            return "android-device-id-error";
        }

    }

}
