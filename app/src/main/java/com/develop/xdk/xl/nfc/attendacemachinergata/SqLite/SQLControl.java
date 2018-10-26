package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseHandle_forTable.BaseAttends;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseHandle_forTable.BaseUserStudent;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL.TableColumns;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.LoadingDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.Loading.timeOutListner;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.LocalBaseUser;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.PersonDossier;
import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

import java.util.List;

public class SQLControl implements TableColumns {
    private static String TAG = "SQLControl";
    private Context context;
    private ToastUntil toastUntil;
    private LoadingDialog myPD;

    public SQLControl(Context context) {
        this.context = context;
        toastUntil = new ToastUntil(context);

        myPD = new LoadingDialog(context, R.style.NormalDialogStyle, C.TIME_OUT, new timeOutListner() {
            @Override
            public void onTimeOut(String msg) {
                toastUntil.ShowToastShort(msg);
            }
        });
    }

    /**
     * 根据卡号查询学生信息
     *
     * @param cardid   卡号
     * @param callback 回调接口
     * @return
     */
    public void selectStudentInfo(String cardid, final SqlCallBack callback) {
        myPD.show();
        Log.e(TAG, "selectStudentInfo: ======================>" + cardid);
        BaseUserStudent.getInstance(context).selectUser(USER_STUDENT_COLUMNS.FIELD_CARD_ID, new String[]{cardid},
                null, null, null, null,
                new SqlCallBack<List<LocalBaseUser>>() {
                    @Override
                    public void onRespose(List<LocalBaseUser> students) {
                        for (LocalBaseUser student1 : students) {
                            callback.onRespose(student1);
                        }

                        myPD.dismiss();
                    }

                    @Override
                    public void onError(String msg) {
                        callback.onError(msg);
                        myPD.dismiss();
                    }
                });

    }


    /**
     * 插入学生用户
     *
     * @param callBack
     * @param student
     */
    public void insertStudentInfo(LocalBaseUser student, final SqlCallBack callBack) {
        ContentValues cv = new ContentValues();
        cv.put(USER_STUDENT_COLUMNS.FIELD__HEAD_IMAGE, student.getU_HeadImage());
        cv.put(USER_STUDENT_COLUMNS.FIELD_CARD_ID, student.getU_CardID());
        cv.put(USER_STUDENT_COLUMNS.FIELD_CLASS, student.getU_Class());
        cv.put(USER_STUDENT_COLUMNS.FIELD_NAME, student.getU_Name());
        cv.put(USER_STUDENT_COLUMNS.FIELD_PHONE, student.getU_Phone());
        cv.put(USER_STUDENT_COLUMNS.FIELD_STATUS, student.getU_Status());
        cv.put(USER_STUDENT_COLUMNS.FIELD_SEX, student.getU_Sex());

        BaseUserStudent.getInstance(context).InsertUserStudent(cv, new SqlCallBack<String>() {
            @Override
            public void onRespose(String msg) {
                callBack.onRespose(msg);
            }

            @Override
            public void onError(String msg) {
                onError(msg);
            }
        });
        cv.clear();
    }


    /***
     * 插入考勤记录
     *
     * @param record
     */
    public void insertAttendances(BaseAttendRecord record, final SqlCallBack callBack) {
        myPD.show();
        ContentValues cv = new ContentValues();
        cv.put(ATTENDANCES_COUMNS.FIELD_ATTENDANCE_MODE, record.getA_attendMode());
        cv.put(ATTENDANCES_COUMNS.FIELD_CARD_ID, record.getA_cardID());
        cv.put(ATTENDANCES_COUMNS.FIELD_CLASS, record.getA_class());
        cv.put(ATTENDANCES_COUMNS.FIELD_HEAD_IMAGE, record.getA_headImage());
        cv.put(ATTENDANCES_COUMNS.FIELD_IN_OUT_MODE, record.getA_inOrOutMode());
        cv.put(ATTENDANCES_COUMNS.FIELD_NAME, record.getA_name());
        cv.put(ATTENDANCES_COUMNS.FIELD_PHONE, record.getA_phone());
        cv.put(ATTENDANCES_COUMNS.FIFLE_ATTENDANCE_DATE, record.getA_attendDate());
        cv.put(ATTENDANCES_COUMNS.FIFLE_IS_HANDLE, record.getA_isHandle());
        cv.put(ATTENDANCES_COUMNS.FIFLE_IS_STUDENT, record.getA_isStudent());
        BaseAttends.getInstance(context).InsertAttends(cv, new SqlCallBack<String>() {
            @Override
            public void onRespose(String msg) {
                callBack.onRespose(msg);
//                myPD.dismiss();
            }

            @Override
            public void onError(String msg) {
                callBack.onError(msg);
//                myPD.dismiss();
            }
        });
        cv.clear();

    }

    /**
     * 分页查询
     *
     * @param startIdex
     * @param endIndex
     * @param callBack
     */
    public void selectAttendanc(int startIdex, int endIndex, final SqlCallBack callBack) {
        BaseAttends.getInstance(context).selectAttends(null, null,
                null, null, null, "" + startIdex + "," + endIndex + "", new SqlCallBack() {
                    @Override
                    public void onRespose(Object attendRecords) {
                        callBack.onRespose(attendRecords);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onError(msg);
                    }
                });
    }

    /**
     * 查询全部考勤记录
     *
     * @param callBack 回调接口
     */
    public void selectAllAttendances(final SqlCallBack callBack) {
        BaseAttends.getInstance(context).selectAttends(null, null,
                null, null, null, null, new SqlCallBack() {
                    @Override
                    public void onRespose(Object attendRecords) {
                        callBack.onRespose(attendRecords);
                    }

                    @Override
                    public void onError(String msg) {
                        callBack.onError(msg);
                    }
                });
    }

    /**
     * 更新学生信息信息
     *
     * @param pd
     * @param nfcCardId
     * @param callBack
     */
    public void updataStudent(PersonDossier pd, String nfcCardId, final SqlCallBack callBack) {
        ContentValues cv = new ContentValues();
        cv.put(USER_STUDENT_COLUMNS.FIELD_NAME, pd.getPdName());
        cv.put(USER_STUDENT_COLUMNS.FIELD_SEX, pd.getPdSex());
        cv.put(USER_STUDENT_COLUMNS.FIELD_PHONE, pd.getPdTelephone());
        cv.put(USER_STUDENT_COLUMNS.FIELD_STATUS, pd.getPdStatus());
        cv.put(USER_STUDENT_COLUMNS.FIELD_CLASS, pd.getPdClass());
        BaseUserStudent.getInstance(context).updataUser(cv, USER_STUDENT_COLUMNS.FIELD_CARD_ID, new String[]{nfcCardId}, new SqlCallBack() {
            @Override
            public void onRespose(Object obj) {
                callBack.onRespose(obj);
            }

            @Override
            public void onError(String msg) {
                callBack.onError(msg);
            }
        });
        cv.clear();
    }

    /**
     * 更新教职工信息信息
     *
     * @param pd
     * @param nfcCardId
     * @param callBack
     */
//    public void updataTeacher(PersonDossier pd, String nfcCardId, final SqlCallBack callBack) {
//        ContentValues cv = new ContentValues();
//        cv.put(USER_TEACHING_COLUMNS.FIELD_NAME, pd.getPdName());
//        cv.put(USER_TEACHING_COLUMNS.FIELD_SEX, pd.getPdSex());
//        cv.put(USER_TEACHING_COLUMNS.FIELD_QUARTER, pd.getPdQuarter());
//        cv.put(USER_TEACHING_COLUMNS.FIELD_PHONE, pd.getPdTelephone());
//        cv.put(USER_TEACHING_COLUMNS.FIELD_JOB, pd.getPdJob());
//        cv.put(USER_TEACHING_COLUMNS.FIELD_CLASS, pd.getPdClass());
//        cv.put(USER_TEACHING_COLUMNS.FIELD__HEAD_IMAGE, pd.getPdHeadImage());
//        BaseUserTeacher.getInstance(context).updataUserTeacher(cv, USER_STUDENT_COLUMNS.FIELD_CARD_ID, new String[]{nfcCardId}, new SqlCallBack() {
//            @Override
//            public void onRespose(Object obj) {
//                callBack.onRespose(obj);
//            }
//
//            @Override
//            public void onError(String msg) {
//                callBack.onError(msg);
//            }
//        });
//        cv.clear();
//    }

    /**
     * 修改学生考勤记录
     */
    public void updataAttendStudent(BaseAttendRecord record, String whereId, String[] valus, final SqlCallBack callBack) {
        ContentValues cv = new ContentValues();
        cv.put(ATTENDANCES_COUMNS.FIFLE_IS_HANDLE, record.getA_isHandle());
        cv.put(ATTENDANCES_COUMNS.FIFLE_ATTENDANCE_DATE, record.getA_attendDate());
        cv.put(ATTENDANCES_COUMNS.FIELD_PHONE, record.getA_phone());
        cv.put(ATTENDANCES_COUMNS.FIELD_NAME, record.getA_name());
        cv.put(ATTENDANCES_COUMNS.FIELD_IN_OUT_MODE, record.getA_inOrOutMode());
        cv.put(ATTENDANCES_COUMNS.FIELD_HEAD_IMAGE, record.getA_headImage());
        cv.put(ATTENDANCES_COUMNS.FIELD_CLASS, record.getA_class());
        cv.put(ATTENDANCES_COUMNS.FIELD_CARD_ID, record.getA_cardID());
        cv.put(ATTENDANCES_COUMNS.FIELD_ATTENDANCE_MODE, record.getA_attendMode());
        cv.put(ATTENDANCES_COUMNS.FIFLE_IS_STUDENT, record.getA_isStudent());
        BaseAttends.getInstance(context).updataAttends(cv, whereId, valus, new SqlCallBack() {
            @Override
            public void onRespose(Object obj) {
                callBack.onRespose(obj);
            }

            @Override
            public void onError(String msg) {
                callBack.onError(msg);
            }
        });
        cv.clear();
    }

    /**
     * 删除用户信息
     *
     * @param callBack
     */
    public void deletUser(final SqlCallBack callBack) {
        myPD.show();
        BaseUserStudent.getInstance(context).clearUser(null, null, new SqlCallBack() {
            @Override
            public void onRespose(Object msg) {
            }

            @Override
            public void onError(String msg) {
                callBack.onError(msg);
                myPD.dismiss();
            }
        });


    }


}
