package com.hsq.myview;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private Map<String, List<ImageBean.ImageDetailBean>> datas = new HashMap<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MainAdapter adapter = (MainAdapter) mRecyclerView.getAdapter();
                adapter.setData(getData());
                mProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private Thread thread;
    private Cursor cursor;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, null);
            if (cursor == null) {
                return;
            }
            while (cursor.moveToNext()) {
//            图片名字
                Log.e("imageName", cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)));

                byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                double size = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));

                String imagePath = new String(data, 0, data.length - 1);
                String fileParentPath = new File(imagePath).getParentFile().getAbsolutePath();

                ImageBean.ImageDetailBean detailBean = new ImageBean.ImageDetailBean();
                detailBean.imageName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                detailBean.imagePath = imagePath;
                detailBean.imageSize = new DecimalFormat("0.00").format(size / 1024 / 1024) + "MB";

                if (!datas.containsKey(fileParentPath)) {
//                如果没有该KEY
                    List<ImageBean.ImageDetailBean> valueList = new ArrayList<>();
                    valueList.add(detailBean);
                    datas.put(fileParentPath, valueList);
                } else {
                    datas.get(fileParentPath).add(detailBean);
                }

//            图片路径/storage/emulated/0/
                Log.e("path:", imagePath);
//            图片父级名字
                Log.e("fileParentName", new File(imagePath).getParentFile().getName());
//            图片父级路径
                Log.e("fileParentPath", fileParentPath);
//            图片标题
                Log.e("title", cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)));
//            图片大小
                Log.e("size:", new DecimalFormat("0.00").format(size / 1024 / 1024) + "MB");
            }
            //通知Handler扫描图片完成
            handler.sendEmptyMessage(1);
            cursor.close();
            
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.main_rv);
        mProgressBar = findViewById(R.id.main_pb);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        MainAdapter adapter = new MainAdapter(new ArrayList<ImageBean>(), this);
        mRecyclerView.setAdapter(adapter);


        thread = new Thread(runnable);
        thread.start();


    }


    public List<ImageBean> getData() {
        List<ImageBean> imageBeans = new ArrayList<>();
        Set<Map.Entry<String, List<ImageBean.ImageDetailBean>>> entries = datas.entrySet();
        Iterator<Map.Entry<String, List<ImageBean.ImageDetailBean>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<ImageBean.ImageDetailBean>> next = iterator.next();
            ImageBean imageBean = new ImageBean();
            imageBean.imageParentName = next.getKey().replace("/storage/emulated/0/", "");
            imageBean.parentFileSize = next.getValue().size();
            imageBean.topImage = next.getValue().get(0).imagePath;
            imageBean.imageList = next.getValue();
            imageBeans.add(imageBean);
        }

        if (imageBeans.size() > 0) {
            return imageBeans;
        }
        return null;
    }
}
