package com.qinlong275.android.picu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.qinlong275.android.picu.common.BaseUIListener;
import com.qinlong275.android.picu.ui.activity.SettingActivity;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private Tencent mTencent;

    @BindView(R.id.login)
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        //登录接口调用
//        btnLogin();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    protected void btnLogin(){
        mTencent=Tencent.createInstance("1106963042", LoginActivity.this);
        IUiListener listener = new BaseUIListener(LoginActivity.this) {
            @Override
            public void onComplete(Object response) {
                sign();
            }
        };
        mTencent.login(LoginActivity.this,"get_simple_userinfo", listener);

    }
    protected void sign(){
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
        //跳转到主操作页面
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
