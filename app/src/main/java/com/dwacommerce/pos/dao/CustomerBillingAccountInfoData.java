package com.dwacommerce.pos.dao;

import java.util.ArrayList;

/**
 * Created by Admin on 2/20/2017.
 */
public class CustomerBillingAccountInfoData extends CommonResponseData {
    public BillingAccountInfo billingAccountInfo;
    public ArrayList<PaymentStatementData> payments;

    public class BillingAccountInfo {
        public double accountLimit;
        public double accountBalance;
        public String billingAccountId;
    }
}
