package com.develop.xdk.xl.nfc.attendacemachinergata.entity.ReSizeDrawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.entity.getScreenSize.ScreenSizeUtils;

public class ReSizeDrawable {

    /***
     *
     *   静态类，用于对图片尺寸进行放大或缩写
     * @param context
     * @param drawable
     * @return
     */
    public static BitmapDrawable reSize(Context context, BitmapDrawable drawable) {
        int mScreenWidth = ScreenSizeUtils.getInstance(context).getScreenWidth();
        Bitmap bitmap = drawable.getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float num = ((float) 105) / (float)bitmapWidth;
        float num1 = ((float)180)/(float) bitmapHeight;
        if (num > 1) {//屏幕的1/2比图片大，图片进行放大
            float numb = ((float) bitmapWidth /  ((float) 105)) + 1.0f;
            Matrix matrix = new Matrix();
            matrix.postScale(numb, numb);
            Log.d("ReSize", "reSize:is big--------------------> ");
            Bitmap reSizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            BitmapDrawable reSizeDrawble = new BitmapDrawable(context.getResources(), reSizeBitmap);
            return reSizeDrawble;
        } else if (num < 1) {//图片比屏幕的1/2大，图片进行缩写
            Matrix matrix = new Matrix();
            matrix.postScale(num, num1);
            Log.d("ReSize", "reSize:is small--------------------> ");
//            Log.e("sss", "reSize: "+mScreenWidth+":"+bitmapWidth+":"+bitmapHeight+":"+matrix.toString() );
            Bitmap reSizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            BitmapDrawable reSizeDrawable = new BitmapDrawable(context.getResources(), reSizeBitmap);
            return reSizeDrawable;
        } else {
            Log.d("ReSize", "reSize:is same--------------------> ");
            return drawable;
        }

    }
}
