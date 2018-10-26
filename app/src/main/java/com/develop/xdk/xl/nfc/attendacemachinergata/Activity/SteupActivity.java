package com.develop.xdk.xl.nfc.attendacemachinergata.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.develop.xdk.xl.nfc.attendacemachinergata.Application.App;
import com.develop.xdk.xl.nfc.attendacemachinergata.Base.BaseActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.MainActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.getScreenSize.ScreenSizeUtils;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class SteupActivity extends BaseActivity {
    @BindView(R.id.set_up_machine)
    EditText setUpMachine;
    @BindView(R.id.set_up_computer)
    EditText setUpComputer;
    @BindView(R.id.set_up_address)
    EditText setUpAddress;
    @BindView(R.id.set_up_clientid)
    EditText setUpClientid;
    @BindView(R.id.set_up_password)
    EditText setUpPassword;
    @BindView(R.id.set_up_save)
    Button setUpSave;
    @BindView(R.id.set_up_back)
    Button setUpBack;
    @BindView(R.id.atten_steup_back)
    ImageButton attenSteupBack;
    @BindView(R.id.setup_cont)
    LinearLayout setCont;
    @BindView(R.id.set_up_sp)
    Spinner setUpSp;
    @BindView(R.id.set_up_sp_inOrout)
    Spinner setUpSpInorOut;
    @BindView(R.id.set_up_check_mac)
    EditText setUpCheckMac;//设备名称

    private ArrayAdapter adapter;
    private ArrayAdapter inAdapter;

    private Boolean isFirst = true;

    private volatile int attend_mode = 0;
    private volatile int in_mode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdpater();
    }

    //适配器
    private void initAdpater() {
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getData());
        setUpSp.setAdapter(adapter);
        setUpSp.setSelection(attend_mode, true);

        inAdapter = new ArrayAdapter(this,R.layout.spinner_item,getInData());
        setUpSpInorOut.setAdapter(inAdapter);
        setUpSpInorOut.setSelection(in_mode - 1, true);
    }

    private List<String> getInData() {
        List<String> list = new ArrayList<>();
        list.add(C.IN_MODE_NAME);
        list.add(C.OUT_MODE_NAME);
        return list;
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add(C.SCHOOL_GATA);
        list.add(C.DOOR);
        list.add(C.LESSON_OR_EXAMINE);
//        list.add(C.TEACHING_STAFF);
        return list;
    }


    @Override
    protected void initView() {
        setUpAddress.setText((String) SharedPreferencesUtils.getParam(this, C.BASE_URL_NAME, C.BASE_URL));
        setUpClientid.setText((String) SharedPreferencesUtils.getParam(this, C.CLIENTID_NAME, C.CLIENTID));
        setUpComputer.setText((String) SharedPreferencesUtils.getParam(this, C.COMPUTER_NAME, C.COMPUTER));
        setUpMachine.setText((String) SharedPreferencesUtils.getParam(this, C.MACHINE_NAME, C.MACHINE));
        setUpPassword.setText((String) SharedPreferencesUtils.getParam(this, C.PASS_NAME, C.PASS));
        attend_mode = (int) SharedPreferencesUtils.getParam(this, C.ATTENDANCE_MODE_NAME, attend_mode);
        in_mode = (int) SharedPreferencesUtils.getParam(this, C.IN_OUT_MODE_NAME, C.IN_MODE);
        setUpCheckMac.setText((String) SharedPreferencesUtils.getParam(this, C.CHECK_MAC_NAME, C.CHECK_MAC));
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_steup);
    }

    @OnClick(R.id.atten_steup_back)
    public void onBackClick() {
        toActivityWithFinish(MainActivity.class);
    }

    @OnClick(R.id.set_up_back)
    public void backClick() {
        toActivityWithFinish(MainActivity.class);
    }

    @OnClick(R.id.set_up_save)
    public void saveDate() {
        if (TextUtils.isEmpty(setUpComputer.getText().toString())) {
            toastUntil.ShowToastShort("电脑号不能为空");
            return;
        }
        if (TextUtils.isEmpty(setUpClientid.getText().toString())) {
            toastUntil.ShowToastShort("单位代码不能为空");
            return;
        }
        if (TextUtils.isEmpty(setUpAddress.getText().toString())) {
            toastUntil.ShowToastShort("请求地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(setUpMachine.getText().toString())) {
            toastUntil.ShowToastShort("机器号不能为空");
            return;
        }
        if (TextUtils.isEmpty(setUpCheckMac.getText().toString())) {
            toastUntil.ShowToastShort("设备名称不能为空");
            return;
        }
//        if (TextUtils.isEmpty(setUpPassword.getText().toString())) {
//            toastUntil.ShowToastShort("密码不能为空");
//            return;
//        }
        SharedPreferencesUtils.setParam(this, C.COMPUTER_NAME, setUpComputer.getText().toString());
        SharedPreferencesUtils.setParam(this, C.CLIENTID_NAME, setUpClientid.getText().toString());
        SharedPreferencesUtils.setParam(this, C.BASE_URL_NAME, setUpAddress.getText().toString());
        SharedPreferencesUtils.setParam(this, C.MACHINE_NAME, setUpMachine.getText().toString());
//        SharedPreferencesUtils.setParam(this, C.PASS_NAME, setUpPassword.getText().toString().trim());
        SharedPreferencesUtils.setParam(this, C.ATTENDANCE_MODE_NAME, attend_mode);
        SharedPreferencesUtils.setParam(this, C.CHECK_MAC_NAME, setUpCheckMac.getText().toString());
        SharedPreferencesUtils.setParam(this, C.IN_OUT_MODE_NAME, in_mode);
        App.getInstance().exitApp();
    }

    /**
     * 操作验证Dialog显示
     **/
//    private void ShowDialog() {
//        dialog = new Dialog(this, R.style.NormalDialogStyle);
//        View view = View.inflate(this, R.layout.dialog_seting, null);
//        Button cancel = (Button) view.findViewById(R.id.setup_bt_cancel);
//        Button ensure = (Button) view.findViewById(R.id.setup_bt_ensure);
//        final EditText password = (EditText) view.findViewById(R.id.setup_et_pass);
//        dialog.setContentView(view);
//        dialog.setCanceledOnTouchOutside(false);//点击对话框外部，不消失对话框
//        //设置对话框大小
//        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.75f);
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//        dialogWindow.setAttributes(lp);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                toActivity(MainActivity.class);
//                dialog.dismiss();
//                App.getInstance().removeActivity(SteupActivity.this);
//            }
//        });
//        ensure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkPassWord(password.getText().toString().trim());
//            }
//        });
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keycode, KeyEvent event) {
//                if (keycode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
////                    toActivity(MainActivity.class);
////                    dialog.dismiss();
//                    App.getInstance().removeActivity(SteupActivity.this);
//                }
//                return false;
//            }
//        });
//        dialog.show();
//    }
    /**
     * 验证密码
     **/
//    private void checkPassWord(String pass) {
//        if (TextUtils.isEmpty(pass)) {
//            toastUntil.ShowToastShort("密码不能为空");
//            return;
//        }
//        if (pass.equals(SharedPreferencesUtils.getParam(this, C.PASS_NAME, C.PASS).toString())) {
//            showCont();
//            dialog.dismiss();
//        } else {
//            toastUntil.ShowToastShort("密码错误");
//            return;
//        }
//    }

    /**
     * 验证通过，显示设置界面
     **/
    private void showCont() {
        if (setCont.getVisibility() == View.GONE) {//如果linear layout为隐藏状态，则显示
            setCont.setVisibility(View.VISIBLE);
        }
    }

    @OnItemSelected(R.id.set_up_sp)
    public void onSpClick(AdapterView<?> parent, View view, int position, long id) {
        if (isFirst) {
            isFirst = false;
        } else {
            attend_mode = position;
            Log.e(TAG, "onSpClick: a11"+attend_mode );
        }
    }

    @OnItemSelected(R.id.set_up_sp_inOrout)
    public void onSpInClick(AdapterView<?> parent, View view, int position, long id) {
        if (isFirst) {
            isFirst = false;
        } else {
            in_mode = position + 1;
            Log.e(TAG, "onSpInClick: " + in_mode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toastUntil.stopToast();
        inAdapter = null;
        adapter = null;
    }
}
