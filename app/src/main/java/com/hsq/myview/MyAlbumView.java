package com.hsq.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by THINK on 2018/4/26.
 */

@SuppressLint("AppCompatCustomView")
public class MyAlbumView extends ImageView {
    private String imageTotalTxt = "0";
    /**
     * 默认字体大小为18sp
     */
    private float imageSize = 18;

    public String getImageTotalTxt() {
        return imageTotalTxt;
    }

    public void setImageTotalTxt(String imageTotalTxt) {
        this.imageTotalTxt = imageTotalTxt;
        this.invalidate();
    }

    public float getImageSize() {
        return imageSize;
    }

    public void setImageSize(float imageSize) {
        this.imageSize = imageSize;
    }

    public MyAlbumView(Context context) {
        this(context, null);
    }

    public MyAlbumView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MyAlbumView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 获取自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
//        //获取自定义属性。
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
//        //获取字体大小,默认大小是16dp
//        fontSize = (int) ta.getDimension(R.styleable.CustomView_font, 16);
//        //获取文字内容
//        customText = ta.getString(R.styleable.CustomView_text);
//        //获取文字颜色，默认颜色是BLUE
//        customColor = ta.getColor(R.styleable.CustomView_color, Color.BLUE);
//        ta.recycle();
//        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        textPaint.setColor(customColor);
//        textPaint.setTextSize(fontSize);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAlbumView);

//        获取图片总数
        imageTotalTxt = typedArray.getString(R.styleable.MyAlbumView_image_total_text);
        imageSize = typedArray.getFloat(R.styleable.MyAlbumView_image_total_font_sise, 18);

        typedArray.recycle();
    }

    //圆角的半径
    // 3x3 矩阵，主要用于缩小放大
    private Matrix mMatrix;
    // 渲染图像，使用图像为绘制图形着色
    private BitmapShader mBitmapShader;
    //矩形
    private RectF mRoundRect;

    //自定义View实现过程中很重要的onDraw绘制图形的方法
    @SuppressLint("DrawAllocation")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {

//        super.onDraw(canvas);
//        设置阴影必须
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色
        paint.setShadowLayer(5f, 15f, 15f, Color.GRAY);
//                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
        //设置抗锯齿，如果不设置，加载位图的时候可能会出现锯齿状的边界，如果设置，边界就会变的稍微有点模糊，锯齿就看不到了。
        paint.setAntiAlias(true);
        //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
        paint.setDither(true);
        //        画出向右偏转5度的两个矩形
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(0.8f, 0.8f);
        canvas.rotate(8);
        canvas.drawRoundRect(new RectF(-getWidth() / 2 - 0.7f, -getHeight() / 2 - 0.7f, getWidth() / 2 + 0.7f, getHeight() / 2 + 07f), 15, 15, paint);

//        画出向左偏转5度的两个矩形
        paint.setColor(Color.WHITE);
        canvas.rotate(-8);
        canvas.drawRoundRect(new RectF(-getWidth() / 2 - 0.7f, -getHeight() / 2 - 0.7f, getWidth() / 2 + 0.7f, getHeight() / 2 + 0.7f), 15, 15, paint);
//        paint.setXfermode(null);


        Bitmap bmp = getBitmap();

        if (null == bmp) {
            return;
        }
        //构造渲染器BitmapShader
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        float scale = 1.0f;
//        float scaleY = 1.0f;

        if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.min(getWidth() * 1.0f / bmp.getWidth(),
                    getHeight() * 1.0f / bmp.getHeight());
//            scaleX = getWidth() * 1.0f / bmp.getWidth();
//            scaleY =  getHeight() * 1.0f / bmp.getHeight();
        }

        // shader的变换矩阵，我们这里主要用于放大或者缩小
        // scale * scale 的矩阵
        mMatrix = new Matrix();
        mMatrix.setScale(scale, scale, 0, 0);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        paint.setShader(mBitmapShader);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);


        //绘制矩形
        mRoundRect = new RectF(-getWidth() / 2 + 0.7f, -getHeight() / 2 + 0.7f, getWidth() / 2 + 0.7f, getHeight() / 2 + 0.7f);
        canvas.scale(0.8f, 0.8f);
//        canvas.rotate(-10);
        //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色
        paint.setShadowLayer(0, 0, 0, 0);
        canvas.drawRect(mRoundRect, paint);


        Paint textPaint = new Paint();

        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL);


        int textX = getWidth() / 2;
        int textY = getHeight() / 2;

//        canvas.scale(1.2f, 1.2f);
        RectF textViewRF = new RectF(-textX * 3 / 4 + 0.7f, textY - textX / 4 + 0.7f, textX * 3 / 4 + 0.7f, textY + textX / 2 + 0.7f);
        canvas.drawRoundRect(textViewRF, 30, 30, textPaint);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(sp2px(getContext(), imageSize));
//        15为画圆角矩形时的圆半径
        canvas.drawText(imageTotalTxt, textViewRF.centerX() - 15, textViewRF.centerY() + 15, textPaint);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取SRC的背景图
     *
     * @return
     */
    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();

        //空值判断，必要步骤，避免由于没有设置src导致的异常错误
        if (drawable == null) {
            return null;
        }

        //必要步骤，避免由于初始化之前导致的异常错误
        if (getWidth() == 0 || getHeight() == 0) {
            return null;
        }

        if (!(drawable instanceof BitmapDrawable)) {
            return null;
        }
        return ((BitmapDrawable) drawable).getBitmap();
    }

//    public void setImageSize(String imageSizeStr) {
//        this.imageSizeStr = imageSizeStr;
//        invalidate();
//    }


}
