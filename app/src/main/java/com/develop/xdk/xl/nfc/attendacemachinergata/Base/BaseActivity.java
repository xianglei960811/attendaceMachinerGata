package com.develop.xdk.xl.nfc.attendacemachinergata.Base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.develop.xdk.xl.nfc.attendacemachinergata.Application.App;
import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.NFC.getNfcCardID;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SQLControl;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.CheckInterner.NetWorkUntil;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.SharedPreferencesUtils;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/2/8.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected ToastUntil toastUntil;
    protected volatile getNfcCardID getcardId;
    protected volatile SQLControl sqlControl;
    protected volatile String NfcCardID;

    protected volatile int attend_mode;
    protected volatile String TAG = this.getClass().getSimpleName();
    protected volatile int in_out_mode ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toastUntil = new ToastUntil(this);
        getcardId = new getNfcCardID(this);
        sqlControl = new SQLControl(this);
        attend_mode = (int) SharedPreferencesUtils.getParam(this, C.ATTENDANCE_MODE_NAME, C.SCHOOL_GATA_MODE);
        in_out_mode = (int) SharedPreferencesUtils.getParam(this,C.IN_OUT_MODE_NAME,C.IN_MODE);
        Log.d(TAG, "onCreate: attend_mode" + attend_mode);
//        Log.e("Base", "onCreate: "+App.getInstance().getIS_FIRST());
        overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
        setContentView();
        ButterKnife.bind(this);
        initView();
        App.getInstance().addActivity(this);

    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    protected abstract void initView();

    protected abstract void setContentView();


    public void toActivityWithFinish(Class<?> toclass) {
        startActivity(new Intent(this, toclass));
        App.getInstance().removeActivity(this);
    }

    public void toActivity(Class<?> toclass) {
        startActivity(new Intent(this, toclass));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            App.getInstance().removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toastUntil != null) {
            toastUntil.stopToast();
            Log.i(TAG, "onDestroy: stopToast");
        }
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
        super.finish();
    }
}
