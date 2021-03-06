package com.develop.xdk.xl.nfc.attendacemachinergata.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2018/6/11.
 */

public class GsonRequestBodyConverter<T>  implements Converter<T, RequestBody> {
    private final Gson gson;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json;charset=UTF-8");

    GsonRequestBodyConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        Log.d("RequestBody", new Gson().toJson(value));
        return  RequestBody.create(MEDIA_TYPE, gson.toJson(value));
    }
}
