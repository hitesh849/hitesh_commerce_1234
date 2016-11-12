package com.dwacommerce.pos.dao;

import java.io.Serializable;

/**
 * Created by Admin on 10/19/2016.
 */
public class ReceiptHeaderData implements Serializable {
    public String invoicePerShipment;
    public String salesChannelEnumId;
    public String orderId;
    public String createdStamp;
    public String orderTypeId;
    public String terminalId;
    public String productStoreId;
    public double remainingSubTotal;
    public String lastUpdatedStamp;
    public String entryDate;
    public double grandTotal;
    public String createdTxStamp;
    public String lastUpdatedTxStamp;
    public String priority;
    public String transactionId;
    public String originFacilityId;
    public String statusId;
    public String createdBy;
    public String currencyUom;
    public String needsInventoryIssuance;
    public String orderDate;
}
