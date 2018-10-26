package com.develop.xdk.xl.nfc.attendacemachinergata.entity;

import android.graphics.drawable.Drawable;

import java.sql.Blob;

/***
 *
 * 本地用户信息的基础类
 */
public class LocalBaseUser {
    String U_Name;
    String U_Sex;
    String U_Class;
    String U_Phone;
    byte[] U_HeadImage;//图片二进制
    String U_CardID;
    String u_Status;//身份
    int U_id;

    public String getU_Status() {
        return u_Status;
    }

    public void setU_Status(String u_Status) {
        this.u_Status = u_Status;
    }

    public String getU_CardID() {
        return U_CardID;
    }

    public void setU_CardID(String u_CardID) {
        U_CardID = u_CardID;
    }

    public int getU_id() {
        return U_id;
    }

    public void setU_id(int u_id) {
        U_id = u_id;
    }

    public String getU_Name() {
        return U_Name;
    }

    public void setU_Name(String u_Name) {
        U_Name = u_Name;
    }

    public String getU_Sex() {
        return U_Sex;
    }

    public void setU_Sex(String u_Sex) {
        U_Sex = u_Sex;
    }

    public String getU_Class() {
        return U_Class;
    }

    public void setU_Class(String u_Class) {
        U_Class = u_Class;
    }


    public String getU_Phone() {
        return U_Phone;
    }

    public void setU_Phone(String u_Phone) {
        U_Phone = u_Phone;
    }

    public byte[] getU_HeadImage() {
        return U_HeadImage;
    }

    public void setU_HeadImage(byte[] u_HeadImage) {
        U_HeadImage = u_HeadImage;
    }
}
