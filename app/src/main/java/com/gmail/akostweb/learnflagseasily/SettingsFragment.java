package com.gmail.akostweb.learnflagseasily;

import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Created by Администратор on 15.09.2016.
 */
public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);// load from XML
    }


}
