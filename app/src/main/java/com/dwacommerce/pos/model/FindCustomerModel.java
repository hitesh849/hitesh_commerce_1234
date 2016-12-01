package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.PartyData;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 8/23/2016.
 */
public class FindCustomerModel extends BasicModel {
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.getServerUrl()).build();
    RestInterface restInterface = restAdapter.create(RestInterface.class);

    public void searchParty(String searchType, String searchString) {
        restInterface.findParty(new HashMap<String, Object>(), searchType, searchString, Config.getCustomerId(), new Callback<PartyData>() {
            @Override
            public void success(PartyData partyData, Response response) {
                notifyObservers(partyData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void setPartyToCart(String partyId, String contactMatchPurposeTypeId, String contactMetchId) {
        restInterface.setPartyToCartRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), partyId, contactMatchPurposeTypeId, contactMetchId, Config.getSessionId(), Config.getCustomerId(), new Callback<CommonResponseData>() {
            @Override
            public void success(CommonResponseData commonResponseData, Response response) {
                notifyObservers(commonResponseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
