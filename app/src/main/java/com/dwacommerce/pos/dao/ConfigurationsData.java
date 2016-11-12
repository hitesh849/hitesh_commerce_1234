package com.dwacommerce.pos.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 8/11/2016.
 */

public class ConfigurationsData implements Serializable {
    public String responseMessage;
    public String productStoreId;
    public ArrayList<Terminals> posTerminals;
    public String serverUrl;
    public ProductStoreData productStoreInfo;
}
