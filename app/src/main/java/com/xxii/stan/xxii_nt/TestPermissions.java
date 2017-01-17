package com.xxii.stan.xxii_nt;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by stan on 15/01/2017.
 */

public class TestPermissions extends MainActivity {
    /* Définition des permissions pour les systèmes plus récents qu'android 6.0
 * Pour les versions antérieures il faut aussi les définir dans AndroiManifest.xml */

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_BACKGROUND_PROCESSES = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    /* Vérification des permissions et demande utilisateur si besoin */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE

            );
        }
    }

    private static String[] PERMISSIONS_PROCESS = {
            Manifest.permission.KILL_BACKGROUND_PROCESSES
    };
    /* Vérification des permissions et demande utilisateur si besoin */
    public static void verifyProcessPermissions(Activity activity) {
        // Check if we have write permission
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.KILL_BACKGROUND_PROCESSES);
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_PROCESS,
                    REQUEST_BACKGROUND_PROCESSES

            );
        }
    }

    private static String[] PERMISSIONS_CACHE = {
            Manifest.permission.DELETE_CACHE_FILES,
            Manifest.permission.CLEAR_APP_CACHE
    };
    /* Vérification des permissions et demande utilisateur si besoin */
    public static void verifyCachePermissions(Activity activity) {
        // Check if we have write permission
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CLEAR_APP_CACHE);
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CACHE,
                    REQUEST_BACKGROUND_PROCESSES

            );
        }
    }
}
