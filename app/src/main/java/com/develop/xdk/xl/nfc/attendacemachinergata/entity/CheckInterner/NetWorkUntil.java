package com.develop.xdk.xl.nfc.attendacemachinergata.entity.CheckInterner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Author: XL
 * Date: 2018-10-23 11:33
 * Describe:获取网络状态
 */
public class NetWorkUntil {

    /**
     * 检测联网状态
     * @param context
     * @return
     */
    public static final  Boolean is_connction(Context context){
        Boolean flag = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo()!=null){
            flag = connectivityManager.getActiveNetworkInfo().isConnected();
        }
        Log.i("is_connction", "is_connction: ----------->"+flag);
        return flag;

    }
}
