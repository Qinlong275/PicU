package com.qinlong275.android.picu.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.common.BaseUIListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;


public class SettingActivity extends AppCompatActivity {

    protected Button back,feedback,useragreement,logout;
    private Tencent mTencent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        setListener();
    }
    protected void setListener(){
        back= (Button) findViewById(R.id.settingback) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack();
            }
        });
        feedback= (Button) findViewById(R.id.settingfeedback) ;
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFeedback();
            }
        });
        useragreement= (Button) findViewById(R.id.settingusersagreement) ;
        useragreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUseragreement();
            }
        });
        logout= (Button) findViewById(R.id.settinglogout) ;
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                btnLogin();
                //退出登录跳转到登陆页面
                btnLogout();
            }
        });
    }

    protected void btnFeedback() {
        Intent intent=new Intent(SettingActivity.this,FeedbackActivity.class);
        startActivity(intent);
    }
    protected void btnUseragreement(){
        Intent intent=new Intent(SettingActivity.this,useragreement.class);
        startActivity(intent);
    }
    protected void btnBack(){
        finish();
    }
    protected void btnLogin(){
        mTencent=Tencent.createInstance("1106963042", SettingActivity.this);
        IUiListener listener = new BaseUIListener(SettingActivity.this) {
            @Override
            public void onComplete(Object response) {
                sign();
            }
        };
        mTencent.login(SettingActivity.this,"get_simple_userinfo", listener);

    }
    protected void sign(){
        Toast.makeText(SettingActivity.this, "登录成功", Toast.LENGTH_LONG).show();
    }
    protected void btnLogout(){
        mTencent.logout(this);
    }
}
