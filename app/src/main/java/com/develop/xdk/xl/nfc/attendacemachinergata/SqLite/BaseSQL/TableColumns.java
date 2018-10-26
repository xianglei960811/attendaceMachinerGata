package com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.BaseSQL;



/**
 *
 * 数据库表，字段（方便统一管理）
 */
public interface TableColumns {
    public static interface USER_STUDENT_COLUMNS{
        public static final String TABLE_USERS = "attendance_user_student";
        public static final String FIELD_ID = "US_id";//字段名
        public static final String FIELD_CARD_ID ="US_cardID";
        public static final String FIELD_NAME = "US_name";
        public static final String FIELD__HEAD_IMAGE = "US_head_image";
        public static final String FIELD_SEX = "US_sex";
        public static final String FIELD_CLASS = "US_class";
        public static final String FIELD_PHONE = "US_phone";
        public static final String FIELD_STATUS ="US_quarter";
    }

//    public static interface USER_TEACHING_COLUMNS{
//        public static final String TABLE_USERS = "attendance_user_teaching";
//        public static final String FIELD_ID = "UT_id";//字段名
//        public static final String FIELD_CARD_ID ="UT_cardID";
//        public static final String FIELD_NAME = "UT_name";
//        public static final String FIELD__HEAD_IMAGE = "UT_head_image";
//        public static final String FIELD_SEX = "UT_sex";
//        public static final String FIELD_CLASS = "UT_class";
//        public static final String FIELD_JOB = "UT_job";
//        public static final String FIELD_PHONE = "UT_phone";
//        public static final String FIELD_QUARTER ="UT_quarter";
//    }
    public static interface ATTENDANCES_COUMNS{
        public static final String TABLE_USERS = "attendances";
        public static final String FIELD_ID = "Atten_id";//字段名
        public static final String FIELD_NAME = "Atten_name";
        public static final String FIELD_CARD_ID ="Atten_cardID";
        public static final String FIELD_HEAD_IMAGE = "Atten_image";
        public static final String FIELD_CLASS = "Atten_class";
        public static final String FIELD_PHONE = "Atten_phone";
        public static final String FIELD_IN_OUT_MODE = "Atten_in_or_out_mode";
        public static final String FIELD_ATTENDANCE_MODE = "Atten_attend_mode";
        public static final String FIFLE_ATTENDANCE_DATE = "Atten_attend_date";
        public static final String FIFLE_IS_HANDLE = "Atten_is_handle";
        public static final String FIFLE_IS_STUDENT  = "Atten_is_student";
    }
//    public static interface ATTENDANCE_TEACHER_COUMNS{
//        public static final String TABLE_USERS = "attendance_attend_teacher";
//        public static final String FIELD_ID = "AT_id";//字段名
//        public static final String FIELD_NAME = "AT_name";
//        public static final String FIELD_CARD_ID ="AT_cardID";
//        public static final String FIELD_HEAD_IMAGE = "AT_image";
//        public static final String FIELD_CLASS = "AT_class";
//        public static final String FIELD_PHONE = "AT_phone";
//        public static final String FIELD_IN_OUT_MODE = "AT_in_or_out_mode";
//        public static final String FIELD_ATTENDANCE_MODE = "AT_attend_mode";
//        public static final String FIFLE_ATTENDANCE_DATE = "AT_attend_date";
//        public static final String FIFLE_IS_HANDLE = "AT_is_handle";
//    }
}
