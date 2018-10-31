package com.develop.xdk.xl.nfc.attendacemachinergata.http.subscribers;

/**
 * Created by liukun on 16/3/10.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);

    void onError(String msg);//错误信息接口
}
