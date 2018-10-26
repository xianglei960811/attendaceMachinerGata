package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class MySqliteHelp extends SQLiteOpenHelper implements TableColumns{
    private static  final String DB_NAME = "OneCard.db";
    private static final  int DB_VERSION = 1;
//学生用户表
    public static final String CREAT_TABLE_USERS_STUDENT = "CREATE TABLE IF NOT EXISTS "+ USER_STUDENT_COLUMNS.TABLE_USERS
            +"("
            +USER_STUDENT_COLUMNS.FIELD_ID+" integer not NULL primary key autoincrement,"
            +USER_STUDENT_COLUMNS.FIELD_NAME+" vachar(200),"
            +USER_STUDENT_COLUMNS.FIELD_CARD_ID+" vachar(200) not NULl,"
            +USER_STUDENT_COLUMNS.FIELD_SEX+" vachar(200),"
            +USER_STUDENT_COLUMNS.FIELD__HEAD_IMAGE+" BLOB,"
            +USER_STUDENT_COLUMNS.FIELD_CLASS+" vachar(200),"
            +USER_STUDENT_COLUMNS.FIELD_PHONE+" vachar(200),"
            +USER_STUDENT_COLUMNS.FIELD_STATUS+" vachar(200)"
            +")";
    //考勤表
    public static final String CREAT_TABLE_ATTEND_STUDENT = "CREATE TABLE IF NOT EXISTS "+ATTENDANCES_COUMNS.TABLE_USERS
            +"("
            +ATTENDANCES_COUMNS.FIELD_ID+" integer not null primary key autoincrement,"
            +ATTENDANCES_COUMNS.FIELD_CARD_ID+" vachar(200) not null,"
            +ATTENDANCES_COUMNS.FIELD_NAME+" vachar(200),"
            +ATTENDANCES_COUMNS.FIELD_HEAD_IMAGE+" BLOB,"
            +ATTENDANCES_COUMNS.FIELD_CLASS+" vachar(200),"
            +ATTENDANCES_COUMNS.FIELD_PHONE+" vachar(200),"
            +ATTENDANCES_COUMNS.FIELD_ATTENDANCE_MODE+" int,"
            +ATTENDANCES_COUMNS.FIELD_IN_OUT_MODE+" int,"
            +ATTENDANCES_COUMNS.FIFLE_ATTENDANCE_DATE+" vachar(200),"
            +ATTENDANCES_COUMNS.FIFLE_IS_HANDLE + " int,"
            +ATTENDANCES_COUMNS.FIFLE_IS_STUDENT+ " int"
            +")";
    public MySqliteHelp(Context context) {
        this(context,DB_NAME,null,DB_VERSION);
    }

    public MySqliteHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
//        //TODO 必须预先加载，不然会报错
//        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String creatUserSql = "create table users (u_id integer not NULL primary key autoincrement,u_cardID vachar(200) not NULl ," +
//                "u_accontID vachar(200),u_name varchar(200),u_cashMoney double,u_subsidyMoney double,u_isloss int,u_consumeData vachar(200))";
//
//        String creatConsumeSql = "create table consumeInfoes(c_id integer not null primary key autoincrement,c_cardID vachar(200) not null," +
//                "c_name vachar(200),c_cashMoney double,c_subsidyMoney double,c_consumeMoney double,c_data vachar(200),c_isHandle vachar(200),c_isOnline int)";
        Log.d(TAG, "Create database------- ");
        db.execSQL(CREAT_TABLE_USERS_STUDENT);
//        db.execSQL(CREAT_TABLE_USERS_TEACHER);
        db.execSQL(CREAT_TABLE_ATTEND_STUDENT);
//        db.execSQL(CREAT_TABLE_ATTEND_TEACHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "newVersion:"+newVersion);
    }


}
