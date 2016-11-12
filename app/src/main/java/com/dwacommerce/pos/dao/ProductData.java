package com.dwacommerce.pos.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 8/12/2016.
 */

public class ProductData implements Serializable, CharSequence {
    public String responseMessage;
    public ProductData results;
    public ArrayList<ProductData> productsList;
    public ArrayList<ProductData> products;
    public String productName;
    public String productId;
    public String internalName;
    public String brandName;
    public String description;
    public double listPrice;
    public double defaultPrice;
    public String inventory;
    public String requireInventory;
    public String productImage;

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int i) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
        return null;
    }

    @Override
    public String toString() {
        return productName;
    }
}
