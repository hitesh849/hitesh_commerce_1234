package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.UserData;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.squareup.okhttp.OkHttpClient;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by admin on 8/10/2016.
 */

public class SettingsModel extends BasicModel {


    public void fetchSettings(String baseUrl, String userName, String password) {
        try {
            RestInterface restInterface = RestClient.getRestInterfaceObject(baseUrl);
            restInterface.settingRest(new HashMap<String, Object>(), userName, password, Config.getCustomerId(), new Callback<UserData>() {
                @Override
                public void success(UserData obj, Response response) {
                    notifyObservers(obj);
                }

                @Override
                public void failure(RetrofitError error) {
                    notifyObservers(error);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

