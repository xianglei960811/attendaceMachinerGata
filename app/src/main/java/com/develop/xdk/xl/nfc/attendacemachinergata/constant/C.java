package com.develop.xdk.xl.nfc.attendacemachinergata.constant;

/**
 * 默认常量值
 */

public class C {
    public static final String SIGN_KEY = "cinzn2055";
    public static final String BASE_URL = "http://www.cinzn.net:9000/";
    public static final String CLIENTID = "02801";
    public static final String MACHINE = "001";
    public static final String MACHINE_NAME = "machine";
    public static final String COMPUTER = "001";
    public static final String COMPUTER_NAME = "computer";
    public static final String CLIENTID_NAME = "clientid";
    public static final String BASE_URL_NAME = "baseurl";
    public static final String PASS = "2055";
    public static final String PASS_NAME = "PASS";
    public static final int MAC_TYPR = 0;//设备类型
    public static final String CHECK_MAC_NAME = "CHECK_MAC";
    public static final String CHECK_MAC = "考勤机1";


    //进出模式
    public static final String IN_OUT_MODE_NAME = "in_out_mode";
    public static final int IN_MODE = 1;
    public static final String IN_MODE_NAME = "进入";
    public static final int OUT_MODE = 2;
    public static final String OUT_MODE_NAME = "离开";
    //考勤模式
    public static final String ATTENDANCE_MODE_NAME = "attendance_mode";
    public static final int SCHOOL_GATA_MODE = 0;//进出校门
    public static final int DOOR_MODE = 1;//公寓
    public static final int LESSON_OR_EXAMINE_MODE = 2;//上课或者考试
    public static final int TEACHING_STAFF_MODE = 3;//教职工

    public static final String SCHOOL_GATA = "校门考勤";
    public static final String DOOR = "公寓考勤";
    public static final String LESSON_OR_EXAMINE = "上课（考试）考勤";
    public static final String TEACHING_STAFF = "教职工考勤";




    public static final int TIME_OUT =10 ;//10s超时
    //学生，教师标识
    public static final Integer IS_STUDENT =1;
    public static final Integer IS_TEACHER = 2;
    //是否处理
    public static final int IS_HANDLE = 1;//已处理
    public static final int IS_NO_HANDLE = 2;//未处理

    public static final String ERROR_FILENAME = "attendaMachingGata_ERROR.log";


}
