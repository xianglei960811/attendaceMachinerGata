package com.develop.xdk.xl.nfc.attendacemachinergata.MyService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SQLControl;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SqlCallBack;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.LocalBaseUser;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.PersonDossier;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.controller.RechargeController;
import com.develop.xdk.xl.nfc.attendacemachinergata.http.subscribers.SubscriberOnNextListener;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Author: XL
 * Date: 2018-10-19 15:34
 * Describe:服务
 */
public class myService extends Service {
    private final String TAG = getClass().getSimpleName();
    private IBinder binder = new MyBinder();
    private SQLControl sqlControl;
    private ToastUntil toastUntil;

    private Intent progressIntent;
    private Boolean old_Online = false;
    private Boolean now_online = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sqlControl = new SQLControl(this);
        toastUntil = new ToastUntil(this);
        progressIntent = new Intent("com.develop.xdk.xl.nfc.attendacemachinergata.communication.RECEIVER");

        Log.d(TAG, "onCreate: ------------------------->");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class MyBinder extends Binder {
        public myService getService() {
            return myService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        checkOnlineConnection();
        return super.onStartCommand(intent, flags, startId);
    }


    /***
     * 计算百分比
     * @param i 当前值
     * @param max 总值
     * @return 返回百分比
     */
    private String getPercent(int i, int max) {
        String result;
        double x = i * 1.0;
        double tempresult = x / max;
        //百分比格式，后面不足两位的用0补齐 ##.00%
        DecimalFormat df = new DecimalFormat("0.00%");
        result = df.format(tempresult);
        result = result.replace("%", "");
        int posiotion = Integer.parseInt(new DecimalFormat("0").format(Double.valueOf(result)));
        return Integer.toString(posiotion);
    }

    /**
     * 下载用户档案
     */
    public void startDown() {
        RechargeController.getInstance().recepDossier(new SubscriberOnNextListener<List<PersonDossier>>() {
            @Override
            public void onNext(final List<PersonDossier> dossiers) {
                final int max_progress = dossiers.size();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < max_progress; i++) {
                            final String progress = getPercent(i + 1, max_progress);
                            PersonDossier pd = dossiers.get(i);
                                final LocalBaseUser student = new LocalBaseUser();
                                student.setU_Name(pd.getPdName());
                                student.setU_Sex(pd.getPdSex());
                                student.setU_Status(pd.getPdStatus());
                                student.setU_Class(pd.getPdClass().toString());
                                student.setU_CardID(pd.getPdCardid());
                                student.setU_Phone(pd.getPdTelephone());
                                sqlControl.insertStudentInfo(student, new SqlCallBack<String>() {
                                    @Override
                                    public void onRespose(String msg) {
                                        Log.d(TAG, "onRespose:============>> " + progress);
                                        progressIntent.putExtra("progress", progress);
                                        sendBroadcast(progressIntent);//发送广播，并将信息传递给ui界面
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        toastUntil.ShowToastShort("学生：" + student.getU_Name() + " 信息下载失败，请稍后重试");
                                        Log.e(TAG, "onError: student" + msg);
                                    }
                                });
                            if (progress.equals(100)) {
                                toastUntil.ShowToastShort("档案更新完成");
                            }
                        }
                    }
                };
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Looper.prepare();
                        new Handler().post(runnable);
                        Looper.loop();
                    }
                }.start();

            }
        }, this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressIntent.putExtra("progress", getPercent(i, 100));
                    progressIntent.putExtra("mode","down");
                    Log.d(TAG, "run: ----------------------->" + i);
                    sendBroadcast(progressIntent);//发送广播，并将信息传递给ui界面
                    if (i == 100) {
                        toastUntil.ShowToastShort("档案更新完成");
                        break;
                    }
                    i++;
                }
            }
        }).start();
    }

    /**
     * 上传考勤记录
     *
     * @param attends
     */
    public void updataAteends(final List<BaseAttendRecord> attends) {
        final int[] progress = {1};
        final int max = attends.size();
//        for (final BaseAttendRecord record : attends) {
//            if (record.getA_isHandle() == C.IS_HANDLE) {
//                continue;
//            }
//
//            RechargeController.getInstance().updataAttends(record.getA_cardID(), record.getA_attendMode(), record.getA_inOrOutMode(),
//                    record.getA_attendDate(), new SubscriberOnNextListener<PersonDossier>() {
//                        @Override
//                        public void onNext(PersonDossier s) {
//                            progress[0]++;
//                            record.setA_isHandle(C.IS_HANDLE);
//                            sqlControl.updataAttendStudent(record, TableColumns.ATTENDANCES_COUMNS.FIELD_ID, new String[]{String.valueOf(record.getA_id())}, new SqlCallBack() {
//                                @Override
//                                public void onRespose(Object obj) {
//                                    progressIntent.putExtra("progress", getPercent(progress[0], max));
//                                    progressIntent.putExtra("mode","updata");
//                                    sendBroadcast(progressIntent);
//                                    if (getPercent(progress[0],max).equals("100")){
//                                        toastUntil.ShowToastShort("上传记录完成");
//                                    }
//                                }
//
//                                @Override
//                                public void onError(String msg) {
//                                    Log.e(TAG, "onError: updataAteends------------->"+msg );
//
//                                }
//                            });
//
//                        }
//                    }, this);
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressIntent.putExtra("progress", getPercent(i, 100));
                    progressIntent.putExtra("mode","updata");
                    Log.d(TAG, "run: ----------------------->" + i);
                    sendBroadcast(progressIntent);//发送广播，并将信息传递给ui界面
                    if (i == 100) {
                        toastUntil.ShowToastShort("档案更新完成");
                        break;
                    }
                    i++;
                }
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
