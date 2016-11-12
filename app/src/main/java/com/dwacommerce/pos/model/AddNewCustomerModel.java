package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.CountryData;
import com.dwacommerce.pos.dao.StateData;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 8/24/2016.
 */
public class AddNewCustomerModel extends BasicModel {
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.getServerUrl()).build();
    RestInterface restInterface = restAdapter.create(RestInterface.class);

    public void getCountryList() {
        restInterface.getCountryList(new HashMap<String, Object>(), Config.getCustomerId(),new Callback<CountryData>() {
            @Override
            public void success(CountryData countryData, Response response) {
                try {
                    if (Constants.RESPONSE_SUCCESS_MSG.equals(countryData.response)) {
                        Config.setCountryList(countryData.countryList.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Util.dimissProDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void createNewParty(String firstName, String lastName, String email, String address, String city, String postalCode, String countryGeoId, String stateProvinceGeoId) {
        restInterface.createPartyRequest(new HashMap<String, Object>(), firstName, lastName, email, address, city, postalCode, countryGeoId, stateProvinceGeoId, Config.getCustomerId(),new Callback<CommonResponseData>() {
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

    public void getStateList(String geoId) {
        restInterface.getStateListRequest(new HashMap<String, Object>(), geoId, Config.getCustomerId(),new Callback<StateData>() {
            @Override
            public void success(StateData stateData, Response response) {
                notifyObservers(stateData);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
