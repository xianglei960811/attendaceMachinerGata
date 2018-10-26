package com.develop.xdk.xl.nfc.attendacemachinergata.NFC;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.util.Log;

import com.develop.xdk.xl.nfc.attendacemachinergata.utils.ToastUntil;

import java.io.IOException;

public class getNfcCardID {
    //    private volatile static getNfcCardID INSTANCE;
    private Context context;
    private Activity activity;
    private volatile NfcAdapter madapter;
    private PendingIntent mPengdingIntent;
    private IntentFilter[] mFilters;
    private Intent intent;
    private String[][] mTechLists;
    private boolean is_NEW = true;

    private ToastUntil toastUntil;

    /***
     * 获取单例，并保证其多线程时同步
     *
     * @param context
     * @return
     */

//    public static getNfcCardID getINSTANCE(Context context) {
//        getNfcCardID ins = null;
//        if (ins == null) {
//            synchronized (getNfcCardID.class) {
//                ins = INSTANCE;
//                if (ins == null) {
//                    ins = new getNfcCardID(context);
//                    INSTANCE = ins;
//                }
//            }
//        }
//        return ins;
//    }
    public getNfcCardID(Context context) {
        this.context = context;
        activity = (Activity) context;
        toastUntil = new ToastUntil(context);
        initNFC();
    }

    /**
     * 初始化nfc扫描器
     */
    private void initNFC() {
        madapter = NfcAdapter.getDefaultAdapter(context);
        if (madapter == null) {
            toastUntil.ShowToastShort("本设备不支持NFC");
            Log.e("NFc", "initNFC: 本设备不支持NFC");
            return;
        }
        if (!madapter.isEnabled()) {
            toastUntil.ShowToastShort("请打开NFC系统开关");
            Log.e("NFC", "initNFC: 请打开NFC系统开关");
            return;
        }
        mPengdingIntent = PendingIntent.getActivity
                (context, 0, new Intent(context, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        intentFilter.addCategory("*/*");
        mFilters = new IntentFilter[]{intentFilter};//过滤器
        mTechLists = new String[][]{
                new String[]{MifareClassic.class.getName()},
                new String[]{NfcA.class.getName()}//允许扫描的类型
        };
        Log.d("NFC", "initNFC: ------------------------------->");
    }

    /**
     * 检测是否有ACTION_TECH_DISCOVERED触发
     * 需在activity中的OnResume中运行
     */
    public String NfcCardID_OnResume() {
        String NfcCardID = null;
        if (madapter != null && mPengdingIntent != null && mFilters != null && mTechLists != null) {
            madapter.enableForegroundDispatch(activity, mPengdingIntent, mFilters, mTechLists);
            if (is_NEW) {
                if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(activity.getIntent().getAction())) {
                    //处理该intent
                    NfcCardID = processIntent(activity.getIntent());
                    intent = activity.getIntent();
                    is_NEW = false;
//                    toastUntil.ShowToastShort("sss"+NfcCardID);
//                    NfcCardID = "D10CEFC8";
                }
            }
        }

        return NfcCardID;
    }

    public String NfcCardID_OnNewIntent(Intent intent) {
        String cardid = null;
        if (madapter != null && mPengdingIntent != null && mFilters != null && mTechLists != null) {
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
                cardid = processIntent(intent);
                this.intent = intent;
//                toastUntil.ShowToastShort(context.getClass().getSimpleName()+cardid);
            }
        }
        return cardid;
    }

    /**
     * 读取NFC数据，并从中取出卡号
     *
     * @param intent
     * @return
     */
    private String processIntent(Intent intent) {
        String cardId = null;
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mfc = MifareClassic.get(tag);
        try {
            mfc.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] MyNfc = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        cardId = bytesToHexString2(MyNfc);
        cardId = cardId.replace(" ", "").toUpperCase();
        Log.d("NFC", "processIntent: "+cardId);

        if (mfc != null) {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cardId;
    }

    /***
     * 字符序列转换为16进制字符串
     * @param src
     * @return
     */
    private String bytesToHexString2(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
