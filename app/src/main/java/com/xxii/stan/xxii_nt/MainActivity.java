package com.xxii.stan.xxii_nt;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TestPermissions.verifyStoragePermissions(this);
        TestPermissions.verifyProcessPermissions(this);
        ListView userInstalledApps = (ListView)findViewById(R.id.installed_app_list);

        List<AppList> installedApps = getInstalledApps();
        AppAdapter installedAppAdapter = new AppAdapter(MainActivity.this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);


        final Button btNtCache = (Button) findViewById(R.id.btNtCache);
        btNtCache.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CacheCleaner();
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

/* Test de récupération des processus en arrière plan */

    private void CacheCleaner() {


        List<AppList> res = new ArrayList<AppList>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {

            PackageInfo p = packs.get(i);
            String test = p.applicationInfo.processName;
            String dir = p.applicationInfo.dataDir.toString()+ "/cache";
            String nom = p.applicationInfo.loadLabel(getPackageManager()).toString();
            File path = new File(dir.toString());
            if (!isSystemPackage(p)) {
                if (dir != "/data/user/0/com.xxii.stan.xxii_nt/cache"){
                    DeleteRecursive(path);
                }

            }

        }
        /*ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo runningProcess : runningProcesses) {
                Log.v("Hello", "kill process "+runningProcess.pid);
                //android.os.Process.killProcess(runningProcess.pid);
                am.killBackgroundProcesses(runningProcess.toString());
        }*/
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
                String appCacheDir = p.applicationInfo.dataDir.toString()+ "/cache";
                res.add(new AppList(appName, appCacheDir, icon));
            }
        }
        return res;
    }

/*Flag à mettre à 0 pour supprimer les app système du résultat*/
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
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

    void DeleteRecursive(File path) {

        if (path.isDirectory())
            for (File child : path.listFiles())
                DeleteRecursive(child);

        try {
            path.delete();
            throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

/* Fonction de suppression des dossiers fichiers ok mais pas encore le cache */
    public void xxii_nt() {

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String chemin = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache";
        File path = new File(chemin);
        boolean isDirectoryCreated = path.exists();


        if (isDirectoryCreated) {
            //Snackbar.make(view, path.getAbsolutePath(), Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();
            //deleteDirectory(path);

            DeleteRecursive(path);

        }

    }
}