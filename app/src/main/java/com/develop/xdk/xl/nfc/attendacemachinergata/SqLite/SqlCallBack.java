package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite;

public interface SqlCallBack<T> {
    void onRespose(T obj);
    void onError(String msg);
}
