package com.endpoint.ghair.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.preferences.Preferences;


public class App extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, Preferences.getInstance().getLanguage(newBase)));

    }

}

