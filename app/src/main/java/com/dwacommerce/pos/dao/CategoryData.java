package com.dwacommerce.pos.dao;

import java.util.ArrayList;

/**
 * Created by admin on 8/13/2016.
 */

public class CategoryData {
    public static final String TABLE_NAME = "categories";


    public static final String PARENT_CATEGORY_ID = "parentCategoryId";
    public static final String FLD_CATEGORY_NAME = "name";
    public static final String FLD_CATEGORY_ID = "id";
    public static final String FLD_CATEGORY_IMAGE_URL = "imageUrl";


    public String responseMessage;
    public String response;
    public ArrayList<CategoryData> categories;
    public ArrayList<CategoryData> subCategories;
    public String name;
    public String id;
    public String parentCategoryId;
    public String imageUrl;
}
