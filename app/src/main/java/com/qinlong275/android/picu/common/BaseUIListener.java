package com.qinlong275.android.picu.common;

/**
 * Created by 或跃在渊i on 2018/6/12.
 */


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.qinlong275.android.picu.common.util.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class BaseUIListener implements IUiListener {
    private Context mContext;
    private String mScope;

    public BaseUIListener(Context context) {
        mContext = context;
    }


    public BaseUIListener(Context mContext, String mScope) {
        super();
        Util.dismissDialog();
        this.mContext = mContext;
        this.mScope = mScope;
    }

    @Override
    public void onComplete(Object response) {
        if (null == response) {
            Util.showResultDialog(mContext, "返回为空", "登录失败");
            return;
        }
        JSONObject jsonResponse = (JSONObject) response;
        if (null != jsonResponse && jsonResponse.length() == 0) {
            Util.showResultDialog(mContext, "返回为空", "登录失败");
            return;
        }
        doComplete((JSONObject) response);
    }

    protected void doComplete(JSONObject values) {

    }

    @Override
    public void onError(UiError e) {
        Util.toastMessage((Activity) mContext, "onError: " + e.errorDetail);
        Util.dismissDialog();
    }

    @Override
    public void onCancel() {
        Util.toastMessage((Activity) mContext, "onCancel: ");
        Util.dismissDialog();
    }

}
