package com.xxii.stan.xxii_nt;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.health.UidHealthStats;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import android.os.Process;

import android.os.Environment;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        verifyStoragePermissions(this);
        ListView userInstalledApps = (ListView)findViewById(R.id.installed_app_list);

        List<AppList> installedApps = getInstalledApps();
        AppAdapter installedAppAdapter = new AppAdapter(MainActivity.this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);


        final Button btNtCache = (Button) findViewById(R.id.btNtCache);
        btNtCache.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                killAppBackground();
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
/* Définition des permissions pour les systèmes plus récents qu'android 6.0
 * Pour les versions antérieures il faut aussi les définir dans AndroiManifest.xml */

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int KILL_BACKGROUND_PROCESSES = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.DELETE_CACHE_FILES,
            Manifest.permission.CLEAR_APP_CACHE,
            Manifest.permission.KILL_BACKGROUND_PROCESSES,
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
/* Test de récupération des processus en arrière plan */

    private void killAppBackground() {
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo runningProcess : runningProcesses) {
                Log.v("Hello", "kill process "+runningProcess.pid);
                //android.os.Process.killProcess(runningProcess.pid);
                am.killBackgroundProcesses(runningProcess.toString());
        }
    }

/*Fonctionne et liste les app installées */
    private List<AppList> getInstalledApps() {
        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);

            if (!isSystemPackage(p)) {

                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppList(appName, icon));
            }
        }
        return res;
    }

/*Flag à mettre à 0 pour supprimer les app système */
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1);
    }

/*Fonction kill process (fonctionne mais ne kill que notre app) */

    public void appProcess () {

        ActivityManager actvityManager = (ActivityManager)
                getApplicationContext().getSystemService( getApplicationContext().ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();

        for(int pnum = 0; pnum < procInfos.size(); pnum++)
        {
            if((procInfos.get(pnum)).processName.contains("android")||(procInfos.get(pnum)).processName.contains("system")||(procInfos.get(pnum)).processName.contains("huawei")||(procInfos.get(pnum)).processName.contains("adil"))
            {
                Toast.makeText(getApplicationContext(), "system apps", Toast.LENGTH_SHORT).show();
            }
            else
            {
                actvityManager.killBackgroundProcesses(procInfos.get(pnum).processName);
                Toast.makeText(getApplicationContext(), "killed "+procInfos.get(pnum).processName, Toast.LENGTH_SHORT).show();

            }
        }

    }
/* Fonction de suppression des dossiers fichiers ok mais pas encore le cache */
    public static void xxii_nt(View view) {

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String chemin = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.txt";
        File path = new File(chemin);
        boolean isDirectoryCreated = path.exists();


        if (!isDirectoryCreated) {
            Snackbar.make(view, path.getAbsolutePath(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //deleteDirectory(path);

            try {
                path.delete();
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }
}