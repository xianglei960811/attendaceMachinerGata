package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseHandle_forTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL.BaseDB;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL.TableColumns;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SqlCallBack;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;

import java.util.ArrayList;
import java.util.List;

public class BaseAttends extends BaseDB implements TableColumns.ATTENDANCES_COUMNS {
    private static final String TAG = "BaseAttends";
    private static BaseAttends INSTANCE = null;
    private Context mcontext;
    private SQLiteDatabase db;

    protected BaseAttends(Context context) {
        super(context);
        mcontext = context;
    }

    public static BaseAttends getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BaseUserStudent.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaseAttends(context);
                }
            }
        }
        return INSTANCE;
    }

    /***
     * 向表attends中插入数据
     * @param  callBack
     * @param cv
     */
    public void InsertAttends(ContentValues cv, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        long i = db.insert(TABLE_USERS, null, cv);
        if (i == -1) {
            callBack.onError("考勤失败，请稍后重试");
        } else {
            callBack.onRespose("success");
            Log.d(TAG, "InsertAttendStudent: success------------------>");
        }
        cv.clear();
    }

    /***
     * 查询表attend_student中的信息
     *
     * @param selectionID  查询 条件
     * @param valus 条件中用了占位符的参数
     * @param groupBy 数据分组
     * @param having 分组后的条件
     * @param orderBy 排序方式
     * @param limit 分页查询 格式："" + startIdex + "," + number + ""  numb:查询的每页数量
     * @param callBack
     * @return 返回cursor
     */
    public void selectAttends(String selectionID, String[] valus, String groupBy,
                              String having, String orderBy, String limit, SqlCallBack callBack) {
        Cursor cursor = null;
        List<BaseAttendRecord> lists = new ArrayList<>();
        String[] users = new String[]{FIELD_CARD_ID, FIELD_NAME, FIELD_SATUS, FIELD_CLASS,
                FIELD_IN_OUT_MODE, FIELD_ID, FIELD_PHONE, FIELD_ATTENDANCE_MODE, FIFLE_ATTENDANCE_DATE, FIFLE_IS_HANDLE, FIFLE_IS_STUDENT};
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
//            String selectSql = "select * from "+TABLE_USERS+" limit "+20+" offset "+10+" order by "+FIELD_ID+"" ;
            cursor = db.query(TABLE_USERS, users, selectionID, valus, groupBy, having, orderBy, limit);
//            cursor = db.rawQuery(selectSql,null);
            Log.d(TAG, "selectAttends: ============>" + limit);
//            cursor = db.query(TABLE_USERS,users,selectionID,valus,groupBy,having,orderBy,"10,20");
            Log.d(TAG, "selectAttends: ---------------------------->cursor===" + cursor.getCount());
            if (cursor == null || cursor.getCount() == 0) {
//                callBack.onError("无此用户");
                callBack.onRespose(lists);
                return;
            }

            while (cursor.moveToNext()) {
                BaseAttendRecord user = new BaseAttendRecord();
                user.setA_attendDate(cursor.getString(cursor.getColumnIndex(FIFLE_ATTENDANCE_DATE)));
                user.setA_attendMode(cursor.getInt(cursor.getColumnIndex(FIELD_ATTENDANCE_MODE)));
                user.setA_cardID(cursor.getString(cursor.getColumnIndex(FIELD_CARD_ID)));
                user.setA_class(cursor.getString(cursor.getColumnIndex(FIELD_CLASS)));
                user.setStatus(cursor.getString(cursor.getColumnIndex(FIELD_SATUS)));
                user.setA_id(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                user.setA_inOrOutMode(cursor.getInt(cursor.getColumnIndex(FIELD_IN_OUT_MODE)));
                user.setA_name(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                user.setA_phone(cursor.getString(cursor.getColumnIndex(FIELD_PHONE)));
                user.setA_isHandle(cursor.getInt(cursor.getColumnIndex(FIFLE_IS_HANDLE)));
                user.setA_isStudent(cursor.getInt(cursor.getColumnIndex(FIFLE_IS_STUDENT)));
                lists.add(user);
            }
            callBack.onRespose(lists);
            return;
        } catch (Exception e) {
            callBack.onError("考勤记录查询失败，请稍后重试");
            Log.e(TAG, "selectAttendStudent: " + e.getMessage());
            return;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /***
     * 更新表attend_student中的信息
     *
     * @param cv  contentValues
     * @param whereId 查询条件
     * @param valus 条件中用了占位符的参数
     * @param callBack
     */
    public void updataAttends(ContentValues cv, String whereId, String[] valus, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            db.update(TABLE_USERS, cv, whereId + "=?", valus);
            callBack.onRespose("success");
        } catch (Exception e) {
            callBack.onError("更新用户表信息失败，请稍后重试");
            return;
        } finally {
            if (!cv.toString().isEmpty()) {
                cv.clear();
            }
        }
    }

    /***
     * 清除表Attend_student的数据
     *
     * @param callBack
     */
    public void clearAttends(String clearSQl, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            db.execSQL(clearSQl);
            callBack.onRespose("success");
        } catch (Exception e) {
            callBack.onError("清除用户信息失败");
        } finally {
        }
    }

    public void closeDB() {
        if (db != null) {
            if (!db.isOpen()) {
                closeDatabase();
            }
        }
    }

}
