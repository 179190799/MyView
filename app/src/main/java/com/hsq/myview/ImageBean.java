package com.hsq.myview;

import java.io.Serializable;
import java.util.List;

/**
 * Created by THINK on 2018/4/28.
 */

public class ImageBean implements Serializable {
    /**
     * 图片父类文件名
     */
    public String imageParentName;
    /**
     * 第一张图片
     */
    public String topImage;
    /**
     * 该文件的图片数
     */
    public int parentFileSize;

    /**
     * 该父类文件下的所有图片
     */
    public List<ImageDetailBean> imageList;

    public static class ImageDetailBean implements Serializable{
        /**
         * 图片大小
         */
        public String imageSize;
        /**
         * 图片路径
         */
        public String imagePath;
        /**
         * 图片名字
         */
        public String imageName;


    }

}
