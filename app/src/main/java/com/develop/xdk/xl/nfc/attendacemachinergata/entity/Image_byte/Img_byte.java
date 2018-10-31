package com.develop.xdk.xl.nfc.attendacemachinergata.entity.Image_byte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.develop.xdk.xl.nfc.attendacemachinergata.R;

import java.io.ByteArrayOutputStream;

public class Img_byte {
    /***
     * 将drable 目录下的图片转换层二进制
     * @param context
     * @param draw 图片地址
     * @return
     */
    public static byte[] imageTobyte(Context context,int draw){
//        Resources resources = context.getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.example);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Drawable drawable  = ContextCompat.getDrawable(context,draw);
        Bitmap bitmap =((BitmapDrawable)drawable).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

    public static Drawable byteToDrawable(Context context,byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(),bitmap);
        return drawable;
    }


}
