package com.develop.xdk.xl.nfc.attendacemachinergata.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.develop.xdk.xl.nfc.attendacemachinergata.Base.BaseActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.MainActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SQLControl;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SqlCallBack;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.Loading.MyLoading;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.Loading.timeOutListner;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.LoadingDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Image_byte.GlideApp;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.MyList.ListAdapter;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.MyList.MyListView;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.ReSizeDrawable.ReSizeDrawable;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.getScreenSize.ScreenSizeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AttendsListActivity extends BaseActivity implements MyListView.MyLoadListner, AdapterView.OnItemClickListener {
    @BindView(R.id.attends_list_ima_back)
    ImageButton attendsListImaBack;
    @BindView(R.id.attends_list_myLiset)
    MyListView attendsListMylist;
    @BindView(R.id.attends_list_back)
    Button attendsListBack;

    private List<BaseAttendRecord> data;
    private List<BaseAttendRecord> datas;
    private ListAdapter adapter;
    private LoadingDialog dialog = null;
    private int startIndex;
    private int numb = 10;//每页加载数
    private int totalNumb;//总数

    @Override
    protected void initView() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        //初始化视图
        data = new ArrayList<>();
        datas = new ArrayList<>();
        dialog = new LoadingDialog(this, R.style.NormalDialogStyle, 5, new timeOutListner() {
            @Override
            public void onTimeOut(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
        Intent intent = this.getIntent();
        totalNumb = intent.getIntExtra("totalNumb", 0);
        Log.e(TAG, "init: " + totalNumb);

        attendsListMylist.intShow(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SQLControl.getINSTANCE().selectAttendanc(AttendsListActivity.this,startIndex, numb, new SqlCallBack<List<BaseAttendRecord>>() {
            @Override
            public void onRespose(List<BaseAttendRecord> records) {
//                Log.e(TAG, "onRespose: " + records.size());
                data.addAll(records);
                datas.addAll(data);
                startIndex += numb;
                showListview(datas);
            }

            @Override
            public void onError(String msg) {
                toastUntil.ShowToastShort(msg);
                attendsListMylist.isend();
            }
        });
        Log.d(TAG, "onStart: ======================>");
    }

    /**
     * 更新listview 视图
     *
     * @param lists
     */
    private void showListview(List<BaseAttendRecord> lists) {
        if (lists == null || lists.size() == 0) {
            attendsListMylist.isend();
            return;
        }
        if (lists.size() < numb) {
            attendsListMylist.isend();
            toastUntil.ShowToastShort("已加载全部数据");
        }
        if (adapter == null) {
            attendsListMylist.setMyLoadListner(AttendsListActivity.this);
            adapter = new ListAdapter(this, lists);
            attendsListMylist.setAdapter(adapter);
            if (lists.size() % numb == 0 && lists.size() == totalNumb) {
                attendsListMylist.isend();
                toastUntil.ShowToastShort("已加载全部数据");
            }
        } else {
            if (lists.size() % numb == 0 && lists.size() == totalNumb) {
                attendsListMylist.isend();
                toastUntil.ShowToastShort("已加载全部数据");
            }
            adapter.onDataChange(lists);
        }
        dialog.dismiss();
        attendsListMylist.setOnItemClickListener(this);
    }

    @OnClick(R.id.attends_list_back)
    public void onBackClick() {
        toActivityWithFinish(AttendanceRecordActivity.class);
    }

    @OnClick(R.id.attends_list_ima_back)
    public void onImaBackClick() {
        toActivityWithFinish(AttendanceRecordActivity.class);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_attends_list);
    }

    @Override
    public void onload() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLoadData();
                showListview(datas);
                attendsListMylist.setComplete();
            }
        }, 2000);
    }

    /**
     * 获取每次加载的数据
     */
    private void getLoadData() {
        SQLControl.getINSTANCE().selectAttendanc(AttendsListActivity.this,startIndex, numb, new SqlCallBack<List<BaseAttendRecord>>() {
            @Override
            public void onRespose(List<BaseAttendRecord> records) {
                Log.e(TAG, "onRespose: " + records.size());
                data.clear();//每次加载前先清空临时储存单位
                data.addAll(records);
                startIndex += numb;
                datas.addAll(data);
                Log.e(TAG, "getLoadData onRespose: " + datas.size());
                if (records == null || records.size() == 0) {
                    toastUntil.ShowToastShort("已加载全部数据");
                    attendsListMylist.isend();
                }
                if (datas.size() == totalNumb) {
                    toastUntil.ShowToastShort("已加载全部数据");
                    attendsListMylist.isend();
                }
            }

            @Override
            public void onError(String msg) {
                toastUntil.ShowToastShort(msg);
                attendsListMylist.isend();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.show();
        if (id == -1) {
            return;
        }
        showMyListDialog(position);
    }

    private void showMyListDialog(int position) {
        final MyLoading loading = new MyLoading(this, R.style.NormalDialogStyle, 30, new timeOutListner() {
            @Override
            public void onTimeOut(String msg) {
                toastUntil.ShowToastShort("msg");
            }
        });
        View view = View.inflate(this, R.layout.my_list_operable_dialog, null);
        Button bt_ensure = view.findViewById(R.id.my_list_dialog_ensure);
        TextView tv_numb = view.findViewById(R.id.my_list_dialog_numb);
        TextView tv_name = view.findViewById(R.id.my_list_dialog_name);
        TextView tv_class = view.findViewById(R.id.my_list_dialog_class);
        TextView tv_status = view.findViewById(R.id.my_list_dialog_status);
        TextView tv_isHandle = view.findViewById(R.id.my_list_dialog_isHandle);
        TextView tv_telPhone = view.findViewById(R.id.my_list_dialog_telPhone);
        TextView tv_attendMode = view.findViewById(R.id.my_list_dialog_attendMode);
        TextView tv_inORout = view.findViewById(R.id.my_list_dialog_inORout);
        TextView tv_attendDate = view.findViewById(R.id.my_list_dialog_attendDate);
        final ImageView ima_head = view.findViewById(R.id.my_list_dialog_ima);

        loading.setContentView(view);
        loading.setCanceledOnTouchOutside(false);
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
        Window dialogWindow = loading.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenSizeUtils.getInstance(this).getScreenWidth();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        try {
            BaseAttendRecord record = datas.get(position);
            SQLControl.getINSTANCE().selectHeadImage(this,record.getA_cardID(), new SqlCallBack<byte[]>() {
                @Override
                public void onRespose(byte[] bytes) {
                    Drawable drawable = ContextCompat.getDrawable(AttendsListActivity.this, R.drawable.img_error);
                    Drawable drawable1 = ContextCompat.getDrawable(AttendsListActivity.this, R.drawable.img_loading);
                    drawable = ReSizeDrawable.reSize(AttendsListActivity.this, (BitmapDrawable) drawable);
                    drawable1 = ReSizeDrawable.reSize(AttendsListActivity.this, (BitmapDrawable) drawable1);
                    GlideApp.with(AttendsListActivity.this)
                            .load(bytes)
                            .placeholder(drawable1)
                            .error(drawable)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ima_head);
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, "showMyListDialog-->onError: "+msg);
                    byte[] bytes = null;
                    Drawable drawable = ContextCompat.getDrawable(AttendsListActivity.this, R.drawable.img_error);
                    Drawable drawable1 = ContextCompat.getDrawable(AttendsListActivity.this, R.drawable.img_loading);
                    drawable = ReSizeDrawable.reSize(AttendsListActivity.this, (BitmapDrawable) drawable);
                    drawable1 = ReSizeDrawable.reSize(AttendsListActivity.this, (BitmapDrawable) drawable1);
                    GlideApp.with(AttendsListActivity.this)
                            .load(bytes)
                            .placeholder(drawable1)
                            .error(drawable)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ima_head);
                }
            });
            tv_numb.setText(position + 1 + "");
            tv_name.setText(record.getA_name());
            tv_class.setText(record.getA_class());
            tv_status.setText(record.getStatus());
            if (record.getA_isHandle() == C.IS_HANDLE) {
                tv_isHandle.setText("是");
                tv_isHandle.setTextColor(Color.GREEN);
            } else {
                tv_isHandle.setText("否");
                tv_isHandle.setTextColor(Color.RED);
            }
            tv_telPhone.setText(record.getA_phone());
            if (record.getA_attendMode() == C.SCHOOL_GATA_MODE) {
                tv_attendMode.setText(C.SCHOOL_GATA);
            } else if (record.getA_attendMode() == C.DOOR_MODE) {
                tv_attendMode.setText(C.DOOR);
            } else if (record.getA_attendMode() == C.LESSON_OR_EXAMINE_MODE) {
                tv_attendMode.setText(C.LESSON_OR_EXAMINE);
            } else {
                tv_attendMode.setText(C.TEACHING_STAFF);
            }
            if (record.getA_inOrOutMode() == C.IN_MODE) {
                tv_inORout.setText(C.IN_MODE_NAME);
            } else {
                tv_inORout.setText(C.OUT_MODE_NAME);
            }
            tv_attendDate.setText(record.getA_attendDate());
        } catch (Exception e) {
            toastUntil.ShowToastShort("加载失败");
            Log.e(TAG, "showMyListDialog: 设置view======================>" + e.getMessage());
        }

        bt_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.dismiss();
            }
        });
        loading.show();
        dialog.dismiss();
    }


}
