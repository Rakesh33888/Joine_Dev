package com.brahmasys.bts.joinme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by apple on 14/09/16.
 */
public class Marshmallow_Permissions {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE=2;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private static String[] PERMISSIONS_STORAGE1 = {
            Manifest.permission.READ_PHONE_STATE};

    public static void Calender_Permissions(Activity activity)
    {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR);
        final int callbackId = 42;


        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,PERMISSIONS_CALENDER,callbackId);
        }
    }

    private static String[] PERMISSIONS_CALENDER = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user

            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);


            //ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        }

    }

    public static void checkPermission(Activity activity) {
        // Here, thisActivity is the current activity

        int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_PHONE_STATE);
      //  int readPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_PHONE_STATE);

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user

            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE1,MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);


            //ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        }
    }



}



