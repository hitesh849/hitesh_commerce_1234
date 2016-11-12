package com.dwacommerce.pos.dao;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 8/12/2016.
 */

public class ShoppingCartData {
    public String orderType;
    public String channel;
    public long billingAccountAmt;
    public long nextItemSeq;
    public boolean viewCartOnAdd;
    public boolean readOnlyCart;
    public ArrayList adjustments;
    public boolean orderTermSet;
    public ArrayList orderTerms;
    public ArrayList<CartLineData> cartLines;
    public JSONObject itemGroupByNumberMap;
    public long nextGroupNumber;
    public ArrayList<PaymentInfoData> paymentInfo;
    public ArrayList shipInfo;
    public JSONObject contactMechIdsMap;
    public JSONObject orderAttributes;
    public JSONObject attributes;
    public ArrayList internalOrderNotes;
    public ArrayList orderNotes;
    public JSONObject additionalPartyRole;
    public ArrayList productPromoUseInfoList;
    public ArrayList productPromoCodes;
    public ArrayList freeShippingProductPromoActions;
    public JSONObject desiredAlternateGiftByAction;
    public String cartCreatedTs;
    public String delegatorName;
    public boolean doPromotions;
    public String transactionId;
    public String terminalId;
    public String locale;
    public String currencyUom;
    public boolean holdOrder;
    public long totalItemSubTotal;
    public long totalPromotions;
    public long totalSalesTax;
    public long totalShipping;
    public long posTotal;
}
