package com.dwacommerce.pos.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 09-08-2016.
 */

public class UserData implements Serializable {
    public ArrayList<ProductStores> productStores;
    public String jsessionid;
    public UserData response;
    public String responseMessage;
    public String errorMessage;
    public UserData userLogin;
    public String enabled;
    public String lastUpdatedStamp;
    public String userLoginId;
    public String currentPassword;
    public String createdTxStamp;
    public String createdStamp;
    public String partyId;
    public String lastUpdatedTxStamp;
}
