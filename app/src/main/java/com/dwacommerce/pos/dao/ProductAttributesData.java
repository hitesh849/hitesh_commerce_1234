package com.dwacommerce.pos.dao;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 9/20/2016.
 */
public class ProductAttributesData extends CommonResponseData {
    public String isVirtual;
    public int variantTreeSize;
    public HashMap featureTypes;
    public String productId;
    public HashMap variantTree;
    public ArrayList<String> variantSampleList;
    public ArrayList<String> featureOrder;
}
