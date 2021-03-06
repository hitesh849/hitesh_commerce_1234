package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 21-08-2016.
 */

public class UpdateCartItemModel extends BasicModel {
    RestInterface restInterface = RestClient.getRestInterface();

    public void updateCartItem(String quantity, String cartLineIndex, String price) {
        restInterface.updateCartItem(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), cartLineIndex, quantity, price, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CommonResponseData>() {
            @Override
            public void success(CommonResponseData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
