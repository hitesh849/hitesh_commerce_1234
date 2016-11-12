package com.dwacommerce.pos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.dao.CategoryData;
import com.dwacommerce.pos.sharedPreferences.Config;

import org.byteclues.lib.utils.Util;

import java.util.ArrayList;

/**
 * Created by user on 16/09/16.
 */
public class ChildCategoryAdapter extends RecyclerView.Adapter<ChildCategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoryData> categoryDatas;
    private View.OnClickListener onClickListener;

    public ChildCategoryAdapter(Context context, ArrayList<CategoryData> categoryDatas, View.OnClickListener onClickListener) {
        this.context = context;
        this.categoryDatas = categoryDatas;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.child_category_row, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryData categoryData = categoryDatas.get(position);
        holder.txtChildCategoryName.setText(categoryData.name);
        holder.imgChildCategory.setTag(categoryData);
        holder.imgChildCategory.setTag(holder.imgChildCategory.getId(), holder.llChildCategoryContainer);
        Util.loadImage(context, holder.imgChildCategory, Config.getBaseUrl() + categoryData.imageUrl, R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return categoryDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtChildCategoryName;
        protected ImageView imgChildCategory;
        public LinearLayout llChildCategoryContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            imgChildCategory = (ImageView) itemView.findViewById(R.id.imgChildCategory);
            txtChildCategoryName = (TextView) itemView.findViewById(R.id.txtChildCategoryName);
            llChildCategoryContainer = (LinearLayout) itemView.findViewById(R.id.llChildCategoryContainer);
            imgChildCategory.setOnClickListener(onClickListener);
        }
    }
}
