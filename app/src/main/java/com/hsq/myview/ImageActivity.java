package com.hsq.myview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by THINK on 2018/4/28.
 */

public class ImageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ImageBean.ImageDetailBean> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        recyclerView = findViewById(R.id.image_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(new ImageAdapter(new ArrayList<ImageBean.ImageDetailBean>(), this));

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            datas = (List<ImageBean.ImageDetailBean>) bundle.getSerializable("data");
            if (datas!=null) {
                ImageAdapter  adapter = (ImageAdapter) recyclerView.getAdapter();
                adapter.setData(datas);
            }
        }
    }

    public static void actionStart(Context mContext, List<ImageBean.ImageDetailBean> imageList) {
        Intent intent = new Intent(mContext, ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", (Serializable) imageList);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
//        intent.putStringArrayListExtra("data", imageList);
    }
}
