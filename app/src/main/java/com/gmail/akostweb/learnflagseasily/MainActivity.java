package com.gmail.akostweb.learnflagseasily;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends Activity {

    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";

    private boolean phoneDevice = true;
    private boolean preferencesChanged = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false;

        if (phoneDevice)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (preferencesChanged) {
            LearnFlagsEasilyFragment learnFlagsEasilyFragment = (LearnFlagsEasilyFragment) getFragmentManager().
                    findFragmentById(R.id.learnFlagsEasilyFragment);
            learnFlagsEasilyFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            learnFlagsEasilyFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));
            learnFlagsEasilyFragment.resetQuiz();
            preferencesChanged = false;

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Point screenSize = new Point();
        display.getRealSize(screenSize);

        if (screenSize.x < screenSize.y) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true;
                    LearnFlagsEasilyFragment learnFlagsEasilyFragment = (LearnFlagsEasilyFragment)
                            getFragmentManager().findFragmentById(R.id.learnFlagsEasilyFragment);
                    if (key.equals(CHOICES)) {
                        learnFlagsEasilyFragment.updateGuessRows(sharedPreferences);
                        learnFlagsEasilyFragment.resetQuiz();
                    } else if (key.equals(REGIONS)) {
                        Set<String> regions = sharedPreferences.getStringSet(REGIONS, null);
                        if (regions != null && regions.size() > 0) {
                            learnFlagsEasilyFragment.updateRegions(sharedPreferences);
                            learnFlagsEasilyFragment.resetQuiz();
                        } else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            regions.add(getResources().getString(R.string.default_region));
                            editor.putStringSet(REGIONS, regions);
                            editor.commit();
                            Toast.makeText(MainActivity.this, R.string.default_region_message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
                }
            };
}
