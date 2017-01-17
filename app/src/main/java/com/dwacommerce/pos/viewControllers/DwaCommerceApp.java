package com.dwacommerce.pos.viewControllers;

import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.database.DatabaseMgr;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.init.Env;

import java.util.Locale;

/**
 * Created by admin on 07-08-2016.
 */

public class DwaCommerceApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Env.appContext = this;
        Config.init(this);
        DatabaseMgr.getInstance(this);
        String language = Config.getLanguageSelected().equals(getResources().getString(R.string.hindi)) ? "hi" : "en";
        setLanguage(language);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String language = Config.getLanguageSelected().equals(getResources().getString(R.string.hindi)) ? "hi" : "en";
        setLanguage(language);
    }

    public void setLanguage(String locale) {
        Locale mLocale = new Locale(locale);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_BACKGROUND || level == TRIM_MEMORY_UI_HIDDEN) {
            Env.APP_STATE = Env.applicationState.BACKGROUND;

        }
    }
}
