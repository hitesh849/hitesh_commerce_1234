package com.dwacommerce.pos.dao;

import java.io.Serializable;

/**
 * Created by admin on 8/12/2016.
 */

public class CartItemsData implements Serializable {
    public String response;
    public String responseMessage;
    public String response_message;
    public double totalItemSubTotal;
    public double totalSalesTax;
    public double totalVatTaxIncluded;
    public long totalShipping;
    public double posTotal;
    public long loyaltyPointsRedeemed;
    public double promotionAmount;
    public double totalDue;
    public ShoppingCartData shoppingCart;
}
