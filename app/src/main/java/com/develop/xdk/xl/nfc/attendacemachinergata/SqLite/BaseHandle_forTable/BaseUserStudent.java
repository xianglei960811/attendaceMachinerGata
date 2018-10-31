package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseHandle_forTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL.BaseDB;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL.TableColumns;
import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SqlCallBack;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.Loading.timeOutListner;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.Dialog.LoadingDialog;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.LocalBaseUser;

import java.util.ArrayList;
import java.util.List;

public class BaseUserStudent extends BaseDB implements TableColumns.USER_STUDENT_COLUMNS {
    private static final String TAG = "BaseUserStudent";
    private static BaseUserStudent INSTANCE = null;
    private Context mcontext;
    private SQLiteDatabase db;


    protected BaseUserStudent(Context context) {
        super(context);
        mcontext = context;

    }

    public static BaseUserStudent getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BaseUserStudent.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaseUserStudent(context);
                }
            }
        }
        return INSTANCE;
    }

    /***
     * 向表user_student中插入数据
     *
     * @param cv
     */
    public synchronized void InsertUserStudent(ContentValues cv, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        if (db.insert(TABLE_USERS, null, cv) == -1) {
            callBack.onError("插入用户表失败，请重试");
        } else {
            callBack.onRespose("success");
        }
        cv.clear();
    }

    /***
     * 查询表user_student中的信息
     *
     * @param selectionID  查询 条件
     * @param valus 条件中用了占位符的参数
     * @param groupBy 数据分组
     * @param having 分组后的条件
     * @param orderBy 排序方式
     * @param limit 分页查询 格式："" + startIdex + "," + endIndex + ""
     * @return 返回cursor
     */
    public synchronized void selectUser(String selectionID, String[] valus, String groupBy, String having, String orderBy, String limit, SqlCallBack callBack) {
        Cursor cursor = null;
        List<LocalBaseUser> lists = new ArrayList<>();
        String[] users = new String[]{FIELD_CARD_ID, FIELD_NAME, FIELD__HEAD_IMAGE, FIELD_CLASS,
                 FIELD_ID, FIELD_PHONE, FIELD_STATUS, FIELD_SEX};
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            cursor = db.query(TABLE_USERS, users, selectionID + "=?", valus, groupBy, having, orderBy, limit);
            if (cursor == null || cursor.getCount() == 0) {
//                ToastUntil.ShowToastShort(context,"此用户不存在");
                callBack.onError("此用户不存在");
                return;
            }
            LocalBaseUser user = new LocalBaseUser();
            while (cursor.moveToNext()) {
                user.setU_Name(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                user.setU_CardID(cursor.getString(cursor.getColumnIndex(FIELD_CARD_ID)));
                user.setU_Class(cursor.getString(cursor.getColumnIndex(FIELD_CLASS)));
                user.setU_HeadImage(cursor.getBlob(cursor.getColumnIndex(FIELD__HEAD_IMAGE)));
                user.setU_id(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
                user.setU_Phone(cursor.getString(cursor.getColumnIndex(FIELD_PHONE)));
                user.setU_Sex(cursor.getString(cursor.getColumnIndex(FIELD_SEX)));
                user.setU_Status(cursor.getString(cursor.getColumnIndex(FIELD_STATUS)));
                lists.add(user);
            }
            callBack.onRespose(lists);
            return;
        } catch (Exception e) {
//            toastUntil.ShowToastShort( e.getMessage());
            callBack.onError(e.getMessage());
            return;
        } finally {
            if (cursor != null ) {
                cursor.close();
                Log.d("dddd", "selectUser:cursor ============================ ");
            }
        }
    }

    /***
     * 更新表user_student中的信息
     *
     * @param cv  contentValues
     * @param whereId 查询条件
     * @param valus 条件中用了占位符的参数
     * @param  callBack
     */
    public synchronized void updataUser(ContentValues cv, String whereId, String[] valus, SqlCallBack callBack) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        try {
            db.update(TABLE_USERS, cv, whereId + "=?", valus);
            callBack.onRespose("success");
        } catch (Exception e) {
            callBack.onError("更新用户表信息失败，请稍后重试重试" + e.getMessage());
            Log.e(TAG, "updataUser: " + e.getMessage());
            return;
        } finally {
            if (!cv.toString().isEmpty()) {
                cv.clear();
            }
        }
    }

    /***
     * 清除表user_student的数据
     *
     * @param whereArgs  删除条件
     * @param valus    条件中用了占位符的参数
     * @param  callBack
     */
    public synchronized void clearUser(String whereArgs, String[] valus, SqlCallBack callBack) {
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
        }
    }

    public void closeDb(){
        if (db!=null){
            if (!db.isOpen()){
                closeDatabase();
            }
        }
    }
}
