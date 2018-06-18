package com.qinlong275.android.picu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.qinlong275.android.picu.MyApplication;
import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.common.BaseUIListener;
import com.qinlong275.android.picu.common.util.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private Tencent mTencent;
    private String mAppId;
    private UserInfo mInfo;

    @BindView(R.id.login)
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        if (TextUtils.isEmpty(mAppId)) {
            mAppId = "1106963042";
            if (mTencent == null) {
                mTencent = Tencent.createInstance(mAppId, this);
            }
        }
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        //登录接口调用
//        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
        Util.showProgressDialog(LoginActivity.this,"","");
        btnLogin();
    }

    IUiListener loginListener = new BaseUIListener(LoginActivity.this) {
        @Override
        protected void doComplete(JSONObject values) {
            //获取到JSONObject 用户信息对象，保存下来
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);

            updateUserInfo();
        }
    };

    private void updateUserInfo(){
        Util.showProgressDialog(this, null, null);
        mInfo = new UserInfo(this, mTencent.getQQToken());
        mInfo.getUserInfo(new BaseUIListener(this,"get_simple_userinfo"){
            @Override
            protected void doComplete(JSONObject values) {
                //获取到JSONObject用户信息对象
                MyApplication.setUserInfo(values);
                Util.dismissDialog();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("setting", "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void btnLogin(){
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        }else {
            //提示您已经登录
            Toast.makeText(LoginActivity.this, "您已经登录过了", Toast.LENGTH_LONG).show();
        }

    }
}
