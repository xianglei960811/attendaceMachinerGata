package com.develop.xdk.xl.nfc.attendacemachinergata.http.service;


import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseParam;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.CheckRecodParam;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.ComsumeParam;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.HttpResult;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.PersonDossier;


import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * nfc接口
 */
public interface NfcService {
    //获取用户信息
    @POST("user/info")
    Observable<HttpResult<PersonDossier>> getUserInfo(@Body BaseParam param);

    //下载档案
    @POST("user/info/all")
    Observable<HttpResult<List<PersonDossier>>> recepDossior(@Body ComsumeParam param);

    //kaoqing
    @POST("/user/checkRecode")
    Observable<HttpResult<PersonDossier>> updataAttends(@Body CheckRecodParam param);

}
