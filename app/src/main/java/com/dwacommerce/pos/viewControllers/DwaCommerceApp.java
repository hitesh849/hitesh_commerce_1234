package com.dwacommerce.pos.viewControllers;

import android.support.multidex.MultiDexApplication;

import com.dwacommerce.pos.database.DatabaseMgr;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.init.Env;

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
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_BACKGROUND || level == TRIM_MEMORY_UI_HIDDEN) {
            Env.APP_STATE = Env.applicationState.BACKGROUND;

        }
    }
}
