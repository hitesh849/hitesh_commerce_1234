package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.CustomerBillingAccountInfoData;
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
public class CreditAmountCustomerModel extends BasicModel {
    RestInterface restInterface = RestClient.getRestInterface();

    public void searchParty(String searchType, String searchString) {
        restInterface.findParty(new HashMap<String, Object>(),Config.getSessionId(),Config.getPosTerminalId(), searchType, searchString, Config.getCustomerId(), new Callback<PartyData>() {
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

    public void setBillingAccountPayment(String partyId, String amount) {
        restInterface.billingAccountPayment(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), partyId, amount, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CommonResponseData>() {
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

    public void getBillingAccountPayments(String partyId) {
        restInterface.getBillingAccountPayments(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), partyId, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CustomerBillingAccountInfoData>() {
            @Override
            public void success(CustomerBillingAccountInfoData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
