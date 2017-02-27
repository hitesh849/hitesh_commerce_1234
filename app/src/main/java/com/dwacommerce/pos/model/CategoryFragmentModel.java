package com.dwacommerce.pos.model;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.CategoryData;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.ProductAttributesData;
import com.dwacommerce.pos.dao.ProductData;
import com.dwacommerce.pos.database.DatabaseMgr;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.google.gson.JsonElement;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 14/09/16.
 */
public class CategoryFragmentModel extends BasicModel {
    RestInterface restInterface = RestClient.getRestInterface();

    public void getTabsCategoryData() {
        if (DatabaseMgr.getInstance(Env.currentActivity).getNoOfRecords(CategoryData.TABLE_NAME) == 0) {
            getAllCategories(null);
        } else {
            notifyObservers(DatabaseMgr.getInstance(Env.currentActivity).getCategoryById(null));
        }
    }

    private void getAllCategories(final String parentCategoryId) {
        restInterface.getCategoriesRequest(new HashMap<String, Object>(), Config.getCustomerId(),new Callback<CategoryData>() {
            @Override
            public void success(CategoryData categoryData, Response response) {
                if (categoryData != null && Constants.RESPONSE_SUCCESS_MSG.equals(categoryData.response)) {
                    DatabaseMgr.getInstance(Env.currentActivity).insertDataToCategoryTable(categoryData.categories);
                    notifyObservers(DatabaseMgr.getInstance(Env.currentActivity).getCategoryById(parentCategoryId));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getProductsByCategoryRequest(String categoryId) {


        restInterface.getProductsByCategoryRequest(new HashMap<String, Object>(), categoryId, Config.getSessionId(), Config.getCustomerId(),new Callback<ProductData>() {
            @Override
            public void success(ProductData productData, Response response) {
                notifyObservers(productData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getProductType(final String productId) {
        restInterface.getProductTypeRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), productId, Config.getCustomerId(),new Callback<ProductAttributesData>() {
            @Override
            public void success(ProductAttributesData productAttributesData, Response response) {
                productAttributesData.productId = productId;
                notifyObservers(productAttributesData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void addItemToCart(String addProductId, String quantity) {
        restInterface.addCartItemRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getProductStoreId(), addProductId, quantity, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(),new Callback<CommonResponseData>() {
            @Override
            public void success(CommonResponseData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
