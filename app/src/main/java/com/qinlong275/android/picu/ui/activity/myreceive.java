package com.qinlong275.android.picu.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.qinlong275.android.picu.R;

public class myreceive extends AppCompatActivity {
    protected int now;
    Button back, setting, head, name, receive, post;
    ImageView picu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreceive);
        now = 1;
        setListener();
    }

//    protected void receiveInit() {
//        now = 1;
//        AnimationSet animationSet = new AnimationSet(true);
//        final ImageView line=(ImageView) findViewById(R.id.myreceiveline) ;
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,-220, 0, 0);
//        translateAnimation.setDuration(500);// 设置移动速度
//        animationSet.setFillEnabled(true);
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                line.layout(line.getLeft()-220, line.getTop(), line.getRight()-220, line.getBottom());
//            }
//        });
//        line.startAnimation(animationSet);
//    }

    //250,220,100
//    protected void postInit() {
//        now = 2;
//        AnimationSet animationSet = new AnimationSet(true);
//        final ImageView line = (ImageView) findViewById(R.id.myreceiveline);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0, line.getRight() - line.getLeft(), 0, 0);
//        translateAnimation.setDuration(500);// 设置移动速度
//        animationSet.setFillEnabled(true);
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                line.layout(line.getRight(), line.getTop(), line.getRight() * 2 - line.getLeft(), line.getBottom());
//            }
//        });
//        line.startAnimation(animationSet);
//    }

    protected void setListener() {
        setting = (Button) findViewById(R.id.myreceivesetting);
        setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSetting();
            }
        });
        back = (Button) findViewById(R.id.myreceiveback);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
            }
        });
//        receive = (Button) findViewById(R.id.myreceivereceive);
//        receive.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        post = (Button) findViewById(R.id.myreceivepost);
//        post.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (1 == now) postInit();
//            }
//        });
//        picu = (ImageView) findViewById(R.id.myreceivephoto);
//        picu.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnPicU();
//            }
//        });
    }

    protected void btnPicU() {
        if (1 == now) {
            Intent intent = new Intent(myreceive.this, othershow.class);
            startActivity(intent);
        }
        if (2 == now) {
            Intent intent = new Intent(myreceive.this, myshow.class);
            startActivity(intent);
        }
    }

    protected void btnSetting() {
        Intent intent = new Intent(myreceive.this, SettingActivity.class);
        startActivity(intent);
    }
}
