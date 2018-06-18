package com.qinlong275.android.picu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.qinlong275.android.picu.R;
import com.qinlong275.android.picu.ui.adapter.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class myreceive extends BaseActivity {
    protected int now;
    Button back, setting, head, name, receive, post;
    ImageView picu;
    @BindView(R.id.myreceiveback)
    Button mMyreceiveback;
    @BindView(R.id.myreceivehead)
    ImageView mMyreceivehead;
    @BindView(R.id.myreceivesetting)
    Button mMyreceivesetting;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreceive);
        ButterKnife.bind(this);
//        now = 1;
//        setListener();

        initTablayout();
    }

    private void initTablayout() {


        PagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
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

    @OnClick({R.id.myreceiveback, R.id.myreceivesetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myreceiveback:
                finish();
                break;
            case R.id.myreceivesetting:
                btnSetting();
                break;
        }
    }
}
