package com.hsq.myview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by THINK on 2018/4/28.
 */

public class ImageAdapter extends RecyclerView.Adapter {

    private List<ImageBean.ImageDetailBean> data;

    private Context mContext;

    public ImageAdapter(List<ImageBean.ImageDetailBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_image_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        ImageBean.ImageDetailBean detailBean = data.get(position);
        viewHolder.mImageView.setImageBitmap(BitmapFactory.decodeFile(detailBean.imagePath));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ImageBean.ImageDetailBean> datas) {
        this.data.addAll(datas);
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image_2);
        }
    }


}
