package com.dwacommerce.pos.dao;

import java.util.ArrayList;

/**
 * Created by admin on 8/26/2016.
 */
public class PaymentInfoData {
    public String paymentMethodTypeId;
    public ArrayList refNum;
    public double amount;
    public boolean singleUse;
    public boolean isPresent;
    public boolean isSwiped;
    public boolean overflow;
}
