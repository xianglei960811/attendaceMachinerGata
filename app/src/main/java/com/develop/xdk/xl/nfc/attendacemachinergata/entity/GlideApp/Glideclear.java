package com.develop.xdk.xl.nfc.attendacemachinergata.entity.GlideApp;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Image_byte.GlideApp;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

/**
 * Author: XL
 * Date: 2018-10-31 14:32
 * Describe:清除glide缓存
 */
public class Glideclear {
    private static Glideclear INSTANCE;

    public static Glideclear getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Glideclear();
        }
        return INSTANCE;
    }

    /**
     * 清除图片磁盘缓存
     *
     * @param context
     */
    private void clearImageDisjCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GlideApp.get(context).clearDiskCache();
                    }
                }).start();
            }

        } catch (Exception e) {
            Log.e("Glideclear", "clearImageDisjCache: " + e.getMessage());
        }
    }

    /**
     * 清除图片内存缓存
     *
     * @param context
     */
    private void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                GlideApp.get(context).clearMemory();
            }
        } catch (Exception e) {
            Log.e("Glideclear", "clearImageMemoryCache: " + e.getMessage());
        }
    }

    /**
     * 清除所有缓存
     *
     * @param context
     */
    public void clearImageAllCache(Context context) {
        clearImageDisjCache(context);
        clearImageMemoryCache(context);
        ToastUntil toastUntil = new ToastUntil(context);
        toastUntil.ShowToastShort("图片缓存清除成功");
    }

}
