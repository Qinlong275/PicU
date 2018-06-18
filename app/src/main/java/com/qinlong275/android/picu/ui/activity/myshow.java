package com.qinlong275.android.picu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qinlong275.android.picu.R;

/**
 * Created by 或跃在渊i on 2018/6/13.
 */

public class myshow extends BaseActivity{

    private String secretType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myshow);
        Intent mIntent=getIntent();
        secretType=mIntent.getStringExtra("secretType");

        Button back= (Button) findViewById(R.id.myshowback) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
