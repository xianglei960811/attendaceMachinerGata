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

public class BaseAttendTeacher extends BaseDB implements TableColumns.ATTENDANCES_COUMNS {
    private static final String TAG = "BaseAttendTeacher";
    private static BaseAttendTeacher INSTANCE = null;
    private Context mcontext;
    private SQLiteDatabase db;

    protected BaseAttendTeacher(Context context) {
        super(context);
        mcontext = context;
    }

    public static BaseAttendTeacher getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BaseUserStudent.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaseAttendTeacher(context);
                }
            }
        }
        return INSTANCE;
    }

    /***
     * 向表attend_teacher中插入数据
     *
     * @param cv
     * @param callBack
     */
    public void InsertUserStudent(ContentValues cv, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }

        if (db.insert(TABLE_USERS, null, cv) == -1) {
            callBack.onError("插入信息失败，请重试");
        } else {
            callBack.onRespose("success");
        }
        if (db != null || db.isOpen()) {
            closeDatabase();
        }
        cv.clear();
    }

    /***
     * 查询表attend_teacher中的信息
     *
     * @param selectionID  查询 条件
     * @param valus 条件中用了占位符的参数
     * @param groupBy 数据分组
     * @param having 分组后的条件
     * @param orderBy 排序方式
     * @param limit 分页查询 格式："" + startIdex + "," + endIndex + ""
     * @param callBack
     * @return 返回cursor
     */
    public void selectAttenTeacher(String selectionID, String[] valus, String groupBy,
                                   String having, String orderBy, String limit, SqlCallBack callBack) {
        Cursor cursor = null;
        List<BaseAttendRecord> lists = new ArrayList<>();
        String[] users = new String[]{FIELD_CARD_ID, FIELD_NAME, FIELD_SATUS, FIELD_CLASS,
                FIELD_IN_OUT_MODE, FIELD_ID, FIELD_PHONE, FIELD_ATTENDANCE_MODE, FIFLE_ATTENDANCE_DATE,FIFLE_IS_HANDLE};
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            cursor = db.query(TABLE_USERS, users, selectionID + "=?", valus, groupBy, having, orderBy, limit);
            if (cursor == null || cursor.getCount() == 0) {
//                callBack.onError("无此信息");
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
                user.setA_inOrOutMode(cursor.getColumnIndexOrThrow(FIELD_IN_OUT_MODE));
                user.setA_name(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                user.setA_phone(cursor.getString(cursor.getColumnIndex(FIELD_PHONE)));
                user.setA_isHandle(cursor.getInt(cursor.getColumnIndex(FIFLE_IS_HANDLE)));
                lists.add(user);
            }
            callBack.onRespose(lists);
            return;
        } catch (Exception e) {
            callBack.onError("查询用户信息失败");
            Log.e(TAG, "selectUser: " + e.getMessage());
            return;
        } finally {
            if (db.isOpen() || db != null) {
                closeDatabase();
                Log.d("dddd", "selectUser:db ============================ ");
            }
            if (cursor != null || cursor.getCount() != 0) {
                cursor.close();
                Log.d("dddd", "selectUser:cursor ============================ ");
            }
        }
    }

    /***
     * 更新表user_teacher中的信息
     *
     * @param cv  contentValues
     * @param whereId 查询条件
     * @param valus 条件中用了占位符的参数
     * @param callBack
     */
    public void updataUser(ContentValues cv, String whereId, String[] valus, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            db.update(TABLE_USERS, cv, whereId + "=?", valus);
            callBack.onRespose("success");
        } catch (Exception e) {
            callBack.onError("更新用户表信息失败，请稍后重试");
            Log.e(TAG, "updataUser: " + e.getMessage());
            return;
        } finally {
            if (db != null || !db.isOpen()) {
                closeDatabase();
            }
            if (!cv.toString().isEmpty()) {
                cv.clear();
            }
        }
    }


    /***
     * 清除表user_teacher的数据
     *
     * @param whereArgs  删除条件
     * @param valus    条件中用了占位符的参数
     * @param callBack
     */
    public void clearUser(String whereArgs, String[] valus, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            String clearSqlite_sequence = "delete from sqlite_sequence";//将自增归0
            db.delete(TABLE_USERS, whereArgs, valus);
            db.execSQL(clearSqlite_sequence);
            callBack.onRespose("success");
        } catch (Exception e) {
            callBack.onError("删除信息失败，请稍后重试");
            Log.e(TAG, "clearUser: " + e.getMessage());
        } finally {
            if (db != null || !db.isOpen()) {
                closeDatabase();
            }
        }
    }
}
