package com.dwacommerce.pos.dao;

import java.io.Serializable;

/**
 * Created by Admin on 10/19/2016.
 */
public class ReceiptItemsData implements Serializable {
    public String orderId;
    public String createdStamp;
    public String itemDescription;
    public double selectedAmount;
    public String orderItemSeqId;
    public double unitPrice;
    public String productId;
    public String createdTxStamp;
    public String lastUpdatedTxStamp;
    public String statusId;
    public String isPromo;
    public String isModifiedPrice;
    public double unitListPrice;
    public String orderItemTypeId;
    public String lastUpdatedStamp;
    public double quantity;
    public String changeByUserLoginId;
    public String prodCatalogId;
}
