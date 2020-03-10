package com.endpoint.ghair.share;


import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.preferences.Preferences;


public class App extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, Preferences.getInstance().getLanguage(newBase)));    }
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(this, "SERIF", "fonts/GE-SS-Two-Bold.otf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(this, "DEFAULT", "fonts/GE-SS-Two-Bold.otf");
        TypefaceUtil.overrideFont(this, "MONOSPACE", "fonts/GE-SS-Two-Bold.otf");
        TypefaceUtil.overrideFont(this, "SANS_SERIF", "fonts/GE-SS-Two-Bold.otf");
    }
}

