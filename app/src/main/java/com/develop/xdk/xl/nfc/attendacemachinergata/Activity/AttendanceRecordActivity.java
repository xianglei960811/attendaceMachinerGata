package com.develop.xdk.xl.nfc.attendacemachinergata.Activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.develop.xdk.xl.nfc.attendacemachinergata.Base.BaseActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.MainActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.MyService.myService;
import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SqlCallBack;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.DownLoadingDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.LoadingDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.Loading.timeOutListner;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.OperableDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Image_byte.Img_byte;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.LocalBaseUser;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.PersonDossier;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AttendanceRecordActivity extends BaseActivity {
    @BindView(R.id.atten_record_total_number)
    TextView attenRecordTotalNum;
    @BindView(R.id.atten_record_gata_in_number)
    TextView attenRecordGataInNumber;
    @BindView(R.id.atten_record_gata_out_number)
    TextView attenRecordGataOutNum;
    @BindView(R.id.atten_record_door_in_number)
    TextView attenRecordDoorInNum;
    @BindView(R.id.atten_record_door_out_number)
    TextView attenRecordDoorOutNum;
    @BindView(R.id.atten_record_lesson_in_numb)
    TextView attenRecordLessonInNum;
    @BindView(R.id.atten_record_lesson_out_number)
    TextView attenRecordLessonOutNum;
    @BindView(R.id.atten_record_teacher_in_numb)
    TextView attenRecordTeacherInNum;
    @BindView(R.id.atten_record_teacher_out_number)
    TextView attenRecordTeacherOutNum;
    @BindView(R.id.atten_record_see)
    Button attenRecordSee;
    @BindView(R.id.atten_record_back)
    Button attenRecordBack;
    @BindView(R.id.attendance_record_back)
    ImageButton attenBack;
    @BindView(R.id.atten_record_up)
    Button attenRecorUp;
    @BindView(R.id.atten_record_down)
    Button attenRecordDown;
    @BindView(R.id.atten_record_clear)
    Button attenRecordClear;

    private int gataInNum, gataOutNum, doorInNum, doorOutNum, lessonInNum, lessonOutNum, teacherInNum, teacherOutNum, totalNum;
    private LoadingDialog loadingDialog;
    private DownLoadingDialog downLoadingDialog;
    private int timeout = 5;//超时时间
    private volatile List<BaseAttendRecord> attends = new ArrayList<>();

    private myService myservice;
    Intent progressIntent;
    private MsgReceive msgReceive;

    private volatile Boolean isLoading = false;//是否正在下载标识
    private volatile Boolean isShowing = false;//是否正在显示dialog
    private volatile Boolean isLoading1 = false;//是否正在下载标识
    private volatile Boolean isShowing1 = false;//是否正在显示dialog

    @Override
    protected void initView() {
        initDialog();
        gataInNum = gataOutNum = doorOutNum = doorInNum = totalNum =
                lessonInNum = lessonOutNum = teacherOutNum = teacherInNum = 0;
        attenRecordTotalNum.setText(attends.size() + "");
        attenRecordGataInNumber.setText(gataInNum + "");
        attenRecordGataOutNum.setText(gataOutNum + "");
        attenRecordDoorInNum.setText(doorInNum + "");
        attenRecordDoorOutNum.setText(doorOutNum + "");
        attenRecordLessonInNum.setText(lessonInNum + "");
        attenRecordLessonOutNum.setText(lessonOutNum + "");
        attenRecordTeacherInNum.setText(teacherInNum + "");
        attenRecordTeacherOutNum.setText(teacherOutNum + "");
    }

    private void initDialog() {
        //初始化dialog
        loadingDialog = new LoadingDialog(this, R.style.NormalDialogStyle, timeout, new timeOutListner() {
            @Override
            public void onTimeOut(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
        downLoadingDialog = new DownLoadingDialog(this, R.style.NormalDialogStyle, 0, new timeOutListner() {
            @Override
            public void onTimeOut(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
        downLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                isShowing = false;
                isShowing1 = false;
            }
        });
        downLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
                    isShowing = false;
                    isShowing1 = false;
                }
                return false;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        progressIntent = new Intent(AttendanceRecordActivity.this, myService.class);
        bindService(progressIntent, conn, Service.BIND_AUTO_CREATE);

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.develop.xdk.xl.nfc.attendacemachinergata.communication.RECEIVER");
        msgReceive = new MsgReceive();
        registerReceiver(msgReceive, intentFilter);
    }

    /**
     * 获取数据
     */
    private synchronized void getData() {
        loadingDialog.show();
        sqlControl.selectAllAttendances(callBack);
        if (attends == null || attends.isEmpty()) {
            toastUntil.ShowToastShort("没有考勤记录");
            loadingDialog.dismiss();
            return;
        }
        for (BaseAttendRecord record : attends) {
            if (record.getA_attendMode() == C.SCHOOL_GATA_MODE) {
                if (record.getA_inOrOutMode() == C.IN_MODE) {
                    gataInNum++;
                } else if (record.getA_inOrOutMode() == C.OUT_MODE) {
                    gataOutNum++;
                }
            } else if (record.getA_attendMode() == C.DOOR_MODE) {
                if (record.getA_inOrOutMode() == C.IN_MODE) {
                    doorInNum++;
                } else {
                    doorOutNum++;
                }
            } else if (record.getA_attendMode() == C.LESSON_OR_EXAMINE_MODE) {
                if (record.getA_inOrOutMode() == C.IN_MODE) {
                    lessonInNum++;
                } else {
                    lessonOutNum++;
                }
            } else {
                if (record.getA_inOrOutMode() == C.IN_MODE) {
                    teacherInNum++;
                } else {
                    teacherOutNum++;
                }
            }
        }
        attenRecordTotalNum.setText(attends.size() + "");
        attenRecordGataInNumber.setText(gataInNum + "");
        attenRecordGataOutNum.setText(gataOutNum + "");
        attenRecordDoorInNum.setText(doorInNum + "");
        attenRecordDoorOutNum.setText(doorOutNum + "");
        attenRecordLessonInNum.setText(lessonInNum + "");
        attenRecordLessonOutNum.setText(lessonOutNum + "");
        attenRecordTeacherInNum.setText(teacherInNum + "");
        attenRecordTeacherOutNum.setText(teacherOutNum + "");
        loadingDialog.dismiss();
    }

    private SqlCallBack callBack = new SqlCallBack<List<BaseAttendRecord>>() {

        @Override
        public void onRespose(List<BaseAttendRecord> attendRecords) {
            attends.addAll(attendRecords);
            Log.e(TAG, "onRespose: " + attends.size());
        }

        @Override
        public void onError(String msg) {
            toastUntil.ShowToastShort(msg);
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_attendance_record);
    }

    @OnClick(R.id.atten_record_back)
    public void onAttenRecordback() {
        toActivityWithFinish(MainActivity.class);
    }

    @OnClick(R.id.attendance_record_back)
    public void onBackClick() {
//        toActivityWithFinish(MainActivity.class);

        LocalBaseUser student = new LocalBaseUser();
        student.setU_Name("小向");
        student.setU_CardID("D57C8F6C");
        student.setU_HeadImage(Img_byte.imageTobyte(this, R.drawable.aaaaa));
        student.setU_Class("2015级4班");
        student.setU_Phone("17828896780");
        student.setU_Status("学生");
        student.setU_Sex("男");
        sqlControl.insertStudentInfo(student, new SqlCallBack() {
            @Override
            public void onRespose(Object obj) {
                toastUntil.ShowToastShort("插入信息成功");
            }

            @Override
            public void onError(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
    }

    @OnClick(R.id.atten_record_see)
    public void onSeeClick() {
        Intent intent = new Intent();
        intent.putExtra("totalNumb", attends.size());
        intent.setClass(this, AttendsListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.atten_record_up)
    public void onUpClick() {
        if (attends == null || attends.size() == 0) {
            toastUntil.ShowToastShort("没有记录需要上传");
            return;
        }
        if (isLoading) {
            toastUntil.ShowToastShort("请在下载完成后，再执行该操作");
            return;
        }
        if (isLoading1) {
            toastUntil.ShowToastShort("正在上传记录");
            isShowing1 = true;
            return;
        }

        myservice.updataAteends(attends);
        isShowing1 = true;
        isLoading1 = true;
    }

    @OnClick(R.id.atten_record_down)
    public void onDownClick() {
        if (isLoading1) {
            toastUntil.ShowToastShort("请在上传操作完成后，再执行该操作");
            return;
        }
        if (isLoading) {
            toastUntil.ShowToastShort("正在下载档案");
            isShowing = true;
            return;
        }
        String title = "操作验证：";
        String message = "更新档案将清除本地档案，是否继续？";
        final OperableDialog operableDialog = new OperableDialog(this, R.style.NormalDialogStyle, 30,
                title, message, "确定", "取消", new timeOutListner() {
            @Override
            public void onTimeOut(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
        operableDialog.show();
        operableDialog.setClickListenerInterface(new OperableDialog.clickListenerInterface() {
            @Override
            public void onEnsure() {
//                sqlControl.deletUser(new SqlCallBack<String>() {
//                    @Override
//                    public void onRespose(String msg) {
//                        toastUntil.ShowToastShort("档案已清除，即将下载......");
//                        myservice.startDown();
//                        isLoading = true;
//                    }
//
//                    @Override
//                    public void onError(String msg) {
//                        toastUntil.ShowToastShort(msg);
//                    }
//                });
                myservice.startDown();
                isLoading = true;
                isShowing = true;
                operableDialog.dismiss();
            }

            @Override
            public void onCancel() {
                operableDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.atten_record_clear)
    public void onClearClick() {
        sqlControl.deletUser(new SqlCallBack<String>() {
            @Override
            public void onRespose(String msg) {
                toastUntil.ShowToastShort("清除用户信息成功");
            }

            @Override
            public void onError(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (myservice == null) {
                myservice = ((myService.MyBinder) iBinder).getService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myservice = null;
        }
    };

    //动态广播接收器
    private class MsgReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String progress = intent.getStringExtra("progress");
            String mode = intent.getStringExtra("mode");
            Log.d(TAG, "onReceive: " + progress + "::::" + mode);
            if (mode.equals("down")) {
                attenRecordDown.setText("查看下载进度");
                isLoading = true;
                if (isLoading && isShowing) {
                    downLoadingDialog.show();
                }
                if (downLoadingDialog.isShowing() && isLoading) {
                    downLoadingDialog.setCurrentPosition(Integer.parseInt(progress));
                }
                if (progress.equals("100")) {
                    Log.d(TAG, "onReceive: 下载完成");
                    downLoadingDialog.dismiss();
                    isLoading = false;
                    isShowing = false;
                    attenRecordDown.setText("更新用户档案");
                }
            } else if (mode.equals("updata")) {
                attenRecorUp.setText("查看更新进度");
                isLoading1 = true;
                if (isLoading1 && isShowing1) {
                    downLoadingDialog.show();
                }
                if (downLoadingDialog.isShowing() && isLoading1) {
                    downLoadingDialog.setCurrentPosition(Integer.parseInt(progress));
                }
                if (progress.equals("100")) {
                    downLoadingDialog.dismiss();
                    isLoading1 = false;
                    isShowing1 = false;
                    attenRecorUp.setText("上传考勤记录");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toastUntil.stopToast();
        unregisterReceiver(msgReceive);
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
        loadingDialog = null;
        downLoadingDialog = null;
        attends = null;
        msgReceive = null;
        myservice = null;
        progressIntent = null;

    }
}
