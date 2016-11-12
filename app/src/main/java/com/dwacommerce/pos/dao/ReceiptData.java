package com.dwacommerce.pos.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 10/19/2016.
 */
public class ReceiptData implements Serializable {
    public String response;
    public ReceiptData order_details;
    public String currencyUomId;
    public ReceiptHeaderData orderHeader;
    public ArrayList<OrderAdjustmentsData> orderAdjustments;
    public ArrayList<ReceiptItemsData> orderItems;
    public double orderSubTotal;
    public String customerName = "Not Available";
    public double VAT_TAX;
    public double SALES_TAX;
    public double LOYALTY_POINTS_AMOUNT;
    public double PROMOTION_AMOUNT;
}
