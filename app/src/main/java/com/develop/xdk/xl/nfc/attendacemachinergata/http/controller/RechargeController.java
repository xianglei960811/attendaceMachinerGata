package com.develop.xdk.xl.nfc.attendacemachinergata.http.controller;

import android.content.Context;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.MyService.myService;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseParam;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.CheckRecodParam;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.ComsumeParam;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.HttpResult;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.PersonDossier;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.ApiException;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.HttpMethods;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.service.NfcService;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.subscribers.ProgressSubscriber;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.subscribers.SubscriberOnNextListener;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.BeanUtil;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.SharedPreferencesUtils;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.SignUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.SortedMap;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2018/6/11.
 */

public class RechargeController {
    private Retrofit retrofit;
    private NfcService NfcService;

    private RechargeController() {
        retrofit = HttpMethods.getInstance().getRetrofit();
        NfcService = retrofit.create(NfcService.class);
    }




    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final RechargeController INSTANCE = new RechargeController();
    }

    //获取单例
    public static RechargeController getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 获取用户信息
     *
     * @param onNextListener
     * @param context
     * @param cardid
     */
    public void getUserinfo(SubscriberOnNextListener onNextListener, Context context, String cardid) throws ApiException{
        ProgressSubscriber subscriber = new ProgressSubscriber(onNextListener, context);
        BaseParam param = new BaseParam();
        param.setCardid(cardid);
        param.setClientid((String) SharedPreferencesUtils.getParam(context, C.CLIENTID_NAME, C.CLIENTID));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        SortedMap map = BeanUtil.ClassToMap(param);
        param.setSign(SignUtil.createSign(map, C.SIGN_KEY));
        Observable observable = NfcService.getUserInfo(param)
                .map(new HttpResultFunc<PersonDossier>());
        toSubscribe(observable, subscriber);
        Log.d("getUserinfo", new Gson().toJson(param));
    }

    /**
     * 接收人员档案
     * @param onNextListener
     * @param context
     */
    public void recepDossier(SubscriberOnNextListener onNextListener,Context context) {
        ProgressSubscriber subscriber = new ProgressSubscriber(onNextListener, context);
        ComsumeParam param = new ComsumeParam();
        param.setComputer((String) SharedPreferencesUtils.getParam(context, C.COMPUTER_NAME, C.COMPUTER));
        param.setClientid((String) SharedPreferencesUtils.getParam(context, C.CLIENTID_NAME, C.CLIENTID));
        param.setWindowNumber((String) SharedPreferencesUtils.getParam(context, C.MACHINE_NAME, C.MACHINE));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        SortedMap map = BeanUtil.ClassToMap(param);
        param.setSign(SignUtil.createSign(map, C.SIGN_KEY));
        Observable observable = NfcService.recepDossior(param).map(new HttpResultFunc<List<PersonDossier>>());
        toSubscribe(observable, subscriber);
        Log.d("recepDossier","------------>"+ new Gson().toJson(param));
    }

    /**
     * 上传考勤记录
     * @param a_cardID 卡号
     * @param a_attendMode 考勤模式
     * @param a_inOrOutMode 进出放心
     * @param at_data
     * @param subscriberOnNextListener 回调接口
     * @param context
     */
    public void updataAttends(String a_cardID, int a_attendMode, int a_inOrOutMode,String at_data, SubscriberOnNextListener subscriberOnNextListener, Context context) {
        ProgressSubscriber subscriber = new ProgressSubscriber(subscriberOnNextListener,context);
        String[] data = at_data.split("\\ ");
        String riqi = data[0];
        String time = data[1];
        CheckRecodParam param = new CheckRecodParam();
        param.setCheckmac((String) SharedPreferencesUtils.getParam(context,C.CHECK_MAC_NAME,C.CHECK_MAC));
        param.setChecktype(String.valueOf(a_attendMode));
        param.setDate(riqi);
        param.setTime(time);
        param.setMactype(String.valueOf(C.MAC_TYPR));
        param.setStyle(String.valueOf(a_inOrOutMode));
        param.setCardid(a_cardID);
        param.setClientid((String) SharedPreferencesUtils.getParam(context,C.CLIENTID_NAME,C.CLIENTID));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        SortedMap map = BeanUtil.ClassToMap(param);
        param.setSign(SignUtil.createSign(map,C.SIGN_KEY));
        Observable observable = NfcService.updataAttends(param).map(new HttpResultFunc<PersonDossier>());
        toSubscribe(observable,subscriber);
        Log.d("updataAttends","------------>"+ new Gson().toJson(param));
    }
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            Log.e("result", new Gson().toJson(httpResult));
            if (httpResult.getCode() == 0) {
                throw new ApiException(httpResult.getMsg());
            }
            return httpResult.getData();
        }
    }

}
