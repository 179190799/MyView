package com.hsq.myview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by THINK on 2018/4/28.
 */

public class MainAdapter extends RecyclerView.Adapter {

    private List<ImageBean> data;

    private Context mContext;

    public MainAdapter(List<ImageBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_lis, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder viewHolder = (ImageViewHolder) holder;
        final ImageBean imageBean = data.get(position);
//        if (data != null) {
//
//            int width = viewHolder.mImageView.getWidth();
////        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
//            int height = (int) (width * 1.2f);
//            Log.e("width", " " + width);
//            Log.e("height", " " + height);
//            viewHolder.mImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        }
        viewHolder.mImageView.setImageTotalTxt(imageBean.parentFileSize + "");
        viewHolder.mImageView.setImageBitmap(BitmapFactory.decodeFile(imageBean.topImage));
        viewHolder.mTitleView.setText(imageBean.imageParentName);
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.actionStart(mContext, imageBean.imageList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ImageBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private MyAlbumView mImageView;
        private TextView mTitleView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.item_title);
            mImageView = itemView.findViewById(R.id.item_image);
        }
    }


}
