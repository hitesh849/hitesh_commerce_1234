package com.dwacommerce.pos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.ProductData;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.utils.Util;

import java.util.ArrayList;

/**
 * Created by admin on 9/17/2016.
 */
public class CategoryProductsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProductData> productDatas;
    private View.OnClickListener onClickListener;

    public CategoryProductsAdapter(Context context, ArrayList<ProductData> productDatas, View.OnClickListener onClickListener) {
        this.context = context;
        this.productDatas = productDatas;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return productDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return productDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = (convertView == null) ? new ViewHolder(convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_data_row, parent, false)) : (ViewHolder) convertView.getTag();
        ProductData productData = productDatas.get(position);
        viewHolder.imgProduct.setTag(productData);
        viewHolder.imgProduct.setTag(viewHolder.imgProduct.getId(), viewHolder.rlProductItemContainer);
        viewHolder.txtProductName.setText(productData.productName);
        Util.loadImage(Env.currentActivity, viewHolder.imgProduct, Config.getBaseUrl()+productData.productImage, R.mipmap.ic_launcher);
        return convertView;
    }

    private class ViewHolder {
        public TextView txtProductName;
        public ImageView imgProduct;
        public RelativeLayout rlProductItemContainer;

        public ViewHolder(View view) {
            txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
            rlProductItemContainer = (RelativeLayout) view.findViewById(R.id.rlProductItemContainer);
            imgProduct.setOnClickListener(onClickListener);
            view.setTag(this);
        }
    }
}
