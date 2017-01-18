package com.xxii.stan.xxii_nt;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TestPermissions.verifyStoragePermissions(this);
        TestPermissions.verifyProcessPermissions(this);
        TestPermissions.verifyCachePermissions(this);


        final Button btNtCache = (Button) findViewById(R.id.btNtCache);
        final Button btNtProcess = (Button) findViewById(R.id.btNtProcess);
        btNtCache.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cacheCleaner(v);
            }

        });
        btNtProcess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processCleaner(v);
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

/* Récupération des applications installées + nettoyage du dossier cache de chacune ROOT */

    private void cacheCleaner(View v) {

        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {

            PackageInfo p = packs.get(i);
            if (!isSystemPackage(p)) {
                String nomProcess = p.applicationInfo.processName;
                Context myCt = null;

                try {
                    myCt = createPackageContext(nomProcess, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                File cacheDir = myCt != null ? myCt.getCacheDir() : null;
                File extCacheDir = myCt.getExternalCacheDir();
                if (cacheDir.exists()) {

                    try {
                        Runtime.getRuntime().exec(new String[]{
                                "su", "-c", "rm -R " + cacheDir
                        });
                        Snackbar.make(v, "Votre cache à été nettoyé avec succes ;)", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (Exception ignored) {

                    }
                }
                if (extCacheDir != null && extCacheDir.exists()) {

                    try {
                        Runtime.getRuntime().exec(new String[]{
                                "su", "-c", "rm -R " + extCacheDir
                        });
                        //deleteDir(new File(appDir, s));
                        Snackbar.make(v, "Votre cache externe à été nettoyé avec succès", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (Exception ignored) {

                    }
                }
            }
        }

    }

    private void processCleaner(View v) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            if (packageInfo.packageName.equals("com.xxii.stan.xxii_nt")) continue;
            try {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);


                Snackbar.make(v, "Le tueur de taches à terminé son travail", Snackbar.LENGTH_LONG);

            } catch (Exception e) {
                Snackbar.make(v, "Erreur de nettoyage processus", Snackbar.LENGTH_LONG);

            }
        }
    }

    /*Flag à mettre à 0 pour supprimer les app système du résultat*/
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}