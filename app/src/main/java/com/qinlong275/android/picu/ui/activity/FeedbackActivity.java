package com.qinlong275.android.picu.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qinlong275.android.picu.R;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class FeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        Button back= (Button) findViewById(R.id.feedbackback) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submit= (Button) findViewById(R.id.feedbacksubmit) ;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit();
            }
        });
    }
    protected void btnShare(){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, 10001);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "https://p.qpic.cn/qqconnect_app_logo/EMEYLRrVo2nrFE3RgjH8y03kqaNYibPsFpN9K5uURkvQ/0?684.0727270465732.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "PicU");
        Tencent mTencent=Tencent.createInstance("1106963042", this);
        mTencent.shareToQQ(FeedbackActivity.this, params, new IUiListener(){

            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });


    }

    private void btnSubmit(){
        //调用服务端接口，反馈信息
    }
}
