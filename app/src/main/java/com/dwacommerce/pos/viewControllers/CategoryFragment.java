package com.dwacommerce.pos.viewControllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.adapters.CategoryProductsAdapter;
import com.dwacommerce.pos.adapters.ChildCategoryAdapter;
import com.dwacommerce.pos.adapters.ParentCategoryAdapter;
import com.dwacommerce.pos.dao.CategoryData;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.ProductAttributesData;
import com.dwacommerce.pos.dao.ProductData;
import com.dwacommerce.pos.database.DatabaseMgr;
import com.dwacommerce.pos.model.CategoryFragmentModel;
import com.dwacommerce.pos.utility.Constants;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.Observable;

import retrofit.RetrofitError;

/**
 * Created by admin on 14/09/16.
 */
public class CategoryFragment extends AbstractFragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private CategoryFragmentModel categoryFragmentModel = new CategoryFragmentModel();
    private View view;
    private ArrayList<CategoryData> parentCategoryList;
    private ArrayList<CategoryData> childCategoryList;
    private ArrayList<ProductData> productsList;

    private RecyclerView parentCategoryView;
    private RecyclerView childCategoryView;
    private GridView productView;
    private TabLayout tabLayoutCategoryFragment;
    private ChildCategoryAdapter childCategoryAdapter;
    private CategoryProductsAdapter categoryProductsAdapter;
    private LinearLayout lastParentSelectedCategory;
    private LinearLayout lastChildSelectedCategory;
    private RelativeLayout lastProductSelected;
    private View childCategoryHeaderView;
    private View productItemHeaderView;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_fragment, container);
        init();
        return view;
    }

    private void init() {
        tabLayoutCategoryFragment = ((TabLayout) view.findViewById(R.id.tabLayoutCategoryFragment));
        parentCategoryView = (RecyclerView) view.findViewById(R.id.parentCategoryView);
        childCategoryView = (RecyclerView) view.findViewById(R.id.childCategoryView);
        productView = (GridView) view.findViewById(R.id.productView);
        childCategoryHeaderView = (View) view.findViewById(R.id.childCategoryHeaderView);
        productItemHeaderView = (View) view.findViewById(R.id.productItemHeaderView);
        tabLayoutCategoryFragment.setOnTabSelectedListener(this);
        categoryFragmentModel.getTabsCategoryData();
    }

    @Override
    protected BasicModel getModel() {
        return categoryFragmentModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        Util.dimissProDialog();
        if (o instanceof ArrayList) {
            ArrayList<CategoryData> list = ((ArrayList) o);
            for (CategoryData categoryData : list) {
                tabLayoutCategoryFragment.addTab(tabLayoutCategoryFragment.newTab().setText(categoryData.name).setTag(categoryData));
            }
        } else if (o instanceof ProductData) {
            ProductData productData = ((ProductData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(productData.responseMessage)) {
                if (productData.results.products != null && !productData.results.products.isEmpty()) {
                    productsList = productData.results.products;
                    setProductsAdapter();
                }
            }
        } else if (o instanceof ProductAttributesData) {
            ProductAttributesData productAttributesData = ((ProductAttributesData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(productAttributesData.response)) {
                if ("N".equals(productAttributesData.isVirtual)) {
                    Util.showProDialog(Env.currentActivity);
                    categoryFragmentModel.addItemToCart(productAttributesData.productId, "1");
                } else if ("Y".equals(productAttributesData.isVirtual)) {
                    ((DashBordActivity) Env.currentActivity).showProductVariantDialog(productAttributesData);
                }
            }
        } else if (o instanceof CommonResponseData) {
            CommonResponseData commonResponseData = ((CommonResponseData) o);
            if (Constants.RESPONSE_SUCCESS_MSG.equals(commonResponseData.response)) {
                ((DashBordActivity) Env.currentActivity).fetchCartData();
            }
        } else if (o instanceof RetrofitError) {
            Util.showAlertDialog(null, Constants.DEFAULT_SERVER_ERROR);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        try {
            CategoryData categoryData = ((CategoryData) tab.getTag());
            parentCategoryList = DatabaseMgr.getInstance(Env.currentActivity).getCategoryById(categoryData.id);

            if (parentCategoryList != null && parentCategoryList.size() > 0) {
                ParentCategoryAdapter parentCategoryAdapter = new ParentCategoryAdapter(Env.currentActivity, parentCategoryList, this);
                parentCategoryView.setAdapter(parentCategoryAdapter);
            } else {
                getProductsByCategoryRequest(categoryData);
            }

            lastParentSelectedCategory = null;
            lastChildSelectedCategory = null;
            childCategoryHeaderView.setVisibility(View.GONE);
            productItemHeaderView.setVisibility(View.GONE);
            lastProductSelected = null;
            if (categoryProductsAdapter != null) {
                productsList.clear();
                categoryProductsAdapter.notifyDataSetChanged();
            }
            if (childCategoryAdapter != null) {
                childCategoryList.clear();
                childCategoryAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        try {
            int vid = view.getId();
            if (vid == R.id.imgCategory) {
                CategoryData parentCategoryData = ((CategoryData) view.getTag());
                setChildCategoryAdapter(parentCategoryData);
                if (lastParentSelectedCategory != null) {
                    lastParentSelectedCategory.setSelected(false);
                }
                lastParentSelectedCategory = ((LinearLayout) view.getTag(vid));
                lastParentSelectedCategory.setSelected(true);
            } else if (vid == R.id.imgChildCategory) {
                CategoryData categoryData = ((CategoryData) view.getTag());
                getProductsByCategoryRequest(categoryData);
                if (lastChildSelectedCategory != null) {
                    lastChildSelectedCategory.setSelected(false);
                }
                lastChildSelectedCategory = ((LinearLayout) view.getTag(vid));
                lastChildSelectedCategory.setSelected(true);
            } else if (vid == R.id.imgProduct) {
                ProductData productData = ((ProductData) view.getTag());
                if (lastProductSelected != null) {
                    lastProductSelected.setSelected(false);
                }
                if (Util.isDeviceOnline()) {
                    Util.showProDialog(Env.currentActivity);
                    categoryFragmentModel.getProductType(productData.productId);
                    // categoryFragmentModel.getProductType("DWA122116");
                }
                lastProductSelected = ((RelativeLayout) view.getTag(vid));
                lastProductSelected.setSelected(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getProductsByCategoryRequest(CategoryData categoryData) {
        if (Util.isDeviceOnline()) {
            Util.showProDialog(Env.currentActivity);
            categoryFragmentModel.getProductsByCategoryRequest(categoryData.id + "");
        } else {
            Util.showAlertDialog(null, Constants.INTERNET_ERROR_MSG);
        }
    }

    private void setChildCategoryAdapter(CategoryData categoryData) {
        try {
            childCategoryList = DatabaseMgr.getInstance(Env.currentActivity).getCategoryById(categoryData.id);

            if (childCategoryList != null && childCategoryList.size() > 0) {
                childCategoryAdapter = new ChildCategoryAdapter(Env.currentActivity, childCategoryList, this);
                childCategoryView.setAdapter(childCategoryAdapter);
            } else {
                getProductsByCategoryRequest(categoryData);
            }
            lastChildSelectedCategory = null;
            lastProductSelected = null;
            childCategoryHeaderView.setVisibility(View.VISIBLE);
            productItemHeaderView.setVisibility(View.GONE);
            if (categoryProductsAdapter != null) {
                productsList.clear();
                categoryProductsAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProductsAdapter() {
        try {
            productItemHeaderView.setVisibility(View.VISIBLE);
            categoryProductsAdapter = new CategoryProductsAdapter(Env.currentActivity, productsList, this);
            productView.setAdapter(categoryProductsAdapter);
            lastProductSelected = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
