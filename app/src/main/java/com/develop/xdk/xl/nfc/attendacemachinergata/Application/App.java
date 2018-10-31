package com.develop.xdk.xl.nfc.attendacemachinergata.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.develop.xdk.xl.nfc.attendacemachinergata.SqLite.SQLControl;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private static App instance;
    private List<Activity> allActivities;
    private static Context context;


    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context =getApplicationContext();
        instance = this;
    }
    public  static Context getContext(){
        return context;
    }


    /**
     * 添加activity
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new ArrayList<>();
        }
        allActivities.add(act);
    }

    /**
     * 移除activity
     */
    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
            act.finish();
        }
    }
    /**
     * 退出app
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        SQLControl.getINSTANCE().closeDB(this);
    }
}