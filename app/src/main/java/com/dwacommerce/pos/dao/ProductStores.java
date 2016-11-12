package com.dwacommerce.pos.dao;

/**
 * Created by admin on 8/10/2016.
 */

public class ProductStores implements CharSequence {
    public String inventoryFacilityId;
    public String storeName;
    public String productStoreId;

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public String toString() {
        return storeName;
    }
}
