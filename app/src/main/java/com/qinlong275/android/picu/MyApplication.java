package com.qinlong275.android.picu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.qinlong275.android.picu.common.SingleTons;

import org.json.JSONObject;

public class MyApplication extends Application{

    private static JSONObject mUserInfo;

    public static JSONObject getUserInfo() {
        return mUserInfo;
    }

    public static void setUserInfo(JSONObject userInfo) {
        mUserInfo = userInfo;
    }

    private static SharedPreferences sharedPreferences;

    public static String getPicuKey() {
        return sharedPreferences.getString("picukey","");
    }

    public static void setPicuKey(String picuKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("picukey", picuKey);
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SingleTons.setmApplication(this);

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
    }

    //做相应的全局初始化操作
}
