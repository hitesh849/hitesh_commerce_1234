package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.ConfigurationsData;
import com.dwacommerce.pos.dao.LoginData;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 07-08-2016.
 */

public class LogInModel extends BasicModel {

    RestInterface restInterface = RestClient.getRestInterface();
    public void doLogin(String userName, String password, String posTerminalId, String productStoreId) {
        try {

            restInterface.loginRequest(new HashMap<String, Object>(), userName, password, posTerminalId, productStoreId, Config.getCustomerId(), new Callback<LoginData>() {
                @Override
                public void success(LoginData obj, Response response) {
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

    public void saveSettings(String productStoreId) {
        try {
            restInterface.saveConfigRequest(new HashMap<String, Object>(), productStoreId, Config.getCustomerId(), new Callback<ConfigurationsData>() {
                @Override
                public void success(ConfigurationsData obj, Response response) {
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
