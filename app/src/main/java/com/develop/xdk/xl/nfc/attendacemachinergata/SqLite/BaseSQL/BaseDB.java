package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.Loading.timeOutListner;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.LoadingDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

import java.util.concurrent.atomic.AtomicInteger;


/***
 * 数据库的基础类
 */
public class BaseDB {
    private static MySqliteHelp dbHeilp;
    private SQLiteDatabase db;
    //数据可打开关闭标记器，可解决重复打开关闭数据库的问题
    protected AtomicInteger mOpenCounter = new AtomicInteger();

    protected BaseDB(Context context) {
        if (dbHeilp == null) {
            synchronized (BaseDB.class) {
                if (dbHeilp == null) {
                    //将SQLCipher所依赖的so库加载进来
//                    SQLiteDatabase.loadLibs(context);
                    dbHeilp = new MySqliteHelp(context);
                }
            }
        }
    }

    /**
     * 获得一个可写的数据库
     *
     * @return
     */
    protected synchronized SQLiteDatabase getWritableDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
//            db = dbHeilp.getWritableDatabase(C.PASS);
            db = dbHeilp.getWritableDatabase();
        }
        return db;
    }

    /**
     * 获得一个可读的数据库
     *
     * @return
     */
    protected synchronized SQLiteDatabase getReadableDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
//            db = dbHeilp.getReadableDatabase(C.PASS);
            db = dbHeilp.getWritableDatabase();
        }
        return db;
    }

    protected synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            db.close();
        }
    }

}
