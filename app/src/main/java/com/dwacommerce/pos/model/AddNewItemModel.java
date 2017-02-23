package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.ProductData;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.google.gson.JsonElement;

import org.byteclues.lib.model.BasicModel;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 8/19/2016.
 */

public class AddNewItemModel extends BasicModel {
    RestInterface restInterface = RestClient.getRestInterface();

    public void findProducts(String searchText) {
        restInterface.findProductRequest(new HashMap<String, Object>(), searchText, Config.getCustomerId(),new Callback<ProductData>() {
            @Override
            public void success(ProductData productData, Response response) {
                notifyObservers(productData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void addItemToCart(String addProductId, String quantity) {
        restInterface.addCartItemRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getProductStoreId(), addProductId, quantity, Config.getSessionId(), Config.getPosTerminalId(),Config.getCustomerId(), new Callback<CommonResponseData>() {
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
