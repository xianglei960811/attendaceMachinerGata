package com.develop.xdk.xl.nfc.attendacemachinergata.http.subscribers;

import android.content.Context;
import android.os.Message;
import android.util.Log;


import com.develop.xdk.xl.nfc.attendacemachinergata.Activity.AttendanceInActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.progress.ProgressCancelListener;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.progress.ProgressDialogHandler;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liukun on 16/3/10.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    ToastUntil toastUntil;
    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        toastUntil = new ToastUntil(context);
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        String message = null;
        if (e instanceof SocketTimeoutException) {
//            toastUntil.ShowToastShort("网络中断，请检查您的网络状态");
            message = "网络中断，请检查您的网络状态";
//            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
//            toastUntil.ShowToastShort("网络中断，请检查您的网络状态");
//            message = "网络中断，请检查您的网络状态";
        } else if (e instanceof UnknownHostException) {
//            toastUntil.ShowToastShort("网络中断，请检查您的网络状态");
            message = "网络中断，请检查您的网络状态";
        } else {
            Log.e("subcriber", "onError: ." + e.getMessage());
            message = e.getMessage();
//            toastUntil.ShowToastShort(e.getMessage());
        }
//        if (context.getClass().getSimpleName().equals("AttendanceInActivity")) {
//            AttendanceInActivity activity = (AttendanceInActivity) context;
//            Message msg = new Message();
//            msg.what = 100;
//            msg.obj = message;
//            activity.mainHandler.sendMessage(msg);
//        } else {
//            toastUntil.ShowToastShort(message);
//        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(message);
        }
        dismissProgressDialog();

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}