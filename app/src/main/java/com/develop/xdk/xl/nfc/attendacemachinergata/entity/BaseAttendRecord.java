package com.develop.xdk.xl.nfc.attendacemachinergata.entity;


public class BaseAttendRecord {
    int A_id;
    String A_cardID;
    String A_name;
    String status;
    String A_class;
    String A_phone;
    int A_attendMode;
    int A_inOrOutMode;
    String A_attendDate;
    int A_isHandle;//是否处理：1,是；2，否
    int A_isStudent;//是否为学生，1是；2不是

    public int getA_isStudent() {
        return A_isStudent;
    }

    public void setA_isStudent(int a_isStudent) {
        A_isStudent = a_isStudent;
    }

    public int getA_isHandle() {
        return A_isHandle;
    }

    public void setA_isHandle(int a_isHandle) {
        A_isHandle = a_isHandle;
    }

    public int getA_id() {
        return A_id;
    }

    public void setA_id(int a_id) {
        A_id = a_id;
    }

    public String getA_cardID() {
        return A_cardID;
    }

    public void setA_cardID(String a_cardID) {
        A_cardID = a_cardID;
    }

    public String getA_name() {
        return A_name;
    }

    public void setA_name(String a_name) {
        A_name = a_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getA_class() {
        return A_class;
    }

    public void setA_class(String a_class) {
        A_class = a_class;
    }

    public String getA_phone() {
        return A_phone;
    }

    public void setA_phone(String a_phone) {
        A_phone = a_phone;
    }

    public int getA_attendMode() {
        return A_attendMode;
    }

    public void setA_attendMode(int a_attendMode) {
        A_attendMode = a_attendMode;
    }

    public int getA_inOrOutMode() {
        return A_inOrOutMode;
    }

    public void setA_inOrOutMode(int a_inOrOutMode) {
        A_inOrOutMode = a_inOrOutMode;
    }

    public String getA_attendDate() {
        return A_attendDate;
    }

    public void setA_attendDate(String a_attendDate) {
        A_attendDate = a_attendDate;
    }
}
