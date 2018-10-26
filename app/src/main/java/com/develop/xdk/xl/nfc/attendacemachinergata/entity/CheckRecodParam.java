package com.develop.xdk.xl.nfc.attendacemachinergata.entity;
//上传parm
public class CheckRecodParam extends BaseParam{
    private String date,time,mactype,checkmac,checktype,style;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMactype() {
        return mactype;
    }

    public void setMactype(String mactype) {
        this.mactype = mactype;
    }

    public String getCheckmac() {
        return checkmac;
    }

    public void setCheckmac(String checkmac) {
        this.checkmac = checkmac;
    }

    public String getChecktype() {
        return checktype;
    }

    public void setChecktype(String checktype) {
        this.checktype = checktype;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
