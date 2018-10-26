package com.develop.xdk.xl.nfc.attendacemachinergata.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.develop.xdk.xl.nfc.attendacemachinergata.Base.BaseActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.MainActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SqlCallBack;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.CheckInterner.NetWorkUntil;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.GetTime.GetNETtime;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Image_byte.Img_byte;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.LocalBaseUser;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.PersonDossier;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.ReSizeDrawable.ReSizeDrawable;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.ReversalString.ReversalString;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.controller.RechargeController;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.subscribers.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.OnClick;

//进入界面
public class AttendanceInActivity extends BaseActivity {
    @BindView(R.id.attendance_gata_title)
    TextView attenGataTitle;
    @BindView(R.id.attendance_gata_hide)//提示
            TextView attenGatahide;
    @BindView(R.id.attendance_gata_back)
    ImageButton attenGataBack;
    @BindView(R.id.attendance_gata_people_name)
    TextView attenGataPeopleName;
    @BindView(R.id.attendance_gata_people_sex)
    TextView attenGataPeopleSex;
    @BindView(R.id.attendance_gata_people_class)
    TextView attenGataPeopleClass;
    @BindView(R.id.attendance_gata_people_phone)
    TextView attenGataPeoplePhone;
    @BindView(R.id.attendance_gata_people_quarter)//人员身份
            TextView attenGataPeopleStatus;
    @BindView(R.id.attendance_gata_people_image)
    ImageView attenGataPeopleImage;
    @BindView(R.id.attendance_gata_bt_in)
    Button attenGataBtIn;
    @BindView(R.id.attendance_gata_bt_out)
    Button attenGataBtOut;
    @BindView(R.id.attendance_gata_bt_currency)//通用
            Button attenGataBtCurrency;
    @BindView(R.id.attendance_gata_bt_back)
    Button attenGataBtBack;

    private volatile LocalBaseUser student_ = null;
    private volatile Boolean isImageChange = false;

    @SuppressLint("HandlerLeak")
    public Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                if (msg.obj != null) {
                    attenGatahide.setText(msg.obj.toString());
                } else {
                    attenGatahide.setText("提醒：获取学生信息失败，请稍后重试");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        student_ = new LocalBaseUser();
        clearView();
        if (attend_mode == C.SCHOOL_GATA_MODE) {
            attenGataTitle.append("·" + C.SCHOOL_GATA);
        } else if (attend_mode == C.DOOR_MODE) {
            attenGataTitle.append("·" + C.DOOR);
        } else if (attend_mode == C.LESSON_OR_EXAMINE_MODE) {
            attenGataTitle.append("·" + C.LESSON_OR_EXAMINE);
        } else if (attend_mode == C.TEACHING_STAFF_MODE) {
            attenGataTitle.append("·" + C.TEACHING_STAFF);
        }
        if (in_out_mode == C.IN_MODE) {
            attenGataTitle.append("·" + C.IN_MODE_NAME);
        } else if (in_out_mode == C.OUT_MODE) {
            attenGataTitle.append("·" + C.OUT_MODE_NAME);
        }
        attenGatahide.setText("提示：请将学生卡放入刷卡区刷卡");
    }

    private void clearView() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.default_1);
        drawable = ReSizeDrawable.reSize(this, (BitmapDrawable) drawable);
        attenGataPeopleImage.setImageDrawable(drawable);
        isImageChange = false;
        attenGataPeopleName.setText("");
        attenGataPeopleSex.setText("");
        attenGataPeopleClass.setText("");
        attenGataPeoplePhone.setText("");
        attenGataPeopleStatus.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcCardID = getcardId.NfcCardID_OnResume();
//        NfcCardID = "D10CEFC8";
        if (NfcCardID == null || NfcCardID.equals("")) {
            return;
        } else {
            attenGatahide.setText("提示：学生卡读取成功，正在获取学生信息......");
            ShowStudentInfo(NfcCardID);
            Log.e(TAG, "onNewIntent: " + NfcCardID);
        }
//        toastUntil.ShowToastShort(NfcCardID);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        clearView();
        NfcCardID = getcardId.NfcCardID_OnNewIntent(intent);
//        toastUntil.ShowToastShort("ssssss"+NfcCardID);
        if (NfcCardID == null || NfcCardID.equals("")) {
            attenGatahide.setText("提示：卡号读取失败，请换卡重试");
            return;
        } else {
            attenGatahide.setText("提示：学生卡读取成功，正在获取学生信息......");
            Log.e(TAG, "onNewIntent: " + NfcCardID);
            ShowStudentInfo(NfcCardID);
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_attendance_gata);
    }

    @OnClick(R.id.attendance_gata_back)
    public void onBackClick() {
        toActivityWithFinish(MainActivity.class);
    }

    @OnClick(R.id.attendance_gata_bt_back)
    public void onBtBackClick() {
        toActivityWithFinish(MainActivity.class);
    }

    /**
     * 查询学生信息并显示
     *
     * @param nfcCardID 卡号
     */
    private void ShowStudentInfo(final String nfcCardID) {
        if (NetWorkUntil.is_connction(this)) {
            //联网情况下
            RechargeController.getInstance().getUserinfo(new SubscriberOnNextListener<PersonDossier>() {
                @Override
                public void onNext(final PersonDossier personDossier) {
                    student_.setU_CardID(personDossier.getPdCardid());
                    student_.setU_Class(String.valueOf(personDossier.getPdClass()));
                    student_.setU_Sex(personDossier.getPdSex());
                    student_.setU_Name(personDossier.getPdName());
                    student_.setU_Status(personDossier.getPdStatus());
                    if (!personDossier.getPdParenttel1().equals("")||personDossier.getPdParenttel1()!=null){
                        student_.setU_Phone(personDossier.getPdParenttel1());
                    }else if (!personDossier.getPdParenttel2().equals("")||personDossier.getPdParenttel2()!=null){
                        student_.setU_Phone(personDossier.getPdParenttel2());
                    }else if (!personDossier.getPdParenttel3().equals("")||personDossier.getPdParenttel3()!=null){
                        student_.setU_Phone(personDossier.getPdParenttel3());
                    }else {
                        student_.setU_Phone("");
                    }

                    attenGatahide.setText("提示：学生信息获取成功,正在考勤......");
//                    BitmapDrawable drawable = (BitmapDrawable) Img_byte.byteToDrawable(AttendanceInActivity.this, student_.getU_HeadImage());
//                    drawable = ReSizeDrawable.reSize(AttendanceInActivity.this, drawable);
//                    attenGataPeopleImage.setImageDrawable(drawable);
                    isImageChange = true;
                    attenGataPeopleName.setText(student_.getU_Name());
                    attenGataPeopleSex.setText(student_.getU_Sex());
                    attenGataPeopleClass.setText(student_.getU_Class());
                    attenGataPeoplePhone.setText(student_.getU_Phone());
                    attenGataPeopleStatus.setText(student_.getU_Status());
                    sqlControl.selectStudentInfo(ReversalString.Reversal(nfcCardID), new SqlCallBack() {
                        @Override
                        public void onRespose(Object obj) {
                            //查询有该用户，则更新数据
                            sqlControl.updataStudent(personDossier, ReversalString.Reversal(nfcCardID), new SqlCallBack<String>() {
                                @Override
                                public void onRespose(String msg) {
                                    Log.e(TAG, "onRespose: ----------------------------------->更新用户数据成功 ");
                                }

                                @Override
                                public void onError(String msg) {
                                    Log.e(TAG, "onError: =========================>>用户信息更新失败" + msg);
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            if (msg.equals("此用户不存在")) {
                                sqlControl.insertStudentInfo(student_, new SqlCallBack() {
                                    @Override
                                    public void onRespose(Object obj) {
                                        Log.e(TAG, "onRespose: ----------------------------->插入信息成功");
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        Log.e(TAG, "onError: =================================>>插入失败" + msg);
                                    }
                                });
                            }
                            Log.e(TAG, "onError: ==========================>查询失败" + msg);
                        }
                    });
                    insertAttend();
                }
            }, this, nfcCardID);
        } else {
            sqlControl.selectStudentInfo(ReversalString.Reversal(nfcCardID), new SqlCallBack<LocalBaseUser>() {
                @Override
                public void onRespose(LocalBaseUser student) {
                    student_ = student;
                    attenGatahide.setText("提示：学生信息获取成功,正在考勤......");
                    isImageChange = true;
                    attenGataPeopleName.setText(student_.getU_Name());
                    attenGataPeopleSex.setText(student_.getU_Sex());
                    attenGataPeopleClass.setText(student_.getU_Class());
                    attenGataPeoplePhone.setText(student_.getU_Phone());
                    attenGataPeopleStatus.setText(student_.getU_Status());
                    insertAttend();
                }

                @Override
                public void onError(String msg) {
                    attenGatahide.setText(msg);
                }
            });
        }
        if (student_ == null || student_.toString().isEmpty()) {
            return;
        }
    }
    /**
     * 考勤
     */
    private void insertAttend() {
        if (attenGataPeopleName.getText().toString().equals("")) {
            attenGatahide.setText("提示：未获取学生姓名");
        } else if (attenGataPeopleSex.getText().toString().equals("")) {
            attenGatahide.setText("提示：未获取学生性别");
        }  else if (attenGataPeopleClass.getText().toString().equals("")) {
            attenGatahide.setText("提示：未获取学生所在班级");
        } else if (attenGataPeopleStatus.getText().toString().equals("")) {
            attenGatahide.setText("提示：未获取身份信息");
        } else {
//            Log.e("aaa", "insertAttend: "+new Gson().toJson(student_));
            if (student_ == null) {
                attenGatahide.setText("提示：未获取学生信息");
                return;
            }
            BaseAttendRecord record = new BaseAttendRecord();
            record.setA_name(student_.getU_Name());
            record.setA_inOrOutMode(in_out_mode);
            record.setA_class(student_.getU_Class());
            record.setA_cardID(student_.getU_CardID());
            record.setA_attendMode(attend_mode);
            record.setA_attendDate(GetNETtime.getInsance().getAllData());
            record.setA_headImage(student_.getU_HeadImage());
            record.setA_phone(student_.getU_Phone());
            record.setA_isHandle(C.IS_NO_HANDLE);
            record.setA_isStudent(C.IS_STUDENT);
//            if (record.getA_attendMode()==C.TEACHING_STAFF_MODE&&student_.getU_Status().equals("学生")){
//                attenGatahide.setText("提醒：请选择教师考勤模式");
//                return;
//            }
//            if (record.getA_attendMode()==C.TEACHING_STAFF_MODE&&student_.getU_Status().equals("教师"))
            if (NetWorkUntil.is_connction(this)) {
                RechargeController.getInstance().updataAttends(record.getA_cardID(), record.getA_attendMode(), record.getA_inOrOutMode(),
                        record.getA_attendDate(), new SubscriberOnNextListener<PersonDossier>() {
                            @Override
                            public void onNext(PersonDossier pd) {
                                attenGatahide.setText("考勤成功");
                            }
                        },this);
            } else {
                sqlControl.insertAttendances(record, new SqlCallBack<String>() {
                    @Override
                    public void onRespose(String msg) {
                        attenGatahide.setText("提示：考勤成功");
//                    clearView();
                    }

                    @Override
                    public void onError(String msg) {
                        attenGatahide.setText(msg);
                    }
                });
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NfcCardID = null;
        student_ = null;
    }
}

