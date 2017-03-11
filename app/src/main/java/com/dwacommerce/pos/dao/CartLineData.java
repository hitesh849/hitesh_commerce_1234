package com.dwacommerce.pos.dao;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by admin on 8/13/2016.
 */

public class CartLineData implements Serializable {
    public String delegatorName;
    public String prodCatalogId;
    public String productId;
    public String productName;
    public String itemType;
    public long reservLength;
    public long reservPersons;
    public double quantity;
    public double basePrice;
    public double displayPrice;
    public double reserv2ndPPPerc;
    public double reservNthPPPerc;
    public double listPrice;
    public boolean isModifiedPrice;
    public long selectedAmount;
    public String orderItemAssocTypeId;
    public JSONObject attributes;
    public String locale;
    public JSONObject contactMechIdsMap;
    public String[] orderItemPriceInfos;
    public String[] itemAdjustments;
    public boolean isPromo;
    public long promoQuantityUsed;
    public JSONObject quantityUsedPerPromoCandidate;
    public JSONObject quantityUsedPerPromoFailed;
    public JSONObject quantityUsedPerPromoActual;
    public JSONObject additionalProductFeatureAndAppls;
    public String[] featuresForSupplier;
    public int cartLineIndex;
    public String orderDecimalQuantity;
}
