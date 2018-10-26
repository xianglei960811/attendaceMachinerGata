package com.develop.xdk.xl.nfc.attendacemachinergata.ExceptionHandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.develop.xdk.xl.nfc.attendacemachinergata.Application.App;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: XL
 * Date: 2018-10-22 17:28
 * Describe:定义全局异常处理器(未完成)
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionHandler INSTANCE = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context context;
    private Map<String, String> mMessage = new HashMap<>();

    private ExceptionHandler() {
    }

    public static ExceptionHandler getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (ExceptionHandler.class) {
                if (INSTANCE == null) {
                    synchronized (ExceptionHandler.class) {
                        INSTANCE = new ExceptionHandler();
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化默认异常捕获
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
        //获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将此类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e)&&mDefaultHandler != null ){
//未处理的让系统默认异常处理器处理
            mDefaultHandler.uncaughtException(t,e);
        }else{
            //拦截到异常后的处理
        }
    }

    /**
     * 是否认为捕获异常
     *
     * @param e
     * @return true:已处理；false：未处理
     */
    private boolean handleException(Throwable e) {
        if (e == null) {
            //异常是否为空
            return false;
        }
        getCrashInfo(e);
        return true;
    }

    /**
     * 收集错误信息
     *
     * @param e
     */
    private void getCrashInfo(Throwable e) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String errorMessage = writer.toString();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String mFilePath = Environment.getExternalStorageDirectory() + "/" + C.ERROR_FILENAME;
//            FileTxt
        }
    }
}
