package com.qinlong275.android.picu.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qinlong275.android.picu.R;

/**
 * Created by 或跃在渊i on 2018/6/13.
 */

public class othershow extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.othershow);
        Button back= (Button) findViewById(R.id.othershowback) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
