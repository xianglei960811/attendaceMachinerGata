package com.develop.xdk.xl.nfc.attendacemachinergata;

import android.os.Bundle;
import android.widget.Button;

import com.develop.xdk.xl.nfc.attendacemachinergata.Activity.AttendanceInActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.Activity.AttendanceRecordActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.Activity.SteupActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.Base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_In)
    Button mainIN;
    @BindView(R.id.main_Out)
    Button mainOUT;
    @BindView(R.id.main_atten_lesson)
    Button mainAttenLesson;
    @BindView(R.id.main_atten_record)
    Button mainAttenRecord;
    @BindView(R.id.main_atten_teaching)
    Button mainAttenTeaching;
    @BindView(R.id.main_setup)
    Button mainAttenceSetuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.main_In)
    public void onMainINClick() {
//        if (attend_mode == 3) {
//            toActivity(AttemdanceTeachingInActivity.class);
//        } else {
            toActivity(AttendanceInActivity.class);
//        }
    }

    @OnClick(R.id.main_atten_record)
    public void onMainAttendanceRecordClick() {
        toActivity(AttendanceRecordActivity.class);
    }

//    @OnClick(R.id.main_Out)
//    public void OnMainOutClick() {
//        if (attend_mode == 3) {
////            toActivity(AttemdanceTeachingOutActivity.class);
//        } else {
////            toActivity(AttendanceOutActivity.class);
//        }
//    }
//
//    @OnClick(R.id.main_atten_teaching)
//    public void onMainAttenTeachingClick() {
//        toActivity(AttemdanceTeachingInActivity.class);
//    }
//
//    @OnClick(R.id.main_atten_lesson)
//    public void onMainAttenLesson() {
//        toActivity(AttendanceLessonActivity.class);
//    }

    @OnClick(R.id.main_setup)
    public void onStep() {
        toActivity(SteupActivity.class);

    }
}
