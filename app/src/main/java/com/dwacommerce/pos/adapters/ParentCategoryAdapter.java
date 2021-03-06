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
 * Created by Admin on 16/09/16.
 */
public class ParentCategoryAdapter extends RecyclerView.Adapter<ParentCategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoryData> categoryDatas;
    private View.OnClickListener onClickListener;

    public ParentCategoryAdapter(Context context, ArrayList<CategoryData> categoryDatas, View.OnClickListener onClickListener) {
        this.context = context;
        this.categoryDatas = categoryDatas;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_row, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryData categoryData = categoryDatas.get(position);
        holder.txtParentCategoryName.setText(categoryData.name);
        holder.imgCategory.setTag(categoryData);
        holder.imgCategory.setTag(holder.imgCategory.getId(), holder.llParentCategoryItemContainer);
        Util.loadImage(context, holder.imgCategory, Config.getBaseUrl() + categoryData.imageUrl, R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return categoryDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgCategory;
        protected TextView txtParentCategoryName;
        public LinearLayout llParentCategoryItemContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            imgCategory.setOnClickListener(onClickListener);
            txtParentCategoryName = (TextView) itemView.findViewById(R.id.txtParentCategoryName);
            llParentCategoryItemContainer = (LinearLayout) itemView.findViewById(R.id.llParentCategoryItemContainer);
        }
    }
}
