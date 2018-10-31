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

    public static interface ATTENDANCES_COUMNS{
        public static final String TABLE_USERS = "attendances";
        public static final String FIELD_ID = "Atten_id";//字段名
        public static final String FIELD_NAME = "Atten_name";
        public static final String FIELD_CARD_ID ="Atten_cardID";
        public static final String FIELD_SATUS = "Atten_status";
        public static final String FIELD_CLASS = "Atten_class";
        public static final String FIELD_PHONE = "Atten_phone";
        public static final String FIELD_IN_OUT_MODE = "Atten_in_or_out_mode";
        public static final String FIELD_ATTENDANCE_MODE = "Atten_attend_mode";
        public static final String FIFLE_ATTENDANCE_DATE = "Atten_attend_date";
        public static final String FIFLE_IS_HANDLE = "Atten_is_handle";
        public static final String FIFLE_IS_STUDENT  = "Atten_is_student";
    }
}
