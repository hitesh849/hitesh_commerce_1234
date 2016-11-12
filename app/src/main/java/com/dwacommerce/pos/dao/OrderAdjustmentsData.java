package com.dwacommerce.pos.dao;

import java.io.Serializable;

/**
 * Created by Admin on 11/7/2016.
 */
public class OrderAdjustmentsData implements Serializable {
    public String orderId;
    public String createdStamp;
    public String taxAuthPartyId;
    public String primaryGeoId;
    public String taxAuthGeoId;
    public String createdByUserLogin;
    public String orderAdjustmentId;
    public double amountAlreadyIncluded;
    public String orderItemSeqId;
    public String lastUpdatedStamp;
    public double amount;
    public String comments;
    public String createdTxStamp;
    public String orderAdjustmentTypeId;
    public String taxAuthorityRateSeqId;
    public String overrideGlAccountId;
    public String lastUpdatedTxStamp;
    public String shipGroupSeqId;
    public String createdDate;
    public double sourcePercentage;
}
